package com.coder.kzxt.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;

/**
 * 引导页1
 * Created by Administrator on 2016/6/4.
 */
public class FirstLauncherFragment extends BaseFragment
{
    private ImageView launcher1_mountain;
    private ImageView launcher1_paper;
    private ImageView launcher1_manage;
    private ImageView launcher1_text;
    private ImageView launcher_yun;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_splash_first,container, false);
        launcher1_mountain= (ImageView) view.findViewById(R.id.launcher1_mountain);
        launcher1_paper= (ImageView) view.findViewById(R.id.launcher1_paper);
        launcher1_manage= (ImageView) view.findViewById(R.id.launcher1_manage);
        launcher1_text= (ImageView) view.findViewById(R.id.launcher1_text);
        launcher_yun= (ImageView) view.findViewById(R.id.launcher_yun);
        startAnimation();
        return view;
    }

    public void startAnimation() {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_toleft);
            launcher1_mountain.startAnimation(animation);
            launcher_yun.startAnimation(animation);
            animation.setFillEnabled(true);
            animation.setFillAfter(true);
            launcher1_mountain.setVisibility(View.VISIBLE);
            launcher_yun.setVisibility(View.VISIBLE);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_down);
                    launcher1_paper.startAnimation(animation1);
                    animation1.setFillEnabled(true);
                    animation1.setFillAfter(true);
                    launcher1_paper.setVisibility(View.VISIBLE);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_down);
                            launcher1_manage.startAnimation(animation2);
                            launcher1_manage.setVisibility(View.VISIBLE);
                            animation2.setFillEnabled(true);
                            animation2.setFillAfter(true);
                        }
                    });
                }
            });
            Animation animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
            launcher1_text.startAnimation(animation3);
            launcher1_text.setVisibility(View.VISIBLE);
            animation3.setFillEnabled(true);
            animation3.setFillAfter(true);
    }
    public void stopAnimation(){

        launcher1_mountain.setVisibility(View.GONE);
        launcher1_paper.setVisibility(View.GONE);
        launcher1_manage.setVisibility(View.GONE);
        launcher_yun.setVisibility(View.GONE);
        launcher1_text.setVisibility(View.GONE);

        launcher1_mountain.clearAnimation();
        launcher1_paper.clearAnimation();//清空view上的动画
        launcher1_manage.clearAnimation();//清空view上的动画
        launcher1_text.clearAnimation();//清空view上的动画
        launcher_yun.clearAnimation();

    }
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void requestData() {

    }
}
