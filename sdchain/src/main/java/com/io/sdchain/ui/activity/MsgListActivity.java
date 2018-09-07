package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.MsgBean;
import com.io.sdchain.bean.MsgNetBean;
import com.io.sdchain.bean.MsgsBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.greendao.EntityManager;
import com.io.sdchain.mvp.presenter.MsgPresenter;
import com.io.sdchain.mvp.view.MsgView;
import com.io.sdchain.ui.adapter.MsgAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.MsgListActivity)
public final class MsgListActivity extends BaseActivity<MsgPresenter> implements MsgView {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.noData)
    public RelativeLayout noData;
    @BindView(R.id.errorData)
    public RelativeLayout errorData;
    @BindView(R.id.refreshLayout)
    public SmartRefreshLayout refreshLayout;
    private List<MsgBean> msgs = new ArrayList<MsgBean>();
    private MsgAdapter adapter;
    public int COUNT = 1;
    private String MARKER = "";
    private boolean isFirtData = true;

    @Override
    protected MsgPresenter createPresenter() {
        return new MsgPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        adapter = new MsgAdapter(msgs, false);
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
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        if (userBean != null) {
            getPresenter().getMsgs(userBean.getUserAccountId(), userBean.getId(), MARKER, true, this);
        }
    }

    @OnClick({R.id.readAll})
    public void onCLickView(View view) {
        switch (view.getId()) {
            case R.id.readAll:
                //All have been read
                //EntityManager.changeReadStatusAllUn();//Set to all unread
                EntityManager.changeReadStatusByPage(COUNT, 10);
                for (MsgBean msg : msgs) {
                    msg.setReadStatus(true);
                }
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private OnRefreshListener onRefreshListener = (refreshLayout2) -> {
        COUNT = 1;
        isFirtData = true;
        MARKER = "";
        msgs.clear();
        refreshLayout.resetNoMoreData();
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().getMsgs(userBean.getUserAccountId(), userBean.getId(), MARKER, false, MsgListActivity.this);
    };

    private OnLoadmoreListener onLoadmoreListener = (refreshLayout) -> {
        isFirtData = false;
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().getMsgs(userBean.getUserAccountId(), userBean.getId(), MARKER, false, MsgListActivity.this);
    };

    private MsgAdapter.OnItemClickListener onItemClickListener = (view, position) -> {
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        EntityManager.changeReadStatus(userBean.getId(), msgs.get(position).getHash());
        //Message prompt can't be done, because someone else has transferred the message to you and you haven't entered the database yet,
//        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.READMSGSUCCESS, null));
        adapter.notifyItemChanged(position);
        ARouter.getInstance()
                .build(ARouterPath.MsgDetailActivity)
                .withString(Constants.PAYMENTHASH, msgs.get(position).getHash())
                .navigation();
    };

    @Override
    public void onSuccess(Object data, String msg) {
        //1.Gets a list of messages on the network
        MsgsBean msgsBean = (MsgsBean) data;
        List<MsgNetBean> msgBeenList = msgsBean.getPayments();
        Logger.e("operate successfully:--" + msgBeenList.size());
        MARKER = msgsBean.getMarker();

        if (MARKER == null && (msgBeenList == null || msgBeenList.size() <= 0)) {
            noData();
        } else {
            haveData();
            //2.Convert to the local MsgBean and insert it into the database
            //Determine if the record exists each time you insert it
            //If it exists, insert it and set the reading state to false unread
            UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            for (MsgNetBean msgNetBean : msgBeenList) {
                MsgBean msgBean = new MsgBean();
                msgBean.setUserId(userBean.getId());
                msgBean.setHash(msgNetBean.getHash());
//                msgBean.setLedger(msgNetBean.getLedger());
//                msgBean.setStatus(msgNetBean.getState());
                msgBean.setSource_account(msgNetBean.getSource_account());
                msgBean.setDestination_account(msgNetBean.getDestination_account());
                msgBean.setCurrency(msgNetBean.getAmount().getCurrency());
                msgBean.setValue(msgNetBean.getAmount().getValue());
                msgBean.setIssuer(msgNetBean.getAmount().getIssuer());
                msgBean.setDirection(msgNetBean.getDirection());
                msgBean.setTimestamp(msgNetBean.getTimestamp());
                msgBean.setDate(msgNetBean.getDate());
//                msgBean.setFee(msgNetBean.getFee());
                msgBean.setAccount(userBean.getAccount());

                if (msgNetBean.getMemos() != null && msgNetBean.getMemos().size() > 0) {
                    msgBean.setMemoData(msgNetBean.getMemos().get(0).getMemoData());
                }
                msgBean.setSuccess(msgNetBean.isSuccess());
                //Insert to database
                EntityManager.insert(msgBean);
            }

            //3.Read from database
            List<MsgBean> msgBeens = EntityManager.queryByPage(userBean.getId(), userBean.getAccount(), COUNT, 10);
            Logger.e("size：" + msgBeens.size() + msgBeens.toString());

            msgs.addAll(msgBeens);
            adapter.notifyDataSetChanged();


            if (MARKER == null && isFirtData) {
                refreshLayout.setEnableLoadmore(false);
            } else if (MARKER == null) {
                refreshLayout.finishLoadmoreWithNoMoreData();
            } else {
                COUNT++;
                refreshLayout.setEnableLoadmore(true);
                refreshLayout.finishLoadmore();
            }
        }
        refreshLayout.finishRefresh();
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
        if (COUNT == 1) {
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
