package com.io.sdchain.mvp.view;

import com.io.sdchain.base.BaseView;

/**
 * @author xiey
 * @date created at 2018/3/21 8:54
 * @package com.io.sdchain.mvp.view
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public interface SetWalletPwdView extends BaseView{

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
     * begin change
     */
    void beginChange();
}
