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
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.CodeBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.ForgetPwdPresenter;
import com.io.sdchain.mvp.view.ForgetPwdView;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.CheckFormat;
import com.io.sdchain.utils.SaveObjectTools;
import com.io.sdchain.utils.TextChange;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.ForgetPwdActivity)
public final class ForgetPwdActivity extends BaseActivity<ForgetPwdPresenter> implements ForgetPwdView {

    @BindView(R.id.registerBtn)
    public Button registerBtn;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.timerDown)
    public TextView timerDown;
    @BindView(R.id.account)
    public EditText account;
    @BindView(R.id.code)
    public EditText code;
    @BindView(R.id.deleteAccount)
    public RelativeLayout deleteAccount;
    @BindView(R.id.deleteKey)
    public RelativeLayout deleteKey;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    private boolean canClick = true;
    private TextChange textChange;

    @Override
    protected ForgetPwdPresenter createPresenter() {
        return new ForgetPwdPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_forget_pwd);
        EventBus.getDefault().register(this);
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
        account.addTextChangedListener(textChange);
        code.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private void initData() {
        registerBtn.setEnabled(false);
        registerBtn.setBackgroundResource(R.drawable.button_unenable);

        code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        code.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info649)));
    }

    @OnClick({R.id.timerDown, R.id.registerBtn, R.id.deleteAccount, R.id.deleteKey})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.timerDown:
                //countdown
                if (canClick) {
                    if (CheckFormat.isMobile(getEditString(account))) {
                        getPresenter().timeDown();
                        canClick = false;
                        getPresenter().getCodeByPhone(
                                getEditString(account).toString().trim(),
                                "1",
                                AesUtil2.encryptGET(getEditString(account).toString().trim()),
                                ForgetPwdActivity.this);
                    } else if (CheckFormat.checkEmail(getEditString(account))) {
                        getPresenter().timeDown();
                        canClick = false;
                        getPresenter().getCodeByEmail(
                                getEditString(account).toString().trim(),
                                "1",
                                AesUtil2.encryptGET("1"),
                                ForgetPwdActivity.this);
                    } else {
                        showToast(getString(R.string.info224));
                    }
                }
                break;
            case R.id.registerBtn:
                //verify code
                SaveObjectTools tools = new SaveObjectTools(this);
                CodeBean codeBean = (CodeBean) tools.getObjectData(Constants.CODEINFO);
                if (CheckFormat.isMobile(getEditString(account))) {
                    if (codeBean != null) {
                        codeBean.setSmsCode(getEditString(code));
                        getPresenter().validatePhone(
                                codeBean,
                                AesUtil2.encryptGET(codeBean.getUserName()),
                                this);
                    } else {
                        showToast(getString(R.string.info225));
                    }
                } else if (CheckFormat.checkEmail(getEditString(account))) {
                    if (codeBean != null) {
                        codeBean.setSmsCode(getEditString(code));
                        getPresenter().validateEmail(
                                codeBean,
                                AesUtil2.encryptGET(codeBean.getSmsId()),
                                this);
                    } else {
                        showToast(getString(R.string.info226));
                    }
                } else {
                    showToast(getString(R.string.info224));
                }
                break;
            case R.id.deleteAccount:
                account.setText("");
                break;
            case R.id.deleteKey:
                code.setText("");
                break;
            default:
                break;
        }
    }

    private TextChange.OnTextChange onTextChange = (editable) -> {
        if (haveString(account) && haveString(code)) {
            registerBtn.setEnabled(true);
            registerBtn.setBackgroundResource(R.drawable.selector_button);
        } else {
            registerBtn.setEnabled(false);
            registerBtn.setBackgroundResource(R.drawable.button_unenable);
        }

        if (haveString(account)) {
            deleteAccount.setVisibility(View.VISIBLE);
        } else {
            deleteAccount.setVisibility(View.GONE);
        }

        if (haveString(code)) {
            deleteKey.setVisibility(View.VISIBLE);
        } else {
            deleteKey.setVisibility(View.GONE);
        }

    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ForgetPwdOk(EventBusType type) {
        if (type.getBusType() == EventBusType.BusType.FORGETPWD) {
            finish();
        }
    }

    @Override
    public void onSuccess(Object data, String msg) {
        ARouter.getInstance()
                .build(ARouterPath.ForgetPwd2Activity)
                .withString(Constants.USERNAME, account.getText().toString().trim())
                .withTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                .navigation();
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
        timerDown.setText(getString(R.string.info227));
        canClick = true;
    }

    @Override
    public void onCodeSuccess(Object data, String msg) {
        CodeBean codeInfo = (CodeBean) data;
        codeInfo.setSmsCode(getEditString(code));
        codeInfo.setUserName(getEditString(account));
        tools.saveData(codeInfo, Constants.CODEINFO);
        showToast(getString(R.string.info228));
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
        EventBus.getDefault().unregister(true);
    }

}
