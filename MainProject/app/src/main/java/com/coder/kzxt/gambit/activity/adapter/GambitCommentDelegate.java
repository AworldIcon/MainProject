package com.coder.kzxt.gambit.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.gambit.activity.ClassGambitParticularsActivity;
import com.coder.kzxt.gambit.activity.GambitReplyParticularsActivity;
import com.coder.kzxt.gambit.activity.ViewPagePicsNetAct;
import com.coder.kzxt.gambit.activity.bean.Comment;
import com.coder.kzxt.gambit.activity.bean.Gambit;
import com.coder.kzxt.gambit.activity.bean.GambitEntity;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Urls;
import com.coder.kzxt.views.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pc on 2017/3/3.
 *
 */

public class GambitCommentDelegate extends PullRefreshDelegate<Gambit>
{

    private Context mContext;
    private String from;
    LinearLayout header_ly ;
    ImageView user_head_img ;
    TextView user_name_tx ;
    TextView praise_status_tx ;
    TextView is_top_tx ;
    TextView content_tx ;
    TextView gambit_content_tx ;
    LinearLayout imgs_ly;
    ImageView single_img_iv ;
    ImageView one_img_iv;
    ImageView two_img_iv;
    ImageView tr_img_iv ;
    RelativeLayout voice_ly;
    RelativeLayout  voice_bj_layout ;
    ImageView voice_img ;
    TextView  voice_time_tx ;
    ProgressBar  voice_progress_header ;
    TextView   TextViewtime_tx;

    LinearLayout  hover_tab_ly ;
    TextView com_tx ;
    TextView  com_num_tx ;
    View com_v ;

    TextView  pra_tx;
    TextView pra_num_tx ;
    View pra_v;

    RelativeLayout comment_ly ;
    RelativeLayout praise_ly ;
    private GambitEntity gambitEntity;

    public GambitEntity getGambitEntity() {
        return gambitEntity;
    }

    public void setGambitEntity(GambitEntity gambitEntity) {
        this.gambitEntity = gambitEntity;
    }

    public static int selection;
    public GambitCommentDelegate(Context Context,String from,GambitEntity gambitEntity)
    {       
        super(R.layout.gambit_particulars_header,R.layout.gambit_info_0);
        this.mContext = Context;
        this.from=from;
        setGambitEntity(gambitEntity);
    }

    @Override
    public void initHeaderView(BaseViewHolder holder) {
        header_ly = (LinearLayout) holder.findViewById(R.id.header_ly);
         user_head_img = (ImageView) holder.findViewById(R.id.user_head_img);
         user_name_tx = (TextView) holder.findViewById(R.id.user_name_tx);
         praise_status_tx = (TextView) holder.findViewById(R.id.praise_status_tx);
        praise_status_tx.setVisibility(View.GONE);
         is_top_tx = (TextView) holder.findViewById(R.id.is_top_tx);
         content_tx = (TextView) holder.findViewById(R.id.content_tx);
         gambit_content_tx = (TextView) holder.findViewById(R.id.gambit_content_tx);
         imgs_ly = (LinearLayout) holder.findViewById(R.id.imgs_ly);
         single_img_iv = (ImageView) holder.findViewById(R.id.single_img_iv);
         one_img_iv = (ImageView) holder.findViewById(R.id.one_img_iv);
         two_img_iv = (ImageView) holder.findViewById(R.id.two_img_iv);
         tr_img_iv = (ImageView) holder.findViewById(R.id.tr_img_iv);
         voice_ly = (RelativeLayout) holder.findViewById(R.id.voice_ly);
          voice_bj_layout = (RelativeLayout) holder.findViewById(R.id.voice_bj_layout);
         voice_img = (ImageView) holder.findViewById(R.id.voice_img);
          voice_time_tx = (TextView) holder.findViewById(R.id.voice_time_tx);
          voice_progress_header = (ProgressBar) holder.findViewById(R.id.voice_progress_header);
           TextViewtime_tx = (TextView) holder.findViewById(R.id.time_tx);

          hover_tab_ly = (LinearLayout) holder.findViewById(R.id.hover_tab_ly);
        hover_tab_ly.setVisibility(View.VISIBLE);
         com_tx = (TextView) holder.findViewById(R.id.com_tx);
          com_num_tx = (TextView) holder.findViewById(R.id.com_num_tx);
        com_v = holder.findViewById(R.id.com_v);

          pra_tx = (TextView) holder.findViewById(R.id.pra_tx);
         pra_num_tx = (TextView) holder.findViewById(R.id.pra_num_tx);
        pra_v = holder.findViewById(R.id.pra_v);

         comment_ly = (RelativeLayout) holder.findViewById(R.id.comment_ly);
         praise_ly = (RelativeLayout) holder.findViewById(R.id.praise_ly);
        setHeaderAndBottomView(getGambitEntity());
        comment_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeComSta(gambitEntity.getPostNum(), String.valueOf(((ClassGambitParticularsActivity)mContext).likeList.size()));
                ((ClassGambitParticularsActivity)mContext).myPullRecyclerView.setAdapter(((ClassGambitParticularsActivity)mContext).gambitCommentAdapter);
               // showTab = "com";
            }
        });

        praise_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePraSta(gambitEntity.getPostNum(), String.valueOf(((ClassGambitParticularsActivity)mContext).likeList.size()));
                ((ClassGambitParticularsActivity)mContext).myPullRecyclerView.setAdapter(((ClassGambitParticularsActivity)mContext).gambitPraAdapter);
//                showTab = "pra";
            }
        });
    }
    public void setHeaderAndBottomView(final GambitEntity gambitEntity) {

        header_ly.setVisibility(View.VISIBLE);
        ((ClassGambitParticularsActivity)mContext).reply_ly.setVisibility(View.VISIBLE);
        user_name_tx.setText(gambitEntity.getUserName());
        //imageLoader.displayImage(gambitEntity.getUserFace(), user_head_img, ImageLoaderOptions.headerOptions);
        GlideUtils.loadCircleHeaderOfCommon(mContext,gambitEntity.getUserFace(),user_head_img);

        content_tx.setText(gambitEntity.getTitle().trim());
        String userGender = gambitEntity.getUserGender();
        String contentx = gambitEntity.getContentText();
        String istop = gambitEntity.getIsTop();
        if (istop.equals("0")) {
            is_top_tx.setVisibility(View.GONE);
        } else {
            is_top_tx.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(contentx)) {
            gambit_content_tx.setVisibility(View.GONE);
        } else {
            gambit_content_tx.setVisibility(View.VISIBLE);
            gambit_content_tx.setText(contentx);
        }

        if (userGender.equals("male")) {
            Drawable drawable0 = mContext.getResources().getDrawable(R.drawable.user_male);
            drawable0.setBounds(0, 0, drawable0.getMinimumWidth(), drawable0.getMinimumHeight());
            user_name_tx.setCompoundDrawables(null, null, drawable0, null);

        } else {
            Drawable drawable0 = mContext.getResources().getDrawable(R.drawable.user_female);
            drawable0.setBounds(0, 0, drawable0.getMinimumWidth(), drawable0.getMinimumHeight());
            user_name_tx.setCompoundDrawables(null, null, drawable0, null);
        }

        changeComSta(gambitEntity.getPostNum(), String.valueOf( ((ClassGambitParticularsActivity)mContext).likeList.size()));

        final ArrayList<String> imgs = gambitEntity.getImgs();

        if (imgs.size() == 0) {
            single_img_iv.setVisibility(View.GONE);
            imgs_ly.setVisibility(View.GONE);
        } else {
            if (imgs.size() == 1) {
                single_img_iv.setVisibility(View.VISIBLE);
                imgs_ly.setVisibility(View.GONE);
                GlideUtils.loadPorstersImg(mContext,imgs.get(0),single_img_iv);
            } else if (imgs.size() == 2) {

                single_img_iv.setVisibility(View.GONE);
                imgs_ly.setVisibility(View.VISIBLE);
                one_img_iv.setVisibility(View.VISIBLE);
                two_img_iv.setVisibility(View.VISIBLE);
                tr_img_iv.setVisibility(View.INVISIBLE);

                GlideUtils.loadPorstersImg(mContext,imgs.get(0),one_img_iv);
                GlideUtils.loadPorstersImg(mContext,imgs.get(1),two_img_iv);

            } else if (imgs.size() >= 3) {

                single_img_iv.setVisibility(View.GONE);
                imgs_ly.setVisibility(View.VISIBLE);
                one_img_iv.setVisibility(View.VISIBLE);
                two_img_iv.setVisibility(View.VISIBLE);
                tr_img_iv.setVisibility(View.VISIBLE);

                GlideUtils.loadPorstersImg(mContext,imgs.get(0),one_img_iv);
                GlideUtils.loadPorstersImg(mContext,imgs.get(1),two_img_iv);
                GlideUtils.loadPorstersImg(mContext,imgs.get(2),tr_img_iv);
            }
        }

        String audioUrl = gambitEntity.getAudioUrl();
        final String audioDuration = gambitEntity.getAudioDuration();

        if (!TextUtils.isEmpty(audioUrl)) {
            voice_ly.setVisibility(View.VISIBLE);
            voice_time_tx.setText(audioDuration + "\"");
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) voice_bj_layout.getLayoutParams();
            if (Integer.parseInt(audioDuration) <= 10) {
                layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.woying_80_dip);
                layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.woying_30_dip);
            } else if (Integer.parseInt(audioDuration) > 10 && Integer.parseInt(audioDuration) <= 30) {
                layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.woying_120_dip);
                layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.woying_30_dip);
            } else if (Integer.parseInt(audioDuration) > 30 && Integer.parseInt(audioDuration) <= 50) {
                layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.woying_140_dip);
                layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.woying_30_dip);
            } else if (Integer.parseInt(audioDuration) > 50 && Integer.parseInt(audioDuration) <= 60) {
                layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.woying_150_dip);
                layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.woying_30_dip);
            }
        } else {
            voice_ly.setVisibility(View.GONE);
        }
        ((ClassGambitParticularsActivity)mContext).time_tx.setText(gambitEntity.getCreatedTime());
        if (gambitEntity.getIsLiked().equals("0")) {
            ((ClassGambitParticularsActivity)mContext).reply_iv.setBackgroundResource(R.drawable.gambit_replay_praise_up);
        } else {
            ((ClassGambitParticularsActivity)mContext).reply_iv.setBackgroundResource(R.drawable.gambit_replay_praise_down);
        }
        //点赞
        ((ClassGambitParticularsActivity)mContext).reply_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gambitEntity.getIsLiked().equals("0")) {
                    ((ClassGambitParticularsActivity)mContext).ispra = "1";
                    ((ClassGambitParticularsActivity)mContext).reply_iv.setBackgroundResource(R.drawable.gambit_replay_praise_down);
                    gambitEntity.setIsLiked("1");
                    String currentTx = pra_num_tx.getText().toString();
                    String currentNum = currentTx.substring(currentTx.indexOf("(") + 1, currentTx.indexOf(")"));
                    pra_num_tx.setText("(" + String.valueOf(Integer.parseInt(currentNum) + 1) + ")");
                    //把自己的用户数据添加到点赞集合
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    /**
                     *
                     * */
                    hashMap.put("likeFace", new SharedPreferencesUtil(mContext).getUserHead());
                    hashMap.put("likeGender", new SharedPreferencesUtil(mContext).getSex());
                    hashMap.put("likeName", new SharedPreferencesUtil(mContext).getNickName());
                    hashMap.put("likeId", String.valueOf(new SharedPreferencesUtil(mContext).getUid()));
                    ((ClassGambitParticularsActivity)mContext).likeList.add(0, hashMap);
                    ((ClassGambitParticularsActivity)mContext).gambitPraAdapter.notifyDataSetChanged();
                } else {
                    ((ClassGambitParticularsActivity)mContext).ispra = "2";
                    ((ClassGambitParticularsActivity)mContext).reply_iv.setBackgroundResource(R.drawable.gambit_replay_praise_up);
                    gambitEntity.setIsLiked("0");
                    String currentTx = pra_num_tx.getText().toString();
                    String currentNum = currentTx.substring(currentTx.indexOf("(") + 1, currentTx.indexOf(")"));
                    pra_num_tx.setText("(" + String.valueOf(Integer.parseInt(currentNum) - 1) + ")");

                    //取消赞在点赞集合中清除自己数据
                    for (int i = 0; i <  ((ClassGambitParticularsActivity)mContext).likeList.size(); i++) {
                        if (String.valueOf(new SharedPreferencesUtil(mContext).getUid()).equals( ((ClassGambitParticularsActivity)mContext).likeList.get(i).get("likeId"))) {
                            ((ClassGambitParticularsActivity)mContext).likeList.remove(i);
                        }
                    }
                    ((ClassGambitParticularsActivity)mContext).gambitPraAdapter.notifyDataSetChanged();
                }
                HttpPostOld httpPost=new HttpPostOld(mContext, mContext, new InterfaceHttpResult() {
                    @Override
                    public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                        if(code==Constants.HTTP_CODE_SUCCESS){
                            //成功后需要重新刷新整个列表数据
                            ((ClassGambitParticularsActivity)mContext).httpRequest();
                            ToastUtils.makeText(mContext,msg, Toast.LENGTH_SHORT).show();
                        }else {
                            ToastUtils.makeText(mContext,mContext.getResources().getString(R.string.opera_fail), Toast.LENGTH_SHORT).show();

                        }
                    }
                },null, Urls.CLASS_THREAD_APPRAISE,gambitEntity.getId(),((ClassGambitParticularsActivity)mContext).ispra,"1");
                httpPost.excute();
                //isRefreshLastPage = true;
            }
        });

        ((ClassGambitParticularsActivity)mContext).reply_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    ((ClassGambitParticularsActivity)mContext).reply_tx.setVisibility(View.GONE);
                } else {
                    ((ClassGambitParticularsActivity)mContext).reply_tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //发送
        ((ClassGambitParticularsActivity)mContext).reply_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClassGambitParticularsActivity)mContext).loginHideSoftInputWindow();
                ((ClassGambitParticularsActivity)mContext).jiazai_layout.setVisibility(View.VISIBLE);
                if (! ((ClassGambitParticularsActivity)mContext).isreply) {
                    //评论
                    HttpPostOld httpPost=new HttpPostOld(mContext, mContext, new InterfaceHttpResult() {
                        @Override
                        public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                            ((ClassGambitParticularsActivity)mContext).jiazai_layout.setVisibility(View.GONE);
                            if(code==Constants.HTTP_CODE_SUCCESS){
                                ((ClassGambitParticularsActivity)mContext).index=1;
                                ((ClassGambitParticularsActivity)mContext).httpRequest();
                            }else {
                                ToastUtils.makeText(mContext,mContext.getResources().getString(R.string.opera_fail), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },null,Urls.POST_CLASS_THREAD,((ClassGambitParticularsActivity)mContext).gambitId,((ClassGambitParticularsActivity)mContext).reply_et.getText().toString(),"0");
                    httpPost.excute();
                } else {
                    //回复
                    HttpPostOld httpPost=new HttpPostOld(mContext, mContext, new InterfaceHttpResult() {
                        @Override
                        public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                            ((ClassGambitParticularsActivity)mContext).jiazai_layout.setVisibility(View.GONE);
                            if(code==Constants.HTTP_CODE_SUCCESS){
                                ((ClassGambitParticularsActivity)mContext).ok=true;
                                ((ClassGambitParticularsActivity)mContext).httpRequest();
                            }else {
                                ToastUtils.makeText(mContext,mContext.getResources().getString(R.string.opera_fail), Toast.LENGTH_SHORT).show();
                            }

                        }
                    },null,Urls.POST_CLASS_THREAD_ACTION,((ClassGambitParticularsActivity)mContext).replayGid,((ClassGambitParticularsActivity)mContext).replayGpostId,"",((ClassGambitParticularsActivity)mContext).reply_et.getText().toString().trim());
                    httpPost.excute();
                }
            }
        });

        //点击查看大图
        single_img_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewPagePicsNetAct.class);
                intent.putStringArrayListExtra("imgurl", imgs);
                intent.putExtra("index", 0);
                ((ClassGambitParticularsActivity)mContext).startActivity(intent);
            }
        });

        one_img_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewPagePicsNetAct.class);
                intent.putStringArrayListExtra("imgurl", imgs);
                intent.putExtra("index", 0);
                ((ClassGambitParticularsActivity)mContext).startActivity(intent);
            }
        });

        two_img_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewPagePicsNetAct.class);
                intent.putStringArrayListExtra("imgurl", imgs);
                intent.putExtra("index", 1);
                ((ClassGambitParticularsActivity)mContext).startActivity(intent);
            }
        });

        tr_img_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewPagePicsNetAct.class);
                intent.putStringArrayListExtra("imgurl", imgs);
                intent.putExtra("index", 2);
                ((ClassGambitParticularsActivity)mContext).startActivity(intent);

            }
        });
        //播放语音
        voice_bj_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                voice_progress_header.setVisibility(View.VISIBLE);
                ((ClassGambitParticularsActivity)mContext).playHeaderMusic(voice_img, gambitEntity.getAudioUrl(), voice_progress_header);
            }
        });
    }


    public void changeComSta(String postNum, String praNum) {
        com_tx.setTextColor(mContext.getResources().getColor(R.color.first_theme));
        com_num_tx.setTextColor(mContext.getResources().getColor(R.color.first_theme));
        com_num_tx.setText("(" + postNum + ")");
        Drawable drawable0 = mContext.getResources().getDrawable(R.drawable.gambit_pager_com_down);
        // 这一步必须要做,否则不会显示.
        drawable0.setBounds(0, 0, drawable0.getMinimumWidth(), drawable0.getMinimumHeight());
        com_tx.setCompoundDrawables(drawable0, null, null, null);
        com_v.setVisibility(View.GONE);
        pra_tx.setTextColor(mContext.getResources().getColor(R.color.font_black));
        pra_num_tx.setTextColor(mContext.getResources().getColor(R.color.font_black));
        pra_num_tx.setText("(" + praNum + ")");
        Drawable drawable1 = mContext.getResources().getDrawable(R.drawable.gambit_pager_praise_up);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        pra_tx.setCompoundDrawables(drawable1, null, null, null);
        pra_v.setVisibility(View.VISIBLE);
    }

    private void changePraSta(String postNum, String praNum) {

        pra_tx.setTextColor(mContext.getResources().getColor(R.color.first_theme));
        pra_num_tx.setTextColor(mContext.getResources().getColor(R.color.first_theme));
        pra_num_tx.setText("(" + praNum + ")");
        Drawable drawable1 = mContext.getResources().getDrawable(R.drawable.gambit_pager_praise_down);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        pra_tx.setCompoundDrawables(drawable1, null, null, null);
        pra_v.setVisibility(View.GONE);
        
        com_tx.setTextColor(mContext.getResources().getColor(R.color.font_black));
        com_num_tx.setTextColor(mContext.getResources().getColor(R.color.font_black));
        com_num_tx.setText("(" + postNum + ")");
        Drawable drawable0 = mContext.getResources().getDrawable(R.drawable.gambit_pager_com_up);
        // 这一步必须要做,否则不会显示.
        drawable0.setBounds(0, 0, drawable0.getMinimumWidth(), drawable0.getMinimumHeight());
        com_tx.setCompoundDrawables(drawable0, null, null, null);
        com_v.setVisibility(View.VISIBLE);
    }
    @Override
    public void initCustomView(BaseViewHolder holder, final List<Gambit> comList, final int position) {

            ImageView user_head_img = (ImageView) holder.findViewById(R.id.user_head_img);
            TextView user_name = (TextView) holder.findViewById(R.id.user_name);
            TextView user_time = (TextView) holder.findViewById(R.id.user_time);
            TextView gambit_title_text = (TextView) holder.findViewById(R.id.gambit_title_text);
            TextView praise_status_tx = (TextView) holder.findViewById(R.id.praise_status_tx);

            RelativeLayout voice_ly = (RelativeLayout) holder.findViewById(R.id.voice_ly);
            RelativeLayout voice_bj_layout = (RelativeLayout) holder.findViewById(R.id.voice_bj_layout);
            ImageView voice_img = (ImageView) holder.findViewById(R.id.voice_img);
            TextView voice_time_tx = (TextView) holder.findViewById(R.id.voice_time_tx);
            ProgressBar voice_progress_header = (ProgressBar) holder.findViewById(R.id.voice_progress_header);

            LinearLayout reply_ly_zong = (LinearLayout) holder.findViewById(R.id.reply_ly_zong);
            LinearLayout reply_ly = (LinearLayout) holder.findViewById(R.id.reply_ly);
            LinearLayout reply_ly_two = (LinearLayout) holder.findViewById(R.id.reply_ly_two);
            TextView reply_tex = (TextView) holder.findViewById(R.id.reply_tex);
            TextView reply_tex_two = (TextView) holder.findViewById(R.id.reply_tex_two);
            TextView all_reply_tx = (TextView) holder.findViewById(R.id.all_reply_tx);
            MyGridView gridView_toolbar = (MyGridView) holder.findViewById(R.id.gridView_toolbar);
            View fenge_view = holder.findViewById(R.id.fenge_view);
            final Gambit gambit = comList.get(position);
            final String gid = gambit.getGid();
            String guserFace = gambit.getGuserFace();
            String guserName = gambit.getGuserName();
            String gcreatedTime = gambit.getGcreatedTime();
            String guserGender = gambit.getGuserGender();
//				final String gtitle = gambit.getGtitle();
            final String gcontentText = gambit.getGcontentText();
            String gpostNum = gambit.getGpostCommentNum();
            ArrayList<Comment> comments = gambit.getComments();
            ArrayList<String> imgs = gambit.getGimgList();
            final String gpostId = gambit.getGpostId();
            String gaudioDuration = gambit.getGaudioDuration();
            final String gaudioUrl = gambit.getGaudioUrl();
            String gaudioPlaying = gambit.getGaudioPlaying();
            String gaudioProgress = gambit.getGaudioProgress();
            final String gisLiked = gambit.getGisLiked();
            final String glikeNum = gambit.getGlikeNum();

            //imageLoader.displayImage(guserFace,  user_head_img, ImageLoaderOptions.headerOptions);
            GlideUtils.loadCircleHeaderOfCommon(mContext,guserFace,user_head_img);

        user_name.setText(guserName);
             user_time.setText(gcreatedTime);
            if (TextUtils.isEmpty(gcontentText)) {
                 gambit_title_text.setVisibility(View.GONE);
            } else {
                 gambit_title_text.setVisibility(View.VISIBLE);
                 gambit_title_text.setText(gcontentText);
            }
             user_head_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!TextUtils.isEmpty(from)) {
                        if (from.equals("MainFragment")) {
                            ((ClassGambitParticularsActivity)mContext).stopMusic();
                            for (int i = 0; i < comList.size(); i++) {
                                comList.get(i).setGaudioPlaying("0");
                                comList.get(i).setGaudioProgress("0");
                            }
                            notifyDataSetChanged();

//                            Intent intent = new Intent( mContext, Members_Of_ClassInfo_Activity.class);
//                            intent.putExtra(Constants.IS_CENTER, isCenter);
//                            if (position == 0) {
//                                intent.putExtra("userid", gambit.getGuserId());
//                            } else {
//                                intent.putExtra("userid", gambit.getGpostUserId());
//                            }
//                            startActivity(intent);
                        }
                    }
                }
            });

            if (gisLiked.equals("0")) {
                Drawable drawable0 =   mContext.getResources().getDrawable(R.drawable.gambit_praise_up);
                drawable0.setBounds(0, 0, drawable0.getMinimumWidth(), drawable0.getMinimumHeight());
                 praise_status_tx.setCompoundDrawables(drawable0, null, null, null);
                 praise_status_tx.setTextColor(  mContext.getResources().getColor(R.color.font_gray));
            } else {
                Drawable drawable0 =   mContext.getResources().getDrawable(R.drawable.gambit_praise_down);
                drawable0.setBounds(0, 0, drawable0.getMinimumWidth(), drawable0.getMinimumHeight());
                 praise_status_tx.setCompoundDrawables(drawable0, null, null, null);
                 praise_status_tx.setTextColor(  mContext.getResources().getColor(R.color.font_red));
            }

             praise_status_tx.setText(glikeNum);

             praise_status_tx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ispra;
                    if (gisLiked.equals("0")) {
                        ispra = "1";
                        gambit.setGlikeNum(String.valueOf(Integer.parseInt(glikeNum) + 1));
                        gambit.setGisLiked("1");
                    } else {
                        ispra = "2";
                        gambit.setGlikeNum(String.valueOf(Integer.parseInt(glikeNum) - 1));
                        gambit.setGisLiked("0");
                    }
                    //给一条评论的点赞或取消,是否还需调试？提交对单条评论的操作成功后，退出再返回，没有改变
                    HttpPostOld httpPost=new HttpPostOld(mContext, mContext, new InterfaceHttpResult() {
                        @Override
                        public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                            if(code==Constants.HTTP_CODE_SUCCESS){
                                notifyDataSetChanged();
                                ToastUtils.makeText(mContext,msg, Toast.LENGTH_SHORT).show();
                            }else {
                                //ToastUtils.makeText(mContext,mContext.getResources().getString(R.string.opera_fail), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },null, Urls.CLASS_THREAD_APPRAISE_ITEM,gambitEntity.getId(),ispra,"2",gpostId);
                    httpPost.excute();
//                    new GambitPraiseTask(praiseInterface,  mContext,
//                            gambitEntity.getService_id(), ispra, "2", gpostId).executeOnExecutor(Constants.exec);
                }
            });

            if (guserGender.equals("male")) {
                Drawable drawable0 =  mContext.getResources().getDrawable(R.drawable.user_male);
                drawable0.setBounds(0, 0, drawable0.getMinimumWidth(), drawable0.getMinimumHeight());
                 user_name.setCompoundDrawables(null, null, drawable0, null);
            } else {
                Drawable drawable0 =  mContext.getResources().getDrawable(R.drawable.user_female);
                drawable0.setBounds(0, 0, drawable0.getMinimumWidth(), drawable0.getMinimumHeight());
                 user_name.setCompoundDrawables(null, null, drawable0, null);
            }

            if (imgs.size() > 0) {
                 gridView_toolbar.setVisibility(View.VISIBLE);
                ChildGridViewAdapter gridview = new ChildGridViewAdapter( mContext, imgs);
                 gridView_toolbar.setAdapter(gridview);
            } else {
                 gridView_toolbar.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(gaudioUrl)) {
                 voice_ly.setVisibility(View.GONE);
            } else {
                 voice_ly.setVisibility(View.VISIBLE);

                ViewGroup.LayoutParams layoutParams =  voice_bj_layout.getLayoutParams();

                if (Integer.parseInt(gaudioDuration) <= 10) {
                    layoutParams.width = (int)  mContext.getResources().getDimension(R.dimen.woying_80_dip);
                    layoutParams.height = (int)  mContext.getResources().getDimension(R.dimen.woying_30_dip);
                } else if (Integer.parseInt(gaudioDuration) > 10 && Integer.parseInt(gaudioDuration) <= 30) {
                    layoutParams.width = (int)  mContext.getResources().getDimension(R.dimen.woying_120_dip);
                    layoutParams.height = (int)  mContext.getResources().getDimension(R.dimen.woying_30_dip);
                } else if (Integer.parseInt(gaudioDuration) > 30 && Integer.parseInt(gaudioDuration) <= 50) {
                    layoutParams.width = (int)  mContext.getResources().getDimension(R.dimen.woying_140_dip);
                    layoutParams.height = (int)  mContext.getResources().getDimension(R.dimen.woying_30_dip);
                } else if (Integer.parseInt(gaudioDuration) > 50 && Integer.parseInt(gaudioDuration) <= 60) {
                    layoutParams.width = (int)  mContext.getResources().getDimension(R.dimen.woying_150_dip);
                    layoutParams.height = (int)  mContext.getResources().getDimension(R.dimen.woying_30_dip);
                }

                 voice_bj_layout.setLayoutParams(layoutParams);

                 voice_time_tx.setText(gaudioDuration + "\"");

                if (gaudioPlaying.equals("0")) {
                     voice_img.setImageResource(R.drawable.play_voice_false);
                    AnimationDrawable animationDrawable = (AnimationDrawable)  voice_img.getDrawable();
                    animationDrawable.stop();
                } else {
                     voice_img.setImageResource(R.drawable.play_voice);
                    AnimationDrawable animationDrawable = (AnimationDrawable)  voice_img.getDrawable();
                    animationDrawable.start();
                }

                if (gaudioProgress.equals("0")) {
                     voice_progress_header.setVisibility(View.GONE);
                } else {
                     voice_progress_header.setVisibility(View.VISIBLE);
                }
            }

            if (comments == null || comments.size() == 0) {
                 reply_ly_zong.setVisibility(View.GONE);
                 fenge_view.setVisibility(View.GONE);
            } else {
                 fenge_view.setVisibility(View.VISIBLE);
                 reply_ly_zong.setVisibility(View.VISIBLE);
                if (comments.size() >= 2) {

                     reply_ly.setVisibility(View.VISIBLE);
                     reply_ly_two.setVisibility(View.VISIBLE);
                    Comment comment0 = comments.get(0);
                    String commentccreatedTime0 = comment0.getCcreatedTime();
                    String ccontent0 = comment0.getCcontent();
                    String cuserName0 = comment0.getCuserName();
                    String creplyName0 = comment0.getCreplyName();

                    if (TextUtils.isEmpty(creplyName0)) {
                        String content = cuserName0 + ":" + ccontent0 + "  " + commentccreatedTime0;
                        SpannableString styledText = new SpannableString(content);
                        styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style_text0), 0, cuserName0.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text1), cuserName0.length() + 1, content.length() - commentccreatedTime0.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text2), content.length() - commentccreatedTime0.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                         reply_tex.setText(styledText, TextView.BufferType.SPANNABLE);

                    } else {
                        String content = cuserName0 +  mContext.getResources().getString(R.string.reply_style2) + creplyName0 + ":" + ccontent0 + "  " + commentccreatedTime0;
                        String replyatx = creplyName0 + ":" + ccontent0 + "  " + commentccreatedTime0;
                        String replyNameTx = ccontent0 + "  " + commentccreatedTime0;
                        SpannableString styledText = new SpannableString(content);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text0), 0, cuserName0.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text1), cuserName0.length(), content.length() - replyatx.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text0), cuserName0.length() + 2, content.length() - replyNameTx.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text2), content.length() - commentccreatedTime0.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                         reply_tex.setText(styledText, TextView.BufferType.SPANNABLE);
                    }

                    Comment comment1 = comments.get(1);
                    String commentccreatedTime1 = comment1.getCcreatedTime();
                    String ccontent1 = comment1.getCcontent();
                    String cuserName1 = comment1.getCuserName();
                    String creplyName1 = comment1.getCreplyName();

                    if (TextUtils.isEmpty(creplyName1)) {
                        String content = cuserName1 + ":" + ccontent1 + "  " + commentccreatedTime1;
                        SpannableString styledText = new SpannableString(content);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text0), 0, cuserName1.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text1), cuserName1.length() + 1, content.length() - commentccreatedTime1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text2), content.length() - commentccreatedTime1.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                         reply_tex_two.setText(styledText, TextView.BufferType.SPANNABLE);

                    } else {
                        String content = cuserName1 +  mContext.getResources().getString(R.string.reply_style2) + creplyName1 + ":" + ccontent1 + "  " + commentccreatedTime1;
                        String replyatx = creplyName1 + ":" + ccontent1 + "  " + commentccreatedTime1;
                        String replyNameTx = ccontent1 + "  " + commentccreatedTime1;

                        SpannableString styledText = new SpannableString(content);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text0), 0, cuserName1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text1), cuserName1.length(), content.length() - replyatx.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text0), cuserName1.length() + 2, content.length() - replyNameTx.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text2), content.length() - commentccreatedTime1.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                         reply_tex_two.setText(styledText, TextView.BufferType.SPANNABLE);
                    }

                } else {
                     reply_ly.setVisibility(View.VISIBLE);
                     reply_ly_two.setVisibility(View.GONE);

                    Comment comment0 = comments.get(0);
                    String commentccreatedTime0 = comment0.getCcreatedTime();
                    String ccontent0 = comment0.getCcontent();
                    String cuserName0 = comment0.getCuserName();
                    String creplyName0 = comment0.getCreplyName();

                    if (TextUtils.isEmpty(creplyName0)) {
                        String content = cuserName0 + ":" + ccontent0 + "  " + commentccreatedTime0;
                        SpannableString styledText = new SpannableString(content);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text0), 0, cuserName0.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text1), cuserName0.length() + 1, content.length() - commentccreatedTime0.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text2), content.length() - commentccreatedTime0.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                         reply_tex.setText(styledText, TextView.BufferType.SPANNABLE);

                    } else {
                        String content = cuserName0 +  mContext.getResources().getString(R.string.reply_style2) + creplyName0 + ":" + ccontent0 + "  " + commentccreatedTime0;
                        String replyatx = creplyName0 + ":" + ccontent0 + "  " + commentccreatedTime0;
                        String replyNameTx = ccontent0 + "  " + commentccreatedTime0;

                        SpannableString styledText = new SpannableString(content);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text0), 0, cuserName0.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text1), cuserName0.length(), content.length() - replyatx.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text0), cuserName0.length() + 2, content.length() - replyNameTx.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new TextAppearanceSpan( mContext, R.style.style_text2), content.length() - commentccreatedTime0.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                         reply_tex.setText(styledText, TextView.BufferType.SPANNABLE);
                    }
                }
            }
             voice_bj_layout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 播放.amr音频文件
                    for (int i = 0; i < comList.size(); i++) {
                        comList.get(i).setGaudioPlaying("0");
                        comList.get(i).setGaudioProgress("0");
                    }

                    gambit.setGaudioProgress("1");
                    notifyDataSetChanged();
                    ((ClassGambitParticularsActivity)mContext).playMusic(gambit, gaudioUrl);
                }
            });
                //话题回复详情
             all_reply_tx.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((ClassGambitParticularsActivity)mContext).stopMusic();
                    Intent intent = new Intent( mContext, GambitReplyParticularsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("gambit", gambit);
                    intent.putExtra("reply_id", gpostId);
                    intent.putExtra("gambitId", gid);
                    intent.putExtra("from", from);
                    intent.putExtra("topicTitle", gcontentText);
                    intent.putExtra(Constants.IS_CENTER, "0");
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection=position;
                if(position<20){
                    ((ClassGambitParticularsActivity)mContext).selection=position;
                }else if(position>=20&&position<40){
                    ((ClassGambitParticularsActivity)mContext).selection=position-20;
                }
                Log.d("zw--",selection+"selection");
                Gambit gambit = comList.get(position);
                ((ClassGambitParticularsActivity)mContext).replayGid = gambit.getGid();
                ((ClassGambitParticularsActivity)mContext).replayGpostId = gambit.getGpostId();
                String guserName = gambit.getGuserName();
                ((ClassGambitParticularsActivity)mContext).isreply = true;
                ((ClassGambitParticularsActivity)mContext).reply_et.setFocusable(true);
                ((ClassGambitParticularsActivity)mContext).reply_et.setFocusableInTouchMode(true);
                ((ClassGambitParticularsActivity)mContext).reply_et.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) ((ClassGambitParticularsActivity)mContext).reply_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(((ClassGambitParticularsActivity)mContext).reply_et, 0);
                ((ClassGambitParticularsActivity)mContext).reply_et.setText("");
                ((ClassGambitParticularsActivity)mContext).reply_et.setHint(mContext.getResources().getString(R.string.reply) + guserName);
            }
        });
        }




}
