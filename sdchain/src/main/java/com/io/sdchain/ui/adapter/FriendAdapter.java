package com.io.sdchain.ui.adapter;

import android.view.View;

import com.io.sdchain.R;
import com.io.sdchain.base.BaseAdapter;
import com.io.sdchain.base.BaseViewHolder;
import com.io.sdchain.bean.FriendBean;

import java.util.List;

/**
 * Created by xiey on 2017/10/11.
 */

public final class FriendAdapter extends BaseAdapter<FriendBean> {

    public FriendAdapter(List<FriendBean> datas) {
        super(R.layout.item_friend,datas);
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position) {
        String word = datas.get(position).getHeaderWord();
        String name = "";
        if (datas.get(position).getRealName() != null && !datas.get(position).getRealName().equals("")) {
            name = datas.get(position).getRealName();
        } else if (datas.get(position).getNickName() != null && !datas.get(position).getNickName().equals("")) {
            name = datas.get(position).getNickName();
        } else {
            name = "";
        }
        if (name.equals("")) {
            holder.setText(R.id.tv_name,datas.get(position).getUserName());
        } else {
            holder.setText(R.id.tv_name,datas.get(position).getName() + " (" + datas.get(position).getUserName() + ")");
        }
        holder.getView(R.id.tv_name).setOnClickListener(v -> {
            if (onClickListener != null) {
                int pos = holder.getLayoutPosition();
                onClickListener.onClick(v, pos);
            }
        });

    }

    //Click on the interface
    public interface OnClickListener {
        void onClick(View v, int position);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
