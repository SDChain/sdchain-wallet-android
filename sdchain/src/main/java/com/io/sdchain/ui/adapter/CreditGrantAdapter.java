package com.io.sdchain.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.io.sdchain.R;
import com.io.sdchain.base.BaseAdapter;
import com.io.sdchain.base.BaseViewHolder;
import com.io.sdchain.bean.CreditGrantBean;
import com.io.sdchain.utils.ImageLoader;

import java.util.List;

/**
 * @author xiey
 * @date created at 2018/4/18 9:26
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class CreditGrantAdapter extends BaseAdapter<CreditGrantBean> {

    public CreditGrantAdapter(List<CreditGrantBean> datas) {
        super(R.layout.item_credit_grant, datas);
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position) {
        if (datas != null && datas.size() > 0) {
            ImageLoader.loadImage(datas.get(position).getPic(), R.mipmap.ic_launcher_error,holder.getView(R.id.tokenLogo));
            holder.setText(R.id.tokenName, datas.get(position).getCurrency());
            holder.setText(R.id.tokenAddress, datas.get(position).getCounterparty());
            holder.getView(R.id.creditGrantSwitch).setOnClickListener(v -> {
                int pos = holder.getLayoutPosition();
                onItemClickListener.onClick(v, pos);
            });

            if (datas.get(position).isTrusted()) {
                ((TextView) holder.getView(R.id.grant)).setText(R.string.info670);
                holder.getView(R.id.grant).setBackgroundResource(R.drawable.selector_grant);
            } else {
                ((TextView) holder.getView(R.id.grant)).setText(R.string.info671);
                holder.getView(R.id.grant).setBackgroundResource(R.drawable.selector_grant_cancel);
            }
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
