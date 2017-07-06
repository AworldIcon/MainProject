package com.coder.kzxt.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.main.activity.MainActivity;

/**
 * 引导页4
 * Created by Administrator on 2016/6/4.
 */
public class FourthLauncherFragment extends LauncherBaseFragment implements View.OnClickListener{
    private ImageView launcher4_mountain;
    private ImageView launcher4_package;
    private ImageView launcher4_chat;
    private ImageView launcher4_box;
    private ImageView launcher4_text;
    private ImageView launcher_yun;
    private ImageView launcher_btn;

    private Animation animation;
    private Animation animation1;
    private Animation animation2;
    private Animation animation3;
    private Animation animation4;
    private Animation animation5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_splash_fourth,container, false);
        launcher4_mountain=(ImageView)view.findViewById(R.id.launcher4_mountain);
        launcher4_package=(ImageView)view.findViewById(R.id.launcher4_package);
        launcher4_chat=(ImageView)view.findViewById(R.id.launcher4_chat);
        launcher4_box=(ImageView)view.findViewById(R.id.launcher4_box);
        launcher4_text=(ImageView)view.findViewById(R.id.launcher4_text);
        launcher_yun=(ImageView)view.findViewById(R.id.launcher_yun);
        launcher_btn=(ImageView)view.findViewById(R.id.launcher_btn);
        launcher_btn.setVisibility(View.VISIBLE);
        return view;
    }

    public void  startAnimation(){

            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_toleft);
            launcher_yun.startAnimation(animation);
            launcher4_mountain.startAnimation(animation);
            launcher4_mountain.setVisibility(View.VISIBLE);
            launcher_yun.setVisibility(View.VISIBLE);
            launcher_btn.setVisibility(View.VISIBLE);
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
                    launcher4_package.startAnimation(animation1);
                    launcher4_package.setVisibility(View.VISIBLE);
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
                            launcher4_chat.startAnimation(animation2);
                            launcher4_chat.setVisibility(View.VISIBLE);
                            animation2.setFillAfter(true);
                            animation2.setFillEnabled(true);
                            animation2.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    animation3= AnimationUtils.loadAnimation(getActivity(), R.anim.splash_down);
                                    launcher4_box.startAnimation(animation3);
                                    launcher4_box.setVisibility(View.VISIBLE);
                                    animation3.setFillAfter(true);
                                    animation3.setFillEnabled(true);
                                }
                            });
                        }
                    });

                }
            });
        animation4= AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
        launcher4_text.startAnimation(animation4);
        launcher4_text.setVisibility(View.VISIBLE);
        animation4.setFillAfter(true);
        animation4.setFillEnabled(true);
        animation4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation5= AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                launcher_btn.startAnimation(animation5);
                launcher_btn.setVisibility(View.VISIBLE);
                animation5.setFillAfter(true);
                animation5.setFillEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        launcher_btn.setOnClickListener(this);
    }
    public void  stopAnimation(){

        launcher4_text.setVisibility(View.GONE);
        launcher_btn.setVisibility(View.INVISIBLE);
        launcher4_mountain.setVisibility(View.GONE);
        launcher4_package.setVisibility(View.GONE);
        launcher4_chat.setVisibility(View.GONE);
        launcher4_box.setVisibility(View.GONE);
        launcher_yun.setVisibility(View.GONE);

        launcher4_mountain.clearAnimation();
        launcher4_package.clearAnimation();
        launcher4_chat.clearAnimation();
        launcher4_box.clearAnimation();
        launcher_yun.clearAnimation();

    }
    private void GoToMainActivity() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        GoToMainActivity();
    }
    public void onDestroy() {
        super.onDestroy();
        stopAnimation();
    }

    @Override
    protected void requestData() {

    }
}
