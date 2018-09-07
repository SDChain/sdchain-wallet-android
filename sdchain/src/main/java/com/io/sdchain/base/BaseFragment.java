package com.io.sdchain.base;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.io.sdchain.common.SharedPref;
import com.io.sdchain.utils.SaveObjectTools;
import com.io.sdchain.widget.SnackBarHelper;

/**
 * Created by xiey on 2017/8/17.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment {

    /**
     * save local data
     */
    protected SharedPref sharedPref;
    protected SaveObjectTools tools;

    protected abstract P createPresenter();

    private P p;

    protected P getPresenter() {
        if (p == null) {
            p = createPresenter();
        }
        return p;
    }

    @Override
    public void onAttach(Activity activity) {
        sharedPref = new SharedPref(getActivity());
        tools = new SaveObjectTools(getActivity());
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //set status bar color
//        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.theme));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public boolean isEmpty(EditText et) {
        return TextUtils.isEmpty(et.getText());
    }

    private SnackBarHelper snackBarHelper;

    public void showToast(String info) {
        if (!TextUtils.isEmpty(info)) {
            if (snackBarHelper == null) {
                snackBarHelper = new SnackBarHelper();
            }
            snackBarHelper.show(getActivity(), getActivity().getWindow().getDecorView(), info);
        }
    }

    public String getEditString(EditText editText) {
        return editText.getText().toString().trim();
    }

    public boolean haveString(EditText editText) {
        String s = editText.getText().toString().trim();
        return !TextUtils.isEmpty(s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).getRequestQueue().cancelAll(getActivity().getLocalClassName());
    }
}
