package com.coder.kzxt.buildwork.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpDeleteBuilder;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.entity.SingleChoice;
import com.coder.kzxt.buildwork.entity.WorkOrderBean;
import com.coder.kzxt.buildwork.views.DragView;
import com.coder.kzxt.dialog.util.CustomDialog;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublishWorkActivity extends BaseActivity implements HttpCallBack {
    private ImageView back_title_button,change_name;
    private RelativeLayout activity_publish_work;
    private TextView title;
    private TextView rightText;
    private DragView course_listview;
    private Button load_fail_button;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private String classId;
    private String courseId;
    private String testId;
    private String isCenter = "0";
    private List<SingleChoice> single_choice_list;
    private List<SingleChoice>choice_list;
    private List<SingleChoice>determine;
    private List<SingleChoice>fill_list;
    private List<SingleChoice>essay;
    private static List<SingleChoice>list;
    private static List<String> keyGroup ;
    private View pop_view;
    private RelativeLayout selfLayout,free_choice_work,single_choice,choices,fill,determine_layout,essay_layout,dissmiss;
    private LinearLayout bottom_layout;
    private Button hand,bankQuestion;
    private PopupWindow pop;
    private String work_name="";
    private static int number_total;
    private int count;//题库试题数量
    private static String score_full="";
    private String questionId="";
    private String detail;
    private ListView listView;
    private int workType;
    private int no_right;//只有从我已发布的作业考试列表跳转来的 1，隐藏发布按钮
    private int status;//2代表此作业未发布到任何班级中，此时可以修改作业，但是只能判断当前自己，无法识别在这过程中别的老师是否发布此作业，还需服务端判断
    private boolean issendBroad=false;
    private HashMap<String,Integer> poMap;
    private String poType="";
    private String url="http://192.168.3.6:88/Mobile/Index/previewTestAction?&mid=1099359&oauth_token=b0863cd2e605225aa99628051c7a1b8f&oauth_token_secret=203c32beb54c11e8e5c2d3da1722ffe6&courseId=4533&testId=2484&deviceId=861735031544595&publicCourse=0";
    private DragListAdapter dragListAdapter;
    private static int poA,poB=0,poC,poD,poE;
    private MyReceiver myReceiver;
    private List<List<SingleChoice>>totalList=new ArrayList<>();
    private List<Integer>question_type_sort=new ArrayList<>();//代表此时卷有几种题型以及题型展示顺序
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_work);
        myReceiver=new MyReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.coder.kzxt.activity.PublishWorkActivity");
        intentFilter.addAction("com.coder.kzxt.activity.time");
        registerReceiver(myReceiver,intentFilter);
        keyGroup = new ArrayList<String>();
        single_choice_list=new ArrayList<>();
        choice_list=new ArrayList<>();
        determine=new ArrayList<>();
        fill_list=new ArrayList<>();
        essay=new ArrayList<>();
        list=new ArrayList<>();
        isCenter = getIntent().getStringExtra(Constants.IS_CENTER);
        detail=getIntent().getStringExtra("detail");
        testId=getIntent().getStringExtra("testId");
        courseId=getIntent().getStringExtra("courseId");
        work_name=getIntent().getStringExtra("title");
        workType=getIntent().getIntExtra("workType",2);
        status=getIntent().getIntExtra("status",2);
        no_right=getIntent().getIntExtra("no_right",0);
        L.d("zw--s",status+"***"+detail);
        activity_publish_work= (RelativeLayout) findViewById(R.id.popu_window);
        back_title_button = (ImageView) findViewById(R.id.leftImage);
        change_name = (ImageView) findViewById(R.id.change_name);
        bottom_layout= (LinearLayout) findViewById(R.id.bottom_layout);

        hand= (Button) findViewById(R.id.hand_build);
        bankQuestion= (Button) findViewById(R.id.bank_build);
        title = (TextView) findViewById(R.id.title);
        rightText = (TextView) findViewById(R.id.rightText);
        course_listview = (DragView) findViewById(R.id.dragView);
        listView= (ListView) findViewById(R.id.listview);
        search_jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info);
        pop_view= LayoutInflater.from(PublishWorkActivity.this).inflate(R.layout.self_build_work_item,null);
        single_choice= (RelativeLayout)pop_view.findViewById(R.id.single_choice);
        choices= (RelativeLayout) pop_view.findViewById(R.id.choices);
        fill= (RelativeLayout) pop_view.findViewById(R.id.fill);
        determine_layout= (RelativeLayout) pop_view.findViewById(R.id.determine);
        essay_layout= (RelativeLayout) pop_view.findViewById(R.id.essay);
        dissmiss= (RelativeLayout)pop_view. findViewById(R.id.dissmiss);
        if(workType==1){
            title.setText(work_name+"-考试");
        }else {
            title.setText(work_name+"-作业");

        }

        if(detail==null&&status==2){//试卷创建者跳转时候没有传值，所以是null
            rightText.setText("发布");
            listView.setVisibility(View.GONE);
            course_listview.setVisibility(View.VISIBLE);
            course_listview.setBackgroundResource(R.color.bg_gray);
            course_listview.setDividerHeight(0);
            dragListAdapter=new DragListAdapter(PublishWorkActivity.this,list);
            course_listview.setAdapter(dragListAdapter);
        }else if(detail!=null&&detail.equals("my_work")){
            //由查看发布作业详情事件跳转
            change_name.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            bottom_layout.setVisibility(View.GONE);
            course_listview.setVisibility(View.GONE);
            listView.setBackgroundResource(R.color.bg_gray);
            listView.setDividerHeight(0);
            dragListAdapter=new DragListAdapter(PublishWorkActivity.this,list);
            listView.setAdapter(dragListAdapter);
        }else if((detail==null&&status!=2)||detail.equals("not_self")){//若果是创建者但是已经发过，也不让任何修改
            rightText.setText("发布");
            change_name.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            bottom_layout.setVisibility(View.GONE);
            course_listview.setVisibility(View.GONE);
            listView.setBackgroundResource(R.color.bg_gray);
            listView.setDividerHeight(0);
            dragListAdapter=new DragListAdapter(PublishWorkActivity.this,list);
            listView.setAdapter(dragListAdapter);
        }
        if(no_right==1){
            rightText.setVisibility(View.GONE);
        }
        gethttpRequstmodel();
        initClick();
    }
    private CustomDialog customNewDialog;

    private void initClick() {
        load_fail_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gethttpRequstmodel();
            }
        });
        change_name.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                customNewDialog = new CustomDialog(PublishWorkActivity.this);
                View view=LayoutInflater.from(PublishWorkActivity.this).inflate(R.layout.add_work_layout,null);
                TextView sure_btn= (TextView) view.findViewById(R.id.btn_sure);
                TextView nagtive_btn= (TextView) view.findViewById(R.id.btn_false);
                final EditText newName= (EditText) view.findViewById(R.id.title);
                if(workType==1){
                    newName.setHint(R.string.build_exame_NewName);
                    newName.setText(work_name);
                }else {
                    newName.setHint(R.string.build_work_NewName);
                    newName.setText(work_name);
                }
                customNewDialog.setRightTextColor(ContextCompat.getColor(PublishWorkActivity.this,R.color.font_blue));
                final Window dialogWindow = customNewDialog.getWindow();
                dialogWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialogWindow.setGravity(Gravity.CENTER);
                customNewDialog.setContentView(view);
                customNewDialog.show();
                sure_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(newName.getText())){
                            search_jiazai_layout.setVisibility(View.VISIBLE);
                            changeNameHttp(newName.getText().toString());
                            customNewDialog.dismiss();
                        }else {
                            Toast.makeText(PublishWorkActivity.this,"标题不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                nagtive_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customNewDialog.dismiss();
                    }
                });
            }
        });
        back_title_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });

        rightText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(dragListAdapter.getCount()!=0){
                    //发布前需要先将此试卷的排序提交服务器，成功后跳转
                    if(detail==null&&status==2){
                        search_jiazai_layout.setVisibility(View.VISIBLE);
                        rightText.setClickable(false);
                        //如果是创建者而且是未发布的试卷，因为可以更改试题顺序，所以请求如下接口，其它情况不需要
                        updateQuestions();
                    }else {
                        Intent intent=new Intent(PublishWorkActivity.this,PublishWorkTimeActivity.class);
                        intent.putExtra("testId",testId);
                        intent.putExtra("title",work_name);
                        intent.putExtra("workType",workType);
                        intent.putExtra("courseId",courseId);
                        startActivity(intent);
                    }

                }else {
                    Toast.makeText(PublishWorkActivity.this,"试题不能为空",Toast.LENGTH_SHORT).show();
                }

            }
        });
        course_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(detail==null&&status==2){//未发布的让删除
                    L.d("tag-ids2",dragListAdapter.getItem(position).getId()+"&&"+position);
                    dialog(dragListAdapter.getItem(position).getId());
                }


                return true;
            }
        });
        course_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(detail==null&&status==2){//自己的但发布过也不让修改那就传details
                    SingleChoice singleChoice=dragListAdapter.getItem(position);
                    Intent intent=null;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("detail", singleChoice);
                    if(singleChoice.getQuestionType().equals("single_choice")||singleChoice.getQuestionType().equals("choice")){
                        intent =new Intent(PublishWorkActivity.this,SingleChoiceActivity.class);
                        if(singleChoice.getType()==null&&singleChoice.getQuestionType().equals("single_choice")){
                            bundle.putString("choice","choice");
                        }else  if(singleChoice.getType()==null&&singleChoice.getQuestionType().equals("choice")){
                            bundle.putString("choice","choices");
                        }
                    }
                    if(singleChoice.getType()==null&&singleChoice.getQuestionType().equals("determine")){
                        intent =new Intent(PublishWorkActivity.this,QuestionDetermineActivity.class);
                    }
                    if(singleChoice.getType()==null&&singleChoice.getQuestionType().equals("fill")){
                        intent =new Intent(PublishWorkActivity.this,QuestionFillActivity.class);
                    }
                    if(singleChoice.getType()==null&&singleChoice.getQuestionType().equals("essay")){
                        intent =new Intent(PublishWorkActivity.this,QuestionEssayActivity.class);
                    }
                    bundle.putString("testId",testId);
                    bundle.putString("courseId",courseId);
                    bundle.putString("title",work_name);
                    bundle.putInt("workType",workType);
                    bundle.putString("itemId",singleChoice.getId());
                    bundle.putString("publicCourse",isCenter);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SingleChoice singleChoice=dragListAdapter.getItem(position);
                        Intent intent=null;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("detail", singleChoice);
                        if(singleChoice.getQuestionType().equals("single_choice")||singleChoice.getQuestionType().equals("choice")){
                            intent =new Intent(PublishWorkActivity.this,SingleChoiceActivity.class);
                            //==null是判断点击的是题还是题干
                            if(singleChoice.getType()==null&&singleChoice.getQuestionType().equals("single_choice")){
                                bundle.putString("choice","choice");
                            }else  if(singleChoice.getType()==null&&singleChoice.getQuestionType().equals("choice")){
                                bundle.putString("choice","choices");
                            }
                        }
                        if(singleChoice.getType()==null&&singleChoice.getQuestionType().equals("determine")){
                            intent =new Intent(PublishWorkActivity.this,QuestionDetermineActivity.class);
                        }
                        if(singleChoice.getType()==null&&singleChoice.getQuestionType().equals("fill")){
                            intent =new Intent(PublishWorkActivity.this,QuestionFillActivity.class);
                        }
                        if(singleChoice.getType()==null&&singleChoice.getQuestionType().equals("essay")){
                            intent =new Intent(PublishWorkActivity.this,QuestionEssayActivity.class);
                        }
                        bundle.putString("testId",testId);
                        bundle.putString("courseId",courseId);
                        bundle.putString("title",work_name);
                        bundle.putInt("workType",workType);
                        bundle.putString("itemId",singleChoice.getId());
                        bundle.putString("publicCourse",isCenter);
                        bundle.putString("details","not_self");
                    if(detail==null&&status!=2){//如果是创建者本人
                        bundle.putInt("status",3);
                    }
                        intent.putExtras(bundle);
                        startActivity(intent);

                }
            });


        /**
         * 手动建题
         * */
        hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop=new PopupWindow(PublishWorkActivity.this);
                pop.setContentView(pop_view);
                pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                pop.setBackgroundDrawable(getResources().getDrawable(R.color.trans_half));
                pop.setFocusable(true);
                pop_view.setAnimation(AnimationUtils.loadAnimation(PublishWorkActivity.this,R.anim.push_bottom_in_2));
                pop.showAtLocation(findViewById(R.id.popu_window), Gravity.BOTTOM, 0, 0);

            }
        });
        single_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                Intent intent=new Intent(PublishWorkActivity.this,SingleChoiceActivity.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("testId",testId);
                intent.putExtra("choice","choice");
                intent.putExtra("title",work_name);
                intent.putExtra("PublicCourse",isCenter);
                startActivity(intent);
            }
        });
        choices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                Intent intent=new Intent(PublishWorkActivity.this,SingleChoiceActivity.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("testId",testId);
                intent.putExtra("choice","choices");
                intent.putExtra("title",work_name);
                intent.putExtra("PublicCourse",isCenter);
                startActivity(intent);
            }
        });
        fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                Intent intent=new Intent(PublishWorkActivity.this,QuestionFillActivity.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("testId",testId);
                intent.putExtra("title",work_name);
                intent.putExtra("PublicCourse",isCenter);
                startActivity(intent);
            }
        });
        determine_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                Intent intent=new Intent(PublishWorkActivity.this,QuestionDetermineActivity.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("testId",testId);
                intent.putExtra("title",work_name);
                intent.putExtra("PublicCourse",isCenter);
                startActivity(intent);

            }
        });
        essay_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                Intent intent=new Intent(PublishWorkActivity.this,QuestionEssayActivity.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("testId",testId);
                intent.putExtra("title",work_name);
                intent.putExtra("PublicCourse",isCenter);
                startActivity(intent);
            }
        });
        dissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        /**
         * 题库选题
         * */
        bankQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number_total>=count){
                    //题库选题
                    Intent intent=new Intent(PublishWorkActivity.this,QuestionBankActivity.class);
                    intent.putExtra("courseId",courseId);
                    intent.putExtra("testId",testId);
                    intent.putExtra("title",work_name);
                    intent.putExtra("PublicCourse",isCenter);
                    startActivity(intent);

                }else {
                    Toast.makeText(PublishWorkActivity.this,"题库暂无试题",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //删除作业试题的dialog
    protected void dialog(String id) {
            questionId=id;
            AlertDialog.Builder builder = new AlertDialog.Builder(PublishWorkActivity.this,R.style.custom_dialog);
            builder.setMessage("确认删除此题目？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    poType="";
                    search_jiazai_layout.setVisibility(View.VISIBLE);
                    deleWork();
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

    private void deleWork() {
        new HttpDeleteBuilder(this).setClassObj(null).setUrl(UrlsNew.URLHeader+"/test_paper/question/").setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                totalList.clear();
                question_type_sort.clear();
                single_choice_list.clear();
                choice_list.clear();
                determine.clear();
                fill_list.clear();
                essay.clear();
                list.clear();
                search_jiazai_layout.setVisibility(View.GONE);
                issendBroad=true;
                gethttpRequstmodel();

            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                search_jiazai_layout.setVisibility(View.GONE);
                ToastUtils.makeText(PublishWorkActivity.this,msg);

            }
        }).setPath(questionId).build();
    }
    public  void changeNameHttp(final String name){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("title",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpPutBuilder(this).setClassObj(null).setUrl(UrlsNew.URLHeader+"/test_paper/").setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                search_jiazai_layout.setVisibility(View.GONE);
                work_name=name;
                if(workType==1){
                    title.setText(work_name+"-考试");
                }else {
                    title.setText(work_name+"-作业");
                }
                sendBroadcast(new Intent().setAction("com.coder.kzxt.activity.PublishWorkActivity"));
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                search_jiazai_layout.setVisibility(View.GONE);
                ToastUtils.makeText(PublishWorkActivity.this,msg);
                if(code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
                    //重新登录
                    NetworkUtil.httpRestartLogin(PublishWorkActivity.this,activity_publish_work);
                }

            }
        }).setPath(testId).addBodyParam(jsonObject.toString()).build();
    }

    private String[]type=new String[]{"A","B","C","D","E"};
    private List<List<SingleChoice>>lists=new ArrayList<>();
    public List<SingleChoice> getSingle_choice_list(JSONObject object){
        try {
            if(object.getString("type").equals("1")){
                SingleChoice singleChoice=new SingleChoice();
                singleChoice.setId(object.optString("id",""));
                singleChoice.setStem_content(object.optString("title",""));
                singleChoice.setScore(object.optString("score",""));
                singleChoice.setDiffculty(object.optString("difficulty","1"));
                singleChoice.setQuestionType("single_choice");
                List<String>answers=new ArrayList<>();
                List<String>option=new ArrayList<>();
                JSONArray array=object.getJSONArray("answer");
                JSONArray array_option=object.getJSONArray("option");
                for(int a=0;a<array.length();a++){
                    answers.add(array.get(a).toString());
                }
                for(int a=0;a<array_option.length();a++){
                    option.add(array_option.get(a).toString());
                }
                singleChoice.setAnswer(answers);
                singleChoice.setChoices(option);
                single_choice_list.add(singleChoice);
            }

            return  single_choice_list;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<SingleChoice> get_fill_list(JSONObject object){
        try {
            if(object.getString("type").equals("3")){
                SingleChoice singleChoice=new SingleChoice();
                singleChoice.setId(object.optString("id",""));
                singleChoice.setStem_content(object.optString("title",""));
                singleChoice.setScore(object.optString("score",""));
                singleChoice.setDiffculty(object.optString("difficulty","1"));
                singleChoice.setQuestionType("fill");
                List<String>answers=new ArrayList<>();
                List<String>option=new ArrayList<>();
                JSONArray array=object.getJSONArray("answer");
                JSONArray array_option=object.getJSONArray("option");
                for(int a=0;a<array.length();a++){
                    answers.add(array.get(a).toString());
                }
                for(int a=0;a<array_option.length();a++){
                    option.add(array_option.get(a).toString());
                }
                singleChoice.setAnswer(answers);
                singleChoice.setChoices(option);
                fill_list.add(singleChoice);
            }
            L.d("tag-ids-",fill_list.size()+"#2");

            return  fill_list;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<SingleChoice> get_choice_list(JSONObject object){
        try {
            if(object.getString("type").equals("2")){
                SingleChoice singleChoice=new SingleChoice();
                singleChoice.setId(object.optString("id",""));
                singleChoice.setStem_content(object.optString("title",""));
                singleChoice.setScore(object.optString("score",""));
                singleChoice.setDiffculty(object.optString("difficulty","1"));
                singleChoice.setQuestionType("choice");
                List<String>answers=new ArrayList<>();
                List<String>option=new ArrayList<>();
                JSONArray array=object.getJSONArray("answer");
                JSONArray array_option=object.getJSONArray("option");
                for(int a=0;a<array.length();a++){
                    answers.add(array.get(a).toString());
                }
                for(int a=0;a<array_option.length();a++){
                    option.add(array_option.get(a).toString());
                }
                singleChoice.setAnswer(answers);
                singleChoice.setChoices(option);
                choice_list.add(singleChoice);
            }
            L.d("tag-ids-",choice_list.size()+"#3");

            return  choice_list;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<SingleChoice> get_determine(JSONObject object){
        try {
            if(object.getString("type").equals("4")){
                SingleChoice singleChoice=new SingleChoice();
                singleChoice.setId(object.optString("id",""));
                singleChoice.setStem_content(object.optString("title",""));
                singleChoice.setScore(object.optString("score",""));
                singleChoice.setDiffculty(object.optString("difficulty","1"));
                singleChoice.setQuestionType("determine");
                List<String>answers=new ArrayList<>();
                List<String>option=new ArrayList<>();
                JSONArray array=object.getJSONArray("answer");
                JSONArray array_option=object.getJSONArray("option");
                for(int a=0;a<array.length();a++){
                    answers.add(array.get(a).toString());
                }
                for(int a=0;a<array_option.length();a++){
                    option.add(array_option.get(a).toString());
                }
                singleChoice.setAnswer(answers);
                singleChoice.setChoices(option);
                determine.add(singleChoice);
            }
            L.d("tag-ids-",determine.size()+"#4");
            return  determine;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<SingleChoice> get_essay(JSONObject object){
        try {
            if(object.getString("type").equals("5")){
                SingleChoice singleChoice=new SingleChoice();
                singleChoice.setId(object.optString("id",""));
                singleChoice.setStem_content(object.optString("title",""));
                singleChoice.setScore(object.optString("score",""));
                singleChoice.setDiffculty(object.optString("difficulty","1"));
                singleChoice.setQuestionType("essay");
                List<String>answers=new ArrayList<>();
                List<String>option=new ArrayList<>();
                JSONArray array=object.getJSONArray("answer");
                JSONArray array_option=object.getJSONArray("option");
                for(int a=0;a<array.length();a++){
                    answers.add(array.get(a).toString());
                }
                for(int a=0;a<array_option.length();a++){
                    option.add(array_option.get(a).toString());
                }
                singleChoice.setAnswer(answers);
                singleChoice.setChoices(option);
                essay.add(singleChoice);
            }
            L.d("tag-ids-",essay.size()+"#5");
            return  essay;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    //获取各个题型的顺序
    private void gethttpRequstmodel() {
        search_jiazai_layout.setVisibility(View.VISIBLE);
        load_fail_layout.setVisibility(View.GONE);
        new HttpGetBuilder(PublishWorkActivity.this).setHttpResult(this)
                .setUrl(UrlsNew.TEST_PAPER_PUBLISH)
                .setClassObj(WorkOrderBean.class)
                .setPath(testId)
                .setRequestCode(1001)
                .build();
    }
    //获取试题内容
    private void loadWorkData() {
        new HttpGetBuilder(PublishWorkActivity.this).setHttpResult(this)
                .setUrl(UrlsNew.TEST_PAPER_QUESTION)
                .setClassObj(null)
                .addQueryParams("test_paper_id",testId)
                .setRequestCode(1002)
                .build();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if(requestCode==1001){
            WorkOrderBean workOrderBean= (WorkOrderBean) resultBean;
            score_full=workOrderBean.getItem().getScore()+"";
            if(!score_full.contains(".")){
                score_full=score_full+".0";
            }
            question_type_sort.add(1);
            question_type_sort.add(2);
            question_type_sort.add(3);
            question_type_sort.add(4);
            question_type_sort.add(5);
            loadWorkData();
        }
        if(requestCode==1002){
            search_jiazai_layout.setVisibility(View.GONE);
            JSONObject singleChoice;
            JSONObject choice;
            JSONObject fill;
            JSONObject essay_object;
            JSONObject determine_object;
            JSONArray total = null;
            JSONObject jsonO = null;
            try {
                jsonO = new JSONObject(resultBean.toString());
                single_choice_list.clear();
                fill_list.clear();
                essay.clear();
                determine.clear();
                choice_list.clear();
                lists.clear();
                keyGroup.clear();
                list.clear();
                total = jsonO.getJSONArray("items");
                count = total.length();
                number_total = total.length();
                for(int i=0;i<total.length();i++){
                    JSONObject object=total.getJSONObject(i);
                    if(object.getString("type").equals("1")){
                        singleChoice=object;
                        getSingle_choice_list(singleChoice);
                    }
                    if(object.getString("type").equals("3")){
                        fill=object;
                        get_fill_list(fill);
                    }
                    if(object.getString("type").equals("5")){
                        essay_object=object;
                        get_essay(essay_object);
                    }
                    if(object.getString("type").equals("4")){
                        determine_object=object;
                        get_determine(determine_object);
                    }
                    if(object.getString("type").equals("2")){
                        choice=object;
                        get_choice_list(choice);
                    }
                }
                if(single_choice_list.size()>0){
                    float totalScore = 0;
                    for(int i=0;i<single_choice_list.size();i++){
                        single_choice_list.get(i).setNumber(single_choice_list.size()+"");
                        totalScore+=Float.valueOf(single_choice_list.get(i).getScore());
                    }
                    for(int i=0;i<single_choice_list.size();i++){
                        single_choice_list.get(i).setTotalScore(totalScore);
                    }
                }else {
                    question_type_sort.remove(question_type_sort.indexOf(1));
                }
                if(choice_list.size()>0){
                    float totalScore = 0;
                    for(int i=0;i<choice_list.size();i++){
                        choice_list.get(i).setNumber(choice_list.size()+"");
                        totalScore+=Float.valueOf(choice_list.get(i).getScore());
                    }
                    for(int i=0;i<choice_list.size();i++){
                        choice_list.get(i).setTotalScore(totalScore);
                    }
                }else {
                    question_type_sort.remove(question_type_sort.indexOf(2));
                }
                if(fill_list.size()>0){
                    float totalScore = 0;
                    for(int i=0;i<fill_list.size();i++){
                        fill_list.get(i).setNumber(fill_list.size()+"");
                        totalScore+=Double.valueOf(fill_list.get(i).getScore());
                    }
                    for(int i=0;i<fill_list.size();i++){
                        fill_list.get(i).setTotalScore(totalScore);
                    }
                }else {
                    question_type_sort.remove(question_type_sort.indexOf(3));
                }
                if(determine.size()>0){
                    float totalScore = 0;
                    for(int i=0;i<determine.size();i++){
                        determine.get(i).setNumber(determine.size()+"");
                        totalScore+=Double.valueOf(determine.get(i).getScore());
                    }
                    for(int i=0;i<determine.size();i++){
                        determine.get(i).setTotalScore(totalScore);
                    }
                }else {
                    question_type_sort.remove(question_type_sort.indexOf(4));
                }
                if(essay.size()>0){
                    float totalScore = 0;
                    for(int i=0;i<essay.size();i++){
                        essay.get(i).setNumber(essay.size()+"");
                        totalScore+=Double.valueOf(essay.get(i).getScore());
                    }
                    for(int i=0;i<essay.size();i++){
                        essay.get(i).setTotalScore(totalScore);
                    }
                }else {
                    question_type_sort.remove(question_type_sort.indexOf(5));
                }
                totalList.add(single_choice_list);
                totalList.add(choice_list);
                totalList.add(fill_list);
                totalList.add(determine);
                totalList.add(essay);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(total.length()>0){
                course_listview.setVisibility(View.VISIBLE);
                no_info_layout.setVisibility(View.GONE);
            }else {
                no_info_layout.setVisibility(View.VISIBLE);
            }

            for(int i=0;i<question_type_sort.size();i++){
                lists.add(totalList.get(question_type_sort.get(i)-1));
            }

            poMap=new HashMap<>();
            //给每种类型标题添加类型数量以及类型的总分数
            for(int i=0;i<question_type_sort.size();i++){
                SingleChoice choices=new SingleChoice();
                choices.setType(type[i]);
                choices.setQuestionType(lists.get(i).get(0).getQuestionType());
                choices.setTotalScore(lists.get(i).get(0).getTotalScore());
                choices.setNumber(lists.get(i).get(0).getNumber());
                keyGroup.add(type[i]);
                //先添加试题标题类型的元素用于标记分割；再添加该类型下的试题集合；
                list.add(choices);
                list.addAll(lists.get(i));
                poMap.put(lists.get(i).get(0).getQuestionType(),list.size());
            }
            if(question_type_sort.size()==1){
                poB=0;
            }
            if(question_type_sort.size()==2){
                poB=lists.get(0).size()+1;
            }
            if(question_type_sort.size()==3){
                poB=lists.get(0).size()+1;
                poC=lists.get(1).size()+1+poB;
            }
            if(question_type_sort.size()==4){
                poB=lists.get(0).size()+1;
                poC=lists.get(1).size()+1+poB;
                poD=lists.get(2).size()+1+poC;
            }
            if(question_type_sort.size()==5){
                poB=lists.get(0).size()+1;
                poC=lists.get(1).size()+1+poB;
                poD=lists.get(2).size()+1+poC;
                poE=lists.get(3).size()+1+poD;
            }
            dragListAdapter.notifyDataSetChanged();
            if(poMap.containsKey(poType)){
                course_listview.setSelection(poMap.get(poType)-1);
            }
            if(issendBroad){
                Intent intent=new Intent();
                intent.setAction("com.coder.kzxt.activity.TeaWorkRecourseActivity");
                sendBroadcast(intent);
            }
            if(detail==null&&status==2){
                bottom_layout.setVisibility(View.VISIBLE);
            }
        }

//        }else if(code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
//            //重新登录
////            NetworkUtil.httpRestartLogin(PublishWorkActivity.this,activity_publish_work);
////            bottom_layout.setVisibility(View.GONE);
//        }else {
//            //加载失败
////            load_fail_layout.setVisibility(View.VISIBLE);
////            bottom_layout.setVisibility(View.GONE);
////            NetworkUtil.httpNetErrTip(PublishWorkActivity.this,activity_publish_work);
//        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        search_jiazai_layout.setVisibility(View.GONE);
        if(code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
            //重新登录
            NetworkUtil.httpRestartLogin(PublishWorkActivity.this,activity_publish_work);
        }else {
            //加载失败
            load_fail_layout.setVisibility(View.VISIBLE);
            NetworkUtil.httpNetErrTip(PublishWorkActivity.this,activity_publish_work);
        }

    }


    private JSONArray getArray(){
        List<String>id_list=new ArrayList<>();
        List<String>score_list=new ArrayList<>();
        for(int i=0;i<dragListAdapter.getCount();i++){
            SingleChoice dragItem = dragListAdapter.getItem(i);
            if(!keyGroup.contains(dragItem.getType())){
                id_list.add(dragItem.getId());
                score_list.add(dragItem.getScore());
            }
        }
        JSONArray jsonArray=new JSONArray();
        for (int i=0;i<id_list.size();i++){
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("id",id_list.get(i));
                jsonObject.put("score",score_list.get(i));
                jsonObject.put("sort",i);
                jsonArray.put(i,jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("zw--",jsonArray.toString());
        return jsonArray;
    }
    private void updateQuestions(){
        new HttpPutBuilder(PublishWorkActivity.this).setClassObj(null).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                search_jiazai_layout.setVisibility(View.GONE);
                rightText.setClickable(true);
                Intent intent=new Intent(PublishWorkActivity.this,PublishWorkTimeActivity.class);
//                String ids=getService_id().toString();
//                intent.putExtra("id",ids);
                intent.putExtra("testId",testId);
                intent.putExtra("title",work_name);
                intent.putExtra("workType",workType);
                intent.putExtra("courseId",courseId);
                startActivity(intent);
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                search_jiazai_layout.setVisibility(View.GONE);
                rightText.setClickable(true);

            }
        }).setUrl(UrlsNew.URLHeader+"/test_paper/question/batch").addBodyParam(getArray().toString()).build();
    }


    private static int po=0;
    public  class DragListAdapter extends ArrayAdapter<SingleChoice> {
        public DragListAdapter(Context context, List<SingleChoice> objects) {
            super(context, 0, objects);
        }
        public List<SingleChoice> getList(){
            return list;
        }


        //检查数据项是否为标题项然后标记是否可以更改
        @Override
        public boolean isEnabled(int position) {
            if(keyGroup.contains(getItem(position).getType())){
                return false;
            }
            return super.isEnabled(position);
        }



        //利用模板布局不同的listview
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(keyGroup.contains(getItem(position).getType())){
                po=position;
                //标题组
                view = LayoutInflater.from(getContext()).inflate(R.layout.question_head, null);
                LinearLayout po= (LinearLayout) view.findViewById(R.id.po);
                View po1=view.findViewById(R.id.po1);
                if(position==0){
                    po.setVisibility(View.VISIBLE);
                    po1.setVisibility(View.GONE);
                }else {
                    po.setVisibility(View.GONE);
                    po1.setVisibility(View.VISIBLE);
                }
                TextView total_number;
                TextView fullScore;
                total_number= (TextView) view.findViewById(R.id.number_t);
                fullScore= (TextView) view.findViewById(R.id.full_score);
                total_number.setText(number_total+"");
                fullScore.setText(score_full);
                TextView textView = (TextView) view.findViewById(R.id.headtext);
                TextView textScore = (TextView) view.findViewById(R.id.score);
                TextView textNum = (TextView) view.findViewById(R.id.number);
                DecimalFormat decimalFormat=new DecimalFormat("0.0");

                textScore.setText(decimalFormat.format(getItem(position).getTotalScore())+"分");
                textNum.setText(getItem(position).getNumber()+" 题 ");
                String name="";
                if(getItem(position).getQuestionType().equals("single_choice")){
                    name="单选题";
                    //  textView.setText("单选题");
                }
                if(getItem(position).getQuestionType().equals("determine")){
                    name="判断题";
                    //  textView.setText("判断题");
                }
                if(getItem(position).getQuestionType().equals("essay")){
                    name="问答题";

                    //  textView.setText("问答题");
                }
                if(getItem(position).getQuestionType().equals("fill")){
                    name="填空题";
                    // textView.setText("填空题");
                }
                if(getItem(position).getQuestionType().equals("choice")){
                    name="多选题";
                    // textView.setText("多选题");
                }
                int index=keyGroup.indexOf(getItem(position).getType());
                String[]num=new String[]{"一 ","二 ","三 ","四 ","五 "};
                textView.setText(num[index]+name);
            }else{
                //图片组
                view = LayoutInflater.from(getContext()).inflate(R.layout.question_item, null);
                //控制交换顺序时候的阴影
                View yinying=view.findViewById(R.id.yinying);
                View yinying1=view.findViewById(R.id.yinying1);
//                yinying.setVisibility(View.GONE);
//                yinying1.setVisibility(View.GONE);
                yinying.setBackgroundColor(ContextCompat.getColor(PublishWorkActivity.this,R.color.font_white));
                yinying1.setBackgroundColor(ContextCompat.getColor(PublishWorkActivity.this,R.color.font_white));
                if(position==getCount()-1){
                    yinying1.setBackgroundColor(ContextCompat.getColor(PublishWorkActivity.this,R.color.bg_gray));
                }
                TextView textView = (TextView) view.findViewById(R.id.headtext);
                TextView one_score = (TextView) view.findViewById(R.id.one_score);
                TextView diffculty_type = (TextView) view.findViewById(R.id.diffculty_type);
                ImageView imageView1= (ImageView) view.findViewById(R.id.imageView1);
                if((detail==null&&status!=2)||detail!=null){
                    imageView1.setVisibility(View.INVISIBLE);
                }
                String score_one=getItem(position).getScore();
                if(score_one.contains(".")){
                    one_score.setText(score_one);
                }else {
                    one_score.setText(score_one+".0");
                }
                String type_diffcult=getItem(position).getDiffculty();
                if(type_diffcult.equals("1")){
                    diffculty_type.setText("简单");
                    diffculty_type.setTextColor(ContextCompat.getColor(PublishWorkActivity.this,R.color.font_green));
                    diffculty_type.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.simple_type_green));
                }
                if(type_diffcult.equals("2")){
                    diffculty_type.setText("一般");
                    diffculty_type.setTextColor(ContextCompat.getColor(PublishWorkActivity.this,R.color.font_blue));
                    diffculty_type.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.normal_type_blue));
                }
                if(type_diffcult.equals("3")){
                    diffculty_type.setText("困难");
                    diffculty_type.setTextColor(ContextCompat.getColor(PublishWorkActivity.this,R.color.font_red));
                    diffculty_type.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.diffcult_type_red));
                }
                L.d("Pob--",poB+"&&");
                if(poB==0){
                    L.d("Pob--",poB+"&"+(position)+"."+getItem(position).getStem_content());
                    textView.setText((position)+"."+getItem(position).getStem_content());
                }
                L.d("TAG--KEYGROUP",keyGroup.size()+"--"+"@"+poB+"@"+poC+"@"+poD+"@"+poE);
                if(keyGroup.size()==2){
                    if(position>poB){
                        textView.setText((position-poB)+"."+getItem(position).getStem_content());
                    }else if(position<poB){
                        textView.setText((position)+"."+getItem(position).getStem_content());
                    }
                }
                if(keyGroup.size()==3){
                    if(position>poB&&position<poC){
                        textView.setText((position-poB)+"."+getItem(position).getStem_content());
                    }else if(position<poB){
                        textView.setText((position)+"."+getItem(position).getStem_content());
                    }else if(position>poC){
                        textView.setText((position-poC)+"."+getItem(position).getStem_content());
                    }
                }
                if(keyGroup.size()==4){
                    if(position>poB&&position<poC){
                        textView.setText((position-poB)+"."+getItem(position).getStem_content());
                    }else if(position<poB){
                        textView.setText((position)+"."+getItem(position).getStem_content());
                    }else if(position<poD&&position>poC){
                        textView.setText((position-poC)+"."+getItem(position).getStem_content());
                    }else if(position>poD){
                        textView.setText((position-poD)+"."+getItem(position).getStem_content());
                    }
                }
                if(keyGroup.size()==5){
                    if(position>poB&&position<poC){
                        textView.setText((position-poB)+"."+getItem(position).getStem_content());
                    }else if(position<poB){
                        textView.setText((position)+"."+getItem(position).getStem_content());
                    }else if(position<poD&&position>poC){
                        textView.setText((position-poC)+"."+getItem(position).getStem_content());
                    }else if(position>poD&&position<poE){
                        textView.setText((position-poD)+"."+getItem(position).getStem_content());
                    }else if(position>poE){
                        textView.setText((position-poE)+"."+getItem(position).getStem_content());
                    }
                }
            }

            return view;
        }
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.coder.kzxt.activity.time")){
                finish();
            }
            if(intent.getAction().equals("com.coder.kzxt.activity.PublishWorkActivity")){
                totalList.clear();
                question_type_sort.clear();
                single_choice_list.clear();
                choice_list.clear();
                determine.clear();
                fill_list.clear();
                essay.clear();
                list.clear();
                poType=intent.getStringExtra("poType");
                issendBroad=true;
                no_info_layout.setVisibility(View.GONE);
                load_fail_layout.setVisibility(View.GONE);
                gethttpRequstmodel();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    unregisterReceiver(myReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Constants.LOGIN_BACK){
            totalList.clear();
            question_type_sort.clear();
            single_choice_list.clear();
            choice_list.clear();
            determine.clear();
            fill_list.clear();
            essay.clear();
            list.clear();
            no_info_layout.setVisibility(View.GONE);
            load_fail_layout.setVisibility(View.GONE);
            gethttpRequstmodel();
        }
    }
}
