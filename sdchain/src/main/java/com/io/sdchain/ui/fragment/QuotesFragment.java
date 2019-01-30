package com.io.sdchain.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseFragment;
import com.io.sdchain.bean.InfoBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.mvp.presenter.CreditGrantPresenter;
import com.io.sdchain.mvp.view.CreditGrantView;
import com.io.sdchain.ui.adapter.InfoAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ziyong on 2018/11/8.
 * —。—
 */
public class QuotesFragment extends BaseFragment<CreditGrantPresenter> implements CreditGrantView {

    public static final String ARGUMENT = "argument";
    private ViewGroup mView;
    private Unbinder unbinder;

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
    private InfoAdapter adapter;
    private List<InfoBean> creditGrantBeens = new ArrayList<InfoBean>();
    private UserBean userBean;
    public int COUNT = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.activity_credit_infomation, container, false);
            unbinder = ButterKnife.bind(this, mView);
            Logger.e("-----MarketFragment-----");
            initView(mView);
            initData();
            initListener();
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }

        return mView;
    }

    private void initView(ViewGroup view) {
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);
        title.setText(getString(R.string.key000274));
        adapter = new InfoAdapter(creditGrantBeens);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected CreditGrantPresenter createPresenter() {
        return new CreditGrantPresenter(this);
    }
    private void initData() {
        getPresenter().getInfoList( COUNT,  getActivity(), true);
    }
    private void initListener() {
        refreshLayout.setOnRefreshListener(onRefreshListener);
        refreshLayout.setOnLoadmoreListener(onLoadmoreListener);
        adapter.setOnItemClickListener(onItemClickListener);
//        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private OnRefreshListener onRefreshListener = (refreshLayout2) -> {
        COUNT = 1;
        creditGrantBeens.clear();
        refreshLayout.resetNoMoreData();
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().getInfoList( COUNT,  getActivity(), true);
    };

    private OnLoadmoreListener onLoadmoreListener = (refreshLayout) -> {
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().getInfoList( COUNT,  getActivity(), true);
    };

    private InfoAdapter.OnItemClickListener onItemClickListener = (v, position) -> {
        pos = position;
        InfoBean data=  creditGrantBeens.get(position);
        ARouter.getInstance()
                .build(ARouterPath.InfoDetailActivity)
                .withSerializable("data",data)
                .navigation();

    };


    @Override
    public void onSuccess(Object data, String msg) {
        List<InfoBean> creditGrantBeanList = (List<InfoBean>) data;
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
    }

    @Override
    public void cancelCreditGrantFailed(String msg) {
        //Cancellation failed
        showToast(msg);
    }

    @Override
    public void pwdWrong1(String msg) {

    }

    @Override
    public void pwdWrong2(String msg) {

    }

    @Override
    public void pwdWrong3(String msg) {

    }

    @Override
    public void pwdWrong4(String msg) {

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
