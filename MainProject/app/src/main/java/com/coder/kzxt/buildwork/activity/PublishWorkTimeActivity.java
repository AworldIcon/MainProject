package com.coder.kzxt.buildwork.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.buildwork.entity.ClassPublish;
import com.coder.kzxt.buildwork.entity.CourseAllClassBean;
import com.coder.kzxt.buildwork.entity.WorkOrderBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.MyApplication;
import com.coder.kzxt.utils.ScreenUtils;
import com.coder.kzxt.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PublishWorkTimeActivity extends BaseActivity implements View.OnClickListener, HttpCallBack {
    private TextView title,confirm,cancle,t1,t2;
    private RelativeLayout start,end;
    private String isCenter,testId,courseId,work_name;
    private String ids,startTime,endTime;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button load_fail_button;
    private int workType;
    private ImageView end_jiantou,start_jiantou;
    private PopupWindow pop;
    private View view;
    private int po=0;
    private ListView select_class;
    private List<ClassPublish>list=new ArrayList<>();
    private ClassAdapter classAdapter;
    private Button publish;
    private TextView selectAll;
    private WorkOrderBean workOrderBean;
    private CourseAllClassBean allClassBean;
    private RelativeLayout all_view;
    private TimePicker timePicker1;
    private DatePicker datePicker;
    private RelativeLayout limit_layout;
    private TextView limitText;
    private LinearLayout bottom_layout;
    private boolean isAllPublish=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_work_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_up);
        toolbar.setNavigationOnClickListener(this);
//        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        isCenter=getIntent().getStringExtra(Constants.IS_CENTER);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ids=getIntent().getStringExtra("id");
        workType=getIntent().getIntExtra("workType",2);
        work_name=getIntent().getStringExtra("title");
        testId=getIntent().getStringExtra("testId");
        courseId=getIntent().getStringExtra("courseId");
        classAdapter=new ClassAdapter();
        mint=new String[200];
        for(int i=0;i<200;i++){
            mint[i]=(i+1)+"分钟";
        }
        initView();
        initEvents();
        getAlreadyPublishClass();

    }
    //public double getX(){}
    private String[]mint;
    private void initView() {
        MyApplication.workList.add(this);
        limit_layout= (RelativeLayout) findViewById(R.id.limt_time);
        bottom_layout= (LinearLayout) findViewById(R.id.bottom_layout);
        limitText= (TextView) findViewById(R.id.limitTime);
        all_view= (RelativeLayout) findViewById(R.id.all_view);
        select_class= (ListView) findViewById(R.id.select_class);
        publish= (Button) findViewById(R.id.publish);
        GradientDrawable myGrad = (GradientDrawable) publish.getBackground();
        myGrad.setColor(getResources().getColor(R.color.first_theme));// 设置内部颜色

        selectAll= (TextView) findViewById(R.id.select_all);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info);
        start_jiantou = (ImageView) findViewById(R.id.start_jiantou);
        end_jiantou = (ImageView) findViewById(R.id.end_jiantou);
        title = (TextView) findViewById(R.id.toolbar_title);
        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        start= (RelativeLayout) findViewById(R.id.start_time);
        end= (RelativeLayout) findViewById(R.id.end_time);
        if(workType==1){
            title.setText(work_name+"-考试");
            limit_layout.setVisibility(View.VISIBLE);
        }else {
            title.setText(work_name+"-作业");

        }
        title.setText(R.string.fabu);
        view= LayoutInflater.from(PublishWorkTimeActivity.this).inflate(R.layout.time_picker_pop,null);
        timePicker1= (TimePicker) view.findViewById(R.id.timePicker);
        datePicker= (DatePicker) view.findViewById(R.id.datePicker);
        resizePikcer(timePicker1);
        resizePikcer(datePicker);
        timePicker1.setIs24HourView(true);
        confirm= (TextView) view.findViewById(R.id.confirm);
        cancle= (TextView) view.findViewById(R.id.cancel);
        pop=new PopupWindow(PublishWorkTimeActivity.this);
        pop.setContentView(view);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(getResources().getDrawable(R.color.trans_half));
        pop.setFocusable(true);
        //初始化时间
        t1.setText(getDate());
        t2.setText(getDate());
        startTime=getDate();
        endTime=getDate();
    }



    public void getAlreadyPublishClass(){
        jiazai_layout.setVisibility(View.VISIBLE);
        new HttpGetBuilder(this).setHttpResult(this)
                .setUrl(UrlsNew.TEST_PAPER_PUBLISH)
                .setClassObj(WorkOrderBean.class)
                .setPath(testId)
                .setRequestCode(1001)
                .build();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if(requestCode==1001){
            workOrderBean= (WorkOrderBean) resultBean;
            getclassData();
        }
        if(requestCode==1002){
            jiazai_layout.setVisibility(View.GONE);
            allClassBean= (CourseAllClassBean) resultBean;
            for(int i=0;i<allClassBean.getItems().size();i++){
                ClassPublish classPublish=new ClassPublish();
                classPublish.setClassId(String.valueOf(allClassBean.getItems().get(i).getId()));
                classPublish.setClassName(allClassBean.getItems().get(i).getClass_name());
                classPublish.setStuNum(String.valueOf(allClassBean.getItems().get(i).getUser_count()));
                for(int w=0;w<workOrderBean.getItem().getPublished_to_class().size();w++){
                    if(workOrderBean.getItem().getPublished_to_class().get(w)==allClassBean.getItems().get(i).getId()){
                        classPublish.setChecked(true);
                        classPublish.setHasPublished(true);
                    }
                }
                list.add(classPublish);
            }
            if(workOrderBean.getItem().getPublished_to_class().size()==allClassBean.getItems().size()){
                selectAll.setVisibility(View.GONE);
                isAllPublish=true;
            }
            select_class.setAdapter(classAdapter);
            if(list.size()>5){
                select_class.getLayoutParams().height= ScreenUtils.getScreenHeight(this)/3;
            }
            if(list.size()==0){
                all_view.setVisibility(View.GONE);
                no_info_layout.setVisibility(View.VISIBLE);
            }else {
                all_view.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.VISIBLE);
        ToastUtils.makeText(this,msg);
    }
    private void getclassData() {
        new HttpGetBuilder(this).setHttpResult(this)
                .setUrl(UrlsNew.GET_COURSE_CLASS)
                .setClassObj(CourseAllClassBean.class)
                .addQueryParams("page","1")
                .addQueryParams("pageSize","100")
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("course_id",courseId)
                .setRequestCode(1002)
                .build();
    }
    /*
		 * 调整numberpicker大小
		 */
    private void resizeNumberPicker(NumberPicker np){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 0,30, 0);
        np.setLayoutParams(params);
    }
    /**
     * 调整FrameLayout大小
     * @param tp
     */
    private void resizePikcer(FrameLayout tp){
        List<NumberPicker> npList = findNumberPicker(tp);
        for(NumberPicker np:npList){
            resizeNumberPicker(np);
        }
    }
    /**
     * 得到viewGroup里面的numberpicker组件
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup){
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if(null != viewGroup){
            for(int i = 0;i<viewGroup.getChildCount();i++){
                child = viewGroup.getChildAt(i);
                if(child instanceof NumberPicker){
                    npList.add((NumberPicker)child);
                }
                else if(child instanceof LinearLayout){
                    List<NumberPicker> result = findNumberPicker((ViewGroup)child);
                    if(result.size()>0){
                        return result;
                    }
                }
            }
        }
        return npList;
    }
    protected void dialog(View view, final NumberPicker numberPicker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PublishWorkTimeActivity.this,R.style.custom_dialog);
        builder.setView(view);
        builder.setTitle("请选择考试时限");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                limitText.setText("考试限时:"+mint[numberPicker.getValue()-1]);
                value=numberPicker.getValue();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //value=numberPicker.getValue();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private String getDate(){
        String year=datePicker.getYear()+"年";
        String month=(datePicker.getMonth()+1)+"月";
        String day=datePicker.getDayOfMonth()+"日  ";
        String hour;
        if(timePicker1.getCurrentHour()/10==0){
            hour="0"+timePicker1.getCurrentHour()+":";
        }else {
            hour=String.valueOf(timePicker1.getCurrentHour())+":";
        }
        String minute="";
        if(timePicker1.getCurrentMinute()/10==0){
            minute="0"+timePicker1.getCurrentMinute();
        }else {
            minute=String.valueOf(timePicker1.getCurrentMinute());
        }

        return year+month+day+hour+minute;
    }
    private int value=60;
    private void initEvents() {
        limit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllPublish){
                    dialog();
                    return;
                }
                View view1=LayoutInflater.from(PublishWorkTimeActivity.this).inflate(R.layout.exam_limit_time,null);
                NumberPicker numberPicker= (NumberPicker) view1.findViewById(R.id.numberPicker);
                numberPicker.setDisplayedValues(mint);
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(mint.length);
                numberPicker.setValue(value);
                dialog(view1,numberPicker);

            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlreadyPublishClass();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllPublish){
                    dialog();
                    return;
                }
                po=1;
                view.setAnimation(AnimationUtils.loadAnimation(PublishWorkTimeActivity.this,R.anim.push_bottom_in_2));
                start_jiantou.setImageResource(R.drawable.department_down);
                pop.showAtLocation(findViewById(R.id.activity_publish_work_time), Gravity.BOTTOM, 0, 0);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllPublish){
                    dialog();
                    return;
                }
                po=2;
                view.setAnimation(AnimationUtils.loadAnimation(PublishWorkTimeActivity.this,R.anim.push_bottom_in_2));
                end_jiantou.setImageResource(R.drawable.department_down);
                pop.showAtLocation(findViewById(R.id.activity_publish_work_time), Gravity.BOTTOM, 0, 0);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                if(po==1){
                    start_jiantou.setImageResource(R.drawable.department_go);
                    startTime=getDate();
                    t1.setText(startTime);
                }
                if(po==2){
                    end_jiantou.setImageResource(R.drawable.department_go);
                    endTime=getDate();
                    t2.setText(endTime);
                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                start_jiantou.setImageResource(R.drawable.department_go);
                end_jiantou.setImageResource(R.drawable.department_go);
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllPublish){
                    dialog();
                    return;
                }
//                Log.d("zw--s",Long.parseLong(data(endTime+":00"))-Long.parseLong(data(startTime+":00"))+"endTime-"+endTime+"startTime-"+startTime);
                if(workType==1&&Long.parseLong(data(endTime+":00"))-Long.parseLong(data(startTime+":00"))<value*60){
                    ToastUtils.makeText(PublishWorkTimeActivity.this,"考试时限不能大于有效时段",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Long.parseLong(data(endTime+":00"))-Long.parseLong(data(startTime+":00"))<300){
                    ToastUtils.makeText(PublishWorkTimeActivity.this,"结束时间应大于开始时间5分钟",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(t1.getText())||TextUtils.isEmpty(t2.getText())){
                    ToastUtils.makeText(PublishWorkTimeActivity.this,"时间不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    jiazai_layout.setVisibility(View.VISIBLE);
                    if(workType==2){
                        value=0;
                    }
                    sumbit_work(ids);

                }
            }
        });
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectAll.getText().equals("全选")){
                    for(int i=0;i<list.size();i++){
                        if(!list.get(i).isHasPublished()){
                            list.get(i).setChecked(true);
                        }
                    }
                    selectAll.setText("取消全选");
                }else {
                    for(int i=0;i<list.size();i++){
                        if(!list.get(i).isHasPublished()) {
                            list.get(i).setChecked(false);
                        }
                    }
                    selectAll.setText("全选");
                }

                classAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onClick(View v) {
        MyApplication.workList.get(MyApplication.workList.size()-1).finish();
        MyApplication.workList.remove(MyApplication.workList.size()-1);

    }
    //得到时间戳
    public String data(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }


    private void sumbit_work(String ids) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
       String startTime1=format.format(new Date(Long.valueOf(data(startTime+":00")) * 1000L));
        String endTime1=format.format(new Date(Long.valueOf(data(endTime+":00")) * 1000L));
        Log.d("zw--",startTime1+"**"+endTime1);
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<list.size();i++){
            if(!list.get(i).isHasPublished()&&list.get(i).isChecked()){
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("type",workType+"");
                    jsonObject.put("title",work_name);
                    jsonObject.put("class_id",list.get(i).getClassId());
                    jsonObject.put("course_id",courseId);
                    jsonObject.put("test_paper_id",testId);
                    jsonObject.put("start_time",startTime1);
                    jsonObject.put("end_time",endTime1);
                    jsonObject.put("limit_time",value*60);
                    jsonObject.put("allow_see_answer","1");
                    jsonObject.put("allow_see_score","1");
                    jsonObject.put("allow_copy","1");
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        if(jsonArray.length()==0){
            jiazai_layout.setVisibility(View.GONE);
            ToastUtils.makeText(this,"至少选择一个班级",Toast.LENGTH_SHORT).show();
            return;
        }else {
            new HttpPostBuilder(PublishWorkTimeActivity.this).setClassObj(null).setHttpResult(new HttpCallBack() {
                @Override
                public void setOnSuccessCallback(int requestCode, Object resultBean) {
                    jiazai_layout.setVisibility(View.GONE);
                    Intent intent=new Intent();
                    intent.setAction("com.coder.kzxt.activity.time");
                    sendBroadcast(intent);
                    for(int i = 0; i< MyApplication.workList.size(); i++){
                        MyApplication.workList.get(i).finish();
                        if(i<MyApplication.workList.size()){
                            MyApplication.workList.remove(i);
                        }
                    }
                }

                @Override
                public void setOnErrorCallback(int requestCode, int code, String msg) {
                    jiazai_layout.setVisibility(View.GONE);

                }
            }).setUrl(UrlsNew.URLHeader+"/test/batch")
                    .addBodyParam(jsonArray.toString())
                    .build();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.tea_work_menu, menu);
       // menu.findItem(R.id.action_setting).setTitle("发布");
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MyApplication.workList.get(MyApplication.workList.size()-1).finish();
            MyApplication.workList.remove(MyApplication.workList.size()-1);
        }
        return super.onKeyDown(keyCode, event);
    }


    public class ClassAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view=LayoutInflater.from(PublishWorkTimeActivity.this).inflate(R.layout.publish_class_item,null);
            final CheckBox check= (CheckBox) view.findViewById(R.id.check);
            TextView class_name= (TextView) view.findViewById(R.id.class_name);
            TextView stu_num= (TextView) view.findViewById(R.id.stu_num);
            RelativeLayout back= (RelativeLayout) view.findViewById(R.id.back);
            class_name.setText(list.get(position).getClassName());
            stu_num.setText(list.get(position).getStuNum()+"人");
            if(list.get(position).isChecked()){
                check.setChecked(true);
            }else {
                check.setChecked(false);
            }
            if(list.get(position).isHasPublished()){
//                back.setBackgroundColor(ContextCompat.getColor(PublishWorkTimeActivity.this,R.color.bg_gray));
                check.setVisibility(View.INVISIBLE);
                check.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            }
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        list.get(position).setChecked(true);
                    }else {
                        list.get(position).setChecked(false);
                    }

                }
            });
            return view;
        }
    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        if(workType==1){
            builder.setMessage("此试卷已对所有班级发布过，不能重复发布！");
        }else {
            builder.setMessage("此作业已对所有班级发布过，不能重复发布！");
        }
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

    //    private void setDatePickerDividerColor(DatePicker datePicker){
//        // Divider changing:
//
//        // 获取 mSpinners
//        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
//
//        // 获取 NumberPicker
//        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
//        for (int i = 0; i < mSpinners.getChildCount(); i++) {
//            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);
//
//            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
//            for (Field pf : pickerFields) {
//                if (pf.getName().equals("mSelectionDivider")) {
//                    pf.setAccessible(true);
//                    try {
//                        pf.set(picker, new ColorDrawable(this.getResources().getColor(R.color.font_red)));
//                    } catch (IllegalArgumentException e) {
//                        e.printStackTrace();
//                    } catch (Resources.NotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                }
//            }
//        }
//    }
}
