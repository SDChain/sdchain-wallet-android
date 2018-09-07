package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.MsgNetBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.mvp.presenter.MsgDetailPresenter;
import com.io.sdchain.mvp.view.MsgDetailView;
import com.io.sdchain.utils.TimeConverterUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = ARouterPath.MsgDetailActivity)
public final class MsgDetailActivity extends BaseActivity<MsgDetailPresenter> implements MsgDetailView {

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
    protected MsgDetailPresenter createPresenter() {
        return new MsgDetailPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info274));
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
        MsgNetBean msgNetBean = (MsgNetBean) data;
        Logger.e("--------" + msgNetBean.toString());
        payment.setText(msgNetBean.getSource_account());
        receiving.setText(msgNetBean.getDestination_account());
        money.setText(msgNetBean.getAmount().getValue() + " " + msgNetBean.getAmount().getCurrency());
        fee.setText(msgNetBean.getFee() + " " + msgNetBean.getAmount().getCurrency());
        String timeStr = TimeConverterUtil.timeStamp2Date(msgNetBean.getDate());
        time.setText(timeStr);
        hash.setText(getIntent().getStringExtra(Constants.PAYMENTHASH));
        block.setText(msgNetBean.getLedger());
        String direction = msgNetBean.getDirection();
        if (direction.equals(Constants.OUTGOING)) {
            type.setText(getString(R.string.info275));
        } else if (direction.equals(Constants.INCOMING)) {
            type.setText(getString(R.string.info276));
        }
        status.setText(msgNetBean.getState());
        //Missing remarks
        if (msgNetBean.getMemos() != null && msgNetBean.getMemos().size() > 0) {
            remarks.setText(msgNetBean.getMemos().get(0).getMemoData());
        } else {
            remarks.setText("");
        }
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }
}
