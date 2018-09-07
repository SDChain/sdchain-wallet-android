package com.io.sdchain.net;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.io.sdchain.bean.ResponseBean;
import com.io.sdchain.utils.LogInfo;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
/**
 * @author xiey
 */
public final class GsonRequest<T extends ResponseBean> extends Request<T> {

    private final Listener<T> mListener;
    private Gson mGson;
    private Map<String, String> params;
    private final String TYPE_UTF8_CHARSET = "charset=UTF-8";

    public GsonRequest(String url, Map<String, String> params, ResponseCallBack responseCallBack) {
        super(Method.POST, url, responseCallBack.errorListener);
        mGson = new Gson();
        this.params = params;
        mListener = responseCallBack.listener;
        setRetryPolicy(new DefaultRetryPolicy(20*1000,0,1.0f));
    }

    public GsonRequest(String url, ResponseCallBack responseCallBack) {
        super(Method.GET, url, responseCallBack.errorListener);
        LogInfo.e(url);
        mGson = new Gson();
        mListener = responseCallBack.listener;
        setRetryPolicy(new DefaultRetryPolicy(20*1000,0,1.0f));
    }

    public GsonRequest(String url, ResponseCallBackForService responseCallBack) {
        super(Method.GET, url, responseCallBack.errorListener);
        LogInfo.e(url);
        mGson = new Gson();
        mListener = responseCallBack.listener;
        setRetryPolicy(new DefaultRetryPolicy(20*1000,0,1.0f));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (params == null) {
            return new HashMap<String, String>();
        }
        return this.params;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String type = response.headers.get("Content-Type");
            if (type == null) {
                type = TYPE_UTF8_CHARSET;
                response.headers.put("Content-Type", type);
            } else if (!type.contains("UTF-8")) {
                type += ";" + TYPE_UTF8_CHARSET;
                response.headers.put("Content-Type", type);
            }
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            LogInfo.e("CallBack Data:" + jsonString);
            return Response.success(mGson.fromJson(jsonString, ResponseBean.class), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            LogInfo.e(e.getMessage());
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mHeaders != null) {
            return mHeaders;
        }
        return super.getHeaders();
    }


    private Map<String, String> mHeaders;

    public void setHeaders(Map<String, String> mHeaders) {
        if (mHeaders != null) {
            this.mHeaders = mHeaders;
        }

    }


//    public  Map<String ,String >  setHeaders()
//    {
//        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("Content-Type", "application/json");
////        headers.put("apiKey", "xxxxxxxxxxxxxxx");
//       // if()
//        return headers;
//       // return super.getHeaders();
//    }

//    @Override
//    public String getBodyContentType() {
//        return "application/json; charset=" + getParamsEncoding();
//    }
//
//    @Override
//    public byte[] getBody() throws AuthFailureError {
//        Map<String, String> params = getParams();
//        if (params != null && params.size() > 0) {
//            JsonObject jsonObject = new JsonObject();
//            for (String key : params.keySet()) {
//                jsonObject.addProperty(key, params.get(key));
//            }
//            Gson gson = new Gson();
//            String mst = gson.toJson(jsonObject);
//            LogInfo.e("CallBack Json:" + mst);
//            try {
//                return mst.getBytes(getParamsEncoding());
//            } catch (UnsupportedEncodingException e) {
//                LogInfo.e(e.getMessage());
//            }
//
//        }
//        return null;
//    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

}