package com.coder.kzxt.classe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.beans.ClassApplyResult;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import java.util.ArrayList;

/**
 * 学生申请适配器
 * Created by wangtingshun on 2017/3/18.
 */

public class StudentApplyAdapter extends RecyclerView.Adapter<StudentApplyAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ClassApplyResult.ClassApplyBean> classApplys;

    public StudentApplyAdapter(Context context,ArrayList<ClassApplyResult.ClassApplyBean> applyBeens) {
        this.mContext = context;
        this.classApplys = applyBeens;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.apply_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final ClassApplyResult.ClassApplyBean applyBean = classApplys.get(position);
        String applyTime = applyBean.getApplyTime();
        String userFace = applyBean.getUserFace();  //申请人头像
        final String userName = applyBean.getUserName(); //申请人
        final String userGender = applyBean.getUserGender();

        holder.rl_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(applyBean,position);
                }
            }
        });

        GlideUtils.loadCircleHeaderOfCommon(mContext, userFace, holder.iv_head);
        holder.tv_user_name.setText(mContext.getResources().getString(R.string.apply_people) + userName);

        if (userGender != null && userGender.equals("male")) {
            holder.iv_sex.setImageDrawable(mContext.getResources().getDrawable(R.drawable.user_male));
        } else {
            holder.iv_sex.setImageDrawable(mContext.getResources().getDrawable(R.drawable.user_female));
        }

        holder.tv_aduit_time.setText(mContext.getResources().getString(R.string.apply_time) + DateUtil.getDateStr(Long.valueOf(applyTime)));
        // 拒绝
        holder.tv_refused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClickRefusedButton(applyBean);
                }
            }
        });

        //同意
        holder.tv_button_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onClickAgreeButton(applyBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return classApplys.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout rl_head;
        private ImageView iv_head;
        private TextView tv_user_name;
        private ImageView iv_sex;
        private TextView tv_aduit_time;
        private TextView tv_refused;  //拒绝
        private TextView tv_button_agree;  //同意

        public MyViewHolder(View itemView) {
            super(itemView);
            rl_head = (RelativeLayout) itemView.findViewById(R.id.rl_head);
            iv_head = (ImageView) itemView.findViewById(R.id.iv_head);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            iv_sex = (ImageView) itemView.findViewById(R.id.iv_sex);
            tv_aduit_time = (TextView) itemView.findViewById(R.id.tv_aduit_time);
            tv_refused = (TextView) itemView.findViewById(R.id.tv_refused);
            tv_button_agree = (TextView) itemView.findViewById(R.id.tv_button_agree);
        }
    }

    private OnItemClickCallBack onItemClickListener;

    public void setOnItemClickListener(OnItemClickCallBack listener){
        this.onItemClickListener = listener;
    }

    public interface OnItemClickCallBack{

        void onItemClick(ClassApplyResult.ClassApplyBean applyBean,int position);

        void onClickAgreeButton(ClassApplyResult.ClassApplyBean applyBean);

        void onClickRefusedButton(ClassApplyResult.ClassApplyBean applyBean);
    }
}
