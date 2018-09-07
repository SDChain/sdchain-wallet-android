package com.io.sdchain.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.io.sdchain.R;
import com.io.sdchain.bean.ResponseBean;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ResponseCallBackForService<T> {
    public Response.ErrorListener errorListener;
    public Response.Listener<ResponseBean> listener;
    private XyDialog2 dialog;

    public ResponseCallBackForService(final Context context) {
        listener = new Response.Listener<ResponseBean>() {
            @Override
            public void onResponse(ResponseBean response) {
                if (dialog != null) {
                    dialog.dismiss();
                }

                if (response.isNull()) {
                    Logger.d(context.getString(R.string.info189));
                }

                if (response.isSuccess()) {
                    if (response.getData() == null) {
                        if (!TextUtils.isEmpty(response.getMsg())) {
                            Logger.d(response.getMsg());
                        }
                        onNoData("");
                        return;
                    }
                    onSuccessResponse((T) new Gson().fromJson(response.getData(), getGenericType(0)), response.getMsg());
                } else if (response.isOutToken()) {
                    onDissToken(context);
                } else {
                    onFailResponse(response.getMsg());
                }
            }
        };
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                onFailResponse(VolleyErrorHelper.getMessage(error, context));
            }
        };
    }

    public abstract void onSuccessResponse(T d, String msg);

    public void onNoData(String msg) {
    }

    private void onDissToken(Context context) {
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
}
