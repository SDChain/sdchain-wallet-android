package com.io.sdchain.utils;

/**
 * @author xiey
 * @date created at 2018/3/9 9:40
 * @package com.io.sdchain.utils
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

import android.text.TextUtils;

/**
 * Word limit
 */
public class TextLimit {
    /**
     * @param limitStr To limit all strings to display
     * @param start    Start somewhere
     * @param end      It's going to end somewhere in the inverse
     * @return The string to limit
     */
    public static String limitString(String limitStr, int start, int end) {
        return limitString(limitStr, "....", start, end);
    }

    public static String limitString(String limitStr, String mark, int start, int end) {
        if (TextUtils.isEmpty(limitStr)) return "";
        int length = limitStr.length();
        String newStr = limitStr.substring(0, start) + mark + limitStr.substring(length - end, length);
        return newStr;
    }
}
