package com.io.sdchain.widget;

/**
 * @author xiey
 * @date created at 2018/4/19 19:03
 * @package com.io.sdchain.customview
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Android7.0 PopupWindow showAsDropDown local question
 * override showAsDropDown method
 */
public final class MyPopupWindow extends PopupWindow {
    Context mContext;
    private WindowManager mWindowManager;

    public MyPopupWindow(View contentView) {
        this(contentView, 0, 0);
    }

    public MyPopupWindow(View contentView, int width, int height) {
        this(contentView, width, height, true);
    }

    public MyPopupWindow(View contentView, int width, int height, boolean focusable) {
        super();
        if (contentView != null) {
            mContext = contentView.getContext();
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }

        setContentView(contentView);
        setWidth(width);
        setHeight(height);
        setFocusable(focusable);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }

}
