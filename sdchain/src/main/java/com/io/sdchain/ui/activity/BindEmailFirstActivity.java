package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
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
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.BindEmailFirstPresenter;
import com.io.sdchain.mvp.view.BindEmailFirstView;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.CheckFormat;
import com.io.sdchain.utils.TextChange;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.BindEmailFirstActivity)
public final class BindEmailFirstActivity extends BaseActivity<BindEmailFirstPresenter> implements BindEmailFirstView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.email)
    public EditText email;
    @BindView(R.id.password)
    public EditText password;
    @BindView(R.id.code)
    public EditText code;
    @BindView(R.id.timerDown)
    public TextView timerDown;
    @BindView(R.id.emailBindBtn)
    public Button emailBindBtn;
    @BindView(R.id.deletePhone)
    public RelativeLayout deletePhone;
    @BindView(R.id.deleteCode)
    public RelativeLayout deleteCode;
    @BindView(R.id.deletePassword)
    public RelativeLayout deletePassword;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    private boolean canClick = true;
    private TextChange textChange;
    private UserBean userBean;

    @Override
    protected BindEmailFirstPresenter createPresenter() {
        return new BindEmailFirstPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_email_first);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info83));
        textChange = new TextChange();
    }

    private void initListener() {
        email.addTextChangedListener(textChange);
        password.addTextChangedListener(textChange);
        code.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private void initData() {
        emailBindBtn.setEnabled(false);
        emailBindBtn.setBackgroundResource(R.drawable.button_unenable);
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);

        password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        password.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info637)));

        code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        code.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info649)));
    }

    @OnClick({R.id.timerDown, R.id.emailBindBtn, R.id.deletePhone, R.id.deleteCode, R.id.deletePassword})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.timerDown:
                //countdown
                //To judge whether it is clickable or not. When it is clickable, to judge whether the mailbox is legal or not
                if (canClick) {
                    if (CheckFormat.checkEmail(getEditString(email))) {
                        if (getEditString(email).equals(userBean.getEmail())) {
                            showToast(getString(R.string.info84));
                            return;
                        }
                        getPresenter().getCodeByEmail(
                                getEditString(email),
                                "1",
                                AesUtil2.encryptGET("1"),
                                this);
                        getPresenter().timeDown();
                        canClick = false;
                    } else {
                        showToast(getString(R.string.info85));
                        return;
                    }
                }
                break;
            case R.id.emailBindBtn:
                CodeBean codeInfo = (CodeBean) tools.getObjectData(Constants.CODEINFO);
                if (codeInfo == null) {
                    showToast(getString(R.string.info86));
                    return;
                }
                if (!CheckFormat.isEmail(getEditString(email))) {
                    showToast(getString(R.string.info87));
                    return;
                }

                if (getEditString(email).equals(userBean.getEmail())) {
                    showToast(getString(R.string.info88));
                    return;
                }

                UserBean userInfo = (UserBean) tools.getObjectData(Constants.USERINFO);
                getPresenter().bindPhone(
                        codeInfo.getSmsId(),
                        userInfo.getId(),
                        getEditString(code),
                        getEditString(password),
                        getEditString(email),
                        AesUtil2.encryptPOST(codeInfo.getSmsId()),
                        this);
                break;
            case R.id.deletePhone:
                email.setText("");
                break;
            case R.id.deleteCode:
                code.setText("");
                break;
            case R.id.deletePassword:
                password.setText("");
                break;
            default:
                break;
        }
    }

    ;

    @Override
    public void onSuccess(Object data, String msg) {
        UserBean userInfo = (UserBean) tools.getObjectData(Constants.USERINFO);
        userInfo.setEmail(getEditString(email));
        userInfo.setUserName(getEditString(email));
        tools.saveData(userInfo, Constants.USERINFO);
        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.BINDEMAIL, getEditString(email)));
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
        timerDown.setText(getString(R.string.info89));
        canClick = true;
    }

    @Override
    public void onCodeSuccess(Object data, String msg) {
        showToast(getString(R.string.info90));
        CodeBean codeInfo = (CodeBean) data;
        tools.saveData(codeInfo, Constants.CODEINFO);
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

    //Input Monitoring
    private TextChange.OnTextChange onTextChange = (editable) -> {
        if (haveString(email) && haveString(code) && haveString(password)) {
            emailBindBtn.setEnabled(true);
            emailBindBtn.setBackgroundResource(R.drawable.selector_button);
        } else {
            emailBindBtn.setEnabled(false);
            emailBindBtn.setBackgroundResource(R.drawable.button_unenable);
        }

        if (haveString(email)) {
            deletePhone.setVisibility(View.VISIBLE);
        } else {
            deletePhone.setVisibility(View.GONE);
        }

        if (haveString(code)) {
            deleteCode.setVisibility(View.VISIBLE);
        } else {
            deleteCode.setVisibility(View.GONE);
        }

        if (haveString(password)) {
            deletePassword.setVisibility(View.VISIBLE);
        } else {
            deletePassword.setVisibility(View.GONE);
        }
    };
}
