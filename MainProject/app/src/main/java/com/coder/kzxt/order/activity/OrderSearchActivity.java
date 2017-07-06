package com.coder.kzxt.order.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.app.utils.Utils;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.course.adapter.SearchMoreCourseDelegate;
import com.coder.kzxt.course.beans.CourseBean;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.order.beans.MyOrderBean;
import com.coder.kzxt.order.beans.OrderPayment;
import com.coder.kzxt.order.context.OrderContext;
import com.coder.kzxt.order.delegate.AllOrderDelegate;
import com.coder.kzxt.order.mInterface.OnOrderInterface;
import com.coder.kzxt.order.mInterface.OrderPayResult;
import com.coder.kzxt.order.payImpl.AliPayStrategy;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SearchHistoryUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import com.coder.kzxt.video.beans.CourseClassBean;
import com.coder.kzxt.views.CustomNewDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * 订单搜索
 * Created by wangtingshun on 2017/4/13.
 */
public class OrderSearchActivity extends BaseActivity implements HttpCallBack,OnOrderInterface,OrderPayResult{

    private LinearLayout jiazai_layout;
    private MyRecyclerView listview;
    private ImageView leftImage;
    private TextView searchText;
    private TextView clear_history;
    private BaseRecyclerAdapter listAdapter;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;

    private LinearLayout no_info_layout;
    private ImageView no_info_img;
    private TextView no_info_text;

    private ScrollView scrollView;
    private RelativeLayout myLayout;
    private AllOrderDelegate courseDelegate;
    private PullRefreshAdapter<MyOrderBean.OrderBean> stageadapter;
    private EditText titleEdit;
    private String keyword, model_key, style;
    private List<String> historyList = new ArrayList<>();
    private List<MyOrderBean.OrderBean> data = new ArrayList<>();
    private SearchHistoryUtils searchHistoryUtils;
    public SharedPreferencesUtil spu;
    private CustomNewDialog orderDialog;
    private TextView cancleOrder; //取消订单
    private TextView cancle; //取消
    private Dialog asyDialog;
    private String pay_method_id;
    private String orderId; //订单id
    private String totalAmount; //总价
    private String amount; //优惠后的价格
    private String courseName; //课程名称
    private String pay_Method;
    private double total_Price; //总价
    private AliPayStrategy aliPayStrategy;
    private MyOrderBean.OrderBean orderBean;
    private String courseId;
    private List<String> history;
    private String orderSn;  //订单号
    private int pageSize = 20;
    private int pageNum; //总页数

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_all_common_search);
        spu = new SharedPreferencesUtil(this);
        model_key = getIntent().getStringExtra("model_key") != null ? getIntent().getStringExtra("model_key") : "";
        style = getIntent().getStringExtra("style") != null ? getIntent().getStringExtra("style") : "";
        leftImage = (ImageView) findViewById(R.id.leftImage);
        listview = (MyRecyclerView) findViewById(R.id.history_list);
        titleEdit = (EditText) findViewById(R.id.edit);
        searchText = (TextView) findViewById(R.id.searchText);
        clear_history = (TextView) findViewById(R.id.clear_history);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        no_info_img = (ImageView) findViewById(R.id.no_info_img);
        no_info_img.setImageResource(R.drawable.empty_order);
        no_info_text = (TextView) findViewById(R.id.no_info_text);
        no_info_text.setText("没有订单信息");
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.my_list);
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);

        //stage的初始化
        courseDelegate = new AllOrderDelegate(this);
//        orderListStageDelegate.model_key = model_key;
//        orderListStageDelegate.model_style = style;
        stageadapter = new PullRefreshAdapter(this, data, courseDelegate);
        myPullRecyclerView.setAdapter(stageadapter);
        stageadapter.setSwipeRefresh(myPullSwipeRefresh);
        courseDelegate.setOrderPayStateListener(OrderSearchActivity.this);

        searchHistoryUtils = new SearchHistoryUtils(this, Constants.SEARCH_LOCAL_COURSE);
        history = searchHistoryUtils.getHistory();
        if (!TextUtils.isEmpty(history.get(0))) {
            historyList.addAll(history);
            clear_history.setVisibility(View.VISIBLE);
        }
        listAdapter = new BaseRecyclerAdapter(OrderSearchActivity.this, historyList, new SearchMoreCourseDelegate(OrderSearchActivity.this, searchHistoryUtils, clear_history));
        listview.setAdapter(listAdapter);
        scrollView.setVisibility(View.VISIBLE);

        InintEvent();
        getPayMethod();
    }

    private void InintEvent() {
        leftImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideWindowSoftInput();
                setResult(90);
                OrderSearchActivity.this.finish();
            }
        });

        this.searchText.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                searchClick();
            }

        });

        titleEdit.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) && s.length() == 0) {
                    titleEdit.setTextColor(ContextCompat.getColor(OrderSearchActivity.this, R.color.font_gray));
                } else {
                    titleEdit.setTextColor(ContextCompat.getColor(OrderSearchActivity.this, R.color.font_black));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (listAdapter != null && listAdapter.getShowCount() != 0) {
                    scrollView.setVisibility(View.VISIBLE);
                    no_info_layout.setVisibility(View.GONE);
                }
            }
        });

        titleEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchClick();
                }
                return true;
            }
        });

        listAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                scrollView.setVisibility(View.GONE);
                keyword = (String) listAdapter.getData().get(position);
                listNotify(keyword);
                showLoadingView();
                ExecuteAsyncTask();
            }
        });

        clear_history.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                searchHistoryUtils.clearHistory();
                historyList.clear();
                listAdapter.resetData(historyList);
            }
        });

        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                stageadapter.resetPageIndex();
                ExecuteAsyncTask();

            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                // 直接请求服务器
                stageadapter.addPageIndex();
                ExecuteAsyncTask();
            }
        });

//        stageadapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        });
    }


    private void searchClick() {
        keyword = titleEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(keyword)) {
            scrollView.setVisibility(View.GONE);
            showLoadingView();
            listNotify(keyword);
            ExecuteAsyncTask();
        }

    }

    private void listNotify(String keyword) {
        listAdapter.resetData(searchHistoryUtils.resetHistory(keyword));
    }

    public void removeItem(String str) {
        searchHistoryUtils.deleteHistoryItem(str);
    }

    private void ExecuteAsyncTask() {
        titleEdit.clearFocus();

//        stageadapter.resetPageIndex();

                 new HttpGetBuilder(OrderSearchActivity.this)
                .setUrl(UrlsNew.GET_SEARCH_ALL_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        CourseBean courseBean = (CourseBean) resultBean;
                        ArrayList<CourseBean.Course> courses = courseBean.getItems();
                        String courseId = getCourseId(courses);
                        if (!TextUtils.isEmpty(courseId)) {
                            no_info_layout.setVisibility(View.GONE);
                            getSearchOrder(courseId);
                        } else {
                            no_info_layout.setVisibility(View.VISIBLE);
                            hideLoadingView();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderSearchActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(OrderSearchActivity.this, myLayout);
                        }
                        no_info_layout.setVisibility(View.VISIBLE);
                        hideLoadingView();
                        cancleLoadDialog();
                    }
                })
                .setClassObj(CourseBean.class)
                .addQueryParams("title", "all|" + keyword)
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("pageSize","200")
                .build();

    }

    /**
     * 获取搜索订单
     * @param courseId
     */
    private void getSearchOrder(String courseId) {
                new HttpGetBuilder(OrderSearchActivity.this)
                .setUrl(UrlsNew.GET_ORDER_LIST)
                .setHttpResult(OrderSearchActivity.this)
                .setClassObj(MyOrderBean.class)
                .addQueryParams("user_id",spu.getUid())
                .addQueryParams("item_id",courseId)
                .addQueryParams("item_type","1")
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("status","")
                .addQueryParams("fields","")
                .addQueryParams("page", stageadapter.getPageIndex() + "")
                .addQueryParams("pageSize",String.valueOf(pageSize))
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 4 && resultCode == 5) {
//            int position = data.getIntExtra("position", 0);
//            PostersBean bean = (PostersBean) data.getSerializableExtra("bean");
//            stageadapter.addData(position, bean);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 拼接courseId
     * @param courses
     * @return
     */
    private String getCourseId(ArrayList<CourseBean.Course> courses) {
        String courseId = null;
        if(courses != null && courses.size() > 0){
            for(int i = 0;i< courses.size();i++){
                CourseBean.Course course = courses.get(i);
                String id = course.getId();
                courseId += id+",";
            }
            if(!TextUtils.isEmpty(courseId)){
                return courseId.substring(4, courseId.length() - 1);
            }
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideWindowSoftInput();
            if (scrollView.getVisibility() == View.VISIBLE) {
                scrollView.setVisibility(View.GONE);
                return true;
            }
            setResult(90);
            finish();
        }
        return true;
    }

    public static void gotoActivity(Context context) {
        context.startActivity(new Intent(context, OrderSearchActivity.class));
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        cancleLoadDialog();
        hideLoadingView();
        MyOrderBean orderBean = (MyOrderBean) resultBean;
        pageNum = orderBean.getPaginate().getPageNum();
        int totalNum = orderBean.getPaginate().getTotalNum();
        stageadapter.setTotalPage(pageNum);
        stageadapter.setPullData(orderBean.getItems());

        if(totalNum == 0){
            no_info_layout.setVisibility(View.VISIBLE);
        } else {
            no_info_layout.setVisibility(View.GONE);
        }
        if (stageadapter.getTotalPage() == 0) {

        } else {
            // 有数据的处理
            scrollView.setVisibility(View.GONE);
            myPullSwipeRefresh.setVisibility(View.VISIBLE);
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(titleEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        cancleLoadDialog();
        hideLoadingView();
        Utils.makeToast(OrderSearchActivity.this, msg);
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
            NetworkUtil.httpRestartLogin(OrderSearchActivity.this, myLayout);
        } else {
            NetworkUtil.httpNetErrTip(OrderSearchActivity.this, myLayout);
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideWindowSoftInput(){
        final View v = getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void laodProgressDialog(){
        asyDialog = MyPublicDialog.createLoadingDialog(this);
        asyDialog.show();
    }


    /**
     * 取消加载框
     */
    private void cancleLoadDialog(){
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
    public void onOrderCancle(MyOrderBean.OrderBean bean, PullRefreshDelegate delegate) {
        orderSelectDialog(bean,delegate);
    }

    private void orderSelectDialog(MyOrderBean.OrderBean bean, PullRefreshDelegate delegate) {
        orderDialog = new CustomNewDialog(this, R.layout.order_select_item);
        cancleOrder = (TextView) orderDialog.findViewById(R.id.tv_cancle_order);
        cancle = (TextView) orderDialog.findViewById(R.id.tv_cancle);
        Window dialogWindow = orderDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        orderDialog.show();
        initAfterDialogListener(bean,delegate);
    }

    private void initAfterDialogListener(final MyOrderBean.OrderBean bean,final PullRefreshDelegate delegate) {
        //取消订单
        cancleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orderDialog != null && orderDialog.isShowing()){
                    orderDialog.cancel();
                }
                laodProgressDialog();
                cancleOrderTask(bean,delegate);
            }
        });
        //取消
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orderDialog != null && orderDialog.isShowing()){
                    orderDialog.cancel();
                }
            }
        });
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
                        ExecuteAsyncTask();
                        ToastUtils.makeText(OrderSearchActivity.this,getResources().getString(R.string.cancel_success), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleLoadDialog();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderSearchActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(OrderSearchActivity.this, myLayout);
                        }
                        ToastUtils.makeText(OrderSearchActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(null)
             //   .addBodyParam("status", "5")  //"订单状态: 1未付款(created) 2已付款(paid) 3已过期(overdue) 4已退款(refunded) 5已取消(cancelled) 6分期中"
                .addBodyParam("action","cancle")  //暂时写死 ，取消订单
                .setPath(bean.getId())
                .build();
    }


    /**
     * 立即支付
     * @param bean
     */
    @Override
    public void onOrderPay(MyOrderBean.OrderBean bean) {
        this.orderBean = bean;
        orderId = bean.getId();
        orderSn = bean.getSn();
        amount = bean.getAmount();
        totalAmount = bean.getTotal_price();
        courseName = bean.getCourse().getTitle();
        courseId = bean.getCourse().getId();
        total_Price = Double.parseDouble(bean.getCourse_class().getPrice());
        if (!TextUtils.isEmpty(pay_Method) && !TextUtils.isEmpty(orderSn)) {
            laodProgressDialog();
            getCourseClassMember();
        }
    }

    private void payTask() {
        aliPayStrategy = new AliPayStrategy(OrderSearchActivity.this, courseName,
                myLayout, orderSn, pay_Method, total_Price, total_Price);
        aliPayStrategy.setOnPayResultListener(OrderSearchActivity.this);
        OrderContext context = new OrderContext(aliPayStrategy);
        context.orderPayMethod();
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
     * 点击item
     * @param bean
     */
    @Override
    public void onOrderItem(MyOrderBean.OrderBean bean) {
        Intent intent = new Intent(OrderSearchActivity.this,OrderDetailActivity.class);
        intent.putExtra("orderBean",bean);
        startActivity(intent);
    }

    /**
     * 支付成功
     */
    @Override
    public void onOrderPaySuccess() {
        laodProgressDialog();
        ExecuteAsyncTask();
    }

    /**
     * 支付失败
     */
    @Override
    public void onOrderPayFail(String result) {
        if (TextUtils.equals(result, "6001")) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(OrderSearchActivity.this,OrderDetailActivity.class);
//                    intent.putExtra("orderId",orderId);
//                    intent.putExtra("courseId",courseId);
//                    intent.putExtra("classId",classId);
//                    startActivity(intent);
//                }
//            },300);
        }
    }

    public void getCourseClassMember(){
                new HttpGetBuilder(OrderSearchActivity.this)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        cancleLoadDialog();
                        CourseClassBean courseClassBean = (CourseClassBean) resultBean;
                        if (courseClassBean.getItems().size() == 0) {
                            payTask();
                        } else {
                            ToastUtils.makeText(OrderSearchActivity.this,"你已加入该课程",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleLoadDialog();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(OrderSearchActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(OrderSearchActivity.this, myLayout);
                        }
                    }
                })
                .setClassObj(CourseClassBean.class)
                .setUrl(UrlsNew.GET_COURSE_CLASS_MEMBER)
                .addQueryParams("user_id", spu.getUid())
                .addQueryParams("course_id", courseId)
                .build();
    }
}
