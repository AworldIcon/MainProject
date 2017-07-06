package com.coder.kzxt.message.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.views.ContainsEmojiEditText;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.message.beans.NotificationClassBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ScreenUtils;
import com.coder.kzxt.utils.ToastUtils;

/**
 * Created by zw on 2017/6/23
 * 发送通知
 */

public class SendNotificationActivity extends BaseActivity
{

    private RelativeLayout mainView;
    private android.support.v7.widget.Toolbar mToolbar;
    private ContainsEmojiEditText mTitleEdit;
    private ContainsEmojiEditText mContentEdit;
    private TextView mReceive_people,notice,cu_number_con,cu_number_ti;
    private ListView class_list;
    private ClassAdapter classAdapter;
    private LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        //初始化 view findviewbyid
        initFindViewById();
        //需要初始化的数据
        initData();
        //响应事件click
        initClick();
    }

    private void initVariable()
    {
    }

    private void initFindViewById()
    {
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mTitleEdit = (ContainsEmojiEditText) findViewById(R.id.titleEdit);
        mContentEdit = (ContainsEmojiEditText) findViewById(R.id.contentEdit);
        mReceive_people = (TextView) findViewById(R.id.receive_people);
        notice = (TextView) findViewById(R.id.notice);
        cu_number_con = (TextView) findViewById(R.id.cu_number_con);
        layout = (LinearLayout) findViewById(R.id.layout);
        class_list= (ListView) findViewById(R.id.class_list);
        classAdapter=new ClassAdapter();
        class_list.setAdapter(classAdapter);

    }

    private void initData()
    {
        mToolbar.setTitle(getResources().getString(R.string.send_notification));
        setSupportActionBar(mToolbar);
    }

    private void initClick()
    {
        mReceive_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SendNotificationActivity.this,NotificationClassActivity.class),1);
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        mContentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <100) {
                    cu_number_con.setText(s.length() + "");
                    cu_number_con.setTextColor(getResources().getColor(R.color.font_gray));
                } else {
                    cu_number_con.setText(s.length() + "");
                    cu_number_con.setTextColor(getResources().getColor(R.color.font_red));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

        protected void dialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
            builder.setMessage("您确定要放弃本次发送吗？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    NotificationClassBean.classNames="";
                    NotificationClassBean.childList.clear();
                    NotificationClassBean.childSelectList.clear();
                    NotificationClassBean.totaldata="";
                    finish();
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

//网络请求
    private void requestData()
    {
        new HttpPostBuilder(this).setUrl(UrlsNew.URLHeader+"/notify").setClassObj(null).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                hideLoadingView();
                NotificationClassBean.classNames="";
                NotificationClassBean.childList.clear();
                NotificationClassBean.childSelectList.clear();
                NotificationClassBean.totaldata="";
                setResult(1);
                finish();
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                hideLoadingView();

                if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
                {
                    //重新登录
                    NetworkUtil.httpRestartLogin(SendNotificationActivity.this, mainView);
                } else
                {
                    NetworkUtil.httpNetErrTip(SendNotificationActivity.this, mainView);
                }
            }
        }).addBodyParam("title",mTitleEdit.getText().toString())
                .addBodyParam("content",mContentEdit.getText().toString())
                .addBodyParam("course_class",NotificationClassBean.totaldata)
                .build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu,menu);
        menu.findItem(R.id.right_item).setTitle("发布");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(TextUtils.isEmpty(mTitleEdit.getText().toString())||TextUtils.isEmpty(mContentEdit.getText().toString())){
            ToastUtils.makeText(SendNotificationActivity.this,"标题或内容不能为空");
            return false;
        }else if(TextUtils.isEmpty(NotificationClassBean.classNames.trim())){
            ToastUtils.makeText(SendNotificationActivity.this,"请选择接收对象");
            return false;
        }else {
            showLoadingView();
            requestData();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(NotificationClassBean.childSelectList.size()>0&&resultCode==1){
            mReceive_people.setText(getResources().getString(R.string.receive_people)+"       已选择"+NotificationClassBean.childSelectList.size()+"个班");
            if(NotificationClassBean.childSelectList.size()>7){
                class_list.getLayoutParams().height= ScreenUtils.getScreenHeight(this)/3;
                classAdapter.notifyDataSetChanged();
            }else {
                classAdapter.notifyDataSetChanged();
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
              class_list.setLayoutParams(layoutParams);
            }
        }else if (NotificationClassBean.childSelectList.size()==0){
            classAdapter.notifyDataSetChanged();
            mReceive_people.setText(getResources().getString(R.string.receive_people));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            dialog();
        }
        return false;
    }
    class ClassAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return NotificationClassBean.childSelectList.size();
        }

        @Override
        public Object getItem(int position) {
            return NotificationClassBean.childSelectList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView= LayoutInflater.from(SendNotificationActivity.this).inflate(R.layout.item,null);
            }
            TextView item_name= (TextView) convertView.findViewById(R.id.item_name);
            item_name.setText(NotificationClassBean.classNames.split(",")[position]);
            return convertView;
        }
    }
}
