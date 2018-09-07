package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
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
import com.io.sdchain.mvp.presenter.BindPhonePresenter;
import com.io.sdchain.mvp.view.BindPhoneView;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.CheckFormat;
import com.io.sdchain.utils.TextChange;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.BindPhoneActivity)
public final class BindPhoneActivity extends BaseActivity<BindPhonePresenter> implements BindPhoneView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.phone)
    public EditText phone;
    @BindView(R.id.password)
    public EditText password;
    @BindView(R.id.code)
    public EditText code;
    @BindView(R.id.timerDown)
    public TextView timerDown;
    @BindView(R.id.phoneBindBtn)
    public Button phoneBindBtn;
    @BindView(R.id.deletePhone)
    public RelativeLayout deletePhone;
    @BindView(R.id.deleteCode)
    public RelativeLayout deleteCode;
    @BindView(R.id.deletePassword)
    public RelativeLayout deletePassword;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.oldPhone)
    public TextView oldPhone;
    private boolean canClick = true;
    private TextChange textChange;
    private UserBean userBean;

    @Override
    protected BindPhonePresenter createPresenter() {
        return new BindPhonePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info96));
        phone.setInputType(InputType.TYPE_CLASS_NUMBER);//phone number
        textChange = new TextChange();
    }

    private void initListener() {
        phone.addTextChangedListener(textChange);
        password.addTextChangedListener(textChange);
        code.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private void initData() {
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        oldPhone.setText(userBean.getPhone());
        phoneBindBtn.setEnabled(false);
        phoneBindBtn.setBackgroundResource(R.drawable.button_unenable);

        password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        password.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info637)));

        code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        code.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info649)));

        phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        phone.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info649)));
    }

    @OnClick({R.id.timerDown, R.id.phoneBindBtn, R.id.deletePhone, R.id.deleteCode, R.id.deletePassword})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.timerDown:
                //countdown
                if (canClick) {
                    if (CheckFormat.isMobile(getEditString(phone))) {
                        if (getEditString(phone).equals(userBean.getPhone())) {
                            showToast(getString(R.string.info97));
                            return;
                        }
                        getPresenter().getCodeByPhone(
                                getEditString(phone),
                                "1",
                                AesUtil2.encryptGET(getEditString(phone)),
                                BindPhoneActivity.this);
                        getPresenter().timeDown();
                        canClick = false;
                    } else {
                        showToast(getString(R.string.info98));
                    }
                }
                break;
            case R.id.phoneBindBtn:
                CodeBean codeInfo = (CodeBean) tools.getObjectData(Constants.CODEINFO);
                if (codeInfo == null) {
                    showToast(getString(R.string.info99));
                    return;
                }
                if (!CheckFormat.isMobile(getEditString(phone))) {
                    showToast(getString(R.string.info100));
                    return;
                }
                if (getEditString(phone).equals(userBean.getPhone())) {
                    showToast(getString(R.string.info101));
                    return;
                }
                UserBean userInfo = (UserBean) tools.getObjectData(Constants.USERINFO);
                getPresenter().bindPhone(
                        codeInfo.getSmsId(),
                        userInfo.getId(),
                        getEditString(code),
                        getEditString(phone),
                        getEditString(password),
                        AesUtil2.encryptPOST(codeInfo.getSmsId()),
                        this);
                break;
            case R.id.deletePhone:
                phone.setText("");
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

    @Override
    public void onSuccess(Object data, String msg) {
        Logger.e("The phone number binding was successful");
        UserBean userInfo = (UserBean) tools.getObjectData(Constants.USERINFO);
        userInfo.setPhone(getEditString(phone));
        userInfo.setUserName(getEditString(phone));
        tools.saveData(userInfo, Constants.USERINFO);
        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.BINDPHONE, getEditString(phone)));
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
        timerDown.setText(getString(R.string.info102));
        canClick = true;
    }

    @Override
    public void onCodeSuccess(Object data, String msg) {
        showToast(getString(R.string.info103));
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
        if (haveString(phone) && haveString(code) && haveString(password)) {
            phoneBindBtn.setEnabled(true);
            phoneBindBtn.setBackgroundResource(R.drawable.selector_button);
        } else {
            phoneBindBtn.setEnabled(false);
            phoneBindBtn.setBackgroundResource(R.drawable.button_unenable);
        }

        if (haveString(phone)) {
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
