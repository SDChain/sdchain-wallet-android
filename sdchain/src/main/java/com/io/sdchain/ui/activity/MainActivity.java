package com.io.sdchain.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.base.BaseApplication;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.view.MainActivityView;
import com.io.sdchain.ui.fragment.AssetFragment;
import com.io.sdchain.ui.fragment.MineFragment;
import com.io.sdchain.ui.fragment.PayFragment;
import com.io.sdchain.ui.fragment.QuotesFragment;
import com.io.sdchain.utils.CleanUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = ARouterPath.MainActivity)
public final class MainActivity extends BaseActivity implements MainActivityView {

    @BindView(R.id.bottom_navigation_bar)
    public BottomNavigationBar bottomNavigationBar;

    private ArrayList<Fragment> fragments;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
//        initState();

        initView();

        initListener();

    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(AssetFragment.newInstance("asset"));
        fragments.add(PayFragment.newInstance("pay"));
        fragments.add(new QuotesFragment());
        fragments.add(MineFragment.newInstance("mine"));
        return fragments;
    }

    private void initView() {
//        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.ic_assets_choosed, getString(R.string.info1))
                        .setInactiveIconResource(R.mipmap.ic_assets)
                        .setActiveColorResource(R.color.bottom_text_color)
                )
                .addItem(new BottomNavigationItem(R.mipmap.ic_pay_choosed, getString(R.string.info2))
                        .setInactiveIconResource(R.mipmap.ic_pay)
                        .setActiveColorResource(R.color.bottom_text_color)
                ).addItem(new BottomNavigationItem(R.mipmap.ic_home_new_choosed, getString(R.string.key000274))
                .setInactiveIconResource(R.mipmap.ic_home_new)
                .setActiveColorResource(R.color.bottom_text_color))
                .addItem(new BottomNavigationItem(R.mipmap.ic_mine_choosed, getString(R.string.info4))
                        .setInactiveIconResource(R.mipmap.ic_mine)
                        .setActiveColorResource(R.color.bottom_text_color)
                )
                .setFirstSelectedPosition(0)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setActiveColor(R.color.colorPrimary)
//                .setInActiveColor("#FFFFFF")
//                .setBarBackgroundColor("#32da42")
                .initialise();
        fragments = getFragments();
        setDefaultFragment();
    }

    private void initListener() {
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (fragments != null) {
                    if (position < fragments.size()) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        //current fragment
                        Fragment from = fm.findFragmentById(R.id.layFrame);
                        //click jump to fragment
                        Fragment fragment = fragments.get(position);
                        if (fragment.isAdded()) {
                            // hide current fragment，show next
                            ft.hide(from).show(fragment);
                        } else {
                            // hide current fragment，add to next Activity
                            ft.hide(from).add(R.id.layFrame, fragment);
                            if (fragment.isHidden()) {
                                ft.show(fragment);
//                                Logger.d("be hide");
                            }
                        }
                        ft.commitAllowingStateLoss();
                    }
                }


            }

            @Override
            public void onTabUnselected(int position) {
                //hide ,or fragment will be overlapping
                if (fragments != null) {
                    if (position < fragments.size()) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Fragment fragment = fragments.get(position);
                        // hide current fragment
                        ft.hide(fragment);
                        ft.commitAllowingStateLoss();
                    }
                }
//                Logger.d("not be choosed：" + position);

            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }


    /**
     * set default
     */
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layFrame, fragments.get(0));
        transaction.commit();
    }

    /**
     * Immersive status bar
     */
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //Transparent status bar
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //Transparent navigation
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    public void onSuccess(Object data, String msg) {

    }

    @Override
    public void onFailed(String msg) {

    }


    /**
     * Double-click on the exit
     */
    private static Boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Logger.e("-----onKeyDown-----");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            showToast(getString(R.string.info265));
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                public void run() {
                    isExit = false;
                }
            }, 2000);

        } else {
            sharedPref.saveData(Constants.LOGINOUT, false);
            ((BaseApplication) getApplication()).exitApplication();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        CleanUtils.getInstance(MainActivity.this)
                .clearCode()
                .clearWalletList()
                .clearFreeze()
                .clearCodeList()
                .clearBalanceInfo();

        Logger.i("MainActivity onDestory");
    }

    /**
     * The buddy details interface transfers to the payment interface
     *
     * @param type
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFriednDetailToPay(EventBusType type) {
        Logger.e("To the home page");
        if (type.getBusType() == EventBusType.BusType.FRIENDDETAILTOPAY) {
            bottomNavigationBar.selectTab(1);
            Observable.timer(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .subscribe(aLong -> {
                        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.ASSETSTOPAY2, type.getObj()));
                    });
        }

        //From the homepage scan to the transfer interface
        if (type.getBusType() == EventBusType.BusType.ASSETSTOPAY) {
            Logger.e("Front page scan to transfer interface");
            bottomNavigationBar.selectTab(1);
            Observable.timer(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .subscribe(aLong -> {
                        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.ASSETSTOPAY2, type.getObj()));
                    });
        }

    }
}
