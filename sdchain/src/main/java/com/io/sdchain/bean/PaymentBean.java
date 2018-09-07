package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * @author xiey
 * @date created at 2018/3/14 15:03
 * @package com.io.sdchain.bean
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class PaymentBean implements Serializable {
    private BillDetailBean payment;

    public BillDetailBean getPayment() {
        return payment;
    }

    public void setPayment(BillDetailBean payment) {
        this.payment = payment;
    }
}
