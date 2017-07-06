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
public class ThirdLauncherFragment extends LauncherBaseFragment {
    private ImageView launcher3_mountain;
    private ImageView launcher3_sign;
    private ImageView launcher3_sign_student;
    private ImageView launcher3_text;
    private ImageView launcher_yun;

    private Animation animation;
    private Animation animation1;
    private Animation animation2;
    private Animation animation3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_splash_third,container, false);
        launcher3_mountain=(ImageView)view.findViewById(R.id.launcher3_mountain);
        launcher_yun=(ImageView)view.findViewById(R.id.launcher_yun);
        launcher3_sign=(ImageView)view.findViewById(R.id.launcher3_sign);
        launcher3_sign_student=(ImageView)view.findViewById(R.id.launcher3_sign_student);
        launcher3_text=(ImageView)view.findViewById(R.id.launcher3_text);
        return view;
    }

    public void  startAnimation(){

            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_toleft);
            launcher_yun.startAnimation(animation);
            launcher3_mountain.startAnimation(animation);
            launcher3_mountain.setVisibility(View.VISIBLE);
            launcher_yun.setVisibility(View.VISIBLE);
            animation.setFillAfter(true);
            animation.setFillEnabled(true);
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
                    launcher3_sign.startAnimation(animation1);
                    launcher3_sign.setVisibility(View.VISIBLE);

                    animation1.setFillAfter(true);
                    animation1.setFillEnabled(true);
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
                            launcher3_sign_student.startAnimation(animation2);
                            launcher3_sign_student.setVisibility(View.VISIBLE);
                            animation2.setFillAfter(true);
                            animation2.setFillEnabled(true);
                        }
                    });

                }
            });
        animation3= AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
        launcher3_text.setVisibility(View.VISIBLE);
        launcher3_text.startAnimation(animation3);
        launcher3_text.setVisibility(View.VISIBLE);
        animation3.setFillAfter(true);
        animation3.setFillEnabled(true);

    }
    public void  stopAnimation(){

        launcher3_mountain.setVisibility(View.GONE);
        launcher3_sign.setVisibility(View.GONE);
        launcher3_sign_student.setVisibility(View.GONE);
        launcher3_text.setVisibility(View.GONE);
        launcher_yun.setVisibility(View.GONE);

        launcher3_mountain.clearAnimation();
        launcher3_sign.clearAnimation();
        launcher3_sign_student.clearAnimation();
        launcher3_text.clearAnimation();
        launcher_yun.clearAnimation();


    }
    public void onDestroy() {
        super.onDestroy();
        stopAnimation();
    }

    @Override
    protected void requestData() {

    }
}
