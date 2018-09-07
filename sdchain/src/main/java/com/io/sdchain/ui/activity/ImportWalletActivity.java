package com.io.sdchain.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.io.sdchain.bean.ImportWalletBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.ImportWalletPresenter;
import com.io.sdchain.mvp.view.ImportWalletView;
import com.io.sdchain.utils.PermissionUtils;
import com.io.sdchain.utils.QrUtils;
import com.io.sdchain.utils.TextChange;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.ImportWalletActivity)
public final class ImportWalletActivity extends BaseActivity<ImportWalletPresenter> implements ImportWalletView {

    @BindView(R.id.account)
    public EditText account;
    @BindView(R.id.secret)
    public EditText secret;
    @BindView(R.id.deleteAccount)
    public RelativeLayout deleteAccount;
    @BindView(R.id.deleteSecret)
    public RelativeLayout deleteSecret;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.btn_import)
    public Button btn_import;
    private TextChange textChange;
    private UserBean userBean;

    @Override
    protected ImportWalletPresenter createPresenter() {
        return new ImportWalletPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_wallet);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info249));
        textChange = new TextChange();
        account.addTextChangedListener(textChange);
        secret.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private void initData() {
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        btn_import.setEnabled(false);
        btn_import.setBackgroundResource(R.drawable.button_unenable);
    }

    @OnClick({R.id.deleteAccount, R.id.deleteSecret, R.id.scanAccount, R.id.scanSecret, R.id.btn_import})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.btn_import:
                new XyDialog2.Builder(ImportWalletActivity.this)
                        .title(getString(R.string.info250))
                        .digit(6)
                        .isNumber(true)
                        .setPositiveButtonListener(getString(R.string.info251), (EditText editText, Dialog dialog, int i) -> {
                            String accountPwd = editText.getText().toString().trim();
                            getPresenter().importWallet(
                                    userBean.getId(),
                                    getEditString(account),
                                    getEditString(secret),
                                    accountPwd,
                                    ImportWalletActivity.this,
                                    true);
                            dialog.dismiss();
                        })
                        .setNegativeButtonListener(getString(R.string.info252), (o, dialog, i) -> {
                            dialog.dismiss();

                        })
                        .createPwd2Dialog()
                        .show();
                break;
            case R.id.deleteAccount:
                account.setText("");
                break;
            case R.id.deleteSecret:
                secret.setText("");
                break;
            case R.id.scanAccount:
                new PermissionUtils().getInstance(ImportWalletActivity.this)
                        .permissions(Permission.CAMERA)
                        .errHint(getString(R.string.info253))
                        .permission(permissions -> {
                            scan();
                        }).start();
                break;
            case R.id.scanSecret:
                new PermissionUtils().getInstance(ImportWalletActivity.this)
                        .permissions(Permission.CAMERA)
                        .errHint(getString(R.string.info254))
                        .permission(permissions -> {
                            scan();
                        }).start();
                break;
            default:
                break;
        }
    }

    private void scan() {
        QrUtils.getInstance().startScan(ImportWalletActivity.this, result -> {
            Logger.d("Scan succeeded：" + result);
            if (result.startsWith(Constants.PRIVATEKEY)) {
                //Gets the private key and displays it
                String key = result.substring(Constants.PRIVATEKEY.length(), result.length());
                secret.setText(key);
            } else if (result.startsWith(Constants.ACCOUNT)) {
                //Gets the private key and displays it
                String accountStr = result.substring(Constants.ACCOUNT.length(), result.length());
                account.setText(accountStr);
            } else {
                ARouter.getInstance()
                        .build(ARouterPath.BadCodeResultActivity)
                        .withString(Constants.BADCODE, result)
                        .navigation();

            }
        });
    }

    private TextChange.OnTextChange onTextChange = editable -> {
        if (haveString(account) && haveString(secret)) {
            btn_import.setEnabled(true);
            btn_import.setBackgroundResource(R.drawable.selector_button);
        } else {
            btn_import.setEnabled(false);
            btn_import.setBackgroundResource(R.drawable.button_unenable);
        }

        if (haveString(account)) {
            deleteAccount.setVisibility(View.VISIBLE);
        } else {
            deleteAccount.setVisibility(View.GONE);
        }

        if (haveString(secret)) {
            deleteSecret.setVisibility(View.VISIBLE);
        } else {
            deleteSecret.setVisibility(View.GONE);
        }
    };

    @Override
    public void onSuccess(Object data, String msg) {
        //1、Home data change；2、current page finish
        showToast(msg);
        ImportWalletBean importWalletBean = (ImportWalletBean) data;
        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.IMPORTWALLETSUCCESS, importWalletBean));
        finish();
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }

}
