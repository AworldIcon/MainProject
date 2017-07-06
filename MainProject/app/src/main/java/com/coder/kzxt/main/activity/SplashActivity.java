package com.coder.kzxt.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.baidu.mobstat.StatService;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.main.fragment.FirstLauncherFragment;
import com.coder.kzxt.main.fragment.FourthLauncherFragment;
import com.coder.kzxt.main.fragment.SecondLauncherFragment;
import com.coder.kzxt.main.fragment.ThirdLauncherFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 * Created by Administrator on 2016/6/4.
 */
public class SplashActivity extends FragmentActivity {
    private ViewPager vPager;
    private List<Fragment> list = new ArrayList();
    private BaseFragmentAdapter adapter;

    private ImageView[] tips;
    FirstLauncherFragment firstLauncehrFragment;
    SecondLauncherFragment secondLauncherFragment;
    ThirdLauncherFragment thirdLauncherFragment;
    FourthLauncherFragment fourthLauncherFragment;
    private int pageState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //初始化选中点
        ViewGroup group = (ViewGroup) findViewById(R.id.viewgroup);
        tips=new ImageView[4];
        for (int i=0;i<tips.length;i++){
            ImageView imageView=new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(10, 10));
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.banner_selected);
            } else {
                imageView.setBackgroundResource(R.drawable.banner_normal);
            }
            tips[i]=imageView;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 15;//设置点点点view的左边距
            layoutParams.rightMargin = 15;//设置点点点view的右边距
            layoutParams.bottomMargin=20;
            group.addView(imageView,layoutParams);
        }
        //获取自定义viewpager 然后设置背景图片
        vPager = (ViewPager) findViewById(R.id.viewpager_launcher);

        /**
         * 初始化四个fragment  并且添加到list中
         */
         firstLauncehrFragment = new FirstLauncherFragment();
         secondLauncherFragment = new SecondLauncherFragment();
         thirdLauncherFragment = new ThirdLauncherFragment();
         fourthLauncherFragment = new FourthLauncherFragment();
        list.add(firstLauncehrFragment);
        list.add(secondLauncherFragment);
        list.add(thirdLauncherFragment);
        list.add(fourthLauncherFragment);

        adapter = new BaseFragmentAdapter(getSupportFragmentManager(),list);
        vPager.setAdapter(adapter);
        vPager.setOffscreenPageLimit(list.size());
        vPager.setCurrentItem(0);
        vPager.setOnPageChangeListener(changeListener);
    }
    /**
     * 监听viewpager的移动
     */
    OnPageChangeListener changeListener=new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(position == 3){
                if(positionOffsetPixels == 0 && pageState == 1){
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }


        @Override
        public void onPageScrollStateChanged(int state) {
            pageState = state;
        }

        @Override
        public void onPageSelected(int index) {
            setImageBackground(index);//改变点点点的切换效果
            if(index==0){
                firstLauncehrFragment.startAnimation();
                secondLauncherFragment.stopAnimation();
                thirdLauncherFragment.stopAnimation();
                fourthLauncherFragment.stopAnimation();
            }else if(index==1){
                secondLauncherFragment.startAnimation();
                firstLauncehrFragment.stopAnimation();
                thirdLauncherFragment.stopAnimation();
                fourthLauncherFragment.stopAnimation();

            }else if(index==2){
                thirdLauncherFragment.startAnimation();
                firstLauncehrFragment.stopAnimation();
                secondLauncherFragment.stopAnimation();
                fourthLauncherFragment.stopAnimation();
            }else if(index==3){
                fourthLauncherFragment.startAnimation();
                firstLauncehrFragment.stopAnimation();
                secondLauncherFragment.stopAnimation();
                thirdLauncherFragment.stopAnimation();
            }
        }
        /**
         * 改变点点点的切换效果
         * @param selectItems
         */
        private void setImageBackground(int selectItems) {
            for (int i = 0; i < tips.length; i++) {
                if (i == selectItems) {
                    tips[i].setBackgroundResource(R.drawable.banner_selected);
                } else {
                    tips[i].setBackgroundResource(R.drawable.banner_normal);
                }
            }
        }
    };
    /**
     * Viewpager适配器
     * @author apple
     *
     */
    public class BaseFragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> list;
        public BaseFragmentAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        public BaseFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    public void onPause() {
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onPause(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onResume(this);
        super.onResume();
    }
}
