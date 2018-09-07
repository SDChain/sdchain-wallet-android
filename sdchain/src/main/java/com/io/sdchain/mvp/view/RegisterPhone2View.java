package com.io.sdchain.mvp.view;

import com.io.sdchain.base.BaseView;

/**
 * Created by xiey on 2017/9/21.
 */

public interface RegisterPhone2View extends BaseView {

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
     * start register
     */
    void beginRegister();

}
