package com.io.sdchain.bean;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * @author xiey
 * @date created at 2018/3/9 13:10
 * @package com.io.sdchain.bean
 * @project SDChain
 */

public final class BalancesBean {
    private ArrayList<BalanceBean> balances;

    private String reserveBase;

    public ArrayList<BalanceBean> getBalances() {
        return balances;
    }

    public void setBalances(ArrayList<BalanceBean> balances) {
        this.balances = balances;
    }

    public String getReserveBase() {
        if (TextUtils.isEmpty(reserveBase)) return "0.6";
        return reserveBase;
    }

    public void setReserveBase(String reserveBase) {
        this.reserveBase = reserveBase;
    }

    @Override
    public String toString() {
        return "BalancesBean{" +
                "balances=" + balances +
                ", reserveBase=" + reserveBase +
                '}';
    }
}
