package com.coder.kzxt.classe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.classe.adapter.MyCreateClassAdapter;
import com.coder.kzxt.classe.adapter.MyJoinClassAdapter;
import com.coder.kzxt.classe.beans.MyCreateClass;
import com.coder.kzxt.classe.mInterface.OnClassItemInterface;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.views.CustomNewDialog;
import com.coder.kzxt.views.FullyLinearLayoutManager;
import com.coder.kzxt.views.MyScrollview;

import java.util.ArrayList;


/**
 * 班级列表
 * Created by wangtingshun on 2017/3/10.
 */

public class ClassListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,OnClassItemInterface{

    private Toolbar mToolbar;
    private MyPullSwipeRefresh pullSwipeRefresh;

    private MyScrollview scollview;

    private MyPullRecyclerView topRecyclerView; //上部
    private MyCreateClassAdapter createClassAdapter; //创建班级的adapter
    private MyPullRecyclerView bottomRecyclerView; //下部
    private MyJoinClassAdapter bottomAdapter;

    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private RelativeLayout topLayout;
    private TextView tv_top_course;
    private ImageView iv_top_arrow;
    private RelativeLayout bottomLayout;
    private TextView tv_bottom_course;
    private ImageView iv_bottom_arrow;
    private View line;
    private boolean isTopExpand = false;
    private boolean isBottomExpand = false;
    private SharedPreferencesUtil spu;
    private RelativeLayout myLayout;
    private Button loadFailBtn;//加载失败
    private int pageNum;
    private int pageIndex = 1;
    private CustomNewDialog classSelectDialog; //班级选择对话框
    private TextView cancle; //取消
    private TextView creaateClass; //创建班级
    private TextView findClass;  //查找班级
    private boolean topHasData = false; //顶部是否有数据
    private boolean bottomHasData = false; //底部是否有数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_layout);
        spu = new SharedPreferencesUtil(this);
        initView();
        initListener();
        requestData();
    }


    private void initListener() {
        pullSwipeRefresh.setOnRefreshListener(this);
        //重新加载
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPage();
                myCreateClass();
                myJoinClass();
            }
        });
        //上部点击
        topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTopExpand) {  //收缩
                    isTopExpand = false;
                    iv_top_arrow.setBackgroundResource(R.drawable.department_go);
                    topRecyclerView.setVisibility(View.GONE);
                    line.setVisibility(View.VISIBLE);
                } else {            //展开
                    isTopExpand = true;
                    iv_top_arrow.setBackgroundResource(R.drawable.department_down);
                    topRecyclerView.setVisibility(View.VISIBLE);
                    line.setVisibility(View.GONE);
                }
            }
        });
        //下部点击
        bottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBottomExpand) {  //收缩
                    isBottomExpand = false;
                    iv_bottom_arrow.setBackgroundResource(R.drawable.department_go);
                    bottomRecyclerView.setVisibility(View.GONE);
                } else {               //展开
                    isBottomExpand = true;
                    iv_bottom_arrow.setBackgroundResource(R.drawable.department_down);
                    bottomRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolBar);
        mToolbar.setTitle(getResources().getString(R.string.my_class));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        topRecyclerView = (MyPullRecyclerView) findViewById(R.id.top_recycler_view);
        bottomRecyclerView = (MyPullRecyclerView)findViewById(R.id.bottom_recycler_view);
        scollview = (MyScrollview) findViewById(R.id.scollview);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout)findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        loadFailBtn = (Button) findViewById(R.id.load_fail_button);
        topRecyclerView.setLayoutManager(new FullyLinearLayoutManager(this));
        bottomRecyclerView.setLayoutManager(new FullyLinearLayoutManager(this));
        //设置Item增加、移除动画
        topRecyclerView.setItemAnimator(new DefaultItemAnimator());
        bottomRecyclerView.setItemAnimator(new DefaultItemAnimator());
        line = findViewById(R.id.line);
        topLayout = (RelativeLayout) findViewById(R.id.include_top);
        tv_top_course = (TextView) findViewById(R.id.tv_top_course);
        iv_top_arrow = (ImageView) findViewById(R.id.iv_top_arrow);
        bottomLayout = (RelativeLayout) findViewById(R.id.include_bottom);
        tv_bottom_course = (TextView) findViewById(R.id.tv_bottom_course);
        iv_bottom_arrow = (ImageView) findViewById(R.id.iv_bottom_arrow);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    private void requestData() {
        loadingPage();
        myCreateClass();
        myJoinClass();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //返回
                finish();
                break;
            case R.id.add_class: //加班
                addClassDialog();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.class_menu,menu);
        MenuItem classItem = menu.findItem(R.id.add_class);
        MenuItemCompat.setShowAsAction(classItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 我创建的班级
     */
    private void myCreateClass() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_CLASS_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        pullSwipeRefresh.setRefreshing(false);
                        MyCreateClass calssBean = (MyCreateClass) resultBean;
                        MyCreateClass.Paginate paginate = calssBean.getPaginate();
                        if (paginate != null) {
                            pageNum = paginate.getPageNum();
                            pageIndex = paginate.getCurrentPage();
                        }
                        ArrayList<MyCreateClass.CreateClassBean> items = calssBean.getItems();
                        if (items != null && items.size() > 0) {
                            topHasData = true;
                            visibleData();
                            topLayout.setVisibility(View.VISIBLE);
                            adapterTopData(items);
                        } else {
                            topHasData = false;
                            if (!bottomHasData && !topHasData) {
                                noDataPage();
                            }
                            topLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ClassListActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ClassListActivity.this, myLayout);
                        }
                        pullSwipeRefresh.setRefreshing(false);
                        loadFailPage();
                    }
                })
                .setClassObj(MyCreateClass.class)
                .addQueryParams("page", String.valueOf(pageIndex))
                .addQueryParams("user_id", spu.getUid())
                .build();
    }

    private void adapterData(ArrayList<MyCreateClass.CreateClassBean> items) {
        ArrayList<MyCreateClass.CreateClassBean> bottomItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            MyCreateClass.CreateClassBean classBean = items.get(i);
            String role = classBean.getSelf_role();
            if (role.equals("0") || role.equals("1")) {
                bottomItems.add(classBean);
            }
        }
        if (bottomItems.size() > 0) {
            bottomLayout.setVisibility(View.VISIBLE);
            adapterBottomData(bottomItems);
        } else {
            bottomLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 我加入的班级
     */
    private void myJoinClass() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_GROUP_MY)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        pullSwipeRefresh.setRefreshing(false);
                        MyCreateClass calssBean = (MyCreateClass) resultBean;
                        MyCreateClass.Paginate paginate = calssBean.getPaginate();
                        if(paginate != null){
                            pageNum = paginate.getPageNum();
                            pageIndex = paginate.getCurrentPage();
                        }
                        ArrayList<MyCreateClass.CreateClassBean> items = calssBean.getItems();
                        if (items != null && items.size() > 0) {
                            bottomHasData = true;
                            visibleData();
                            adapterData(items);
                        } else {
                            bottomHasData = false;
                            if (bottomAdapter != null) {
                                bottomAdapter.setJoinClassAdapter(items);
                                bottomAdapter.notifyDataSetChanged();
                            }
                            bottomLayout.setVisibility(View.GONE);
                            if (!bottomHasData && !topHasData) {
                                noDataPage();
                            }
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ClassListActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ClassListActivity.this, myLayout);
                        }
                        pullSwipeRefresh.setRefreshing(false);
                        loadFailPage();
                    }
                })
                .setClassObj(MyCreateClass.class)
                .addQueryParams("page", String.valueOf(pageIndex))
                .build();
    }


    /**
     * 下部数据
     * @param items
     */
    private void adapterBottomData(ArrayList<MyCreateClass.CreateClassBean> items) {
        isBottomExpand = true;
        tv_bottom_course.setText("我加入的班级");
        iv_bottom_arrow.setBackgroundResource(R.drawable.department_down);
        bottomAdapter = new MyJoinClassAdapter(this,items);
        bottomRecyclerView.setAdapter(bottomAdapter);
        bottomAdapter.setOnClassItemClickListener(this);
    }


    /**
     * 上部数据
     * @param items
     */
    private void adapterTopData(ArrayList<MyCreateClass.CreateClassBean> items) {
        isTopExpand = true;
        tv_top_course.setText("我创建的班级");
        iv_top_arrow.setBackgroundResource(R.drawable.department_down);
        createClassAdapter = new MyCreateClassAdapter(this,items);
        topRecyclerView.setAdapter(createClassAdapter);
        createClassAdapter.setOnClassItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        myCreateClass();
        myJoinClass();
    }


    private void addClassDialog() {
        classSelectDialog = new CustomNewDialog(this, R.layout.add_class_dialog_item);
        cancle = (TextView) classSelectDialog.findViewById(R.id.tv_cancle);
        creaateClass = (TextView) classSelectDialog.findViewById(R.id.create_class);
        findClass = (TextView) classSelectDialog.findViewById(R.id.find_class);
        classSelectDialog.show();
        initAfterDialogListener();
    }

    private void initAfterDialogListener() {

        //取消
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classSelectDialog.isShowing()) {
                    classSelectDialog.cancel();
                }
            }
        });

        //创建班级
        creaateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classSelectDialog.isShowing()) {
                    classSelectDialog.cancel();
                }
                Intent intent = new Intent(ClassListActivity.this,CreateClassActivity.class);
                intent.putExtra("classFlag","create");
                startActivityForResult(intent,Constants.REQUEST_CODE);
            }
        });

        //查找班级
        findClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classSelectDialog.isShowing()) {
                    classSelectDialog.cancel();
                }
                Intent intent = new Intent(ClassListActivity.this,FindClassActivity.class);
                startActivityForResult(intent,1);

            }
        });
    }


    /**
     * 暂无数据
     */
    private void noDataPage() {
        load_fail_layout.setVisibility(View.GONE);
        pullSwipeRefresh.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        load_fail_layout.setVisibility(View.VISIBLE);
        pullSwipeRefresh.setVisibility(View.GONE);
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
        pullSwipeRefresh.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载页
     */
    private void loadingPage() {
        jiazai_layout.setVisibility(View.VISIBLE);
        pullSwipeRefresh.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    /**
     * 点击班级item
     * @param item
     */
    @Override
    public void onClassItemClick(MyCreateClass.CreateClassBean item) {
            Intent intent = new Intent(ClassListActivity.this,ClassDetailActivity.class);
            intent.putExtra("class",item);
            startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            requestData();
        } else if (requestCode == Constants.REQUEST_CODE && resultCode == 7) {
            requestData();
        }
    }

}
