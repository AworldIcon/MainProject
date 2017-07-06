package com.coder.kzxt.order.payImpl;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.order.beans.OrderSignBean;
import com.coder.kzxt.order.mInterface.OrderPayResult;
import com.coder.kzxt.order.mInterface.PayStrategy;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import cc.pay.sdk.utils.alipay.Alipay;

/**
 * 支付宝实现类
 * Created by wangtingshun on 2017/4/18.
 */

public class AliPayStrategy implements PayStrategy {

    private Activity mContext;
    private SharedPreferencesUtil spu;
    private String orderSn;
    private String pay_method_id;
    private double totalPrice;
    private double amountPrice;
    private String title;
    private RelativeLayout view;

    public AliPayStrategy(Activity ccontext, String title, RelativeLayout view, String orderSn, String methodId, double totalPrice, double amountPrice) {
        this.spu = new SharedPreferencesUtil(ccontext);
        this.mContext = ccontext;
        this.title = title;
        this.view = view;
        this.orderSn = orderSn;
        this.totalPrice = totalPrice;
        this.amountPrice = amountPrice;
        this.pay_method_id = methodId;
    }


    @Override
    public void pay() {
        if (!TextUtils.isEmpty(orderSn) && !TextUtils.isEmpty(pay_method_id))
                    new HttpPostBuilder(mContext)
                    .setUrl(UrlsNew.POST_ORDER_SIGN)
                    .setHttpResult(new HttpCallBack() {
                        @Override
                        public void setOnSuccessCallback(int requestCode, Object resultBean) {
                            OrderSignBean bean = (OrderSignBean) resultBean;
                            OrderSignBean.SignItem signItem = bean.getItem();
                            startPay(signItem.getSign());
                        }

                        @Override
                        public void setOnErrorCallback(int requestCode, int code, String msg) {
                            if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                                NetworkUtil.httpRestartLogin(mContext, view);
                            } else {
                                NetworkUtil.httpNetErrTip(mContext, view);
                            }
                               ToastUtils.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setClassObj(OrderSignBean.class)
                    .addBodyParam("order_no", orderSn)
                    .addBodyParam("user_id", spu.getUid())
                    .addBodyParam("device_info", spu.getDevicedId())
                    .addBodyParam("business_logic", "1")
                    .addBodyParam("platform_type", Constants.PLATFORM)
                    .addBodyParam("payment_provider", "1")
                    .addBodyParam("payment_method_id", pay_method_id)
                    .addBodyParam("total_price", String.valueOf(totalPrice))  //商品总价
                    .addBodyParam("paid_price", String.valueOf(totalPrice))  //商品总价优惠后的价格
                    .addBodyParam("title", title)     // 商品标题
                    .addBodyParam("type", "1")   // 1 课程  2 服务
                    .build();
    }

    private void startPay(String sign) {
        Alipay Alipay = new Alipay(mContext, sign, new Alipay.PayStatusCallBack() {
            @Override
            public void onAliPaySuccess(String resultStatus) {
                if (TextUtils.equals(resultStatus, "9000")) {
                    ToastUtils.makeText(mContext, mContext.getResources().getString(R.string.order_form_pay_success), Toast.LENGTH_SHORT).show();
                    payListener.onOrderPaySuccess();
                } else if (TextUtils.equals(resultStatus, "8000")) {
                    ToastUtils.makeText(mContext, mContext.getResources().getString(R.string.order_form_pay_ing), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAliPayFailure(String resultStatus) {
                if (TextUtils.equals(resultStatus, "4000")) {
                    ToastUtils.makeText(mContext, mContext.getResources().getString(R.string.order_form_pay_fail), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.equals(resultStatus, "6001")) {
                    ToastUtils.makeText(mContext, mContext.getResources().getString(R.string.order_form_cancel_by_user), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.equals(resultStatus, "6002")) {
                    ToastUtils.makeText(mContext, mContext.getResources().getString(R.string.net_not_good), Toast.LENGTH_SHORT).show();
                }
                payListener.onOrderPayFail(resultStatus);
            }
        });
        Alipay.pay();
    }

    private OrderPayResult payListener;

    public void setOnPayResultListener(OrderPayResult listener){
        this.payListener = listener;
    }

}
