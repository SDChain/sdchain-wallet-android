package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.CodeBean;
import com.io.sdchain.bean.CreditGrantBean;
import com.io.sdchain.common.API;
import com.io.sdchain.mvp.view.CreditGrantView;
import com.io.sdchain.net.ResponseCallBack;

import java.util.HashMap;
import java.util.List;

/**
 * @author xiey
 * @date created at 2018/4/18 9:05
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class CreditGrantPresenter extends BasePresenter<CreditGrantView> {

    public CreditGrantPresenter(CreditGrantView creditGrantView) {
        super(creditGrantView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * get trust list
     */
    public void getCreditGrantList(String userId, String code, int page, String account, Context context,boolean isShow) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("code", code);
        map.put("page", "" + page);
        map.put("account", account);
        getData(API.GETCURRENCYLISTS, map, true, new ResponseCallBack<List<CreditGrantBean>>(context) {
            @Override
            public void onSuccessResponse(List<CreditGrantBean> data, String msg) {
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
        }, null, context.getString(R.string.info168), isShow, context);
    }

    /**
     * trust
     */
    public void creditGrant(String userId, String userAccountId, String walletPassword, String limit, String currency, String counterparty, boolean isShow, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("userAccountId", userAccountId);
        map.put("walletPassword", walletPassword);
        map.put("limit", limit);
        map.put("currency", currency);
        map.put("counterparty", counterparty);
        postData(API.TRUSTLINE, map, true, new ResponseCallBack<List<CreditGrantBean>>(context) {
            @Override
            public void onSuccessResponse(List<CreditGrantBean> data, String msg) {
                if (getView() != null) {
                    getView().creditGrantSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().creditGrantFailed(msg);
                }
            }

        }, null, context.getString(R.string.info169), isShow, context);
    }

    /**
     * cancel trust
     */
    public void cancelCreditGrant(String userId, String userAccountId, String walletPassword, String currency, String counterparty, boolean isShow, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("userAccountId", userAccountId);
        map.put("walletPassword", walletPassword);
        map.put("limit", "0");
        map.put("currency", currency);
        map.put("counterparty", counterparty);
        postData(API.CANCELTRUSTLINE, map, true, new ResponseCallBack<CodeBean>(context) {
            @Override
            public void onSuccessResponse(CodeBean data, String msg) {
                if (getView() != null) {
                    getView().cancelCreditGrantSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().cancelCreditGrantFailed(msg);
                }
            }
        }, null, context.getString(R.string.info170), isShow, context);
    }

}
