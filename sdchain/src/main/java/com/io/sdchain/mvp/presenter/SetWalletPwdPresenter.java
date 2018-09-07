package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.common.API;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.mvp.view.SetWalletPwdView;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/21 8:57
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class SetWalletPwdPresenter extends BasePresenter<SetWalletPwdView> {

    public SetWalletPwdPresenter(SetWalletPwdView setWalletPwdView) {
        super(setWalletPwdView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * verify password is same
     */
    private boolean validatePasswordIsEmpty(String password1, String password2) {
        if (password1 != null && !password1.equals("") && password2 != null && !password2.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * verify password is 6~16 digits
     */
    private boolean validatePasswordIsComplex(String password1, String password2) {
        if (password1.length() >= 6 && password1.length() <= 16 && password2.length() >= 6 && password2.length() <= 16) {
            return true;
        }
        return false;
    }

    /**
     * verify password is same
     */
    private boolean validatePasswordIsSame(String password1, String password2) {
        if (password1.equals(password2)) {
            return true;
        }
        return false;
    }

    /**
     * verify password
     */
    public void validatePassword(String password1, String password2) {
        if (validatePasswordIsEmpty(password1, password2)) {
            if (validatePasswordIsComplex(password1, password2)) {
                if (validatePasswordIsSame(password1, password2)) {
                    if (getView() != null) {
                        getView().beginChange();
                    }
                } else {
                    if (getView() != null) {
                        getView().passwordNotSame();
                    }
                }
            } else {
                if (getView() != null) {
                    getView().passwordNotComplex();
                }
            }
        } else {
            if (getView() != null) {
                getView().passwordNotEmpty();
            }
        }
    }


    /**
     * change wallet password
     */
    public void changeWalletPwd(String id, String userAccountId, String walletPassword, String newWalletPassword,String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("validStr", validStr);
        map.put("userAccountId", userAccountId);
        map.put("walletPassword", walletPassword);
        map.put("newWalletPassword", newWalletPassword);
        postData(API.UPDATEWALLETPASSWORD, map, true, new ResponseCallBack<Object>(context) {
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
        }, null, context.getString(R.string.info31), true,context);
    }
}
