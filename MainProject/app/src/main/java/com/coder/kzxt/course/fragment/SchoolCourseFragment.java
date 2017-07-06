package com.coder.kzxt.course.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.adapter.SchoolCourseAdapter;
import com.coder.kzxt.course.beans.LearnBean;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.ArrayList;
import java.util.List;

public class SchoolCourseFragment extends BaseFragment implements HttpCallBack {

    private SharedPreferencesUtil spu;
    private View v;
    private ExpandableListView myListView;
    private LinearLayout jiazai_layout;
    private LinearLayout no_info_layout;
    private LinearLayout load_fail_layout;
    private Button load_fail_button;
    private RelativeLayout my_layout;
    private ArrayList<String> groups;
    private ArrayList<List<LearnBean.ItemsBean>> childs;
    private SchoolCourseAdapter courseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(getActivity());
        groups = new ArrayList<>();
        childs = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_school_course, container, false);
            myListView = (ExpandableListView) v.findViewById(R.id.myListView);
            jiazai_layout = (LinearLayout) v.findViewById(R.id.jiazai_layout);
            jiazai_layout.setVisibility(View.VISIBLE);
            no_info_layout = (LinearLayout) v.findViewById(R.id.no_info_layout);
            load_fail_layout = (LinearLayout) v.findViewById(R.id.load_fail_layout);
            load_fail_button = (Button) v.findViewById(R.id.load_fail_button);
            my_layout = (RelativeLayout) v.findViewById(R.id.my_layout);
        }

        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_fail_layout.setVisibility(View.GONE);
                jiazai_layout.setVisibility(View.VISIBLE);
                request();
            }
        });

        myListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        myListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                LearnBean.ItemsBean courseBean = childs.get(groupPosition).get(childPosition);
                Intent intent = new Intent(getActivity(), VideoViewActivity.class);
                intent.putExtra("flag", Constants.ONLINE);
                intent.putExtra("treeid", courseBean.getId());
                intent.putExtra("tree_name", courseBean.getTitle());
                intent.putExtra("pic", courseBean.getMiddle_pic());
                intent.putExtra(Constants.IS_CENTER, courseBean.getSource());
                startActivity(intent);
                return false;
            }
        });


        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }
        return v;
    }


    @Override
    protected void requestData() {
        request();

    }

    private void request(){

        new HttpGetBuilder(getActivity())
                .setHttpResult(this)
                .setClassObj(LearnBean.class)
                .setUrl(UrlsNew.GET_COURSE_LEARNING)
                .addQueryParams("user_id",spu.getUid())
                .addQueryParams("orderBy", "create_time desc")
                .build();
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        jiazai_layout.setVisibility(View.GONE);
        LearnBean courseResultBean = (LearnBean) resultBean;
        List<LearnBean.ItemsBean> courseBeens = courseResultBean.getItems();
        if(courseBeens.size()==0){
             no_info_layout.setVisibility(View.VISIBLE);
             return;
        }
        List<LearnBean.ItemsBean> payList = new ArrayList<>();
        List<LearnBean.ItemsBean> freeList = new ArrayList<>();
        for(int i=0;i<courseBeens.size();i++){
            int  isfree  = courseBeens.get(i).getIs_free();
            if(isfree==1){
                payList.add(courseBeens.get(i));
                childs.add(payList);
            }else {
                freeList.add(courseBeens.get(i));
                childs.add(freeList);
            }
        }

         if(payList.size()!=0){
             groups.add(getResources().getString(R.string.pay_course));
         }

         if(freeList.size()!=0){
            groups.add(getResources().getString(R.string.free_course));
         }

        courseAdapter = new SchoolCourseAdapter(getActivity(),groups,childs);
        myListView.setAdapter(courseAdapter);

        for(int i=0;i<groups.size();i++){
            myListView.expandGroup(i);
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        load_fail_layout.setVisibility(View.VISIBLE);
        jiazai_layout.setVisibility(View.GONE);
        NetworkUtil.httpNetErrTip(getActivity(),my_layout);
        if(code== Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
            NetworkUtil.httpRestartLogin(SchoolCourseFragment.this,my_layout);
        }
    }
}
