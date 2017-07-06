package com.coder.kzxt.course.delegate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.course.activity.LiveSynopsisActivity;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/4/13
 */

public class LiveDelegate extends PullRefreshDelegate<LiveBean>
{
    private Context mContext;
    private long systomTime;
    private String keyword;
    private int live_status;//直播状态 0=>未开始 1=>直播中 2=>已结束

    public LiveDelegate(Context context,int live_status,String keyword)
    {
        super(R.layout.item_live);
        this.mContext = context;
        this.live_status =live_status;
        this.keyword = keyword;
    }

    @Override
    public void initCustomView(BaseViewHolder view, List<LiveBean> data, int position)
    {

        ImageView course_img = (ImageView) view.findViewById(R.id.course_img);
        TextView live_name_tv = (TextView) view.findViewById(R.id.live_name_tv);
        TextView host_name_tv = (TextView) view.findViewById(R.id.host_name_tv);
        ProgressBar liveProgress = view.findViewById(R.id.liveProgress);
        LinearLayout live_sta_tv = view.findViewById(R.id.live_sta_tv);
        TextView liveTitle = view.findViewById(R.id.liveTitle);

        final LiveBean liveBean = data.get(position);

        GlideUtils.loadCourseImg(this.mContext, liveBean.getPicture(), course_img);
        live_name_tv.setText(liveBean.getLive_title());


        //搜索关键字变色
        int index1;
        ForegroundColorSpan span1 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.font_blue));//要显示的颜色
        SpannableStringBuilder builder1 = new SpannableStringBuilder(liveBean.getLive_title());
        index1 = liveBean.getLive_title().indexOf(keyword);//从第几个匹配上
        if(index1!=-1){//有这个关键字用builder显示
            builder1.setSpan(span1, index1, index1+keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            live_name_tv.setText(builder1);
        }else{//没有则直接显示
            live_name_tv.setText(liveBean.getLive_title());
        }


        long  start_time = Long.parseLong(liveBean.getStart_time());
        long end_time = Long.parseLong(liveBean.getEnd_time());
        if(live_status==1){
            live_sta_tv.setVisibility(View.VISIBLE);
            liveProgress.setVisibility(View.VISIBLE);
            if (android.os.Build.VERSION.SDK_INT > 22) {//android 6.0替换clip的加载动画
                final Drawable drawable =  mContext.getResources().getDrawable(R.drawable.liveing_anim_60);
                liveProgress.setIndeterminateDrawable(drawable);
            }else {
                final Drawable drawable =  mContext.getResources().getDrawable(R.drawable.liveing_anim);
                liveProgress.setIndeterminateDrawable(drawable);
            }

            liveTitle.setText(mContext.getResources().getString(R.string.isliving)+ DateUtil.getHourMin(start_time)+"--"+DateUtil.getHourMin(end_time));
        }else if (live_status==0){
            live_sta_tv.setVisibility(View.VISIBLE);
            liveProgress.setVisibility(View.INVISIBLE);
            liveTitle.setText("即将开始："+DateUtil.getHourMin(start_time)+"--"+DateUtil.getHourMin(end_time));
        }else {
            live_sta_tv.setVisibility(View.GONE);
        }

        if(liveBean.getUser_profile()!=null){
            host_name_tv.setText(mContext.getString(R.string.lecturer) + liveBean.getUser_profile().getNickname());
        }

        view.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LiveSynopsisActivity.gotoActivity(mContext,liveBean);
            }
        });
    }

    public void setSystomTime(long systomTime)
    {
        this.systomTime = systomTime;
    }
    public  void  setKeyword(String keyword){
        this.keyword = keyword;
    }
}
