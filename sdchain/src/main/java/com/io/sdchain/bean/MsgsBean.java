package com.io.sdchain.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiey
 * @date created at 2018/3/19 13:43
 * @package com.io.sdchain.bean
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class MsgsBean implements Serializable{
    private String marker;
    private List<MsgNetBean> payments;

    public List<MsgNetBean> getPayments() {
        return payments;
    }

    public void setPayments(List<MsgNetBean> payments) {
        this.payments = payments;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }
}
