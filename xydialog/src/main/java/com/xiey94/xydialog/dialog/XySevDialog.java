package com.xiey94.xydialog.dialog;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiey94.xydialog.R;
import com.xiey94.xydialog.view.SecurityCodeView;

/**
 * @author xieyang
 * created at 2017/10/26.
 */
public class XySevDialog extends Dialog {
    private Context context;
    private boolean cancelTouchout;
    private View view;

    private XySevDialog(Builder builder) {
        super(builder.context);
        context = builder.context;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }

    private XySevDialog(Builder builder, int resStyle) {
        super(builder.context, resStyle);
        context = builder.context;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }

    public interface OnSevListener {
        void sevComplete();

        void sevDeleteContent(boolean isDelete);

        void sevRefresh(XySevDialog xySevDialog);
    }

    public interface ActionListener {
        void confirm(XySevDialog dialog, String code);

        void cancel(XySevDialog dialog);

        void empty(XySevDialog dialog);

        void refresh(XySevDialog dialog);

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
    }

    public static final class Builder {
        private Context context;
        private boolean cancelTouchout = true;
        private View view;
        private int resStyle = -1;
        private XySevDialog xyDialog2;
        private String title;
        private String hint;
        private boolean isShowSoftKeyboard = true;
        private OnSevListener sevListener;
        private ActionListener actionListener;


        public Builder(Context context) {
            this.context = context;
        }

        public Builder cancelTouchout(boolean cancelTouchout) {
            this.cancelTouchout = cancelTouchout;
            return this;
        }

        public Builder isShowSoftKeyboard(boolean isShowSoftKeyboard) {
            this.isShowSoftKeyboard = isShowSoftKeyboard;
            return this;
        }


        public Builder view(int resView) {
            this.view = LayoutInflater.from(context).inflate(resView, null);
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder title(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder hint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder hint(int hint) {
            this.hint = (String) context.getText(hint);
            return this;
        }


        public Builder setSevListener(OnSevListener sevListener) {
            this.sevListener = sevListener;
            return this;
        }

        public Builder setActionListener(ActionListener actionListener) {
            this.actionListener = actionListener;
            return this;
        }

        public XySevDialog createSeVDialog() {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_layout_code, null);
            TextView hintTV = view.findViewById(R.id.hint);
            if (null != hint) {
                hintTV.setText(hint);
            }

            final SecurityCodeView input = view.findViewById(R.id.securityCode);
            final ImageView imageView = view.findViewById(R.id.codeImage);
            final RelativeLayout refreshCode = view.findViewById(R.id.refreshCode);
            final ImageView refreshImage = view.findViewById(R.id.refreshImage);
            refreshCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sevListener.sevRefresh(xyDialog2);
                    xyDialog2.startRefresh(refreshImage);
                }
            });

            input.setInputCompleteListener(new SecurityCodeView.InputCompleteListener() {
                @Override
                public void inputComplete() {
                    sevListener.sevComplete();
                }

                @Override
                public void deleteContent(boolean isDelete) {
                    sevListener.sevDeleteContent(isDelete);
                }
            });

            input.setFocusable(true);


            if (resStyle != -1) {
                xyDialog2 = new XySevDialog(this, resStyle);
            } else {
                xyDialog2 = new XySevDialog(this, R.style.Dialog);
            }

            if (isShowSoftKeyboard) {
                input.getEditText().setFocusable(true);
                input.getEditText().setFocusableInTouchMode(true);
                input.getEditText().requestFocus();
                input.getEditText().post(new Runnable() {
                    @Override
                    public void
                    run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) ((Activity) context).
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(0,
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                });
            }
            xyDialog2.setImageView(imageView);
            xyDialog2.setSecurityCode(input);

            return xyDialog2;
        }

        public XySevDialog createCodeDialog() {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_layout_image_code, null);
            TextView tvTitle = view.findViewById(R.id.title);
            if (title != null) {
                tvTitle.setText(title);
            }

            final EditText etCode = view.findViewById(R.id.et_code);

            if (actionListener != null) {
                TextView okTv = view.findViewById(R.id.positiveButton);
                okTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code = etCode.getText().toString().trim();
                        if (TextUtils.isEmpty(code)) {
                            actionListener.empty(xyDialog2);
                        } else {
                            actionListener.confirm(xyDialog2, code);
                        }
                    }
                });

                TextView cancelTv = view.findViewById(R.id.negativeButton);
                cancelTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionListener.cancel(xyDialog2);
                    }
                });
            }

            ImageView ivCode = view.findViewById(R.id.iv_code);
            ivCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etCode.setText("");
                    if (actionListener != null) {
                        actionListener.refresh(xyDialog2);
                    }
                }
            });

            if (resStyle != -1) {
                xyDialog2 = new XySevDialog(this, resStyle);
            } else {
                xyDialog2 = new XySevDialog(this, R.style.Dialog);
            }

            xyDialog2.setImageView(ivCode);
            xyDialog2.setEditText(etCode);

            xyDialog2.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    etCode.setText("");
                }
            });

            return xyDialog2;
        }

    }

    private EditText editText;

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public void showInput(){
        if (editText!=null){
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            editText.post(new Runnable() {
                @Override
                public void
                run() {
                    InputMethodManager inputMethodManager = (InputMethodManager) ((Activity) context).
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });
        }
    }

    private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    private ObjectAnimator rotate;

    private void startRefresh(View view) {
        if (rotate == null) {
            rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        }
        rotate.setDuration(1000);
        rotate.setRepeatCount(ValueAnimator.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.start();
    }

    public void stopRefresh() {
        if (rotate != null && rotate.isRunning()) {
            rotate.end();
            rotate.clone();
        }
    }

    private SecurityCodeView securityCode;

    public SecurityCodeView getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(SecurityCodeView securityCode) {
        this.securityCode = securityCode;
    }
}
