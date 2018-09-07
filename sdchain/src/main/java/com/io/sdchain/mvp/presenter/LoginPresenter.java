package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.API;
import com.io.sdchain.common.Constants;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.utils.SaveObjectTools;
import com.io.sdchain.mvp.view.LoginView;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;

/**
 * Created by xiey on 2017/8/20.
 */

public final class LoginPresenter extends BasePresenter<LoginView> {

    private Disposable disposable;

    public LoginPresenter(LoginView loginView) {
        super(loginView);
    }

    @Override
    public void closeDisposable() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * verify input is empty
     *
     * @param userName
     * @param password
     * @return
     */
    public boolean notEmpty(String userName, String password) {
        if (userName.equals("") || password.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * login
     * @param account
     * @param password
     * @param context
     * @param isShow
     */
    public void login(String account, String password, Context context,boolean isShow) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userName", account);
        map.put("password", password);
        postData(API.LOGIN, map, true, new ResponseCallBack<UserBean>(context) {
            @Override
            public void onSuccessResponse(UserBean data, String msg) {
                SaveObjectTools tools = new SaveObjectTools(context);
                UserBean userBean = data;
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
        }, null, context.getString(R.string.info47), isShow,context);
    }
}
