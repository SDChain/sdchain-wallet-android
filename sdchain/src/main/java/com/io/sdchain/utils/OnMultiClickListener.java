package com.io.sdchain.utils;

import android.view.View;

import java.util.Calendar;

/**
 * @author : xiey
 * @project name : SDChain.
 * @package name  : com.io.sdchain.utils.
 * @date : 2018/8/20.
 * @signature : do my best.
 * @explain :
 */
public abstract class OnMultiClickListener implements View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 2000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onMultiClick(v);
        }
    }

    public abstract void onMultiClick(View v);
}
