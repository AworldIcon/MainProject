package com.coder.kzxt.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.coder.kzxt.activity.R;

/**
 * 引导页用的fragment抽象类
 * Created by Administrator on 2016/6/4.
 */
public class SecondLauncherFragment extends LauncherBaseFragment {
    private ImageView launcher2_mountain;
    private ImageView launcher2_mobile;
    private ImageView launcher2_people;
    private ImageView launcher2_text;
    private ImageView launcher_yun;
    private View view;

    private Animation animation;
    private Animation animation1;
    private Animation animation2;
    private Animation animation3;
    private boolean started=false;//是否开启动画(ViewPage滑动时候给这个变量赋值)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view=inflater.inflate(R.layout.fragment_splash_second,container, false);
            launcher2_mountain= (ImageView) view.findViewById(R.id.launcher2_mountain);
            launcher2_mobile= (ImageView) view.findViewById(R.id.launcher2_mobile);
            launcher2_people= (ImageView) view.findViewById(R.id.launcher2_people);
            launcher2_text= (ImageView) view.findViewById(R.id.launcher2_text);
            launcher_yun= (ImageView) view.findViewById(R.id.launcher_yun);
            return view;
    }

    public void  startAnimation(){

            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_toleft);
            launcher_yun.startAnimation(animation);
            launcher2_mountain.startAnimation(animation);
            launcher2_mountain.setVisibility(View.VISIBLE);
            launcher_yun.setVisibility(View.VISIBLE);
            animation.setFillEnabled(true);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animation1= AnimationUtils.loadAnimation(getActivity(), R.anim.splash_down);
                    launcher2_mobile.startAnimation(animation1);
                    launcher2_mobile.setVisibility(View.VISIBLE);
                    animation1.setFillEnabled(true);
                    animation1.setFillAfter(true);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            animation2= AnimationUtils.loadAnimation(getActivity(), R.anim.splash_down);
                            launcher2_people.startAnimation(animation2);
                            launcher2_people.setVisibility(View.VISIBLE);
                            animation2.setFillEnabled(true);
                            animation2.setFillAfter(true);
                        }
                    });

                }
            });
        animation3= AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
        launcher2_text.startAnimation(animation3);
        launcher2_text.setVisibility(View.VISIBLE);
        animation3.setFillEnabled(true);
        animation3.setFillAfter(true);
    }
    public void  stopAnimation(){

        launcher2_mountain.setVisibility(View.GONE);
        launcher2_mobile.setVisibility(View.GONE);
        launcher2_people.setVisibility(View.GONE);
        launcher2_text.setVisibility(View.GONE);
        launcher_yun.setVisibility(View.GONE);
        launcher2_mountain.setVisibility(View.GONE);

        launcher2_mountain.clearAnimation();
        launcher2_mobile.clearAnimation();
        launcher2_people.clearAnimation();
        launcher2_text.clearAnimation();
        launcher_yun.clearAnimation();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAnimation();
    }

    @Override
    protected void requestData() {

    }
}
