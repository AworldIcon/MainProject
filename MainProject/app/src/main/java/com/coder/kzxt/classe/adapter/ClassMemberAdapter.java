package com.coder.kzxt.classe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.beans.ClassMemberBean;
import com.coder.kzxt.utils.GlideUtils;

import java.util.ArrayList;

/**
 * 班级成员
 * Created by wangtingshun on 2017/3/14.
 */

public class ClassMemberAdapter extends BaseAdapter{


    private Context mContext;
    private ArrayList<ClassMemberBean.ClassMember> memberList;

    public ClassMemberAdapter(Context context,ArrayList<ClassMemberBean.ClassMember> list) {
        this.mContext = context;
        this.memberList = list;
    }


    @Override
    public int getCount() {
        if (memberList.size() >= 5) {
            return 5;
        }
        return memberList.size();
    }

    @Override
    public Object getItem(int i) {
        return memberList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.class_member_head_layout, null);
        }
        ImageView headImage = (ImageView) convertView.findViewById(R.id.menumber_head_img);
        RelativeLayout memberLayout = (RelativeLayout) convertView.findViewById(R.id.class_member_layout);
        final ClassMemberBean.ClassMember memberBean = memberList.get(position);
        ClassMemberBean.ClassMember.Profile profile = memberBean.getProfile();
        if (profile != null) {
            GlideUtils.loadCircleHeaderOfCommon(mContext, profile.getAvatar(), headImage);
        }
        if (onItemClickListener != null) {
            memberLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnHeadItemClick(memberBean);
                }
            });
        }
        return convertView;
    }

    private OnItemClickInterface onItemClickListener;

    public interface OnItemClickInterface{
        void OnHeadItemClick(ClassMemberBean.ClassMember memberBean);
    }

    public void setOnItemClickListener(OnItemClickInterface listener){
        this.onItemClickListener = listener;
    }
}
