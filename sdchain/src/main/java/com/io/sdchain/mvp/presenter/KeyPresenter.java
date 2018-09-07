package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.AccountBean;
import com.io.sdchain.common.API;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.mvp.view.KeyView;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/10 18:27
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class KeyPresenter extends BasePresenter<KeyView> {

    public KeyPresenter(KeyView keyView) {
        super(keyView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * get key
     */
    public void getWalletSecret(String userId, String userAccountId,String walletPassword,String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", userId);
        map.put("validStr", validStr);
        map.put("userAccountId", userAccountId);
        map.put("walletPassword", walletPassword);
        postData(API.GETWALLETSECRET, map, true, new ResponseCallBack<AccountBean>(context) {
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
        }, null, "", true,context);
    }
}
