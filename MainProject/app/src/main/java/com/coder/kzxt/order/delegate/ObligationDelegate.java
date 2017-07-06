package com.coder.kzxt.order.delegate;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.order.beans.MyOrderBean;
import com.coder.kzxt.order.mInterface.OnOrderInterface;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.Utils;

import java.util.List;

/**
 * 待付款的delegate
 * Created by wangtingshun on 2017/4/13.
 */

public class ObligationDelegate extends PullRefreshDelegate<MyOrderBean.OrderBean> {

    private Context mContext;

    public ObligationDelegate(Context context) {
        super(R.layout.my_order_item);
        this.mContext = context;
    }


    @Override
    public void initCustomView(BaseViewHolder holder, List<MyOrderBean.OrderBean> data, int position) {
        TextView payState = holder.findViewById(R.id.tv_pay_state);  //付款状态
        TextView totalPrice = holder.findViewById(R.id.tv_total_price);  //总计
        RelativeLayout itemBottom = holder.findViewById(R.id.rl_order_bottom);  //订单底部
        TextView cancleOrder = holder.findViewById(R.id.tv_cancle_order);  //取消订单
        TextView nowPay = holder.findViewById(R.id.tv_now_pay);    //立即支付
        ImageView stateImg = holder.findViewById(R.id.iv_pay_state);  //付款状态
        ImageView courseImg = holder.findViewById(R.id.iv_course); //课程img
        TextView courseName = holder.findViewById(R.id.tv_course_name); //课程名称
        TextView courseTime = holder.findViewById(R.id.tv_course_time); //课程时长
        TextView coursePrice = holder.findViewById(R.id.tv_course_price); //课程价格
        RelativeLayout contentItem = holder.findViewById(R.id.rl_content);

        final MyOrderBean.OrderBean orderBean = data.get(position);

        String status = orderBean.getStatus();
        if (status.equals("1")) {  //等待付款
            payState.setText(mContext.getResources().getString(R.string.wait_pay));
            stateImg.setVisibility(View.GONE);
            itemBottom.setVisibility(View.VISIBLE);
        }
//        else if (orderBean.getStatus().equals("2")) {  //已完成
//            stateImg.setVisibility(View.VISIBLE);
//            itemBottom.setVisibility(View.GONE);
//            payState.setText(mContext.getResources().getString(R.string.already_complete));
//        } else if (status.equals("3")) {  //已过期
//            payState.setText(mContext.getResources().getString(R.string.order_close));
//            stateImg.setVisibility(View.GONE);
//            itemBottom.setVisibility(View.GONE);
//        } else if (status.equals("4")) {     //已退款
//            payState.setText(orderBean.getStatus_name());
//            stateImg.setVisibility(View.GONE);
//            itemBottom.setVisibility(View.GONE);
//        } else if (status.equals("5")) {  //已取消
//            payState.setText(mContext.getResources().getString(R.string.order_close));
//            itemBottom.setVisibility(View.GONE);
//            stateImg.setVisibility(View.GONE);
//        } else if (status.equals("6")) {   //分期中
//            payState.setText(orderBean.getStatus_name());
//            stateImg.setVisibility(View.GONE);
//            itemBottom.setVisibility(View.GONE);
//        }

        if (orderListener != null) {
            cancleOrder.setOnClickListener(new View.OnClickListener() { //取消订单
                @Override
                public void onClick(View view) {
                    orderListener.onOrderCancle(orderBean, ObligationDelegate.this);
                }
            });

            nowPay.setOnClickListener(new View.OnClickListener() {  //立即支付
                @Override
                public void onClick(View view) {
                    orderListener.onOrderPay(orderBean);
                }
            });

            contentItem.setOnClickListener(new View.OnClickListener() {   //点击item
                @Override
                public void onClick(View view) {
                    orderListener.onOrderItem(orderBean);
                }
            });
        }

        Utils.setPrice(totalPrice, orderBean.getTotal_price());
        GlideUtils.loadCourseImg(mContext, orderBean.getCourse().getMiddle_pic(), courseImg);
        courseName.setText(orderBean.getCourse().getTitle());
        Utils.setPrice(coursePrice, orderBean.getCourse_class().getPrice()); //课程价格暂时跟班级走
        courseTime.setText(orderBean.getCourse().getLesson_num());     //课程时长
    }

    private OnOrderInterface orderListener;

    public void setOrderPayStateListener(OnOrderInterface listener) {
        this.orderListener = listener;
    }

}
