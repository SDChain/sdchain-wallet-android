package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.common.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = ARouterPath.BadCodeResultActivity)
public final class BadCodeResultActivity extends BaseActivity {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.badCodeResult)
    public TextView badCodeResult;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bad_code_result);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener((v) -> {
            onBackPressed();
        });
        title.setText(getString(R.string.info23));
        String result = getIntent().getStringExtra(Constants.BADCODE);
        badCodeResult.setText(result);
    }

    @Override
    public void onSuccess(Object data, String msg) {

    }

    @Override
    public void onFailed(String msg) {

    }
}
