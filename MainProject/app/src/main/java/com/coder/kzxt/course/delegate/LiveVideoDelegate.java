//package com.coder.kzxt.course.delegate;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.coder.kzxt.activity.R;
//import com.coder.kzxt.base.beans.VideoBean;
//import com.coder.kzxt.course.activity.LiveSynopsisActivity;
//import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
//import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
//import com.coder.kzxt.utils.GlideUtils;
//
//import java.util.List;
//
///**
// * Created by MaShiZhao on 2017/4/13
// */
//
//public class LiveVideoDelegate extends PullRefreshDelegate<VideoBean>
//{
//    private Context mContext;
//    private long systomTime;
//
//    public LiveVideoDelegate(Context context)
//    {
//        super(R.layout.item_live);
//        this.mContext = context;
//    }
//
//    @Override
//    public void initCustomView(BaseViewHolder view, List<VideoBean> data, int position)
//    {
//
//
//        LinearLayout all_ly = (LinearLayout) view.findViewById(R.id.all_ly);
//        ImageView course_img = (ImageView) view.findViewById(R.id.course_img);
//        TextView live_name_tv = (TextView) view.findViewById(R.id.live_name_tv);
//        TextView host_name_tv = (TextView) view.findViewById(R.id.host_name_tv);
//        LinearLayout live_sta_tv = view.findViewById(R.id.live_sta_tv);
//        final VideoBean videoBean = data.get(position);
//        live_sta_tv.setVisibility(View.GONE);
//        GlideUtils.loadCourseImg(this.mContext, videoBean.getVideo_detail().getFirst_image(), course_img);
//        live_name_tv.setText(videoBean.getTitle());
//        host_name_tv.setText(mContext.getString(R.string.lecturer) + videoBean.getUser_profile().getNickname());
//
//        view.getConvertView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LiveSynopsisActivity.gotoActivity(mContext,videoBean);
//            }
//        });
//
//    }
//
//    public void setSystomTime(long systomTime)
//    {
//        this.systomTime = systomTime;
//    }
//}
