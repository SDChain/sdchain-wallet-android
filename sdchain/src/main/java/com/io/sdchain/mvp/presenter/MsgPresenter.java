package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.MsgsBean;
import com.io.sdchain.common.API;
import com.io.sdchain.mvp.view.MsgView;
import com.io.sdchain.net.ResponseCallBack;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/19 13:46
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class MsgPresenter extends BasePresenter<MsgView> {

    public MsgPresenter(MsgView msgView) {
        super(msgView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * get message list
     */
    public void getMsgs(String userAccountId, String userId, String marker, boolean isShow, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userAccountId", userAccountId);
        map.put("marker", "" + marker);
        getData(API.GETPAYMENTSLIST, map, true, new ResponseCallBack<MsgsBean>(context) {
            @Override
            public void onSuccessResponse(MsgsBean data, String msg) {
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
        }, null, context.getString(R.string.info179), isShow,context);
    }
}
