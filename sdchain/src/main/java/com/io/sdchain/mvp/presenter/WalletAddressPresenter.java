package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.common.API;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.mvp.view.WalletAddressView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/15 16:15
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class WalletAddressPresenter extends BasePresenter<WalletAddressView> {

    public WalletAddressPresenter(WalletAddressView walletAddressView) {
        super(walletAddressView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * change wallet nick
     */
    public void changeAccountName(String userId, String accountName, String userAccountId,String validStr,boolean isShow, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("validStr", validStr);
        map.put("accountName", accountName);
        map.put("userAccountId", userAccountId);
        getData(API.UPDATEACCOUNTNAME, map, true, new ResponseCallBack<Object>(context) {
            @Override
            public void onSuccessResponse(Object data, String msg) {
                if (getView() != null) {
                    String content = "";
                    try {
                        content = URLDecoder.decode("" + accountName, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    getView().onSuccess(content, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().onFailed(msg);
                }
            }
        }, null, context.getString(R.string.info32), isShow,context);
    }
}
