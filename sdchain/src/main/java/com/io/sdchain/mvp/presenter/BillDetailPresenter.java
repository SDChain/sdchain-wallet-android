package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.PaymentBean;
import com.io.sdchain.common.API;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.mvp.view.BillDetailView;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/14 13:46
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class BillDetailPresenter extends BasePresenter<BillDetailView> {

    public BillDetailPresenter(BillDetailView billDetailView) {
        super(billDetailView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * get trade detail
     */
    public void getDetail(String userAccountId, String hash, boolean isShow, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userAccountId", userAccountId);
        map.put("hash", hash);
        getData(API.GETPAYMENTSINFO, map, true, new ResponseCallBack<PaymentBean>(context) {
            @Override
            public void onSuccessResponse(PaymentBean data, String msg) {
                if (getView() != null) {
                    getView().onSuccess(data.getPayment(), msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().onFailed(msg);
                }
            }
        }, null, context.getString(R.string.info139), isShow,context);
    }
}
