package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * @author xiey
 * @date created at 2018/3/19 17:51
 * @package com.io.sdchain.bean
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class MsgDetailBean implements Serializable{
    private MsgNetBean payment;

    public MsgNetBean getPayment() {
        return payment;
    }

    public void setPayment(MsgNetBean payment) {
        this.payment = payment;
    }
}
