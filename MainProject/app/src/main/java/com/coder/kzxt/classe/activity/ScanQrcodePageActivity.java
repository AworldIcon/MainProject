package com.coder.kzxt.classe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.coder.kzxt.activity.R;

/**
 * 对返回的url链接做特殊处理
 * @author wang
 */
public class ScanQrcodePageActivity extends Activity {

    private WebView webView;
    private ProgressBar pb_web_url;
    private TextView title_tx;
    //返回键
    private ImageView leftImage;
    private String web_url;
    private String titleString;
    private Dialog asydialog;
    private TextView tv_result;
    private LinearLayout load_fail_layout;
    private Button load_fail_button;
    private static final int START_LOAD = 0x0001;
    private static final int STOP_LAOD = 0x0002;
    private static final int NET_ERROR = 0x0003;
    private static final int HORIZONTAL_LOAD = 0x0000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_LOAD:
//                    startLoad();
                    break;
                case STOP_LAOD:
//                    stopLoad();
                    break;
                case NET_ERROR:
//                    load_fail_layout.setVisibility(View.VISIBLE);
                    break;
                case HORIZONTAL_LOAD:
                    horizontalLoad(msg);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_page);
        init();
        initView();
        initListener();
    }

    private void initListener() {
        leftImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onDestroy();
            }
        });

        //加载失败
        load_fail_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWebView();
            }
        });
    }

    private void initView() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                sendProgressMessage(newProgress,HORIZONTAL_LOAD);
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

            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                sendProgressMessage(0,START_LOAD);
                load_fail_layout.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(final WebView webView, String url) {
                super.onPageFinished(webView, url);
//                webView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendProgressMessage(0,STOP_LAOD);
//                    }
//                },2000);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
//                webView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendProgressMessage(0,NET_ERROR);
//                    }
//                },2500);
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendProgressMessage(int newProgress,int flag) {
        Message msg = Message.obtain();
        msg.what = flag;
        msg.arg1 = newProgress;
        handler.sendMessage(msg);
    }

    private void init() {
        web_url = getIntent().getStringExtra("web_url");
        titleString = getIntent().getStringExtra("title");
        title_tx = (TextView) findViewById(R.id.title);
        webView = (WebView) findViewById(R.id.mywebView);
        leftImage = (ImageView) findViewById(R.id.leftImage);
        tv_result = (TextView) findViewById(R.id.tv_result);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        title_tx.setText(titleString);
        initWebViewSettings();
        webView.requestFocus();
        webView.requestFocusFromTouch();
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        pb_web_url = (ProgressBar) findViewById(R.id.pb_web_url);
        pb_web_url.setMax(100);
        loadWebView();
    }

    private void loadWebView() {
        if (!TextUtils.isEmpty(web_url)) {
            if (web_url.contains("http") || web_url.contains("https")
                    || web_url.contains("https://") || web_url.contains("ftp")) {
                webView.setVisibility(View.VISIBLE);
                tv_result.setVisibility(View.GONE);
                webView.loadUrl(web_url);
            } else {
                webView.setVisibility(View.GONE);
                tv_result.setVisibility(View.VISIBLE);
                tv_result.setText(web_url);
            }
        }
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
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        // 禁用页面缩放
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
    }


//    private void startLoad() {
//        asydialog = MyPublicDialog.createLoadingDialog(this);
//        asydialog.show();
//    }


//    private void stopLoad() {
//        if(asydialog != null && asydialog.isShowing()){
//            asydialog.cancel();
//        }
//    }

    private void horizontalLoad(Message msg) {
        pb_web_url.setProgress(msg.arg1);
        if (msg.arg1 == 100) {
            pb_web_url.setVisibility(View.GONE);
        } else {
            pb_web_url.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onPause(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (asydialog != null && asydialog.isShowing()) {
//            asydialog.cancel();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewCache();
    }

    public void clearWebViewCache() {
        // 清除cookie即可彻底清除缓存
        CookieSyncManager.createInstance(ScanQrcodePageActivity.this);
        CookieManager.getInstance().removeAllCookie();
    }
}
