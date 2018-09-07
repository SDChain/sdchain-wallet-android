package com.io.sdchain.ui.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.bean.WalletAddressBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.WalletAddressPresenter;
import com.io.sdchain.mvp.view.WalletAddressView;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.EditTextUtils;
import com.io.sdchain.utils.FileUtils;
import com.io.sdchain.utils.PermissionUtils;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bertsir.zbar.QRUtils;

@Route(path = ARouterPath.WalletAddressActivity)
public final class WalletAddressActivity extends BaseActivity<WalletAddressPresenter> implements WalletAddressView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.right)
    public TextView right;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.accountCode)
    public ImageView accountCode;
    @BindView(R.id.saveBtn)
    public Button saveBtn;
    @BindView(R.id.walletNickName)
    public EditText walletNickName;
    @BindView(R.id.allLayout)
    public CoordinatorLayout allLayout;
    @BindView(R.id.allLayout2)
    public LinearLayout allLayout2;
    @BindView(R.id.account)
    public TextView account;
    @BindView(R.id.edit)
    public FrameLayout edit;
    private String from;
    private ClipboardManager cm;
    private Bitmap accountBitmap;
    private WalletAddressBean walletAddressBean;
    private String nickName;

    @Override
    protected WalletAddressPresenter createPresenter() {
        return new WalletAddressPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_address);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Fade());
        }

        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info321));
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener((v) -> {
            if (nickName != null && !nickName.equals(walletNickName.getText().toString().trim())) {
                save();
            } else {
                onBackPressed();
            }
        });
        walletNickName.setOnFocusChangeListener(onFocusChangeListener);
        walletNickName.setOnEditorActionListener(onEditorActionListener);
        allLayout.setOnTouchListener(onTouchListener);
        allLayout2.setOnTouchListener(onTouchListener);
    }

    private View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Logger.e("get Focus");
            } else {
                Logger.e("lose focus");
                //Network request
                UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                String nickName = getEditString(walletNickName);

                if (nickName.equals("")) {
                    showToast(getString(R.string.info322));
                    return;
                }

                if (nickName.length() > 4) {
                    showToast(getString(R.string.info323));
                    return;
                }

                try {
                    nickName = URLEncoder.encode("" + nickName, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                getPresenter().changeAccountName(
                        userBean.getId(),
                        nickName,
                        userBean.getUserAccountId(),
                        AesUtil2.encryptGET(userBean.getId()),
                        false,
                        WalletAddressActivity.this);
            }
        }
    };

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Logger.e("Complete operation execution");
                //Network request
                UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                String nickName = getEditString(walletNickName);

                if (nickName.length() > 4) {
                    showToast(getString(R.string.info324));
                    return false;
                }

                try {
                    nickName = URLEncoder.encode("" + nickName, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                getPresenter().changeAccountName(
                        userBean.getId(),
                        nickName,
                        userBean.getUserAccountId(),
                        AesUtil2.encryptGET(userBean.getId()),
                        false,
                        WalletAddressActivity.this);
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(allLayout2.getWindowToken(), 0);
            allLayout2.setFocusable(true);
            allLayout2.setFocusableInTouchMode(true);
            allLayout2.requestFocus();
            return false;
        }
    };

    private View.OnTouchListener onTouchListener = (v, event) -> {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        return false;
    };

    private void initData() {
        allLayout2.setFocusable(true);
        allLayout2.setFocusableInTouchMode(true);
        allLayout2.requestFocus();
        from = getIntent().getStringExtra(ARouterPath.FROM);
        walletAddressBean = (WalletAddressBean) getIntent().getSerializableExtra(Constants.WALLETADDRESSINFO);
        if (from != null) {
            if (from.equals(ARouterPath.FriendDetailActivity)) {
                right.setVisibility(View.INVISIBLE);
                saveBtn.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                walletNickName.setEnabled(false);
                walletNickName.setText(walletAddressBean.getUserName());
                account.setText(walletAddressBean.getAccount());
            } else if (from.equals(ARouterPath.MineFragment)) {
                UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                Logger.e(userBean.getWalletName());
                if (userBean.getWalletName() != null) {
                    walletNickName.setText(userBean.getWalletName());
                    nickName = userBean.getWalletName();
                }
                account.setText(userBean.getAccount());
                right.setVisibility(View.VISIBLE);
                right.setText(getString(R.string.info325));
            }
        }

        //Generate public key qr code
        accountBitmap = QRUtils.getInstance().createQRCodeAddLogo(Constants.ACCOUNT + walletAddressBean.getAccount(), BitmapFactory.decodeResource(getResources(), R.mipmap.ic_account_logo));
        accountCode.setImageBitmap(accountBitmap);
    }

    @OnClick({R.id.right, R.id.copyBtn, R.id.saveBtn, R.id.edit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.right:
                //secret key
                ARouter.getInstance()
                        .build(ARouterPath.KeyActivity)
                        .withTransition(android.R.anim.fade_in, android.R.anim.fade_in)
                        .navigation();
                break;
            case R.id.copyBtn:
                cm.setPrimaryClip(ClipData.newPlainText(null, walletAddressBean.getAccount()));
                showToast(getString(R.string.info326));
                break;
            case R.id.saveBtn:
                new PermissionUtils().getInstance(WalletAddressActivity.this)
                        .permissions(Permission.WRITE_EXTERNAL_STORAGE)
                        .errHint(getString(R.string.info327))
                        .permission(permissions -> {
                            String path = Environment.getExternalStorageDirectory() + Constants.FILENAME + walletAddressBean.getUserName() + Constants.FILEACCOUNTNAME + walletAddressBean.getAccount() + "/";
                            FileUtils.saveBitmap(accountBitmap, Constants.ACCOUNTNAME + walletAddressBean.getAccount(), path);
                            showToast(getString(R.string.info328) + path + getString(R.string.info329));
                        }).start();
                break;
            case R.id.edit:
                EditTextUtils.showSoftInput2(walletNickName);
                break;
            default:
                break;
        }
    }

    ;

    @Override
    public void onSuccess(Object data, String msg) {
        walletNickName.setText((String) data);
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        userBean.setWalletName((String) data);
        nickName = (String) data;
        tools.saveData(userBean, Constants.USERINFO);
        //After the name is changed successfully, the home page list needs to be refreshed
        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.UPDATEWALLETNAME, null));
        if (isBack) {
            showToast(getString(R.string.info330));
            onBackPressed();
        }
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
        if (isBack) {
            isBack = false;
        }
    }

    private boolean isBack = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && nickName != null && !nickName.equals(walletNickName.getText().toString().trim())) {
            save();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void save() {
        new XyDialog2.Builder(WalletAddressActivity.this)
                .title(getString(R.string.info331))
                .message(getString(R.string.info332))
                .setPositiveButtonListener(getString(R.string.info333), new XyDialog2.OnNoticeClickListener<Object>() {
                    @Override
                    public void onNotice(Object o, Dialog dialog, int i) {
                        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                        String nickName = getEditString(walletNickName);

                        if (nickName.length() > 4) {
                            showToast(getString(R.string.info334));
                            return;
                        }

                        try {
                            nickName = URLEncoder.encode("" + nickName, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        isBack = true;
                        getPresenter().changeAccountName(
                                userBean.getId(),
                                nickName,
                                userBean.getUserAccountId(),
                                AesUtil2.encryptGET(userBean.getId()),
                                true,
                                WalletAddressActivity.this);
                        dialog.dismiss();
                    }
                })
                .setNegativeButtonListener(getString(R.string.info335), new XyDialog2.OnNoticeClickListener<Object>() {
                    @Override
                    public void onNotice(Object o, Dialog dialog, int i) {
                        onBackPressed();
                        dialog.dismiss();
                    }
                })
                .createNoticeDialog()
                .show();
    }
}
