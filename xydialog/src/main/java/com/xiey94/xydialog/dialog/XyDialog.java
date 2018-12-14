package com.xiey94.xydialog.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiey94.xydialog.R;
import com.xiey94.xydialog.util.XyCommon;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xiey on 2017/10/25.
 */

public class XyDialog extends Dialog {

    public XyDialog(@NonNull Context context) {
        super(context);
    }

    public XyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected XyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public interface OnDialogEditClickListener {
        /**
         *
         * @param input
         * @param dialog
         * @param confirm
         */
        void onPositiveButonListener(EditText input, Dialog dialog, int confirm);
    }

    public interface OnDialogChooseClickListener {
        /**
         *
         * @param choose
         * @param dialog
         * @param whitch
         */
        void onChoose(TextView choose, Dialog dialog, int whitch);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        public OnDialogEditClickListener onDialogClickListener;
        private String hint;
        private boolean isShow;
        private boolean isInputType;
        private OnDialogChooseClickListener onDialogChooseClickListener;
        private List<String> chooseList;
        private boolean isShowSoftKeyboard = true;
        private String editContent;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setHint(int hint) {
            this.hint = (String) context.getText(hint);
            return this;
        }

        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder setIsShow(boolean isShow) {
            this.isShow = isShow;
            return this;
        }

        public Builder setIsInputType(boolean isInputType) {
            this.isInputType = isInputType;
            return this;
        }

        public Builder setEditContent(String editContent) {
            this.editContent = editContent;
            return this;
        }

        public Builder isShowSoftKeyboard(boolean isShowSoftKeyboard) {
            this.isShowSoftKeyboard = isShowSoftKeyboard;
            return this;
        }

        public Builder setOnChooseOneButton(List<String> chooseList, OnDialogChooseClickListener listener) {
            this.chooseList = chooseList;
            this.onDialogChooseClickListener = listener;
            return this;
        }

        public Builder setOnChooseOneButton(int resArray, OnDialogChooseClickListener listener) {
            String[] items = context.getResources().getStringArray(resArray);
            this.chooseList = Arrays.asList(items);
            this.onDialogChooseClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnDialogEditClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.onDialogClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        private void setDialogWidth(Dialog dialog) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            Point point = new Point();
            display.getSize(point);
            if (Build.VERSION.SDK_INT >= 21) {
                lp.width = point.x - 200;
            } else {
                lp.width = point.x - 40;
            }
            dialog.getWindow().setAttributes(lp);
        }

        private void setContentViewEnd(View contentView, View layout, Dialog dialog) {
            if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            dialog.setContentView(layout);
        }

        public XyDialog createWarn() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final XyDialog dialog = new XyDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_layout, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            setDialogWidth(dialog);

            ((TextView) layout.findViewById(R.id.title)).setText(title);
            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((TextView) layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
            }

            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((TextView) layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog, BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
            }

            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            }

            setContentViewEnd(contentView, layout, dialog);
            return dialog;
        }

        public XyDialog createEdit() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final XyDialog dialog = new XyDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_layout_edit, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            setDialogWidth(dialog);

            ((TextView) layout.findViewById(R.id.title)).setText(title);
            final EditText input = layout.findViewById(R.id.message);
            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);

                if (hint != null) {
                    input.setHint(hint);
                }

                if (editContent != null) {
                    input.setText(editContent);
                }

                if (!isShow) {
                    input.setTransformationMethod(new PasswordTransformationMethod());
                }
                if (isInputType) {
                    input.setKeyListener(new NumberKeyListener() {
                        @Override
                        protected char[] getAcceptedChars() {
                            return new char[]{
                                    '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
                                    'a', 'b', 'c', 'd', 'e', 'f', 'g',
                                    'h', 'i', 'j', 'k', 'l', 'm', 'n',
                                    'o', 'p', 'q', 'r', 's', 't',
                                    'u', 'v', 'w', 'x', 'y', 'z',
                                    'A', 'B', 'C', 'D', 'E', 'F', 'G',
                                    'H', 'I', 'J', 'K', 'L', 'M', 'N',
                                    'O', 'P', 'Q', 'R', 'S', 'T',
                                    'U', 'V', 'W', 'X', 'Y', 'Z'
                            };
                        }

                        @Override
                        public int getInputType() {
                            return InputType.TYPE_CLASS_PHONE;
                        }
                    });
                }
                if (onDialogClickListener != null) {
                    ((TextView) layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onDialogClickListener.onPositiveButonListener(input, dialog, BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
            }

            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((TextView) layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog, BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
            }

            setContentViewEnd(contentView, layout, dialog);
            if (isShowSoftKeyboard) {
                input.setFocusable(true);
                input.setFocusableInTouchMode(true);
                input.requestFocus();
                input.post(new Runnable() {
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
            return dialog;
        }

        public XyDialog createChoose() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final XyDialog dialog = new XyDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_layout_choose, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            setDialogWidth(dialog);
            if (title != null) {
                ((TextView) layout.findViewById(R.id.title)).setText(title);
            }

            LinearLayout linear = layout.findViewById(R.id.linear);
            for (final String s : chooseList) {
                final TextView textView = new TextView(context);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(lp2);
                textView.setText(s);
                textView.setPadding(40, 25, 40, 25);
                if (Build.VERSION.SDK_INT >= 21) {
                    textView.setTextSize(18);
                } else {
                    textView.setTextSize(XyCommon.dip2px(context, 10));
                }
                textView.setBackgroundResource(R.drawable.xy_selector_text);
                if (onDialogChooseClickListener != null) {
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int which = chooseList.indexOf(s);
                            onDialogChooseClickListener.onChoose(textView, dialog, which);
                        }
                    });
                }
                linear.addView(textView);
            }

            setContentViewEnd(contentView, layout, dialog);
            return dialog;


        }

    }

    public void showE(EditText edit) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);
    }
}
