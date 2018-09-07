package com.io.sdchain.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : xiey
 * @project name : sdchian.
 * @package name  : com.io.sdchian.bean.
 * @date : 2018/6/28.
 * @signature : do my best.
 * @explain :
 */
public final class MoneyBean implements Serializable{
    /**
     * get current account all money、freeze money、can use money
     */
    private BigDecimal all;
    private BigDecimal freeze;
    private BigDecimal canUse;

    public BigDecimal getAll() {
        return all;
    }

    public void setAll(BigDecimal all) {
        this.all = all;
    }

    public BigDecimal getFreeze() {
        return freeze;
    }

    public void setFreeze(BigDecimal freeze) {
        this.freeze = freeze;
    }

    public BigDecimal getCanUse() {
        return canUse;
    }

    public void setCanUse(BigDecimal canUse) {
        this.canUse = canUse;
    }
}
