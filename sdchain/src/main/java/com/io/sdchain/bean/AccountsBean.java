package com.io.sdchain.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiey
 * @date created at 2018/3/9 19:44
 * @package com.io.sdchain.bean
 * @project SDChain
 */

public final class AccountsBean implements Serializable {
    private List<AccountBean> walletList;

    public List<AccountBean> getWalletList() {
        return walletList;
    }

    public void setWalletList(List<AccountBean> walletList) {
        this.walletList = walletList;
    }
}
