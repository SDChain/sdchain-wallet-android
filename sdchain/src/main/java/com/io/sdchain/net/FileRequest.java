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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public final class FileRequest<T extends ResponseBean> extends Request<T> {

    private final Listener<T> mListener;
    private Gson mGson;
    private FileParams params;
    private final String TYPE_UTF8_CHARSET = "charset=UTF-8";

    public FileRequest(String url, FileParams params, ResponseCallBack responseCallBack) {
        super(Method.POST, url, responseCallBack.errorListener);
        mGson = new Gson();
        this.params = params;
        mListener = responseCallBack.listener;
        setShouldCache(false);
        setRetryPolicy(new DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            params.writeTo(bos);
        } catch (IOException e) {
            e.printStackTrace();
            LogInfo.e(e.getMessage());
        }
        return bos.toByteArray();
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
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            LogInfo.e("RESPONSE:"+jsonString);
            return Response.success(mGson.fromJson(jsonString, ResponseBean.class), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            LogInfo.e(e.getMessage());
            return Response.error(new ParseError(e));
        }
    }
//
//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        // TODO Auto-generated method stub
//        Map<String, String> headers = super.getHeaders();
//        if (null == headers || headers.equals(Collections.emptyMap())) {
//            headers = new HashMap<String, String>();
//        }
//        return headers;
//    }

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

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return params.getContentType();
    }
}
