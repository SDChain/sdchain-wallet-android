package com.xiey94.xydialog.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiey94.xydialog.R;

import java.lang.reflect.Field;

/**
 * @author xiey
 * @date created at 2018/3/20 10:14
 * @package com.xiey94.simple
 * @project XyDialog
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public class PasswordInputView extends AppCompatEditText {

    private int passwordLength = 6;
    private int borderWidth = 8; //px
    private int borderRadius = 6; //four conner px
    private int borderColor = 0xff333333;
    private int passwordWidth = 20; //px
    private int passwordColor = 0xff000000;
    private int defaultSplitLineWidth = 3; //px

    private Paint borderPaint;
    private Paint passwordPaint;

    private int textLength;
    private int defaultContentMargin = 2;
    private String originText;
    private OnFinishListener onFinishListener;

    public PasswordInputView(Context context) {
        this(context, null);
    }

    public PasswordInputView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context, attr);
        setLongClickable(false);
        setTextIsSelectable(false);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // setInsertionDisabled when user touches the view
                    setInsertionDisabled(PasswordInputView.this);
                }
                return false;
            }
        });
        
    }

    private void setInsertionDisabled(EditText editText) {
        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editorObject = editorField.get(editText);
            // if this view supports insertion handles
            Class editorClass = Class.forName("android.widget.Editor");
            Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
            mInsertionControllerEnabledField.setAccessible(true);
            mInsertionControllerEnabledField.set(editorObject, false);
            // if this view supports selection handles
            Field mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled");
            mSelectionControllerEnabledField.setAccessible(true);
            mSelectionControllerEnabledField.set(editorObject, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Context context, AttributeSet attr) {
        TypedArray ta = context.obtainStyledAttributes(attr, R.styleable.PasswordInputView);
        try {
            passwordLength = ta.getInt(R.styleable.PasswordInputView_passwordLength, passwordLength);
            borderWidth = ta.getDimensionPixelSize(R.styleable.PasswordInputView_borderWidth, borderWidth);
            borderRadius = ta.getDimensionPixelSize(R.styleable.PasswordInputView_borderRadius, borderRadius);
            borderColor = ta.getColor(R.styleable.PasswordInputView_borderColor, borderColor);
            passwordWidth = ta.getDimensionPixelSize(R.styleable.PasswordInputView_passwordWidth, passwordWidth);
            passwordColor = ta.getColor(R.styleable.PasswordInputView_passwordColor, passwordColor);
        } catch (Exception e) {

        }
        ta.recycle();

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStyle(Paint.Style.FILL);

        passwordPaint = new Paint();
        passwordPaint.setAntiAlias(true);
        passwordPaint.setColor(passwordColor);
        passwordPaint.setStrokeWidth(passwordWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        RectF rect = new RectF(0, 0, width, height);
        borderPaint.setColor(borderColor);
        canvas.drawRoundRect(rect, borderRadius, borderRadius, borderPaint);

        RectF rectContent = new RectF(rect.left + defaultContentMargin, rect.top + defaultContentMargin, rect.right - defaultContentMargin, rect.bottom - defaultContentMargin);
        borderPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectContent, borderRadius, borderRadius, borderPaint);

        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(defaultSplitLineWidth);
        for (int i = 1; i < passwordLength; i++) {
            float x = width * i / passwordLength;
            canvas.drawLine(x, 0, x, height, borderPaint);
        }

        float px, py = height / 2;
        float halfWidth = width / passwordLength / 2;
        for (int i = 0; i < textLength; i++) {
            px = width * i / passwordLength + halfWidth;
            canvas.drawCircle(px, py, passwordWidth, passwordPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        textLength = text.length();
        invalidate();
        onInputFinished(text);
    }

    public void onInputFinished(CharSequence text) {
        if (text != null) {
            originText = text.toString();
            if (onFinishListener != null) {
                onFinishListener.setOnPasswordFinished(originText);
            }
        }
    }

    /**
     *
     * @return
     */
    public int getMaxPasswordLength() {
        return passwordLength;
    }

    /**
     *
     * @return
     */
    public String getOriginText() {
        return originText;
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        postInvalidate();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        borderPaint.setStrokeWidth(borderWidth);
        postInvalidate();
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        postInvalidate();
    }

    public float getBorderRadius() {
        return borderRadius;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        postInvalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setPasswordWidth(int passwordWidth) {
        this.passwordWidth = passwordWidth;
        passwordPaint.setStrokeWidth(passwordWidth);
        postInvalidate();
    }

    public float getPasswordWidth() {
        return passwordWidth;
    }

    public void setPasswordColor(int passwordColor) {
        this.passwordColor = passwordColor;
        passwordPaint.setColor(passwordColor);
        postInvalidate();
    }

    public int getPasswordColor() {
        return passwordColor;
    }

    public interface OnFinishListener {
        void setOnPasswordFinished(String password);
    }
}
