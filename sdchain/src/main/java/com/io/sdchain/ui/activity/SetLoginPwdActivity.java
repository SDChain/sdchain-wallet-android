package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.CodeBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.mvp.presenter.SetLoginPwdPresenter;
import com.io.sdchain.mvp.view.SetLoginPwdView;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.TextChange;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.SetLoginPwdActivity)
public final class SetLoginPwdActivity extends BaseActivity<SetLoginPwdPresenter> implements SetLoginPwdView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.loginPwd)
    public EditText loginPwd;
    @BindView(R.id.password1)
    public EditText password1;
    @BindView(R.id.password2)
    public EditText password2;
    @BindView(R.id.code)
    public EditText code;
    @BindView(R.id.timerDown)
    public TextView timerDown;
    @BindView(R.id.changePayPwdBtn)
    public Button changePayPwdBtn;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.changeAccount)
    public TextView changeAccount;
    @BindView(R.id.changeAccountType)
    public TextView changeAccountType;
    @BindView(R.id.deleteLoginpwd)
    public RelativeLayout deleteLoginpwd;
    @BindView(R.id.deleteCode)
    public RelativeLayout deleteCode;
    @BindView(R.id.deletePassword1)
    public RelativeLayout deletePassword1;
    @BindView(R.id.deletePassword2)
    public RelativeLayout deletePassword2;
    private boolean canClick = true;
    private TextChange textChange;
    private String account = "";
    private UserBean userBean;

    @Override
    protected SetLoginPwdPresenter createPresenter() {
        return new SetLoginPwdPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_login_pwd);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info304));
        textChange = new TextChange();
        account = getIntent().getStringExtra(ARouterPath.DATA);
    }

    private void initListener() {
        loginPwd.addTextChangedListener(textChange);
        code.addTextChangedListener(textChange);
        password1.addTextChangedListener(textChange);
        password2.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private void initData() {
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        if (account.equals("0")) {
            changeAccountType.setText(getString(R.string.info305));
            if (userBean != null && !TextUtils.isEmpty(userBean.getPhone())) {
                changeAccount.setHint(userBean.getPhone());
            }

        } else if (account.equals("1")) {
            changeAccountType.setText(getString(R.string.info306));
            if (userBean != null && !TextUtils.isEmpty(userBean.getEmail())) {
                changeAccount.setHint(userBean.getEmail());
            }
        }
        changePayPwdBtn.setEnabled(false);
        changePayPwdBtn.setBackgroundResource(R.drawable.button_unenable);

        loginPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        loginPwd.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info637)));

        password1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        password1.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info637)));

        password2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        password2.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info637)));

        code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        code.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info649)));

    }

    @OnClick({R.id.timerDown, R.id.changePayPwdBtn, R.id.deleteLoginpwd, R.id.deleteCode, R.id.deletePassword1, R.id.deletePassword2})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.timerDown:
                //count down
                if (canClick) {
                    getPresenter().timeDown();
                    if (account.equals("0")) {
                        //mobile phone
                        getPresenter().getCodeByPhone(
                                userBean.getPhone(),
                                "1",
                                AesUtil2.encryptGET(userBean.getPhone()),
                                this);
                    } else if (account.equals("1")) {
                        //mailbox
                        getPresenter().getCodeByEmail(
                                userBean.getEmail(),
                                "1",
                                AesUtil2.encryptGET("1"),
                                this);
                    }
                    canClick = false;
                }
                break;
            case R.id.changePayPwdBtn:
                getPresenter().validatePassword(getEditString(password1), getEditString(password2));
                break;
            case R.id.deleteLoginpwd:
                loginPwd.setText("");
                break;
            case R.id.deleteCode:
                code.setText("");
                break;
            case R.id.deletePassword1:
                password1.setText("");
                break;
            case R.id.deletePassword2:
                password2.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(Object data, String msg) {
        //The login password was changed successfully
        showToast(msg);
        finish();
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void updateTime(long time) {
        timerDown.setTextColor(getResources().getColor(R.color.theme_btn_unpressed));
        timerDown.setText("" + time + "s");
    }

    @Override
    public void updateTimeOver() {
        timerDown.setTextColor(getResources().getColor(R.color.theme));
        timerDown.setText(getString(R.string.info307));
        canClick = true;
    }

    @Override
    public void changePayPwdSuccess(Object data, String msg) {
        //The payment password has been modified successfully
        showToast(msg);
        finish();
    }

    @Override
    public void changePwdFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void passwordNotEmpty() {
        showToast(getString(R.string.info308));
    }

    @Override
    public void passwordNotComplex() {
        showToast(getString(R.string.info309));
    }

    @Override
    public void passwordNotSame() {
        showToast(getString(R.string.info310));
    }

    @Override
    public void beginChange() {
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        CodeBean codeBean = (CodeBean) tools.getObjectData(Constants.CODEINFO);
        if (codeBean == null) {
            showToast(getString(R.string.info311));
            return;
        }
        //Change Logon Password
        getPresenter().changeLoginPwd(
                userBean.getId(),
                getEditString(loginPwd),
                getEditString(password1),
                codeBean.getSmsId(),
                getEditString(code),
                account,
                AesUtil2.encryptPOST(userBean.getId()),
                this);
    }

    @Override
    public void onCodeSuccess(Object data, String msg) {
        showToast(getString(R.string.info312));
        CodeBean codeBean = (CodeBean) data;
        tools.saveData(codeBean, Constants.CODEINFO);
    }

    @Override
    public void onCodeFailed(String msg) {
        showToast(msg);
        updateTimeOver();
        getPresenter().closeDisposable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().closeDisposable();
    }

    private TextChange.OnTextChange onTextChange = (editable) -> {
        if (haveString(loginPwd) && haveString(code) && haveString(password1) && haveString(password2)) {
            changePayPwdBtn.setEnabled(true);
            changePayPwdBtn.setBackgroundResource(R.drawable.selector_button);
        } else {
            changePayPwdBtn.setEnabled(false);
            changePayPwdBtn.setBackgroundResource(R.drawable.button_unenable);
        }

        if (haveString(loginPwd)) {
            deleteLoginpwd.setVisibility(View.VISIBLE);
        } else {
            deleteLoginpwd.setVisibility(View.GONE);
        }

        if (haveString(code)) {
            deleteCode.setVisibility(View.VISIBLE);
        } else {
            deleteCode.setVisibility(View.GONE);
        }

        if (haveString(password1)) {
            deletePassword1.setVisibility(View.VISIBLE);
        } else {
            deletePassword1.setVisibility(View.GONE);
        }

        if (haveString(password2)) {
            deletePassword2.setVisibility(View.VISIBLE);
        } else {
            deletePassword2.setVisibility(View.GONE);
        }

    };

}
