package com.coder.kzxt.buildwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.activity.CheckWorkCalssActivity;
import com.coder.kzxt.buildwork.entity.AlreadyPublishBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zw on 2017/3/18.
 */

public class QuestionResourseDelegate extends PullRefreshDelegate<AlreadyPublishBean.ItemsBean>implements View.OnClickListener, View.OnLongClickListener {
    private Context context;
    private int courseId,workType;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;
    private MyPullRecyclerView mRecyclerView;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }
    public QuestionResourseDelegate(Context context,MyPullRecyclerView mRecyclerView,int courseId,int workType) {
        super(R.layout.work_manage_item);
        this.context=context;
        this.courseId=courseId;
        this.mRecyclerView=mRecyclerView;
        this.workType=workType;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, final List<AlreadyPublishBean.ItemsBean> data, final int position) {
        TextView textTile;
        TextView create_time;
        TextView work_num;
        TextView score;
        TextView classNames;
        TextView button;
        textTile= (TextView) holder. findViewById(R.id.work_title);
        create_time= (TextView) holder. findViewById(R.id.create_time);
        work_num= (TextView) holder. findViewById(R.id.work_num);
        score= (TextView) holder. findViewById(R.id.score);
        classNames= (TextView) holder. findViewById(R.id.class_names);
        button= (TextView) holder. findViewById(R.id.button);
        RelativeLayout class_layout=holder.findViewById(R.id.class_layout);
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd");
        long lcc = Long.valueOf(data.get(position).getCreate_time());
        Long i = data.get(position).getCreate_time();
        String times = sdr.format(new Date(i * 1000L));
        final String release=String.valueOf(data.get(position).getStatus());
         textTile.setText(data.get(position).getTitle());
         work_num.setText(data.get(position).getQuestion_count()+"");
         score.setText(data.get(position).getScore()+"");
        String names="";
        for(int c=0;c<data.get(position).getTest().size();c++){
            if(c==data.get(position).getTest().size()-1){
                names+=data.get(position).getTest().get(c).getClass_name();
            }else {
                names+=data.get(position).getTest().get(c).getClass_name()+"、";
            }
        }

        classNames.setText("发布班级: "+names);

             create_time.setVisibility(View.GONE);
            class_layout.setVisibility(View.VISIBLE);
             button.setVisibility(View.GONE);

        holder.getConvertView().setOnLongClickListener(this);
        holder.getConvertView().setOnClickListener(this);
        if(data.get(position).getTest().size()==0){
            class_layout.setVisibility(View.GONE);
        }
        class_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.get(position).getTest()!=null&&data.get(position).getTest().size()>0){
                    String testid=data.get(position).getId()+"";
                    String name=data.get(position).getTitle();
                    Intent intent=new Intent(context,CheckWorkCalssActivity.class);
                    intent.putExtra("testId",testid);
                    intent.putExtra("title",name);
                    intent.putExtra("course_id",courseId+"");
                    intent.putExtra("workType",workType);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mRecyclerView != null) {
            // 通过传递点击的View计算出点击的位置
            int position = mRecyclerView.getChildAdapterPosition(v);
            Log.d("zw--clc",position+"**");
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
            Log.d("zw--clc",position+"**");

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
