package com.coder.kzxt.order.context;

import com.coder.kzxt.order.mInterface.PayStrategy;

/**
 * 订单环境类
 * Created by wangtingshun on 2017/4/18.
 */

public class OrderContext {

    private PayStrategy strategy;

    public OrderContext(PayStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 订单支付方法
     */
    public void orderPayMethod(){
        strategy.pay();
    }

}
