package com.io.sdchain.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.io.sdchain.utils.Common;

/**
 * @author xiey
 * @date created at 2018/2/7 13:47
 * @package com.io.sdchain.widget
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class SectionDecoration2 extends RecyclerView.ItemDecoration {
    private DecorationCallBack callBack;
    private TextPaint textPaint;
    private Paint paint;
    private int topGap;

    public SectionDecoration2(Context mContext, DecorationCallBack callBack) {
        this.callBack = callBack;
        paint = new Paint();
        paint.setColor(Color.parseColor("#efeff5"));

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(Common.dip2px(mContext, 14));
        textPaint.setColor(Color.parseColor("#63AEFF"));
        textPaint.setTextAlign(Paint.Align.LEFT);
        topGap = Common.dip2px(mContext, 25);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        long groupId = callBack.getGroupId(pos);
        if (groupId < 0) return;
        if (pos == 0 || isFirstInGroup(pos)) {
            outRect.top = topGap;
        } else {
            outRect.top = 0;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        long preGroupId, groupId = -1;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            preGroupId = groupId;
            groupId = callBack.getGroupId(position);

            if (groupId < 0 || groupId == preGroupId) continue;

            String textLine = callBack.getGroupFirstLine(position).toUpperCase();
            if (TextUtils.isEmpty(textLine)) continue;

            int viewBottom = view.getBottom();

            float textY = Math.max(topGap, view.getTop());
            if (position + 1 < itemCount) {
                long nextGroupId = callBack.getGroupId(position + 1);
                if (nextGroupId != groupId && viewBottom < textY) {
                    textY = viewBottom;
                }
            }

            int baseline = (int) (topGap / 2 + (textPaint.ascent() + textPaint.descent()) / 2);

            c.drawRect(left, textY - topGap, right, textY, paint);
            c.drawText(textLine, left + 20, textY - baseline, textPaint);
        }
    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            long prevGroupId = callBack.getGroupId(pos - 1);
            long groupId = callBack.getGroupId(pos);
            return prevGroupId != groupId;
        }
    }

    public interface DecorationCallBack {
        long getGroupId(int position);

        String getGroupFirstLine(int position);
    }
}
