package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.BalancesBean;
import com.io.sdchain.bean.PayBean;
import com.io.sdchain.common.API;
import com.io.sdchain.common.ErrorCode;
import com.io.sdchain.mvp.view.PayFragmentView;
import com.io.sdchain.net.ResponseCallBack;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

/**
 * Created by xiey on 2017/8/19.
 */

public final class PayFragmentPresenter extends BasePresenter<PayFragmentView> {

    public PayFragmentPresenter(PayFragmentView payFragmentView) {
        super(payFragmentView);
    }


    @Override
    public void closeDisposable() {

    }

    /**
     * pay
     *
     * @param destinationAccount aims account
     * @param value              value
     * @param currency           coin
     * @param memo               memo
     * @param walletPassword     pay password
     * @param userId             user id
     * @param userAccountId      wallet id
     * @param context
     */
    public void payCurrency(String destinationAccount, String value, String currency, String issuer, String memo, String walletPassword, String userId, String userAccountId, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("destinationAccount", destinationAccount);
        map.put("value", value);
        map.put("currency", currency);
        map.put("issuer", issuer);
        map.put("memo", memo);
        map.put("walletPassword", walletPassword);
        map.put("userId", userId);
        map.put("userAccountId", userAccountId);
        postData(API.ISSUECURRENCY, map, true, new ResponseCallBack<PayBean>(context) {
            @Override
            public void onSuccessResponse(PayBean data, String msg) {
                if (getView() != null) {
                    getView().onSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().onFailed(msg);
                }
            }
        }, null, context.getString(R.string.info10), true, context);
    }

    /**
     * get sda asset number
     *
     * @param userId
     * @param account
     * @param context
     * @param isShow
     */
    public void getSdaValue(String userId, String account, Context context, boolean isShow) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("account", account);
        getData(API.GETBALANCE, map, true, new ResponseCallBack<BalancesBean>(context) {
            @Override
            public void onSuccessResponse(BalancesBean data, String msg) {
                if (getView() != null) {
                    getView().getValueSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                Logger.e("get asset failed:" + msg);
                if (getView() != null) {
                    getView().getValueFailed(msg);
                }
            }
        }, null, "", isShow, context);
    }

    public boolean checkInput(int errorCode) {
        if (errorCode == ErrorCode.ERROE1) {
            getView().showError1();
            return false;
        }
        if (errorCode == ErrorCode.ERROE2) {
            getView().showError2();
            return false;
        }
        if (errorCode == ErrorCode.ERROE3) {
            getView().showError3();
            return false;
        }
        if (errorCode == ErrorCode.ERROE4) {
            getView().showError4();
            return false;
        }
        if (errorCode == ErrorCode.ERROE5) {
            getView().showError5();
            return false;
        }
        if (errorCode == ErrorCode.ERROE6) {
            getView().showError6();
            return false;
        }
        return true;
    }
}
