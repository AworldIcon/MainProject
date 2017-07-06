package com.coder.kzxt.buildwork.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.activity.PublishWorkActivity;
import com.coder.kzxt.buildwork.adapter.WorkBankAdapter;
import com.coder.kzxt.buildwork.adapter.WorkBankDelegate;
import com.coder.kzxt.buildwork.entity.AlreadyPublishBean;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017/3/18.
 */

public class AllWorkBankFragment extends BaseFragment implements WorkBankDelegate.OnItemClickListener, WorkBankDelegate.OnItemLongClickListener, HttpCallBack {

    private View v;
    private MyPullRecyclerView work_recycler;
    private RelativeLayout activity_tea_check_work;
    private List<AlreadyPublishBean.ItemsBean> itemsBeanList=new ArrayList<>();
    private MyReceiver myReceiver;
    private TextView title;
    private int classId;
    private int courseId;
    private String testId="";
    private int workType;//"1"是作业，“0”是考试
    private String isCenter = "0";
    public static final int RequestCode = 1001;
    public static final int ResultCode = 1001;
    private Button load_fail_button;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private TextView no_info_text;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private WorkBankAdapter questionResourseAdapter;
    private WorkBankDelegate questionResourseDelegate;
    private AlreadyPublishBean publishBean;
    private ImageView no_info_img;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_work_first, container, false);
            Bundle bundle=getArguments();
            courseId = bundle.getInt("course_id", 0);
            classId = bundle.getInt("classId", 0);
            workType = bundle.getInt("workType",1);
            isCenter =bundle.getString(Constants.IS_CENTER);
            myReceiver=new MyReceiver();
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction("com.coder.kzxt.activity.TeaWorkRecourseActivity");
            intentFilter.addAction("com.coder.kzxt.activity.time");
            intentFilter.addAction("com.coder.kzxt.activity.PublishWorkActivity");
            getContext().registerReceiver(myReceiver,intentFilter);

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
        questionResourseDelegate=new WorkBankDelegate(getContext(),work_recycler,workType,courseId);
        questionResourseAdapter=new WorkBankAdapter(getContext(),itemsBeanList,questionResourseDelegate);
        work_recycler.setAdapter(questionResourseAdapter);
        questionResourseAdapter.setSwipeRefresh(myPullSwipeRefresh);
    }

    private void initEvents() {

        load_fail_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                load_fail_layout.setVisibility(View.GONE);
                no_info_layout.setVisibility(View.GONE);
                search_jiazai_layout.setVisibility(View.VISIBLE);
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
        work_recycler.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                questionResourseAdapter.addPageIndex();
                workData();
            }
        });
        questionResourseDelegate.setOnItemClickListener(this);
        questionResourseDelegate.setOnItemLongClickListener(this);
    }

    public void workData(){
        new HttpGetBuilder(getContext()).setHttpResult(this)
                .setUrl(UrlsNew.TEST_PAPER_PUBLISH)
                .setClassObj(AlreadyPublishBean.class)
                .addQueryParams("page",questionResourseAdapter.getPageIndex()+"")
                .addQueryParams("pageSize","20")
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("course_id",courseId+"")
                .addQueryParams("type",workType+"")
                .build();
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
        dialog(position);
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
        if(!new SharedPreferencesUtil(getContext()).getUid().trim().equals(itemsBeanList.get(position).getCreator().getId()+"")){
            intent.putExtra("detail","not_self");
        }
            intent.putExtra("workType",workType);
            intent.putExtra("status",itemsBeanList.get(position).getStatus());
            getActivity().startActivity(intent);


    }
    protected void dialog(final int po) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.custom_dialog);
        if(workType==1){
            builder.setMessage("确认复制此考试");
        }else {
            builder.setMessage("确认复制此作业");
        }
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                search_jiazai_layout.setVisibility(View.VISIBLE);
                copyWork(po);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void copyWork(final int po) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("id",itemsBeanList.get(po).getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpPostBuilder(getContext()).setUrl(UrlsNew.URLHeader+"/copy_test_paper").setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                search_jiazai_layout.setVisibility(View.GONE);
                questionResourseAdapter.resetPageIndex();
                workData();
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                search_jiazai_layout.setVisibility(View.GONE);
                if(code== Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
                    //重新登录
                    NetworkUtil.httpRestartLogin(getActivity(),activity_tea_check_work);
                }else {
                    //加载失败
                    load_fail_layout.setVisibility(View.VISIBLE);
                    NetworkUtil.httpNetErrTip(getActivity(),activity_tea_check_work);
                }
            }
        }).addBodyParam(jsonObject.toString()).build();
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
