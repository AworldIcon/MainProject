package com.coder.kzxt.classe.delegate;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.beans.NewClassListBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

/**
 * Created by wangtingshun on 2017/6/6.
 */

public class FindClassDelegate extends PullRefreshDelegate <NewClassListBean.ClassListBean>{


    private Context mContext;

    public FindClassDelegate(Context context) {
        super(R.layout.my_class_item_layout);
        this.mContext = context;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<NewClassListBean.ClassListBean> data, int position) {
        ImageView classIcon = holder.findViewById(R.id.class_icon);
        final TextView className = holder.findViewById(R.id.tv_class_name);
//        TextView classPerson = holder.findViewById(R.id.tv_class_person);
        RelativeLayout classItem = holder.findViewById(R.id.class_item);
        final NewClassListBean.ClassListBean classBean = data.get(position);

        className.setText(classBean.getName());
//        classPerson.setVisibility(View.VISIBLE);
//        classPerson.setText("("+classBean.getMember_count()+"人"+")");
        GlideUtils.loadHeaderOfClass(mContext,classBean.getLogo(),classIcon);

        classItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onFindItem(classBean);
                }
            }
        });
    }

    public OnFindItemClickInterface listener;

    public void setOnFindItemClickListener(OnFindItemClickInterface onFindItemListener){
        this.listener = onFindItemListener;
    }

    public interface OnFindItemClickInterface{
       void onFindItem(NewClassListBean.ClassListBean classBean);
   }


}
