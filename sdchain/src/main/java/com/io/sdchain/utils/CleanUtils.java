package com.io.sdchain.utils;

import android.content.Context;
import android.text.TextUtils;

import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.common.SharedPref;


/**
 * @author : xiey
 * @project name : sdchian.
 * @package name  : com.io.sdchian.utils.
 * @date : 2018/7/7.
 * @signature : do my best.
 * @explain :
 */
public final class CleanUtils {

    /**
     * clear local save data
     */
    private SharedPref sharedPref;
    private SaveObjectTools tools;

    private CleanUtils(Context context) {
        sharedPref = new SharedPref(context);
        tools = new SaveObjectTools(context);
    }

    public static CleanUtils getInstance(Context context) {
        return new CleanUtils(context);
    }


    /**
     * code info
     */
    public CleanUtils clearCode() {
        if (tools.getObjectData(Constants.CODEINFO) != null) {
            tools.saveData(null, Constants.CODEINFO);
        }
        return this;
    }

    /**
     * user info keep username
     */
    public CleanUtils clearUserInfo() {
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        if (userBean != null) {
            if (userBean.getUserName() != null) {
                userBean = new UserBean(userBean.getUserName());
            } else {
                userBean = null;
            }
            tools.saveData(userBean, Constants.USERINFO);
        }
        return this;
    }

    public CleanUtils clearUserInfo2() {
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        if (userBean != null) {
            if (userBean.getUserName() != null && userBean.getPassword() != null) {
                userBean = new UserBean(userBean.getUserName(), userBean.getPassword());
            } else {
                userBean = null;
            }
            tools.saveData(userBean, Constants.USERINFO);
        }
        return this;
    }


    /**
     * token
     */
    public CleanUtils clearToken() {
        if (sharedPref.getData(Constants.TOKWN) != null) {
            sharedPref.saveData(Constants.TOKWN, null);
        }
        return this;
    }

    /**
     * version
     */
    public CleanUtils clearRemind() {
        if (sharedPref.getData(Constants.REMIND) != null) {
            sharedPref.saveData(Constants.REMIND, null);
        }
        return this;
    }

    /**
     * freeze value
     */
    public CleanUtils clearFreeze() {
        if (tools.getObjectData(Constants.FREEZESDA) != null) {
            tools.saveData(null, Constants.FREEZESDA);
        }
        return this;
    }

    /**
     * coin list,use trade
     */
    public CleanUtils clearCodeList() {
        if (tools.getObjectData(Constants.CODELIST) != null) {
            tools.saveData(null, Constants.CODELIST);
        }
        return this;
    }

    /**
     * remember password
     */
    public CleanUtils clearRememberPwd() {
        if (sharedPref.getData(Constants.REMEMBERPWD) != null) {
            sharedPref.saveData(Constants.REMEMBERPWD, null);
        }
        return this;
    }

    /**
     * balance info
     */
    public CleanUtils clearBalanceInfo() {
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        if (userBean != null && !TextUtils.isEmpty(userBean.getUserAccountId())) {
            if (tools.getObjectData(userBean.getUserAccountId()) != null) {
                tools.saveData(null, userBean.getUserAccountId());
            }
        }
        return this;
    }

    /**
     * wallet list
     *
     * @return
     */
    public CleanUtils clearWalletList() {
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        if (userBean != null && !TextUtils.isEmpty(userBean.getUserName())) {
            if (tools.getObjectData(userBean.getUserName()) != null) {
                tools.saveData(null, userBean.getUserName());
            }
        }
        return this;
    }

    /**
     * login out
     *
     * @return
     */
    public CleanUtils clearLoginOut() {
        if (sharedPref.getData(Constants.LOGINOUT) != null) {
            sharedPref.saveData(Constants.LOGINOUT, null);
        }
        return this;
    }


    public CleanUtils clearAll() {
        clearCode();
        clearToken();
//        clearRemind();
        clearFreeze();
        clearCodeList();
        clearRememberPwd();
        clearBalanceInfo();
        clearWalletList();
        return this;
    }


}
