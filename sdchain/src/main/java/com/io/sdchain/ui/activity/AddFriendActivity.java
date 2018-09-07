package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.FriendBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.AddFriendPresenter;
import com.io.sdchain.mvp.view.AddFriendView;
import com.io.sdchain.utils.PermissionUtils;
import com.io.sdchain.utils.QrUtils;
import com.io.sdchain.utils.TextChange;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.AddFriendActivity)
public final class AddFriendActivity extends BaseActivity<AddFriendPresenter> implements AddFriendView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.delete)
    public RelativeLayout delete;
    @BindView(R.id.friendAccount)
    public EditText friendAccount;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.search)
    public TextView search;
    private TextChange textChange;

    @Override
    protected AddFriendPresenter createPresenter() {
        return new AddFriendPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.info185));
        friendAccount.setCursorVisible(false);
        textChange = new TextChange();
    }

    private void initListener() {
        friendAccount.setOnKeyListener(keyListener);
        friendAccount.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
        friendAccount.setOnTouchListener(touchListener);
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private View.OnTouchListener touchListener = (v, event) -> {
        friendAccount.setCursorVisible(true);
        return false;
    };

    @OnClick({R.id.right, R.id.delete, R.id.search})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.right:
                new PermissionUtils().getInstance(AddFriendActivity.this)
                        .permissions(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)
                        .errHint(getString(R.string.info187))
                        .permission(permissions -> {
                            scan();
                        }).start();
                break;
            case R.id.delete:
                friendAccount.setText("");
                break;
            case R.id.search:
                //Search for friends
                String userName = "";
                try {
                    userName = URLEncoder.encode("" + getEditString(friendAccount), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (userName.equals("")) {
                    showToast(getString(R.string.info186));
                } else {
                    try {
                        getPresenter().addFriend(
                                userName,
                                this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void scan() {
        QrUtils.getInstance().startScan(AddFriendActivity.this, result -> {
            Logger.d("Scan succeeded：" + result);
            if (result.startsWith(Constants.USERID)) {
                //User ID to friend interface
                String userId = result.substring(Constants.USERID.length(), result.length());
                ARouter.getInstance()
                        .build(ARouterPath.FriendDetailActivity)
                        .withString(Constants.USERID, userId)
                        .navigation();

            } else {
                ARouter.getInstance()
                        .build(ARouterPath.BadCodeResultActivity)
                        .withString(Constants.BADCODE, result)
                        .navigation();
            }
        });
    }

    private View.OnKeyListener keyListener = (v, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(AddFriendActivity.this.getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            //A method to perform a search operation in which a non-null judgment of mEditSearchUser can be added
//                search();
        }
        return false;
    };

    private TextChange.OnTextChange onTextChange = (editable) -> {
        if (haveString(friendAccount)) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
        }
    };


    @Override
    public void onSuccess(Object data, String msg) {
        FriendBean friendBean = (FriendBean) data;
        if (friendBean == null) {
            showToast(msg);
            return;
        }
        ARouter.getInstance()
                .build(ARouterPath.FriendDetailActivity)
                .withString(Constants.USERID, friendBean.getId())
                .navigation();
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void change(EventBusType type) {
        if (type.getBusType() == EventBusType.BusType.FRIENDDETAILTOPAY) {
            finish();
        }
    }
}
