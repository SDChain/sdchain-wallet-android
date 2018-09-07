package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.FriendBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.bean.WalletAddressBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.FriendDetailPresenter;
import com.io.sdchain.mvp.view.FriendDetailView;
import com.io.sdchain.utils.AesUtil2;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.FriendDetailActivity)
public final class FriendDetailActivity extends BaseActivity<FriendDetailPresenter> implements FriendDetailView {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.add_or_del_friend_img)
    public ImageView add_or_del_friend_img;
    @BindView(R.id.add_or_del_friend_text)
    public TextView add_or_del_friend_text;
    @BindView(R.id.realName)
    public TextView realName;
    private String addOrDelFriendMessage;
    private String addOrDelFriendTitle;
    private String isFriend = "false";
    private UserBean userBean;
    private String friendId;
    private FriendBean friendBean;

    @Override
    protected FriendDetailPresenter createPresenter() {
        return new FriendDetailPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();

    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private void initData() {
        addOrDelFriendMessage = getString(R.string.info236);
        addOrDelFriendTitle = getString(R.string.info237);
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        friendId = getIntent().getStringExtra(Constants.USERID);
        getPresenter().friendDetail(
                userBean.getId(),
                friendId,
                AesUtil2.encryptGET(userBean.getId()),
                this);
    }

    @OnClick({R.id.friend_wallet_address, R.id.add_or_del_friend, R.id.pay_to_friend})
    public void onCLickView(View view) {
        switch (view.getId()) {
            case R.id.friend_wallet_address:
                WalletAddressBean walletAddressBean = new WalletAddressBean();
                walletAddressBean
                        .setAccount(friendBean.getAccount())
                        .setNickName(friendBean.getNickName())
                        .setRealName(friendBean.getRealName())
                        .setUserName(friendBean.getUserName())
                        .setAccount(friendBean.getAccount());
                ARouter.getInstance()
                        .build(ARouterPath.WalletAddressActivity)
                        .withString(ARouterPath.FROM, ARouterPath.FriendDetailActivity)
                        .withSerializable(Constants.WALLETADDRESSINFO, walletAddressBean)
                        .navigation();
                break;
            case R.id.add_or_del_friend:
                if (isFriend.equals("true")) {
                    addOrDelFriendTitle = getString(R.string.info238);
                    addOrDelFriendMessage = getString(R.string.info239);
                } else {
                    addOrDelFriendTitle = getString(R.string.info240);
                    addOrDelFriendMessage = getString(R.string.info241);
                }

                new XyDialog2.Builder(this)
                        .title(addOrDelFriendTitle)
                        .message(addOrDelFriendMessage)
                        .setPositiveButtonListener(getString(R.string.info242), (o, dialog, i) -> {
                            dialog.dismiss();
                            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                            if (isFriend.equals("true")) {
                                getPresenter().deleteFriend(
                                        userBean.getId(),
                                        friendId,
                                        AesUtil2.encryptGET(userBean.getId()),
                                        this);
                            } else {
                                getPresenter().addFriend(
                                        userBean.getId(),
                                        friendId,
                                        AesUtil2.encryptGET(userBean.getId()),
                                        this);
                            }
                        })
                        .setNegativeButtonListener(getString(R.string.info243), (o, dialog, i) -> {
                            dialog.dismiss();

                        })
                        .createNoticeDialog()
                        .show();
                break;
            case R.id.pay_to_friend:
                //Jump to the payment interface
                EventBus.getDefault().post(
                        new EventBusType(EventBusType.BusType.FRIENDDETAILTOPAY, friendBean.getAccount()));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(Object data, String msg) {
        friendBean = (FriendBean) data;
        Logger.e(friendBean.toString());
        if (friendBean.getRealName() != null && !friendBean.getRealName().equals("")) {
            realName.setText(friendBean.getRealName());
        } else {
            realName.setText(getString(R.string.info244));
        }

        isFriend = friendBean.getIsFriend();

        if (isFriend.equals("true")) {
            add_or_del_friend_img.setImageResource(R.drawable.selector_friend_detail_delete);
            add_or_del_friend_text.setText(getString(R.string.info245));
        } else {
            add_or_del_friend_img.setImageResource(R.drawable.selector_friend_detail_add);
            add_or_del_friend_text.setText(getString(R.string.info246));
        }

    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void addFriendSuccess(Object data, String msg) {
        add_or_del_friend_img.setImageResource(R.drawable.selector_friend_detail_delete);
        add_or_del_friend_text.setText(getString(R.string.info247));
        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.ADDORDELETEFRIEND, null));
        isFriend = "true";
    }

    @Override
    public void addFriendFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void deleteFriendSuccess(Object data, String msg) {
        add_or_del_friend_img.setImageResource(R.drawable.selector_friend_detail_add);
        add_or_del_friend_text.setText(getString(R.string.info248));
        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.ADDORDELETEFRIEND, null));
        isFriend = "false";
    }

    @Override
    public void deleteFriendFailed(String msg) {
        showToast(msg);
    }

}
