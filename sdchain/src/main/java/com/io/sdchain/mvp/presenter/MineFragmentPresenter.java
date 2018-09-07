package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.VersionBean;
import com.io.sdchain.common.API;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.mvp.view.MineFragmentView;

import java.util.HashMap;

/**
 * Created by xiey on 2017/8/19.
 */

public final class MineFragmentPresenter extends BasePresenter<MineFragmentView> {

    public MineFragmentPresenter(MineFragmentView mineFragmentView) {
        super(mineFragmentView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * get application version
     */
    public void getApkVersion(String versionCode, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", "0");
        map.put("versionCode", versionCode);
        getData(API.CHECKVERSION, map, true, new ResponseCallBack<VersionBean>(context) {
            @Override
            public void onSuccessResponse(VersionBean data, String msg) {
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
        }, context.getString(R.string.info21), null, false,context);
    }
}
