package com.io.sdchain.ui.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.AccountBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.mvp.presenter.KeyPresenter;
import com.io.sdchain.mvp.view.KeyView;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.FileUtils;
import com.io.sdchain.utils.PermissionUtils;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bertsir.zbar.QRUtils;

@Route(path = ARouterPath.KeyActivity)
public final class KeyActivity extends BaseActivity<KeyPresenter> implements KeyView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.realName)
    public TextView realName;
    @BindView(R.id.userName)
    public TextView userName;
    @BindView(R.id.imageCode)
    public ImageView imageCode;
    @BindView(R.id.doKey)
    public LinearLayout doKey;
    @BindView(R.id.attention)
    public TextView attention;
    private String loginPwd = "";
    private ClipboardManager cm;
    private UserBean userInfo;
    private Bitmap keyBitmap;
    private String keyStr = "";
    private boolean hasGetSecret = false;

    @Override
    protected KeyPresenter createPresenter() {
        return new KeyPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setExitTransition(new Explode());
        }

        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info255));
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    @OnClick({R.id.watch, R.id.copyBtn, R.id.saveBtn})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.key:
                //key
                break;
            case R.id.watch:
                if (!hasGetSecret) {
                    //click check
                    new XyDialog2.Builder(KeyActivity.this)
                            .title(getString(R.string.info256))
                            .isNumber(true)
                            .digit(6)
                            .setPositiveButtonListener(getString(R.string.info257), (EditText input, Dialog dialog, int i) -> {
                                loginPwd = getEditString(input);
                                if (loginPwd.length() != 6) {
                                    showToast(getString(R.string.info258));
                                    return;
                                }
                                UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                                getPresenter().getWalletSecret(
                                        userBean.getId(),
                                        userBean.getUserAccountId(),
                                        loginPwd,
                                        AesUtil2.encryptPOST(userBean.getId()),
                                        this);
                                dialog.dismiss();
                            })
                            .createPwd2Dialog()
                            .show();
                }
                break;
            case R.id.copyBtn:
                cm.setPrimaryClip(ClipData.newPlainText(null, keyStr));
                showToast(getString(R.string.info259));
                break;
            case R.id.saveBtn:
                new PermissionUtils().getInstance(KeyActivity.this)
                        .permissions(Permission.CAMERA)
                        .errHint(getString(R.string.info260))
                        .permission(permissions -> {
                            String path = Environment.getExternalStorageDirectory() + Constants.FILENAME + userInfo.getUserName() + Constants.FILEACCOUNTNAME + userInfo.getAccount() + "/";
                            FileUtils.saveBitmap(keyBitmap, Constants.PRIVETEKEYNAME + userInfo.getAccount(), path);
                            showToast(getString(R.string.info328) + path + getString(R.string.info329));
                        }).start();
                break;
            default:
                break;
        }
    }

    private void initData() {
        userInfo = (UserBean) tools.getObjectData(Constants.USERINFO);
        if (userInfo.getWalletName() != null && !userInfo.getWalletName().equals("")) {
            realName.setText(userInfo.getWalletName());
        } else {
            realName.setText(getString(R.string.info261));
        }

        if (userInfo.getUserName() != null && !userInfo.getUserName().equals("")) {
            userName.setText(userInfo.getUserName());
        } else {
            userName.setText("");
        }
    }

    public void saveBitmap(Bitmap bitmap, String name) throws IOException {
        String path = Environment.getExternalStorageDirectory() + Constants.FILENAME + userInfo.getUserName() + Constants.FILEACCOUNTNAME + userInfo.getAccount() + "/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(path, name + ".jpg");
        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent);

        showToast(getString(R.string.info262) + path + getString(R.string.info263));
    }

    @Override
    public void onSuccess(Object data, String msg) {
        Logger.e("Key success!!!");
        AccountBean accountInfo = (AccountBean) data;
        keyStr = accountInfo.getSecret();
        //Generate public key qr code
        keyBitmap = QRUtils.getInstance().createQRCodeAddLogo(Constants.PRIVATEKEY + keyStr, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_privatekey_logo));
        imageCode.setImageBitmap(keyBitmap);
        hasGetSecret = true;
        doKey.setVisibility(View.VISIBLE);
        attention.setVisibility(View.GONE);
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
        hasGetSecret = false;
        doKey.setVisibility(View.GONE);
    }

}
