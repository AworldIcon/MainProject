package com.coder.kzxt.buildwork.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.activity.PublishWorkActivity;
import com.coder.kzxt.buildwork.adapter.QuestionResourseAdapter;
import com.coder.kzxt.buildwork.adapter.QuestionResourseDelegate;
import com.coder.kzxt.buildwork.entity.AlreadyPublishBean;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import java.util.ArrayList;
import java.util.List;
import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by pc on 2017/3/18.
 */

public class AlreadyPublishFragment extends BaseFragment implements QuestionResourseDelegate.OnItemClickListener, QuestionResourseDelegate.OnItemLongClickListener, HttpCallBack {
    private View v;
    private MyPullRecyclerView work_recycler;
    private RelativeLayout activity_tea_check_work;
    private List<AlreadyPublishBean.ItemsBean> itemsBeanList=new ArrayList<>();
    private MyReceiver myReceiver;
    private int classId;
    private int courseId;
    private String testId="";
    private int workType;//"1"是作业，“0”是考试
    private String isCenter = "0";
    private Button load_fail_button;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private ImageView no_info_img;
    private TextView no_info_text;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private QuestionResourseAdapter questionResourseAdapter;
    private QuestionResourseDelegate questionResourseDelegate;
    private AlreadyPublishBean publishBean;
    private EditText search_edittext;
    private ImageView search_image;
    private RelativeLayout search_layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_work_first, container, false);
            Bundle bundle=getArguments();
            myReceiver=new MyReceiver();
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction("com.coder.kzxt.activity.TeaWorkRecourseActivity");
            intentFilter.addAction("com.coder.kzxt.activity.time");
            intentFilter.addAction("com.coder.kzxt.activity.PublishWorkActivity");
            getContext().registerReceiver(myReceiver,intentFilter);
            courseId = bundle.getInt("course_id", 0);
            classId = bundle.getInt("classId", 0);
            workType = bundle.getInt("workType",1);
            isCenter =bundle.getString(Constants.IS_CENTER);

            initViews();
            workData();
            initEvents();
        }
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }
        return v;

    }

    private void initViews() {
        search_edittext= (EditText) v.findViewById(R.id.search_edittext);
        search_image= (ImageView) v.findViewById(R.id.search_image);
        activity_tea_check_work= (RelativeLayout) v.findViewById(R.id.work_first);
        no_info_text = (TextView) v.findViewById(R.id.no_info_text);
        search_jiazai_layout = (LinearLayout) v.findViewById(R.id.jiazai_layout);
        search_jiazai_layout.setVisibility(View.VISIBLE);
        load_fail_layout = (LinearLayout) v.findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) v.findViewById(R.id.load_fail_button);
        no_info_layout = (LinearLayout) v.findViewById(R.id.no_info_layout);
        no_info_img= (ImageView) v.findViewById(R.id.no_info_img);
        no_info_img.setImageResource(R.drawable.empty_work_info);
        if(workType==1){
            no_info_text.setText(R.string.no_exame);
        }else {
            no_info_text.setText(R.string.no_homework);

        }
        myPullSwipeRefresh= (MyPullSwipeRefresh) v.findViewById(R.id.myPullSwipeRefresh);
        work_recycler= (MyPullRecyclerView) v.findViewById(R.id.work_recycler);
        questionResourseDelegate=new QuestionResourseDelegate(getContext(),work_recycler,courseId,workType);
        questionResourseAdapter=new QuestionResourseAdapter(getContext(),itemsBeanList,questionResourseDelegate);
        work_recycler.setAdapter(questionResourseAdapter);
        questionResourseAdapter.setSwipeRefresh(myPullSwipeRefresh);
    }
    private void search() {

        String searchContext = search_edittext.getText().toString().trim();
        if (TextUtils.isEmpty(searchContext)) {
            //(this, "输入框为空，请输入");
        } else {
            // 调用搜索的API方法
           // finishWorkAdapter.notifyDataSetChanged();
        }
    }
    private void initEvents() {
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search_image.setVisibility(View.GONE);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)){
                    search_image.setVisibility(View.VISIBLE);
                }else {
                    search_image.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        search_edittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    search();
                }
                return false;
            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                search_jiazai_layout.setVisibility(View.VISIBLE);
                load_fail_layout.setVisibility(View.GONE);
                no_info_layout.setVisibility(View.GONE);
                workData();
            }
        });
        work_recycler.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                questionResourseAdapter.addPageIndex();
                workData();
            }
        });
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                questionResourseAdapter.resetPageIndex();
                workData();
            }
        });

        questionResourseDelegate.setOnItemClickListener(this);
        questionResourseDelegate.setOnItemLongClickListener(this);

    }
    public void workData(){
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        new HttpGetBuilder(getContext()).setHttpResult(this)
                .setUrl(UrlsNew.TEST_PAPER_PUBLISH)
                .setClassObj(AlreadyPublishBean.class)
                .addQueryParams("page",questionResourseAdapter.getPageIndex()+"")
                .addQueryParams("pageSize","20")
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("course_id",courseId+"")
                .addQueryParams("type",workType+"")
                .addQueryParams("status","3")
                .build();
//        HttpGetOld httpGet=new HttpGetOld(getContext(),this, new InterfaceHttpResult() {
//            @Override
//            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//                search_jiazai_layout.setVisibility(View.GONE);
//                if(code==Constants.HTTP_CODE_1000){
//                    WorkManage workManage= (WorkManage) baseBean;
//
//                    if (workManage.getData().size() != 0) {
//                        no_info_layout.setVisibility(View.GONE);
//                    } else {
//                        no_info_layout.setVisibility(View.VISIBLE);
//                    }
//                }else if(code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
//                    //重新登录
//                    NetworkUtil.httpRestartLogin(getActivity(),activity_tea_check_work);
//                }else {
//                    //加载失败
//                    load_fail_layout.setVisibility(View.VISIBLE);
//                    NetworkUtil.httpNetErrTip(getContext(),activity_tea_check_work);
//                }
//            }
//        }, WorkManage.class, Urls.GET_COURSE_TEST_LIST,String.valueOf(4654),String.valueOf(5169),"1",isCenter);
//        httpGet.excute();
    }
    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        search_jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        publishBean= (AlreadyPublishBean) resultBean;
        questionResourseAdapter.setTotalPage(publishBean.getPaginate().getPageNum());
        questionResourseAdapter.setPullData(publishBean.getItems());
        if(questionResourseAdapter.getData().size()==0){
              no_info_layout.setVisibility(View.VISIBLE);
        }else {
            no_info_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        search_jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        if (code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
                    //重新登录
                    NetworkUtil.httpRestartLogin(getActivity(),activity_tea_check_work);
                }else {
                    ToastUtils.makeText(getContext(),msg);
                    //加载失败
                    load_fail_layout.setVisibility(View.VISIBLE);
                    NetworkUtil.httpNetErrTip(getContext(),activity_tea_check_work);
        }
    }
    @Override
    public void onItemLongClick(int position) {
    }

    @Override
    public void onItemClick(int position) {
        String coursId=courseId+"";
        String testid=itemsBeanList.get(position).getId()+"";
        String name=itemsBeanList.get(position).getTitle();
        Intent intent=new Intent(getContext(),PublishWorkActivity.class);
        intent.putExtra("testId",testid);
        intent.putExtra("courseId",coursId);
        intent.putExtra("title",name);
        intent.putExtra("workType",workType);
        if(!new SharedPreferencesUtil(getContext()).getUid().trim().equals(itemsBeanList.get(position).getCreator().getId()+"")){
            intent.putExtra("detail","not_self");
        }
        intent.putExtra("status",3);//此页面都是已发布的
        intent.putExtra("no_right",1);//此页面都是已发布的
        startActivity(intent);

    }





    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.coder.kzxt.activity.time")){
                no_info_layout.setVisibility(View.GONE);
                workData();
            }
            if(intent.getAction().equals("com.coder.kzxt.activity.TeaWorkRecourseActivity")||intent.getAction().equals("com.coder.kzxt.activity.PublishWorkActivity")){
                no_info_layout.setVisibility(View.GONE);
                workData();
            }

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(myReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Constants.LOGIN_BACK){
            workData();
        }
    }
    @Override
    protected void requestData() {

    }
    public void refreshData(){
        search_jiazai_layout.setVisibility(View.VISIBLE);
        workData();
    }
    
}
