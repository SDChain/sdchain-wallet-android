package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.BillsBean;
import com.io.sdchain.common.API;
import com.io.sdchain.mvp.view.BillView;
import com.io.sdchain.net.ResponseCallBack;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/14 10:18
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class BillPresenter extends BasePresenter<BillView> {

    public BillPresenter(BillView billView) {
        super(billView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * get bill
     */
    public void getBills(String userAccountId, String marker, String currency, String minDate, String maxDate, boolean isShow, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userAccountId", userAccountId);
        map.put("marker", marker);
        map.put("currency", currency);
        map.put("minDate", "" + minDate);
        map.put("maxDate", "" + maxDate);
        getData(API.GETPAYMENTSLIST, map, true, new ResponseCallBack<BillsBean>(context) {
            @Override
            public void onSuccessResponse(BillsBean data, String msg) {
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
        }, null, context.getString(R.string.info140), isShow, context);
    }
}
