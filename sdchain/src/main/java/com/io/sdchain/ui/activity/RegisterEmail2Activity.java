package com.io.sdchain.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.io.sdchain.bean.AccountBean;
import com.io.sdchain.bean.CodeBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.RegisterEmail2Presenter;
import com.io.sdchain.mvp.view.RegisterEmail2View;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.LanguageUtils;
import com.io.sdchain.utils.SaveObjectTools;
import com.io.sdchain.utils.TextChange;
import com.io.sdchain.widget.SmoothCheckBox;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.RegisterEmail2Activity)
public final class RegisterEmail2Activity extends BaseActivity<RegisterEmail2Presenter> implements RegisterEmail2View {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.checkBox)
    public SmoothCheckBox checkBox;
    @BindView(R.id.emailRegisterBtn)
    public Button emailRegisterBtn;
    @BindView(R.id.password1)
    public EditText password1;
    @BindView(R.id.walletPassword)
    public EditText walletPassword;
    @BindView(R.id.deletePassword1)
    public RelativeLayout deletePassword1;
    @BindView(R.id.deletePassword2)
    public RelativeLayout deletePassword2;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.lookPwd)
    public CheckBox lookPwd;
    @BindView(R.id.lookWalletPwd)
    public CheckBox lookWalletPwd;
    private TextChange textChange;
    private boolean isChoose = false;

    @Override
    protected RegisterEmail2Presenter createPresenter() {
        return new RegisterEmail2Presenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_register_email2);
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
        checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        password1.addTextChangedListener(textChange);
        walletPassword.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
        toolbar.setNavigationOnClickListener((v) -> finish());
        lookPwd.setOnCheckedChangeListener(onEyesListener);
        lookWalletPwd.setOnCheckedChangeListener(onEyesListener);
    }

    private void initData() {
        emailRegisterBtn.setEnabled(false);
        emailRegisterBtn.setBackgroundResource(R.drawable.button_unenable);

        password1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        password1.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info637)));

        walletPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        walletPassword.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info649)));
    }

    private CompoundButton.OnCheckedChangeListener onEyesListener = (view, isChecked) -> {
        switch (view.getId()) {
            case R.id.lookPwd:
                changeEyes(password1, isChecked);
                break;
            case R.id.lookWalletPwd:
                changeEyes(walletPassword, isChecked);
                break;
            default:
                break;
        }
    };

    //The judgment shows the ciphertext in plain text
    private void changeEyes(EditText view, boolean isChecked) {
        if (isChecked) {
            //Set to clear text
            view.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //Set to ciphertext display
            view.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        //The cursor is displayed at the end
        view.setSelection(view.length());
    }

    @OnClick({R.id.emailRegisterBtn, R.id.deletePassword1, R.id.deletePassword2, R.id.agreement})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.emailRegisterBtn:
                //Sign up button
                String pwd1 = password1.getText().toString().trim();
                String walletPwd = walletPassword.getText().toString().trim();
                getPresenter().validatePassword(pwd1, walletPwd);
                break;
            case R.id.deletePassword1:
                password1.setText("");
                break;
            case R.id.deletePassword2:
                walletPassword.setText("");
                break;
            case R.id.agreement:
                String country = LanguageUtils.getCountry(RegisterEmail2Activity.this);
                ARouter.getInstance()
                        .build(ARouterPath.WebViewActivity)
                        .withString(ARouterPath.FROM, ARouterPath.RegisterPhone2Activity)
                        .withString(ARouterPath.DATA, country)
                        .withString(Constants.TITLE, getString(R.string.info133))
                        .navigation();
                break;
            default:
                break;
        }
    }

    private SmoothCheckBox.OnCheckedChangeListener onCheckedChangeListener = (checkBox, isChecked) -> {
        isChoose = isChecked;
        if (haveString(password1) && haveString(walletPassword) && isChoose) {
            emailRegisterBtn.setEnabled(true);
            emailRegisterBtn.setBackgroundResource(R.drawable.selector_button);
        } else {
            emailRegisterBtn.setEnabled(false);
            emailRegisterBtn.setBackgroundResource(R.drawable.button_unenable);
        }
    };

    private TextChange.OnTextChange onTextChange = (editable) -> {
        if (haveString(password1) && haveString(walletPassword) && isChoose) {
            emailRegisterBtn.setEnabled(true);
            emailRegisterBtn.setBackgroundResource(R.drawable.selector_button);
        } else {
            emailRegisterBtn.setEnabled(false);
            emailRegisterBtn.setBackgroundResource(R.drawable.button_unenable);
        }


        if (haveString(password1)) {
            deletePassword1.setVisibility(View.VISIBLE);
        } else {
            deletePassword1.setVisibility(View.GONE);
        }

        if (haveString(walletPassword)) {
            deletePassword2.setVisibility(View.VISIBLE);
        } else {
            deletePassword2.setVisibility(View.GONE);
        }

    };

    @Override
    public void onSuccess(Object data, String msg) {
        AccountBean accountBean= (AccountBean) data;
        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.REGISTBYEMAIL, "Registration was successful"));
        ARouter.getInstance()
                .build(ARouterPath.CreateAccountActivity)
                .withString(ARouterPath.FROM, ARouterPath.RegisterEmail2Activity)
                .withSerializable(Constants.ACCOUNTINFO, accountBean)
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void passwordNotEmpty() {
        showToast(getString(R.string.info279));
    }

    @Override
    public void passwordNotComplex() {
        showToast(getString(R.string.info280));
    }

    @Override
    public void passwordNotSame() {
        showToast(getString(R.string.info281));
    }

    @Override
    public void beginRegister() {
        SaveObjectTools tools = new SaveObjectTools(this);
        CodeBean codeBean = (CodeBean) tools.getObjectData(Constants.CODEINFO);
        if (codeBean == null) {
            showToast(getString(R.string.info282));
            return;
        }
        getPresenter().registByEmail(
                codeBean.getEmail(),
                password1.getText().toString().trim(),
                codeBean.getSmsId(),
                walletPassword.getText().toString().trim(),
                AesUtil2.encryptPOST(codeBean.getSmsId()),
                this
        );
    }
}
