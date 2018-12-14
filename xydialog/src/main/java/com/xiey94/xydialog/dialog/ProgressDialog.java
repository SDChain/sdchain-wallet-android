package com.xiey94.xydialog.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.xiey94.xydialog.R;

public class ProgressDialog extends Dialog {

    private Context context;
    private boolean cancelTouchout;
    private View view;

    private ProgressDialog(ProgressDialog.Builder builder) {
        super(builder.context);
        context = builder.context;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }

    private ProgressDialog(ProgressDialog.Builder builder, int resStyle) {
        super(builder.context, resStyle);
        context = builder.context;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }

    public interface OnNoticeClickListener<T> {
        void onNotice(T view, Dialog dialog, int which);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCanceledOnTouchOutside(cancelTouchout);

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        Point point = new Point();
        display.getSize(point);
        if (Build.VERSION.SDK_INT >= 21) {
            lp.width = point.x - 200;
        } else {
            lp.width = point.x - 40;
        }
        win.setAttributes(lp);
        win.setDimAmount(0f);
    }

    public static final class Builder {
        private Context context;
        private boolean cancelTouchout = true;
        private View view;
        private int resStyle = -1;

        private String title;
        private String message;
        private ProgressDialog dialog;

        public Builder(Context context) {
            this.context = context;
        }

        public ProgressDialog.Builder title(String title) {
            this.title = title;
            return this;
        }

        public ProgressDialog.Builder title(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public ProgressDialog.Builder message(String message) {
            this.message = message;
            return this;
        }

        public ProgressDialog.Builder message(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public ProgressDialog.Builder cancelTouchout(boolean cancelTouchout) {
            this.cancelTouchout = cancelTouchout;
            return this;
        }

        public ProgressDialog.Builder view(int resView) {
            this.view = LayoutInflater.from(context).inflate(resView, null);
            return this;
        }

        public ProgressDialog.Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public ProgressDialog progress() {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_layout_progress0, null);
            final ImageView dialogImage = view.findViewById(R.id.dialogImage);
            final Animation rotate = AnimationUtils.loadAnimation(context, R.anim.dialog_rotate);

            dialog = new ProgressDialog(this, R.style.Dialog);
            dialog.setOnShowListener(new OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    dialogImage.startAnimation(rotate);
                }
            });

            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog2) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            return dialog;
        }

    }
}
