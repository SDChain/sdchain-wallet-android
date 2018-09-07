package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * @author xiey
 * @date created at 2018/3/9 14:59
 * @package com.io.sdchain.bean
 * @project SDChain
 */

public final class AccountBean implements Serializable {
    private String userAccountId;
    private String account;
    private String secret;
    private String isdefault;//0:not default;1:default
    private String name;//wallet nick

    public AccountBean() {
    }

    public AccountBean(String account, String secret) {
        this.account = account;
        this.secret = secret;
    }

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AccountBean{" +
                "userAccountId='" + userAccountId + '\'' +
                ", account='" + account + '\'' +
                ", secret='" + secret + '\'' +
                ", isdefault='" + isdefault + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
