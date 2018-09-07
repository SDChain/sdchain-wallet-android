package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * @author xiey
 * @date created at 2018/3/9 13:02
 * @package com.io.sdchain.bean
 * @project SDChain
 */

public final class BalanceBean implements Serializable {
    private String value;
    private String currency;
    private String counterparty;
    private String pic;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    @Override
    public String toString() {
        return "BalanceBean{" +
                "value='" + value + '\'' +
                ", currency='" + currency + '\'' +
                ", counterparty='" + counterparty + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
