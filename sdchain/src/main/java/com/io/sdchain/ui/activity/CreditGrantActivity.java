package com.io.sdchain.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.CreditGrantBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.CreditGrantPresenter;
import com.io.sdchain.mvp.view.CreditGrantView;
import com.io.sdchain.ui.adapter.CreditGrantAdapter;
import com.io.sdchain.utils.Common;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiey94.xydialog.dialog.XyDialog2;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Route(path = ARouterPath.CreditGrantActivity)
public final class CreditGrantActivity extends BaseActivity<CreditGrantPresenter> implements CreditGrantView {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.right)
    public TextView right;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.refreshLayout)
    public SmartRefreshLayout refreshLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.noData)
    public RelativeLayout noData;
    @BindView(R.id.errorData)
    public RelativeLayout errorData;
    private CreditGrantAdapter adapter;
    private List<CreditGrantBean> creditGrantBeens = new ArrayList<CreditGrantBean>();
    private UserBean userBean;
    public int COUNT = 1;

    @Override
    protected CreditGrantPresenter createPresenter() {
        return new CreditGrantPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_grant);
        ButterKnife.bind(this);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info213));
        right.setText(getString(R.string.info214));
        right.setVisibility(View.VISIBLE);
        adapter = new CreditGrantAdapter(creditGrantBeens);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().getCreditGrantList(userBean.getId(), "", COUNT, userBean.getAccount(), this, true);
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(onRefreshListener);
        refreshLayout.setOnLoadmoreListener(onLoadmoreListener);
        adapter.setOnItemClickListener(onItemClickListener);
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    @OnClick({R.id.right})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.right:
                ARouter.getInstance()
                        .build(ARouterPath.CreditGrantRecordActivity)
                        .navigation();
                break;
            default:
                break;
        }
    }

    private OnRefreshListener onRefreshListener = (refreshLayout2) -> {
        COUNT = 1;
        creditGrantBeens.clear();
        refreshLayout.resetNoMoreData();
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().getCreditGrantList(userBean.getId(), "", COUNT, userBean.getAccount(), this, true);
    };

    private OnLoadmoreListener onLoadmoreListener = (refreshLayout) -> {
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().getCreditGrantList(userBean.getId(), "", COUNT, userBean.getAccount(), this, false);
    };

    private CreditGrantAdapter.OnItemClickListener onItemClickListener = (v, position) -> {
        pos = position;
        boolean trust = false;
        if (notNull(creditGrantBeens) && notNull(creditGrantBeens.get(position)) && notNull(creditGrantBeens.get(position).isTrusted())) {
            trust = creditGrantBeens.get(position).isTrusted();
        }
        String message = trust ? getString(R.string.info672) : getString(R.string.info675);
        if (notNull(creditGrantBeens) && notNull(creditGrantBeens.get(position)) && notNull(creditGrantBeens.get(position).getCurrency())) {
            message = String.format(message, creditGrantBeens.get(position).getCurrency());
        }
        new XyDialog2.Builder(CreditGrantActivity.this)
                .message(message)
                .marginTop(Common.dip2px(CreditGrantActivity.this, R.dimen.dimen10))
                .setPositiveButtonListener(getString(R.string.info673), (View view, Dialog d, int which) -> {
                    showInput();
                    d.dismiss();
                })
                .setNegativeButtonListener(getString(R.string.info674), (o, d, i) -> {
                    d.dismiss();
                })
                .createNoticeDialog()
                .show();
    };

    @Override
    public void onSuccess(Object data, String msg) {
        List<CreditGrantBean> creditGrantBeanList = (List<CreditGrantBean>) data;
        if (COUNT == 1 && (creditGrantBeanList == null || creditGrantBeanList.size() <= 0)) {
            refreshLayout.setEnableLoadmore(false);
            noData();
        } else {
            Logger.e("operate successfully:--" + creditGrantBeanList.size());
            haveData();
            if (COUNT > 1 && creditGrantBeanList.size() < 10) {
                refreshLayout.finishLoadmoreWithNoMoreData();
            } else if (COUNT == 1 && creditGrantBeanList.size() < 10) {
                refreshLayout.setEnableLoadmore(false);
                creditGrantBeens.clear();
            } else {
                COUNT += 1;
                refreshLayout.setEnableLoadmore(true);
                refreshLayout.finishLoadmore();
            }
            creditGrantBeens.addAll(creditGrantBeanList);
            adapter.notifyDataSetChanged();
        }
        refreshLayout.finishRefresh();
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
        //If you fail to pull the refresh, you need to process the previous list data, otherwise loading more would be tragic
        if (COUNT == 1) {
            adapter.notifyDataSetChanged();
            dataError();
            refreshLayout.setEnableLoadmore(false);
        }
        refreshLayout.finishLoadmore();
        refreshLayout.finishRefresh();
    }

    private int pos = 0;

    @Override
    public void creditGrantSuccess(Object data, String msg) {
        //Credit successfully
        creditGrantBeens.get(pos).setTrusted(true);
        adapter.notifyItemChanged(pos);
        showToast(msg);
        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.CREDIT, null));
    }

    @Override
    public void creditGrantFailed(String msg) {
        //Credit failure
        showToast(msg);
    }

    @Override
    public void cancelCreditGrantSuccess(Object data, String msg) {
        //Credit cancelled successfully
        creditGrantBeens.get(pos).setTrusted(false);
        adapter.notifyItemChanged(pos);
        showToast(msg);
        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.CREDIT, null));
    }

    @Override
    public void cancelCreditGrantFailed(String msg) {
        //Cancellation failed
        showToast(msg);
    }

    private void haveData() {
        recyclerView.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        errorData.setVisibility(View.GONE);
    }

    private void noData() {
        recyclerView.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
        errorData.setVisibility(View.GONE);
    }

    private void dataError() {
        recyclerView.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        errorData.setVisibility(View.VISIBLE);
    }

    @Override
    public void pwdWrong1(String msg) {
        showWarn(msg);
    }

    @Override
    public void pwdWrong2(String msg) {
        showWarn(msg);
    }

    @Override
    public void pwdWrong3(String msg) {
        showWarn(msg);
    }

    private void showWarn(String msg){
        new XyDialog2.Builder(this)
                .message(msg)
                .gravity(Gravity.CENTER)
                .setPositiveButtonListener(getString(R.string.info680), (view, dialog, which) -> {
                    ARouter.getInstance()
                            .build(ARouterPath.ForgetWalletPwdActivity)
                            .navigation();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButtonListener(getString(R.string.info679), (view, dialog, which) -> {
                    showInput();
                    dialog.dismiss();
                })
                .pwdWrong()
                .show();
    }

    private void showInput() {
        new XyDialog2.Builder(CreditGrantActivity.this)
                .title(getString(R.string.info215))
                .isNumber(true)
                .digit(6)
                .setPositiveButtonListener(getString(R.string.info216), (EditText editText, Dialog dialog1, int i) -> {
                    String payPwd = getEditString(editText);
                    if (payPwd.length() != 6) {
                        showToast(getString(R.string.info217));
                        return;
                    }
                    if (creditGrantBeens.get(pos).isTrusted()) {
                        getPresenter().cancelCreditGrant(
                                userBean.getId(),
                                userBean.getUserAccountId(),
                                payPwd,
                                creditGrantBeens.get(pos).getCurrency(),
                                creditGrantBeens.get(pos).getCounterparty(),
                                true,
                                CreditGrantActivity.this
                        );
                    } else {
                        getPresenter().creditGrant(
                                userBean.getId(),
                                userBean.getUserAccountId(),
                                payPwd,
                                creditGrantBeens.get(pos).getLimit(),
                                creditGrantBeens.get(pos).getCurrency(),
                                creditGrantBeens.get(pos).getCounterparty(),
                                true,
                                CreditGrantActivity.this
                        );
                    }
                    dialog1.dismiss();
                })
                .setNegativeButtonListener(getString(R.string.info218), (o, dialog2, i) -> {
                    ARouter.getInstance()
                            .build(ARouterPath.ForgetWalletPwdActivity)
                            .navigation();
                    dialog2.dismiss();
                })
                .createPwd2Dialog()
                .show();
    }

    private XyDialog2 dialogLock;
    private Disposable disposableLock;

    @Override
    public void pwdWrong4(String msg) {
        if (dialogLock == null) {
            dialogLock = new XyDialog2.Builder(this)
                    .message(msg)
                    .gravity(Gravity.CENTER)
                    .pwdWrongLock();
        }
        dialogLock.show();
        disposableLock = Observable.timer(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (dialogLock != null && dialogLock.isShowing()) {
                        dialogLock.dismiss();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogLock != null) {
            dialogLock.dismiss();
        }
        if (disposableLock != null && !disposableLock.isDisposed()) {
            disposableLock.dispose();
        }
    }

}
