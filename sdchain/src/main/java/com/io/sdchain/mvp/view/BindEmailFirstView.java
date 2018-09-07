package com.io.sdchain.mvp.view;

import com.io.sdchain.base.BaseView;

/**
 * @author xiey
 * @date created at 2018/3/6 15:04
 * @package com.io.sdchain.mvp.view
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public interface BindEmailFirstView extends BaseView {

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
