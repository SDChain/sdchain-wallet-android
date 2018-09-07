package com.io.sdchain.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.io.sdchain.R;
import com.io.sdchain.common.SharedPref;
import com.io.sdchain.utils.SaveObjectTools;
import com.io.sdchain.widget.SnackBarHelper;
import com.orhanobut.logger.Logger;
import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * Created by xiey on 2017/8/17.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    protected String TAG = this.getClass().getSimpleName();

    /**
     * local data save
     */
    protected SharedPref sharedPref;
    protected SaveObjectTools tools;

    protected abstract P createPresenter();

    private P p;

    protected P getPresenter() {
        Logger.e("-----use presenter-----" + p);
        if (p == null) {
            p = createPresenter();
            Logger.e("-----create presenter-----" + p);
        }
        return p;
    }

//    public abstract void closeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((BaseApplication) getApplication()).addStackActivity(this);
        sharedPref = new SharedPref(this);
        tools = new SaveObjectTools(this);
        super.onCreate(savedInstanceState);
        //set status bar coor
//        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.theme));
        ((BaseApplication) getApplication()).setContext(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((BaseApplication) getApplication()).setContext(this);
    }

    /**
     * check input is null or not
     */
    public boolean isEmpty(EditText et) {
        return TextUtils.isEmpty(et.getText());
    }

    private SnackBarHelper snackBarHelper;

    public void showToast(String info) {
        if (!TextUtils.isEmpty(info)) {
            if (snackBarHelper == null) {
                snackBarHelper = new SnackBarHelper();
            }
            snackBarHelper.show(this, getWindow().getDecorView(), info);
        }
    }

    public String getEditString(EditText editText) {
        return editText.getText().toString().trim();
    }

    public boolean haveString(EditText editText) {
        String s = editText.getText().toString().trim();
        return !TextUtils.isEmpty(s);
    }

    /**
     * click other ,hide soft input
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getApplication()).getRequestQueue().cancelAll(getLocalClassName());
        if (p != null) {
            p.closeDisposable();
        }
    }

    /**
     * check android version , change system bar color
     */
    public void initSystemBar() {
        Window window = getWindow();
        //4.4 up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //5.0 up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.theme));
//            window.setStatusBarColor(Color.TRANSPARENT);

        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.theme);
        //open SystemBarTint
        tintManager.setStatusBarTintEnabled(true);
    }


}
