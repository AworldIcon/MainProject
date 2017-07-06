/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.pizidea.imagepicker.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.R;
import com.pizidea.imagepicker.bean.ImageItem;

import java.util.List;

/**
 * <b>Image Preview Fragment with multi select mode</b><br/>
 * Created by Eason.Lai on 2015/11/1 10:42 <br/>
 * contact：easonline7@gmail.com <br/>
 */
public class ImagePreviewFragment extends Fragment{
    Activity mContext;
    ViewPager mViewPager;
    TouchImageAdapter mAdapter ;
    List<ImageItem> mImageList;
    private int mCurrentItemPosition = 0;
    private boolean enableSingleTap = true;//singleTap to do something
    AndroidImagePicker androidImagePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        //mSelectedImages = new SparseArray<>();
        androidImagePicker = AndroidImagePicker.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_preview,null);
        mImageList = androidImagePicker.getImageItemsOfCurrentImageSet();
        mCurrentItemPosition = getArguments().getInt(AndroidImagePicker.KEY_PIC_SELECTED_POSITION,0);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        mViewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
        mAdapter = new TouchImageAdapter(((FragmentActivity)mContext).getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentItemPosition, false);
        ImageItem item = mImageList.get(mCurrentItemPosition);
        if(mContext instanceof OnImagePageSelectedListener){
            boolean isSelected = false;
            if(androidImagePicker.isSelect(mCurrentItemPosition,item) ){
                isSelected = true;
            }
            ((OnImagePageSelectedListener)mContext).onImagePageSelected(mCurrentItemPosition, mImageList.get(mCurrentItemPosition), isSelected);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                mCurrentItemPosition = position;
                if(mContext instanceof OnImagePageSelectedListener){
                    boolean isSelected = false;
                    ImageItem item = mImageList.get(mCurrentItemPosition);
                    if(androidImagePicker.isSelect(position,item)  ){
                        isSelected = true;
                    }
                    ((OnImagePageSelectedListener)mContext).onImagePageSelected(mCurrentItemPosition,item,isSelected);
                }
            }
            @Override public void onPageScrollStateChanged(int state) { }

        });

    }

    /**
     * public method:select the current show image
     */
    public void selectCurrent(boolean isCheck){
        ImageItem item = mImageList.get(mCurrentItemPosition);
        boolean isSelect = androidImagePicker.isSelect(mCurrentItemPosition,item);
        if(isCheck){
            if(!isSelect){
                AndroidImagePicker.getInstance().addSelectedImageItem(mCurrentItemPosition,item);
            }
        }else{
            if(isSelect){
                AndroidImagePicker.getInstance().deleteSelectedImageItem(mCurrentItemPosition, item);
            }
        }

    }

    class TouchImageAdapter extends FragmentStatePagerAdapter {
        public TouchImageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public Fragment getItem(int position) {
            SinglePreviewFragment fragment = new SinglePreviewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(SinglePreviewFragment.KEY_URL, mImageList.get(position));
            bundle.putBoolean("enableSingleTap",enableSingleTap);
            fragment.setArguments(bundle);
            fragment.setActivity(mContext);
            return fragment;
        }

    }

    /**
     * Interface for SingleTap Watching
     */
    public interface OnImageSingleTapClickListener{
        void onImageSingleTap(MotionEvent e);
    }

    /**
     * Interface for swipe page watching,you can get the current item,item position and whether the item is selected
     */
    public interface OnImagePageSelectedListener {
        void onImagePageSelected(int position, ImageItem item, boolean isSelected);
    }


}
