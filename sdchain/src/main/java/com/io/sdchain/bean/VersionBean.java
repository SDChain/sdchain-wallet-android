package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * Created by ziyong on 2016/6/7.
 */
public final class VersionBean implements Serializable {

    private String id;
    private String versionName;
    private String versionCode;
    private String url;
    private String versionState;
    private String type;
    private String versionDesc;
    private String isDel;
    private String createDate;
    private String createUser;
    private String updateDate;
    private String updateUser;

    public void setId(String id) {
        this.id = id;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVersionState(String versionState) {
        this.versionState = versionState;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getId() {
        return id;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getUrl() {
        return url;
    }

    public String getVersionState() {
        return versionState;
    }

    public String getType() {
        return type;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public String getIsDel() {
        return isDel;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "VersionBean{" +
                "id='" + id + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", url='" + url + '\'' +
                ", versionState='" + versionState + '\'' +
                ", type='" + type + '\'' +
                ", versionDesc='" + versionDesc + '\'' +
                ", isDel='" + isDel + '\'' +
                ", createDate='" + createDate + '\'' +
                ", createUser='" + createUser + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }
}
