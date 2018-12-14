package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * @author xiey
 * @date created at 2018/4/18 9:26
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class CreditGrantBean implements Serializable {

    private String currency;
    private String limit;
    private String counterparty;
    private String pic;
    private boolean trusted;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    @Override
    public String toString() {
        return "CreditGrantBean{" +
                "currency='" + currency + '\'' +
                ", limit='" + limit + '\'' +
                ", counterparty='" + counterparty + '\'' +
                ", pic='" + pic + '\'' +
                ", trusted=" + trusted +
                '}';
    }
}
