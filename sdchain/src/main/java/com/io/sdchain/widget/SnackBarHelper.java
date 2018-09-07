package com.io.sdchain.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.io.sdchain.R;

/**
 * @author : xiey
 * @project name : SDChain.
 * @package name  : com.io.sdchain.widget.
 * @date : 2018/8/13.
 * @signature : do my best.
 * @explain :
 */
public final class SnackBarHelper {

    private Snackbar snackbar;

    public void setGravity(int gravity){

    }

    public void show(Context context,View rootView,String info){
        if (snackbar == null) {
            snackbar = Snackbar.make(rootView, info, Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            if (view instanceof FrameLayout) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(view.getLayoutParams().width, view.getLayoutParams().height);
                params.gravity = Gravity.CENTER;
                params.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 32, context.getResources().getDisplayMetrics());
                params.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 32, context.getResources().getDisplayMetrics());
                snackbar.getView().setLayoutParams(params);
            } else {
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(view.getLayoutParams().width, view.getLayoutParams().height);
                params.gravity = Gravity.CENTER;
                params.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 32, context.getResources().getDisplayMetrics());
                params.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 32, context.getResources().getDisplayMetrics());
                snackbar.getView().setLayoutParams(params);
            }
            TextView message = view.findViewById(R.id.snackbar_text);
            message.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            message.setGravity(Gravity.CENTER);
        } else {
            snackbar.setText(info);
        }
        snackbar.show();
    }

}
