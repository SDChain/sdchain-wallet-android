package com.io.sdchain.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.BillDetailBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.BillDetailPresenter;
import com.io.sdchain.mvp.view.BillDetailView;
import com.io.sdchain.utils.TimeConverterUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = ARouterPath.BillDetailActivity)
public final class BillDetailActivity extends BaseActivity<BillDetailPresenter> implements BillDetailView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.payment)
    public TextView payment;
    @BindView(R.id.receiving)
    public TextView receiving;
    @BindView(R.id.money)
    public TextView money;
    @BindView(R.id.fee)
    public TextView fee;
    @BindView(R.id.time)
    public TextView time;
    @BindView(R.id.hash)
    public TextView hash;
    @BindView(R.id.block)
    public TextView block;
    @BindView(R.id.type)
    public TextView type;
    @BindView(R.id.status)
    public TextView status;
    @BindView(R.id.remarks)
    public TextView remarks;
    private UserBean userBean;

    @Override
    protected BillDetailPresenter createPresenter() {
        return new BillDetailPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();

    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info148));
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private void initData() {
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        String hash = getIntent().getStringExtra(Constants.PAYMENTHASH);
        getPresenter().getDetail(userBean.getUserAccountId(), hash, true, this);
    }

    @Override
    public void onSuccess(Object data, String msg) {
        BillDetailBean billBean = (BillDetailBean) data;
        Logger.e("--------" + billBean.toString());
        payment.setText(billBean.getSource_account());
        receiving.setText(billBean.getDestination_account());
        money.setText(billBean.getAmount().getValue() + " " + billBean.getAmount().getCurrency());
        fee.setText(billBean.getFee() + " " + billBean.getAmount().getCurrency());
        if (!TextUtils.isEmpty(billBean.getDate())) {
            String timeStr = TimeConverterUtil.timeStamp2Date(billBean.getDate());
            time.setText(timeStr);
        }
        hash.setText(getIntent().getStringExtra(Constants.PAYMENTHASH));
        block.setText(billBean.getLedger());
        String direction = billBean.getDirection();
        if (direction.equals(Constants.OUTGOING)) {
            type.setText(getString(R.string.info149));
        } else if (direction.equals(Constants.INCOMING)) {
            type.setText(getString(R.string.info150));
        }

        //Missing remarks
        if (billBean.getMemos() != null && billBean.getMemos().size() > 0) {
            remarks.setText(billBean.getMemos().get(0).getMemoData());
        } else {
            remarks.setText("");
        }
        if (billBean.getState().equals("pending")) {
            status.setTextColor(Color.GREEN);
            status.setText(billBean.getState() + "....");
            String hash = getIntent().getStringExtra(Constants.PAYMENTHASH);
            getPresenter().getDetail(userBean.getUserAccountId(), hash, false, this);
        } else {
            status.setTextColor(Color.parseColor("#282828"));
            status.setText(billBean.getState());
            //Refresh the currency balance of the home page
            EventBus.getDefault().post(new EventBusType(EventBusType.BusType.ASSETSCHANGEDS, "The amount changes after the transfer"));
        }

    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }
}
