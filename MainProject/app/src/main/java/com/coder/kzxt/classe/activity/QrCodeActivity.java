package com.coder.kzxt.classe.activity;

import android.os.Handler;

import com.coder.kzxt.base.activity.BaseActivity;
import com.google.zxing.Result;
import com.google.zxing.decode.CaptureActivityHandler;

/**
 * 二维码扫描类。
 */
public class QrCodeActivity extends BaseActivity
{

    private CaptureActivityHandler mCaptureActivityHandler;

    @Override
    protected void onResume() {
        super.onResume();
        if (mCaptureActivityHandler == null) {
            mCaptureActivityHandler = new CaptureActivityHandler(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCaptureActivityHandler != null) {
            mCaptureActivityHandler.quitSynchronously();
            mCaptureActivityHandler = null;
        }
    }

    /**
     * Handler scan result
     * @param result
     */
    public void handleDecode(Result result) {

    }

    public void restartPreview() {
        if (null != mCaptureActivityHandler) {
            mCaptureActivityHandler.restartPreviewAndDecode();
        }
    }

    public Handler getCaptureActivityHandler() {
        if (null != mCaptureActivityHandler) {
            return mCaptureActivityHandler;
        }
        return new Handler();
    }

}