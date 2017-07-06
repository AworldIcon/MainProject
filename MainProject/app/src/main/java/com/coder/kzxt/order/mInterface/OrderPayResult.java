package com.coder.kzxt.order.mInterface;

/**
 * 订单支付结果
 * Created by wangtingshun on 2017/4/18.
 */

public interface OrderPayResult {

    /**
     * 订单支付成功
     */
    void onOrderPaySuccess();

    /**
     * 订单支付失败
     */
    void onOrderPayFail(String resultCode);
}
