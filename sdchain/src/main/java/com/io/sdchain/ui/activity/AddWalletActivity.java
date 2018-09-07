package com.io.sdchain.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.AccountBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.mvp.presenter.AddWalletPresenter;
import com.io.sdchain.mvp.view.AddWalletView;
import com.io.sdchain.utils.AesUtil2;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.AddWalletActivity)
public final class AddWalletActivity extends BaseActivity<AddWalletPresenter> implements AddWalletView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected AddWalletPresenter createPresenter() {
        return new AddWalletPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info36));
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    @OnClick({R.id.btn_create_wallet, R.id.btn_import_wallet})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.btn_create_wallet:
                new XyDialog2.Builder(AddWalletActivity.this)
                        .title(getString(R.string.info37))
                        .digit(6)
                        .isNumber(true)
                        .setPositiveButtonListener(getString(R.string.info38), (EditText editText, Dialog dialog, int i) -> {
                            String accountPwd = editText.getText().toString().trim();
                            UserBean userInfo = (UserBean) tools.getObjectData(Constants.USERINFO);
                            getPresenter().createAccount(
                                    userInfo.getId(),
                                    accountPwd,
                                    AesUtil2.encryptPOST(userInfo.getId()),
                                    AddWalletActivity.this,
                                    true);
                            dialog.dismiss();
                        })
                        .setNegativeButtonListener(getString(R.string.info39), (o, dialog, i) -> {
                            dialog.dismiss();

                        })
                        .createPwd2Dialog()
                        .show();
                break;
            case R.id.btn_import_wallet:
                ARouter.getInstance()
                        .build(ARouterPath.ImportWalletActivity)
                        .navigation();
                break;
            default:
                break;
        }
    }

    ;

    private void initData() {

    }

    @Override
    public void onSuccess(Object data, String msg) {
        //Create wallet successfully
        AccountBean accountBean = (AccountBean) data;
        ARouter.getInstance()
                .build(ARouterPath.CreateAccountActivity)
                .withString(ARouterPath.FROM, ARouterPath.AddWalletActivity)
                .withSerializable(Constants.ACCOUNTINFO, accountBean)
                .navigation();

    }

    @Override
    public void onFailed(String msg) {
        //Wallet creation failed
        Logger.e("-----Created wallet failed-----");
        showToast(msg);
    }
}
