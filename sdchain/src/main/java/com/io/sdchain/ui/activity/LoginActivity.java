package com.io.sdchain.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.LoginPresenter;
import com.io.sdchain.mvp.view.LoginView;
import com.io.sdchain.utils.TextChange;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.LoginActivity)
public final class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView {

    @BindView(R.id.account)
    public EditText account;
    @BindView(R.id.password)
    public EditText password;
    @BindView(R.id.deleteAccount)
    public RelativeLayout deleteAccount;
    @BindView(R.id.deletePassword)
    public RelativeLayout deletePassword;
    private TextChange textChange;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();

    }

    private void initView() {
        textChange = new TextChange();
    }

    private void initListener() {
        account.addTextChangedListener(textChange);
        password.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
    }

    private void initData() {
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        if (userBean != null) {
            account.setText(userBean.getUserName());
            password.setText(userBean.getPassword());
            account.setSelection(account.length());
            if (sharedPref.getData(Constants.LOGINOUT) != null) {
                boolean loginOut = (boolean) sharedPref.getData(Constants.LOGINOUT);
                Logger.e("-----loginOut-----" + loginOut);
                if (!loginOut) {
                    sharedPref.saveData(Constants.LOGINOUT, false);
                    ARouter.getInstance()
                            .build(ARouterPath.MainActivity)
                            .withTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            .navigation(this, new NavCallback() {
                                @Override
                                public void onArrival(Postcard postcard) {
                                    finish();
                                }
                            });
                }
            }
        }

        password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        password.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info637)));

    }

    @OnClick({R.id.loginBtn, R.id.loginForgetPwd, R.id.loginRegister, R.id.deleteAccount, R.id.deletePassword})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                //login
                String userNameStr = account.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();

                if (getPresenter().notEmpty(userNameStr, passwordStr)) {
                    getPresenter().login(
                            userNameStr,
                            passwordStr,
                            LoginActivity.this,
                            true);
                } else {
                    showToast(getString(R.string.info264));
                }
                break;
            case R.id.loginForgetPwd:
                //forget password
                ARouter.getInstance()
                        .build(ARouterPath.ForgetPwdActivity)
                        .withTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        .navigation();
                break;
            case R.id.loginRegister:
                //register
                ARouter.getInstance()
                        .build(ARouterPath.RegisterPhoneActivity)
                        .withTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        .navigation();
                break;
            case R.id.deleteAccount:
                //delete account info
                account.setText("");
                password.setText("");
                break;
            case R.id.deletePassword:
                //delete password info
                password.setText("");
                break;
            default:
                break;
        }
    }

    private TextChange.OnTextChange onTextChange = (editable) -> {
        if (haveString(account)) {
            deleteAccount.setVisibility(View.VISIBLE);
        } else {
            deleteAccount.setVisibility(View.GONE);
        }

        if (haveString(password)) {
            deletePassword.setVisibility(View.VISIBLE);
        } else {
            deletePassword.setVisibility(View.GONE);
        }

    };

    @Override
    public void onSuccess(Object data, String msg) {
        UserBean userBean = (UserBean) data;
        tools.saveData(userBean, Constants.USERINFO);
        sharedPref.saveData(Constants.TOKWN, userBean.getApptoken());
        ARouter.getInstance()
                .build(ARouterPath.MainActivity)
                .navigation(this, new NavCallback() {
                    @Override
                    public void onArrival(Postcard postcard) {
                        finish();
                    }
                });
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
        tools.saveData(null, Constants.USERINFO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().closeDisposable();
        EventBus.getDefault().unregister(this);
    }

    /**
     * The password was registered and changed successfully
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterOk(EventBusType type) {
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        //phone register
        if (type.getBusType() == EventBusType.BusType.REGISTBYPHONE) {
            account.setText(userBean.getUserName());
            password.setText(userBean.getPassword());
            account.setSelection(account.length());
            finish();
        }
        //email register
        if (type.getBusType() == EventBusType.BusType.REGISTBYEMAIL) {
            account.setText(userBean.getUserName());
            password.setText(userBean.getPassword());
            account.setSelection(account.length());
            finish();
        }
        //forget password
        if (type.getBusType() == EventBusType.BusType.FORGETPWD) {
            account.setText(userBean.getUserName());
            password.setText(userBean.getPassword());
            account.setSelection(account.length());
        }
    }

}
