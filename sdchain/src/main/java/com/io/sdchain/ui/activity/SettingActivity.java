package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.utils.CheckFormat;
import com.io.sdchain.utils.LanguageUtils;
import com.xiey94.xydialog.dialog.XyDialog2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.SettingActivity)
public final class SettingActivity extends BaseActivity {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    private String account = "";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info313));
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    @OnClick({R.id.changeLoginPwd,  R.id.aboutUs})
    public void onCliCkView(View view) {
        switch (view.getId()) {
            case R.id.changeLoginPwd:
                //Change login password
                changeWay(getString(R.string.info314), R.array.changeWay_login);
                break;
            case R.id.aboutUs:
                //About Us
                String country = LanguageUtils.getCountry(SettingActivity.this);
                ARouter.getInstance()
                        .build(ARouterPath.WebViewActivity)
                        .withString(ARouterPath.FROM, ARouterPath.SettingActivity)
                        .withString(ARouterPath.DATA, country)
                        .withString(Constants.TITLE, getString(R.string.info134))
                        .navigation();
                break;
            default:
                break;
        }
    }

    //selector mode
    private void changeWay(String title, int resArray) {
        new XyDialog2.Builder(this)
                .title(title)
                .setPositiveButtonListener(resArray, (textView, dialog, which) -> {
                    UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                    switch (which) {
                        case 0:
                            //Mobile phone way
                            account = "0";
                            if (CheckFormat.isMobile(userBean.getUserName()) || !TextUtils.isEmpty(userBean.getPhone())) {
                                //The user name is the phone number and is bound to the phone number
                                ARouter.getInstance()
                                        .build(ARouterPath.SetLoginPwdActivity)
                                        .withString(ARouterPath.DATA, account)
                                        .navigation();
                            } else {
                                showToast(getString(R.string.info315));
                            }
                            break;
                        case 1:
                            //Mail the alchemist
                            account = "1";
                            if (CheckFormat.checkEmail(userBean.getUserName()) || (userBean.getEmail() != null && !userBean.getEmail().equals(""))) {
                                //The user name is the phone number and is bound to the phone number
                                ARouter.getInstance()
                                        .build(ARouterPath.SetLoginPwdActivity)
                                        .withString(ARouterPath.DATA, account)
                                        .navigation();
                            } else {
                                showToast(getString(R.string.info316));
                            }
                            break;
                        default:
                            break;
                    }
                    dialog.dismiss();

                })
                .createChooseButton()
                .show();
    }

    @Override
    public void onSuccess(Object data, String msg) {

    }

    @Override
    public void onFailed(String msg) {

    }
}
