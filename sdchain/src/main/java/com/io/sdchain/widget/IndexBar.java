package com.io.sdchain.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.io.sdchain.utils.Common;
import com.orhanobut.logger.Logger;

import static android.graphics.Paint.SUBPIXEL_TEXT_FLAG;

/**
 * Created by xiey on 2017/10/11.
 */

public final class IndexBar extends View {
    public static final float TEXT_SIZE = 1F / 28F;
    public static final float CIRCLE_RADIUS = 1F / 45F;
    /**
     * char paint
     */
    private TextPaint wordsPaint;
    /**
     * char  background paint
     */
    private Paint bgPaint;
    /**
     * char width
     */
    private int itemWidth;
    /**
     * char height
     */
    private int itemHeight;
    /**
     * press char index
     */
    private int touchIndex = 0;
    /**
     * pressed char change interface
     */
    private onWordsChangeListener listener;
    /**
     * paint size
     */
    private float textSize;
    /**
     * index circle radius
     */
    private float circleRadius;
    /**
     * screen width/height
     */
    private int width;
    /**
     * text Y offset value
     */
    private float textOffsetY;

    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        wordsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | SUBPIXEL_TEXT_FLAG);
        wordsPaint.setColor(Color.parseColor("#F7F7F7"));
        wordsPaint.setAntiAlias(true);
        wordsPaint.setTextAlign(Paint.Align.CENTER);

        wordsPaint.setTypeface(Typeface.DEFAULT_BOLD);

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.parseColor("#1dcdef"));
    }

    /**
     * draw navigation char
     */
    private String[] words = {
            "#", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    /**
     * pressed interface
     */
    public interface onWordsChangeListener {
        void wordsChange(String word);
    }

    /**
     * set listener
     */
    public void setListener(onWordsChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //set width/height to screen width/height
        width = Common.getWidth(getContext());

        Logger.e("" + w + "----" + width);

        //calculation param--paint size
        textSize = TEXT_SIZE * width;
        wordsPaint.setTextSize(textSize);

//        circleRadius = CIRCLE_RADIUS * width;

        textOffsetY = (wordsPaint.descent() + wordsPaint.ascent()) / 2;
    }

    /**
     * get canvas width,each char height
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //get measure width
        itemWidth = getMeasuredWidth();
        //add margin
        int height = getMeasuredHeight() - 10;
        itemHeight = height / 27;

        //promise background circle in draw range
        circleRadius = Math.min(itemWidth, itemHeight) / 2;

    }

    /**
     * draw char and background
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < words.length; i++) {
            //judgment pressed is current char
            if (touchIndex == i) {
                //draw text circle background
                canvas.drawCircle(itemWidth / 2, itemHeight / 2 + i * itemHeight, circleRadius, bgPaint);
                wordsPaint.setColor(Color.WHITE);
            } else {
                wordsPaint.setColor(Color.GRAY);
            }
            //draw char
            float wordX = itemWidth / 2;
            float wordY = itemHeight / 2 + i * itemHeight - textOffsetY;
            canvas.drawText(words[i], wordX, wordY, wordsPaint);
        }
    }

    /**
     * change char background color when pressed
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                //get index(char) when pressed
                int index = (int) (y / itemHeight);
                if (index != touchIndex) {
                    touchIndex = index;
                }
                //prevent array out of size
                if (listener != null && 0 <= touchIndex && touchIndex <= words.length - 1) {
                    //callback pressed char
                    listener.wordsChange(words[touchIndex]);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * set char when pressed
     */
    public void setTouchIndex(String word) {
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word)) {
                touchIndex = i;
                invalidate();
                return;
            }
        }
    }
}
