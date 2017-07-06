package com.coder.kzxt.classe.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpGetOld;
import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.activity.ApplyListActivity;
import com.coder.kzxt.classe.activity.ApplyMemberInfo;
import com.coder.kzxt.classe.adapter.StudentApplyAdapter;
import com.coder.kzxt.classe.beans.ClassApplyResult;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Urls;
import com.coder.kzxt.views.CustomNewDialog;

import java.util.ArrayList;

/**
 * 学生申请列表
 * Created by wangtingshun on 2017/3/18.
 */

public class StudentApplyFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,StudentApplyAdapter.OnItemClickCallBack{

    private ApplyListActivity mContext;
    private View rootView;
    private MyPullRecyclerView recyclerView;
    private String classId;
    private String className;
    private StudentApplyAdapter applyAdapter;
    private MyPullSwipeRefresh swipeRefresh;
    private RelativeLayout myLayout;
    private String status = "3";//类型：0 全部 ，3 未审核，2 未通过, 1.已通过
    private String reason = ""; //理由
    private String positionId = "1"; //申请用户角色：0 全部 ，1 学生，2 老师
    private SharedPreferencesUtil spu;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (ApplyListActivity)context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(mContext);
        classId = getArguments().getString("classId");
        className = getArguments().getString("className");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView =  LayoutInflater.from(mContext).inflate(R.layout.apply_list_layout,container,false);
            initView();
        }
        initListener();
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initView() {
        myLayout = (RelativeLayout) rootView.findViewById(R.id.my_layout);
        swipeRefresh = (MyPullSwipeRefresh) rootView.findViewById(R.id.myPullSwipeRefresh);
        recyclerView = (MyPullRecyclerView) rootView.findViewById(R.id.recyclerView);
        jiazai_layout = (LinearLayout) rootView.findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) rootView.findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) rootView.findViewById(R.id.no_info_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void initListener() {
        swipeRefresh.setOnRefreshListener(StudentApplyFragment.this);

    }

    @Override
    protected void requestData() {
        showLoadingView(rootView);
        requestTask();
    }

    private void requestTask() {
        status = "3";
        HttpGetOld httpget = new HttpGetOld(mContext,this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                hideLoadingView();
                swipeRefresh.setRefreshing(false);
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    ClassApplyResult classApply = (ClassApplyResult) baseBean;
                    ArrayList<ClassApplyResult.ClassApplyBean> classApplys = classApply.getData();
                    if(classApplys.size() > 0){
                        adapterListData(classApplys);
                        swipeRefresh.setVisibility(View.VISIBLE);
                        no_info_layout.setVisibility(View.GONE);
                    } else {
                        no_info_layout.setVisibility(View.VISIBLE);
                        swipeRefresh.setVisibility(View.GONE);
                    }
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(mContext, myLayout);
                    if(!NetworkUtil.isNetworkAvailable(mContext)){
                        showLoadFailView(myLayout);
                        swipeRefresh.setVisibility(View.GONE);
                        setOnLoadFailClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestData();
                            }
                        });
                    }
                } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    NetworkUtil.httpRestartLogin(StudentApplyFragment.this, myLayout);
                    no_info_layout.setVisibility(View.VISIBLE);
                    swipeRefresh.setVisibility(View.GONE);
                } else {
                    no_info_layout.setVisibility(View.VISIBLE);
                    swipeRefresh.setVisibility(View.GONE);
                }
            }
        }, ClassApplyResult.class, Urls.GET_CLASS_APPLY_LIST_ACTION, classId,status,positionId,"20","1");
        httpget.excute(1000);
    }

    private void adapterListData(ArrayList<ClassApplyResult.ClassApplyBean> classApplys) {
        applyAdapter = new StudentApplyAdapter(mContext,classApplys);
        recyclerView.setAdapter(applyAdapter);
        applyAdapter.setOnItemClickListener(StudentApplyFragment.this);
    }

    @Override
    public void onRefresh() {
        requestTask();
    }

    /**
     * 点击item
     * @param applyBean
     * @param position
     */
    @Override
    public void onItemClick(ClassApplyResult.ClassApplyBean applyBean,int position) {
        Intent intent = new Intent();
        intent.setClass(mContext, ApplyMemberInfo.class);
        intent.putExtra("applyBean", applyBean);
        mContext.startActivityForResult(intent, Constants.APPLY_REQUEST_CODE);

    }

    /**
     * 点击同意
     */
    @Override
    public void onClickAgreeButton(ClassApplyResult.ClassApplyBean applyBean) {
        status = "1";
        subitCheckOption(applyBean);
    }

    /**
     * 提交审核操作
     */
    private void subitCheckOption(ClassApplyResult.ClassApplyBean applyBean) {
        showLoadingView(rootView);
        new HttpPostOld(mContext, StudentApplyFragment.this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    requestTask();
                    spu.setApplyNum(String.valueOf(Integer.parseInt(spu.getApplyNum())-1));
                    ToastUtils.makeText(getActivity(), getResources().getString(R.string.opera_success), Toast.LENGTH_SHORT).show();
                }else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(mContext, myLayout);
                    if(!NetworkUtil.isNetworkAvailable(mContext)){
                        showLoadFailView(myLayout);
                        setOnLoadFailClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestData();
                            }
                        });
                    }
                } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    NetworkUtil.httpRestartLogin(StudentApplyFragment.this, myLayout);
                    no_info_layout.setVisibility(View.VISIBLE);
                } else {
                    hideLoadingView();
                    no_info_layout.setVisibility(View.VISIBLE);
                    ToastUtils.makeText(getActivity(),getResources().getString(R.string.opera_fail), Toast.LENGTH_SHORT).show();
                }
            }
        }, null, Urls.POST_CHECK_CLASS_APPLY_ACTION,applyBean.getId(),status,reason).excute(1000);
    }

    /**
     * 判断是否填写数据
     * @param refuseReason
     * @param customNewDialog
     */
    private void judgeInfo(EditText refuseReason, CustomNewDialog customNewDialog,
                           ClassApplyResult.ClassApplyBean applyBean) {
        String reason = refuseReason.getText().toString().trim();
        if (reason.length() > 50) {
            refuseReason.setError(getResources().getString(R.string.refuse_num_fifty));
            refuseReason.setHintTextColor(getResources().getColor(R.color.font_red));
            return;
        }
        if(customNewDialog !=  null && customNewDialog.isShowing()){
            customNewDialog.cancel();
        }
        submitData(applyBean);
    }

    public void submitData(ClassApplyResult.ClassApplyBean applyBean) {
        status = "2";
        subitCheckOption(applyBean);
    }

    /**
     * 点击拒绝
     */
    @Override
    public void onClickRefusedButton(final ClassApplyResult.ClassApplyBean applyBean) {
        final CustomNewDialog customNewDialog=new CustomNewDialog(getActivity(),R.layout.refuse);
        final EditText refuseReason=(EditText)customNewDialog.findViewById(R.id.refuse_resaon);
        TextView cancle = (TextView) customNewDialog.findViewById(R.id.cancle);
        TextView confirm=(TextView)customNewDialog.findViewById(R.id.confirm);
        customNewDialog.show();
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customNewDialog != null && customNewDialog.isShowing()) {
                    customNewDialog.cancel();
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeInfo(refuseReason,customNewDialog,applyBean);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
