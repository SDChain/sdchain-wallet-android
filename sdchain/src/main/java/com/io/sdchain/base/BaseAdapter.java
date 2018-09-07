package com.io.sdchain.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xieyang on 2018/4/20.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    //data
    protected List<T> datas;
    private int layoutId;
    protected Context context;

    public BaseAdapter(@LayoutRes int layoutId, List<T> datas) {
        this.layoutId=layoutId;
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new BaseViewHolder(view);
    }

    protected abstract void bindData(BaseViewHolder holder, int position);

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        bindData(holder, position);
    }

    public void refresh(List<T> dats) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void addData(List<T> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }
}
