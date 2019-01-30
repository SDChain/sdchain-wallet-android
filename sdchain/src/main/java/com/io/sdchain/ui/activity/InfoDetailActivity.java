package com.io.sdchain.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.io.sdchain.R;
import com.io.sdchain.arouter.ARouterPath;
import com.io.sdchain.base.BaseActivity;
import com.io.sdchain.bean.InfoBean;
import com.io.sdchain.mvp.presenter.CreditGrantPresenter;
import com.io.sdchain.mvp.view.CreditGrantView;
import com.io.sdchain.utils.Common;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = ARouterPath.InfoDetailActivity)
public final class InfoDetailActivity extends BaseActivity<CreditGrantPresenter> implements CreditGrantView {

    @BindView(R.id.title)
    public TextView title;

    @BindView(R.id.txt_title)
    public TextView txt_title;

    @BindView(R.id.txt_time)
    public TextView txt_time;

    @BindView(R.id.txt_read)
    public TextView txt_read;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    private String account = "";

    private WebView webView;
    InfoBean data;

    @Override
    protected CreditGrantPresenter createPresenter() {
        return new CreditGrantPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSystemBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);
        ButterKnife.bind(this);
        data= (InfoBean) getIntent().getSerializableExtra("data");
        initView();
        initListener();
    }

    private void initView() {
        webView=findViewById(R.id.webView);
        webViewShow(webView);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(R.string.key000275);

        if(data!=null){
            txt_title.setText(data.getTitle());
            txt_time.setText(Common.getTimeDate(data.getTimeDiff(),this));
            txt_read.setText(data.getReadNum()+getString(R.string.key000276));
            getPresenter().getInfoDetail(data.getNewsId(),this,true);
        }

    }


    private void webViewShow(WebView webView){
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView_info.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


    }

    private void initListener() {
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    @Override
    public void onSuccess(Object data, String msg) {
        InfoBean bean= (InfoBean) data;
//        webView.loadDataWithBaseURL(null, bean.getContent(), "text/html", "utf-8", null);
        webView.loadDataWithBaseURL(null, getHtmlData(bean.getContent()), "text/html", "utf-8", null);
    }



    /**
     * @param bodyHTML
     * @return
     */
    private  String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width:100% !important; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body style:'height:auto;max-width: 100%; width:auto;'>" + bodyHTML + "</body></html>";
    }


    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void creditGrantSuccess(Object data, String msg) {

    }

    @Override
    public void creditGrantFailed(String msg) {

    }

    @Override
    public void cancelCreditGrantSuccess(Object data, String msg) {

    }

    @Override
    public void cancelCreditGrantFailed(String msg) {

    }

    @Override
    public void pwdWrong1(String msg) {

    }

    @Override
    public void pwdWrong2(String msg) {

    }

    @Override
    public void pwdWrong3(String msg) {

    }

    @Override
    public void pwdWrong4(String msg) {

    }
}
