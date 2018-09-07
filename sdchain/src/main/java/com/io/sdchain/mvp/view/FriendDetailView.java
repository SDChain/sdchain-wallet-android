package com.io.sdchain.mvp.view;

import com.io.sdchain.base.BaseView;

/**
 * @author xiey
 * @date created at 2018/3/13 10:49
 * @package com.io.sdchain.mvp.view
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public interface FriendDetailView extends BaseView {

    /**
     * add friend success
     *
     * @param data
     * @param msg
     */
    void addFriendSuccess(Object data, String msg);

    /**
     * add friend failed
     *
     * @param msg
     */
    void addFriendFailed(String msg);

    /**
     * delete friend success
     *
     * @param data
     * @param msg
     */
    void deleteFriendSuccess(Object data, String msg);

    /**
     * delete friend failed
     *
     * @param msg
     */
    void deleteFriendFailed(String msg);
}
