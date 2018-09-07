package com.io.sdchain.mvp.view;

import com.io.sdchain.base.BaseView;

/**
 * @author xiey
 * @date created at 2018/3/7 14:55
 * @package com.io.sdchain.mvp.view
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public interface AssetsFragmentView extends BaseView {

    /**
     * active success
     *
     * @param data
     * @param msg
     */
    void activeSuccess(Object data, String msg);

    /**
     * active failed
     *
     * @param msg
     */
    void activeFailed(String msg);

    /**
     * get wallet list success
     *
     * @param data
     * @param msg
     */
    void walletListSuccess(Object data, String msg);

    /**
     * get wallet list failed
     *
     * @param msg
     */
    void walletListFailed(String msg);

    /**
     * change default wallet success
     *
     * @param data
     * @param msg
     */
    void changeDefaultWalletSuccess(Object data, String msg);

    /**
     * change default wallet failed
     *
     * @param msg
     */
    void changeDefaultWalletFailed(String msg);

    /**
     * get current version success
     *
     * @param data
     * @param msg
     */
    void versionSuccess(Object data, String msg);

    /**
     * get current version failed
     *
     * @param msg
     */
    void versionFailed(String msg);

    /**
     * after manual active get balance success
     *
     * @param data
     * @param msg
     */
    void onSuccessAfterActive(Object data, String msg);

    /**
     * after manual active get balance failed
     *
     * @param msg
     */
    void onFailedAfterActive(String msg);

    /**
     * after change wallet get balance success
     *
     * @param data
     * @param msg
     */
    void onSuccessAfterChangeAccount(Object data, String msg);

    /**
     * after change wallet get balance failed
     *
     * @param msg
     */
    void onFailedAfterChangeAccount(String msg);

}
