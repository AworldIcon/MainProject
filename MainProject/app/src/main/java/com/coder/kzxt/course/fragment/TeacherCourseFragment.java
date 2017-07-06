//package com.coder.kzxt.course.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ExpandableListView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import com.app.http.HttpCallBack;
//import com.app.http.builders.HttpGetBuilder;
//import com.coder.kzxt.activity.R;
//import com.coder.kzxt.base.fragment.BaseFragment;
//import com.coder.kzxt.course.adapter.TeachAdapter;
//import com.coder.kzxt.course.beans.CourseBean;
//import com.coder.kzxt.course.beans.TeachBean;
//import com.coder.kzxt.course.beans.TeachResult;
//import com.coder.kzxt.course.delegate.CourseListDelegate;
//import com.coder.kzxt.course.delegate.PublicFirstLevel;
//import com.coder.kzxt.recyclerview.MyPullRecyclerView;
//import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
//import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
//import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
//import com.coder.kzxt.recyclerview.adapter.TreeRecyclerViewAdapter;
//import com.coder.kzxt.recyclerview.adapter.TreeRecyclerViewType;
//import com.coder.kzxt.utils.Constants;
//import com.coder.kzxt.utils.NetworkUtil;
//import com.coder.kzxt.utils.SharedPreferencesUtil;
//import com.app.http.UrlsNew;
//import com.coder.kzxt.video.activity.VideoViewActivity;
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * 在教课程
// */
//
//public class TeacherCourseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
//        MyPullRecyclerView.OnAddMoreListener {
//
//    private Context mContext;
//    private View rootView;
//    private MyPullSwipeRefresh swipeRefresh;
//
//    private MyPullRecyclerView recyclerView;
//    private TreeRecyclerViewAdapter treeRecyclerViewAdapter;
//
//    private LinearLayout jiazai_layout;
//    private LinearLayout load_fail_layout;
//    private LinearLayout no_info_layout;
//    private PullRefreshAdapter pullRefreshAdapter;
//    private CourseListDelegate delegate;
//    private SharedPreferencesUtil spu;
//    private RelativeLayout myLayout;
//    private Button loadFailBtn;//加载失败
//    private List<CourseBean.Course> data = new ArrayList<>();
//    private int pageNum;
//    private int pageIndex = 1;
//    private TeachResult result;
//    private ArrayList<TeachResult> teachList = new ArrayList<>();
//    private ArrayList<TeachBean.TeachCourseItem> itemList = null;
//    private ExpandableListView expandableListView;
//    private TeachAdapter teachAdapter;
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.mContext = context;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        spu = new SharedPreferencesUtil(mContext);
//
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.activity_course_and_class_layout, container, false);
//            initView();
//        }
//        initData();
//        initListener();
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(parent);
//        }
//        return rootView;
//    }
//
//    private void initData() {
//        delegate = new CourseListDelegate(mContext,"");
//        pullRefreshAdapter = new PullRefreshAdapter(mContext, data, delegate);
//        recyclerView.setAdapter(pullRefreshAdapter);
//        pullRefreshAdapter.setSwipeRefresh(swipeRefresh);
//    }
//
//    private void initView() {
//        recyclerView = (MyPullRecyclerView) rootView.findViewById(R.id.recyclerView);
//        swipeRefresh = (MyPullSwipeRefresh) rootView.findViewById(R.id.myPullSwipeRefresh);
//        jiazai_layout = (LinearLayout) rootView.findViewById(R.id.jiazai_layout);
//        load_fail_layout = (LinearLayout) rootView.findViewById(R.id.load_fail_layout);
//        no_info_layout = (LinearLayout) rootView.findViewById(R.id.no_info_layout);
//        myLayout = (RelativeLayout) rootView.findViewById(R.id.rl_layout);
//        loadFailBtn = (Button) rootView.findViewById(R.id.load_fail_button);
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        //设置Item增加、移除动画
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
////        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expand_list_view);
////        expandableListView.setGroupIndicator(null);
//    }
//
//    private void initListener() {
//        swipeRefresh.setOnRefreshListener(TeacherCourseFragment.this);
//        recyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
//            @Override
//            public void addMoreListener() {
//                pullRefreshAdapter.addPageIndex();
//                pageIndex = pullRefreshAdapter.getPageIndex();
//                if (pageIndex == pageNum) {
//                    pullRefreshAdapter.setPullData(null);
//                }
//                getTeachingCourse();
//            }
//        });
//
//        //重新加载
//        loadFailBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadingPage();
//                getTeachingCourse();
//            }
//        });
//
//        pullRefreshAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                CourseBean.Course course = data.get(position);
//                Intent intent = new Intent(mContext, VideoViewActivity.class);
//                intent.putExtra("flag", Constants.ONLINE);
//                intent.putExtra("treeid", course.getId());
//                intent.putExtra("tree_name", course.getTitle());
//                intent.putExtra("pic", course.getMiddle_pic());
//                intent.putExtra(Constants.IS_CENTER, "0");
//                startActivity(intent);
//            }
//        });
//
//    }
//
//
//    @Override
//    protected void requestData() {
//        loadingPage();
//        getTeachingCourse();
//    }
//
//    /**
//     * 获取在教课程
//     */
//    private void getTeachingCourse() {
//                new HttpGetBuilder(mContext)
//                .setUrl(UrlsNew.GET_COURSE_TEACHERING)
//                .setHttpResult(new HttpCallBack() {
//                    @Override
//                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
//                        swipeRefresh.setRefreshing(false);
//                        TeachBean teachBean = (TeachBean) resultBean;
//                        TeachBean.Paginate paginate = teachBean.getPaginate();
//                        if(paginate != null){
//                            pageNum = paginate.getPageNum();
//                            pageIndex = paginate.getCurrentPage();
//                        }
//                        ArrayList<PublicFirstLevel> treeBean = new ArrayList<>();
//                        ArrayList<TeachBean.TeachCourseItem> items = teachBean.getItems();
//                        if (items != null && items.size() > 0) {
//                            visibleData();
//                            ArrayList<TeachResult> teachResults = compoundData(items);
//                            Log.i("test","teachResults="+teachResults.toString());
////                          teachAdapter = new TeachAdapter(mContext,teachResults);
////                          expandableListView.setAdapter(teachAdapter);
////                          for(int i = 0; i < teachAdapter.getGroupCount(); i++){
////                                expandableListView.expandGroup(i);
////                          }
//                            for (int i = 0; i < teachResults.size(); i++) {
//                                treeBean.add(new PublicFirstLevel(teachResults.get(i)));
//                            }
//                            treeRecyclerViewAdapter = new TreeRecyclerViewAdapter<>(mContext,
//                                    treeBean, TreeRecyclerViewType.SHOW_ALL);
//                            recyclerView.setAdapter(treeRecyclerViewAdapter);
//                        } else {
//                            noDataPage();
//                        }
//                    }
//
//                    @Override
//                    public void setOnErrorCallback(int requestCode, int code, String msg) {
//                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
//                            NetworkUtil.httpRestartLogin(TeacherCourseFragment.this, myLayout);
//                        } else {
//                            NetworkUtil.httpNetErrTip(mContext, myLayout);
//                        }
//                        loadFailPage();
//                    }
//                })
//                .setClassObj(TeachBean.class)
//                .addQueryParams("page", String.valueOf(pageIndex))
//                .addQueryParams("user_id", spu.getUid())
//                .addQueryParams("status", "")
//                .addQueryParams("pageSize", "40")
//                .build();
//    }
//
//    /**
//     * 组合数据
//     * @param items
//     */
//    private ArrayList<TeachResult> compoundData(ArrayList<TeachBean.TeachCourseItem> items) {
//        if (teachList != null) {
//            teachList.clear();
//        }
//        boolean flag = false;
//        for (int i = 0; i < items.size(); i++) {
//            TeachBean.TeachCourseItem teachItem = items.get(i);
//            if (teachItem.getStatus().equals("2")) {
//                itemList = new ArrayList<>();
//                result = new TeachResult();
//                if(!flag){
//                    flag = true;
//                    result.setTitle("已发布的课程");
//                } else {
//                    result.setTitle("");
//                }
//                itemList.add(teachItem);
//                if (result != null) {
//                    result.setCourseList(itemList);
//                    teachList.add(result);
//                }
//            }
//        }
//
//        flag = false;
//        for (int i = 0; i < items.size(); i++) {
//            TeachBean.TeachCourseItem teachItem = items.get(i);
//            if (teachItem.getStatus().equals("0") || teachItem.getStatus().equals("1")) {
//                itemList = new ArrayList<>();
//                result = new TeachResult();
//                if(!flag){
//                    flag = true;
//                    result.setTitle("未发布的课程");
//                } else {
//                    result.setTitle("");
//                }
//                itemList.add(teachItem);
//                if (result != null) {
//                    result.setCourseList(itemList);
//                    teachList.add(result);
//                }
//            }
//        }
//        return teachList;
//    }
//
//    /**
//     * 组合数据
//     * @param items
//     */
//    private ArrayList<TeachResult> compoundData2(ArrayList<TeachBean.TeachCourseItem> items) {
//        if (teachList != null) {
//            teachList.clear();
//        }
//        boolean flag = false;
//        for (int i = 0; i < items.size(); i++) {
//            TeachBean.TeachCourseItem teachItem = items.get(i);
//            if (teachItem.getStatus().equals("2")) {
//                itemList = new ArrayList<>();
//                result = new TeachResult();
//                if(!flag){
//                    flag = true;
//                    result.setTitle("已发布的课程");
//                } else {
//                    result.setTitle("");
//                }
//                itemList.add(teachItem);
//                if (result != null) {
//                    result.setCourseList(itemList);
//                    teachList.add(result);
//                }
//            }
//        }
//
//        flag = false;
//        for (int i = 0; i < items.size(); i++) {
//            TeachBean.TeachCourseItem teachItem = items.get(i);
//            if (teachItem.getStatus().equals("0") || teachItem.getStatus().equals("1")) {
//                itemList = new ArrayList<>();
//                result = new TeachResult();
//                if(!flag){
//                    flag = true;
//                    result.setTitle("未发布的课程");
//                } else {
//                    result.setTitle("");
//                }
//                itemList.add(teachItem);
//                if (result != null) {
//                    result.setCourseList(itemList);
//                    teachList.add(result);
//                }
//            }
//        }
//        return teachList;
//    }
//
//
//    @Override
//    public void onRefresh() {
//        if(pageIndex == pageNum){
//            swipeRefresh.setRefreshing(false);
//            return;
//        }
//        getTeachingCourse();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }
//
//    /**
//     * 加载更多
//     */
//    @Override
//    public void addMoreListener() {
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    /**
//     * 暂无数据
//     */
//    private void noDataPage() {
//        load_fail_layout.setVisibility(View.GONE);
//        swipeRefresh.setVisibility(View.GONE);
//        jiazai_layout.setVisibility(View.GONE);
//        no_info_layout.setVisibility(View.VISIBLE);
//    }
//
//    /**
//     * 加载失败页
//     */
//    private void loadFailPage() {
//        load_fail_layout.setVisibility(View.VISIBLE);
//        swipeRefresh.setVisibility(View.GONE);
//        jiazai_layout.setVisibility(View.GONE);
//        no_info_layout.setVisibility(View.GONE);
//    }
//
//    /**
//     * 显示数据
//     */
//    private void visibleData() {
//        jiazai_layout.setVisibility(View.GONE);
//        load_fail_layout.setVisibility(View.GONE);
//        no_info_layout.setVisibility(View.GONE);
//        swipeRefresh.setVisibility(View.VISIBLE);
//    }
//
//    /**
//     * 显示加载页
//     */
//    private void loadingPage() {
//        jiazai_layout.setVisibility(View.VISIBLE);
//        swipeRefresh.setVisibility(View.GONE);
//        load_fail_layout.setVisibility(View.GONE);
//        no_info_layout.setVisibility(View.GONE);
//    }
//
//}
