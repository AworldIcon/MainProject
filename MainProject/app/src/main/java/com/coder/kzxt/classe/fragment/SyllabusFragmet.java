package com.coder.kzxt.classe.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.adapter.SyllabusFragmentAdapter;
import com.coder.kzxt.classe.beans.CourseTableResult;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import java.util.ArrayList;

/**
 * 班级课程表
 * Created by wangtingshun on 2017/3/16.
 */

public class SyllabusFragmet extends BaseFragment {

    private RecyclerView recyclerView;
    private Context mContext;
    private View rootView;
    private SharedPreferencesUtil spu;
    private CourseTableResult.DayBean dayBean;
    private SyllabusFragmentAdapter adapter;
    private LinearLayout jiazai_layout;
    private LinearLayout no_info_layout;
    private Button load_fail_button;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(mContext);
        dayBean = (CourseTableResult.DayBean) getArguments().getSerializable("syllabus");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = LayoutInflater.from(mContext).inflate(R.layout.class_syllabus_fragment_layout,container,false);
            initView();
            loadingLayout();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if(parent != null){
           parent.removeView(rootView);
        }
        return rootView;
    }

    private void initView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        jiazai_layout = (LinearLayout) rootView.findViewById(R.id.jiazai_layout);
        no_info_layout = (LinearLayout) rootView.findViewById(R.id.no_info_layout);
        load_fail_button = (Button) rootView.findViewById(R.id.load_fail_button);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    protected void requestData() {
        visibleDataPage();
        ArrayList<CourseTableResult.DayBean.Day> list = dayBean.getList();
        if(list != null && list.size() > 0){
            adapter = new SyllabusFragmentAdapter(mContext,dayBean);
            recyclerView.setAdapter(adapter);
        } else {
            noDataPage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 暂无数据页面
     */
    public void noDataPage(){
        no_info_layout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
    }

    /**
     * 加载页
     */
    public void loadingLayout(){
        recyclerView.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.VISIBLE);
        no_info_layout.setVisibility(View.GONE);
    }

    /**
     * 显示数据
     */
    public void visibleDataPage(){
        recyclerView.setVisibility(View.VISIBLE);
        no_info_layout.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
    }

}
