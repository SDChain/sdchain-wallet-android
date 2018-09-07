package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.eventbus.EventBusType;
import com.io.sdchain.mvp.presenter.FriendPresenter;
import com.io.sdchain.mvp.view.FriendView;
import com.io.sdchain.ui.adapter.FriendAdapter;
import com.io.sdchain.utils.AesUtil2;
import com.io.sdchain.utils.PinyinUtils;
import com.io.sdchain.utils.TextChange;
import com.io.sdchain.widget.IndexBar;
import com.io.sdchain.widget.SectionDecoration2;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * There is also a search: listen for input changes, then request the interface
 */
@Route(path = ARouterPath.FriendActivity)
public final class FriendActivity extends BaseActivity<FriendPresenter> implements FriendView {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.recyclerFriend)
    public RecyclerView recyclerFriend;
    @BindView(R.id.tv)
    public TextView tv;
    @BindView(R.id.delete)
    public RelativeLayout delete;
    @BindView(R.id.friendAccount)
    public EditText friendAccount;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.noData)
    public RelativeLayout noData;
    @BindView(R.id.errorData)
    public RelativeLayout errorData;

    @BindView(R.id.words)
    public IndexBar word;
    private List<FriendBean> list = new ArrayList<>();
    private TextChange textChange;
    private FriendAdapter adapter;
    private LinearLayoutManager manager;
    private String fromFlag = "";
    private UserBean userBean;

    @Override
    protected FriendPresenter createPresenter() {
        return new FriendPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        fromFlag = getIntent().getStringExtra(ARouterPath.FROM);
        if (!TextUtils.isEmpty(fromFlag)) {
            if (fromFlag.equals(ARouterPath.PayFragment)) {
                title.setText(getString(R.string.info234));
            } else if ((fromFlag.equals(ARouterPath.MineFragment))) {
                title.setText(getString(R.string.info235));
            }
        }
        adapter = new FriendAdapter(list);
        recyclerFriend.setAdapter(adapter);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerFriend.setLayoutManager(manager);
        recyclerFriend.addItemDecoration(new SectionDecoration2(this, new SectionDecoration2.DecorationCallBack() {
            @Override
            public long getGroupId(int position) {
                return Character.toUpperCase(PinyinUtils.getPinyin(list.get(position).getName()).charAt(0));
            }

            @Override
            public String getGroupFirstLine(int position) {
                return PinyinUtils.getPinyin(list.get(position).getName()).substring(0, 1).toUpperCase();
            }
        }));
        word.setListener((word) -> updateWord(word));
        friendAccount.setCursorVisible(false);
        textChange = new TextChange();
    }

    private void initListener() {
        friendAccount.setOnKeyListener(keyListener);
        friendAccount.addTextChangedListener(textChange);
        textChange.setOnTextChange(onTextChange);
        friendAccount.setOnTouchListener(touchListener);
        toolbar.setNavigationOnClickListener((v) -> finish());
        recyclerFriend.addOnScrollListener(onScrollListener);
        adapter.setOnClickListener(fOnClickListener);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                if (firstItemPosition >= 0) {
                    word.setTouchIndex(list.get(firstItemPosition).getHeaderWord());
                }
            }
        }
    };

    private View.OnTouchListener touchListener = (v, event) -> {
        friendAccount.setCursorVisible(true);
        return false;
    };

    @OnClick({R.id.right, R.id.delete})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.right:
                ARouter.getInstance()
                        .build(ARouterPath.AddFriendActivity)
                        .navigation();
                break;
            case R.id.delete:
                friendAccount.setText("");
                break;
            default:
                break;
        }
    }

    private FriendAdapter.OnClickListener fOnClickListener = (v, position) -> {
        //When it comes from the pay interface
        if (fromFlag != null && !fromFlag.equals("")) {
            //If it comes from the money transfer interface
            if (fromFlag.equals(ARouterPath.PayFragment)) {
                EventBus.getDefault().post(
                        new EventBusType(EventBusType.BusType.FRIENDDETAL, list.get(position).getAccount()));
                finish();
            } else if ((fromFlag.equals(ARouterPath.MineFragment))) {
                //mine page
                Logger.e("get friend ID：" + list.get(position).getUserId());
                ARouter.getInstance()
                        .build(ARouterPath.FriendDetailActivity)
                        .withString(Constants.USERID, list.get(position).getUserId())
                        .navigation();
            }
        }
    };

    private View.OnKeyListener keyListener = (v, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(FriendActivity.this.getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            //A method to perform a search operation in which a non-null judgment of mEditSearchUser can be added
//                search();
        }
        return false;
    };

    private Disposable mDisposable;

    /**
     * change center char
     */
    private void updateWord(String words) {
        tv.setText(words);
        tv.setVisibility(View.VISIBLE);
        Logger.e("mDisposable");
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mDisposable = Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> tv.setVisibility(View.GONE));

        updateListView(words);
    }

    private void updateListView(String words) {
        for (int i = 0; i < list.size(); i++) {
            String headerWord = list.get(i).getHeaderWord();
            //Find the letter that your finger presses that starts with the same letter in the list
            if (words.equals(headerWord)) {
                //Select which one of the lists
                manager.scrollToPositionWithOffset(i, 0);
                //Find the first one
                return;
            }
        }
    }

    /**
     * Initialize contact list information
     */
    private void initData() {
        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().searchFriendList(
                userBean.getId(),
                "",
                AesUtil2.encryptGET(userBean.getId()),
                this,
                true);
    }

    private TextChange.OnTextChange onTextChange = (editable) -> {
        if (haveString(friendAccount)) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
        }

        String userName = "";
        try {
            userName = URLEncoder.encode("" + getEditString(friendAccount), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        getPresenter().searchFriendList(
                userBean.getId(),
                userName,
                AesUtil2.encryptGET(userBean.getId()),
                this,
                false);

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    /**
     * The buddy details interface transfers to the payment interface
     *
     * @param type
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFriednDetailToPay(EventBusType type) {
        if (type.getBusType() == EventBusType.BusType.FRIENDDETAILTOPAY) {
            finish();
        }

        if (type.getBusType() == EventBusType.BusType.ADDORDELETEFRIEND) {
            //Add and delete friends and then refresh the list
            userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
            getPresenter().searchFriendList(
                    userBean.getId(),
                    "",
                    AesUtil2.encryptGET(userBean.getId()),
                    this,
                    false);
        }
    }

    @Override
    public void onSuccess(Object data, String msg) {
        List<FriendBean> friendBeanList = (List<FriendBean>) data;
        for (FriendBean friendBean : friendBeanList) {
            friendBean.setName();
        }

        if (friendBeanList == null || friendBeanList.size() == 0) {
            noData();
            return;
        }
        haveData();
        list.clear();
        list.addAll(friendBeanList);
        //Sort the set
        Collections.sort(list, (lhs, rhs) -> {
            //Sort by pinyin
            return lhs.getPinyin().compareTo(rhs.getPinyin());
        });

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
        adapter.notifyDataSetChanged();
        dataError();
    }

    private void haveData() {
        recyclerFriend.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        errorData.setVisibility(View.GONE);
    }

    private void noData() {
        recyclerFriend.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
        errorData.setVisibility(View.GONE);
    }

    private void dataError() {
        recyclerFriend.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        errorData.setVisibility(View.VISIBLE);
    }

}
