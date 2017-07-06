package com.coder.kzxt.message.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.message.beans.NotificationClassBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;

public class NotificationClassActivity extends BaseActivity {
    private Button load_fail_button;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Toolbar toolbar;
    private RelativeLayout mainView;
    private ExpandableListView expand_class;
    private NotificationClassBean notificationClassBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_class);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("选择通知班级");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initClick();
        httpRequest();
    }
    //页面点击事件
    private void initClick() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               // selectClass();
            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_fail_layout.setVisibility(View.GONE);
                search_jiazai_layout.setVisibility(View.VISIBLE);
                httpRequest();
            }
        });

    }
    //初始化组件
    private void initView() {
        expand_class= (ExpandableListView) findViewById(R.id.expand_class);
        mainView= (RelativeLayout) findViewById(R.id.activity_questions_detail);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        search_jiazai_layout = (LinearLayout)findViewById(R.id.jiazai_layout);
    }
    //网络请求数据
    ExpandAdapter expandAdapter;
    private void httpRequest() {

        new HttpGetBuilder(this).setClassObj(NotificationClassBean.class).setUrl(UrlsNew.GET_NOTIFY_CLASS).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                search_jiazai_layout.setVisibility(View.GONE);
                expand_class.setVisibility(View.VISIBLE);
                notificationClassBean= (NotificationClassBean) resultBean;


                for(int g=0;g<notificationClassBean.getItems().size();g++){
                    int childSize=0;
                    for(int i=0;i<notificationClassBean.getItems().get(g).getClassX().size();i++){
                        if(NotificationClassBean.childList.containsKey(notificationClassBean.getItems().get(g).getClassX().get(i).getId()+"")){
                            childSize++;
                        }
                        if(childSize==notificationClassBean.getItems().get(g).getClassX().size()){
                            notificationClassBean.getItems().get(g).setExpand(true);
                        }
                    }
                }

                 expandAdapter=new ExpandAdapter();
                expand_class.setAdapter(expandAdapter);
                expandAdapter.notifyDataSetChanged();
                if(notificationClassBean.getItems().size()==0){
                    no_info_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                expand_class.setVisibility(View.GONE);
                search_jiazai_layout.setVisibility(View.GONE);
                no_info_layout.setVisibility(View.GONE);
                if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
                {
                    NetworkUtil.httpRestartLogin(NotificationClassActivity.this, mainView);
                }else {
                    load_fail_layout.setVisibility(View.VISIBLE);
                    ToastUtils.makeText(NotificationClassActivity.this,msg);
                }
            }
        }).addQueryParams("user_id",new SharedPreferencesUtil(NotificationClassActivity.this).getUid())
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu, menu);
        menu.findItem(R.id.right_item).setTitle("确定");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        selectClass();
        return super.onOptionsItemSelected(item);
    }
//保存数据
    private void selectClass() {
        NotificationClassBean.childList.clear();
        NotificationClassBean.childSelectList.clear();
        NotificationClassBean.totaldata="";
        NotificationClassBean.classNames="";
        for(int g=0;g<notificationClassBean.getItems().size();g++){
            String chlidData="";
            for(int j=0;j<notificationClassBean.getItems().get(g).getClassX().size();j++){
                if(notificationClassBean.getItems().get(g).getClassX().get(j).isExpand()){
                    NotificationClassBean.childList.put(notificationClassBean.getItems().get(g).getClassX().get(j).getId()+"",notificationClassBean.getItems().get(g).getClassX().get(j).getCourse_id());
                    NotificationClassBean.childSelectList.put(notificationClassBean.getItems().get(g).getClassX().get(j).getId()+"",notificationClassBean.getItems().get(g).getClassX().get(j).getCourse_id());
                    chlidData+=notificationClassBean.getItems().get(g).getClassX().get(j).getId()+",";
                    NotificationClassBean.classNames+=notificationClassBean.getItems().get(g).getTitle()+"-"+notificationClassBean.getItems().get(g).getClassX().get(j).getClass_name()+",";
                }
            }
            if(!TextUtils.isEmpty(chlidData.trim())){
                NotificationClassBean.totaldata+=notificationClassBean.getItems().get(g).getId()+","+chlidData+"|";
            }
        }
        Log.d("zw--d", NotificationClassBean.totaldata);
        setResult(1);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
           // selectClass();
        }
        return super.onKeyDown(keyCode, event);
    }

    class ExpandAdapter extends BaseExpandableListAdapter{
        @Override
        public int getGroupCount() {
            return notificationClassBean.getItems().size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return notificationClassBean.getItems().get(groupPosition).getClassX().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return notificationClassBean.getItems().get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return notificationClassBean.getItems().get(groupPosition).getClassX().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_course,null);
            CheckBox choice_image= (CheckBox) view.findViewById(R.id.choice_image);
            TextView class_name= (TextView) view.findViewById(R.id.class_name);
            ImageView group_image= (ImageView) view.findViewById(R.id.group_image);
            choice_image.setFocusable(false);
           // choice_image.setClickable(false);

            choice_image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        notificationClassBean.getItems().get(groupPosition).setExpand(true);
                        for(int i=0;i<notificationClassBean.getItems().get(groupPosition).getClassX().size();i++){
                            notificationClassBean.getItems().get(groupPosition).getClassX().get(i).setExpand(true);
                            NotificationClassBean.childList.put(String.valueOf(notificationClassBean.getItems().get(groupPosition).getClassX().get(i).getId()),notificationClassBean.getItems().get(groupPosition).getClassX().get(i).getCourse_id());
                        }
                        expandAdapter.notifyDataSetChanged();
                    }else {
                        notificationClassBean.getItems().get(groupPosition).setExpand(false);
                        for(int i=0;i<notificationClassBean.getItems().get(groupPosition).getClassX().size();i++){
                            notificationClassBean.getItems().get(groupPosition).getClassX().get(i).setExpand(false);
                            NotificationClassBean.childList.remove(String.valueOf(notificationClassBean.getItems().get(groupPosition).getClassX().get(i).getId()));
                        }
                        expandAdapter.notifyDataSetChanged();
                    }
                }
            });

            if(isExpanded){
                group_image.setImageResource(R.drawable.department_down);
            }else {
                group_image.setImageResource(R.drawable.department_go);
            }

            if(notificationClassBean.getItems().get(groupPosition).isExpand()){
                choice_image.setChecked(true);
            }else {
                choice_image.setChecked(false);
            }


            class_name.setText(notificationClassBean.getItems().get(groupPosition).getTitle());

            return view;
        }

        @Override
        public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_class,null);
            final CheckBox choice_image= (CheckBox) view.findViewById(R.id.choice_image);
            TextView class_name= (TextView) view.findViewById(R.id.class_name);
            TextView stu_number= (TextView) view.findViewById(R.id.stu_number);
            View bottom_line=view.findViewById(R.id.bottom_line);
            final NotificationClassBean.ItemsBean.CourseclasshasmanyBean classBean = notificationClassBean.getItems().get(groupPosition).getClassX().get(childPosition);
            if(childPosition==notificationClassBean.getItems().get(groupPosition).getClassX().size()-1){
                bottom_line.setVisibility(View.GONE);
            }
            class_name.setText(classBean.getClass_name());
            stu_number.setText(classBean.getUser_count()+"人");
            for(int i=0;i<NotificationClassBean.childList.size();i++){
                if(NotificationClassBean.childList.containsKey(String.valueOf(classBean.getId()))){
                    choice_image.setChecked(true);
                    classBean.setExpand(true);
                }
            }

            if(classBean.isExpand()){
                choice_image.setChecked(true);
            }else {
                choice_image.setChecked(false);
            }
            choice_image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        classBean.setExpand(true);
                        NotificationClassBean.childList.put(String.valueOf(classBean.getId()),classBean.getCourse_id());
                    }else {
                        classBean.setExpand(false);
                        NotificationClassBean.childList.remove(String.valueOf(classBean.getId()));
                        if(notificationClassBean.getItems().get(groupPosition).isExpand()){
                            notificationClassBean.getItems().get(groupPosition).setExpand(false);
                            expandAdapter.notifyDataSetChanged();
                        }

                    }


                    int childSize=0;
                    for(int i=0;i<notificationClassBean.getItems().get(groupPosition).getClassX().size();i++){
                        if(notificationClassBean.getItems().get(groupPosition).getClassX().get(i).isExpand()){
                            childSize++;
                        }
                        if(childSize==notificationClassBean.getItems().get(groupPosition).getClassX().size()){
                            notificationClassBean.getItems().get(groupPosition).setExpand(true);
                            expandAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
            return view;
        }
        private int i=0;
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            Log.d("zw--","nnnnnnnn");
            //首次进入页面notifi时候走这判断，需要展开；
            if(i==0){
                for(int g=0;g<notificationClassBean.getItems().size();g++){
                    w:  for(int j=0;j<notificationClassBean.getItems().get(g).getClassX().size();j++){
                        if(NotificationClassBean.childList.containsKey(notificationClassBean.getItems().get(g).getClassX().get(j).getId()+"")){
                            expand_class.expandGroup(g);
                            break w;
                        }
                    }
                }
                i++;
            }

        }
    }

}
