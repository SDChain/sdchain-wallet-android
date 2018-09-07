package com.io.sdchain.bean;

import com.io.sdchain.utils.PinyinUtils;

import java.io.Serializable;

/**
 * Created by xiey on 2017/10/12.
 */

public final class FriendBean implements Serializable {
    //name
    private String name;
    //phone
    private String phone;
    //pinyin
    private String pinyin;
    //pinyin first char
    private String headerWord;
    //wallet address
    private String walletAddress;
    //user ID
    private String userId;
    //real name
    private String realName;
    //nick
    private String nickName;
    //user name
    private String userName;
    private String email;
    private String account;//default wallet address
    private String isFriend;
    private String id;//same to userId

    public FriendBean() {
    }

    public FriendBean(String name, String userName) {
        this.name = name;
        this.userName = userName;
        this.pinyin = PinyinUtils.getPinyin(name);
        headerWord = pinyin.substring(0, 1);
    }

    public String getPinyin() {
        return pinyin = PinyinUtils.getPinyin(name);
    }

    public String getName() {
        return name;
    }

    public void setName() {

        if (realName != null && !realName.equals("")) {
            this.name = realName;
        } else if (nickName != null && !nickName.equals("")) {
            this.name = nickName;
        } else {
            this.name = userName;
        }
    }

    public String getHeaderWord() {
        return headerWord = getPinyin().substring(0, 1);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FriendBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", headerWord='" + headerWord + '\'' +
                ", walletAddress='" + walletAddress + '\'' +
                ", userId='" + userId + '\'' +
                ", realName='" + realName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", account='" + account + '\'' +
                ", isFriend='" + isFriend + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
