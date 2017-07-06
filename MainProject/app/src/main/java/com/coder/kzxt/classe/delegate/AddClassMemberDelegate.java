package com.coder.kzxt.classe.delegate;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.mInterface.OnAddClassMemberInterface;
import com.coder.kzxt.login.beans.UserInfoList;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;
import java.util.List;

/**
 * Created by wangtingshun on 2017/6/6.
 * 添加班级成员的delegate
 */

public class AddClassMemberDelegate extends PullRefreshDelegate <UserInfoList.UserBean>{


    private Context mContext;

    public AddClassMemberDelegate(Context context) {
        super(R.layout.add_class_member_item);
        this.mContext = context;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<UserInfoList.UserBean> data, int position) {
        ImageView memberIcon = holder.findViewById(R.id.class_icon);
        TextView memberName = holder.findViewById(R.id.tv_member_name);
        TextView memberNumber = holder.findViewById(R.id.tv_member_number);
        Button addButton = holder.findViewById(R.id.btn_add);

        final UserInfoList.UserBean classMember = data.get(position);
        GlideUtils.loadHeaderOfClass(mContext,classMember.getProfile().getAvatar(),memberIcon);
        memberName.setText(classMember.getProfile().getNickname());
        memberNumber.setText("账号"+classMember.getMobile());
        addButton.setVisibility(View.VISIBLE);

        if (classMember.isAdd()) {
            addButton.setEnabled(false);
            addButton.setText("已添加");
            addButton.setTextColor(mContext.getResources().getColor(R.color.font_gray));
            addButton.setBackgroundColor(mContext.getResources().getColor(R.color.bg_gray));
        } else {
            addButton.setEnabled(true);
            addButton.setText("添加");
            addButton.setTextColor(mContext.getResources().getColor(R.color.font_white));
            addButton.setBackgroundColor(mContext.getResources().getColor(R.color.first_theme));
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onAddClassMember(classMember);
                }
            }
        });
    }

    public OnAddClassMemberInterface listener;

    public void setOnAddClassMemberListener(OnAddClassMemberInterface onAddClassMemberListener){
        this.listener = onAddClassMemberListener;
    }

}
