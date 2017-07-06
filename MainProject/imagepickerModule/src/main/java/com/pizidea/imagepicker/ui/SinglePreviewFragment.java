package com.pizidea.imagepicker.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.pizidea.imagepicker.GlideImgLoader;
import com.pizidea.imagepicker.ImgLoader;
import com.pizidea.imagepicker.bean.ImageItem;
import com.pizidea.imagepicker.widget.TouchImageView;

/**
 * Created by Administrator on 2017/2/23.
 */

public class SinglePreviewFragment extends Fragment {

    public static final String KEY_URL = "key_url";
    private TouchImageView imageView;
    private String url;
    private ImgLoader mImagePresenter;//interface to load image,you can implements it with your own code
    private boolean enableSingleTap;
    private Activity mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        ImageItem imageItem = (ImageItem) bundle.getSerializable(KEY_URL);
        enableSingleTap = bundle.getBoolean("enableSingleTap");
        url = imageItem.path;
        mImagePresenter = new GlideImgLoader();
        imageView = new TouchImageView(mContext);
        imageView.setBackgroundColor(0xff000000);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);

        imageView.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (enableSingleTap) {
                    if(mContext instanceof ImagePreviewFragment.OnImageSingleTapClickListener){
                        ((ImagePreviewFragment.OnImageSingleTapClickListener)mContext).onImageSingleTap(e);
                    }
                }
                return false;
            }
            @Override public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
            @Override public boolean onDoubleTap(MotionEvent e) {
                return false;
            }

        });

        ((GlideImgLoader)mImagePresenter).onPresentImage(imageView, url, imageView.getWidth());//display the image with your own ImageLoader
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return imageView;
    }


    public void setActivity(Activity mContext){
        this.mContext = mContext;
    }

}
