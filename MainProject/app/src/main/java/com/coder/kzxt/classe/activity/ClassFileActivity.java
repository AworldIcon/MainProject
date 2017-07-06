package com.coder.kzxt.classe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.app.http.HttpGetOld;
import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.adapter.ClassFileAdapter;
import com.coder.kzxt.classe.beans.ClassFileBean;
import com.coder.kzxt.base.activity.BaseActivity;
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
 * 班级文件
 * Created by wangtingshun on 2017/3/14.
 */

public class ClassFileActivity extends BaseActivity implements ClassFileAdapter.OnItemClickInterface, SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferencesUtil spu;
    /**
     * 班级id
     */
    private String classId;
    /**
     * 加载进度匡
     */
    private LinearLayout loadLayout;
    /**
     * 暂无数据
     */
    private LinearLayout no_info_layout;
    /**
     * 网络异常
     */
    private LinearLayout network_set_layout;
    /**
     * 加载失败
     */
    private LinearLayout load_fail_view;

    /**
     * 文件共享适配器
     */
    private ClassFileAdapter adapter;
    /**
     * 重新加载按钮
     */
    private Button reLoadButton;
    /**
     * 返回键
     */
    private MyPullRecyclerView recyclerView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private Context mContext;
    private String identity;
    private RelativeLayout shareFileLayout;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_file);
        mContext = this;
        spu = new SharedPreferencesUtil(this);
        identity = getIntent().getStringExtra("identity");
        classId = getIntent().getStringExtra("classId");
        initView();
        setListener();
        startAsyncData(classId);
    }

    private void initView() {
        loadLayout = (LinearLayout) findViewById(R.id.jiazai_layout);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        network_set_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_view = (LinearLayout) findViewById(R.id.load_fail_view);
        reLoadButton = (Button) findViewById(R.id.load_fail_button);
        shareFileLayout = (RelativeLayout) findViewById(R.id.share_file_layout);
        recyclerView = (MyPullRecyclerView) findViewById(R.id.recyclerView);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle(getResources().getString(R.string.file_share));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        reLoadButton.setOnClickListener(clickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
     * 获取班级共享数据
     */
    private void startAsyncData(String classId) {
        loadDialog();
        HttpGetOld httpGet = new HttpGetOld(this, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                visibleData();
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    myPullSwipeRefresh.setRefreshing(false);
                    ClassFileBean fileBean = (ClassFileBean) baseBean;
                    ArrayList<ClassFileBean.ClassFile> classFiles = fileBean.getData();
                    if(classFiles != null && classFiles.size() > 0){
                        adapterData(classFiles);
                    } else {
                        noData();
                    }
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(mContext, shareFileLayout);
                    netWorkLoadFail();
                } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    NetworkUtil.httpRestartLogin(ClassFileActivity.this, shareFileLayout);
                } else {
                    noData();
                }
            }
        }, ClassFileBean.class, Urls.GET_SHARED_FILE_ACTION, classId, spu.getUid(), spu.getIsLogin(), spu.getTokenSecret(), spu.getDevicedId(),"20");
        httpGet.excute(1000);
    }

    /**
     * 适配数据
     */
    private void adapterData(ArrayList<ClassFileBean.ClassFile> classFiles) {
        adapter = new ClassFileAdapter(this,classFiles);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(ClassFileActivity.this);
    }

    /**
     * 加载进度框
     */
    private void loadDialog() {
        loadLayout.setVisibility(View.VISIBLE);
        network_set_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        myPullSwipeRefresh.setVisibility(View.GONE);
    }

    /**
     * 显示数据
     */
    private void visibleData() {
        network_set_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        loadLayout.setVisibility(View.GONE);
        myPullSwipeRefresh.setVisibility(View.VISIBLE);
    }

    /**
     * 网络加载失败
     */
    private void netWorkLoadFail() {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            network_set_layout.setVisibility(View.VISIBLE);
        }
        myPullSwipeRefresh.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        loadLayout.setVisibility(View.GONE);
    }

    /**
     * 暂无数据
     */
    private void noData() {
        no_info_layout.setVisibility(View.VISIBLE);
        myPullSwipeRefresh.setVisibility(View.GONE);
        network_set_layout.setVisibility(View.GONE);
        loadLayout.setVisibility(View.GONE);
    }


    /**
     * 请求删除
     * @param classFile
     * @param identity
     */
    private void requestDelete(final ClassFileBean.ClassFile classFile,final String identity) {
        final CustomNewDialog dialog = new CustomNewDialog(ClassFileActivity.this);
        dialog.setTitleVisibility(getResources().getString(R.string.dialog_livelesson_prompt));
        dialog.setMessage(getResources().getString(R.string.delete_file_warn));
        dialog.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        dialog.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (identity != null || classFile.getUserName().equals(spu.getNickName())) {
                    dialog.cancel();
                    deletFileTask( classId, classFile.getFileId());
                } else {
                    ToastUtils.makeText(ClassFileActivity.this, getResources().getString(R.string.no_delete_power), Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

    /**
     * 删除文件的task
     * @param classId
     * @param fileId
     */
    private void deletFileTask(final String classId, String fileId) {
        loadLayout.setVisibility(View.VISIBLE);
        new HttpPostOld(this, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                loadLayout.setVisibility(View.GONE);
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    startAsyncData(classId);
                    ToastUtils.makeText(mContext, getResources().getString(R.string.delete_file) + msg, Toast.LENGTH_SHORT).show();
                } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    NetworkUtil.httpRestartLogin(ClassFileActivity.this, shareFileLayout);
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(mContext, shareFileLayout);
                } else {
                    ToastUtils.makeText(mContext, getResources().getString(R.string.delete_file) + msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, null, Urls.POST_DELETE_SHARED_FILE_ACTION, spu.getUid(), spu.getIsLogin(), spu.getTokenSecret(), spu.getDevicedId(), classId, fileId).excute(1000);
    }

    /**
     * 点击监听
     */
    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.load_fail_button:
                    //重新加载
                    startAsyncData(classId);
                    break;
                case R.id.network_set_layout:
                    //设置网络
                    openSetting();
                    break;
            }
        }
    };

    /**
     * 打开系统设置
     */
    private void openSetting() {
        if (Constants.API_LEVEL_11) {
            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        } else {
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    /**
     * 长按item事件
     * @param classFile
     */
    @Override
    public void onItemLongClick(ClassFileBean.ClassFile classFile) {
        if (TextUtils.isEmpty(identity)) {
            if (identity.equals("header") || identity.equals("admin") ||
                    identity.equals("owner") || classFile.getUserName().equals(spu.getNickName())) {
                requestDelete(classFile, identity);
            }
        }
    }

    @Override
    public void onRefresh() {
        startAsyncData(classId);
    }
}
