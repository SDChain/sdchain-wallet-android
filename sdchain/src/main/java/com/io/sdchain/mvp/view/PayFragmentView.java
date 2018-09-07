package com.io.sdchain.mvp.view;


import com.io.sdchain.base.BaseView;

/**
 * Created by xiey on 2017/8/19.
 */

public interface PayFragmentView extends BaseView {

    /**
     * get asset success
     *
     * @param data
     * @param msg
     */
    void getValueSuccess(Object data, String msg);

    /**
     * get asset failed
     *
     * @param msg
     */
    void getValueFailed(String msg);

    void showError1();

    void showError2();

    void showError3();

    void showError4();

    void showError5();

    void showError6();
}
