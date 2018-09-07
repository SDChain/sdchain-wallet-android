package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * @author xiey
 * @date created at 2018/3/8 13:52
 * @package com.io.sdchain.bean
 * @project SDChain
 */

public final class CodeBean implements Serializable {
    //phone number
    private String userName;
    //code id
    private String smsId;
    //code
    private String smsCode;
    //phone
    private String phone;
    //email
    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CodeBean{" +
                "userName='" + userName + '\'' +
                ", smsId='" + smsId + '\'' +
                ", smsCode='" + smsCode + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
