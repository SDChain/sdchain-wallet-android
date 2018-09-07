package com.io.sdchain.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiey
 * @date created at 2018/3/14 10:13
 * @package com.io.sdchain.bean
 * @project SDChain
 */

public final class BillsBean implements Serializable {
    private List<BillBean> payments;
    private String marker;

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public List<BillBean> getPayments() {
        return payments;
    }

    public void setPayments(List<BillBean> payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return "BillsBean{" +
                "payments=" + payments +
                ", marker='" + marker + '\'' +
                '}';
    }
}
