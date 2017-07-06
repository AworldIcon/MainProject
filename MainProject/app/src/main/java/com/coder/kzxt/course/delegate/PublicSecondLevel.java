package com.coder.kzxt.course.delegate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.beans.TeachBean;
import com.coder.kzxt.recyclerview.viewholder.TreeItem;
import com.coder.kzxt.recyclerview.viewholder.TreeParentItem;
import com.coder.kzxt.recyclerview.viewholder.ViewHolder;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.List;

import static com.coder.kzxt.utils.PublicUtils.IsEmpty;

/**
 * 发布第二级
 * Created by wangtingshun on 2017/4/26.
 */

public class PublicSecondLevel extends TreeParentItem <TeachBean.TeachCourseItem> implements TreeItem.onClickChangeListener{

    private Context mContext;

    public PublicSecondLevel(TeachBean.TeachCourseItem data, TreeParentItem parentItem) {
        super(data, parentItem);
    }


    @Override
    protected List<TreeItem> initChildsList(TeachBean.TeachCourseItem data) {

        return null;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.order_course_top_layout;
    }

    @Override
    public void onBindViewHolder(Context context, ViewHolder holder) {
        this.mContext = context;
        holder.setText(R.id.tv_course_name,data.getTitle());
        holder.setText(R.id.tv_course_time,data.getLesson_num());
        LinearLayout memberLayout = (LinearLayout) holder.getConvertView().findViewById(R.id.ll_member);
        memberLayout.setVisibility(View.VISIBLE);
        holder.setText(R.id.tv_num,data.getStudent_num()+" 人");

        ImageView courseImg = (ImageView) holder.getConvertView().findViewById(R.id.iv_course);
        ImageView freeOrVipImage = (ImageView) holder.getConvertView().findViewById(R.id.free_or_vip);
        GlideUtils.loadCourseImg(context,data.getMiddle_pic(),courseImg);
        if (IsEmpty(data.getPrice())) {
            freeOrVipImage.setBackgroundResource(R.drawable.live_free_iv);
        } else {
            freeOrVipImage.setBackgroundResource(R.drawable.live_vip_iv);
        }
        holder.getConvertView().findViewById(R.id.tv_course_price).setVisibility(View.GONE);
    }

    @Override
    public void onClickChange(TreeItem treeItem) {
        super.onClickChange(treeItem);
        PublicSecondLevel secondChild = (PublicSecondLevel) treeItem;
        TeachBean.TeachCourseItem courseItem = secondChild.getData();
        if (mContext != null && courseItem != null) {
            Intent intent = new Intent(mContext, VideoViewActivity.class);
            intent.putExtra("flag", Constants.ONLINE);
            intent.putExtra("treeid", courseItem.getId());
            intent.putExtra("tree_name", courseItem.getTitle());
            intent.putExtra("pic", courseItem.getMiddle_pic());
            mContext.startActivity(intent);
        }
    }
}
