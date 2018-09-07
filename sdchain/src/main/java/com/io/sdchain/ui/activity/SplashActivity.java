package com.io.sdchain.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.io.sdchain.arouter.ARouterPath;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
@Route(path = ARouterPath.SplashActivity)
public final class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.create(e -> e.onNext(1))
                .delay(300, TimeUnit.MILLISECONDS)
                .subscribe(s -> {
                    ARouter.getInstance()
                            .build(ARouterPath.LoginActivity)
                            .withTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            .navigation(this, new NavCallback() {
                                @Override
                                public void onArrival(Postcard postcard) {
                                    finish();
                                }
                            });
                });
    }

}
