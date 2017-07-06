package com.coder.kzxt.message.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.message.activity.ChatPersonActivity;
import com.coder.kzxt.message.beans.ManagerUserProfile;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;
import com.tencent.TIMUserProfile;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/16
 * <p>
 * 消息管理页面
 */

public class ManagerTeacherDelegate extends PullRefreshDelegate<TIMUserProfile>
{
    private Context context;
    private List<ManagerUserProfile> silenceSeconds;

    public ManagerTeacherDelegate(Context context, List<ManagerUserProfile> silenceSeconds)
    {
        super(R.layout.item_chat_manager);
        this.context = context;
        this.silenceSeconds = silenceSeconds;
    }

    @Override
    public void initCustomView(BaseViewHolder view, List<TIMUserProfile> data, int position)
    {
        ImageView mImageView = (ImageView) view.findViewById(R.id.imageView);
        TextView mName = (TextView) view.findViewById(R.id.name);
        TextView role = (TextView) view.findViewById(R.id.role);
        TextView phone = (TextView) view.findViewById(R.id.phone);
        ImageView no_disturb = (ImageView) view.findViewById(R.id.no_disturb);

        final TIMUserProfile memberInfo = data.get(position);

        switch (silenceSeconds.get(position).getRoleType())
        {
            case Owner:
                role.setVisibility(View.VISIBLE);
                role.setText(context.getResources().getString(R.string.creator));
                break;
            case Admin:
                role.setVisibility(View.VISIBLE);
                role.setText(context.getResources().getString(R.string.teacher));
                break;
            case Normal:
                role.setVisibility(View.GONE);
                role.setText("");
                break;
            case NotMember:
                role.setVisibility(View.GONE);
                role.setText("");
                break;
            default:
                break;
        }
//        TIMGroupManager.getInstance().getGroupMembersInfo();

//        FriendProfile friendProfile = FriendshipInfo.getInstance().getProfile(memberInfo.getUser());
//        if (friendProfile == null)
//        {
//
//            phone.setText("");
//            mName.setText("");
//
//        } else
//        {
        GlideUtils.loadCircleHeaderOfCommon(context, memberInfo.getFaceUrl(), mImageView);
        phone.setText(memberInfo.getRemark());
        mName.setText(memberInfo.getNickName());

        if (silenceSeconds.get(position).getShutUp())
        {
            no_disturb.setVisibility(View.VISIBLE);
        } else
        {
            no_disturb.setVisibility(View.GONE);
        }

        view.getConvertView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChatPersonActivity.gotoActivity(context, memberInfo.getIdentifier());
            }
        });
//        }
    }

}
