package com.io.sdchain.net;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.base.BaseApplication;
import com.io.sdchain.bean.ResponseBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.common.SharedPref;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ResponseCallBack<T> {
    public Response.ErrorListener errorListener;
    public Response.Listener<ResponseBean> listener;
    private XyDialog2 dialog;

    public ResponseCallBack(final Context context) {
        listener = new Response.Listener<ResponseBean>() {
            @Override
            public void onResponse(ResponseBean response) {
                if (dialog != null) {
                    dialog.dismiss();
                }

                if (response.isNull()) {
                    Toast.makeText(context, context.getString(R.string.info189), Toast.LENGTH_SHORT).show();
                }

                if (response.isSuccess()) {
                    if (response.getData() == null) {
                        if (!TextUtils.isEmpty(response.getMsg())) {
                            Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        if (toNext((Activity) context)) {
                            onNoData("");
                        }
                        return;
                    }
                    try {
                        if (toNext((Activity) context)) {
                            onSuccessResponse((T) new Gson().fromJson(response.getData(), getGenericType(0)), response.getMsg());
                        }
                    } catch (Exception e) {
                        Logger.d(e.toString());
                    }
                } else if (response.isOutToken()) {
                    Toast.makeText(context, context.getString(R.string.info191), Toast.LENGTH_LONG).show();
                    if (toNext((Activity) context)) {
                        onDissToken(context);
                    }
                } else {
                    if (toNext((Activity) context)) {
                        onFailResponse(response.getMsg());
                    }

                }
            }
        };
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (toNext((Activity) context)) {
                    onFailResponse(VolleyErrorHelper.getMessage(error, context));
                }
            }
        };
    }

    public abstract void onSuccessResponse(T d, String msg);

    public void onNoData(String msg) {
    }

    private void onDissToken(Context context) {
        SharedPref sharedPref = new SharedPref(context);
        sharedPref.saveData(Constants.LOGINOUT, true);

        ARouter.getInstance()
                .build(ARouterPath.LoginActivity)
                .navigation(context, new NavCallback() {
                    @Override
                    public void onArrival(Postcard postcard) {
                        ((BaseApplication) (((BaseActivity) context).getApplication())).clearStackActivity();
                    }
                });
    }


    public abstract void onFailResponse(String msg);

    public Type getGenericType(int index) {
        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return genType;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index outof bounds");
        }

        Log.e("ccer", "back type----------" + params[0]);
        return params[index];
    }


    public void setDialog(XyDialog2 dialog) {
        this.dialog = dialog;
    }


    private boolean toNext(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

}
