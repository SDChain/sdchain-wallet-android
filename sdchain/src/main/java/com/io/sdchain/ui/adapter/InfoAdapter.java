package com.io.sdchain.ui.adapter;

import android.view.View;


import com.io.sdchain.R;
import com.io.sdchain.base.BaseAdapter;
import com.io.sdchain.base.BaseViewHolder;
import com.io.sdchain.bean.InfoBean;
import com.io.sdchain.utils.Common;

import java.util.List;

/**
 * @author xiey
 * @date created at 2018/4/18 9:26
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class InfoAdapter extends BaseAdapter<InfoBean> {

    public InfoAdapter(List<InfoBean> datas) {
        super(R.layout.item_credit_grant_info, datas);
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position) {
        if (datas != null && datas.size() > 0) {
            holder.setText(R.id.txt_title, datas.get(position).getTitle());
            holder.setText(R.id.txt_read, datas.get(position).getReadNum()+context.getResources().getString(R.string.key000276));
            holder.setText(R.id.txt_time, Common.getTimeDate(datas.get(position).getTimeDiff(),context));
            holder.getView(R.id.layout_item).setOnClickListener(v -> {
                int pos = holder.getLayoutPosition();
                if(onItemClickListener!=null) {
                    onItemClickListener.onClick(v, pos);
                }
            });

//            if (datas.get(position).isTrusted()) {
//                ((TextView) holder.getView(R.id.grant)).setText(R.string.info670);
//                holder.getView(R.id.grant).setBackgroundResource(R.drawable.selector_grant);
//            } else {
//                ((TextView) holder.getView(R.id.grant)).setText(R.string.info671);
//                holder.getView(R.id.grant).setBackgroundResource(R.drawable.selector_grant_cancel);
//            }
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
