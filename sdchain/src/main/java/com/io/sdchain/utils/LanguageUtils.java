package com.io.sdchain.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

/**
 * @author xiey
 * @date created at 2018/7/11 13:17
 * @package com.io.sdchain.utils
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public class LanguageUtils {

    /**
     *
     * @param context
     * @return
     */
    public static String getCountry(Context context) {

        String country;
        Resources resources = context.getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  7.0 up
            country = resources.getConfiguration().getLocales().get(0).getCountry();
        } else {
            //  7.0 low
            country = resources.getConfiguration().locale.getCountry();
        }
        Logger.i("now country:" + country);
        if (!TextUtils.isEmpty(country)) {
            if (country.equals("TW")) {
                return "tc";
            } else if (country.equals("US")//usa
                    || country.equals("GB")//england
                    || country.equals("AU")//Australia
                    || country.equals("CA")//Canada
                    || country.equals("IE")//Ireland
                    || country.equals("IN")//India
                    || country.equals("NZ")//new Zealand
                    || country.equals("SG")//Singapore
                    || country.equals("ZA")//South Africa
                    ) {
                return "en";
            }
        }
        //default chinese
        return "";
    }

}
