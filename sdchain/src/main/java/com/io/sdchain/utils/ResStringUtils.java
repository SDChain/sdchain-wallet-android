package com.io.sdchain.utils;

import android.support.annotation.StringRes;

import com.io.sdchain.base.BaseApplication;

/**
 * @author xiey
 * @date created at 2018/7/11 8:43
 * @package com.io.sdchain.utils
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public class ResStringUtils {
    public static String getString(@StringRes int stringRes) {
        if (stringRes != -1) {
            return BaseApplication.getContext().getString(stringRes);
        }
        return "not found";
    }
}
