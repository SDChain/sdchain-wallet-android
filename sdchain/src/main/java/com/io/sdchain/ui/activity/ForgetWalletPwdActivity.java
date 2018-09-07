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
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.mvp.presenter.ForgetWalletPwdPresenter;
import com.io.sdchain.mvp.view.ForgetWalletPwdView;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.PermissionUtils;
import com.io.sdchain.utils.QrUtils;
import com.io.sdchain.utils.TextChange;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.permission.Permission;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.ForgetWalletPwdActivity)
public final class ForgetWalletPwdActivity extends BaseActivity<ForgetWalletPwdPresenter> implements ForgetWalletPwdView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.secret)
    public EditText secret;
    @BindView(R.id.password1)
    public EditText password1;
    @BindView(R.id.password2)
    public EditText password2;
    @BindView(R.id.forgetWalletPwdBtn)
    public Button forgetWalletPwdBtn;
    @BindView(R.id.deleteSecret)
    public RelativeLayout deleteSecret;
    @BindView(R.id.deletePassword1)
    public RelativeLayout deletePassword1;
    @BindView(R.id.deletePassword2)
    public RelativeLayout deletePassword2;
    public TextChange textChange;

    @Override
    protected ForgetWalletPwdPresenter createPresenter() {
        return new ForgetWalletPwdPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_wallet_pwd);
        ButterKnife.bind(this);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info229));
        textChange = new TextChange();
    }

    private void initListener() {
        secret.addTextChangedListener(textChange);
        password1.addTextChangedListener(textChange);
        password2.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private void initData() {
        forgetWalletPwdBtn.setEnabled(false);
        forgetWalletPwdBtn.setBackgroundResource(R.drawable.button_unenable);

        password1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        password1.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info649)));

        password2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        password2.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info649)));
    }

    @OnClick({R.id.forgetWalletPwdBtn, R.id.deleteSecret, R.id.deletePassword1, R.id.deletePassword2, R.id.scan})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.forgetWalletPwdBtn:
                getPresenter().validatePassword(getEditString(password1), getEditString(password2));
                break;
            case R.id.deleteSecret:
                secret.setText("");
                break;
            case R.id.deletePassword1:
                password1.setText("");
                break;
            case R.id.deletePassword2:
                password2.setText("");
                break;
            case R.id.scan:
                new PermissionUtils().getInstance(ForgetWalletPwdActivity.this)
                        .permissions(Permission.CAMERA)
                        .errHint(getString(R.string.info230))
                        .permission(permissions -> {
                            scan();
                        }).start();
                break;
            default:
                break;
        }
    }

    private void scan() {
        QrUtils.getInstance().startScan(ForgetWalletPwdActivity.this, result -> {
            Logger.d("Scan succeeded：" + result);
            if (result.startsWith(Constants.PRIVATEKEY)) {
                //Gets the private key and displays it
                String key = result.substring(Constants.PRIVATEKEY.length(), result.length());
                secret.setText(key);
            } else {
                ARouter.getInstance()
                        .build(ARouterPath.BadCodeResultActivity)
                        .withString(Constants.BADCODE, result)
                        .navigation();
            }
        });
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
    public void passwordNotEmpty() {
        showToast(getString(R.string.info231));
    }

    @Override
    public void passwordNotComplex() {
        showToast(getString(R.string.info232));
    }

    @Override
    public void passwordNotSame() {
        showToast(getString(R.string.info233));
    }

    @Override
    public void beginChange() {
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        //change login password
        getPresenter().forgetWalletPwd(
                userBean.getId(),
                userBean.getUserAccountId(),
                getEditString(password1),
                getEditString(secret),
                AesUtil2.encryptPOST(userBean.getId()),
                this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private TextChange.OnTextChange onTextChange = (editable) -> {
        if (haveString(secret) && haveString(password1) && haveString(password2)) {
            forgetWalletPwdBtn.setEnabled(true);
            forgetWalletPwdBtn.setBackgroundResource(R.drawable.selector_button);
        } else {
            forgetWalletPwdBtn.setEnabled(false);
            forgetWalletPwdBtn.setBackgroundResource(R.drawable.button_unenable);
        }

        if (haveString(secret)) {
            deleteSecret.setVisibility(View.VISIBLE);
        } else {
            deleteSecret.setVisibility(View.GONE);
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
