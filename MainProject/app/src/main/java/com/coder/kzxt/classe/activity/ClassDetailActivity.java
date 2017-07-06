package com.coder.kzxt.classe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpDeleteBuilder;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.classe.adapter.ClassMemberAdapter;
import com.coder.kzxt.classe.beans.ClassDetailBean;
import com.coder.kzxt.classe.beans.ClassMemberBean;
import com.coder.kzxt.classe.beans.MyCreateClass;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.views.MyGridView;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by wangtingshun on 2017/6/8.
 * 班级详情
 */
public class ClassDetailActivity extends BaseActivity implements ClassMemberAdapter.OnItemClickInterface {

    private Toolbar mToolbar;
    private ImageView classIcon; //班级icon
    private TextView className; //班级名称
    private TextView studyYear;  //入学年份
    private RelativeLayout classTopic; //班级话题
    private RelativeLayout classPhoto; //班级相册
    private RelativeLayout classFile; //班级文件
    private TextView memberCount;  //成员数量
    private MyGridView myGridView;
    private RelativeLayout classCheck; //班级审核
    private RelativeLayout classManager;  //班级管理
    private RelativeLayout classNotice; //班级公告
    private TextView noticeContent; //公告内容
    private MyCreateClass.CreateClassBean classBean;
    private RelativeLayout myLayout;
    private ClassMemberAdapter memberAdapter; //班级成员adapter
    private RelativeLayout memberLayout;  //班级成员
    private ScrollView scrollView;
    private String announcement;    //班级公告
    private Button dissolveClass;  //解散班级
    private ClassDetailBean.ClassDetail item;  //班级详情bean
    private SharedPreferencesUtil spu;
    private ImageView classNoticeArrow;
    private  String self_role;
    private String pageFlag;
    private boolean isExit = false;  //是否退出班级
    private boolean isJoin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail_layout);
        spu = new SharedPreferencesUtil(this);
        pageFlag = getIntent().getStringExtra("create");
        classBean = (MyCreateClass.CreateClassBean) getIntent().getSerializableExtra("class");
        initView();
        initListener();
        requestData();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolBar);
        mToolbar.setTitle(getResources().getString(R.string.class_particulars));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);
        classIcon = (ImageView) findViewById(R.id.iv_class_icon);
        className = (TextView) findViewById(R.id.tv_class_name);
        studyYear = (TextView) findViewById(R.id.tv_year);
        classTopic = (RelativeLayout) findViewById(R.id.class_topic_layout);
        classPhoto = (RelativeLayout) findViewById(R.id.class_photo_layout);
        classFile = (RelativeLayout) findViewById(R.id.class_file_layout);
        memberCount = (TextView) findViewById(R.id.class_member_count);
        myGridView = (MyGridView) findViewById(R.id.my_grid_view);
//        myRecyclerView.setLayoutManager(new GridLayoutManager(this, 10));
//        myRecyclerView.addItemDecoration(new DividerGridItemDecoration(this,0));
        classCheck = (RelativeLayout) findViewById(R.id.class_check_layout);
        classManager = (RelativeLayout) findViewById(R.id.class_manager_layout);
        classNotice = (RelativeLayout) findViewById(R.id.class_notice_layout);
        noticeContent = (TextView) findViewById(R.id.tv_notice_content);
        memberLayout = (RelativeLayout) findViewById(R.id.bottom_member_layout);
        dissolveClass = (Button) findViewById(R.id.btn_dissolve_class);
        scrollView = (ScrollView) findViewById(R.id.my_scrollView);
        classNoticeArrow = (ImageView) findViewById(R.id.class_notice_arrow);
        scrollView.setVisibility(View.GONE);
    }

    private void initState() {
        if (self_role.equals("2")) {
            classCheck.setVisibility(View.VISIBLE);
            classManager.setVisibility(View.VISIBLE);
            classNoticeArrow.setVisibility(View.VISIBLE);
        } else {
            classCheck.setVisibility(View.GONE);
            classManager.setVisibility(View.GONE);
            classNoticeArrow.setVisibility(View.GONE);
        }

        if (!self_role.equals("2")) {
            classNotice.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }
    }


    private void initListener() {

        //话题
        classTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(ClassDetailActivity.this,ClassTopicActivity.class);
                 intent.putExtra("selfRole",item.getSelf_role());
                 intent.putExtra("joinState",item.getJoin_status());
                 intent.putExtra("classBean",classBean);
                 startActivity(intent);
            }
        });
        //相册
        classPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ToastUtils.makeText(ClassDetailActivity.this,"暂未开放此功能",Toast.LENGTH_SHORT).show();
            }
        });
        //文件
        classFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.makeText(ClassDetailActivity.this,"暂未开放此功能",Toast.LENGTH_SHORT).show();
            }
        });
        //班级成员
        memberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassDetailActivity.this,ClassMemberListActivity.class);
                intent.putExtra("userId",spu.getUid());
                intent.putExtra("groupId",classBean.getId());
                startActivity(intent);
            }
        });

        //班级审核
        classCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassDetailActivity.this,JoinCheckActivity.class);
                intent.putExtra("detail",item);
                startActivityForResult(intent,2);
            }
        });
        //班级管理
        classManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassDetailActivity.this,CreateClassActivity.class);
                intent.putExtra("classFlag","manager");
                intent.putExtra("detailBean",item);
                startActivityForResult(intent,Constants.REQUEST_CODE);
            }
        });
        //班级公告
        classNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassDetailActivity.this,ClassAnnounceActivity.class);
                intent.putExtra("content",announcement);
                startActivityForResult(intent,Constants.REQUEST_CODE);
            }
        });
        //解散班级
        dissolveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processClickEvent();
            }
        });
    }

    private void processClickEvent() {
        if (item.getSelf_role().equals("0") && !item.getJoin_status().equals("2")) { //加入班级
            showLoadingView();
            joinClassRequest();
        } else if (item.getSelf_role().equals("2")) { //解散班级
            dissolveClassDialog();
        } else if (item.getSelf_role().equals("1")) { //退出班级
            exitClassDialog();

        }
    }

    private void exitClassDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ClassDetailActivity.this, R.style.custom_dialog);
        builder.setMessage("确定退出此班级吗？");
        builder.setPositiveButton(getResources().getString(R.string.password_dialog_ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exitClass();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void joinClassRequest() {
                 new HttpPostBuilder(this)
                .setUrl(UrlsNew.GET_GROUP_MEMBER_APPLY)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        isJoin = true;
                        getClassDetail();
                        getClassMember();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ClassDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ClassDetailActivity.this, myLayout);
                        }
                        isJoin = false;
                        ToastUtils.makeText(ClassDetailActivity.this,msg, Toast.LENGTH_SHORT).show();
                        hideLoadingView();
                    }
                })
                .setClassObj(null)
                .addBodyParam("group_id",classBean.getId())
                .build();
    }

    /**
     * 解散班级
     */
    private void dissolveClassDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ClassDetailActivity.this, R.style.custom_dialog);
        builder.setMessage("您是班级创建者，如有需要可以解散班级");
        builder.setPositiveButton(getResources().getString(R.string.password_dialog_ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showLoadingView();
                getDissolveClass();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    /**
     * 获取解散班级的接口
     */
    private void getDissolveClass() {
                new HttpDeleteBuilder(this)
                .setUrl(UrlsNew.DELETE_DISSOLVE_CLASS)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                         hideLoadingView();
                        if (!TextUtils.isEmpty(pageFlag)) {
                            backPrevious();
                        } else {
                            setResult(2);
                            finish();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ClassDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ClassDetailActivity.this, myLayout);
                        }
                        hideLoadFailView();
                    }
                })
                .setClassObj(null)
                .setPath(classBean.getId())
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //返回
                backPrevious();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void requestData() {
              showLoadingView();
              getClassDetail();
              getClassMember();
    }

    private void getClassDetail() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_CLASS_DETAIL)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        hideLoadingView();
                        ClassDetailBean detailBean = (ClassDetailBean) resultBean;
                        item = detailBean.getItem();
                        self_role = item.getSelf_role();
                        initState();
                        if (item != null) {
                            scrollView.setVisibility(View.VISIBLE);
                            setDetailData(item);
                        } else {
                            scrollView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ClassDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ClassDetailActivity.this, myLayout);
                        }
                        hideLoadFailView();
                    }
                })
                .setClassObj(ClassDetailBean.class)
                .setPath(classBean.getId())
                .build();
    }

    private void setDetailData(ClassDetailBean.ClassDetail item) {
        GlideUtils.loadHeaderOfClass(this,item.getLogo(),classIcon);
        className.setText(item.getName());
        studyYear.setText(item.getYear()+"年");
        announcement = item.getAnnouncement();
        if(!TextUtils.isEmpty(announcement)){
            noticeContent.setText(announcement);
        } else {
            noticeContent.setText("暂无公告");
        }
        memberCount.setText("("+item.getMember_count()+"名)");
        setState(item);
    }

    private void setState(ClassDetailBean.ClassDetail item) {
        dissolveClass.setClickable(true);
        if (item.getSelf_role().equals("2")) {
            dissolveClass.setText(getResources().getString(R.string.dissolve_class));
            dissolveClass.setTextColor(getResources().getColor(R.color.font_red));
            dissolveClass.setBackgroundColor(getResources().getColor(R.color.bg_white));
        } else if(item.getSelf_role().equals("0") && item.getHas_apply().equals("1")){
            dissolveClass.setText("已申请");
            dissolveClass.setTextColor(getResources().getColor(R.color.font_gray));
            dissolveClass.setBackgroundColor(getResources().getColor(R.color.banned_join));
        } else if(item.getSelf_role().equals("0")){
            if (item.getJoin_status().equals("2")) {
                dissolveClass.setText(getResources().getString(R.string.joinin_ban)); //禁止加入
                dissolveClass.setTextColor(getResources().getColor(R.color.font_gray));
                dissolveClass.setBackgroundColor(getResources().getColor(R.color.banned_join));
                dissolveClass.setClickable(false);
            } else if (item.getJoin_status().equals("1")) {
                dissolveClass.setText(getResources().getString(R.string.join_class_now));  //立即加入
                dissolveClass.setTextColor(getResources().getColor(R.color.font_white));
                dissolveClass.setBackgroundColor(getResources().getColor(R.color.first_theme));
            } else if (item.getJoin_status().equals("0")) {
                dissolveClass.setText("申请加入");  //需要审核
                dissolveClass.setTextColor(getResources().getColor(R.color.font_white));
                dissolveClass.setBackgroundColor(getResources().getColor(R.color.first_theme));
            }
        } else if(item.getSelf_role().equals("1")){
            dissolveClass.setText("退出班级");
            dissolveClass.setTextColor(getResources().getColor(R.color.font_red));
            dissolveClass.setBackgroundColor(getResources().getColor(R.color.bg_white));
        }  else {

        }
    }

    private void getClassMember() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_CLASS_MEMBER)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        hideLoadingView();
                        ClassMemberBean  memberBean = (ClassMemberBean) resultBean;
                        ArrayList<ClassMemberBean.ClassMember> items = memberBean.getItems();
                        if (items != null && items.size() > 0) {
                            memberLayout.setVisibility(View.VISIBLE);
                            adapterData(items);
                        } else {
                            memberLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ClassDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ClassDetailActivity.this, myLayout);
                        }
                        hideLoadFailView();
                    }
                })
                .setClassObj(ClassMemberBean.class)
                .addQueryParams("group_id", classBean.getId())
                .build();
    }

    /**
     * 退出班级
     */
    public void exitClass(){
                showLoadingView();
                new HttpDeleteBuilder(this)
                .setUrl(UrlsNew.DELETE_EXIT_CLASS)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        isExit = true;
                        getClassDetail();
                        getClassMember();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ClassDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ClassDetailActivity.this, myLayout);
                        }
                        isExit = false;
                        ToastUtils.makeText(ClassDetailActivity.this,msg,Toast.LENGTH_SHORT).show();
                        hideLoadFailView();
                    }
                })
                .setPath(classBean.getId())
                .build();
    }

    /**
     * 更新班级数据
     */
    public void updateClassData(String data){
                 new HttpPutBuilder(this)
                .setUrl(UrlsNew.PUT_UPDATE_CLASS)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {

                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ClassDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ClassDetailActivity.this, myLayout);
                        }
                        ToastUtils.makeText(ClassDetailActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(null)
                .setPath(item.getId())
                .addBodyParam(data)
                .build();
    }

    private void adapterData(ArrayList<ClassMemberBean.ClassMember> items) {
        memberAdapter = new ClassMemberAdapter(this,items);
        myGridView.setAdapter(memberAdapter);
        memberAdapter.setOnItemClickListener(this);
        initRecyclerViewWidth(items.size());
    }

    /**
     * 初始化recyclerView的宽度
     * @param size
     */
    private void initRecyclerViewWidth(int size) {
        ViewGroup.LayoutParams layoutParams = myGridView.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(10, 0, 0, 0);
        if (size == 1) {
            layoutParams.width = 100;
            myGridView.setNumColumns(1);
        } else if (size == 2) {
            layoutParams.width = 200;
            myGridView.setNumColumns(2);
        } else if (size == 3) {
            layoutParams.width = 250;
            myGridView.setNumColumns(3);
        } else if (size == 4) {
            layoutParams.width = 300;
            myGridView.setNumColumns(4);
        } else if (size >= 5) {
            layoutParams.width = 400;
            myGridView.setNumColumns(5);
        }
        myGridView.setLayoutParams(layoutParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == 3) {
            String content = data.getStringExtra("content");
            noticeContent.setText(content);
            announcement = noticeContent.getText().toString();
            updateClassData(getSaveClassData());
        } else if(requestCode == Constants.REQUEST_CODE && resultCode == 6){
            requestData();
        }
    }

    /**
     * 获取保存班级的数据
     * @return
     */
    private String getSaveClassData() {
        JSONObject object = new JSONObject();
        try {
            object.put("name", item.getName());
            object.put("year", item.getYear());
            object.put("category_id", item.getCategory_id());
            object.put("join_status", item.getJoin_status());
            object.put("logo", item.getLogo());
            object.put("announcement",announcement);
        } catch (Exception e) {

        }
        return object.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnHeadItemClick(ClassMemberBean.ClassMember memberBean) {
        Intent intent = new Intent(ClassDetailActivity.this,ClassMemberListActivity.class);
        intent.putExtra("userId",spu.getUid());
        intent.putExtra("groupId",classBean.getId());
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backPrevious();
        }
        return true;
    }

    public void backPrevious() {
        if (!TextUtils.isEmpty(pageFlag)
                && pageFlag.equals("create")) {
            setResult(12);
        } else if(isExit){
            setResult(2);
        } else if(isJoin){
            setResult(3);
        }
        finish();
    }
}
