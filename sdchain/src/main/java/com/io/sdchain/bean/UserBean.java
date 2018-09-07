package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * Created by xiey on 2017/9/21.
 */

public final class UserBean implements Serializable {

    private String id;
    private String userName;//login username
    private String password;
    private String realName;
    private String nickName;
    private String apptoken;
    private String phone;
    private String email;
    private String account;//wallet address
    private String type;//wallet is active ? 0:no;1:yes
    private String userAccountId;//user wallet Id
    private String idCode;//card number
    private String passwordKey;//
    private String accountName;
    private String walletName;//wallet nick

    public UserBean() {
    }

    public UserBean(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public UserBean(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getApptoken() {
        return apptoken;
    }

    public void setApptoken(String apptoken) {
        this.apptoken = apptoken;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getPasswordKey() {
        return passwordKey;
    }

    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", realName='" + realName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", apptoken='" + apptoken + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", account='" + account + '\'' +
                ", type='" + type + '\'' +
                ", userAccountId='" + userAccountId + '\'' +
                ", idCode='" + idCode + '\'' +
                ", passwordKey='" + passwordKey + '\'' +
                ", accountName='" + accountName + '\'' +
                ", walletName='" + walletName + '\'' +
                '}';
    }
}
