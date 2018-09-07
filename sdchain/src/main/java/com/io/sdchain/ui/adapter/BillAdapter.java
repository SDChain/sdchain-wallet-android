package com.io.sdchain.ui.adapter;

import android.view.View;

import com.io.sdchain.R;
import com.io.sdchain.base.BaseAdapter;
import com.io.sdchain.base.BaseViewHolder;
import com.io.sdchain.bean.BillBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.utils.TextLimit;
import com.io.sdchain.utils.TimeConverterUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xiey on 2017/9/25.
 */

public final class BillAdapter extends BaseAdapter<BillBean> {

    public BillAdapter(List<BillBean> datas) {
        super(R.layout.item_bill, datas);
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
        if (datas.get(position).getDirection().equals(Constants.OUTGOING)) {
            //roll out
            holder.setText(R.id.billType, context.getString(R.string.info135));
            holder.setText(R.id.translationNum, context.getString(R.string.info136) + TextLimit.limitString(datas.get(position).getDestination(), 9, 9));
        } else if (datas.get(position).getDirection().equals(Constants.INCOMING)) {
            //shift to
            holder.setText(R.id.billType, context.getString(R.string.info137));
            holder.setText(R.id.translationNum, context.getString(R.string.info138) + TextLimit.limitString(datas.get(position).getSource(), 9, 9));
        }
        holder.setText(R.id.billPrice, "" + new BigDecimal(datas.get(position).getAmount().getValue()) + " " + datas.get(position).getSource_currency());

        String time = TimeConverterUtil.timeStamp2Date(datas.get(position).getDate());
        holder.setText(R.id.billTime, time);

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                int pos = holder.getLayoutPosition();
                onItemClickListener.OnItemClick(v, pos);
            });
        }
    }
}
