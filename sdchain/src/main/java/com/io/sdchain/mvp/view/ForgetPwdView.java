package com.io.sdchain.mvp.view;

import com.io.sdchain.base.BaseView;

/**
 * Created by xiey on 2017/9/21.
 */

public interface ForgetPwdView extends BaseView{
    /**
     * update countdown time
     *
     * @param time
     */
    void updateTime(long time);

    /**
     * update countdown over
     */
    void updateTimeOver();

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
