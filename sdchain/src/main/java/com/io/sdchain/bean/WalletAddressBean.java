package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * @author xiey
 * @date created at 2018/3/13 14:39
 * @package com.io.sdchain.bean
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class WalletAddressBean implements Serializable {

    private String realName;
    private String nickName;
    private String account;
    private String userName;

    public String getRealName() {
        return realName;
    }

    public WalletAddressBean setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public WalletAddressBean setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public WalletAddressBean setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public WalletAddressBean setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Override
    public String toString() {
        return "WalletAddressBean{" +
                "realName='" + realName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", account='" + account + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
