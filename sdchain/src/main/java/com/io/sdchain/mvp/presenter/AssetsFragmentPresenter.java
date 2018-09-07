package com.io.sdchain.mvp.presenter;

import android.content.Context;

import com.io.sdchain.R;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.AccountBean;
import com.io.sdchain.bean.AccountsBean;
import com.io.sdchain.bean.BalancesBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.bean.VersionBean;
import com.io.sdchain.common.API;
import com.io.sdchain.common.Constants;
import com.io.sdchain.mvp.view.AssetsFragmentView;
import com.io.sdchain.net.ResponseCallBack;
import com.io.sdchain.utils.SaveObjectTools;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

/**
 * @author xiey
 * @date created at 2018/3/7 14:55
 * @package com.io.sdchain.mvp.presenter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class AssetsFragmentPresenter extends BasePresenter<AssetsFragmentView> {

    public AssetsFragmentPresenter(AssetsFragmentView assetsFragmentView) {
        super(assetsFragmentView);
    }

    @Override
    public void closeDisposable() {

    }

    /**
     * get sda balance
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
                    getView().onSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                Logger.e("-----Failed to get assets-----:" + msg);
                if (getView() != null) {
                    getView().onFailed(msg);
                }
            }
        }, null, "", isShow, context);
    }

    /**
     * get sda balance after avtice by manual
     *
     * @param userId
     * @param account
     * @param context
     * @param isShow
     */
    public void getSdaValueAfterActive(String userId, String account, Context context, boolean isShow) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("account", account);
        getData(API.GETBALANCE, map, true, new ResponseCallBack<BalancesBean>(context) {
            @Override
            public void onSuccessResponse(BalancesBean data, String msg) {
                if (getView() != null) {
                    getView().onSuccessAfterActive(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                Logger.e("-----Failed to Get Assets After Manual Activation-----:" + msg);
                if (getView() != null) {
                    getView().onFailedAfterActive(msg);
                }
            }
        }, null, "", isShow, context);
    }

    /**
     * get sda balance after change wallet
     *
     * @param userId
     * @param account
     * @param context
     * @param isShow
     */
    public void getSdaValueAfterChangeAccount(String userId, String account, Context context, boolean isShow) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("account", account);
        getData(API.GETBALANCE, map, true, new ResponseCallBack<BalancesBean>(context) {
            @Override
            public void onSuccessResponse(BalancesBean data, String msg) {
                if (getView() != null) {
                    Logger.e("-----getSdaValueAfterChangeAccount success-----");
                    getView().onSuccessAfterChangeAccount(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                Logger.e("-----Failed to Get Assets After change account-----:" + msg);
                if (getView() != null) {
                    getView().onFailedAfterChangeAccount(msg);
                }
            }
        }, null, "", isShow, context);
    }

    /**
     * active wallet address
     *
     * @param userAccountId
     * @param validStr
     * @param context
     * @param isShow
     */
    public void activeAccount(String userAccountId, String validStr, Context context, boolean isShow) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userAccountId", userAccountId);
        map.put("validStr", validStr);
        postData(API.ACTIVATION, map, true, new ResponseCallBack<Object>(context) {
            @Override
            public void onSuccessResponse(Object data, String msg) {
                SaveObjectTools tools = new SaveObjectTools(context);
                UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
                userBean.setType("1");
                tools.saveData(userBean, Constants.USERINFO);
                if (getView() != null) {
                    getView().activeSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().activeFailed(msg);
                }
            }
        }, null, context.getString(R.string.info28), isShow, context);
    }

    /**
     * get wallet address list
     *
     * @param userId
     * @param context
     * @param isShow
     */
    public void accountsList(String userId, Context context, boolean isShow) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        getData(API.WALLETLIST, map, true, new ResponseCallBack<AccountsBean>(context) {
            @Override
            public void onSuccessResponse(AccountsBean data, String msg) {
                if (getView() != null) {
                    getView().walletListSuccess(data.getWalletList(), msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().walletListFailed(msg);
                }
            }
        }, null, "", isShow, context);
    }

    /**
     * change default
     */
    public void changeDefaultWallet(String id, String newAccount, Context context, boolean isShow) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("account", newAccount);
        postData(API.CHANGEDEFAULTWALLET, map, true, new ResponseCallBack<AccountBean>(context) {
            @Override
            public void onSuccessResponse(AccountBean data, String msg) {
                Logger.e("-----Change wallet successfully-----:" + data.toString());
                if (getView() != null) {
                    getView().changeDefaultWalletSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                Logger.e("-----Failed to change wallet-----:" + msg);
                if (getView() != null) {
                    getView().changeDefaultWalletFailed(msg);
                }
            }
        }, null, "", isShow, context);
    }

    /**
     * get software version
     */
    public void getApkVersion(String versionCode, Context context, boolean isShow) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", "0");
        map.put("versionCode", versionCode);
        getData(API.CHECKVERSION, map, true, new ResponseCallBack<VersionBean>(context) {
            @Override
            public void onSuccessResponse(VersionBean data, String msg) {
                if (getView() != null) {
                    getView().versionSuccess(data, msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                if (getView() != null) {
                    getView().versionFailed(msg);
                }
            }
        }, context.getString(R.string.info29), null, isShow, context);
    }

}
