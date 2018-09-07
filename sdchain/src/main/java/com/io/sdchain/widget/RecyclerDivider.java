package com.io.sdchain.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author xiey
 * @date created at 2018/2/9 14:21
 * @package com.io.sdchain.utils
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class RecyclerDivider extends RecyclerView.ItemDecoration {
    private int height;

    public RecyclerDivider(int height) {
        this.height = height;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, height, 0, 0);
    }
}
