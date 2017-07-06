package com.coder.kzxt.classe.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ImageView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Utils;

/**
 * Created by wangtingshun on 2017/6/2.
 * 分享班级二维码
 */

public class ShareClassQrcode extends BaseActivity {

    private Toolbar mToolbar;
    private ImageView iv_qrcode; //二维码
    private String qrcodeContent;
    private String className; //班级名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_class_qrcode);
        className = getIntent().getStringExtra("className");
        qrcodeContent = getIntent().getStringExtra("qrcode");
        initView();
        createQrcode();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(className);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);

    }

    /**
     * 创建二维码图片
     */
    private void createQrcode() {
        Bitmap qrImage = Utils.createQRImage(qrcodeContent);
        if (qrImage != null) {
            iv_qrcode.setImageBitmap(qrImage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
             finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
