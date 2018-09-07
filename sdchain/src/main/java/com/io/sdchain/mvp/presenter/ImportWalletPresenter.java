package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.common.API;
import com.io.sdchain.mvp.view.ImportWalletView;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.bean.ImportWalletBean;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/5/16 9:59
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class ImportWalletPresenter extends BasePresenter<ImportWalletView>{

    public ImportWalletPresenter(ImportWalletView importWalletView) {
        super(importWalletView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * import wallet
     */
    public void importWallet(String userId,String account, String secret,  String walletPassword,Context context, boolean isShow) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("account", account);
        map.put("secret", secret);
        map.put("walletPassword", walletPassword);
        postData(API.IMPORTWALLET, map, true, new ResponseCallBack<ImportWalletBean>(context) {
            @Override
            public void onSuccessResponse(ImportWalletBean data, String msg) {
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
        }, null, context.getString(R.string.info30), isShow,context);
    }
}
