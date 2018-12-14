package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * @author xiey
 * @date created at 2018/5/7 17:51
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class TransactionBean implements Serializable {
    private String date;
    private String value;
    private String code;
    private String issuer;
    private String pic;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "TransactionBean{" +
                "date='" + date + '\'' +
                ", value='" + value + '\'' +
                ", code='" + code + '\'' +
                ", issuer='" + issuer + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
