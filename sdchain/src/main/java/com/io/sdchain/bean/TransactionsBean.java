package com.io.sdchain.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiey
 * @date created at 2018/5/7 17:51
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class TransactionsBean implements Serializable{

    private List<TransactionBean> data;

    private String marker;

    public List<TransactionBean> getData() {
        return data;
    }

    public void setData(List<TransactionBean> data) {
        this.data = data;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }
}
