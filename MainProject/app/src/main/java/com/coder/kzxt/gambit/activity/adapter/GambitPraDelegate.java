package com.coder.kzxt.gambit.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.gambit.activity.ClassGambitParticularsActivity;
import com.coder.kzxt.gambit.activity.ViewPagePicsNetAct;
import com.coder.kzxt.gambit.activity.bean.GambitEntity;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pc on 2017/3/3.
 *
 */

public class GambitPraDelegate extends PullRefreshDelegate<HashMap<String, String>>
{

    private Context mContext;
    private String from;
    private GambitEntity gambitEntity;
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
    public GambitPraDelegate(Context Context, String from, GambitEntity gambitEntity)
    {
        super(R.layout.gambit_particulars_header,R.layout.give_class_member_item);
        this.mContext = Context;
        this.from=from;
        this.gambitEntity=gambitEntity;
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
        setHeaderAndBottomView(gambitEntity);
        comment_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeComSta(gambitEntity.getPostNum(), String.valueOf(((ClassGambitParticularsActivity)mContext).likeList.size()));
                ((ClassGambitParticularsActivity)mContext).myPullRecyclerView.setAdapter(((ClassGambitParticularsActivity)mContext).gambitCommentAdapter);
                //my_listview.setAdapter(comAdapter);
                // showTab = "com";
            }
        });

        praise_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePraSta(gambitEntity.getPostNum(), String.valueOf(((ClassGambitParticularsActivity)mContext).likeList.size()));
                ((ClassGambitParticularsActivity)mContext).myPullRecyclerView.setAdapter(((ClassGambitParticularsActivity)mContext).gambitPraAdapter);
//                my_listview.setAdapter(praAdapter);
//                showTab = "pra";
            }
        });
    }
    private void setHeaderAndBottomView(final GambitEntity gambitEntity) {

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
    public void initCustomView(BaseViewHolder holder, final List<HashMap<String, String>> likeList, final int position) {
        TextView member_tx = (TextView) holder.findViewById(R.id.member_tx);
//        TextView class_tx = (TextView) holder.findViewById(R.id.class_tx);
        ImageView member_img = (ImageView) holder.findViewById(R.id.member_img);
//        ImageView sex_img = (ImageView) holder.findViewById(R.id.sex_img);
//        class_tx.setVisibility(View.GONE);
        HashMap<String, String> hashMap = likeList.get(position);
        Log.d("zw--size",likeList.size()+"---size");
        String likeFace = hashMap.get("likeFace");
        String likeGender = hashMap.get("likeGender");
//			String likeId = hashMap.get("likeId");
        String likeName = hashMap.get("likeName");

        //imageLoader.displayImage(likeFace, viewHolderPra.member_img, ImageLoaderOptions.headerOptions);
        GlideUtils.loadCircleHeaderOfCommon(mContext,likeFace,member_img);
//        if (likeGender.equals("male")) {
//            sex_img.setBackgroundResource(R.drawable.user_male);
//        } else {
//            sex_img.setBackgroundResource(R.drawable.user_female);
//        }
        member_tx.setText(likeName);
    }
}
