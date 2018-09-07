package com.io.sdchain.ui.activity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.MinePresenter;
import com.io.sdchain.mvp.view.MineView;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.TextLimit;
import com.xiey94.xydialog.dialog.XyDialog2;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.MineActivity)
public final class MineActivity extends BaseActivity<MinePresenter> implements MineView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.mineaty_nickname)
    public TextView mineaty_nickname;
    @BindView(R.id.mineaty_userName)
    public TextView mineaty_userName;
    @BindView(R.id.mineaty_phone)
    public TextView mineaty_phone;
    @BindView(R.id.mineaty_email)
    public TextView mineaty_email;
    @BindView(R.id.mineaty_realname)
    public TextView mineaty_realname;
    @BindView(R.id.mineaty_cardnum)
    public TextView mineaty_cardnum;
    @BindView(R.id.mine_realname_certification)
    public LinearLayout mine_realname_certification;
    @BindView(R.id.userRealMessage)
    public LinearLayout userRealMessage;
    @BindView(R.id.code)
    public ImageView code;
    private String nickName = "";
    private UserBean userBean;

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            initSystemBar();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_aty);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        title.setText(getString(R.string.info266));
    }

    private void initListener() {
    }

    private void initData() {
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        //nickname
        nickName = userBean.getNickName();
        if (nickName == null) {
            nickName = getString(R.string.info267);
        }
        mineaty_nickname.setText(nickName);
        //user name
        mineaty_userName.setText(userBean.getUserName());
        //real name authentication
        if (!TextUtils.isEmpty(userBean.getRealName())) {
            mine_realname_certification.setVisibility(View.GONE);
            userRealMessage.setVisibility(View.VISIBLE);
            mineaty_realname.setText(userBean.getRealName());
            if (userBean.getIdCode() != null && !userBean.getIdCode().equals("")) {
                mineaty_cardnum.setText(TextLimit.limitString(userBean.getIdCode(), "***********", 3, 4));
            }
        } else {
            mine_realname_certification.setVisibility(View.VISIBLE);
            userRealMessage.setVisibility(View.GONE);
        }
        //cell-phone number
        if (!TextUtils.isEmpty(userBean.getPhone())) {
            mineaty_phone.setText(userBean.getPhone());
        } else {
            mineaty_phone.setText(getString(R.string.info268));
        }

        //mailbox
        if (!TextUtils.isEmpty(userBean.getEmail())) {
            mineaty_email.setText(userBean.getEmail());
        } else {
            mineaty_email.setText(getString(R.string.info269));
        }
    }

    @OnClick({R.id.mineaty_item_nickname, R.id.mineaty_item_phone, R.id.mineaty_item_email, R.id.mine_realname_certification, R.id.mineaty_item_code, R.id.back})
    public void onoClickView(View view) {
        switch (view.getId()) {
            case R.id.mineaty_item_nickname:
                //nick item
                new XyDialog2.Builder(MineActivity.this)
                        .title(getString(R.string.info270))
                        .hint(getString(R.string.info271))
                        .isShow(true)
                        .digit(10)
                        .setEditContent(nickName)
                        .setPositiveButtonListener(getString(R.string.info272), (EditText input, Dialog dialog, int confirm) -> {
                            nickName = getEditString(input);
                            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                            if (nickName.length() > 10) {
                                showToast(getString(R.string.info273));
                            } else {
                                getPresenter().updateNickName(
                                        userBean.getId(),
                                        nickName,
                                        AesUtil2.encryptPOST(userBean.getId()),
                                        this);
                                dialog.dismiss();
                            }
                        })
                        .createPwdDialog()
                        .show();
                break;
            case R.id.mineaty_item_phone:
                //phone number

                if (!TextUtils.isEmpty(userBean.getPhone())) {
                    ARouter.getInstance()
                            .build(ARouterPath.BindPhoneActivity)
                            .navigation();
                } else {
                    ARouter.getInstance()
                            .build(ARouterPath.BindPhoneFirstActivity)
                            .navigation();
                }
                break;
            case R.id.mineaty_item_email:
                //email
                if (!TextUtils.isEmpty(userBean.getEmail())) {
                    ARouter.getInstance()
                            .build(ARouterPath.BindEmailActivity)
                            .navigation();
                } else {
                    ARouter.getInstance()
                            .build(ARouterPath.BindEmailFirstActivity)
                            .navigation();
                }
                break;
            case R.id.mine_realname_certification:
                //certification
                ARouter.getInstance()
                        .build(ARouterPath.CertificationActivity)
                        .navigation();
                break;
            case R.id.mineaty_item_code:
                //qr code
                if (Build.VERSION.SDK_INT >= 21) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MineActivity.this, code, "code");
                    ARouter.getInstance()
                            .build(ARouterPath.CodeActivity)
                            .withOptionsCompat(options)
                            .navigation(MineActivity.this);
                } else {
                    ARouter.getInstance()
                            .build(ARouterPath.CodeActivity)
                            .navigation();
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(Object data, String msg) {
        //Nickname modified successfully
        nickName = (String) data;
        if (nickName == null) {
            nickName = "";
        }
        mineaty_nickname.setText(nickName);
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        userBean.setNickName(nickName);
        tools.saveData(userBean, Constants.USERINFO);
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bindPhoneOrEmailOk(EventBusType type) {
        if (type.getBusType() == EventBusType.BusType.BINDPHONE) {
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            mineaty_userName.setText(userBean.getUserName());
            mineaty_phone.setText(userBean.getPhone());
        }

        if (type.getBusType() == EventBusType.BusType.BINDEMAIL) {
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            mineaty_email.setText(userBean.getEmail());
            mineaty_userName.setText(userBean.getUserName());
        }

        //certification success
        if (type.getBusType() == EventBusType.BusType.REALNAMECERTIFICATION) {
            //certification
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            if (userBean.getRealName() != null && !userBean.getRealName().equals("")) {
                mine_realname_certification.setVisibility(View.GONE);
                userRealMessage.setVisibility(View.VISIBLE);
                mineaty_realname.setText(userBean.getRealName());
                if (userBean.getIdCode() != null && !userBean.getIdCode().equals("")) {
                    mineaty_cardnum.setText(TextLimit.limitString(userBean.getIdCode(), "***********", 3, 4));
                }
            } else {
                mine_realname_certification.setVisibility(View.VISIBLE);
                userRealMessage.setVisibility(View.GONE);
            }
        }
    }
}
