package com.coder.kzxt.gambit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpGetOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.gambit.activity.adapter.MyGambitAdapter;
import com.coder.kzxt.gambit.activity.adapter.MyGambitDelegate;
import com.coder.kzxt.gambit.activity.bean.MyGambitBean;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.Urls;
import com.coder.kzxt.views.CustomNewDialog;

import java.util.ArrayList;
import java.util.List;

public class MyGambitsActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView no_info_text;
    private Button load_fail_button;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private RelativeLayout activity_my_gambit;
    private MyPullSwipeRefresh pullToRefreshView;
    private MyPullRecyclerView myRecyclerView;
    private String from,userId,userName;
    private int index=1;
    private List<MyGambitBean.DataBean>dataBeanList;
    private MyGambitDelegate myGambitDelegate;
    private MyGambitAdapter myGambitAdapter;
    private List<MyGambitBean.MyClassBean> myclassList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gambit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        initViews();
        initEvents();
        httpRequst();

    }
    private void initViews() {
        userName = getIntent().getStringExtra("userName");
        from = getIntent().getStringExtra("from");
        userId = getIntent().getStringExtra("userId");
        activity_my_gambit= (RelativeLayout) findViewById(R.id.activity_my_gambit);
        no_info_text = (TextView) findViewById(R.id.no_info_text);
        search_jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        search_jiazai_layout.setVisibility(View.VISIBLE);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        title = (TextView) findViewById(R.id.toolbar_title);
        customListDialog = new CustomListDialog(MyGambitsActivity.this);
        rangDialogList = customListDialog.getListView();
        rangDialogList.setVisibility(View.VISIBLE);
        customListDialog.getRecyclerView().setVisibility(View.GONE);
        if (from!=null&&from.equals("MemberClassInfo")) {
            if (!String.valueOf(new SharedPreferencesUtil(this).getUid()).equals(userId)) {
                title.setText(userName + getResources().getString(R.string.others_gambits));
            }
        } else {
            title.setText(getResources().getString(R.string.my_gambits));
        }
        dataBeanList=new ArrayList<>();
        pullToRefreshView= (MyPullSwipeRefresh) findViewById(R.id.pullToRefreshView);
        myRecyclerView= (MyPullRecyclerView) findViewById(R.id.myRecyclerView);
        myGambitDelegate=new MyGambitDelegate(this,from);
        myGambitAdapter=new MyGambitAdapter(this,dataBeanList,myGambitDelegate);
        myRecyclerView.setAdapter(myGambitAdapter);
        myGambitAdapter.setSwipeRefresh(pullToRefreshView);
    }

    private void initEvents() {
        pullToRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                myGambitAdapter.resetPageIndex();
                index=1;
                httpRequst();
            }
        });
        myRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                if(index<myGambitAdapter.getTotalPage()){
                    myGambitAdapter.addPageIndex();
                    index++;
                    httpRequst();
                }
            }
        });
        rangDialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyGambitsActivity.this, PublishGambitActivity.class);
                intent.putExtra("commitId", myclassList.get(position).getClassId());
                intent.putExtra("commitType","gambit");
                intent.putExtra(Constants.IS_CENTER, "0");
                startActivityForResult(intent, 1);
                customListDialog.dismiss();
            }
        });
    }

/**
 * 网络加载
 * */
    private void httpRequst() {
        search_jiazai_layout.setVisibility(View.VISIBLE);
        load_fail_layout.setVisibility(View.GONE);
        HttpGetOld httpGet=new HttpGetOld(MyGambitsActivity.this, MyGambitsActivity.this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                search_jiazai_layout.setVisibility(View.GONE);
                if(code== Constants.HTTP_CODE_SUCCESS){
                    MyGambitBean myGambitBean= (MyGambitBean) baseBean;
                    myclassList=myGambitBean.getMyClass();
                    dataBeanList.addAll(myGambitBean.getData());
                    myGambitAdapter.setTotalPage(myGambitBean.getTotalPages());
                    myGambitAdapter.setPullData(myGambitBean.getData());
                }else if(code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
                    //重新登录
                    NetworkUtil.httpRestartLogin(MyGambitsActivity.this,activity_my_gambit);
                }else {
                    //加载失败
                    load_fail_layout.setVisibility(View.VISIBLE);
                    NetworkUtil.httpNetErrTip(MyGambitsActivity.this,activity_my_gambit);
                }
            }
        },MyGambitBean.class, Urls.GET_MY_CLASS_THREAD,"20",String.valueOf(index));
        httpGet.excute();
    }
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(item.getItemId()==R.id.action_setting){
                ArrayList<String> strlist = new ArrayList<String>();
                if (myclassList.size() != 0) {
                    for (int i = 0; i <myclassList.size() ; i++) {
                        strlist.add(myclassList.get(i).getClassName());
                    }
                    String[] arr = (String[])strlist.toArray(new String[strlist.size()]);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            MyGambitsActivity.this, android.R.layout.simple_list_item_1,arr);
                    rangDialogList.setAdapter(arrayAdapter);
                    customListDialog.show();
                } else {
                    showDialog();
                }
            }
            return true;
        }
    };


    @Override
    public void onClick(View v) {
        finish();

    }
    private ListView rangDialogList;
    private CustomListDialog customListDialog;
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.action_setting){
//            ArrayList<String> strlist = new ArrayList<String>();
//            if (myclassList.size() != 0) {
//                for (int i = 0; i <myclassList.size() ; i++) {
//                    strlist.add(myclassList.get(i).getClassName());
//                }
//                String[] arr = (String[])strlist.toArray(new String[strlist.size()]);
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                        MyGambitsActivity.this, android.R.layout.simple_list_item_1,arr);
//                rangDialogList.setAdapter(arrayAdapter);
//                customListDialog.show();
//            } else {
//                showDialog();
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }
    private CustomNewDialog dialogJoinClass;
    private void showDialog(){
        if (dialogJoinClass != null && dialogJoinClass.isShowing()) {
            dialogJoinClass.cancel();
        }
        dialogJoinClass = new CustomNewDialog(MyGambitsActivity.this);
        dialogJoinClass.setMessage(getResources().getString(R.string.remind_join_class));
        dialogJoinClass.setRightClick(new View.OnClickListener() {

            public void onClick(View v) {
                dialogJoinClass.cancel();
//                Intent intent = new Intent(MyGambitsActivity.this, AllClassActivity.class);
//                intent.putExtra("form", "mysendgambitactivity");
//                startActivityForResult(intent, 1);
            }
        });
        dialogJoinClass.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.tea_work_menu,menu);
        menu.findItem(R.id.action_setting).setTitle(R.string.publish);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2||resultCode==3){
            httpRequst();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
