package com.coder.kzxt.order.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.order.adapter.CustomDialogAdapter;
import com.coder.kzxt.order.adapter.OrderAdapter;
import com.coder.kzxt.order.beans.AddressBean;
import com.coder.kzxt.order.beans.CourseClass;
import com.coder.kzxt.order.beans.MyOrderBean;
import com.coder.kzxt.order.beans.Order;
import com.coder.kzxt.order.beans.OrderPayment;
import com.coder.kzxt.order.beans.SingleCourse;
import com.coder.kzxt.order.context.OrderContext;
import com.coder.kzxt.order.mInterface.OnItemClickInterface;
import com.coder.kzxt.order.mInterface.OrderPayResult;
import com.coder.kzxt.order.payImpl.AliPayStrategy;
import com.coder.kzxt.recyclerview.decoration.DividerItemDecoration;
import com.coder.kzxt.setting.activity.AddressEditActivity;
import com.coder.kzxt.setting.activity.MyAddressActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Utils;
import com.coder.kzxt.views.CustomNewDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 订单确认
 * Created by wangtingshun on 2017/4/10.
 */
public class OrderConfirmationActivity extends BaseActivity implements OnItemClickInterface,OrderPayResult {

    private Toolbar mToolBar;
    private TextView receiver; //收货人
    private TextView phoneNumber; //手机号
    private TextView orderAddress;  //订单地址
    private RecyclerView orderRecylcerView;
    private RelativeLayout addressLayout;  //用户地址
    private RelativeLayout addAddress; //添加地址
    private RelativeLayout myLayout;
    private SharedPreferencesUtil spu;
    private TextView tv_add_address;
    private Context mContext;
    private Dialog asyDialog;
    private String courseId;
    private OrderAdapter orderAdapter;
    private LinearLayout load_fail_layout;
    private RelativeLayout payTypeLayout;
    private LinearLayout orderTotalLayout;
    private Button loadFailBtn;
    private TextView totalPrice;
    private LinearLayout loadingLayout;
    private RelativeLayout contentLayout;
    private ArrayList<CourseClass.ClassBean> classItems; //班级集合
    private AddressBean.Address normalAddress;
    private LinearLayout payLayout; //立即支付
    private String orderId;  //订单id.
    private SingleCourse.Course course; //课程
    private ArrayList<SingleCourse.Course> courseItems; //课程集合
    private double totalAmount = 0.0; //总价
    private String classPrice; //默认班级价格
    private AliPayStrategy aliPayStrategy;
    private int totalNum = 0;
    private  String payMethodId; //支付方式id
    private String classId;  //班级id
    private String orderSn; //订单号
    private boolean isPay = false;
    private boolean isRefresh = false; //是否刷新
    private String scanClassId; //扫码班级id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_confirmation_layout);
        spu = new SharedPreferencesUtil(this);
        courseId = getIntent().getStringExtra("courseId");
        scanClassId = getIntent().getStringExtra("scanClassId");
        mContext = this;
        initView();
        initListener();
        getRequestData(1);
    }

    private void getRequestData(int type) {
        loadProgressDialog(type);
        getPayMethod();
        getUserAddressList();
    }

    private void initView() {
        mToolBar = (Toolbar) findViewById(R.id.mToolBar);
        mToolBar.setTitle(R.string.order_confirm);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        receiver = (TextView) findViewById(R.id.tv_order_name);
        phoneNumber = (TextView) findViewById(R.id.tv_order_phone);
        orderAddress = (TextView) findViewById(R.id.tv_order_address);
        orderRecylcerView = (RecyclerView) findViewById(R.id.order_Recycler_View);
        orderRecylcerView.setLayoutManager(new LinearLayoutManager(mContext));
        //设置Item增加、移除动画
        orderRecylcerView.setItemAnimator(new DefaultItemAnimator());
        addressLayout = (RelativeLayout) findViewById(R.id.rl_address);
        addAddress = (RelativeLayout) findViewById(R.id.rl_add_address);
        tv_add_address = (TextView) findViewById(R.id.tv_add_address);
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        payTypeLayout = (RelativeLayout) findViewById(R.id.rl_pay_type);
        orderTotalLayout = (LinearLayout) findViewById(R.id.rl_order_total);
        loadFailBtn = (Button) findViewById(R.id.load_fail_button);
        totalPrice = (TextView) findViewById(R.id.tv_total_price);
        loadingLayout = (LinearLayout) findViewById(R.id.jiazai_layout);
        contentLayout = (RelativeLayout) findViewById(R.id.rl_confirm_layout);
        payLayout = (LinearLayout) findViewById(R.id.ll_pay);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initListener() {
        //添加地址
        tv_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddressEditActivity.class);
                intent.putExtra(Constants.ADD_ADDRESS, "addAddress");
                startActivityForResult(intent,11);
            }
        });

        //地址
        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyAddressActivity.class);
                intent.putExtra(Constants.USER_INFO_ENTRANCE,"order");
                startActivityForResult(intent, 1);
            }
        });

        //加载失败
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRequestData(1);
            }
        });
        //立即支付
        payLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goPay();
            }
        });
    }

    public void goPay() {
        if(totalNum > 0){
            directPayMent();
        } else {
            ToastUtils.makeText(OrderConfirmationActivity.this,
                    getResources().getString(R.string.add_receive_address), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isPay) {
            loadProgressDialog(2);
            createOrderTask(classId);
        }
    }

    /**
     * 直接支付
     */
    public void directPayMent() {
        if ( totalAmount == 0.0) {
            isPay = true;
            paySuccess(classId);
        }
    }

    /**
     * 获取单条课程
     */
    private void getOrderCourse() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_SINGLE_COURSE)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        SingleCourse singleCourse = (SingleCourse) resultBean;
                        courseItems = singleCourse.getItems();
                        if (courseItems != null && courseItems.size() > 0) {
                            adapterData(courseItems);
                            getOrderClass();
                        } else {
                            loadFailPage();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderConfirmationActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(OrderConfirmationActivity.this, myLayout);
                        }
                        cancleDialog();
                        loadFailPage();
                    }
                })
                .setClassObj(SingleCourse.class)
                .addQueryParams("id", courseId)
                .build();
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
     * 计算总价格
     */
    private void calculatePrice() {
        //double coursePrice = 0.0;
        boolean isDefault = false;
        double claPrice = 0.0;
        if (courseItems != null && courseItems.size() > 0) {
            course = courseItems.get(0);
            //coursePrice = Double.parseDouble(course.getPrice());
        }
        if (classItems != null && classItems.size() > 0) {
            for (int i = 0; i < classItems.size(); i++) {
                CourseClass.ClassBean classBean = classItems.get(i);
                if (classBean.getIs_default().equals("1")) {
                    isDefault = true;
                    claPrice = Double.parseDouble(classBean.getPrice());
                }
                if (!isDefault) {
                    claPrice = Double.parseDouble(classBean.getPrice());
                }
            }
        } else if (!TextUtils.isEmpty(classPrice)) {
            claPrice = Double.parseDouble(classPrice);
        }
        //计算总价  //暂时跟随班级价格
        totalAmount = claPrice;

        Utils.setPrice(totalPrice, String.valueOf(totalAmount));
    }


    /**
     * 适配数据
     *
     * @param course
     */
    private void adapterData(ArrayList<SingleCourse.Course> course) {
        load_fail_layout.setVisibility(View.GONE);
        if (course != null && course.size() > 0) {
            contentLayout.setVisibility(View.VISIBLE);
            orderAdapter = new OrderAdapter(mContext, course,scanClassId);
            orderRecylcerView.setAdapter(orderAdapter);
            orderAdapter.setOnClassItemClickListener(this);
        } else {
            contentLayout.setVisibility(View.GONE);
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
                        cancleDialog();
                        loadingLayout.setVisibility(View.GONE);
                        CourseClass bean = (CourseClass) resultBean;
                        classItems = applyClass(bean.getItems());
                        getNormalClassId(classItems);
                        setClassInfo(classItems);
                        calculatePrice();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderConfirmationActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(OrderConfirmationActivity.this, myLayout);
                        }
                        cancleDialog();
                        loadingLayout.setVisibility(View.GONE);
                    }
                })
                .setClassObj(CourseClass.class)
                .addQueryParams("course_id", courseId)
                .build();
    }

    /**
     * 筛选包名的班级
     * @param items
     * @return
     */
    private ArrayList<CourseClass.ClassBean> applyClass(ArrayList<CourseClass.ClassBean> items) {
        ArrayList<CourseClass.ClassBean> classList = new ArrayList<>();
        if (items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                CourseClass.ClassBean classBean = items.get(i);
                if (!classBean.getApply_status().equals("0")) {
                    classList.add(classBean);
                }
            }
            return classList;
        }
        return null;
    }

    private void getNormalClassId(ArrayList<CourseClass.ClassBean> classItems) {
        boolean isDefault = false;
        if (classItems != null && classItems.size() > 0) {
            for (int i = 0; i < classItems.size(); i++) {
                CourseClass.ClassBean classBean = classItems.get(i);
                if (classBean.getIs_default().equals("1")) {
                    isDefault = true;
                    classId = classBean.getId();
                    classPrice = classBean.getPrice();
                }
                if (!isDefault) {
                    classId = classBean.getId();
                    classPrice = classBean.getPrice();
                }
            }
        }
    }

    /**
     * 支付任务
     */
    private void payTask() {
        if (!TextUtils.isEmpty(orderSn) && !TextUtils.isEmpty(payMethodId)){
            aliPayStrategy = new AliPayStrategy(OrderConfirmationActivity.this,course.getTitle(),
                    myLayout,orderSn,payMethodId,totalAmount,totalAmount);
            aliPayStrategy.setOnPayResultListener(OrderConfirmationActivity.this);
            OrderContext context = new OrderContext(aliPayStrategy);
            context.orderPayMethod();
        } else {
            cancleDialog();
        }
    }


    /**
     * 创建订单
     * @param classId
     */
    private void createOrderTask(String classId) {

        if (TextUtils.isEmpty(classId)) {
            cancleDialog();
            ToastUtils.makeText(OrderConfirmationActivity.this,
                    "请选择授课班", Toast.LENGTH_SHORT).show();
            return;
        }
        if (totalNum > 0) {
            createNewOrder(checkParams(classId));
        } else {
            ToastUtils.makeText(OrderConfirmationActivity.this,
                    getResources().getString(R.string.add_receive_address), Toast.LENGTH_SHORT).show();
            cancleDialog();
            loadingLayout.setVisibility(View.GONE);
        }
        if (orderAdapter != null) {
            orderAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 校验参数
     * @return
     */
    private String checkParams(String classId) {
        String confirm = "";
//        String serviceIds ="";
//        String service_price ="";
//        String alone = "0";  //是服务的保过不保过
//        String type = "1";
//        String item_id = courseId;   //下单的来源
//        String class_price = classPrice;
          confirm = "serviceIds=" + "" + "&" + "service_price=" + "" + "&" +
                  "alone=" + "0" + "&" + "type=" + "1" + "&" + "item_id=" + courseId
                  + "&" + "classId=" + classId + "&" + "class_price"+"["+classId+"]=" + classPrice;
        return confirm;
    }

    private void createNewOrder(String confirm) {       //TODO:待修改，接口那边已修改，等待文档更新添加新参数。
                 new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_CREATE_ORDER)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        Order order = (Order) resultBean;
                        orderId = order.getItem().getOrder_id();
                        if (!TextUtils.isEmpty(orderId)) {
                            spu.setOrderId(orderId);
                        }
                        getSingleOrder(orderId);
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderConfirmationActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(OrderConfirmationActivity.this, myLayout);
                        }
                        if (code == 5007) {
                            orderId = spu.getOrderId();
//                          ToastUtils.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
                            getSingleOrder(orderId);
                        }
                        cancleDialog();
                        loadingLayout.setVisibility(View.GONE);
                    }
                })
                .setClassObj(Order.class)
                .addBodyParam(compoundData(confirm))
                .build();
    }



    private String compoundData(String confirm) {

        JSONObject objData = new JSONObject();
        try {
            objData.put("confirm",confirm);
            objData.put("addressId",normalAddress.getId());
            objData.put("user_id",spu.getUid());
            JSONArray goodArray = new JSONArray();
            JSONObject goodObj = new JSONObject();
            goodObj.put("classId",classId);
            goodObj.put("courseId",courseId);
            goodArray.put(goodObj);
            objData.put("goods",goodArray);

//            JSONArray serviceArray = new JSONArray();
//            JSONObject serviceObj = new JSONObject();
//            serviceObj.put("serviceId",""); //一期暂时传空
//            serviceArray.put(serviceObj);
//            objData.put("services",serviceArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return objData.toString();
    }

    /**
     * 设置班级信息
     *
     * @param items
     */
    private void setClassInfo(ArrayList<CourseClass.ClassBean> items) {
        if (items.size() > 0 && orderAdapter != null) {
            orderAdapter.setOrderData(items);
            orderAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载进度框
     *
     * @param type
     */
    private void loadProgressDialog(int type) {
        if (type == 1) {
            contentLayout.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.VISIBLE);
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
                        AddressBean addressBean = (AddressBean) resultBean;
                        ArrayList<AddressBean.Address> items = addressBean.getItems();
                        AddressBean.Paginate paginate = addressBean.getPaginate();
                        totalNum = paginate.getTotalNum();
                        if (totalNum > 0) {
                            addressLayout.setVisibility(View.VISIBLE);
                            addAddress.setVisibility(View.GONE);
                            setAddressInfos(items);
                        } else {
                            addAddress.setVisibility(View.VISIBLE);
                            addressLayout.setVisibility(View.GONE);
                        }
                        if(!isRefresh){
                            getOrderCourse();
                        }
                        isRefresh = false;
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleDialog();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderConfirmationActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(OrderConfirmationActivity.this, myLayout);
                        }
                        addAddress.setVisibility(View.VISIBLE);
                        loadingLayout.setVisibility(View.GONE);
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

    /**
     * 设置地址信息
     *
     * @param address
     */
    private void setAddress(AddressBean.Address address) {
        this.normalAddress = address;
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
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
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
                        payMethodId = "8";
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
                    payMethodId = payMent.getId();
                    if(TextUtils.isEmpty(payMethodId)){
                        payMethodId = "8";
                    }
                } else if (payMent.getPaymentProvider().equals("WEIXIN")) {

                }
            }
        }
    }

    /**
     * 获取单条订单
     */
    private void getSingleOrder(String orderId) {
                 new HttpGetBuilder(mContext)
                .setUrl(UrlsNew.GET_ORDER_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        MyOrderBean orderBean = (MyOrderBean) resultBean;
                        ArrayList<MyOrderBean.OrderBean> items = orderBean.getItems();
                        if (items.size() > 0) {
                            orderSn = items.get(0).getSn();
                            payTask();
                        }
                        cancleDialog();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleDialog();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderConfirmationActivity.this, myLayout);
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
     * 取消进度框
     */
    private void cancleDialog() {
        if (asyDialog != null && asyDialog.isShowing()) {
            asyDialog.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1 ||requestCode == 11) && resultCode == 100) {
            isRefresh = true;
            loadProgressDialog(2);
            getUserAddressList();
        } else if(resultCode == Constants.LOGIN_BACK){
            getRequestData(1);
        } else if(resultCode == Constants.PAY_SUCCESS){
            String classId = data.getStringExtra("classId");
            paySuccess(classId);
        } else if(requestCode == 1 && resultCode == Constants.RESULT_CODE){
            AddressBean.Address addressBean = (AddressBean.Address)data.getSerializableExtra("addressBean");
            setAddress(addressBean);
        }
    }

    /**
     * 点击授课班
     *
     * @param classBean
     */
    @Override
    public void onClassItemClick(CourseClass.ClassBean classBean) {
        if (classItems.size() > 1) {
            alertSelectClass();
        }
    }

    /**
     * 点击授课班内部条目
     *
     * @param classBean
     */
    @Override
    public void onInnerClassItemClick(CourseClass.ClassBean classBean) {
        if (classDialog != null && classDialog.isShowing()) {
            classDialog.cancel();
        }
        if (classItems != null) {
            for (int i = 0; i < classItems.size(); i++) {
                CourseClass.ClassBean bean = classItems.get(i);
                if (bean.getId().equals(classBean.getId())) {
                    classId = classBean.getId();
                    classPrice = classBean.getPrice();
                    bean.setIs_default("1");
                    calculatePrice();
                } else {
                    bean.setIs_default("0");
                }
            }
            orderAdapter.setOrderData(classItems);
            orderAdapter.notifyDataSetChanged();
        }

    }

    private CustomNewDialog classDialog;  //合计对话框
    private ImageView closeImg; //关闭
    private TextView title;  //标题
    private RecyclerView myRecyclerView;
    private CustomDialogAdapter dialogAdapter;
    private RelativeLayout selectClassLayout;
    private RelativeLayout rl_out;
    private RelativeLayout rl_list_top;

    /**
     * 弹出选择授课班
     *
     */
    private void alertSelectClass() {
        initDialogView();
        initDialogListener();
        adapterDialogData();
    }

    private void adapterDialogData() {
        if (classItems != null && classItems.size() > 0) {
            dialogAdapter = new CustomDialogAdapter(mContext, classItems);
            myRecyclerView.setAdapter(dialogAdapter);
            dialogAdapter.setOnClassItemClickListener(this);
        }
    }

    private void initDialogView() {
        classDialog = new CustomNewDialog(mContext, R.layout.activity_order_dialog);
        closeImg = (ImageView) classDialog.findViewById(R.id.iv_close);
        title = (TextView) classDialog.findViewById(R.id.tv_title);
        myRecyclerView = (RecyclerView) classDialog.findViewById(R.id.recyclerView);
        selectClassLayout = (RelativeLayout) classDialog.findViewById(R.id.select_class_layout);
        rl_out = (RelativeLayout) classDialog.findViewById(R.id.rl_out);
        rl_list_top = (RelativeLayout) classDialog.findViewById(R.id.rl_list_top);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        title.setText(getResources().getString(R.string.select_teaching_class));
//        if(classItems.size() >= 5){
//            DisplayMetrics metric = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(metric);
//            int height = metric.heightPixels * 3/7;
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) selectClassLayout.getLayoutParams();
//            params.height = height;
//            selectClassLayout.setLayoutParams(params);
//        }
//        final Window dialogWindow = classDialog.getWindow();
//        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialogWindow.setGravity(Gravity.CENTER);
        classDialog.show();
    }

    private void initDialogListener() {
        //关闭
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (classDialog != null && classDialog.isShowing()) {
                    classDialog.cancel();
                }
            }
        });
        rl_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(classDialog != null && classDialog.isShowing()){
                    classDialog.cancel();
                }
            }
        });
        rl_list_top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

    }


    /**
     * 支付成功
     */
    @Override
    public void onOrderPaySuccess() {
        paySuccess(classId);
    }

    public void paySuccess(String classId) {
        Intent intent = new Intent();
        intent.putExtra("classId",classId);
        intent.setAction(Constants.PAY_SUCCESS_BACK);
        sendBroadcast(intent);
//        setResult(Constants.PAY_SUCCESS,intent);
        finish();
    }

    /**
     * 支付失败
     */
    @Override
    public void onOrderPayFail(String result) {
        if (TextUtils.equals(result, "6001")) {
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   if(!TextUtils.isEmpty(orderSn)){
                       Intent intent = new Intent(OrderConfirmationActivity.this,OrderDetailActivity.class);
                       intent.putExtra("orderSn",orderSn);
                       intent.putExtra("orderId",orderId);
                       intent.putExtra("courseId",courseId);
                       intent.putExtra("classId",classId);
                       startActivityForResult(intent,9000);
                       finish();
                   }
               }
           },300);
        }
    }
}
