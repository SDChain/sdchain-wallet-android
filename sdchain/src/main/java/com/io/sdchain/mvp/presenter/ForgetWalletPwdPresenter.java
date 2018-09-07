package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.common.API;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.mvp.view.ForgetWalletPwdView;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/21 8:59
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class ForgetWalletPwdPresenter extends BasePresenter<ForgetWalletPwdView> {

    public ForgetWalletPwdPresenter(ForgetWalletPwdView forgetWalletPwdView) {
        super(forgetWalletPwdView);
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
     * verity password
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
    public void forgetWalletPwd(String id, String userAccountId, String newWalletPassword,String secret, String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("validStr", validStr);
        map.put("userAccountId", userAccountId);
        map.put("newWalletPassword", newWalletPassword);
        map.put("secret", secret);
        postData(API.FORGETWALLETPASSWORD, map, true, new ResponseCallBack<Object>(context) {
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
        }, null, context.getString(R.string.info53), true,context);
    }
}
