package com.io.sdchain.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseFragment;
import com.io.sdchain.bean.BalanceBean;
import com.io.sdchain.bean.BalancesBean;
import com.io.sdchain.bean.MoneyBean;
import com.io.sdchain.bean.PayBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.PayFragmentPresenter;
import com.io.sdchain.mvp.view.PayFragmentView;
import com.io.sdchain.ui.adapter.CurrencyAdapter;
import com.io.sdchain.utils.Common;
import com.io.sdchain.utils.ImageLoader;
import com.io.sdchain.utils.ListUtils;
import com.io.sdchain.utils.PayUtils;
import com.io.sdchain.utils.TextChange;
import com.io.sdchain.widget.MyPopupWindow;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by xiey on 2017/8/19.
 */
@Route(path = ARouterPath.PayFragment)
public final class PayFragment extends BaseFragment<PayFragmentPresenter> implements PayFragmentView {

    public static final String ARGUMENT = "argument";
    private ViewGroup mView;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.addAccount)
    public RelativeLayout addAccount;
    @BindView(R.id.addAccountAddress)
    public EditText addAccountAddress;
    @BindView(R.id.moneyCount)
    public EditText moneyCount;
    @BindView(R.id.remarks)
    public EditText remarks;
    @BindView(R.id.payBtn)
    public Button payBtn;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.deleteAccount)
    public RelativeLayout deleteAccount;
    @BindView(R.id.deleteMoney)
    public RelativeLayout deleteMoney;
    @BindView(R.id.deleteRemarks)
    public RelativeLayout deleteRemarks;
    @BindView(R.id.attention)
    public TextView attention;
    @BindView(R.id.allMoney)
    public TextView allMoney;
    @BindView(R.id.chooseCurrency)
    public LinearLayout chooseCurrency;
    @BindView(R.id.choose)
    public ImageView choose;
    @BindView(R.id.appbar)
    public AppBarLayout appbar;
    private String payPwd = "";
    private TextChange textChange;
    private UserBean userBean;

    private Unbinder unbinder;

    @Override
    protected PayFragmentPresenter createPresenter() {
        return new PayFragmentPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.frag_pay, container, false);
            unbinder = ButterKnife.bind(this, mView);
            Logger.e("-----PayFragment-----");
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

    private void initView(View view) {
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        title.setText(Constants.SDA);
        addAccountAddress.setCursorVisible(false);
        textChange = new TextChange();
    }

    private void initListener() {
        addAccountAddress.addTextChangedListener(textChange);
        moneyCount.addTextChangedListener(textChange);
        remarks.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
        addAccountAddress.setOnTouchListener(touchListener);
    }

    private ArrayList<BalanceBean> balanceBeens;

    private void initData() {
        currency = Constants.SDA;
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        balanceBeens = (ArrayList<BalanceBean>) tools.getObjectData(userBean.getUserAccountId());

        showValue();

        payBtn.setEnabled(false);
        payBtn.setBackgroundResource(R.drawable.button_unenable);

        //init
        getCode();
    }

    private View.OnTouchListener touchListener = (v, event) -> {
        switch (v.getId()) {
            case R.id.addAccountAddress: {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    addAccountAddress.setCursorVisible(true);
                }
                break;
            }
            default:
                break;
        }
        return false;
    };

    @OnClick({R.id.addAccount, R.id.payBtn, R.id.deleteAccount, R.id.deleteMoney, R.id.deleteRemarks, R.id.allMoney, R.id.chooseCurrency})
    public void onCLickView(View view) {
        switch (view.getId()) {
            case R.id.addAccount:
                ARouter.getInstance()
                        .build(ARouterPath.FriendActivity)
                        .withString(ARouterPath.FROM, ARouterPath.PayFragment)
                        .navigation();
                break;
            case R.id.payBtn:
                userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                int checkCode = PayUtils.checkPay(getActivity(), getEditString(addAccountAddress), currency, getEditString(moneyCount));
                if (!getPresenter().checkInput(checkCode)) {
                    return;
                }
                Logger.i("The input amount is：" + PayUtils.getMoney(getEditString(moneyCount)));
                balanceBeens = (ArrayList<BalanceBean>) tools.getObjectData(userBean.getUserAccountId());
                new XyDialog2.Builder(getActivity())
                        .title(getString(R.string.info389))
                        .isNumber(true)
                        .digit(6)
                        .setPositiveButtonListener(getString(R.string.info390), (EditText editText, Dialog dialog, int i) -> {
                            payPwd = getEditString(editText);
                            if (payPwd.length() != 6) {
                                showToast(getString(R.string.info391));
                                return;
                            }
                            getPresenter().payCurrency(
                                    getEditString(addAccountAddress),
                                    getEditString(moneyCount),
                                    currency,
                                    ListUtils.getCounterParty(currency, balanceBeens),
                                    getEditString(remarks),
                                    payPwd,
                                    userBean.getId(),
                                    userBean.getUserAccountId(),
                                    getActivity()
                            );
                            dialog.dismiss();
                        })
                        .setNegativeButtonListener(getString(R.string.info5), (o, dialog, i) -> {
                            ARouter.getInstance()
                                    .build(ARouterPath.ForgetWalletPwdActivity)
                                    .navigation();
                            dialog.dismiss();
                        })
                        .createPwd2Dialog()
                        .show();
                break;
            case R.id.allMoney:
                MoneyBean moneyBean = PayUtils.balance(getActivity(), currency);
                moneyCount.setText("" + moneyBean.getCanUse());
                moneyCount.setSelection(moneyCount.length());
                moneyCount.setFocusable(true);
                moneyCount.setFocusableInTouchMode(true);
                moneyCount.requestFocus();
                break;
            case R.id.deleteAccount:
                addAccountAddress.setText("");
                break;
            case R.id.deleteMoney:
                moneyCount.setText("");
                break;
            case R.id.deleteRemarks:
                remarks.setText("");
                break;
            case R.id.chooseCurrency:
                ImageLoader.loadImage(R.mipmap.ic_currency_show, choose);
                showCurrency();
                break;
            default:
                break;
        }
    }

    ;

    @Override
    public void onSuccess(Object data, String msg) {
        PayBean payBean = (PayBean) data;
        String hash = payBean.getHash();
        ARouter.getInstance()
                .build(ARouterPath.BillDetailActivity)
                .withString(Constants.PAYMENTHASH, hash)
                .navigation();
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }

    //instance
    public static PayFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        PayFragment payFragment = new PayFragment();
        payFragment.setArguments(bundle);
        return payFragment;
    }

    private TextChange.OnTextChange onTextChange = ((editable) -> {
        if (haveString(addAccountAddress) && haveString(moneyCount)) {
            payBtn.setEnabled(true);
            payBtn.setBackgroundResource(R.drawable.selector_button);
        } else {
            payBtn.setEnabled(false);
            payBtn.setBackgroundResource(R.drawable.button_unenable);
        }

        if (haveString(addAccountAddress)) {
            deleteAccount.setVisibility(View.VISIBLE);
        } else {
            deleteAccount.setVisibility(View.GONE);
        }

        if (haveString(moneyCount)) {
            deleteMoney.setVisibility(View.VISIBLE);
        } else {
            deleteMoney.setVisibility(View.GONE);
        }

        if (haveString(remarks)) {
            deleteRemarks.setVisibility(View.VISIBLE);
        } else {
            deleteRemarks.setVisibility(View.GONE);
        }
    });


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    /**
     * Add buddy address to transfer
     *
     * @param type
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusType type) {
        Logger.e("-----Add buddy address to transfer-----");
        //Add buddy address to transfer
        if (type.getBusType() == EventBusType.BusType.FRIENDDETAL) {
            addAccountAddress.setText((String) type.getObj());
        }

        //From the homepage scan to the transfer interface
        if (type.getBusType() == EventBusType.BusType.ASSETSTOPAY2) {
            String result = (String) type.getObj();
            Logger.e("This is from scan" + result);
            addAccountAddress.setText((String) type.getObj());
        }

        //Transfer success
        if (type.getBusType() == EventBusType.BusType.ASSETSCHANGEDS) {
            showValue();
            getPresenter().getSdaValue(userBean.getId(), userBean.getAccount(), getActivity(), false);
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            addAccountAddress.setText("");
            moneyCount.setText("");
            remarks.setText("");
        } else {
            showValue();
            getCode();
            if (currencyAdapter != null) {
                currencyAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getCode() {
        if (balanceBeens == null || balanceBeens.size() == 0) {
            balanceBeens = new ArrayList<BalanceBean>();
            BalanceBean balanceBean = new BalanceBean();
            balanceBean.setCurrency(Constants.SDA);
            balanceBean.setCounterparty("");
            balanceBeens.add(balanceBean);
        }
    }

    @Override
    public void getValueSuccess(Object data, String msg) {
        BalancesBean balancesBeans = (BalancesBean) data;
        tools.saveData(balancesBeans.getReserveBase(), Constants.FREEZESDA);
        balanceBeens = balancesBeans.getBalances();
        tools.saveData(balanceBeens, userBean.getUserAccountId());

        showValue();
    }

    @Override
    public void getValueFailed(String msg) {
        Logger.e(msg);
    }

    @Override
    public void showError1() {
        showToast(getString(R.string.pay_error_1));
    }

    @Override
    public void showError2() {
        showToast(getString(R.string.pay_error_2));
    }

    @Override
    public void showError3() {
        showToast(getString(R.string.pay_error_3));
    }

    @Override
    public void showError4() {
        showToast(getString(R.string.pay_error_4));
    }

    @Override
    public void showError5() {
        showToast(getString(R.string.pay_error_5));
    }

    @Override
    public void showError6() {
        showToast(getString(R.string.pay_error_6));
    }


    private RecyclerView recyclerView;
    private CurrencyAdapter currencyAdapter;
    //The current currency
    private static String currency;

    private void showCurrency() {
        View popupView = getActivity().getLayoutInflater().inflate(R.layout.layout_popup_choose_currency, null);
        recyclerView = popupView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        currencyAdapter = new CurrencyAdapter(balanceBeens);
        recyclerView.setAdapter(currencyAdapter);
        final MyPopupWindow window = new MyPopupWindow(popupView, Common.getWidth(getActivity()), Common.getHeight(getActivity()) - Common.dip2px(getActivity(), 70));
        window.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        window.setAnimationStyle(R.style.popup_window_anim);
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        backgroundAlpha(1f);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        window.setOnDismissListener(() -> {
            backgroundAlpha(1f);
            ImageLoader.loadImage(R.mipmap.ic_currency_hide, choose);
        });
        window.showAsDropDown(appbar, 0, 0);
        popupView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    window.dismiss();
                    return true;
                default:
                    break;
            }
            return false;
        });
        currencyAdapter.setOnItemClickListener((v, position) -> {
            currency = balanceBeens.get(position).getCurrency();
            title.setText(currency);

            showValue();

            window.dismiss();
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * According to the amount of
     */
    private void showValue() {
        MoneyBean moneyBean = PayUtils.balance(getActivity(), currency);
        attention.setText(getString(R.string.info392) + " " + moneyBean.getCanUse() + " " + getString(R.string.info393) + " " + moneyBean.getFreeze());
    }

}
