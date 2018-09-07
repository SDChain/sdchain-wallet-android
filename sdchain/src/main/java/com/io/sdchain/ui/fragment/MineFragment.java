package com.io.sdchain.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseFragment;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.bean.VersionBean;
import com.io.sdchain.bean.WalletAddressBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.MineFragmentPresenter;
import com.io.sdchain.mvp.view.MineFragmentView;
import com.io.sdchain.service.APKTOupdateDownService;
import com.io.sdchain.utils.AppUtils;
import com.io.sdchain.utils.CheckFormat;
import com.io.sdchain.utils.PermissionUtils;
import com.io.sdchain.utils.SaveObjectTools;
import com.io.sdchain.utils.TextLimit;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author xiey
 * created at 2017/8/19.
 */
@Route(path = ARouterPath.MineFragment)
public final class MineFragment extends BaseFragment<MineFragmentPresenter> implements MineFragmentView {
    public static final String ARGUMENT = "argument";
    private ViewGroup mView;
    @BindView(R.id.mine_wallet_address)
    public LinearLayout mine_wallet_address;
    @BindView(R.id.mine_friends)
    public LinearLayout mine_friends;
    @BindView(R.id.mine_bill)
    public LinearLayout mine_bill;
    @BindView(R.id.mine_update)
    public LinearLayout mine_update;
    @BindView(R.id.mine_content)
    public LinearLayout mine_content;
    @BindView(R.id.mine_message)
    public LinearLayout mine_message;
    @BindView(R.id.mine_setting)
    public LinearLayout mine_setting;
    @BindView(R.id.mine_change_wallet_password)
    public LinearLayout mine_change_wallet_password;
    @BindView(R.id.imageView)
    public ImageView imageView;
    @BindView(R.id.userName)
    public TextView userName;
    @BindView(R.id.userPhone)
    public TextView userPhone;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.versionAttention)
    public TextView versionAttention;
    @BindView(R.id.msgStatus)
    public RelativeLayout msgStatus;
    private boolean isFromUpdate = false;
    private UserBean userBean;

    private Unbinder unbinder;

    @Override
    protected MineFragmentPresenter createPresenter() {
        return new MineFragmentPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.frag_mine, container, false);
            unbinder = ButterKnife.bind(this, mView);
            Logger.e("-----MineFragment-----");
            EventBus.getDefault().register(this);
            initView(mView);
            initListener();
            initData();
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (tools == null) {
            tools = new SaveObjectTools(getActivity());
        }
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        if (userBean.getRealName() != null && !userBean.getRealName().equals("")) {
            userName.setVisibility(View.VISIBLE);
            userName.setText(userBean.getRealName());
        } else {
            userName.setVisibility(View.GONE);
            userName.setText("");
        }

        if (CheckFormat.isMobile(userBean.getUserName())) {
            userPhone.setText(TextLimit.limitString(userBean.getUserName(), "******", 3, 2));
        } else {
            userPhone.setText(userBean.getUserName());
        }
        getPresenter().getApkVersion("" + AppUtils.getVersionCode(getActivity()), getActivity());
    }

    private void initView(View view) {
        title.setText(getString(R.string.info4));
    }

    private void initListener() {
    }

    private void initData() {
//        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
//        int size = EntityManager.queryAllReadState(userBean.getId(), userBean.getAccount()).size();

    }

    @OnClick({R.id.mine_wallet_address, R.id.mine_friends, R.id.mine_bill, R.id.mine_update, R.id.mine_content, R.id.mine_message, R.id.mine_setting, R.id.mine_change_wallet_password})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.mine_content:
                //personal information
                if (Build.VERSION.SDK_INT >= 21) {
                    imageView.setTransitionName("touxiang");
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imageView, "touxiang");
                    ARouter.getInstance()
                            .build(ARouterPath.MineActivity)
                            .withOptionsCompat(options)
                            .navigation(getActivity());
                } else {
                    ARouter.getInstance()
                            .build(ARouterPath.MineActivity)
                            .navigation();
                }
                break;
            case R.id.mine_wallet_address:
                //The wallet address
                WalletAddressBean walletAddressBean = new WalletAddressBean();
                userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                walletAddressBean
                        .setAccount(userBean.getAccount())
                        .setNickName(userBean.getNickName())
                        .setRealName(userBean.getRealName())
                        .setUserName(userBean.getUserName());
                ARouter.getInstance()
                        .build(ARouterPath.WalletAddressActivity)
                        .withString(ARouterPath.FROM, ARouterPath.MineFragment)
                        .withSerializable(Constants.WALLETADDRESSINFO, walletAddressBean)
                        .navigation();
                break;
            case R.id.mine_change_wallet_password:
                //Change payment password
                ARouter.getInstance()
                        .build(ARouterPath.SetWalletPwdActivity)
                        .navigation();
                break;
            case R.id.mine_friends:
                //friend
                ARouter.getInstance()
                        .build(ARouterPath.FriendActivity)
                        .withString(ARouterPath.FROM, ARouterPath.MineFragment)
                        .navigation();
                break;
            case R.id.mine_bill:
                //bill
                ARouter.getInstance()
                        .build(ARouterPath.BillActivity)
                        .navigation();
                break;
            case R.id.mine_update:
                //update
                getPresenter().getApkVersion("" + AppUtils.getVersionCode(getActivity()), getActivity());
                isFromUpdate = true;
                break;
            case R.id.mine_message:
                //information
                ARouter.getInstance()
                        .build(ARouterPath.MsgListActivity)
                        .navigation();
                break;
            case R.id.mine_setting:
                //setting
                ARouter.getInstance()
                        .build(ARouterPath.SettingActivity)
                        .navigation();
                break;
            default:
                break;
        }
    }

    ;


    @Override
    public void onSuccess(Object data, String msg) {
        VersionBean versionInfo = (VersionBean) data;
        if (versionInfo == null || versionInfo.getVersionCode().equals("" + AppUtils.getVersionCode(getActivity()))) {
            versionAttention.setText("v" + AppUtils.getVersionName(getActivity()));
            if (isFromUpdate) {
                showToast(msg);
            }
        } else {
            versionAttention.setText(getString(R.string.info379));
            if (isFromUpdate) {
                new XyDialog2.Builder(getActivity())
                        .title(getString(R.string.info380))
                        .message("" + versionInfo.getVersionDesc())
                        .isNeedLine(true)
                        .setPositiveButtonListener(R.array.select_dialog_notice, (view, dialog, which) -> {
                            //which:0-现在升级;1-不，谢谢，当前版本永不提醒
                            switch (which) {
                                case 0:
                                    new PermissionUtils().getInstance(getActivity())
                                            .permissions(Permission.WRITE_EXTERNAL_STORAGE)
                                            .errHint(getString(R.string.info381))
                                            .permission(permissions -> {
                                                downLoadApk(versionInfo.getUrl());
                                                showToast(getString(R.string.info382));
                                            }).start();
                                    break;
                                case 1:
                                default:
                                    break;
                            }
                            dialog.dismiss();
                        })
                        .createChooseContentButton()
                        .show();
            }
        }
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }

    /**
     * 下载
     *
     * @return
     */
    public void downLoadApk(String url) {
        Intent updateIntent = new Intent(getActivity(), APKTOupdateDownService.class);
        updateIntent.putExtra("app_name", getActivity().getResources().getString(R.string.app_name));
        updateIntent.putExtra("address", url);
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startService(updateIntent);
    }

    //实例化
    public static MineFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        MineFragment mineFragment = new MineFragment();
        mineFragment.setArguments(bundle);
        return mineFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeInfo(EventBusType type) {
        if (type.getBusType() == EventBusType.BusType.BINDPHONE) {
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            userPhone.setText(userBean.getUserName());
        }

        if (type.getBusType() == EventBusType.BusType.BINDEMAIL) {
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            userPhone.setText(userBean.getUserName());
        }
    }

}
