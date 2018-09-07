package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.MsgDetailBean;
import com.io.sdchain.common.API;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.mvp.view.MsgDetailView;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/19 17:36
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class MsgDetailPresenter extends BasePresenter<MsgDetailView> {

    public MsgDetailPresenter(MsgDetailView msgDetailView) {
        super(msgDetailView);
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
        getData(API.GETPAYMENTSINFO, map, true, new ResponseCallBack<MsgDetailBean>(context) {
            @Override
            public void onSuccessResponse(MsgDetailBean data, String msg) {
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
        }, null, context.getString(R.string.info178), isShow,context);
    }
}
