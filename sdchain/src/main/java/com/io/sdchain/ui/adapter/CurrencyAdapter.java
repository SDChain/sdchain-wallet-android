package com.io.sdchain.ui.adapter;

import android.view.View;

import com.io.sdchain.R;
import com.io.sdchain.base.BaseAdapter;
import com.io.sdchain.base.BaseViewHolder;
import com.io.sdchain.bean.BalanceBean;

import java.util.List;

/**
 * @author xiey
 * @date created at 2018/4/19 14:16
 * @package com.io.sdchain.adapter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class CurrencyAdapter extends BaseAdapter<BalanceBean> {

    public CurrencyAdapter(List<BalanceBean> datas) {
        super(R.layout.item_currency,datas);
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position) {
        if (datas != null && datas.size() >= 0) {
            holder.setText(R.id.currency,datas.get(position).getCurrency());
        }
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                int pos = holder.getLayoutPosition();
                onItemClickListener.onItemClick(v, pos);
            });
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
