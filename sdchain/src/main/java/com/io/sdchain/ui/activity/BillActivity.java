package com.io.sdchain.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.BillBean;
import com.io.sdchain.bean.BillsBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.mvp.presenter.BillPresenter;
import com.io.sdchain.mvp.view.BillView;
import com.io.sdchain.ui.adapter.BillAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.BillActivity)
public final class BillActivity extends BaseActivity<BillPresenter> implements BillView {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.refreshLayout)
    public SmartRefreshLayout refreshLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.chooseDateTime)
    public TextView chooseDateTime;
    @BindView(R.id.noData)
    public RelativeLayout noData;
    @BindView(R.id.errorData)
    public RelativeLayout errorData;
    private BillAdapter adapter;
    private List<BillBean> bills = new ArrayList<BillBean>();
    private UserBean userBean;
    public int COUNT = 1;
    private String chooseDate = "", untilDate = "";
    private String nowTime = "1970-01-01";
    private Calendar calendar;
    private String MARKER = "";
    private boolean isFirtData = true;
    private String currency = "";

    @Override
    protected BillPresenter createPresenter() {
        return new BillPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ButterKnife.bind(this);
        initView();
        initListener();

        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        refreshLayout.setEnableLoadmore(false);
        title.setText(getString(R.string.info141));
        adapter = new BillAdapter(bills);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(onRefreshListener);
        refreshLayout.setOnLoadmoreListener(onLoadmoreListener);
        adapter.setOnItemClickListener(onItemClickListener);
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private void initData() {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10) {
            nowTime = "" + year + "-0" + month;
        } else {
            nowTime = "" + year + "-" + month;
        }

        chooseDate = nowTime + "-01";

        if (month == 12) {
            untilDate = "" + (year + 1) + "-01-01";
        } else {
            if (month < 10) {
                untilDate = "" + year + "-0" + (month + 1) + "-01";
            } else {
                untilDate = "" + year + "-" + (month + 1) + "-01";
            }
        }

        currency = getIntent().getStringExtra(Constants.CURRENCY);
        if (currency == null) {
            currency = "";
        }

        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().getBills(userBean.getUserAccountId(), MARKER, currency, chooseDate, untilDate, true, this);
    }

    @OnClick({R.id.chooseTime})
    public void onClickView(View view){
        switch (view.getId()) {
            case R.id.chooseTime:
                showDateSelector();
                break;
            default:
                break;
        }
    }

    //option date
    private void showDateSelector() {

        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(this, (year, month, dateDesc) -> {
            nowTime = dateDesc;
            chooseDate = dateDesc + "-01";
            if (month == 12) {
                untilDate = "" + (year + 1) + "-01-01";
            } else {
                if (month < 10) {
                    untilDate = "" + year + "-0" + (month + 1) + "-01";
                } else {
                    untilDate = "" + year + "-" + (month + 1) + "-01";
                }
            }

            if ((year == calendar.get(Calendar.YEAR)) && (month == (calendar.get(Calendar.MONTH) + 1))) {
                chooseDateTime.setText(getString(R.string.info145));
            } else {
                chooseDateTime.setText(chooseDate + "~" + untilDate);
            }

            Logger.e("chooseDate=" + chooseDate);
            Logger.e("untilDate=" + untilDate);
            COUNT = 1;
            bills.clear();
            refreshLayout.resetNoMoreData();
            getPresenter().getBills(userBean.getUserAccountId(), MARKER, currency, chooseDate, untilDate, true, BillActivity.this);
        }).textConfirm(getString(R.string.info146))
                .textCancel(getString(R.string.info147))
                .btnTextSize(16)
                .viewTextSize(25)
                .colorCancel(Color.parseColor("#999999"))
                .colorConfirm(Color.parseColor("#009900"))
                .minYear(1990)
                .maxYear(2550)
                .showDayMonthYear(false)
                .dateChose(nowTime)
                .build();
        pickerPopWin.showPopWin(this);
    }

    private OnRefreshListener onRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshLayout2) {
            isFirtData = true;
            MARKER = "";
            bills.clear();
            refreshLayout.resetNoMoreData();
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            getPresenter().getBills(userBean.getUserAccountId(), MARKER, currency, chooseDate, untilDate, false, BillActivity.this);
        }
    };

    private OnLoadmoreListener onLoadmoreListener = new OnLoadmoreListener() {
        @Override
        public void onLoadmore(final RefreshLayout refreshLayout) {
            isFirtData = false;
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            getPresenter().getBills(userBean.getUserAccountId(), MARKER, currency, chooseDate, untilDate, false, BillActivity.this);
        }

    };

    private BillAdapter.OnItemClickListener onItemClickListener = (view, position) -> {
        ARouter.getInstance()
                .build(ARouterPath.BillDetailActivity)
                .withString(Constants.PAYMENTHASH, bills.get(position).getTx_hash())
                .navigation();
    };

    @Override
    public void onSuccess(Object data, String msg) {
        BillsBean billsBean = (BillsBean) data;
        List<BillBean> billBeanList = billsBean.getPayments();
        Logger.e("operate successfully:--" + billBeanList.size());

        MARKER = billsBean.getMarker();
        if (MARKER == null && (billBeanList == null || billBeanList.size() <= 0)) {
            noData();
        } else {
            haveData();
            bills.addAll(billBeanList);
            adapter.notifyDataSetChanged();
            if (MARKER == null && isFirtData) {
                refreshLayout.setEnableLoadmore(false);
            } else if (MARKER == null) {
                refreshLayout.finishLoadmoreWithNoMoreData();
            } else {
                refreshLayout.setEnableLoadmore(true);
                refreshLayout.finishLoadmore();
            }
        }
        refreshLayout.finishRefresh();
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
        if (MARKER .equals("")) {
            adapter.notifyDataSetChanged();
            dataError();
            refreshLayout.setEnableLoadmore(false);
        }
        refreshLayout.finishLoadmore();
        refreshLayout.finishRefresh();
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
