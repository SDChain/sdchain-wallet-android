package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.CertificationPresenter;
import com.io.sdchain.mvp.view.CertificationView;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.IDValidator;
import com.io.sdchain.utils.TextChange;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.CertificationActivity)
public final class CertificationActivity extends BaseActivity<CertificationPresenter> implements CertificationView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.realName)
    public EditText realName;
    @BindView(R.id.idCard)
    public EditText idCard;
    @BindView(R.id.certificationBtn)
    public Button certificationBtn;
    @BindView(R.id.deleteRealName)
    public RelativeLayout deleteRealName;
    @BindView(R.id.deleteIdCard)
    public RelativeLayout deleteIdCard;
    private TextChange textChange;

    @Override
    protected CertificationPresenter createPresenter() {
        return new CertificationPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);
        ButterKnife.bind(this);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info199));
        textChange = new TextChange();
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener((v) -> finish());
        realName.addTextChangedListener(textChange);
        idCard.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
    }

    private void initData() {
        certificationBtn.setEnabled(false);
        certificationBtn.setBackgroundResource(R.drawable.button_unenable);

        idCard.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
    }

    @OnClick({R.id.certificationBtn, R.id.deleteRealName, R.id.deleteIdCard})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.certificationBtn:
                if (IDValidator.validator(getEditString(idCard))) {
                    UserBean userInfo = (UserBean) tools.getObjectData(Constants.USERINFO);
                    getPresenter().realNameCertification(
                            userInfo.getId(),
                            getEditString(idCard),
                            getEditString(realName),
                            AesUtil2.encryptPOST(getEditString(idCard)),
                            CertificationActivity.this);
                } else {
                    showToast(getString(R.string.info200));
                }
                break;
            case R.id.deleteRealName:
                realName.setText("");
                break;
            case R.id.deleteIdCard:
                idCard.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(Object data, String msg) {
        UserBean userInfo = (UserBean) tools.getObjectData(Constants.USERINFO);
        userInfo.setRealName(getEditString(realName));
        userInfo.setIdCode(getEditString(idCard));
        tools.saveData(userInfo, Constants.USERINFO);
        EventBus.getDefault().post(new EventBusType(EventBusType.BusType.REALNAMECERTIFICATION, "实名认证成功"));
        finish();
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }

    //Input Monitoring
    private TextChange.OnTextChange onTextChange = (editable) -> {
        if (haveString(realName) && haveString(idCard)) {
            certificationBtn.setEnabled(true);
            certificationBtn.setBackgroundResource(R.drawable.selector_button);
        } else {
            certificationBtn.setEnabled(false);
            certificationBtn.setBackgroundResource(R.drawable.button_unenable);
        }

        if (haveString(realName)) {
            deleteRealName.setVisibility(View.VISIBLE);
        } else {
            deleteRealName.setVisibility(View.GONE);
        }

        if (haveString(idCard)) {
            deleteIdCard.setVisibility(View.VISIBLE);
        } else {
            deleteIdCard.setVisibility(View.GONE);
        }

    };
}
