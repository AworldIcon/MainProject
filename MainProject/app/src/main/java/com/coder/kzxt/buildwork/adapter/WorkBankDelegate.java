package com.coder.kzxt.buildwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.activity.PublishWorkTimeActivity;
import com.coder.kzxt.buildwork.activity.TeaWorkRecourseActivity;
import com.coder.kzxt.buildwork.entity.AlreadyPublishBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pc on 2017/3/21.
 */

public class WorkBankDelegate extends PullRefreshDelegate<AlreadyPublishBean.ItemsBean> implements View.OnClickListener, View.OnLongClickListener {
    private Context context;
    private int type,courseId;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;
    private MyPullRecyclerView mRecyclerView;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }
    public WorkBankDelegate(Context context,MyPullRecyclerView mRecyclerView,int type,int courseId) {
        super(R.layout.work_manage_item);
        this.context=context;
        this.type=type;
        this.courseId=courseId;
        this.mRecyclerView=mRecyclerView;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, final List<AlreadyPublishBean.ItemsBean> data, final int position) {
        TextView textTile;
        TextView create_time;
        TextView work_num;
        TextView score;
        TextView classNames;
        TextView build_self;
        TextView button;
        TextView creator;
        textTile= (TextView) holder. findViewById(R.id.work_title);
        create_time= (TextView) holder. findViewById(R.id.create_time);
        work_num= (TextView) holder. findViewById(R.id.work_num);
        score= (TextView) holder. findViewById(R.id.score);
        classNames= (TextView) holder. findViewById(R.id.class_names);
        button= (TextView) holder. findViewById(R.id.button);
        build_self= (TextView) holder. findViewById(R.id.build_self);
        creator= (TextView) holder. findViewById(R.id.creator);
        creator.setVisibility(View.VISIBLE);
        RelativeLayout class_layout=holder.findViewById(R.id.class_layout);
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd");
        Long i = data.get(position).getCreate_time();
        String times = sdr.format(new Date(i * 1000L));
        textTile.setText(data.get(position).getTitle());
        work_num.setText(data.get(position).getQuestion_count()+"");
        score.setText(data.get(position).getScore()+"");
        create_time.setText("创建时间: "+times);
        creator.setText("创建者: "+data.get(position).getCreator().getNickname());
        //隐藏发布班级以及发布状态
            class_layout.setVisibility(View.GONE);
        //显示发布按钮，以及自建标识
            button.setVisibility(View.VISIBLE);

        if(String.valueOf(data.get(position).getUser_id()).equals(new SharedPreferencesUtil(context).getUid())){
            build_self.setVisibility(View.VISIBLE);
        }else {
            build_self.setVisibility(View.GONE);
        }
        holder.getConvertView().setOnLongClickListener(this);
        holder.getConvertView().setOnClickListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.get(position).getQuestion_count()==0){
                    ToastUtils.makeText(context,"试卷不能为空");
                    return;
                }
                Intent intent=new Intent(context,PublishWorkTimeActivity.class);
                intent.putExtra("testId",data.get(position).getId()+"");
                intent.putExtra("title",data.get(position).getTitle());
                intent.putExtra("workType",type);
                intent.putExtra("courseId",courseId+"");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mRecyclerView != null) {
            // 通过传递点击的View计算出点击的位置
            int position = mRecyclerView.getChildAdapterPosition(v);
            if (listener != null) {
                listener.onItemClick(position);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mRecyclerView != null) {
            // 通过传递点击的View计算出点击的位置
            int position = mRecyclerView.getChildAdapterPosition(v);
            if (longClickListener != null) {
                longClickListener.onItemLongClick(position);
            }
        }
        return true;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }
}
