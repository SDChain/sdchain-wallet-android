package com.io.sdchain.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.AccountBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.API;
import com.io.sdchain.common.Constants;
import com.io.sdchain.mvp.view.RegisterPhone2View;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.utils.SaveObjectTools;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;

/**
 * Created by xiey on 2017/9/21.
 */

public final class RegisterPhone2Presenter extends BasePresenter<RegisterPhone2View> {

    public RegisterPhone2Presenter(RegisterPhone2View registerPhone2View) {
        super(registerPhone2View);
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
        if (!TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2)) {
            return true;
        }
        return false;
    }

    /**
     * verify password is 6~16 digits
     */
    private boolean validatePasswordIsComplex(String password1, String password2) {
        if (password1.length() >= 6 && password1.length() <= 16 && password2.length() == 6) {
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
                //开始注册
                if (getView() != null) {
                    getView().beginRegister();
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
     * phone number register
     */
    public void registByPhone(String phone, String password, String smsId, String walletPassword, String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userName", phone);
        map.put("validStr", validStr);
        map.put("password", password);
        map.put("smsId", smsId);
        map.put("walletPassword", walletPassword);
        map.put("phone", phone);
        postData(API.REGISTER, map, true, new ResponseCallBack<AccountBean>(context) {
            @Override
            public void onSuccessResponse(AccountBean data, String msg) {
                SaveObjectTools tools = new SaveObjectTools(context);
                tools.saveData(null, Constants.CODEINFO);
                UserBean userBean = new UserBean();
                userBean.setUserName(phone);
                userBean.setPassword(password);
                userBean.setPhone(phone);
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
        }, null, "", true, context);
    }

}
