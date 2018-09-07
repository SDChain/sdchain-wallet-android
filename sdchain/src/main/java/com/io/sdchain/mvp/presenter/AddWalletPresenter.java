package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.AccountBean;
import com.io.sdchain.common.API;
import com.io.sdchain.mvp.view.AddWalletView;
import com.io.sdchain.net.ResponseCallBack;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/5/16 9:41
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class AddWalletPresenter extends BasePresenter<AddWalletView>{

    public AddWalletPresenter(AddWalletView addWalletView) {
        super(addWalletView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * create wallet
     */
    public void createAccount(String id, String walletPassword, String validStr, Context context, boolean isShow) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("validStr", validStr);
        map.put("walletPassword", walletPassword);
        postData(API.CREATEWALLET, map, true, new ResponseCallBack<AccountBean>(context) {
            @Override
            public void onSuccessResponse(AccountBean data, String msg) {
                if (getView() != null) {
                    getView().onSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().onFailed(msg);
                }
            }
        }, null, context.getString(R.string.info27), isShow,context);
    }

}
