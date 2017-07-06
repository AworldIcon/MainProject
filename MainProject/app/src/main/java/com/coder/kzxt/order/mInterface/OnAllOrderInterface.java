package com.coder.kzxt.order.mInterface;

import com.coder.kzxt.order.beans.MyOrderBean;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;

/**
 *
 * Created by Administrator on 2017/4/17.
 */

public interface OnAllOrderInterface {

    /**
     * 订单取消
     */
    void onAllOrderCancle(MyOrderBean.OrderBean bean, PullRefreshDelegate delegate);

    /**
     * 订单支付
     */
    void onAllOrderPay(MyOrderBean.OrderBean bean,PullRefreshDelegate delegate);

//    /**
//     * 点击订单item
//     */
//    void onAllOrderItem();

    void onCancleLoading();

}
