package com.coder.kzxt.message.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.entity.CheckWorkCalssBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/16
 * <p>
 * 功能列表 作业 考试 签到 以后会有新的添加
 */

public class FunctionDelegate extends PullRefreshDelegate<CheckWorkCalssBean.ItemsBean>
{
    private Context context;
    // 作业 考试 签到
    private int type;
    // 用户角色
    private int userType;

    public FunctionDelegate(Context context, int type, int userType)
    {
        super(R.layout.item_chat_function);
        this.context = context;
        this.type = type;
        this.userType = type;
    }

    @Override
    public void initCustomView(BaseViewHolder view, List<CheckWorkCalssBean.ItemsBean> data, int position)
    {
        CheckWorkCalssBean.ItemsBean bean = data.get(position);
        TextView mTypeName = (TextView) view.findViewById(R.id.typeName);
        TextView status = (TextView) view.findViewById(R.id.status);
        TextView mTime = (TextView) view.findViewById(R.id.time);
        ImageView mImageView = (ImageView) view.findViewById(R.id.imageView);
        TextView mName = (TextView) view.findViewById(R.id.name);
        TextView mBottomTextView = (TextView) view.findViewById(R.id.bottomTextView);
        TextView mBottomTextView2 = (TextView) view.findViewById(R.id.bottomTextView2);
        TextView mBottomTextView3 = (TextView) view.findViewById(R.id.bottomTextView3);


        switch (type)
        {
            case 1:

                mTypeName.setText("[考试]");
                mImageView.setBackgroundResource(R.drawable.default_test);
                break;
            case 2:

                mTypeName.setText("[作业]");
                mImageView.setBackgroundResource(R.drawable.default_work);
                break;

            case 3:

                mTypeName.setText("[签到]");
                mImageView.setBackgroundResource(R.drawable.default_sign);
                break;
            default:
                break;
        }


        switch (bean.getStatus())
        {
            case 1:

                status.setText("未开始");
                break;
            case 2:

                status.setText("进行中");
                break;
            case 3:

                status.setText("已过期");
                break;
            default:
                break;
        }

        mBottomTextView.setText("题量:" + bean.getTest_paper().getQuestion_count());
        mBottomTextView2.setText("满分:" + bean.getTest_paper().getScore());
        mBottomTextView3.setText("有效期至:" + bean.getEnd_time());

    }

}
