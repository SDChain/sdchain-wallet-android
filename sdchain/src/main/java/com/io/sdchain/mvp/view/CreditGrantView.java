package com.io.sdchain.mvp.view;


import com.io.sdchain.base.BaseView;

/**
 * @author xiey
 * @date created at 2018/4/18 9:00
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public interface CreditGrantView extends BaseView {

    /**
     * trust success
     *
     * @param data
     * @param msg
     */
    void creditGrantSuccess(Object data, String msg);

    /**
     * trust failed
     *
     * @param msg
     */
    void creditGrantFailed(String msg);

    /**
     * cancel trust success
     *
     * @param data
     * @param msg
     */
    void cancelCreditGrantSuccess(Object data, String msg);

    /**
     * cancel trust failed
     *
     * @param msg
     */
    void cancelCreditGrantFailed(String msg);

    void pwdWrong1(String msg);

    void pwdWrong2(String msg);

    void pwdWrong3(String msg);

    void pwdWrong4(String msg);
}
