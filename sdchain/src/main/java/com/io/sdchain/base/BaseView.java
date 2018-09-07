package com.io.sdchain.base;

/**
 * Created by xiey on 2017/9/21.
 */

public interface BaseView {

    /**
     * success
     *
     * @param data
     * @param msg
     */
    void onSuccess(Object data, String msg);

    /**
     * failed
     *
     * @param msg
     */
    void onFailed(String msg);

}
