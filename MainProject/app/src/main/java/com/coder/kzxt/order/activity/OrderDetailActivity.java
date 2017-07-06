package com.coder.kzxt.order.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.order.adapter.OrderDetailAdapter;
import com.coder.kzxt.order.beans.AddressBean;
import com.coder.kzxt.order.beans.CourseClass;
import com.coder.kzxt.order.beans.MyOrderBean;
import com.coder.kzxt.order.beans.OrderPayment;
import com.coder.kzxt.order.beans.OrderRecordBean;
import com.coder.kzxt.order.context.OrderContext;
import com.coder.kzxt.order.mInterface.OrderPayResult;
import com.coder.kzxt.order.payImpl.AliPayStrategy;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Utils;
import com.coder.kzxt.video.beans.CourseClassBean;
import com.coder.kzxt.views.CustomNewDialog;

import java.util.ArrayList;

/**
 * 订单详情
 * Created by wangtingshun on 2017/4/10.
 */

public class OrderDetailActivity extends BaseActivity implements OrderPayResult{

    private Toolbar mToolBar;
    private ImageView locationImg; //
    private TextView receiver;  //收货人
    private TextView phoneNumber; //手机号
    private TextView orderAddress; //订单地址
    private ImageView arrowImg;   //地址右侧箭头
    private RelativeLayout myLayout;
    private RelativeLayout contentLayout;
    private Button loadFailBtn;
    private TextView totalPrice;
    private Dialog asyDialog;
    private LinearLayout loadingLayout;
    private Context mContext;
    private LinearLayout load_fail_layout;
    private RelativeLayout addressLayout;  //用户地址
    private RecyclerView recyclerView;
    private OrderDetailAdapter detailAdapter;
    private RelativeLayout orderInfoLayout;
    private TextView payState;  //支付状态
    private SharedPreferencesUtil spu;
    private ArrayList<MyOrderBean.OrderBean> courseList = new ArrayList<>();
    private RelativeLayout orderBotom; //订单底部
    private RelativeLayout payType;//支付方式
    private CustomNewDialog orderInfoDialog;
    private MyOrderBean.OrderBean course; //课程
    private String status; //订单状态
    private MyOrderBean.OrderBean bean;
    private TextView cancleOrder; //取消订单
    private TextView orderPay;   //订单支付
    private AliPayStrategy aliPayStrategy;
    private String pay_Method;
    private double total_Price; //总价
    private String orderId; //订单id
    private String courseId; //课程id
    private String courseTitle; //课程标题
    private String orderSn; //订单编号
    private String price;
    private String classId;
    private ImageView order_lin;
    private boolean isFlag = false;
    private RelativeLayout totalLayout;//共计
    private OrderRecordBean.RecordBean recordBean; //订单记录的bean

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_activity);
        spu = new SharedPreferencesUtil(this);
        mContext = this;
        initView();
        getIntentData();
        initListener();
        getRequestData(1);
        getPayMethod();
    }

    private void getIntentData() {
        bean = (MyOrderBean.OrderBean) getIntent().getSerializableExtra("orderBean");
        if (bean != null) {
            orderSn = bean.getSn();
            orderId = bean.getId();
            courseId = bean.getCourse().getId();
            courseTitle = bean.getCourse().getTitle();
            price = bean.getCourse_class().getPrice();
            classId = bean.getCourse_class_id();
        } else {
            orderSn = getIntent().getStringExtra("orderSn");
            orderId = getIntent().getStringExtra("orderId");
            courseId = getIntent().getStringExtra("courseId");
            classId = getIntent().getStringExtra("classId");
        }
    }

    private void initView() {
        mToolBar = (Toolbar) findViewById(R.id.mToolBar);
        mToolBar.setTitle(R.string.order_form_info);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        locationImg = (ImageView) findViewById(R.id.iv_location);
        receiver = (TextView) findViewById(R.id.tv_order_name);
        phoneNumber = (TextView) findViewById(R.id.tv_order_phone);
        orderAddress = (TextView) findViewById(R.id.tv_order_address);
        arrowImg = (ImageView) findViewById(R.id.iv_arrow);
        arrowImg.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);
        contentLayout = (RelativeLayout) findViewById(R.id.rl_detail_layout);
        loadFailBtn = (Button) findViewById(R.id.load_fail_button);
        totalPrice = (TextView) findViewById(R.id.tv_total_price);
        loadingLayout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        addressLayout = (RelativeLayout) findViewById(R.id.rl_address);
        orderInfoLayout = (RelativeLayout) findViewById(R.id.rl_order_info);
        payState = (TextView) findViewById(R.id.tv_pay_state);
        payType = (RelativeLayout) findViewById(R.id.rl_pay_type);
        orderBotom = (RelativeLayout) findViewById(R.id.rl_order_bottom);
        cancleOrder = (TextView) findViewById(R.id.tv_cancle_order);
        orderPay = (TextView) findViewById(R.id.tv_now_pay);
        order_lin = (ImageView) findViewById(R.id.order_lin);
        order_lin.setVisibility(View.GONE);
        totalLayout = (RelativeLayout) findViewById(R.id.rl_order_total);
    }

    private void initListener() {
        //订单信息
        orderInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOrderInfoDialog();
            }
        });
        //取消订单
        cancleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderSelectDialog();
            }
        });

        //立即支付
        orderPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProgressDialog(2);
                getCourseClassMember();
            }
        });
        //加载失败
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRequestData(1);
                getPayMethod();
            }
        });
    }

    /**
     * 支付任务
     */
    private void payTask() {
        total_Price = Double.parseDouble(price);
        if (!TextUtils.isEmpty(pay_Method) && !TextUtils.isEmpty(orderSn)) {
            aliPayStrategy = new AliPayStrategy(OrderDetailActivity.this, courseTitle,
                    myLayout, orderSn, pay_Method, total_Price, total_Price);
            aliPayStrategy.setOnPayResultListener(OrderDetailActivity.this);
            OrderContext context = new OrderContext(aliPayStrategy);
            context.orderPayMethod();
        }
    }

    private void alertOrderInfoDialog() {
        initOrderInfoView();
        initOrderData();
        initOrderListener();
    }

    private TextView orderState;  //订单状态
    private TextView orderNumber; //订单号
    private TextView orderTime; // 下单时间
    private TextView payMethod; //支付方式
    private TextView payTime;  // 支付时间
    private TextView copyOrderNumber; //复制订单号
    private ImageView iv_close_info; //关闭

    private RelativeLayout rl_order_state;
    private RelativeLayout rl_order_number;
    private RelativeLayout rl_create_order;
    private RelativeLayout rl_pay_method;
    private RelativeLayout rl_pay_time;
    private View method_line;
    private View pay_time_line;

    private void initOrderInfoView() {
        orderInfoDialog = new CustomNewDialog(OrderDetailActivity.this, R.layout.activity_order_info_item);
        iv_close_info = (ImageView) orderInfoDialog.findViewById(R.id.iv_close_info);
        orderState = (TextView) orderInfoDialog.findViewById(R.id.tv_order_start);  //订单状态
        orderNumber = (TextView) orderInfoDialog.findViewById(R.id.tv_order_number);  //订单编号
        orderTime = (TextView) orderInfoDialog.findViewById(R.id.tv_create_order_time);  //下单时间
        payMethod = (TextView) orderInfoDialog.findViewById(R.id.tv_pay_method);  //支付方式
        payTime = (TextView) orderInfoDialog.findViewById(R.id.tv_pay_time);  //支付时间
        copyOrderNumber = (TextView) orderInfoDialog.findViewById(R.id.tv_copy_order_number);  //复制订单编号

        rl_order_state = (RelativeLayout) orderInfoDialog.findViewById(R.id.rl_order_state);  //订单状态
        rl_order_number = (RelativeLayout) orderInfoDialog.findViewById(R.id.rl_order_number);  //订单编号
        rl_create_order = (RelativeLayout) orderInfoDialog.findViewById(R.id.rl_create_order);  //下单时间
        rl_pay_method = (RelativeLayout) orderInfoDialog.findViewById(R.id.rl_pay_method);  //支付方式
        rl_pay_time = (RelativeLayout) orderInfoDialog.findViewById(R.id.rl_pay_time);//支付时间
        method_line = orderInfoDialog.findViewById(R.id.method_line);
        pay_time_line = orderInfoDialog.findViewById(R.id.pay_time_line);

//        Window dialogWindow = orderInfoDialog.getWindow();
//        dialogWindow.setGravity(Gravity.BOTTOM);
        orderInfoDialog.show();
    }

    private void initOrderData() {
        String status = course.getStatus();
        orderNumber.setText(course.getSn());
        String create_time = course.getCreate_time();
        String update_time = course.getUpdate_time();
        if(recordBean != null){
            String payment_name = recordBean.getOrder_payment().getPayment_name();
            payMethod.setText(payment_name);
        } else {
            payMethod.setText("支付宝");
        }
        orderTime.setText(DateUtil.getDateOrderStrDot(Long.parseLong(create_time)));
        payTime.setText(DateUtil.getDateOrderStrDot(Long.parseLong(update_time)));
        if (status.equals("2")) {
            rl_pay_method.setVisibility(View.VISIBLE);
            rl_pay_time.setVisibility(View.VISIBLE);
            method_line.setVisibility(View.VISIBLE);
            pay_time_line.setVisibility(View.VISIBLE);
        } else {
            rl_pay_method.setVisibility(View.GONE);
            rl_pay_time.setVisibility(View.GONE);
            method_line.setVisibility(View.GONE);
            pay_time_line.setVisibility(View.GONE);
        }
        if (status.equals("1")) {  //等待付款
            orderState.setText(mContext.getResources().getString(R.string.wait_pay));
        } else if (status.equals("2")) {  //已完成
            orderState.setText(mContext.getResources().getString(R.string.already_complete));
        } else if (status.equals("3")) {  // 已过期
            orderState.setText(mContext.getResources().getString(R.string.order_close));
        } else if (status.equals("4")) {
            orderState.setText(course.getStatus_name());
        } else if (status.equals("5")) {  //已取消
            orderState.setText(mContext.getResources().getString(R.string.order_close));
        } else if (status.equals("6")) {
            orderState.setText(course.getStatus_name());

        }
    }

    private void initOrderListener() {
        //关闭
        iv_close_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderInfoDialog != null && orderInfoDialog.isShowing()) {
                    orderInfoDialog.cancel();
                }
            }
        });
        //复制订单编号
        copyOrderNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancleOrderDialog();
                copy(orderSn,OrderDetailActivity.this);
                Toast.makeText(OrderDetailActivity.this,"编号已复制",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 取消对话框
     */
    public void cancleOrderDialog(){
        if(orderInfoDialog != null && orderInfoDialog.isShowing()){
            orderInfoDialog.cancel();
        }
    }

    private void getRequestData(int type) {
        loadProgressDialog(type);
        if(!TextUtils.isEmpty(orderId)){
            getSingleOrder(orderId);
        } else {
            getOrderCourse();
        }
        getSingleOrderRecord(orderId);
    }


    /**
     * 获取单条订单
     */
    private void getSingleOrder(final String orderId) {
                new HttpGetBuilder(mContext)
                .setUrl(UrlsNew.GET_ORDER_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        MyOrderBean orderBean = (MyOrderBean) resultBean;
                        ArrayList<MyOrderBean.OrderBean> items = orderBean.getItems();
                        if (items != null && items.size() > 0) {
                            load_fail_layout.setVisibility(View.GONE);
                            adapterData(items);
                            calculatePrice(items);
                            getOrderClass();
                        } else {
                            loadFailPage();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleLoadDialog();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mContext, myLayout);
                        }
                    }
                })
                .setClassObj(MyOrderBean.class)
                .addQueryParams("id",orderId)
                .build();
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
                        pay_Method = "8";
                    }
                })
                .setClassObj(OrderPayment.class)
                .addQueryParams("platform_type", Constants.PLATFORM)
                .addQueryParams("business_logic", "1")
                .addQueryParams(Constants.PAY_TYPE, "1")
                .build();

    }

    /**
     * 获取单条订单记录
     */
    private void getSingleOrderRecord(String orderId) {
               new HttpGetBuilder(mContext)
                .setUrl(UrlsNew.GET_ORDER_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        OrderRecordBean orderBean = (OrderRecordBean) resultBean;
                        recordBean = orderBean.getItem();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mContext, myLayout);
                        }
                    }
                })
                .setClassObj(OrderRecordBean.class)
                .setPath(orderId)
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
                    pay_Method = payMent.getId();
                    if(TextUtils.isEmpty(pay_Method)){
                        pay_Method = "8";
                    }
                } else if (payMent.getPaymentProvider().equals("WEIXIN")) {

                }
            }
        }
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     * @param content
     */
    public void copy(String content, Context context){
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void loadProgressDialog(int type) {
        if (type == 1) {
            loadingLayout.setVisibility(View.VISIBLE);
            contentLayout.setVisibility(View.GONE);
        } else {
            asyDialog = MyPublicDialog.createLoadingDialog(mContext);
            asyDialog.show();
        }
    }

    /**
     * 获取用户地址列表
     */
    private void getUserAddressList() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_USER_ADDRESS_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        cancleDialog();
                        loadingLayout.setVisibility(View.GONE);
                        contentLayout.setVisibility(View.VISIBLE);
                        AddressBean addressBean = (AddressBean) resultBean;
                        ArrayList<AddressBean.Address> items = addressBean.getItems();
                        AddressBean.Paginate paginate = addressBean.getPaginate();
                        int totalNum = paginate.getTotalNum();
                        if (totalNum > 0) {
                            addressLayout.setVisibility(View.VISIBLE);
                            setAddressInfos(items);
                        } else {
                            addressLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleDialog();
                        loadFailPage();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(OrderDetailActivity.this, myLayout);
                        }
                    }
                })
                .setClassObj(AddressBean.class)
                .addQueryParams("user_id", spu.getUid())
                .build();
    }

    /**
     * 设置地址信息
     *
     * @param items
     */
    private void setAddressInfos(ArrayList<AddressBean.Address> items) {
        for (int i = 0; i < items.size(); i++) {
            AddressBean.Address address = items.get(i);
            if (address.getIs_default().equals("1")) {
                setAddress(address);
            }
        }
    }

    private void setAddress(AddressBean.Address address) {
        receiver.setText(address.getReceiver());
        phoneNumber.setText(address.getMobile());
        String province = address.getProvince();
        String city = address.getCity();
        String district = address.getDistrict();
        if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
            orderAddress.setText(province + city + district + address.getAddress_detail());
        } else if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
            orderAddress.setText(city + district + address.getAddress_detail());
        } else if (!TextUtils.isEmpty(district)) {
            orderAddress.setText(district + address.getAddress_detail());
        } else {
            orderAddress.setText(address.getAddress_detail());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isFlag) {
                    backActivity();
                } else {
                    setResult(2);
                }
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取单条课程
     */
    private void getOrderCourse() {
        if (bean != null) {
            courseList.add(bean);
            if (courseList != null && courseList.size() > 0) {
                adapterData(courseList);
                calculatePrice(courseList);
                getOrderClass();
            } else {
                loadFailPage();
            }
        } else {
            loadFailPage();
        }
    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        loadingLayout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
    }

    /**
     * 适配数据
     * @param course
     */
    private void adapterData( ArrayList<MyOrderBean.OrderBean> course) {
        detailAdapter = new OrderDetailAdapter(mContext, course,classId);
        recyclerView.setAdapter(detailAdapter);
        seOrderInfo(course.get(0));
    }

    /**
     * 设置订单信息
     * @param course
     */
    private void seOrderInfo(MyOrderBean.OrderBean course) {
        this.course = course;
        if(course != null){
            status = course.getStatus();
            orderSn  = course.getSn();
            price = course.getCourse_class().getPrice();
            courseTitle = course.getCourse().getTitle();
            if (status.equals("1")) {  //等待付款
                payType.setVisibility(View.VISIBLE);
                orderBotom.setVisibility(View.VISIBLE);
                payState.setText(getResources().getString(R.string.wait_pay));
            } else if (status.equals("2")) {  //已完成
                orderBotom.setVisibility(View.GONE);
                payType.setVisibility(View.GONE);
                payState.setText(getResources().getString(R.string.already_complete));
            } else if (status.equals("3")) {  //已过期
                payType.setVisibility(View.GONE);
                orderBotom.setVisibility(View.GONE);
                payState.setText(getResources().getString(R.string.order_close));
            } else if (status.equals("4")) {
                payState.setText(course.getStatus_name());
                orderBotom.setVisibility(View.GONE);
            } else if (status.equals("5")) {  //已取消
                payState.setText(getResources().getString(R.string.order_close));
                orderBotom.setVisibility(View.GONE);
            } else if (status.equals("6")) {
                payState.setText(course.getStatus_name());
            }
        }
    }

    /**
     * 获取课程下关联的班级
     */
    private void getOrderClass() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_COURSE_CLASS)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        CourseClass bean = (CourseClass) resultBean;
                        ArrayList<CourseClass.ClassBean> items = bean.getItems();
                        setClassInfo(items);
                        getUserAddressList();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(OrderDetailActivity.this, myLayout);
                        }
                        loadFailPage();
                        cancleDialog();
                    }
                })
                .setClassObj(CourseClass.class)
                .addQueryParams("course_id",courseId)
                .build();
    }

    public void getCourseClassMember(){
        new HttpGetBuilder(OrderDetailActivity.this)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        cancleLoadDialog();
                        CourseClassBean courseClassBean = (CourseClassBean) resultBean;
                        if (courseClassBean.getItems().size() == 0) {
                            payTask();
                        } else {
                            ToastUtils.makeText(OrderDetailActivity.this,"你已购买过此课程",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleLoadDialog();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(OrderDetailActivity.this, myLayout);
                        }
                    }
                })
                .setClassObj(CourseClassBean.class)
                .setUrl(UrlsNew.GET_COURSE_CLASS_MEMBER)
                .addQueryParams("user_id", spu.getUid())
                .addQueryParams("course_id", courseId)
                .build();
    }

    /**
     * 计算价格
     * @param items
     */
    private void calculatePrice(ArrayList<MyOrderBean.OrderBean> items) {
        MyOrderBean.OrderBean course = items.get(0);
        Utils.setPrice(totalPrice,course.getCourse_class().getPrice());
    }

    /**
     * 取消进度框
     */
    private void cancleDialog() {
        if (asyDialog != null && asyDialog.isShowing()) {
            asyDialog.cancel();
        }
    }

    /**
     * 设置班级信息
     * @param items
     */
    private void setClassInfo(ArrayList<CourseClass.ClassBean> items) {
        if (items.size() > 0) {
            detailAdapter.setOrderDetailData(items,classId);
            detailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 支付成功
     */
    @Override
    public void onOrderPaySuccess() {
        isFlag = true;
//        getRequestData(2);
        totalLayout.setVisibility(View.GONE);
        orderBotom.setVisibility(View.GONE);
        payState.setText(getResources().getString(R.string.already_complete));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isFlag) {
                backActivity();
            } else {
                setResult(2);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void backActivity() {
        Intent intent = new Intent();
        intent.putExtra("classId", classId);
        setResult(Constants.PAY_SUCCESS, intent);
    }

    /**
     * 支付失败
     */
    @Override
    public void onOrderPayFail(String result) {
        isFlag = false;
    }


    private void orderSelectDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this, R.style.custom_dialog);
        builder.setMessage("是否确定取消订单？");
        builder.setPositiveButton(getResources().getString(R.string.dialog_close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.password_dialog_ensure), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadProgressDialog(2);
                cancleOrderTask();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 取消订单
     */
    private void cancleOrderTask() {
                new HttpPutBuilder(this)
                .setUrl(UrlsNew.PUT_CANCLE_ORDER)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        getOrderCourseTask();
                        ToastUtils.makeText(OrderDetailActivity.this,getResources().getString(R.string.cancel_success), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleLoadDialog();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(OrderDetailActivity.this, myLayout);
                        }
                        ToastUtils.makeText(OrderDetailActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(null)
                // .addBodyParam("status", "5")  //"订单状态: 1未付款(created) 2已付款(paid) 3已过期(overdue) 4已退款(refunded) 5已取消(cancelled) 6分期中"
                .addBodyParam("action","cancle")  //暂时写死 ，取消订单
                .setPath(orderId)
                .build();

    }

    private void getOrderCourseTask() {
                 new HttpGetBuilder(mContext)
                .setUrl(UrlsNew.GET_ORDER_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        cancleLoadDialog();
                        MyOrderBean orderBean = (MyOrderBean) resultBean;
                        ArrayList<MyOrderBean.OrderBean> items = orderBean.getItems();
                        resetOrderData(items);
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleLoadDialog();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mContext, myLayout);
                        }
                    }
                })
                .setClassObj(MyOrderBean.class)
                .addQueryParams("user_id",spu.getUid())
                .build();

    }

    /**
     * 重置数据
     * @param items
     */
    private void resetOrderData(ArrayList<MyOrderBean.OrderBean> items) {
        if (items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                MyOrderBean.OrderBean orderBean = items.get(i);
                if (orderBean.getId().equals(orderId)) {
                    if (courseList != null) {
                        courseList.clear();
                    }
                    courseList.add(orderBean);
                }
            }
        }
        adapterData(courseList);
    }

    /**
     * 取消加载框
     */
    private void cancleLoadDialog(){
        if(asyDialog != null && asyDialog.isShowing()){
            asyDialog.cancel();
        }
    }
}
