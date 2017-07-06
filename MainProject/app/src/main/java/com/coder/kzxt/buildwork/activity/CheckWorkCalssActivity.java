package com.coder.kzxt.buildwork.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpDeleteBuilder;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.entity.CheckWorkCalssBean;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckWorkCalssActivity extends AppCompatActivity implements HttpCallBack {
    private ListView class_list;
    private String titleString;
    private TextView title;
    private List<CheckWorkCalssBean.ItemsBean> itemsBeanList=new ArrayList<>();
    private Myadapter myadapter;
    private String course_id;
    private String test_paper_id;
    private int workType;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button load_fail_button;
    private MyPullSwipeRefresh myPullSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_work_calss);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        course_id=getIntent().getStringExtra("course_id");
        test_paper_id=getIntent().getStringExtra("testId");
        workType=getIntent().getIntExtra("workType",1);
        initViews();
        httpRequst();
        initEvents();
    }




    private void initViews() {
        myPullSwipeRefresh= (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        search_jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        search_jiazai_layout.setVisibility(View.VISIBLE);
        load_fail_layout = (LinearLayout)findViewById(R.id.load_fail_layout);
        load_fail_button = (Button)findViewById(R.id.load_fail_button);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        title = (TextView) findViewById(R.id.toolbar_title);
        titleString = getIntent().getStringExtra("title") == null ? "" : getIntent().getStringExtra("title");
        title.setText(titleString);
        class_list= (ListView) findViewById(R.id.class_list);
        myadapter=new Myadapter();
        class_list.setAdapter(myadapter);
    }
    private void initEvents() {
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemsBeanList.clear();
                httpRequst();
            }
        });
        class_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(CheckWorkCalssActivity.this,MyWorkActivity.class);
                intent.putExtra("testId",itemsBeanList.get(position).getId()+"");
                intent.putExtra("name",titleString);
                intent.putExtra("class_name",itemsBeanList.get(position).getCourse_class().getClass_name());
                intent.putExtra("course_id",course_id);
                intent.putExtra("finish_num",itemsBeanList.get(position).getFinished_number());
                intent.putExtra("unfinish_num",(itemsBeanList.get(position).getTotal_number()-itemsBeanList.get(position).getFinished_number()));
                intent.putExtra("workType",workType);
                startActivityForResult(intent,1);
            }
        });
        class_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(itemsBeanList.get(position).getStatus()!=1){
                    dialog();
                }else {
                    dialog(position);
                }

                return true;
            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_jiazai_layout.setVisibility(View.VISIBLE);
                load_fail_layout.setVisibility(View.GONE);
            }
        });
    }
    private void httpRequst() {
        new HttpGetBuilder(CheckWorkCalssActivity.this).setHttpResult(this)
                .setUrl(UrlsNew.TEST_PUBLISH)
                .setClassObj(CheckWorkCalssBean.class)
                .addQueryParams("page","1")
                .addQueryParams("pageSize","200")
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("course_id",course_id)
                .addQueryParams("type",workType+"")
                .addQueryParams("test_paper_id",test_paper_id)
                .build();

    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        myPullSwipeRefresh.setRefreshing(false);
        search_jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        CheckWorkCalssBean checkWorkCalssBean= (CheckWorkCalssBean) resultBean;
        itemsBeanList.addAll(checkWorkCalssBean.getItems());
        myadapter.notifyDataSetChanged();
        if(itemsBeanList.size()==0){
            no_info_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        myPullSwipeRefresh.setRefreshing(false);
        load_fail_layout.setVisibility(View.VISIBLE);
        search_jiazai_layout.setVisibility(View.GONE);
    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        builder.setMessage("该作业已被学生领取  无法删除");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
    protected void dialog(final int po) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        if(workType==1){
            builder.setMessage("删除后试卷对该班级学员不可见");
        }else {
            builder.setMessage("删除后作业对该班级学员不可见");

        }
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                if(itemsBeanList.get(po).getStatus()==2){
//                    if(workType==1){
//                        ToastUtils.makeText(CheckWorkCalssActivity.this,"考试已开始不能删除");
//                    }else {
//                        ToastUtils.makeText(CheckWorkCalssActivity.this,"作业已开始不能删除");
//                    }
//                    return ;
//                }
                search_jiazai_layout.setVisibility(View.VISIBLE);
                deleWork(po);
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

    private void deleWork(final int postion) {
            new HttpDeleteBuilder(this)
                    .setClassObj(null)
                    .setUrl(UrlsNew.URLHeader+"/test")
                    .setHttpResult(new HttpCallBack() {
                @Override
                public void setOnSuccessCallback(int requestCode, Object resultBean) {
                    search_jiazai_layout.setVisibility(View.GONE);
                    itemsBeanList.remove(postion);
                    myadapter.notifyDataSetChanged();
                    if (itemsBeanList.size()==0){
                        no_info_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void setOnErrorCallback(int requestCode, int code, String msg) {
                    search_jiazai_layout.setVisibility(View.GONE);
                    ToastUtils.makeText(CheckWorkCalssActivity.this,msg, Toast.LENGTH_SHORT).show();

                }
            })
                    .setPath(itemsBeanList.get(postion).getId()+"")
                    .build();

    }



    public class  Myadapter extends BaseAdapter{
        @Override
        public int getCount() {
            return itemsBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemsBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView= LayoutInflater.from(CheckWorkCalssActivity.this).inflate(R.layout.item_work_class,null);
            }
            TextView class_name= (TextView) convertView.findViewById(R.id.class_name);
            TextView time= (TextView) convertView.findViewById(R.id.time);
            TextView total_number= (TextView) convertView.findViewById(R.id.total_number);
            TextView number= (TextView) convertView.findViewById(R.id.number);
            TextView status= (TextView) convertView.findViewById(R.id.status);
            class_name.setText(itemsBeanList.get(position).getCourse_class().getClass_name());
            Long start = itemsBeanList.get(position).getStart_time();
            Long end = itemsBeanList.get(position).getEnd_time();
            SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd");
            String start_time = sdr.format(new Date(start * 1000L));
            String end_time = sdr.format(new Date(end * 1000L));
            time.setText("有效期至: "+start_time+"—"+end_time);
            number.setText(itemsBeanList.get(position).getFinished_number()+"");
            total_number.setText(itemsBeanList.get(position).getTotal_number()+"");
            if(itemsBeanList.get(position).getStatus()==1){
                status.setText("未开始");
                status.setTextColor(ContextCompat.getColor(parent.getContext(),R.color.font_gray));
            }
            if(itemsBeanList.get(position).getStatus()==2){
                status.setText("进行中");
                status.setTextColor(ContextCompat.getColor(parent.getContext(),R.color.font_green));
            }
            if(itemsBeanList.get(position).getStatus()==3){
                status.setText("已过期");
                status.setTextColor(ContextCompat.getColor(parent.getContext(),R.color.font_gray));
            }
            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            itemsBeanList.clear();
            httpRequst();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
