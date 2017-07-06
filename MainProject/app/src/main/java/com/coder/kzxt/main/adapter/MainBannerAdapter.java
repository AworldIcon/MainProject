package com.coder.kzxt.main.adapter;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.views.AutoScrollViewPager;
import com.coder.kzxt.views.CubeTransformer;
import com.coder.kzxt.views.DepthPageTransformer;
import com.coder.kzxt.views.ImagePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 首页banner
 */
public class MainBannerAdapter {

    private Activity mContext;
    //private PublicUtils pu;
    private List<MainModelBean.ItemsBean.ListBean> data;
    private MainModelBean.ItemsBean model_style;
    //所有的类型；
    private static final String
            HORIZONTAL_SCROLL_STYLE = "HORIZONTAL_SCROLL_STYLE",
            CUBE_STYLE = "CUBE_STYLE",
            FADE_IN_OUT_STYLE = "FADE_IN_OUT_STYLE";

    public static String types = HORIZONTAL_SCROLL_STYLE + "," + CUBE_STYLE + "," + FADE_IN_OUT_STYLE;
    private ArrayList<HashMap<String, String>> dataHashMap;


    public MainBannerAdapter(Activity mContext, MainModelBean.ItemsBean moduleType, List<MainModelBean.ItemsBean.ListBean> data) {
        super();
        this.mContext = mContext;
        this.data = data;
        //this.pu = new PublicUtils(this.mContext);
        this.model_style = moduleType;
        //把数据对象改成hashmap
        dataHashMap = new ArrayList<>();
        for (MainModelBean.ItemsBean.ListBean dataBean : this.data) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("title", dataBean.getTitle());
            hashMap.put("img", dataBean.getBannerImg());
            hashMap.put("url", dataBean.getLinks());
            dataHashMap.add(hashMap);
        }
    }
//    public void update(MainModelBean.ItemsBean moduleType, List<MainModelBean.ItemsBean.ListBean> data){
//        this.data = data;
//        //this.pu = new PublicUtils(this.mContext);
//        this.model_style = moduleType;
//        //把数据对象改成hashmap
//        dataHashMap.clear();
//        for (MainModelBean.ItemsBean.ListBean dataBean : this.data) {
//            HashMap<String, String> hashMap = new HashMap<String, String>();
//            hashMap.put("title", dataBean.getTitle());
//            hashMap.put("img", dataBean.getBannerImg());
//            hashMap.put("url", dataBean.getLinks());
//            dataHashMap.add(hashMap);
//        }
//    }
    private RelativeLayout bannerRy;
    private AutoScrollViewPager viewpager;
    private LinearLayout viewGroup;
    private ImageView[] imageViewpoints;

    public boolean isAdded=false;
    public View getView() {
        View view = null;
        if (this.data == null || this.data.size() == 0) return view;
        isAdded=true;
        view = View.inflate(mContext, R.layout.single_autoviewpager, null);
        bannerRy = (RelativeLayout) view.findViewById(R.id.bannerRy);
        viewpager = (AutoScrollViewPager) view.findViewById(R.id.viewpager);
        viewGroup = (LinearLayout) view.findViewById(R.id.viewGroup);
        isAdded=true;
        judgeBannerLayoutTotal();
        return view;
    }

    private void judgeBannerLayoutTotal() {
        if (viewGroup.getChildCount() != 0) {
            viewGroup.removeAllViews();
        }
        // 创建指示器
        imageViewpoints = new ImageView[data.size()];
        for (int i = 0; i < imageViewpoints.length; i++) {
            imageViewpoints[i] = new ImageView(mContext);
            LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
           /**
            * @！！！  此处有改动
            * */
            params.setMargins(0, 0, (int) mContext.getResources().getDimension(R.dimen.woying_6_dip), 0);
            params.weight = 1.0f;
            imageViewpoints[i].setLayoutParams(params);
            if (i == 0) {
                imageViewpoints[i].setBackgroundResource(R.drawable.bannerwhite);
            } else {
                imageViewpoints[i].setBackgroundResource(R.drawable.bannerblack);
            }
            viewGroup.addView(imageViewpoints[i]);
        }

        if (data.size() == 1) {
            viewpager.setAdapter(new ImagePagerAdapter(mContext, dataHashMap).setInfiniteLoop(false));
        } else {
            viewpager.setAdapter(new ImagePagerAdapter(mContext, dataHashMap).setInfiniteLoop(true));
            viewpager.setInterval(5000);// 设置自动滚动间隔
            viewpager.startAutoScroll();// 设置开始制动滚动
            viewpager.setCurrentItem(data.size() * 10000);
            viewpager.setAutoScrollDurationFactor(5);// 一次滚动的持续时间
            if (model_style.getModule_style().equals(CUBE_STYLE)) {
                viewpager.setPageTransformer(true, new CubeTransformer()); // 设置立方体样式动画
            } else if (model_style.getModule_style().equals(FADE_IN_OUT_STYLE)) {
                viewpager.setPageTransformer(true, new DepthPageTransformer()); //设置深入浅出样式动画
            }

        }
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 算出imageViews里的角标
                position = position % dataHashMap.size();
                 for (int i = 0; i < imageViewpoints.length; i++) {
                    imageViewpoints[position].setBackgroundResource(R.drawable.bannerwhite);
                    if (position != i) {
                        imageViewpoints[i].setBackgroundResource(R.drawable.bannerblack);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

//    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//
//        @Override
//        public void onPageSelected(int position) {
//            // 算出imageViews里的角标
//            position = position % dataHashMap.size();
//            Log.d("zw--view",position+"");
//            for (int i = 0; i < imageViewpoints.length; i++) {
//                imageViewpoints[position].setBackgroundResource(R.drawable.bannerblack);
//                if (position != i) {
//                    imageViewpoints[i].setBackgroundResource(R.drawable.bannerwhite);
//                }
//            }
//        }
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//
//        }
//    }

}
