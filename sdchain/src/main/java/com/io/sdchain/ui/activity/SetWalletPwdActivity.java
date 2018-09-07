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
import com.io.sdchain.mvp.presenter.SetWalletPwdPresenter;
import com.io.sdchain.mvp.view.SetWalletPwdView;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.TextChange;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.SetWalletPwdActivity)
public final class SetWalletPwdActivity extends BaseActivity<SetWalletPwdPresenter> implements SetWalletPwdView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.walletPwd)
    public EditText walletPwd;
    @BindView(R.id.password1)
    public EditText password1;
    @BindView(R.id.password2)
    public EditText password2;
    @BindView(R.id.changeWalletPwdBtn)
    public Button changeWalletPwdBtn;
    @BindView(R.id.deleteWalletpwd)
    public RelativeLayout deleteWalletpwd;
    @BindView(R.id.deletePassword1)
    public RelativeLayout deletePassword1;
    @BindView(R.id.deletePassword2)
    public RelativeLayout deletePassword2;
    @BindView(R.id.right)
    public TextView right;
    private TextChange textChange;

    @Override
    protected SetWalletPwdPresenter createPresenter() {
        return new SetWalletPwdPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallet_pwd);
        ButterKnife.bind(this);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info317));
        textChange = new TextChange();
    }

    private void initListener() {
        walletPwd.addTextChangedListener(textChange);
        password1.addTextChangedListener(textChange);
        password2.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private void initData() {
        changeWalletPwdBtn.setEnabled(false);
        changeWalletPwdBtn.setBackgroundResource(R.drawable.button_unenable);

        walletPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        walletPwd.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info649)));

        password1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        password1.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info649)));

        password2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        password2.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.info649)));
    }

    @OnClick({R.id.changeWalletPwdBtn, R.id.deleteWalletpwd, R.id.deletePassword1, R.id.deletePassword2, R.id.right})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.changeWalletPwdBtn:
                getPresenter().validatePassword(getEditString(password1), getEditString(password2));
                break;
            case R.id.deleteWalletpwd:
                walletPwd.setText("");
                break;
            case R.id.deletePassword1:
                password1.setText("");
                break;
            case R.id.deletePassword2:
                password2.setText("");
                break;
            case R.id.right:
                ARouter.getInstance()
                        .build(ARouterPath.ForgetWalletPwdActivity)
                        .navigation();
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
    public void passwordNotEmpty() {
        showToast(getString(R.string.info318));
    }

    @Override
    public void passwordNotComplex() {
        showToast(getString(R.string.info319));
    }

    @Override
    public void passwordNotSame() {
        showToast(getString(R.string.info320));
    }

    @Override
    public void beginChange() {
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        //Change login password
        getPresenter().changeWalletPwd(
                userBean.getId(),
                userBean.getUserAccountId(),
                getEditString(walletPwd),
                getEditString(password1),
                AesUtil2.encryptPOST(userBean.getId()),
                this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private TextChange.OnTextChange onTextChange = (editable) -> {
        if (haveString(walletPwd) && haveString(password1) && haveString(password2)) {
            changeWalletPwdBtn.setEnabled(true);
            changeWalletPwdBtn.setBackgroundResource(R.drawable.selector_button);
        } else {
            changeWalletPwdBtn.setEnabled(false);
            changeWalletPwdBtn.setBackgroundResource(R.drawable.button_unenable);
        }

        if (haveString(walletPwd)) {
            deleteWalletpwd.setVisibility(View.VISIBLE);
        } else {
            deleteWalletpwd.setVisibility(View.GONE);
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
