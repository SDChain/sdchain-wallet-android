package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.FriendListBean;
import com.io.sdchain.common.API;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.mvp.view.FriendView;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/13 11:02
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class FriendPresenter extends BasePresenter<FriendView> {

    public FriendPresenter(FriendView friendView) {
        super(friendView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * blurry search friend
     */
    public void searchFriendList(String userId, String userName,String validStr, Context context,boolean isShow) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("validStr", validStr);
        map.put("userName", userName);
        getData(API.SEARCHFRIEND, map, true, new ResponseCallBack<FriendListBean>(context) {
            @Override
            public void onSuccessResponse(FriendListBean data, String msg) {
                if (getView() != null) {
                    getView().onSuccess(data.getUsers(), msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().onFailed(msg);
                }
            }

            @Override
            public void onNoData(String msg) {
                super.onNoData(msg);
            }
        }, null, context.getString(R.string.info184), isShow,context);
    }
}
