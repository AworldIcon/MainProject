package com.coder.kzxt.order.mInterface;

import com.coder.kzxt.order.beans.MyOrderBean;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;

/**
 * 订单支付回调接口
 * Created by wangtingshun on 2017/4/13.
 */

public interface OnOrderInterface {

    /**
     * 点击订单取消
     * @param bean
     */
    void onOrderCancle(MyOrderBean.OrderBean bean, PullRefreshDelegate delegate);

    /**
     * 点击订单支付
     * @param bean
     */
    void onOrderPay(MyOrderBean.OrderBean bean);

    /**
     * 点击订单item
     * @param bean
     */
    void onOrderItem(MyOrderBean.OrderBean bean);
}
