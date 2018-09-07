package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.common.API;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.mvp.view.MineView;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/10 14:30
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class MinePresenter extends BasePresenter<MineView> {

    public MinePresenter(MineView mineView) {
        super(mineView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * change nick
     */
    public void updateNickName(String id, String nickName,String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("validStr", validStr);
        map.put("nickName", nickName);
        postData(API.UPDATENICKNAME, map, true, new ResponseCallBack<Object>(context) {
            @Override
            public void onSuccessResponse(Object data, String msg) {
                if (getView() != null) {
                    getView().onSuccess(nickName, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().onFailed(msg);
                }
            }
        }, null, "", false,context);
    }

}
