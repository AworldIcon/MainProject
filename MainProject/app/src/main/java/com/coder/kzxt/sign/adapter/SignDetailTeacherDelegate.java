package com.coder.kzxt.sign.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.sign.activity.MarkStudentActivity;
import com.coder.kzxt.sign.activity.SignDetailListTeacherActivity;
import com.coder.kzxt.sign.beans.SignInfoResult;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/16
 * <p>
 * 签到详情列表
 *
 */

public class SignDetailTeacherDelegate extends PullRefreshDelegate<SignInfoResult.SignInfoBean>
{
    private Context context;
    private String signId;

    public SignDetailTeacherDelegate(Context context, String signId)
    {
        super(R.layout.item_sign_detail);
        this.context = context;
        this.signId = signId;
    }

    @Override
    public void initCustomView(BaseViewHolder view, List<SignInfoResult.SignInfoBean> data, int position)
    {

        ImageView head = (ImageView) view.findViewById(R.id.head);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView mark = (TextView) view.findViewById(R.id.mark);
        TextView status = (TextView) view.findViewById(R.id.status);
        TextView status_mark = (TextView) view.findViewById(R.id.status_mark);
        TextView time = (TextView) view.findViewById(R.id.time);
        MyRecyclerView myRecyclerView = (com.coder.kzxt.recyclerview.MyRecyclerView) view.findViewById(R.id.myRecyclerView);

        final SignInfoResult.SignInfoBean signInfoBean = data.get(position);
        GlideUtils.loadCircleHeaderOfCommon(context, signInfoBean.getUser().getAvatar(), head);
        name.setText(signInfoBean.getUser().getNickname());
        time.setText(DateUtil.getDateAndSecond(signInfoBean.getTime()));

        if (signInfoBean.getStatus() == 0)
        {
            status.setText(context.getString(R.string.signed_not));
        } else
        {
            status.setText(context.getString(R.string.signed));
        }

        if (signInfoBean.getIsRecord())
        {
            status.setTextColor(ContextCompat.getColor(context, R.color.first_theme));
        } else
        {
            status.setTextColor(ContextCompat.getColor(context, R.color.font_black));
        }

        if (signInfoBean.getRecord().getUser_id() != null)
        {
            time.setTextColor(ContextCompat.getColor(context, R.color.first_theme));
        } else
        {
            time.setTextColor(ContextCompat.getColor(context, R.color.font_gray));
        }

        if (signInfoBean.getIsRecord())
        {
            status_mark.setText("");
        } else if (signInfoBean.getSign().getStatus() == 0)
        {
            status_mark.setText("");
        } else
        {
            if (signInfoBean.getSign().getIs_range() == 0)
            {
                status_mark.setText(context.getString(R.string.not_in_scope));
            } else if (signInfoBean.getSign().getIs_offline() == 1)
            {
                status_mark.setText(context.getString(R.string.offline));
            }
        }

        if (signInfoBean.getRecord().getTags().size() == 0)
        {
            myRecyclerView.setVisibility(View.GONE);
        } else
        {
            myRecyclerView.setVisibility(View.VISIBLE);
            myRecyclerView.setAdapter(new BaseRecyclerAdapter(context, signInfoBean.getRecord().getTags(), new TagDelegate()));
        }

        mark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MarkStudentActivity.gotoActivity((SignDetailListTeacherActivity) context, signInfoBean, signId);
            }
        });

    }


}
