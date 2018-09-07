package com.io.sdchain.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.AccountBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.view.CreateAccountView;
import com.io.sdchain.utils.FileUtils;
import com.io.sdchain.utils.PermissionUtils;
import com.xiey94.xydialog.dialog.XyDialog2;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bertsir.zbar.QRUtils;

@Route(path = ARouterPath.CreateAccountActivity)
public final class CreateAccountActivity extends BaseActivity implements CreateAccountView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.back)
    public FrameLayout back;
    @BindView(R.id.right)
    public TextView right;
    @BindView(R.id.account)
    public TextView account;
    @BindView(R.id.secret)
    public TextView secret;
    @BindView(R.id.accountBtn)
    public TextView accountBtn;
    @BindView(R.id.secretBtn)
    public TextView secretBtn;
    @BindView(R.id.accountImage)
    public ImageView accountImage;
    @BindView(R.id.secretImage)
    public ImageView secretImage;
    private Bitmap accountBitmap, secretBitmap;
    private AccountBean accountBean;
    private ClipboardManager cm;
    private UserBean userBean;
    private String from;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        title.setText(getString(R.string.info651));
        accountBean = (AccountBean) getIntent().getSerializableExtra(Constants.ACCOUNTINFO);
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    private void initListener() {
        accountImage.setOnLongClickListener(onLongClickListener);
        secretImage.setOnLongClickListener(onLongClickListener);
    }

    private void initData() {
        from = getIntent().getStringExtra(ARouterPath.FROM);
        if (!TextUtils.isEmpty(from)) {
            if (from.equals(ARouterPath.RegisterPhone2Activity) || from.equals(ARouterPath.RegisterEmail2Activity)) {
                right.setVisibility(View.VISIBLE);
                right.setText(getString(R.string.info650));
                back.setVisibility(View.INVISIBLE);
            } else {
                right.setVisibility(View.INVISIBLE);
                back.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new EventBusType(EventBusType.BusType.CREATEACCOUNTSUCCESS, accountBean));
            }
        }

        account.setText(accountBean.getAccount());
        secret.setText(accountBean.getSecret());
        //Generate public key qr code
        accountBitmap = QRUtils.getInstance().createQRCodeAddLogo(Constants.ACCOUNT + accountBean.getAccount(), BitmapFactory.decodeResource(getResources(), R.mipmap.ic_user_logo));
        accountImage.setImageBitmap(accountBitmap);
        //Generate the private key qr code
        secretBitmap = QRUtils.getInstance().createQRCodeAddLogo(Constants.PRIVATEKEY + accountBean.getSecret(), BitmapFactory.decodeResource(getResources(), R.mipmap.ic_privatekey_logo));
        secretImage.setImageBitmap(secretBitmap);
    }

    @OnClick({R.id.accountBtn, R.id.secretBtn, R.id.back, R.id.right})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.accountBtn:
                cm.setPrimaryClip(ClipData.newPlainText(null, account.getText().toString().trim()));
                showToast(getString(R.string.info203));
                break;
            case R.id.secretBtn:
                cm.setPrimaryClip(ClipData.newPlainText(null, secret.getText().toString().trim()));
                showToast(getString(R.string.info204));
                break;
            case R.id.back:
                finish();
                break;
            case R.id.right:
                new XyDialog2.Builder(this)
                        .title(getString(R.string.info209))
                        .message(getString(R.string.info210))
                        .setPositiveButtonListener(getString(R.string.info211), (o, dialog, i) -> {
                            dialog.dismiss();
                            ARouter.getInstance()
                                    .build(ARouterPath.LoginActivity)
                                    .withString(ARouterPath.FROM, ARouterPath.CreateAccountActivity)
                                    .navigation(this, new NavCallback() {
                                        @Override
                                        public void onArrival(Postcard postcard) {
                                            finish();
                                        }
                                    });
                        })
                        .setNegativeButtonListener(getString(R.string.info212), (o, dialog, i) -> {
                            dialog.dismiss();
                        })
                        .createNoticeDialog()
                        .show();
                break;
            default:
                break;
        }
    }

    private View.OnLongClickListener onLongClickListener = view -> {
        switch (view.getId()) {
            case R.id.accountImage:
                new PermissionUtils().getInstance(CreateAccountActivity.this)
                        .permissions(Permission.WRITE_EXTERNAL_STORAGE)
                        .errHint(getString(R.string.info205))
                        .permission(permissions -> {
                            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                            String path = Environment.getExternalStorageDirectory() + Constants.FILENAME + userBean.getUserName() +Constants.FILEACCOUNTNAME+ accountBean.getAccount() + "/";
                            FileUtils.saveBitmap(accountBitmap, Constants.ACCOUNTNAME + accountBean.getAccount(), path);
                            showToast(getString(R.string.info328) + path + getString(R.string.info329));
                        }).start();
                break;
            case R.id.secretImage:
                new PermissionUtils().getInstance(CreateAccountActivity.this)
                        .permissions(Permission.WRITE_EXTERNAL_STORAGE)
                        .errHint(getString(R.string.info206))
                        .permission(permissions -> {
                            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                            String path = Environment.getExternalStorageDirectory() + Constants.FILENAME  + userBean.getUserName() + Constants.FILEACCOUNTNAME+ accountBean.getAccount() + "/";
                            FileUtils.saveBitmap(secretBitmap, Constants.PRIVETEKEYNAME + accountBean.getAccount(), path);
                            showToast(getString(R.string.info328) + path + getString(R.string.info329));
                        }).start();
                break;
            default:
                break;
        }
        return false;
    };

    @Override
    public void onSuccess(Object data, String msg) {
    }

    @Override
    public void onFailed(String msg) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            attention();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Reminder to save the public key private key
     */
    private void attention() {
        new XyDialog2.Builder(this)
                .title(getString(R.string.info209))
                .message(getString(R.string.info210))
                .setPositiveButtonListener(getString(R.string.info211), (o, dialog, i) -> {
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButtonListener(getString(R.string.info212), (o, dialog, i) -> {
                    dialog.dismiss();
                })
                .createNoticeDialog()
                .show();
    }
}
