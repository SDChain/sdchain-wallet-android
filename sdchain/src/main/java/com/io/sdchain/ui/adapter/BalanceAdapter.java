package com.io.sdchain.ui.adapter;

import android.view.View;

import com.io.sdchain.R;
import com.io.sdchain.base.BaseAdapter;
import com.io.sdchain.base.BaseViewHolder;
import com.io.sdchain.bean.BalanceBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.utils.ImageLoader;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xiey
 * @date created at 2018/4/4 17:29
 * @package com.io.sdchain.adapter
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class BalanceAdapter extends BaseAdapter<BalanceBean> {

    public BalanceAdapter(List<BalanceBean> datas) {
        super(R.layout.item_balances, datas);
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position) {
        if (datas != null) {
            if (datas.get(position) != null) {
                if (datas.get(position).getCurrency() != null) {
                    holder.setText(R.id.currency, datas.get(position).getCurrency());
                }

                if (datas.get(position).getValue() != null) {
                    holder.setText(R.id.value, "" + new BigDecimal(datas.get(position).getValue()).setScale(6, BigDecimal.ROUND_HALF_UP));
                }
                if (datas.get(position).getCurrency().equals(Constants.SDA)) {
                    ImageLoader.loadImage(datas.get(position).getPic(), R.mipmap.ic_sda_3, holder.getView(R.id.balanceLogo));
                } else {
                    ImageLoader.loadImage(datas.get(position).getPic(), R.mipmap.ic_launcher_error, holder.getView(R.id.balanceLogo));
                }
            }
        }

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                int pos = holder.getLayoutPosition();
                onItemClickListener.OnItemClick(v, pos);
            });
        }
    }

}

