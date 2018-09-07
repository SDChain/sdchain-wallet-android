package com.io.sdchain.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.CodeBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.RegisterPhonePresenter;
import com.io.sdchain.mvp.view.RegisterPhoneView;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.CheckFormat;
import com.io.sdchain.utils.ImageLoader;
import com.io.sdchain.utils.OnMultiClickListener;
import com.io.sdchain.utils.PermissionUtils;
import com.io.sdchain.utils.SaveObjectTools;
import com.io.sdchain.utils.TextChange;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;
import com.xiey94.xydialog.dialog.XySevDialog;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.RegisterPhoneActivity)
public final class RegisterPhoneActivity extends BaseActivity<RegisterPhonePresenter> implements RegisterPhoneView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.timerDown)
    public TextView timerDown;
    @BindView(R.id.phoneRegisterBtn)
    public Button phoneRegisterBtn;
    @BindView(R.id.phone)
    public EditText phone;
    @BindView(R.id.code)
    public EditText code;
    @BindView(R.id.deletePhone)
    public RelativeLayout deletePhone;
    @BindView(R.id.deleteCode)
    public RelativeLayout deleteCode;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    private TextChange textChange;
    private String phoneNumber = "";

    @Override
    protected RegisterPhonePresenter createPresenter() {
        return new RegisterPhonePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_register_phone);
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
        phone.addTextChangedListener(textChange);
        code.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
        toolbar.setNavigationOnClickListener((v) -> finish());
        phoneRegisterBtn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //next step
                SaveObjectTools tools = new SaveObjectTools(RegisterPhoneActivity.this);
                CodeBean codeBean = (CodeBean) tools.getObjectData(Constants.CODEINFO);
                if (codeBean != null) {
                    codeBean.setSmsCode(getEditString(code));
                    getPresenter().validatePhone(
                            codeBean,
                            AesUtil2.encryptGET(codeBean.getUserName()),
                            RegisterPhoneActivity.this);
                } else {
                    showToast(getString(R.string.info299));
                }
            }
        });
    }

    private void initData() {
        phoneRegisterBtn.setEnabled(false);
        phoneRegisterBtn.setBackgroundResource(R.drawable.button_unenable);

        code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        code.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info649)));

    }


    private XySevDialog dialog;
    private XyDialog2 progressDialog;

    @OnClick({R.id.timerDown, R.id.deletePhone, R.id.deleteCode, R.id.email_register})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.timerDown:
                if (!CheckFormat.isMobile(getEditString(phone))) {
                    showToast(getString(R.string.info296));
                    return;
                }

                new PermissionUtils().getInstance(RegisterPhoneActivity.this)
                        .permissions(Permission.READ_PHONE_STATE)
                        .errHint(getString(R.string.info297))
                        .permission(permissions -> {
                            getPresenter().getImageCode(false, this);
                            if (null == progressDialog) {
                                progressDialog = new XyDialog2.Builder(RegisterPhoneActivity.this)
                                        .message(getString(R.string.info298))
                                        .createProgressDialog();
                            }
                            progressDialog.show();
                        }).start();
                break;

            case R.id.deletePhone:
                phone.setText("");
                break;
            case R.id.deleteCode:
                code.setText("");
                break;
            case R.id.email_register:
                ARouter.getInstance()
                        .build(ARouterPath.RegisterEmailActivity)
                        .withTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        .navigation(RegisterPhoneActivity.this, new NavCallback() {
                            @Override
                            public void onArrival(Postcard postcard) {
                                finish();
                            }
                        });
                break;
            default:
                break;
        }
    }

    private TextChange.OnTextChange onTextChange = (editable) -> {
        if (haveString(phone) && haveString(code)) {
            phoneRegisterBtn.setEnabled(true);
            phoneRegisterBtn.setBackgroundResource(R.drawable.selector_button);
        } else {
            phoneRegisterBtn.setEnabled(false);
            phoneRegisterBtn.setBackgroundResource(R.drawable.button_unenable);
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

    };

    @Override
    public void onSuccess(Object data, String msg) {
        ARouter.getInstance()
                .build(ARouterPath.RegisterPhone2Activity)
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
        timerDown.setText(getString(R.string.info300));
    }

    @Override
    public void onCodeSuccess(Object data, String msg) {
        CodeBean codeInfo = (CodeBean) data;
        codeInfo.setUserName(getEditString(phone));
        codeInfo.setSmsCode(getEditString(code));
        tools.saveData(codeInfo, Constants.CODEINFO);
        showToast(getString(R.string.info301));
        if (null != dialog) {
            dialog.dismiss();
        }
    }

    @Override
    public void onCodeFailed(String msg) {
        showToast(msg);
        updateTimeOver();
        if (null != dialog) {
            dialog.stopRefresh();
        }
        getPresenter().closeDisposable();
    }

    @Override
    public void onGetCodeSuccess(Object data, String msg) {
        String base64ImageUrl = (String) data;
        //succeed
        if (null == dialog) {
            dialog = new XySevDialog.Builder(this)
                    .hint(getString(R.string.info302))
                    .setSevListener(new XySevDialog.OnSevListener() {
                        @Override
                        public void sevComplete() {
                            Logger.e("code---" + dialog.getSecurityCode().getEditContent());
                            phoneNumber = phone.getText().toString().trim();
                            getPresenter().getCodeByPhone(
                                    phoneNumber,
                                    "0",
                                    dialog.getSecurityCode().getEditContent(),
                                    AesUtil2.encryptGET(phoneNumber),
                                    RegisterPhoneActivity.this);
                            getPresenter().timeDown();
                            dialog.dismiss();
                        }

                        @Override
                        public void sevDeleteContent(boolean b) {

                        }

                        @Override
                        public void sevRefresh(XySevDialog xySevDialog) {
                            xySevDialog.getSecurityCode().clearEditText();
                            getPresenter().getImageCode(false, RegisterPhoneActivity.this);
                        }
                    })
                    .createSeVDialog();
        }
        dialog.getSecurityCode().clearEditText();

        dialog.getSecurityCode().getEditText().setFocusable(true);
        dialog.getSecurityCode().getEditText().setFocusableInTouchMode(true);
        dialog.getSecurityCode().getEditText().requestFocus();
        dialog.getSecurityCode().getEditText().post(() -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
            inputMethodManager.toggleSoftInput(0, 2);
        });

        if (null != progressDialog) {
            progressDialog.dismiss();
        }
        dialog.show();
        ImageLoader.loadBase64Image(base64ImageUrl, dialog.getImageView());
        dialog.stopRefresh();
    }

    @Override
    public void onGetCodeFailed(String msg) {
        if (null != dialog) {
            dialog.stopRefresh();
        }
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
        showToast(getString(R.string.info303));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().closeDisposable();
        EventBus.getDefault().unregister(true);
    }

    /**
     * Registered successfully, delete current interface
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterOK(EventBusType type) {
        if (type.getBusType() == EventBusType.BusType.REGISTBYPHONE) {
            finish();
        }
    }

}
