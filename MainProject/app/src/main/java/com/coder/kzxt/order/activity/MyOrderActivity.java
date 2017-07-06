package com.coder.kzxt.order.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.order.adapter.MyOrderAdapter;
import com.coder.kzxt.order.beans.MyOrderBean;
import com.coder.kzxt.order.beans.OrderPayment;
import com.coder.kzxt.order.context.OrderContext;
import com.coder.kzxt.order.delegate.AllOrderDelegate;
import com.coder.kzxt.order.delegate.DoneOrderDelegate;
import com.coder.kzxt.order.delegate.ObligationDelegate;
import com.coder.kzxt.order.fragment.AllOrderFragment;
import com.coder.kzxt.order.fragment.DoneFragment;
import com.coder.kzxt.order.fragment.ObligationFragment;
import com.coder.kzxt.order.mInterface.OnAllOrderInterface;
import com.coder.kzxt.order.mInterface.OnOrderInterface;
import com.coder.kzxt.order.mInterface.OrderPayResult;
import com.coder.kzxt.order.payImpl.AliPayStrategy;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.video.beans.CourseClassBean;

import java.util.ArrayList;

/**
 * 我的订单
 * Created by wangtingshun on 2017/4/13.
 */
public class MyOrderActivity extends BaseActivity implements OnOrderInterface,OnAllOrderInterface,OrderPayResult{

    private Toolbar mToolBar;
    private ViewPager pager;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private TabLayout tabLayout;
    private MyOrderAdapter orderAdapter;
    private AllOrderFragment orderFragment; //全部
    private ObligationFragment obligFragment;//待付款
    private DoneFragment  doneFragment;  //已完成
    private AllOrderDelegate allOrderDelegate;
    private ObligationDelegate obligationDelegate;
    private DoneOrderDelegate doneDelegate;
    private RelativeLayout myLayout;
    private Dialog asyDialog;
    private String pay_method;
    private String orderId; //订单id
    private Double totalAmount; //总价
    private String amount; //优惠后的价格
    private String courseName; //课程名称
    private AliPayStrategy aliPayStrategy;
    private int index = -1; //索引
    private MyOrderBean.OrderBean orderBean;
    private SharedPreferencesUtil spu;
    private String courseId; //课程id
    private String orderSn; //订单号
    private PullRefreshDelegate delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_layout);
        spu = new SharedPreferencesUtil(this);
        initView();
        initFragment();
        initListener();
        initFragmentData();
        getPayMethod();
    }

    private void initView() {
        mToolBar = (Toolbar) findViewById(R.id.mToolBar);
        mToolBar.setTitle(getResources().getString(R.string.my_order));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        pager = (ViewPager) findViewById(R.id.pager);
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);
        pager.setOffscreenPageLimit(1);
    }

    private void initFragment() {
        fragments.clear();// 清空
        Bundle bundle = new Bundle();
        orderFragment = new AllOrderFragment();
        obligFragment = new ObligationFragment();
        bundle.putString(obligFragment.OBLIG,"1");
        obligFragment.setArguments(bundle);
        doneFragment = new DoneFragment();
        bundle.putString(doneFragment.DONE,"2");
        doneFragment.setArguments(bundle);
        fragments.add(orderFragment);
        fragments.add(obligFragment);
        fragments.add(doneFragment);
    }

    private void initListener() {
        orderFragment.setOnAllOrderInterface(this);
        obligFragment.setOnAllOrderInterface(this);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                refreshData(index);
                pageFragmentSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 刷新数据
     * @param index
     */
    private void refreshData(int index) {
        if (index == 0) {
            orderFragment.refreshOrderList();
        } else if (index == 1) {
            obligFragment.refreshOrderList();
        } else {
            orderFragment.refreshOrderList();
        }
    }

    /**
     * 切换不同的fragment
     * @param position
     */
    private void pageFragmentSelect(int position) {
        switch (position){
            case 0:
//                onPageAllOrderFragment();
                break;
            case 1:
                onPageObligationFragment();
                break;
            case 2:
                onPageDoneFragment();
                break;
        }
    }

    private void onPageDoneFragment() {
        if (doneFragment != null) {
            doneDelegate = doneFragment.getDoneDelegate();
            if (doneDelegate != null) {
                doneDelegate.setOrderPayStateListener(MyOrderActivity.this);
            }
        }
    }

    private void onPageObligationFragment() {
        if (obligFragment != null) {
            obligationDelegate = obligFragment.getObligationDelegate();
            if (obligationDelegate != null) {
                obligationDelegate.setOrderPayStateListener(MyOrderActivity.this);
            }
        }
    }

//    private void onPageAllOrderFragment() {
//        if (orderFragment != null) {
//            allOrderDelegate = orderFragment.getAllOrderDelegate();
//            if (allOrderDelegate != null) {
//                allOrderDelegate.setOrderPayStateListener(MyOrderActivity.this);
//            }
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        menu.findItem(R.id.menu_download).setVisible(false);
        menu.findItem(R.id.menu_message).setVisible(false);
        MenuItem serarchItem = menu.findItem(R.id.menu_search);
        MenuItemCompat.setShowAsAction(serarchItem,MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    private void initFragmentData() {
        orderAdapter = new MyOrderAdapter(this,getSupportFragmentManager(),fragments);
        pager.setAdapter(orderAdapter);
        tabLayout.setupWithViewPager(pager);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_search:
                startSearch();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 开始搜索
     */
    private void startSearch() {
        Intent intent = new Intent(MyOrderActivity.this, OrderSearchActivity.class);
        startActivityForResult(intent,100);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void laodProgressDialog(){
        asyDialog = MyPublicDialog.createLoadingDialog(this);
        asyDialog.show();
    }

    /**
     * 获取支付方式
     */
    public void getPayMethod() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_ORDER_PAYMENT)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        OrderPayment payment = (OrderPayment) resultBean;
                        setPayment(payment.getItems());
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        pay_method = "8";
                    }
                })
                .setClassObj(OrderPayment.class)
                .addQueryParams("platform_type", Constants.PLATFORM)
                .addQueryParams("business_logic", "1")
                .addQueryParams(Constants.PAY_TYPE, "1")
                .build();
    }


    /**
     * 设置支付方式
     * @param items
     */
    private void setPayment(ArrayList<OrderPayment.Item> items) {
        if (items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                OrderPayment.Item payMent = items.get(i);
                if (payMent.getPaymentProvider().equals("ALIPAY")) {
                    pay_method = payMent.getId();
                    if(TextUtils.isEmpty(pay_method)){
                        pay_method = "8";
                    }
                } else if (payMent.getPaymentProvider().equals("WEIXIN")) {

                }
            }
        }
    }


    @Override
    public void onOrderCancle(MyOrderBean.OrderBean bean , PullRefreshDelegate delegate) {
         orderSelectDialog(bean,delegate);
    }

    private void orderSelectDialog(final MyOrderBean.OrderBean bean , final PullRefreshDelegate delegate) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MyOrderActivity.this, R.style.custom_dialog);
        builder.setMessage("是否确定取消订单？");
        builder.setNegativeButton(getResources().getString(R.string.dialog_close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        builder.setPositiveButton(getResources().getString(R.string.password_dialog_ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                laodProgressDialog();
                cancleOrderTask(bean, delegate);
                dialog.cancel();
            }
        });
        builder.show();
    }


    /**
     * 取消订单
     * @param bean
     * @param delegate
     */
    private void cancleOrderTask(MyOrderBean.OrderBean bean, final PullRefreshDelegate delegate) {
                new HttpPutBuilder(this)
                .setUrl(UrlsNew.PUT_CANCLE_ORDER)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        resetRefreshData(delegate);
                        ToastUtils.makeText(MyOrderActivity.this,getResources().getString(R.string.cancel_success), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleLoadDialog();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(MyOrderActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(MyOrderActivity.this, myLayout);
                        }
                        ToastUtils.makeText(MyOrderActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(null)
               // .addBodyParam("status", "5")  //"订单状态: 1未付款(created) 2已付款(paid) 3已过期(overdue) 4已退款(refunded) 5已取消(cancelled) 6分期中"
                .addBodyParam("action","cancle")  //暂时写死 ，取消订单
                .setPath(bean.getId())
                .build();

    }

    /**
     * 刷新数据
     * @param delegate
     */
    public void resetRefreshData(PullRefreshDelegate delegate) {
        if (delegate instanceof AllOrderDelegate) {
            orderFragment.refreshOrderList();
        } else if (delegate instanceof ObligationDelegate) {
            obligFragment.refreshOrderList();
        } else {
            orderFragment.refreshOrderList();
            obligFragment.refreshOrderList();
        }
    }


    /**
     * 立即支付
     * @param bean
     */
    @Override
    public void onOrderPay(MyOrderBean.OrderBean bean) {
       orderPay(bean);
    }

    private void orderPay(MyOrderBean.OrderBean bean) {
        orderId = bean.getId();
        amount = bean.getAmount();
        totalAmount = Double.parseDouble(bean.getTotal_price());
        courseName = bean.getCourse().getTitle();
        courseId = bean.getCourse().getId();
        orderSn = bean.getSn();
        if (!TextUtils.isEmpty(pay_method) && !TextUtils.isEmpty(orderSn)) {
            laodProgressDialog();
            getCourseClassMember();
        }
    }


    @Override
    public void onOrderItem(MyOrderBean.OrderBean bean) {
        this.orderBean = bean;
        Intent intent = new Intent(MyOrderActivity.this,OrderDetailActivity.class);
        intent.putExtra("orderBean",bean);
        startActivityForResult(intent,1);
    }

    /**
     * 取消加载框
     */
    public void cancleLoadDialog(){
        if(asyDialog != null && asyDialog.isShowing()){
            asyDialog.cancel();
        }
    }

    /**
     * 订单取消
     * @param bean
     * @param delegate
     */
    @Override
    public void onAllOrderCancle(MyOrderBean.OrderBean bean, PullRefreshDelegate delegate) {
        orderSelectDialog(bean,delegate);
    }

    /**
     * 订单支付
     * @param bean
     */
    @Override
    public void onAllOrderPay(MyOrderBean.OrderBean bean,PullRefreshDelegate delegate) {
        this.delegate = delegate;
        orderPay(bean);
    }

    /**
     * 取消加载框
     */
    @Override
    public void onCancleLoading() {
        cancleLoadDialog();
    }


    /**
     * 支付任务
     */
    private void payTask() {
        aliPayStrategy = new AliPayStrategy(MyOrderActivity.this,courseName,
                myLayout,orderSn,pay_method,totalAmount,totalAmount);
        aliPayStrategy.setOnPayResultListener(MyOrderActivity.this);
        OrderContext context = new OrderContext(aliPayStrategy);
        context.orderPayMethod();
    }

    /**
     * 支付成功
     */
    @Override
    public void onOrderPaySuccess() {
        resetRefreshData(delegate);
    }

    /**
     * 支付失败
     */
    @Override
    public void onOrderPayFail(String result) {
        if (TextUtils.equals(result, "6001")) {
            if (index == 0) {
                orderFragment.refreshOrderList();
            } else if (index == 1) {
                obligFragment.refreshOrderList();
            }
        }
    }

    public void getCourseClassMember(){
                new HttpGetBuilder(MyOrderActivity.this)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        cancleLoadDialog();
                        CourseClassBean courseClassBean = (CourseClassBean) resultBean;
                        if (courseClassBean.getItems().size() == 0) {
                            payTask();
                        } else {
                            ToastUtils.makeText(MyOrderActivity.this,"你已购买过此课程",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleLoadDialog();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(MyOrderActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(MyOrderActivity.this, myLayout);
                        }
                    }
                })
                .setClassObj(CourseClassBean.class)
                .setUrl(UrlsNew.GET_COURSE_CLASS_MEMBER)
                .addQueryParams("user_id", spu.getUid())
                .addQueryParams("course_id", courseId)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        orderFragment.refreshOrderList();
        obligFragment.refreshOrderList();
    }
}
