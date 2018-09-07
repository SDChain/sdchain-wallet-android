package com.io.sdchain.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.io.sdchain.net.HTTPSTrustManager;
import com.io.sdchain.utils.LanguageUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.Stack;

import static com.io.sdchain.BuildConfig.DEBUG;

/**
 * Created by xiey on 2017/8/17.
 */

public class BaseApplication extends Application {

    private static Context mContext;

    //tag
    private final static String MyTAG = "ccer";

    public BaseActivity context;
    /**
     * input activity into stack
     */
    private Stack<Activity> activityStack = new Stack<>();
    /**
     * Volley Queue
     */
    private RequestQueue mRequestQueue;

    /**
     * application context
     */
    private static BaseApplication instance;

    /**
     * get application context
     */
    public static BaseApplication getApplicationInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //get application context
        instance = this;
        mContext = getApplicationContext();
        //logger
        printLogger();

        //ARouter init
        if (DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(instance);

        String c = LanguageUtils.getCountry(mContext);
        Logger.i("language environment：" + c);

    }

    /**
     * clear Activity
     */
    public void exitApplication() {
        clearStackActivity();
    }


    public void setContext(BaseActivity context) {
        this.context = context;
    }

    /**
     * into stack
     *
     * @param context
     */
    public void addStackActivity(Activity context) {
        activityStack.add(context);
    }

    /**
     * clear all activity in stack
     */
    public void clearStackActivity() {
        for (Activity context : activityStack) {
            if (context != null) {
                context.finish();
            }
        }
    }

    /**
     * double check
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            synchronized (this) {
                if (mRequestQueue == null) {
                    HTTPSTrustManager.allowAllSSL();//trust all SSL
                    mRequestQueue = Volley
                            .newRequestQueue(getApplicationContext());
                }
            }
        }
        return mRequestQueue;
    }


    /**
     * add to stack
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        Logger.e("-----context.getLocalClassName()-----" + context.getLocalClassName());
        req.setTag(context.getLocalClassName());
        getRequestQueue().add(req);
    }

    /**
     * print log
     */
    private void printLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .methodOffset(7)
//                .logStrategy("TAG")
                .tag(MyTAG)
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    /**
     * get application context
     */
    public static Context getContext() {
        return mContext;
    }


    static {
        //set application Header creater
        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) -> {
            // set application theme color
//            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
            return new ClassicsHeader(context);
        });
        //set application Footer creater
        SmartRefreshLayout.setDefaultRefreshFooterCreater((context, layout) ->
                new ClassicsFooter(context).setDrawableSize(20)
        );
    }


}
