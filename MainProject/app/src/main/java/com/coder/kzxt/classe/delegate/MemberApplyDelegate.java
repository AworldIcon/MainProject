package com.coder.kzxt.classe.delegate;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.beans.ClassMemberApply;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import java.util.List;

/**
 * Created by wangtingshun on 2017/6/9.
 *
 */

public class MemberApplyDelegate extends PullRefreshDelegate <ClassMemberApply.MemberApplyBean>{

    private Context mContext;

    public MemberApplyDelegate(Context context) {
        super(R.layout.join_check_item);
        this.mContext = context;
    }


    @Override
    public void initCustomView(BaseViewHolder holder, List<ClassMemberApply.MemberApplyBean> data, int position) {
        super.initCustomView(holder, data, position);
        ImageView classIcon = holder.findViewById(R.id.class_icon);
        TextView className = holder.findViewById(R.id.tv_class_name);
        TextView joinDate = holder.findViewById(R.id.tv_date);
        TextView tv_agree = holder.findViewById(R.id.tv_agree);
        TextView tv_refused = holder.findViewById(R.id.tv_refused);

        final ClassMemberApply.MemberApplyBean applyBean = data.get(position);
//      GlideUtils.loadHeaderOfCommon(mContext,applyBean.getLogo(),classIcon);
        String create_time = applyBean.getCreate_time();
        long createTime = Long.parseLong(create_time);
        joinDate.setText(DateUtil.getDateAndMinute(createTime));
        className.setText(applyBean.getProfile().getNickname());
        //同意
        tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (applyInterface != null) {
                    applyInterface.onAgreeApply(applyBean);
                }
            }
        });

        //拒绝
        tv_refused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (applyInterface != null) {
                    applyInterface.onRefusedApply(applyBean);
                }
            }
        });
    }


    private OnClassMemberApplyInterface applyInterface;

    public interface OnClassMemberApplyInterface{

        void onAgreeApply(ClassMemberApply.MemberApplyBean applyBean);
        void onRefusedApply(ClassMemberApply.MemberApplyBean applyBean);
    }

    public void setOnClassMemberApplayListener(OnClassMemberApplyInterface listener){
        this.applyInterface = listener;
    }

}
