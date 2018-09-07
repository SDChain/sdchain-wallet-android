package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.FriendBean;
import com.io.sdchain.common.API;
import com.io.sdchain.mvp.view.AddFriendView;
import com.io.sdchain.net.ResponseCallBack;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/13 15:51
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class AddFriendPresenter extends BasePresenter<AddFriendView> {

    public AddFriendPresenter(AddFriendView addFriendView) {
        super(addFriendView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * blurry search friend
     */
    public void addFriend(String userName,Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userName", userName);
        getData(API.SEARCHUSER, map, true, new ResponseCallBack<FriendBean>(context) {
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

            @Override
            public void onNoData(String msg) {
                super.onNoData(msg);
            }
        }, null, context.getString(R.string.info180), true,context);
    }
}
