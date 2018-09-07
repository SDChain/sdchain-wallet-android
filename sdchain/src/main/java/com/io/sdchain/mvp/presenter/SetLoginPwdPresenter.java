package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.CodeBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.API;
import com.io.sdchain.common.Constants;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.utils.SaveObjectTools;
import com.io.sdchain.mvp.view.SetLoginPwdView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiey on 2017/9/28.
 */

public final class SetLoginPwdPresenter extends BasePresenter<SetLoginPwdView> {

    public SetLoginPwdPresenter(SetLoginPwdView setLoginPwdView) {
        super(setLoginPwdView);
    }

    @Override
    public void closeDisposable() {
        if (disposable!=null&&!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private Disposable disposable;

    /**
     * start countdown
     */
    public void timeDown() {
        final long count = 60;
        Observable.interval(1, TimeUnit.SECONDS)
                .take(61)
                .map(aLong -> count - aLong)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        if (getView() != null) {
                            getView().updateTime(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (getView() != null) {
                            getView().updateTimeOver();
                        }
                    }
                });
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
     * verify is same
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
     * get code
     */
    public void getCodeByPhone(String phone,String mark,String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("account", phone);
        map.put("validStr", validStr);
        map.put("mark", mark);
        getData(API.PHONECODE, map, true, new ResponseCallBack<CodeBean>(context) {
            @Override
            public void onSuccessResponse(CodeBean data, String msg) {
                if (getView() != null) {
                    getView().onCodeSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().onCodeFailed(msg);
                }
            }
        }, null, "", false,context);
    }

    public void getCodeByEmail(String email,String mark,String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("account", email);
        map.put("validStr", validStr);
        map.put("mark", mark);
        getData(API.EMAILCODE, map, true, new ResponseCallBack<CodeBean>(context) {
            @Override
            public void onSuccessResponse(CodeBean data, String msg) {
                if (getView() != null) {
                    getView().onCodeSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().onCodeFailed(msg);
                }
            }
        }, null, "", false,context);
    }

    /**
     * change login password
     */
    public void changeLoginPwd(String id, String oldPassword, String password, String smsId, String code, String mark,String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("validStr", validStr);
        map.put("oldPassword", oldPassword);
        map.put("password", password);
        map.put("smsId", smsId);
        map.put("code", code);
        map.put("mark", mark);
        postData(API.UPDATEPASSWORD, map, true, new ResponseCallBack<Object>(context) {
            @Override
            public void onSuccessResponse(Object data, String msg) {
                SaveObjectTools tools = new SaveObjectTools(context);
                UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                userBean.setPassword(password);
                tools.saveData(userBean, Constants.USERINFO);
                tools.saveData(null,Constants.CODEINFO);
                if (getView() != null) {
                    getView().onSuccess(userBean, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().onFailed(msg);
                }
            }
        }, null, context.getString(R.string.info48), true,context);
    }


    /**
     * set pay password
     */
    public void changePayPwd(String id, String payPassword, String password, String smsId, String code, String mark,String validStr, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("validStr", validStr);
        map.put("payPassword", payPassword);
        map.put("password", password);
        map.put("smsId", smsId);
        map.put("code", code);
        map.put("mark", mark);
        postData(API.UPDATEPAYPASSWORD, map, true, new ResponseCallBack<Object>(context) {
            @Override
            public void onSuccessResponse(Object data, String msg) {
                if (getView() != null) {
                    getView().changePayPwdSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().changePwdFailed(msg);
                }
            }
        }, null, context.getString(R.string.info11), true,context);
    }
}
