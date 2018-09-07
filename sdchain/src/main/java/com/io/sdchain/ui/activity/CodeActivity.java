package com.io.sdchain.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bertsir.zbar.QRUtils;

@Route(path = ARouterPath.CodeActivity)
public final class CodeActivity extends BaseActivity {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.code)
    public ImageView code;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            initSystemBar();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        title.setText(getString(R.string.info201));
        if (Build.VERSION.SDK_INT >= 21) {
            code.setTransitionName("code");
        }
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        String userId = userBean.getId();
        Bitmap codeBitmap = QRUtils.getInstance().createQRCodeAddLogo(Constants.USERID + userId, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_user_logo));
        code.setImageBitmap(codeBitmap);
    }

    @OnClick({R.id.back})
    public void onClickView(View view){
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onSuccess(Object data, String msg) {

    }

    @Override
    public void onFailed(String msg) {

    }
}
