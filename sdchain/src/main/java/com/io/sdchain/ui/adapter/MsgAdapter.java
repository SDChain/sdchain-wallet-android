package com.io.sdchain.ui.adapter;

import android.view.View;

import com.io.sdchain.R;
import com.io.sdchain.base.BaseAdapter;
import com.io.sdchain.base.BaseViewHolder;
import com.io.sdchain.bean.MsgBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.utils.TextLimit;
import com.io.sdchain.utils.TimeConverterUtil;

import java.util.List;


/**
 * Created by xiey on 2017/9/28.
 */

public final class MsgAdapter extends BaseAdapter<MsgBean> {

    //edit state
    private boolean isEdit;

    public MsgAdapter(List<MsgBean> datas, boolean isEdit) {
        super(R.layout.item_msg, datas);
        this.isEdit = isEdit;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(View view, int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position) {
        //title
        String msgTitleStr = "";
        if (datas.get(position).getDirection().equals(Constants.OUTGOING)) {
            msgTitleStr += context.getString(R.string.info172);
        } else {
            msgTitleStr += context.getString(R.string.info173);
        }
        msgTitleStr += datas.get(position).getValue() + " "+datas.get(position).getCurrency();
        if (datas.get(position).getDirection().equals(Constants.OUTGOING)) {
            msgTitleStr += context.getString(R.string.info174);
        } else {
            msgTitleStr += context.getString(R.string.info175);
        }

        holder.setText(R.id.msgTitle,"" + msgTitleStr);
        //state
        if (datas.get(position).isReadStatus()) {
            holder.getView(R.id.msgStatus).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.msgStatus).setVisibility(View.VISIBLE);
        }
        //time
        String time = TimeConverterUtil.timeStamp2Date(datas.get(position).getDate());
        holder.setText(R.id.msgTime,time);

        String msgContentStr = "";
        if (datas.get(position).getDirection().equals(Constants.OUTGOING)) {
            //roll out
            msgContentStr = context.getString(R.string.info176) + TextLimit.limitString(datas.get(position).getDestination_account(), 9, 9);
        } else if (datas.get(position).getDirection().equals(Constants.INCOMING)) {
            //shift to
            msgContentStr = context.getString(R.string.info177) + TextLimit.limitString(datas.get(position).getSource_account(), 9, 9);
        }

        holder.setText(R.id.msgContent,msgContentStr);
        if (mListener != null) {
            holder.itemView.setOnClickListener(v -> {
                int pos = holder.getLayoutPosition();
                mListener.OnItemClickListener(holder.itemView, pos);
            });
        }
    }

}
