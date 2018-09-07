package com.io.sdchain.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.io.sdchain.R;
import com.io.sdchain.common.Constants;
import com.io.sdchain.common.SharedPref;
import com.io.sdchain.net.FileParams;
import com.io.sdchain.net.FileRequest;
import com.io.sdchain.net.GsonRequest;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.utils.LanguageUtils;
import com.io.sdchain.utils.LogInfo;
import com.io.sdchain.utils.PermissionUtils;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;
import com.yanzhenjie.permission.Permission;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xiey on 2017/9/21.
 */

public abstract class BasePresenter<V extends BaseView> {
    private V v;

    public BasePresenter(V v) {
        this.v = v;
    }

    public abstract void closeDisposable();

    public V getView() {
        return v;
    }

    public void showToast(String info) {
        if (!TextUtils.isEmpty(info)) {
            Toast.makeText(BaseApplication.getContext(), info, Toast.LENGTH_SHORT).show();
        }
    }

    public String getData(String url, HashMap<String, String> params, boolean isVerify, ResponseCallBack callBack, String mTitle, String mContent, boolean isShowDialog, Context mContext) {
        //show dialog ?
        if (isShowDialog) {
            setDialogCall(callBack, mTitle, mContent, mContext);
        }
        params = params != null ? params : new HashMap<String, String>();
        params.put("t", System.currentTimeMillis() + "");
        params.put("version", getVersion() + "");
//        params.put("uuid", getMyUUID());
//        params.put("machineId", getMyUUID());

        SharedPref sharedPref = new SharedPref(BaseApplication.getContext());
        String apptoken = (String) sharedPref.getData(Constants.TOKWN);
        if (apptoken == null) {
            apptoken = "";
        }
        params.put("apptoken", apptoken);
        params.put("languageType", LanguageUtils.getCountry(mContext));
        //change to params style
        final String api_url = url + "?" + getParamsToUrl(params);
        LogInfo.e("CallBack url:" + api_url);
        GsonRequest gsonRequest = new GsonRequest(api_url, callBack);

        //add request to queue
        BaseApplication.getApplicationInstance().addToRequestQueue(gsonRequest);
        return api_url;
    }

    public String getDataWithUUID(String url, HashMap<String, String> params, boolean isVerify, ResponseCallBack callBack, String mTitle, String mContent, boolean isShowDialog, Context mContext) {
        //is show dialog ?
        if (isShowDialog) {
            setDialogCall(callBack, mTitle, mContent, mContext);
        }
        params = params != null ? params : new HashMap<String, String>();
        params.put("t", System.currentTimeMillis() + "");
        params.put("version", getVersion() + "");
//        params.put("uuid", getMyUUID());
        params.put("machineId", getRightUUID());

        SharedPref sharedPref = new SharedPref(BaseApplication.getContext());
        String apptoken = (String) sharedPref.getData(Constants.TOKWN);
        if (apptoken == null) {
            apptoken = "";
        }
        params.put("apptoken", apptoken);
        params.put("languageType", LanguageUtils.getCountry(mContext));
        //change to params style
        final String api_url = url + "?" + getParamsToUrl(params);
        LogInfo.e("CallBack url:" + api_url);
        GsonRequest gsonRequest = new GsonRequest(api_url, callBack);

        //add request to queue
        BaseApplication.getApplicationInstance().addToRequestQueue(gsonRequest);
        return api_url;
    }

    //post
    public void postData(String url, HashMap<String, String> params, boolean isVerify, ResponseCallBack callBack, String mTitle, String mContent, boolean isShowDialog, Context mContext) {
        if (isShowDialog) {
            setDialogCall(callBack, mTitle, mContent, mContext);
        }
        params = params != null ? params : new HashMap<String, String>();
        params.put("t", System.currentTimeMillis() + "");
        params.put("version", getVersion() + "");
//        params.put("uuid", getMyUUID());
//        params.put("machineId", getMyUUID());

        SharedPref sharedPref = new SharedPref(BaseApplication.getContext());

        String apptoken = (String) sharedPref.getData(Constants.TOKWN);
        if (apptoken == null) {
            apptoken = "";
        }
        params.put("apptoken", apptoken);
        params.put("languageType", LanguageUtils.getCountry(mContext));
        LogInfo.e("CallBack url:" + url);
        LogInfo.e("params url:" + params);
        GsonRequest gsonRequest = new GsonRequest(url, params, callBack);
        HashMap<String, String> mHeaders = new HashMap<String, String>();
        //set header
        gsonRequest.setHeaders(mHeaders);
        BaseApplication.getApplicationInstance().addToRequestQueue(gsonRequest);
    }

    /**
     * post file and data
     */
    public void postFileData(String url, FileParams params, boolean isVerify, ResponseCallBack callBack, String mTitle, String mContent, boolean isShowDialog, Context mContext) {
        if (isShowDialog) {
            setDialogCall(callBack, mTitle, mContent, mContext);
        }

        params = params != null ? params : new FileParams();

        SharedPref sharedPref = new SharedPref(BaseApplication.getContext());
        String apptoken = (String) sharedPref.getData(Constants.TOKWN);
        if (apptoken == null) {
            apptoken = "";
        }
        params.put("apptoken", apptoken);
        params.put("languageType", LanguageUtils.getCountry(mContext));
//        params.put("uuid", getMyUUID());
//        params.put("machineId", getMyUUID());
        FileRequest gsonRequest = new FileRequest(url, params, callBack);
        //set timeout=20 ,maxRetry=1
        gsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        BaseApplication.getApplicationInstance().addToRequestQueue(gsonRequest);
        ;
    }

    /**
     * 获取版本号
     */
    public int getVersion() {
        try {
            PackageManager manager = BaseApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(BaseApplication.getContext().getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            LogInfo.e(e.getMessage());
            return 1;
        }
    }

    private String getRightUUID() {
        final TelephonyManager tm = (TelephonyManager) BaseApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(BaseApplication.getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        Log.d("debug", "uuid=" + uniqueId);
        return uniqueId;
    }

    //get UUID
    private String getMyUUID() {
        new PermissionUtils().getInstance(BaseApplication.getContext())
                .permissions(Permission.READ_PHONE_STATE)
                .errHint("Phone Permission")
                .permission(permissions -> {
                    Logger.e("apply phone permission success");
                    changeRight(true);
                }).start();

        if (isRight) {
            return getRightUUID();
        }

        return "";
    }

    private boolean isRight = false;

    private void changeRight(boolean right) {
        this.isRight = right;
    }

    /**
     * change to params style
     */
    protected String getParamsToUrl(Map<String, String> keyMaps) {
        StringBuilder result = new StringBuilder();
        if (keyMaps == null) {
            return "";
        }
        for (ConcurrentHashMap.Entry<String, String> entry : keyMaps.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
//            if (TextUtils.isEmpty(entry.getValue())) {
//                continue;
//            }
            if (entry.getValue() == null) {
                continue;
            }
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }
        return result.toString();
    }

    /**
     * dialog
     */
    public void setDialogCall(ResponseCallBack callBack, String mTitle, String mContent, Context mContext) {
        XyDialog2.Builder builder = new XyDialog2.Builder(mContext);
        if (TextUtils.isEmpty(mContent)) {
            mContent = mContext.getString(R.string.info188);
        }
        builder.title(mTitle);
        builder.message(mContent);
        builder.cancelTouchout(false);
        XyDialog2 xyDialog2 = builder.createProgressDialog();
        callBack.setDialog(xyDialog2);
        xyDialog2.show();

    }


}
