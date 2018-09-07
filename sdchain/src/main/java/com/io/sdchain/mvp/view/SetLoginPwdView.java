package com.io.sdchain.mvp.view;

import com.io.sdchain.base.BaseView;

/**
 * Created by xiey on 2017/9/28.
 */

public interface SetLoginPwdView extends BaseView {
    /**
     * update countdown time
     * @param time
     */
    void updateTime(long time);

    /**
     * update countdown over
     */
    void updateTimeOver();

    /**
     * change pay  password success
     *
     * @param data
     * @param msg
     */
    void changePayPwdSuccess(Object data, String msg);

    /**
     * change pay password failed
     *
     * @param msg
     */
    void changePwdFailed(String msg);

    /**
     * password cant empty
     */
    void passwordNotEmpty();

    /**
     * password digits not enough
     */
    void passwordNotComplex();

    /**
     * password not same
     */
    void passwordNotSame();

    /**
     * start change
     */
    void beginChange();

    /**
     * get code success
     *
     * @param data
     * @param msg
     */
    void onCodeSuccess(Object data, String msg);

    /**
     * get code failed
     *
     * @param msg
     */
    void onCodeFailed(String msg);
}
