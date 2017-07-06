package com.coder.kzxt.main.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.course.activity.LiveCourseActivity;
import com.coder.kzxt.course.activity.LiveSynopsisActivity;
import com.coder.kzxt.main.beans.LivePlayingBean;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

import static com.coder.kzxt.activity.R.id.live_name_tv1;

/**
 * Created by MaShiZhao on 2017/4/13
 */


public class LivePlayingAdapter extends BaseExpandableListAdapter
{

    private List<LivePlayingBean> mData;
    private Context context;
    private long sysTime;

    public LivePlayingAdapter(Context context, List<LivePlayingBean> v)
    {
        this.context = context;
        mData = v;
        sysTime =0;
    }

    public void setSysTime(long sysTime)
    {
        this.sysTime = sysTime;
    }

    @Override
    public int getGroupCount()
    {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        LivePlayingBean livePlayingBean = mData.get(groupPosition);

        if (livePlayingBean.getType().equals("review")) {
            if(livePlayingBean.getVideoBean().size()==1){
                return livePlayingBean.getVideoBean().size();
            }else {
                return livePlayingBean.getVideoBean().size() / 2;
            }
        } else if (livePlayingBean.getType().equals("notice")) {
            return livePlayingBean.getLiveCourseBean().size();
        } else {
            return livePlayingBean.getLiveCourseBean().size();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_live_group_item, null);

        final TextView title = (TextView) view.findViewById(R.id.title);
        final TextView more = (TextView) view.findViewById(R.id.more);
        RelativeLayout zong_ly = (RelativeLayout) view.findViewById(R.id.zong_ly);

        final LivePlayingBean livePlayingBean = mData.get(groupPosition);
        switch (livePlayingBean.getType())
        {
            case "live":
                title.setText(context.getString(R.string.liveing2));
                more.setVisibility(View.GONE);
                zong_ly.setVisibility(View.GONE);
                break;
            case "notice":
                title.setText(context.getString(R.string.live_advance));
                zong_ly.setVisibility(View.VISIBLE);
                break;
            case "review":
                title.setText(context.getString(R.string.live_replay));
                zong_ly.setVisibility(View.VISIBLE);
                break;
        }

        more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (livePlayingBean.getType())
                {
                    case "live":
                        LiveCourseActivity.gotoActivity(context, 0);
                        break;
                    case "notice":
                        LiveCourseActivity.gotoActivity(context, 1);
                        break;
                    case "review":
                        LiveCourseActivity.gotoActivity(context, 2);
                        break;
                }
            }
        });


        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        View view = null;
        LivePlayingBean livePlayingBean = mData.get(groupPosition);
        switch (livePlayingBean.getType())
        {
            case "live":
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_live_child_item, null);

                ImageView live_image = (ImageView) view.findViewById(R.id.live_image);
                ImageView host_head_iv = (ImageView) view.findViewById(R.id.host_head_iv);
                TextView live_num = (TextView) view.findViewById(R.id.live_num);
                TextView live_name_tv_1 = (TextView) view.findViewById(R.id.live_name_tv);
                TextView host_name_tv_1 = (TextView) view.findViewById(R.id.host_name_tv);
                View line_v = view.findViewById(R.id.line_v);

                if(!isLastChild){
                    line_v.setVisibility(View.VISIBLE);
                }else {
                    line_v.setVisibility(View.GONE);
                }

                final LiveBean liveBean0 = livePlayingBean.getLiveCourseBean().get(childPosition);
                GlideUtils.loadCourseImg(context, liveBean0.getPicture(), live_image);
                GlideUtils.loadHeaderOfTeacher(this.context, liveBean0.getUser_profile().getAvatar(), host_head_iv);
                //        live_num.setText(liveBean.get);
                live_name_tv_1.setText(liveBean0.getLive_title());
                host_name_tv_1.setText(liveBean0.getUser_profile().getNickname());
                live_num.setText(liveBean0.getStudy_num() + "人观看");
                view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        LiveSynopsisActivity.gotoActivity(context, liveBean0);
                    }
                });

                break;
            case "notice":
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_live_child2_item, null);

                LinearLayout all_ly = (LinearLayout) view.findViewById(R.id.all_ly);
                ImageView course_img = (ImageView) view.findViewById(R.id.course_img);
                TextView live_name_tv = (TextView) view.findViewById(R.id.live_name_tv);
                TextView host_name_tv = (TextView) view.findViewById(R.id.host_name_tv);
                TextView time_tv = (TextView) view.findViewById(R.id.time_tv);

                final LiveBean liveBean = livePlayingBean.getLiveCourseBean().get(childPosition);

                GlideUtils.loadCourseImg(this.context, liveBean.getPicture(), course_img);
                live_name_tv.setText(liveBean.getLive_title());
                host_name_tv.setText(context.getString(R.string.lecturer) + liveBean.getUser_profile().getNickname());

                  long between = Long.parseLong(liveBean.getStart_time())-sysTime;
                  long day = between / (24 * 3600);
                  long hour = between % (24 * 3600) / 3600;
                  long minute = between % 3600 / 60;
                  long second = between % 60 ;

                 if(second<0){
                     time_tv.setText(context.getResources().getString(R.string.live_will_begin));
                 }else {
                     time_tv.setText((fomatData(day)+"天"+fomatData(hour)+"时"+fomatData(minute)+"分"+fomatData(second)+"秒"));
                     SpannableStringBuilder builder = new SpannableStringBuilder(time_tv.getText().toString());
                     ForegroundColorSpan redSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.font_red));
                     ForegroundColorSpan redSpan1 = new ForegroundColorSpan(context.getResources().getColor(R.color.font_red));
                     ForegroundColorSpan redSpan2 = new ForegroundColorSpan(context.getResources().getColor(R.color.font_red));
                     ForegroundColorSpan redSpan3 = new ForegroundColorSpan(context.getResources().getColor(R.color.font_red));
//                 00天00时00分00秒
                     builder.setSpan(redSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                     builder.setSpan(redSpan1, 3, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                     builder.setSpan(redSpan2, 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                     builder.setSpan(redSpan3, 9, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                     time_tv.setText(builder);
                 }

                view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        LiveSynopsisActivity.gotoActivity(context, liveBean);
                    }
                });

                break;
            case "review":

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_live_child3_item, null);
                ImageView course_img_reivew = (ImageView) view.findViewById(R.id.course_img);
                TextView live_name_tv_reivew = (TextView) view.findViewById(R.id.live_name_tv);
                TextView host_name_tv_reivew = (TextView) view.findViewById(R.id.host_name_tv);
                LinearLayout all_ly__reivew2 = (LinearLayout) view.findViewById(R.id.all_ly1);
                LinearLayout all_ly__reivew1 = (LinearLayout) view.findViewById(R.id.all_ly);
                ImageView course_img_reivew2 = (ImageView) view.findViewById(R.id.course_img1);
                TextView live_name_tv_reivew2 = (TextView) view.findViewById(live_name_tv1);
                TextView host_name_tv_reivew2 = (TextView) view.findViewById(R.id.host_name_tv1);

                final LiveBean videoBean = livePlayingBean.getVideoBean().get(0);

                GlideUtils.loadCourseImg(this.context, videoBean.getPicture(), course_img_reivew);
                live_name_tv_reivew.setText(videoBean.getLive_title());
                host_name_tv_reivew.setText(context.getString(R.string.lecturer) + videoBean.getUser_profile().getNickname());

                if (livePlayingBean.getVideoBean().size() == 1)
                {
                    all_ly__reivew2.setVisibility(View.GONE);
                } else
                {
                    all_ly__reivew2.setVisibility(View.VISIBLE);
                    final LiveBean videoBean1 = livePlayingBean.getVideoBean().get(1);
                    GlideUtils.loadCourseImg(this.context, videoBean1.getPicture(), course_img_reivew2);
                    live_name_tv_reivew2.setText(videoBean1.getLive_title());
                    host_name_tv_reivew2.setText(context.getString(R.string.lecturer) + videoBean1.getUser_profile().getNickname());

                    all_ly__reivew2.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            LiveSynopsisActivity.gotoActivity(context, videoBean1);
                        }
                    });

                }
                all_ly__reivew1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        LiveSynopsisActivity.gotoActivity(context, videoBean);
                    }
                });


                break;
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

    public void tickTime()
    {
        for (LivePlayingBean livePlayingBean : mData)
        {
            if (livePlayingBean.getType().equals("notice"))
            {
                for (LiveBean liveBean : livePlayingBean.getLiveCourseBean())
                {
                    liveBean.setStart_time((Long.valueOf(liveBean.getStart_time()) - 1) + "");
                    notifyDataSetChanged();
                }
                return;
            }
        }

    }

    private String fomatData(long l){
        return String.format("%02d",l);
    }
}
