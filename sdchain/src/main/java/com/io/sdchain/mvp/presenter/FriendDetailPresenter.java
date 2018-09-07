package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.FriendBean;
import com.io.sdchain.common.API;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.mvp.view.FriendDetailView;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/13 10:49
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class FriendDetailPresenter extends BasePresenter<FriendDetailView> {

    public FriendDetailPresenter(FriendDetailView friendDetailView) {
        super(friendDetailView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * get friend detail
     */
    public void friendDetail(String userId, String adverseUserId,String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("validStr", validStr);
        map.put("adverseUserId", adverseUserId);
        getData(API.GETFRIENDINFO, map, true, new ResponseCallBack<FriendBean>(context) {
            @Override
            public void onSuccessResponse(FriendBean data, String msg) {
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
        }, null, context.getString(R.string.info181), true,context);
    }

    /**
     * add friend
     */
    public void addFriend(String userId, String adverseUserId,String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("validStr", validStr);
        map.put("adverseUserId", adverseUserId);
        getData(API.ADDFRIEND, map, true, new ResponseCallBack<Object>(context) {
            @Override
            public void onSuccessResponse(Object data, String msg) {
                if (getView() != null) {
                    getView().addFriendSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().addFriendFailed(msg);
                }
            }
        }, null, context.getString(R.string.info182), true,context);
    }


    /**
     * delete friend
     */
    public void deleteFriend(String userId, String adverseUserId,String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("validStr", validStr);
        map.put("adverseUserId", adverseUserId);
        getData(API.DELETEFRIEND, map, true, new ResponseCallBack<Object>(context) {
            @Override
            public void onSuccessResponse(Object data, String msg) {
                if (getView() != null) {
                    getView().deleteFriendSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().deleteFriendFailed(msg);
                }
            }
        }, null, context.getString(R.string.info183), true,context);
    }
}
