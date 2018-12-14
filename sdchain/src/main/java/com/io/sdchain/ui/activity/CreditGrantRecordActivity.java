package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.TransactionBean;
import com.io.sdchain.bean.TransactionsBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.mvp.presenter.CreditGrantRecordPresenter;
import com.io.sdchain.mvp.view.CreditGrantRecordView;
import com.io.sdchain.ui.adapter.CreditGrantRecordAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = ARouterPath.CreditGrantRecordActivity)
public final class CreditGrantRecordActivity extends BaseActivity<CreditGrantRecordPresenter> implements CreditGrantRecordView {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
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
    private CreditGrantRecordAdapter adapter;
    private List<TransactionBean> transactionBeen = new ArrayList<TransactionBean>();
    private UserBean userBean;
    private String MARKER = "";
    private boolean isFirstData = true;

    @Override
    protected CreditGrantRecordPresenter createPresenter() {
        return new CreditGrantRecordPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_grant_record);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info219));
        adapter = new CreditGrantRecordAdapter(transactionBeen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().getCreditGrantRecord(userBean.getId(), userBean.getAccount(), "", this, true);
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(onRefreshListener);
        refreshLayout.setOnLoadmoreListener(onLoadmoreListener);
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private OnRefreshListener onRefreshListener = (refreshLayout2) -> {
        isFirstData = true;
        MARKER = "";
        transactionBeen.clear();
        refreshLayout.resetNoMoreData();
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().getCreditGrantRecord(userBean.getId(), userBean.getAccount(), MARKER, this, true);
    };

    private OnLoadmoreListener onLoadmoreListener = (refreshLayout) -> {
        isFirstData = false;
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().getCreditGrantRecord(userBean.getId(), userBean.getAccount(), MARKER, this, false);
    };

    @Override
    public void onSuccess(Object data, String msg) {
        TransactionsBean transactionsBean = (TransactionsBean) data;
        List<TransactionBean> transactionBeanList = transactionsBean.getData();
        MARKER = transactionsBean.getMarker();
        if (MARKER == null && (transactionBeanList == null || transactionBeanList.size() <= 0)) {
            noData();
        } else {
            haveData();
            transactionBeen.addAll(transactionBeanList);
            adapter.notifyDataSetChanged();
            if (MARKER == null && isFirstData) {
                refreshLayout.setEnableLoadmore(false);
            } else if (MARKER == null) {
                refreshLayout.finishLoadmoreWithNoMoreData();
            } else {
                refreshLayout.finishLoadmore();
                refreshLayout.setEnableLoadmore(true);
            }
        }
        refreshLayout.finishRefresh();
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
        if ("".equals(MARKER) && isFirstData) {
            adapter.notifyDataSetChanged();
            dataError();
            refreshLayout.setEnableLoadmore(false);
        }
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
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
}
