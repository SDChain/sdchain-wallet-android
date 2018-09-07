package com.io.sdchain.ui.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.WindowCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.base.BasePresenter;
import com.io.sdchain.common.Constants;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterPath.WebViewActivity)
public final class WebViewActivity extends BaseActivity {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.webView)
    public WebView webView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.error)
    public RelativeLayout error;
    @BindView(R.id.normal)
    public LinearLayout normal;
    @BindView(R.id.progressBar1)
    public ProgressBar pg1;
    private String titleStr = "", urlStr = "";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_MODE_OVERLAY);
        setContentView(R.layout.activity_official_website);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        titleStr = getIntent().getStringExtra(Constants.TITLE);
        urlStr = getIntent().getStringExtra(Constants.URL);
        Logger.i(urlStr);
        title.setText(titleStr);
        webView.loadUrl(urlStr);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebChromeClient(new WebChromeClient() {
            //进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    pg1.setVisibility(View.GONE);//The page progress bar disappears after loading
                } else {
                    pg1.setVisibility(View.VISIBLE);//The progress bar is displayed when the page begins to load
                    pg1.setProgress(newProgress);//Set progress value
                }

            }

            @Override
            public void onReceivedTitle(WebView view, String titles) {
                super.onReceivedTitle(view, titles);
                title.setText(titles);
                // Android 6.0 below get through title
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    if (titles.contains("404") || titles.contains("500") || titles.contains("Error")) {
                        Logger.e("-----loading failed1-----");
                        isError = true;
                    }
                }
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            //begin
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            //finish
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Logger.e("-----loading finished-----" + isError);
                if (isError) {
                    error.setVisibility(View.VISIBLE);
                    normal.setVisibility(View.GONE);
                } else {
                    error.setVisibility(View.GONE);
                    normal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

//            @TargetApi(android.os.Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//                super.onReceivedHttpError(view, request, errorResponse);
//                // This method only appears in 6.0
//                int statusCode = errorResponse.getStatusCode();
//                Logger.e("onReceivedHttpError code = " + statusCode);
//                if (404 == statusCode || 500 == statusCode) {
//                    Logger.e("-----loading failed2-----");
//                }
//            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // Disconnect or network connection timed out
                if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                    Logger.e("-----loading failed3-----");
                    isError = true;
                }
            }
        });
    }

    private boolean isError = false;

    private void initListener() {
        toolbar.setNavigationOnClickListener(v -> {
            if (webView.canGoBack()) {
                isError = false;
                webView.goBack();//Return to the previous page
            } else {
                finish();
            }
        });
    }

    @OnClick({R.id.error})
    public void onCLickView(View view) {
        switch (view.getId()) {
            case R.id.error:
                webView.loadUrl(urlStr);
                break;
            default:
                break;
        }
    }

    ;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                isError = false;
                webView.goBack();//Return to the previous page
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSuccess(Object data, String msg) {

    }

    @Override
    public void onFailed(String msg) {

    }
}
