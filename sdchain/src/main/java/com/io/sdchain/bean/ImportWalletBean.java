package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * @author xiey
 * @date created at 2018/5/16 13:32
 * @package com.io.sdchain.ui.activity
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class ImportWalletBean implements Serializable{

    private String id;
    private String userId;
    private String type;
    private String account;
    private String secret;
    private String isDefault;
    private String name;
    private String comment1;
    private Object comment2;
    private Object comment3;
    private Object comment4;
    private Object createDate;
    private Object updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public Object getComment2() {
        return comment2;
    }

    public void setComment2(Object comment2) {
        this.comment2 = comment2;
    }

    public Object getComment3() {
        return comment3;
    }

    public void setComment3(Object comment3) {
        this.comment3 = comment3;
    }

    public Object getComment4() {
        return comment4;
    }

    public void setComment4(Object comment4) {
        this.comment4 = comment4;
    }

    public Object getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Object createDate) {
        this.createDate = createDate;
    }

    public Object getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Object updateDate) {
        this.updateDate = updateDate;
    }
}
