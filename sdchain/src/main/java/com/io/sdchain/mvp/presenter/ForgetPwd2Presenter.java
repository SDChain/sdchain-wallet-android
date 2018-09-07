package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.API;
import com.io.sdchain.common.Constants;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.utils.SaveObjectTools;
import com.io.sdchain.mvp.view.ForgetPwd2View;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;

/**
 * Created by xiey on 2017/9/21.
 */

public final class ForgetPwd2Presenter extends BasePresenter<ForgetPwd2View> {

    public ForgetPwd2Presenter(ForgetPwd2View forgetPwd2View) {
        super(forgetPwd2View);
    }

    @Override
    public void closeDisposable() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private Disposable disposable;

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
     * verity password is same
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
                    //start forget password
                    if (getView() != null) {
                        getView().beginNewPwd();
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

    public void forgetPassword(String userName, String password, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userName", userName);
        map.put("password", password);
        postData(API.FORGETPASSWORD, map, true, new ResponseCallBack<Object>(context) {
            @Override
            public void onSuccessResponse(Object data, String msg) {
                SaveObjectTools tools = new SaveObjectTools(context);
                tools.saveData(null, Constants.CODEINFO);
                UserBean userBean  = new UserBean();
                userBean.setUserName(userName);
                userBean.setPassword(password);
                tools.saveData(userBean, Constants.USERINFO);
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
        }, null,context.getString(R.string.info52), true,context);
    }


}
