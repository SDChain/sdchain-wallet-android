package com.io.sdchain.base;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.io.sdchain.utils.ImageLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xieyang on 2018/4/20.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private Map<Integer, View> mViewMap;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mViewMap = new HashMap<>();
    }

    public <T extends View> T getView(@IdRes int id) {
        T view = (T) mViewMap.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViewMap.put(id, view);
        }
        return view;
    }

    public void setText(int id, String text) {
        if (getView(id) instanceof TextView) {
            ((TextView) getView(id)).setText(text);
        }
    }

    public void loadImage(int id, int res) {
        if (getView(id) instanceof ImageView) {
            ImageLoader.loadImage(res, getView(id));
        }
    }

    public void loadImage(int id, String res) {
        if (getView(id) instanceof ImageView) {
            ImageLoader.loadImage(res, getView(id));
        }
    }


}
