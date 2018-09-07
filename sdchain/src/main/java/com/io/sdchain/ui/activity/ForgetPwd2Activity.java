package com.io.sdchain.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.ForgetPwd2Presenter;
import com.io.sdchain.mvp.view.ForgetPwd2View;
import com.io.sdchain.utils.TextChange;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.ForgetPwd2Activity)
public final class ForgetPwd2Activity extends BaseActivity<ForgetPwd2Presenter> implements ForgetPwd2View {

    @BindView(R.id.forgetBtn)
    public Button forgetBtn;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.password1)
    public EditText password1;
    @BindView(R.id.password2)
    public EditText password2;
    @BindView(R.id.deletePassword1)
    public RelativeLayout deletePassword1;
    @BindView(R.id.deletePassword2)
    public RelativeLayout deletePassword2;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    private TextChange textChange;

    @Override
    protected ForgetPwd2Presenter createPresenter() {
        return new ForgetPwd2Presenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_forget_pwd2);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        textChange = new TextChange();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void initListener() {
        password1.addTextChangedListener(textChange);
        password2.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private void initData() {
        forgetBtn.setEnabled(false);
        forgetBtn.setBackgroundResource(R.drawable.button_unenable);

        password1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        password1.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info637)));

        password2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        password2.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info637)));
    }

    @OnClick({R.id.forgetBtn, R.id.deletePassword1, R.id.deletePassword2})
    public void onCLickView(View view) {
        switch (view.getId()) {
            case R.id.forgetBtn:
                //forget password
                String pwd1 = password1.getText().toString().trim();
                String pwd2 = password2.getText().toString().trim();
                getPresenter().validatePassword(pwd1, pwd2);
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

    private TextChange.OnTextChange onTextChange = (editable) -> {
        if (haveString(password1) && haveString(password2)) {
            forgetBtn.setEnabled(true);
            forgetBtn.setBackgroundResource(R.drawable.selector_button);
        } else {
            forgetBtn.setEnabled(false);
            forgetBtn.setBackgroundResource(R.drawable.button_unenable);
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

    @Override
    public void onSuccess(Object data, String msg) {
        showToast(getString(R.string.info220));
        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.FORGETPWD, getString(R.string.info220)));
        finish();
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void passwordNotEmpty() {
        showToast(getString(R.string.info221));
    }

    @Override
    public void passwordNotComplex() {
        showToast(getString(R.string.info222));
    }

    @Override
    public void passwordNotSame() {
        showToast(getString(R.string.info223));
    }

    @Override
    public void beginNewPwd() {
        String userName = getIntent().getStringExtra(Constants.USERNAME);
        String pwd1 = password1.getText().toString().trim();
        try {
            getPresenter().forgetPassword(
                    userName,
                    pwd1,
                    this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
