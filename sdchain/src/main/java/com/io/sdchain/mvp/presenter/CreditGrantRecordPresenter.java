package com.io.sdchain.mvp.presenter;

import android.content.Context;


import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.base.BaseView;
import com.io.sdchain.bean.TransactionsBean;
import com.io.sdchain.common.API;
import com.io.sdchain.net.ResponseCallBack;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/4/18 17:39
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class CreditGrantRecordPresenter extends BasePresenter<BaseView> {

    public CreditGrantRecordPresenter(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * get trust record
     */
    public void getCreditGrantRecord(String userId, String account, String marker, Context context,boolean isShow) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("account", account);
        map.put("marker", marker);
        postData(API.GETHISTRUSTLINES, map, true, new ResponseCallBack<TransactionsBean>(context) {
            @Override
            public void onSuccessResponse(TransactionsBean data, String msg) {
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
        }, null, context.getString(R.string.info171), isShow, context);
    }

}
