package com.coder.kzxt.course.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.course.beans.TeachBean;
import com.coder.kzxt.course.beans.TeachCourseBean;
import com.coder.kzxt.course.delegate.TeachCourseAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 在教课程
 */

public class TeacherCourseFragmentNew extends BaseFragment implements HttpCallBack {

    private View rootView;
    private ArrayList<TeachBean.TeachCourseItem> courseList = new ArrayList<>();
    private RelativeLayout myLayout;
    private SharedPreferencesUtil spu;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button loadFailBtn;//加载失败

    private ExpandableListView my_list;
    private List<TeachCourseBean> mData;
    private TeachCourseAdapter teachCourseAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(getActivity());
        mData = new ArrayList<>();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_course_new_class_layout, container, false);
        }

        initView();
        initListener();

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(parent);
        }
        return rootView;
    }

    private void initView() {
        jiazai_layout = (LinearLayout) rootView.findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) rootView.findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) rootView.findViewById(R.id.no_info_layout);
        myLayout = (RelativeLayout) rootView.findViewById(R.id.rl_layout);
        loadFailBtn = (Button) rootView.findViewById(R.id.load_fail_button);
        my_list = (ExpandableListView) rootView.findViewById(R.id.my_list);

    }

    private void initListener() {

        //重新加载
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPage();
                httpRequesData();
            }
        });

        my_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                TeachBean.TeachCourseItem teachCourseItem =  mData.get(groupPosition).getOpenCourses().get(childPosition);
                Intent intent = new Intent(getActivity(), VideoViewActivity.class);
                intent.putExtra("flag", Constants.ONLINE);
                intent.putExtra("treeid", teachCourseItem.getId());
                intent.putExtra("tree_name", teachCourseItem.getTitle());
                intent.putExtra("pic", teachCourseItem.getMiddle_pic());
                intent.putExtra(Constants.IS_CENTER, "0");
                startActivity(intent);
                return false;
            }
        });

    }

    @Override
    protected void requestData() {
        loadingPage();
        httpRequesData();
    }


    private void httpRequesData(){

        //获取已发布的在教课程
        new HttpGetBuilder(getActivity())
                .setUrl(UrlsNew.GET_COURSE_TEACHERING)
                .setClassObj(TeachBean.class)
                .setHttpResult(this)
                .setRequestCode(1000)
                .addQueryParams("page", "1")
                .addQueryParams("user_id", spu.getUid())
                .addQueryParams("status", "2")
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("pageSize", "40")
                .build();

    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if(requestCode==1000){
            TeachCourseBean teachCourseBean = new TeachCourseBean();
            teachCourseBean.setType("openCourse");
            TeachBean teachBean = (TeachBean) resultBean;
            if(teachBean.getItems().size()!=0){
                teachCourseBean.setOpenCourses(teachBean.getItems());
                mData.add(teachCourseBean);
            }


            //获取待发布的在教课程
            new HttpGetBuilder(getActivity())
                    .setUrl(UrlsNew.GET_COURSE_TEACHERING)
                    .setClassObj(TeachBean.class)
                    .setRequestCode(1001)
                    .setHttpResult(this)
                    .addQueryParams("page", "1")
                    .addQueryParams("user_id", spu.getUid())
                    .addQueryParams("status", "")
                    .addQueryParams("orderBy","create_time desc")
                    .addQueryParams("pageSize", "40")
                    .build();

        }

        if(requestCode==1001){
            TeachCourseBean teachCourseBean = new TeachCourseBean();
            teachCourseBean.setType("closeCourse");
            TeachBean teachBean = (TeachBean) resultBean;
            if(teachBean.getItems().size()!=0){
                teachCourseBean.setOpenCourses(combinateData(teachBean.getItems()));
                mData.add(teachCourseBean);
            }
            visibleData();
            if(mData.size()==0){
                noDataPage();
            }else {
                teachCourseAdapter = new TeachCourseAdapter(getActivity(),mData);
                my_list.setAdapter(teachCourseAdapter);
                for (int i = 0; i < mData.size(); i++) {
                    my_list.expandGroup(i);
                }
                int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                //设置默认箭头位置
                my_list.setIndicatorBounds(width - 80, width - 10);
            }

        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
            NetworkUtil.httpRestartLogin(TeacherCourseFragmentNew.this, myLayout);
        } else {
            NetworkUtil.httpNetErrTip(getActivity(), myLayout);
        }
        loadFailPage();

    }

    private ArrayList<TeachBean.TeachCourseItem> combinateData(ArrayList<TeachBean.TeachCourseItem> items) {
        if(items != null && items.size() > 0){
            for (int i = 0 ; i < items.size(); i++){
                TeachBean.TeachCourseItem courseItem = items.get(i);
                if(courseItem.getStatus().equals("0") || courseItem.getStatus().equals("1")){
                    courseList.add(courseItem);
                }
            }
        }
        return courseList;
    }


    /**
     * 暂无数据
     */
    private void noDataPage() {
        load_fail_layout.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        load_fail_layout.setVisibility(View.VISIBLE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    /**
     * 显示数据
     */
    private void visibleData() {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    /**
     * 显示加载页
     */
    private void loadingPage() {
        jiazai_layout.setVisibility(View.VISIBLE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }
}
