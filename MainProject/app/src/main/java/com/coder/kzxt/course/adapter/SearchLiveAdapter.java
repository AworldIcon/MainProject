package com.coder.kzxt.course.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.course.activity.LiveSynopsisActivity;
import com.coder.kzxt.course.activity.SearchLiveListActivity;
import com.coder.kzxt.main.beans.LivePlayingBean;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/4/13
 */


public class SearchLiveAdapter extends BaseExpandableListAdapter
{

    private List<LivePlayingBean> mData;
    private Context context;
    private String keyword;

    public SearchLiveAdapter(Context context, List<LivePlayingBean> v)
    {
        this.context = context;
        mData = v;
    }

    @Override
    public int getGroupCount()
    {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        LivePlayingBean livePlayingBean = mData.get(groupPosition);

        if (livePlayingBean.getType().equals("review"))
        {
            return livePlayingBean.getVideoBean().size() + 1;
        } else if (livePlayingBean.getType().equals("notice"))
        {
            return livePlayingBean.getLiveCourseBean().size() + 1;
        } else
        {
            return livePlayingBean.getLiveCourseBean().size() + 1;
        }
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_search_live_group_item, null);

        final TextView title = (TextView) view.findViewById(R.id.title);
        TextView number = (TextView) view.findViewById(R.id.number);

        LivePlayingBean livePlayingBean = mData.get(groupPosition);

        switch (livePlayingBean.getType())
        {
            case "live":
                title.setText(context.getString(R.string.liveing));
                number.setText("(" + livePlayingBean.getAllNumbers() + context.getString(R.string.unit_number) + ")");
                break;
            case "notice":
                title.setText(context.getString(R.string.live_advance));
                number.setText("(" + livePlayingBean.getAllNumbers() + context.getString(R.string.unit_number) + ")");
                break;
            default:
                title.setText(context.getString(R.string.live_replay));
                number.setText("(" + livePlayingBean.getAllNumbers() + context.getString(R.string.unit_number) + ")");
                break;
        }


        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        View view;
        final LivePlayingBean livePlayingBean = mData.get(groupPosition);


        if (childPosition + 1 == getChildrenCount(groupPosition))
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_search_live_group_bottom_item, null);
            TextView more = (TextView) view.findViewById(R.id.more);
            more.setVisibility(View.VISIBLE);
            more.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                SearchLiveListActivity.gotoActivity(context, livePlayingBean.getType(), keyword);

                }
            });

        } else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live, null);

            ImageView course_img = (ImageView) view.findViewById(R.id.course_img);
            TextView live_name_tv = (TextView) view.findViewById(R.id.live_name_tv);
            TextView host_name_tv = (TextView) view.findViewById(R.id.host_name_tv);
            ProgressBar liveProgress = (ProgressBar) view.findViewById(R.id.liveProgress);
            TextView liveTitle = (TextView) view.findViewById(R.id.liveTitle);
            LinearLayout live_sta_tv = (LinearLayout) view.findViewById(R.id.live_sta_tv);

            switch (livePlayingBean.getType())
            {
                case "live":

                    final LiveBean liveBean = livePlayingBean.getLiveCourseBean().get(childPosition);
                    GlideUtils.loadCourseImg(this.context, liveBean.getPicture(), course_img);


                    //搜索关键字变色
                    int index1;
                    ForegroundColorSpan span1 = new ForegroundColorSpan(context.getResources().getColor(R.color.font_blue));//要显示的颜色
                    SpannableStringBuilder builder1 = new SpannableStringBuilder(liveBean.getLive_title());
                    index1 = liveBean.getLive_title().indexOf(keyword);//从第几个匹配上
                    if(index1!=-1){//有这个关键字用builder显示
                        builder1.setSpan(span1, index1, index1+keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        live_name_tv.setText(builder1);
                    }else{//没有则直接显示
                        live_name_tv.setText(liveBean.getLive_title());
                    }

                    host_name_tv.setText(context.getString(R.string.lecturer) + liveBean.getUser_profile().getNickname());

                    long  start_time = Long.parseLong(liveBean.getStart_time());
                    long end_time = Long.parseLong(liveBean.getEnd_time());

                    live_sta_tv.setVisibility(View.VISIBLE);
                    liveProgress.setVisibility(View.VISIBLE);
                    if (android.os.Build.VERSION.SDK_INT > 22) {//android 6.0替换clip的加载动画
                        final Drawable drawable =  context.getResources().getDrawable(R.drawable.liveing_anim_60);
                        liveProgress.setIndeterminateDrawable(drawable);
                    }
                    liveTitle.setText("正在直播："+ DateUtil.getHourMin(start_time)+"--"+DateUtil.getHourMin(end_time));

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LiveSynopsisActivity.gotoActivity(context,liveBean);
                        }
                    });

                    break;
                case "notice":

                    final  LiveBean liveBean2 = livePlayingBean.getLiveCourseBean().get(childPosition);
                    GlideUtils.loadCourseImg(this.context, liveBean2.getPicture(), course_img);

                    //搜索关键字变色
                    int index2;
                    ForegroundColorSpan span2 = new ForegroundColorSpan(context.getResources().getColor(R.color.font_blue));//要显示的颜色
                    SpannableStringBuilder builder2 = new SpannableStringBuilder(liveBean2.getLive_title());
                    index2 = liveBean2.getLive_title().indexOf(keyword);//从第几个匹配上
                    if(index2!=-1){//有这个关键字用builder显示
                        builder2.setSpan(span2, index2, index2+keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        live_name_tv.setText(builder2);
                    }else{//没有则直接显示
                        live_name_tv.setText(liveBean2.getLive_title());
                    }

                    host_name_tv.setText(context.getString(R.string.lecturer) + liveBean2.getUser_profile().getNickname());

                    long  start_time2 = Long.parseLong(liveBean2.getStart_time());
                    long end_time2 = Long.parseLong(liveBean2.getEnd_time());

                    live_sta_tv.setVisibility(View.VISIBLE);
                    liveProgress.setVisibility(View.INVISIBLE);
                    liveTitle.setText(DateUtil.getHourMin(start_time2)+"--"+DateUtil.getHourMin(end_time2));

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LiveSynopsisActivity.gotoActivity(context,liveBean2);
                        }
                    });

                    break;
                default:

                    final LiveBean videoBean = livePlayingBean.getVideoBean().get(childPosition);
                    live_sta_tv.setVisibility(View.GONE);
                    GlideUtils.loadCourseImg(context, videoBean.getPicture(), course_img);

                    int index3;
                    ForegroundColorSpan span3 = new ForegroundColorSpan(context.getResources().getColor(R.color.font_blue));//要显示的颜色
                    SpannableStringBuilder builder3 = new SpannableStringBuilder(videoBean.getLive_title());
                    index3 = videoBean.getLive_title().indexOf(keyword);//从第几个匹配上
                    if(index3!=-1){//有这个关键字用builder显示
                        builder3.setSpan(span3, index3, index3+keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        live_name_tv.setText(builder3);
                    }else{//没有则直接显示
                        live_name_tv.setText(videoBean.getLive_title());
                    }

                    host_name_tv.setText(context.getString(R.string.lecturer) + videoBean.getUser_profile().getNickname());

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LiveSynopsisActivity.gotoActivity(context,videoBean);
                        }
                    });

                    break;
            }
        }

        return view;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

    public void  setKeyword(String keyword){
        this.keyword = keyword;
    }
}
