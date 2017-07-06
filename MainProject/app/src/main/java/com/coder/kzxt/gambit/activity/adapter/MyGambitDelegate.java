package com.coder.kzxt.gambit.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import com.coder.kzxt.gambit.activity.bean.MyGambitBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Urls;

import java.util.List;

/**
 * Created by pc on 2017/3/3.
 *
 */

public class MyGambitDelegate extends PullRefreshDelegate<MyGambitBean.DataBean>
{

    private Context mContext;
    private String from;
    public MyGambitDelegate(Context Context,String from)
    {
        super(R.layout.item_gambit_adapter);
        this.mContext = Context;
        this.from=from;
    }
    

    @Override
    public void initCustomView(BaseViewHolder holder, final List<MyGambitBean.DataBean> data, final int position) {
        ImageView user_head_img = (ImageView) holder.findViewById(R.id.user_head_img);
        TextView user_name_tx = (TextView) holder.findViewById(R.id.user_name_tx);
        TextView content_tx = (TextView) holder.findViewById(R.id.content_tx);
        TextView praise_status_tx = (TextView) holder.findViewById(R.id.praise_status_tx);
        TextView is_top_tx = (TextView) holder.findViewById(R.id.is_top_tx);


        LinearLayout imgs_ly = (LinearLayout) holder.findViewById(R.id.imgs_ly);
        ImageView single_img_iv = (ImageView) holder.findViewById(R.id.single_img_iv);
        ImageView one_img_iv  =(ImageView) holder.findViewById(R.id.one_img_iv);
        ImageView two_img_iv  =(ImageView) holder.findViewById(R.id.two_img_iv);
        ImageView tr_img_iv  =(ImageView) holder.findViewById(R.id.tr_img_iv);

        RelativeLayout voice_ly = (RelativeLayout) holder.findViewById(R.id.voice_ly);
        RelativeLayout voice_bj_layout = (RelativeLayout) holder.findViewById(R.id.voice_bj_layout);
        ImageView voice_img = (ImageView) holder.findViewById(R.id.voice_img);
        TextView voice_time_tx = (TextView) holder.findViewById(R.id.voice_time_tx);
        ProgressBar voice_progress_header = (ProgressBar) holder.findViewById(R.id.voice_progress_header);
        TextView time_tx = (TextView) holder.findViewById(R.id.time_tx);
        final MyGambitBean.DataBean gambitEntity = data.get(position);
        user_name_tx.setText(gambitEntity.getUserName());
        //imageLoader.displayImage(gambitEntity.getUserFace(), holder.user_head_img, ImageLoaderOptions.headerOptions);
        GlideUtils.loadCircleHeaderOfCommon(mContext,gambitEntity.getUserFace(),user_head_img);
        content_tx.setText(gambitEntity.getTitle());

        //0非置顶1置顶
        if(gambitEntity.getIsTop().equals("0")){
            is_top_tx.setVisibility(View.GONE);
        }else {
            is_top_tx.setVisibility(View.VISIBLE);
        }

        final String isLiked = gambitEntity.getIsLiked();
        final String likeNum = gambitEntity.getLikeNum();

        if(isLiked.equals("0")){
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

        if(likeNum.length()>4){
            praise_status_tx.setText("1万+");
        }else {
            praise_status_tx.setText(likeNum);
        }


        praise_status_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ispra =null;
                if(isLiked.equals("0")){
                    ispra = "1";
                    gambitEntity.setLikeNum(String.valueOf(Integer.parseInt(likeNum)+1));
                    gambitEntity.setIsLiked("1");
                }else {
                    ispra = "2";
                    gambitEntity.setLikeNum(String.valueOf(Integer.parseInt(likeNum)-1));
                    gambitEntity.setIsLiked("0");

                }
                HttpPostOld httpPost=new HttpPostOld(mContext, mContext, new InterfaceHttpResult() {
                    @Override
                    public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                        if(code==Constants.HTTP_CODE_SUCCESS){
                            notifyDataSetChanged();
                            ToastUtils.makeText(mContext,msg, Toast.LENGTH_SHORT);
                        }
                    }
                },null, Urls.CLASS_THREAD_APPRAISE,gambitEntity.getId(),ispra,"1");
                httpPost.excute();

            }
        });

        List<String> imgs = gambitEntity.getImg();
        if(imgs.size()==0){
            single_img_iv.setVisibility(View.GONE);
            imgs_ly.setVisibility(View.GONE);
        }else{
            if(imgs.size()==1){
                single_img_iv.setVisibility(View.VISIBLE);
                imgs_ly.setVisibility(View.GONE);
                GlideUtils.loadPorstersImg(mContext,imgs.get(0),single_img_iv);
            }else if (imgs.size()==2){

                single_img_iv.setVisibility(View.GONE);
                imgs_ly.setVisibility(View.VISIBLE);
                one_img_iv.setVisibility(View.VISIBLE);
                two_img_iv.setVisibility(View.VISIBLE);
                tr_img_iv.setVisibility(View.INVISIBLE);

                GlideUtils.loadPorstersImg(mContext,imgs.get(0),one_img_iv);
                GlideUtils.loadPorstersImg(mContext,imgs.get(1),two_img_iv);

            }else if (imgs.size()>=3){

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

        if(!TextUtils.isEmpty(audioUrl)){
            voice_ly.setVisibility(View.VISIBLE);
            voice_time_tx.setText(audioDuration+"\"");

            ViewGroup.LayoutParams layoutParams = voice_bj_layout.getLayoutParams();
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
                layoutParams.height = (int)mContext.getResources().getDimension(R.dimen.woying_30_dip);

            }

        }else {
            voice_ly.setVisibility(View.GONE);
        }
       time_tx.setText(gambitEntity.getCreatedTime());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassGambitParticularsActivity.class);
                intent.putExtra("gambitId",  gambitEntity.getId());
                intent.putExtra("from",from);
                ((Activity)mContext).startActivityForResult(intent,1);
            }
        });
    }
}
