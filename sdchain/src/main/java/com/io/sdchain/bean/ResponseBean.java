package com.io.sdchain.bean;

import com.google.gson.JsonElement;

/**
 * Created by ziyong on 2016/2/23.
 */
public final class ResponseBean {
    private String code="";
    private String message="";
    private String debug="";
    private JsonElement data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }



    /**
     *
     */
    public boolean isSuccess() {
        if (code.startsWith("S00001")) {
            return true;
        }
        return false;
    }

    public boolean isNoData() {
        if (code.equals("E00001")) {
            return true;
        }
        return false;
    }

    public boolean isOutToken() {
        if (code.equals("E00002")) {
            return true;
        }
        return false;
    }
    public boolean isNull() {
        if (code==null) {
            return true;
        }
        return false;
    }

}
