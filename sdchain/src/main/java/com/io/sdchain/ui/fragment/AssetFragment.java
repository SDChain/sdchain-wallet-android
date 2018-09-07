package com.io.sdchain.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseApplication;
import com.io.sdchain.base.BaseFragment;
import com.io.sdchain.bean.AccountBean;
import com.io.sdchain.bean.AccountsBean;
import com.io.sdchain.bean.BalanceBean;
import com.io.sdchain.bean.BalancesBean;
import com.io.sdchain.bean.ImportWalletBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.bean.VersionBean;
import com.io.sdchain.bean.WalletAddressBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.AssetsFragmentPresenter;
import com.io.sdchain.mvp.view.AssetsFragmentView;
import com.io.sdchain.service.APKTOupdateDownService;
import com.io.sdchain.ui.adapter.BalanceAdapter;
import com.io.sdchain.ui.adapter.FragAssetsAccountAdapter;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.AppUtils;
import com.io.sdchain.utils.CheckFormat;
import com.io.sdchain.utils.CleanUtils;
import com.io.sdchain.utils.Common;
import com.io.sdchain.utils.PermissionUtils;
import com.io.sdchain.utils.QrUtils;
import com.io.sdchain.utils.SaveObjectTools;
import com.io.sdchain.utils.TextLimit;
import com.io.sdchain.widget.RecyclerDivider;
import com.io.sdchain.widget.ShineTextView;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author xiey
 * @date created at 2018/2/26 15:57
 * @package com.io.sdchain.frag
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 * Confusion of others' recharge activation: when others recharge no more than 6, the acquisition of assets is always a failure, that is, as long as the acquisition of assets successful activation successful
 */
@Route(path = ARouterPath.AssetFragment)
public final class AssetFragment extends BaseFragment<AssetsFragmentPresenter> implements AssetsFragmentView {

    public static final String ARGUMENT = "argument";
    //root View
    public ViewGroup mView;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    @BindView(R.id.frag_asset_recycler)
    public RecyclerView fragAssetRecycler;
    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    @BindView(R.id.jihuo)
    public ShineTextView jihuo;
    @BindView(R.id.userName)
    public TextView userName;
    @BindView(R.id.account)
    public TextView account;
    @BindView(R.id.walletAddress)
    public ImageView walletAddress;
    @BindView(R.id.balances)
    public RecyclerView balances;
    private FragAssetsAccountAdapter fragAssetsAccountAdapter;
    private List<AccountBean> accounts = new ArrayList<AccountBean>();
    private UserBean userBean;
    private SaveObjectTools tools;
    private Vibrator vibrator;
    private boolean canClick = true;
    private BalanceAdapter balanceAdapter;
    private ArrayList<BalanceBean> balanceBeens = new ArrayList<BalanceBean>();

    private Unbinder unbinder;

    @Override
    protected AssetsFragmentPresenter createPresenter() {
        return new AssetsFragmentPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.frag_asset, container, false);
            Logger.e("-----AssetFragment-----");
            unbinder = ButterKnife.bind(this, mView);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tools = new SaveObjectTools(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPresenter().getApkVersion("" + AppUtils.getVersionCode(getActivity()), getActivity(), false);
    }

    private void initView(View view) {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme));
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        fragAssetRecycler.setLayoutManager(manager);
        fragAssetRecycler.addItemDecoration(new RecyclerDivider(3));
        fragAssetsAccountAdapter = new FragAssetsAccountAdapter(getActivity(), accounts);
        fragAssetRecycler.setAdapter(fragAssetsAccountAdapter);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity());
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        balances.setLayoutManager(manager1);
        balances.addItemDecoration(new RecyclerDivider(3));
        BalanceBean balanceBean = new BalanceBean();
        balanceBean.setValue(Constants.UNACTIVEMONEY);
        balanceBean.setCurrency(Constants.SDA);
        balanceBean.setCounterparty("");
        balanceBeens.add(balanceBean);
        balanceAdapter = new BalanceAdapter(balanceBeens);
        balances.setAdapter(balanceAdapter);
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(onRefreshListener);
        fragAssetsAccountAdapter.setOnClickListener(onAdapterClickListener);
        balanceAdapter.setOnItemClickListener(onItemClickListener);
    }

    private void initData() {
        //Display header information
        baseHeader();
        //Access to assets
        getPresenter().getSdaValue(userBean.getId(), userBean.getAccount(), getActivity(), true);

        //Cached wallet list
        AccountsBean accountsBean = (AccountsBean) tools.getObjectData(userBean.getUserName());
        if (accountsBean == null) {
            accountsBean = new AccountsBean();
            AccountBean accountBean = new AccountBean();
            accountBean.setAccount(userBean.getAccount());
            accountBean.setUserAccountId(userBean.getUserAccountId());
            accountBean.setName(userBean.getAccountName());
            accountBean.setIsdefault("1");
            List<AccountBean> accountBeanList = new ArrayList<AccountBean>();
            accountBeanList.add(accountBean);
            accountsBean.setWalletList(accountBeanList);
        }
        walletListSuccess(accountsBean.getWalletList(), "");

        //Get the real wallet list
        getPresenter().accountsList(userBean.getId(), getActivity(), false);

    }

    @OnClick({R.id.slide, R.id.assets_menu_create_account, R.id.assets_menu_exit_login, R.id.jihuo, R.id.left_scan, R.id.walletAddress})
    public void onCLickView(View view) {
        switch (view.getId()) {
            case R.id.slide:
                drawer.openDrawer(GravityCompat.END);
                userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                getPresenter().accountsList(userBean.getId(), getActivity(), false);
                break;
            case R.id.assets_menu_create_account:
                drawer.closeDrawer(GravityCompat.END);
                ARouter.getInstance()
                        .build(ARouterPath.AddWalletActivity)
                        .navigation();
                break;
            case R.id.assets_menu_exit_login:
                drawer.closeDrawer(GravityCompat.END);
                new XyDialog2.Builder(getActivity())
                        .message(getString(R.string.info336))
                        .title(getString(R.string.info337))
                        .setPositiveButtonListener(getString(R.string.info338), (o, dialog, i) -> {
                            sharedPref.saveData(Constants.LOGINOUT, true);
                            CleanUtils.getInstance(getActivity()).clearAll().clearUserInfo2();
                            ARouter.getInstance()
                                    .build(ARouterPath.LoginActivity)
                                    .navigation(getActivity(), new NavCallback() {
                                        @Override
                                        public void onArrival(Postcard postcard) {
                                            ((BaseApplication) getActivity().getApplication()).exitApplication();
                                        }
                                    });
                            dialog.dismiss();
                        })
                        .setNegativeButtonListener(getString(R.string.info339), (o, dialog, i) -> {
                            dialog.dismiss();
                        })
                        .createNoticeDialog()
                        .show();
                break;
            case R.id.jihuo:
                Logger.e("canClick " + canClick);
                if (canClick) {
                    Logger.e("-------Can click to activate-------");
                    canClick = false;
                    getPresenter().activeAccount(
                            userBean.getUserAccountId(),
                            AesUtil2.encryptPOST(userBean.getUserAccountId()),
                            getActivity(),
                            true);
                }
                break;
            case R.id.left_scan:
                Logger.e("-----scan begin-----");
                new PermissionUtils().getInstance(getActivity())
                        .permissions(Permission.CAMERA)
                        .errHint(getString(R.string.info340))
                        .permission(permissions -> {
                            scan();
                        }).start();
                break;
            case R.id.walletAddress:
                //The wallet address
                WalletAddressBean walletAddressBean = new WalletAddressBean();
                UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
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
            default:
                break;
        }
    }

    private void scan() {
        QrUtils.getInstance().startScan(getActivity(), result -> {
            Logger.d("Scan succeeded：" + result);
            if (result.startsWith(Constants.USERID)) {
                //User ID to friend interface
                String userId = result.substring(Constants.USERID.length(), result.length());
                Logger.e("-----Scanned user information-----" + userId);
                ARouter.getInstance()
                        .build(ARouterPath.FriendDetailActivity)
                        .withString(Constants.USERID, userId)
                        .navigation();
            } else if (result.startsWith(Constants.ACCOUNT)) {
                //Public key to transfer interface
                String account = result.substring(Constants.ACCOUNT.length(), result.length());
                Logger.e("-----Scanned wallet address-----" + account);
                EventBus.getDefault().post(new EventBusType(EventBusType.BusType.ASSETSTOPAY, account));
            } else {
                ARouter.getInstance()
                        .build(ARouterPath.BadCodeResultActivity)
                        .withString(Constants.BADCODE, result)
                        .navigation();
            }
        });
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            jihuo.setVisibility(View.GONE);
            //get SDA balance
            getPresenter().getSdaValue(userBean.getId(), userBean.getAccount(), getActivity(), false);
        }
    };

    private BalanceAdapter.OnItemClickListener onItemClickListener = (view, position) -> {
        ARouter.getInstance()
                .build(ARouterPath.BillActivity)
                .withString(Constants.CURRENCY, balanceBeens.get(position).getCurrency())
                .navigation();
    };

    private FragAssetsAccountAdapter.OnClickListener onAdapterClickListener = new FragAssetsAccountAdapter.OnClickListener() {
        @Override
        public void onClick(View view, int position) {
            drawer.closeDrawer(GravityCompat.END);
            jihuo.setVisibility(View.GONE);
            //change wallet
            pos = position;

            //change wallet success
            AccountBean accountInfo = accounts.get(position);
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            userBean.setAccount(accountInfo.getAccount());
            userBean.setUserAccountId(accounts.get(pos).getUserAccountId());
            userBean.setWalletName(accounts.get(pos).getName());
            tools.saveData(userBean, Constants.USERINFO);
            //base info head
            baseHeader();
            //get balance
            getPresenter().getSdaValueAfterChangeAccount(userBean.getId(), userBean.getAccount(), getActivity(), true);
            //button cant click
            canClick = false;
        }

        @Override
        public void onLongClick(View view, int position) {
            //change default wallet
            jihuo.setVisibility(View.GONE);
            pos = position;
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(200);
            }
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            getPresenter().changeDefaultWallet(userBean.getId(), accounts.get(position).getAccount(), getActivity(), false);
        }
    };


    private void baseHeader() {
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        if (TextUtils.isEmpty(userBean.getRealName())) {
            if (CheckFormat.isMobile(userBean.getUserName())) {
                userName.setText(TextLimit.limitString(userBean.getUserName(), "******", 3, 2));
            }
            userName.setText(userBean.getUserName());
        } else {
            userName.setText(userBean.getRealName());
        }
        //wallet address
        if (userBean.getAccount() != null && !userBean.getAccount().equals("")) {
            account.setText(TextLimit.limitString(userBean.getAccount(), 9, 10));
        }
    }


    private int pos;

    /**
     * instance
     */
    public static AssetFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        AssetFragment assetFragment = new AssetFragment();
        assetFragment.setArguments(bundle);
        return assetFragment;
    }

    @Override
    public void onSuccess(Object data, String msg) {
        Logger.e("-----Get assets successful-----");
        BalancesBean balancesBeans = (BalancesBean) data;
        Logger.e("-----------------------------" + balancesBeans.getReserveBase());
        tools.saveData(balancesBeans.getReserveBase(), Constants.FREEZESDA);
        ArrayList<BalanceBean> balanceBean = balancesBeans.getBalances();
        BalancesData(balanceBean);
        jihuo.setVisibility(View.GONE);
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        tools.saveData(balanceBean, userBean.getUserAccountId());
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
        BalancesDataFailed();
        refreshLayout.setRefreshing(false);
        jihuo.setVisibility(View.VISIBLE);
        jihuo.setText(getString(R.string.info341));
    }

    @Override
    public void activeSuccess(Object data, String msg) {
        Logger.e("-----Activated successfully-----");
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        jihuo.setVisibility(View.VISIBLE);
        jihuo.setText(getString(R.string.info342));
        getPresenter().getSdaValueAfterActive(userBean.getId(), userBean.getAccount(), getActivity(), false);
    }

    @Override
    public void activeFailed(String msg) {
        jihuo.setVisibility(View.VISIBLE);
        jihuo.setText(getString(R.string.info343));
        canClick = true;
        showToast(msg);
    }

    @Override
    public void walletListSuccess(Object data, String msg) {
        //The wallet list was successfully retrieved
        accounts.clear();
        List<AccountBean> accountBeanList = (List<AccountBean>) data;
        accounts.addAll(accountBeanList);
        //If item<=6, the number is displayed; otherwise, there are 6 items
        if (accounts.size() < 6) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            fragAssetRecycler.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Common.dip2px(BaseApplication.getApplicationInstance(), 300));
            fragAssetRecycler.setLayoutParams(lp);
        }

        fragAssetsAccountAdapter.notifyDataSetChanged();
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);

        //In the local
        AccountsBean accountsBean = new AccountsBean();
        accountsBean.setWalletList(accounts);
        tools.saveData(accountsBean, userBean.getUserName());
    }

    @Override
    public void walletListFailed(String msg) {
        Logger.e(msg);
    }


    @Override
    public void changeDefaultWalletSuccess(Object data, String msg) {
        //Change purse successfully
        AccountBean accountInfo = (AccountBean) data;
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        userBean.setAccount(accountInfo.getAccount());
        userBean.setUserAccountId(accounts.get(pos).getUserAccountId());
        userBean.setWalletName(accounts.get(pos).getName());
        tools.saveData(userBean, Constants.USERINFO);

        //Basic Information Header
        baseHeader();
        //To get the amount
        getPresenter().getSdaValueAfterChangeAccount(userBean.getId(), userBean.getAccount(), getActivity(), false);
        //Button not clickable
        canClick = true;
        //Get the wallet list
        getPresenter().accountsList(userBean.getId(), getActivity(), false);
    }

    @Override
    public void changeDefaultWalletFailed(String msg) {
        //Changing the default wallet failed
        showToast(msg);
    }

    @Override
    public void versionSuccess(Object data, String msg) {
        VersionBean versionInfo = (VersionBean) data;
        if (versionInfo != null && versionInfo.getVersionState() != null && versionInfo.getVersionState().equals("1")) {
            //forced updating
            XyDialog2 xyDialog2 = new XyDialog2.Builder(getActivity())
                    .title(getString(R.string.info344))
                    .message("" + versionInfo.getVersionDesc())
                    .isNeedLine(true)
                    .cancelTouchout(false)
                    .setPositiveButtonListener(R.array.select_dialog_notice_force, (view, dialog, which) -> {
                        //which:0-Upgrade now
                        switch (which) {
                            case 0:
                                new PermissionUtils().getInstance(getActivity())
                                        .permissions(Permission.WRITE_EXTERNAL_STORAGE)
                                        .errHint(getString(R.string.info345))
                                        .permission(permissions -> {
                                            downLoadApk(versionInfo.getUrl());
                                            showToast(getString(R.string.info346));
                                        }).start();
                                break;
                            default:
                                break;
                        }
                    })
                    .createChooseContentButton();
            xyDialog2.setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            });
            xyDialog2.show();
            return;
        }

        boolean isRemind = true;
        if (sharedPref.getData(Constants.REMIND) == null) {
            sharedPref.saveData(Constants.REMIND, true);
            isRemind = true;
        } else {
            isRemind = (boolean) sharedPref.getData(Constants.REMIND);
        }

        if (versionInfo == null || ("" + AppUtils.getVersionCode(getActivity())).equals(versionInfo.getVersionCode())) {
            //not update
            sharedPref.saveData(Constants.REMIND, true);
        } else {
            //need to be updated
            if (isRemind) {
                //pop-up
                new XyDialog2.Builder(getActivity())
                        .title(getString(R.string.info347))
                        .message("" + versionInfo.getVersionDesc())
                        .isNeedLine(true)
                        .setPositiveButtonListener(R.array.select_dialog_notice, (view, dialog, which) -> {
                            //which:0-Upgrade now;1-No, thanks，The current version never alerts
                            switch (which) {
                                case 0:
                                    new PermissionUtils().getInstance(getActivity())
                                            .permissions(Permission.WRITE_EXTERNAL_STORAGE)
                                            .errHint(getString(R.string.info345))
                                            .permission(permissions -> {
                                                downLoadApk(versionInfo.getUrl());
                                                showToast(getString(R.string.info346));
                                            }).start();
                                    break;
                                case 1:
                                default:
                                    sharedPref.saveData(Constants.REMIND, false);
                                    break;
                            }
                            dialog.dismiss();
                        })
                        .createChooseContentButton()
                        .show();
            }
        }

        Logger.e("Whether to update:" + isRemind);
    }

    @Override
    public void versionFailed(String msg) {

    }

    @Override
    public void onSuccessAfterActive(Object data, String msg) {
        Logger.e("-----Manually activate to get assets successfully-----");
        BalancesBean balancesBeans = (BalancesBean) data;
        tools.saveData(balancesBeans.getReserveBase(), Constants.FREEZESDA);
        ArrayList<BalanceBean> balanceBean = balancesBeans.getBalances();
        BalancesData(balanceBean);
        jihuo.setVisibility(View.GONE);
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        tools.saveData(balanceBean, userBean.getUserAccountId());
        refreshLayout.setRefreshing(false);
        Logger.e("-------Activation success gets amount success-------");
        canClick = true;
    }

    @Override
    public void onFailedAfterActive(String msg) {
        BalancesDataFailed();
        refreshLayout.setRefreshing(false);
        getPresenter().getSdaValueAfterActive(userBean.getId(), userBean.getAccount(), getActivity(), false);
    }


    @Override
    public void onSuccessAfterChangeAccount(Object data, String msg) {
        Logger.e("-----Get assets successful after change account-----");
        BalancesBean balancesBeans = (BalancesBean) data;
        tools.saveData(balancesBeans.getReserveBase(), Constants.FREEZESDA);
        ArrayList<BalanceBean> balanceBean = balancesBeans.getBalances();
        BalancesData(balanceBean);
        jihuo.setVisibility(View.GONE);
        canClick = true;
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        tools.saveData(balanceBean, userBean.getUserAccountId());
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailedAfterChangeAccount(String msg) {
        Logger.e("-----Request amount failed after wallet change-----");
        BalancesDataFailed();
        //inactive
        refreshLayout.setRefreshing(false);
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        jihuo.setVisibility(View.VISIBLE);
        jihuo.setText(getString(R.string.info349));
        canClick = true;
        tools.saveData(userBean, Constants.USERINFO);
    }

    /**
     * Set the currency amount list
     */
    private void BalancesData(ArrayList<BalanceBean> balanceBeanList) {
        balanceBeens.clear();
        balanceBeens.addAll(balanceBeanList);
        balanceAdapter.notifyDataSetChanged();
    }

    private void BalancesDataFailed() {
        balanceBeens.clear();
        BalanceBean balanceBean = new BalanceBean();
        balanceBean.setValue(Constants.UNACTIVEMONEY);
        balanceBean.setCurrency(Constants.SDA);
        balanceBean.setCounterparty("");
        balanceBeens.add(balanceBean);
        tools.saveData(balanceBeens, userBean.getUserAccountId());
        balanceAdapter.notifyDataSetChanged();
    }

    /**
     * download
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void change(EventBusType type) {
        //After the transfer is successful, modify the amount of the home asset
        if (type.getBusType() == EventBusType.BusType.ASSETSCHANGEDS) {
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            getPresenter().getSdaValue(userBean.getId(), userBean.getAccount(), getActivity(), false);
        }

        //Refresh the list of wallets after the wallet has been created successfully
        if (type.getBusType() == EventBusType.BusType.CREATEACCOUNTSUCCESS) {
            AccountBean accountBeanInfo = (AccountBean) type.getObj();
            if (accountBeanInfo != null) {
                //Change purse successfully
                userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                userBean.setAccount(accountBeanInfo.getAccount());
                userBean.setUserAccountId(accountBeanInfo.getUserAccountId());
                userBean.setWalletName(getString(R.string.info350));
                tools.saveData(userBean, Constants.USERINFO);
                baseHeader();
                //To get the amount
                getPresenter().getSdaValueAfterChangeAccount(userBean.getId(), userBean.getAccount(), getActivity(), false);
                //Button not clickable
                canClick = false;
                //Get the wallet list
                getPresenter().accountsList(userBean.getId(), getActivity(), false);
            }
        }

        //Refresh the list of wallets after you import them
        if (type.getBusType() == EventBusType.BusType.IMPORTWALLETSUCCESS) {
            ImportWalletBean importWalletBean = (ImportWalletBean) type.getObj();
            if (importWalletBean != null) {
                //Change purse successfully
                userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                userBean.setAccount(importWalletBean.getAccount());
                userBean.setUserAccountId(importWalletBean.getId());
                userBean.setWalletName(importWalletBean.getName());
                tools.saveData(userBean, Constants.USERINFO);
                baseHeader();
                //To get the amount
                getPresenter().getSdaValueAfterChangeAccount(userBean.getId(), userBean.getAccount(), getActivity(), false);
                //Button not clickable
                canClick = false;
                //Get the wallet list
                getPresenter().accountsList(userBean.getId(), getActivity(), false);
            }
        }

        //You need to refresh the home wallet list after changing the wallet nickname
        if (type.getBusType() == EventBusType.BusType.UPDATEWALLETNAME) {
            UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            getPresenter().accountsList(userBean.getId(), getActivity(), false);
        }

        if (type.getBusType() == EventBusType.BusType.BINDPHONE) {
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            userName.setText(userBean.getUserName());
        }

        if (type.getBusType() == EventBusType.BusType.BINDEMAIL) {
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            userName.setText(userBean.getUserName());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }
}
