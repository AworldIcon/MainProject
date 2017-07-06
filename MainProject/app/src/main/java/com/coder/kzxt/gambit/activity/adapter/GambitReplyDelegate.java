package com.coder.kzxt.gambit.activity.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
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
import com.coder.kzxt.gambit.activity.GambitReplyParticularsActivity;
import com.coder.kzxt.gambit.activity.bean.Comment;
import com.coder.kzxt.gambit.activity.bean.Gambit;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Urls;
import com.coder.kzxt.views.MyGridView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017/3/16.
 */

public class GambitReplyDelegate extends PullRefreshDelegate<Comment> {

    private Context mContext;
    private String from;
    private Gambit gambitEntity;

    private LinearLayout head_ly;
    private ImageView user_head_img;
    private TextView user_name;
    private TextView user_time;
    private TextView gambit_title_text;
    private TextView praise_status_tx;
    private RelativeLayout voice_ly;
    private RelativeLayout voice_bj_layout;
    private ImageView voice_img;
    private TextView voice_time_tx;
    private ProgressBar voice_progress_header;
    private MyGridView gridView_toolbar;
    private View bottom_v;
    
    private String gtitle;
    
    public Gambit getGambitEntity() {
        return gambitEntity;
    }

    public void setGambitEntity(Gambit gambitEntity) {
        this.gambitEntity = gambitEntity;
    }

    public GambitReplyDelegate(Context Context, String from, Gambit gambitEntity,String gtitle) {
        super(R.layout.gambit_reply_header,R.layout.gambit_reply_item);
        this.mContext = Context;
        this.from=from;
        this.gtitle=gtitle;
        setGambitEntity(gambitEntity);
    }

    @Override
    public void initHeaderView(BaseViewHolder holder) {

            head_ly = (LinearLayout) holder.findViewById(R.id.head_ly);
            praise_status_tx = (TextView) holder.findViewById(R.id.praise_status_tx);
            voice_time_tx = (TextView) holder.findViewById(R.id.voice_time_tx);
            voice_ly = (RelativeLayout) holder.findViewById(R.id.voice_ly);
            user_head_img = (ImageView) holder.findViewById(R.id.user_head_img);
            user_name = (TextView) holder.findViewById(R.id.user_name);
            user_time = (TextView) holder.findViewById(R.id.user_time);
            gambit_title_text = (TextView)holder.findViewById(R.id.gambit_title_text);
            voice_bj_layout = (RelativeLayout) holder.findViewById(R.id.voice_bj_layout);
            voice_img = (ImageView) holder.findViewById(R.id.voice_img);
            voice_progress_header = (ProgressBar) holder.findViewById(R.id.voice_progress_header);
            gridView_toolbar = (MyGridView) holder.findViewById(R.id.gridView_toolbar);
            bottom_v = holder.findViewById(R.id.bottom_v);
            bottom_v.setVisibility(View.GONE);
        user_head_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(from)){
                    if (from.equals("MainFragment")) {
//                        Intent intent = new Intent(mContext, Members_Of_ClassInfo_Activity.class);
//                        intent.putExtra("userid", gambitEntity.getGpostUserId());
//                        intent.putExtra(Constants.IS_CENTER,"");
//                        mContext.startActivity(intent);
                    }
                }
            }
        });
            setHeaderView(getGambitEntity());

    }
    private void setHeaderView(final Gambit gambit){
        head_ly.setVisibility(View.VISIBLE);
        String guserName = gambit.getGuserName();
        String guserFace = gambit.getGuserFace();
        String gcreatedTime = gambit.getGcreatedTime();
        String guserGender = gambit.getGuserGender();
        final String gisLiked = gambit.getGisLiked();
        final String glikeNum = gambit.getGlikeNum();
        ArrayList<String> imgs = gambit.getGimgs();
        final String gaudioUrl = gambit.getGaudioUrl();
        String gaudioDuration = gambit.getGaudioDuration();

        GlideUtils.loadCircleHeaderOfCommon(mContext,guserFace,user_head_img);
        user_name.setText(guserName);
        user_time.setText(gcreatedTime);
        if(TextUtils.isEmpty(gtitle)){
            gambit_title_text.setVisibility(View.GONE);
        }else {
            gambit_title_text.setVisibility(View.VISIBLE);
            gambit_title_text.setText(gtitle);
        }

        if (guserGender.equals("male")) {
            Drawable drawable0= mContext.getResources().getDrawable(R.drawable.user_male);
            drawable0.setBounds(0, 0, drawable0.getMinimumWidth(),drawable0.getMinimumHeight());
            user_name.setCompoundDrawables(null,null,drawable0,null);
        } else {
            Drawable drawable0= mContext.getResources().getDrawable(R.drawable.user_female);
            drawable0.setBounds(0, 0, drawable0.getMinimumWidth(),drawable0.getMinimumHeight());
            user_name.setCompoundDrawables(null,null,drawable0,null);
        }

        if(gisLiked.equals("0")){
            Drawable drawable0= mContext.getResources().getDrawable(R.drawable.gambit_praise_up);
            drawable0.setBounds(0, 0, drawable0.getMinimumWidth(),drawable0.getMinimumHeight());
            praise_status_tx.setCompoundDrawables(drawable0,null,null,null);
            praise_status_tx.setTextColor(mContext.getResources().getColor(R.color.font_gray));
        }else {
            Drawable drawable0= mContext.getResources().getDrawable(R.drawable.gambit_praise_down);
            drawable0.setBounds(0, 0, drawable0.getMinimumWidth(),drawable0.getMinimumHeight());
            praise_status_tx.setCompoundDrawables(drawable0,null,null,null);
            praise_status_tx.setTextColor(mContext.getResources().getColor(R.color.font_red));
        }

        praise_status_tx.setText(glikeNum);

        praise_status_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ispra;
                if(gambit.getGisLiked().equals("0")){
                    ispra = "1";
                    gambit.setGlikeNum(String.valueOf(Integer.parseInt(gambit.getGisLiked())+1));
                    gambit.setGisLiked("1");
                    Drawable drawable0= mContext.getResources().getDrawable(R.drawable.gambit_praise_down);
                    drawable0.setBounds(0, 0, drawable0.getMinimumWidth(),drawable0.getMinimumHeight());
                    praise_status_tx.setCompoundDrawables(drawable0,null,null,null);
                    praise_status_tx.setTextColor(mContext.getResources().getColor(R.color.font_red));
                    praise_status_tx.setText(gambit.getGlikeNum());
                }else {
                    ispra = "2";
                    gambit.setGlikeNum(String.valueOf(Integer.parseInt(gambit.getGisLiked())-1));
                    gambit.setGisLiked("0");
                    Drawable drawable0= mContext.getResources().getDrawable(R.drawable.gambit_praise_up);
                    drawable0.setBounds(0, 0, drawable0.getMinimumWidth(),drawable0.getMinimumHeight());
                    praise_status_tx.setCompoundDrawables(drawable0,null,null,null);
                    praise_status_tx.setTextColor(mContext.getResources().getColor(R.color.font_gray));
                    praise_status_tx.setText(gambit.getGlikeNum());
                }
                //调用接口有问题
             //   new GambitPraiseTask(praiseInterface,mContext, gambit.getGid(),ispra,"2",gambit.getGid()).executeOnExecutor(Constants.exec);
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
                },null, Urls.CLASS_THREAD_APPRAISE_ITEM,gambit.getGid(),ispra,"2",gambit.getGid());
                httpPost.excute();
            }
        });

        if(imgs.size()>0){
            gridView_toolbar.setVisibility(View.VISIBLE);
            ChildGridViewAdapter gridview = new ChildGridViewAdapter(mContext, imgs);
            gridView_toolbar.setAdapter(gridview);
        }else {
            gridView_toolbar.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(gaudioUrl)){
            voice_ly.setVisibility(View.GONE);
        }else {
            voice_ly.setVisibility(View.VISIBLE);
        }

        ViewGroup.LayoutParams layoutParams = voice_bj_layout.getLayoutParams();

        if (Integer.parseInt(gaudioDuration) <= 10) {
            layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.woying_80_dip);
            layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.woying_30_dip);
        } else if (Integer.parseInt(gaudioDuration) > 10 && Integer.parseInt(gaudioDuration) <= 30) {
            layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.woying_120_dip);
            layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.woying_30_dip);
        } else if (Integer.parseInt(gaudioDuration) > 30 && Integer.parseInt(gaudioDuration) <= 50) {
            layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.woying_140_dip);
            layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.woying_30_dip);
        } else if (Integer.parseInt(gaudioDuration) > 50 && Integer.parseInt(gaudioDuration) <= 60) {
            layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.woying_150_dip);
            layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.woying_30_dip);
        }
        voice_bj_layout.setLayoutParams(layoutParams);
        voice_time_tx.setText(gaudioDuration + "\"");
        voice_bj_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                voice_progress_header.setVisibility(View.VISIBLE);
                ((GambitReplyParticularsActivity)mContext).playMusic(voice_img, gaudioUrl, voice_progress_header);
            }
        });

    }


    @Override
    public void initCustomView(BaseViewHolder holder, List<Comment> data, int position) {
        TextView reply_tex = (TextView) holder.findViewById(R.id.reply_tex);
        Comment comment = data.get(position);
        String ccontent = comment.getCcontent();
        String ccreatedTime = comment.getCcreatedTime();
        final String cuserName = comment.getCuserName();
        final String creplyName = comment.getCreplyName();
        final String cuserId = comment.getCuserId();
        if(TextUtils.isEmpty(creplyName)){
            String content = cuserName+":"+ccontent+"  "+ccreatedTime;
            SpannableString styledText = new SpannableString(content);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style_text0), 0, cuserName.length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style_text1), cuserName.length()+1, content.length()-ccreatedTime.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style_text2), content.length()-ccreatedTime.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            reply_tex.setText(styledText, TextView.BufferType.SPANNABLE);
        }else {
            String content = cuserName+mContext.getResources().getString(R.string.reply_style2)+creplyName+":"+ccontent+"  "+ccreatedTime;
            String replyatx = creplyName+":"+ccontent+"  "+ccreatedTime;
            String replyNameTx = ccontent+"  "+ccreatedTime;
            SpannableString styledText = new SpannableString(content);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style_text0), 0, cuserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style_text1), cuserName.length(), content.length()-replyatx.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style_text0), cuserName.length()+2, content.length()-replyNameTx.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style_text2), content.length()-ccreatedTime.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            reply_tex.setText(styledText, TextView.BufferType.SPANNABLE);
        }
        reply_tex.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((GambitReplyParticularsActivity)mContext).isreply = true;
                ((GambitReplyParticularsActivity)mContext).reply_userId = cuserId;
                ((GambitReplyParticularsActivity)mContext).reply_user_replyname = cuserName;
                ((GambitReplyParticularsActivity)mContext).reply_write_edit.setFocusable(true);
                ((GambitReplyParticularsActivity)mContext).reply_write_edit.setFocusableInTouchMode(true);
                ((GambitReplyParticularsActivity)mContext).reply_write_edit.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) ((GambitReplyParticularsActivity)mContext).reply_write_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(((GambitReplyParticularsActivity)mContext).reply_write_edit,0);
                ((GambitReplyParticularsActivity)mContext).reply_write_edit.setText("");
                ((GambitReplyParticularsActivity)mContext).reply_write_edit.setHint(mContext.getResources().getString(R.string.reply) + cuserName);
            }
        });    }
    
}
