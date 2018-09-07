package com.io.sdchain.utils;

import com.io.sdchain.bean.BalanceBean;

import java.util.ArrayList;

/**
 * @author xiey
 * @date created at 2018/5/9 9:56
 * @package com.io.sdchain.utils
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public class ListUtils {

    public static int getPosition(String key, ArrayList<BalanceBean> arrayList) {
        for (BalanceBean balanceBean : arrayList) {
            if (balanceBean.getCurrency().equals(key)) {
                return arrayList.indexOf(balanceBean);
            }
        }
        return -1;
    }

    public static String getCounterParty(String key, ArrayList<BalanceBean> arrayList) {
        if (arrayList == null) return "";
        for (BalanceBean balanceBean : arrayList) {
            if (balanceBean.getCurrency().equals(key)) {
                return balanceBean.getCounterparty();
            }
        }
        return null;
    }

    public static String getValue(String key, ArrayList<BalanceBean> arrayList) {
        if (arrayList == null) return "0.000000";
        for (BalanceBean balanceBean : arrayList) {
            if (balanceBean.getCurrency().equals(key)) {
                if (balanceBean.getValue() != null) {
                    return balanceBean.getValue();
                }
            }
        }
        return "0.000000";
    }

}
