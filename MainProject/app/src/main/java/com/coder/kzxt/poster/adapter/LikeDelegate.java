package com.coder.kzxt.poster.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.poster.beans.LikePraiseBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;
import java.util.List;

/**
 * Created by tcy on 2017/3/8.
 */

public class LikeDelegate extends PullRefreshDelegate<LikePraiseBean.ItemsBean> {

    private Context context;
    public LikeDelegate(Context context) {
        super(R.layout.give_class_member_item);
        this.context =context;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<LikePraiseBean.ItemsBean> obj, int position) {

        TextView member_tx = (TextView) holder.findViewById(R.id.member_tx);
        ImageView member_img = (ImageView) holder.findViewById(R.id.member_img);
        LikePraiseBean.ItemsBean ItemsBean = obj.get(position);

        String userName = ItemsBean.getNickname();
        String userFace = ItemsBean.getAvatar();
        if(userName.length()>6){
            member_tx.setText(userName.substring(0,5)+"...");
        }else{
            member_tx.setText(userName);
        }
        GlideUtils.loadCircleHeaderOfCommon(context,userFace,member_img);

    }
}
