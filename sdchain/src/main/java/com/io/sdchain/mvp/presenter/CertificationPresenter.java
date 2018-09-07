package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.common.API;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.mvp.view.CertificationView;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/6 16:04
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class CertificationPresenter extends BasePresenter<CertificationView> {

    public CertificationPresenter(CertificationView certificationView) {
        super(certificationView);
    }

    @Override
    public void closeDisposable() {

    }

    public void realNameCertification(String id, String idCode,String realName,String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("idCode", idCode);
        map.put("validStr", validStr);
        map.put("realName", realName);
        postData(API.REALNAME, map, true, new ResponseCallBack<Object>(context) {
            @Override
            public void onSuccessResponse(Object data, String msg) {
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
        }, null, "", true,context);
    }
}
