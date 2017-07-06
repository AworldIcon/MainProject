package com.coder.kzxt.stuwork.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.activity.CheckStuWorkActivity;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.stuwork.activity.StuCourseWorkActivity;
import com.coder.kzxt.stuwork.activity.StuUnCheckWorkActivity;
import com.coder.kzxt.stuwork.activity.TestPageActivity;
import com.coder.kzxt.stuwork.entity.StuCourseWorkBean;
import com.coder.kzxt.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pc on 2017/4/6.
 */

public class StuCourseWorkDelegate extends PullRefreshDelegate<StuCourseWorkBean.ItemsBean> {
    private Context context;
    private int workType;
    public StuCourseWorkDelegate(Context context,int workType) {
        super(R.layout.stu_work_item);
        this.context=context;
        this.workType=workType;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, final List<StuCourseWorkBean.ItemsBean> data, final int position) {
        TextView textTile= holder. findViewById(R.id.work_name);
        TextView work_num= holder. findViewById(R.id.work_num);
        TextView work_score= holder. findViewById(R.id.work_score);
        TextView time_work= holder. findViewById(R.id.time_work);
        TextView status= holder. findViewById(R.id.status);
        TextView my_score=holder.findViewById(R.id.my_score);
        TextView get_score=holder.findViewById(R.id.get_score);
        my_score.setVisibility(View.GONE);
        get_score.setVisibility(View.GONE);
        Long start = data.get(position).getStart_time();
        Long end = data.get(position).getEnd_time();
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd");
        String start_time = sdr.format(new Date(start * 1000L));
        String end_time = sdr.format(new Date(end * 1000L));
        time_work.setText("有效期至: "+start_time+"—"+end_time);
        textTile.setText(data.get(position).getTitle());
        work_score.setText(data.get(position).getTest_paper().getScore());
        work_num.setText(data.get(position).getTest_paper().getQuestion_count()+"");
        if(data.get(position).getStatus()==1){
            status.setText(data.get(position).getStatus_text());
            status.setTextColor(context.getResources().getColor(R.color.font_gray));
        }
        if(data.get(position).getStatus()==2){
            if(data.get(position).getTest_result().getStatus()==1&&data.get(position).getTest_result().getStart_time()>0){
                status.setText("继续做");
                status.setTextColor(context.getResources().getColor(R.color.font_red));
            }else if(data.get(position).getTest_result().getStatus()==1&&data.get(position).getTest_result().getStart_time()==0){
                status.setText("待做");
                status.setTextColor(context.getResources().getColor(R.color.font_red));
            }else {
                if(data.get(position).getTest_result().getStatus()==3){//已完成
                    my_score.setVisibility(View.VISIBLE);
                    get_score.setVisibility(View.VISIBLE);
                    get_score.setText(data.get(position).getTest_result().getScore());
                    status.setText(data.get(position).getTest_result().getStatus_text());
                    status.setTextColor(context.getResources().getColor(R.color.font_green));
                }else {//待批阅
                    status.setText(data.get(position).getTest_result().getStatus_text());
                    status.setTextColor(context.getResources().getColor(R.color.font_green));
                }


            }
        }
        if(data.get(position).getStatus()==3){
            status.setText(data.get(position).getStatus_text());
            status.setTextColor(context.getResources().getColor(R.color.font_gray));
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.get(position).getStatus()==1){
                    ToastUtils.makeText(context,"答题暂未开始");
                    return;
                }
                if(data.get(position).getStatus()==2&&data.get(position).getTest_result().getStatus()==1){//待做，继续做
                    final Intent intent = new Intent(context, TestPageActivity.class);
                    intent.putExtra("test_paper_id",data.get(position).getTest_paper_id()+"");
                    intent.putExtra("test_result_id", data.get(position).getTest_result().getId()+"");
                    intent.putExtra("limit_time",data.get(position).getLimit_time());
                    intent.putExtra("showType", TestPageActivity.TEST_PAGE_STATUS_TEST);
                    intent.putExtra("workType", workType);
                    if(workType==1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.custom_dialog);
                        builder.setMessage("考试开始后不能中断，请确保当前网络畅通。");
                        builder.setTitle("提示");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ((StuCourseWorkActivity)context).startActivityForResult(intent,1);
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }});
                        builder.create().show();
                    }else {
                        ((StuCourseWorkActivity)context).startActivityForResult(intent,1);
                    }
                }else if(data.get(position).getStatus()==3){//作业/考试 已经过期，可查看作业答案以及内容，即使学生作业属于完成状态也先按照过期的处理
                    Intent intent=new Intent(context,StuUnCheckWorkActivity.class);
                    intent.putExtra("nickname",data.get(position).getTitle());
                    intent.putExtra("resultId",data.get(position).getTest_result().getId()+"");
                    intent.putExtra("testid",data.get(position).getTest_paper_id()+"");
                    intent.putExtra("workType",workType);
                    intent.putExtra("status",3);
                    context.startActivity(intent);
                }else if(data.get(position).getStatus()==2&&data.get(position).getTest_result().getStatus()==2){//作业/考试 进行中，待批阅状态，可查看作业我的答案以及内容
                    Intent intent=new Intent(context,CheckStuWorkActivity.class);
                    intent.putExtra("nickname",data.get(position).getTitle());
                    intent.putExtra("resultId",data.get(position).getTest_result().getId()+"");
                    intent.putExtra("workType",workType);
                    intent.putExtra("status","3");
                    intent.putExtra("stu",2);//代表学生查看批阅
                    context.startActivity(intent);
                }else if(data.get(position).getStatus()==2&&data.get(position).getTest_result().getStatus()==3){//已完成
                    Intent intent=new Intent(context,CheckStuWorkActivity.class);
                    intent.putExtra("nickname",data.get(position).getTitle());
                    intent.putExtra("resultId",data.get(position).getTest_result().getId()+"");
                    intent.putExtra("workType",workType);
                    intent.putExtra("status","3");
                    intent.putExtra("stu",1);//代表学生完成
                    context.startActivity(intent);
                }

            }
        });
    }
}
