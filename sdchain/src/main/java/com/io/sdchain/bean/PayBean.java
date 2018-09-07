package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * @author xiey
 * @date created at 2018/3/20 16:22
 * @package com.io.sdchain.bean
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class PayBean implements Serializable {

    private String hash;
    private String status_url;
    private boolean success;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getStatus_url() {
        return status_url;
    }

    public void setStatus_url(String status_url) {
        this.status_url = status_url;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "PayBean{" +
                "hash='" + hash + '\'' +
                ", status_url='" + status_url + '\'' +
                ", success=" + success +
                '}';
    }
}
