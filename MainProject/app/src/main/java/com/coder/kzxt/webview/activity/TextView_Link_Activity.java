package com.coder.kzxt.webview.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.SharedPreferencesUtil;

public class TextView_Link_Activity extends BaseActivity{
    private WebView webView;
    private Handler handler;
    private ProgressBar pb_web_url;
    private TextView title_tx;
    private String web_url;
    private String titleString;
    private SharedPreferencesUtil pu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view__link_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        pu = new SharedPreferencesUtil(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(4);
                finish();
            }
        });

        web_url = getIntent().getStringExtra("web_url");
//		web_url = "http://wyzctest.up4g.com/1/";
        titleString = getIntent().getStringExtra("title");
        title_tx = (TextView) findViewById(R.id.toolbar_title);
        title_tx.setText(titleString);
        pb_web_url = (ProgressBar) findViewById(R.id.pb_web_url);
        pb_web_url.setMax(100);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                pb_web_url.setProgress(msg.arg1);
                if (msg.arg1 == 100) {
                    pb_web_url.setVisibility(View.GONE);
                } else {
                    pb_web_url.setVisibility(View.VISIBLE);
                }
            }
        };

        webView = (WebView) findViewById(R.id.mywebView);
        initWebViewSettings();

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        L.v("tangcy","加载的url"+web_url);
        if (!TextUtils.isEmpty(web_url)) {
         if(!web_url.startsWith("http")){
             webView.loadDataWithBaseURL(null,web_url, "text/html", "utf-8", null);
         }else {
             webView.loadUrl(web_url);
         }
        }

        webView.requestFocus();

        webView.requestFocusFromTouch();

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                webView.requestFocus();
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Message msg = new Message();
                msg.arg1 = newProgress;
                handler.sendMessage(msg);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (message != null) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    result.cancel(); // 一定要cancel，否则会出现各种奇怪问题
                }
                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title) && TextUtils.isEmpty(titleString)) {
                    title_tx.setText(title);
                }

            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                // sys("urlurlurlurlurl:"+url);
                super.onPageFinished(webView, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                if (url.contains(".apk")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }else if(url.contains("getMyWalletAction")){
//                    if(TextUtils.isEmpty(pu.getIsLogin())){
//                        webView.pauseTimers();
//                        webView.destroy();
//                        setResult(4);
//                        finish();
////                        Intent intent = new Intent(TextView_Link_Activity.this,LoginActivity.class);
////                        intent.putExtra("from","textview_wallet");
////                        startActivity(intent);
//                    }else{
//                        setResult(4);
//                        webView.pauseTimers();
//                        webView.destroy();
//                        finish();
//                        //startActivity(new Intent(TextView_Link_Activity.this,My_Wallet_Activity.class));
//                    }
                        //暂时用此模拟
                    setResult(4);
                        webView.pauseTimers();
                        webView.destroy();
                        finish();
                } else {
                    view.loadUrl(url);

                }

                return true;
            }

        });

    }

    /**
     * webView的一些属性设定
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {

        // 取消垂直 和水平进度条显示
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        // 支持JavaScrip
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// 允许js弹出窗口

        webView.getSettings().setDomStorageEnabled(true);
        // 支持网页中的动画
        // webView.getSettings().setPluginsEnabled(true);
        webView.requestFocus();

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        // 禁用页面缩放
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(4);
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewCache();
    }

    public void clearWebViewCache() {
// 清除cookie即可彻底清除缓存
        CookieSyncManager.createInstance(TextView_Link_Activity.this);
        CookieManager.getInstance().removeAllCookie();
    }
}
