package com.io.sdchain.ui.adapter;

import android.view.View;


import com.io.sdchain.R;
import com.io.sdchain.base.BaseAdapter;
import com.io.sdchain.base.BaseViewHolder;
import com.io.sdchain.bean.TransactionBean;
import com.io.sdchain.utils.ImageLoader;

import java.util.List;

/**
 * @author xiey
 * @date created at 2018/4/18 9:26
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class CreditGrantRecordAdapter extends BaseAdapter<TransactionBean> {

    public CreditGrantRecordAdapter(List<TransactionBean> datas) {
        super(R.layout.item_credit_grant_record, datas);
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position) {
        if (datas != null && datas.size() > 0) {
            ImageLoader.loadImage(datas.get(position).getPic(),R.mipmap.ic_launcher_error, holder.getView(R.id.logo));
            holder.setText(R.id.value, datas.get(position).getValue());
            holder.setText(R.id.code, datas.get(position).getCode());
            holder.setText(R.id.date, datas.get(position).getDate());
            holder.setText(R.id.issuer, datas.get(position).getIssuer());
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
