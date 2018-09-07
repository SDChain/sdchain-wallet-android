package com.io.sdchain.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.io.sdchain.R;
import com.io.sdchain.base.BaseAdapter;
import com.io.sdchain.base.BaseViewHolder;
import com.io.sdchain.bean.AccountBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.utils.SaveObjectTools;

import java.util.List;

/**
 * @author xiey
 * @date created at 2018/2/9 13:41
 * @package com.io.sdchain.adapter
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class FragAssetsAccountAdapter extends BaseAdapter<AccountBean> {

    private Context context;

    public FragAssetsAccountAdapter(Context context, List<AccountBean> accountBeen) {
        super(R.layout.item_frag_assets, accountBeen);
        this.context = context;
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position) {
        if (datas != null && datas.size() > 0) {
            if (onClickListener != null) {
                holder.itemView.setOnClickListener(v -> {
                    int pos = holder.getLayoutPosition();
                    onClickListener.onClick(v, pos);
                });

                holder.itemView.setOnLongClickListener(v -> {
                    int pos = holder.getLayoutPosition();
                    onClickListener.onLongClick(v, pos);
                    return false;
                });
            }

            //default icon
            if (datas.get(position).getIsdefault().equals("1")) {
                holder.loadImage(R.id.walletImage, R.mipmap.ic_wallet_address_default);
            } else {
                holder.loadImage(R.id.walletImage, R.mipmap.ic_wallet_address);
            }

            SaveObjectTools tools = new SaveObjectTools(context);
            UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            if (datas.get(position).getAccount().equals(userBean.getAccount())) {
                ((TextView) holder.getView(R.id.fragAssetsName)).setTextColor(context.getResources().getColor(R.color.theme));
            } else {
                ((TextView) holder.getView(R.id.fragAssetsName)).setTextColor(context.getResources().getColor(R.color.black));
            }


            if (datas.get(position).getName() != null && !datas.get(position).getName().equals("")) {
                holder.setText(R.id.fragAssetsName, datas.get(position).getName());
            } else {
                holder.setText(R.id.fragAssetsName, context.getString(R.string.info26));
            }
        }
    }

    public interface OnClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
