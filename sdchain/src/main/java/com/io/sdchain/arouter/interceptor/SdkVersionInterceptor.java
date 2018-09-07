package com.io.sdchain.arouter.interceptor;


import android.content.Context;
import android.os.Build;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.io.sdchain.arouter.ARouterPath;


/**
 * @author : xiey
 * @project name : SDChain.
 * @package name  : com.io.sdchain.arouter.interceptor.
 * @date : 2018/7/19.
 * @signature : do my best.
 * @explain :
 */
@Interceptor(priority = ARouterPath.PRIORITY_4, name = ARouterPath.SdkVersionInterceptor)
public class SdkVersionInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (Build.VERSION.SDK_INT >= 21) {

        }
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {
    }
}
