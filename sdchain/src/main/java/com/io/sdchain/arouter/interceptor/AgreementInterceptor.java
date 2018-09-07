package com.io.sdchain.arouter.interceptor;


import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.common.API;
import com.io.sdchain.common.Constants;


/**
 * @author : xiey
 * @project name : SDChain.
 * @package name  : com.io.sdchain.arouter.interceptor.
 * @date : 2018/7/19.
 * @signature : do my best.
 * @explain :
 */
@Interceptor(priority = 5, name = ARouterPath.AgreementInterceptor)
public class AgreementInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (postcard.getPath().equals(ARouterPath.WebViewActivity)) {
            Bundle bundle = postcard.getExtras();
            String country = bundle.getString(ARouterPath.DATA);
            String from = bundle.getString(ARouterPath.FROM);
            if (ARouterPath.RegisterPhone2Activity.equals(from) || ARouterPath.RegisterEmail2Activity.equals(from)) {
                if (country.equals("en")) {
                    postcard.withString(Constants.URL, API.SERVICEAGREEMENT_EN);
                } else if (country.equals("tc")) {
                    postcard.withString(Constants.URL, API.SERVICEAGREEMENT_TC);
                } else {
                    postcard.withString(Constants.URL, API.SERVICEAGREEMENT);
                }
            }
        }
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {
    }
}
