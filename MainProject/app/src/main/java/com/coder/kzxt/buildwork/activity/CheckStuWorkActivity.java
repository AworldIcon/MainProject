package com.coder.kzxt.buildwork.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.buildwork.entity.CheckWorkBean;
import com.coder.kzxt.buildwork.entity.SingleChoice;
import com.coder.kzxt.buildwork.entity.StuWorkChild;
import com.coder.kzxt.buildwork.entity.StuWorkGroup;
import com.coder.kzxt.buildwork.entity.WorkOrderBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CheckStuWorkActivity extends BaseActivity implements HttpCallBack {
    private RelativeLayout activity_tea_check_work;
    private ImageView back_title_button;
    private TextView title;
    private TextView rightText;
    private Button load_fail_button;
    private RelativeLayout network_set_layout;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private ExpandableListView expandableListView;
    private String iscenter;
    private String resultId;//get请求时候用
    private String nickname;
    private ArrayList<StuWorkChild> childList;
    private ArrayList<StuWorkGroup>groupList;
    private ArrayList<StuWorkGroup>groupListNew=new ArrayList<>();
    private ArrayList<List<StuWorkChild>>child;
    private ArrayList<List<SingleChoice>>childNew=new ArrayList<>();
    private List<String>tea_word;//存储老师评语
    private List<String>essay_score_list;//存储学生此题获得的分数
    private List<String>essay_fullscore_list;//存储此问答题原本分数
    private List<String>id_essay;//存储每个问答试题的id
    private int index=-1;
    private ExpandAdapter expandAdapter;
    private int essay_length=0;
    //private String testid;//跳转页面时候用
    private String work_name;
    private String status;//作业状态
    private int stu;//1学生查看完成的试卷跳转传值
    private Boolean isCourse; //区分课程 服务
    private Button btn;
    private boolean hasEssay=false;
    private int workType;
    private List<SingleChoice> single_choice_list;
    private List<SingleChoice>choice_list;
    private List<SingleChoice>determine;
    private List<SingleChoice>fill_list;
    private List<SingleChoice>essay;
    private List<Integer>question_type_sort=new ArrayList<>();//代表此时卷有几种题型以及题型展示顺序

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_stu_work);
        // pu=new PublicUtils(this);
        iscenter=getIntent().getStringExtra(Constants.IS_CENTER);
        resultId=getIntent().getStringExtra("resultId");
        nickname=getIntent().getStringExtra("nickname");
       // testid=getIntent().getStringExtra("testid");
        work_name=getIntent().getStringExtra("name");
        status=getIntent().getStringExtra("status");
        workType=getIntent().getIntExtra("workType",2);
        stu=getIntent().getIntExtra("stu",0);
        //是否是课程 区分服务
        isCourse=getIntent().getBooleanExtra("isCourse",true);

        single_choice_list=new ArrayList<>();
        choice_list=new ArrayList<>();
        determine=new ArrayList<>();
        fill_list=new ArrayList<>();
        essay=new ArrayList<>();

//        imageLoader= ImageLoader.getInstance();
//        topicSquare = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.photo_default)
//                .showImageForEmptyUri(R.drawable.photo_default)
//                .showImageOnFail(R.drawable.photo_default)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .cacheInMemory(false) // 加载图片时会在内存中加载缓存
//                .cacheOnDisk(true)// 加载图片时会在磁盘中加载缓存
//                .considerExifParams(true)// 启用EXIF和JPEG图像格式
//                .bitmapConfig(Bitmap.Config.RGB_565).build();
        activity_tea_check_work= (RelativeLayout) findViewById(R.id.activity_check_stu_work);
        back_title_button = (ImageView) findViewById(R.id.leftImage);
        title = (TextView) findViewById(R.id.title);
        rightText = (TextView) findViewById(R.id.rightText);
        View footView=LayoutInflater.from(this).inflate(R.layout.piyue_foot,null);
        btn= (Button) footView.findViewById(R.id.result_btn);
        search_jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        network_set_layout = (RelativeLayout) findViewById(R.id.network_set_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        expandableListView= (ExpandableListView) findViewById(R.id.expandlist);
        if(status.equals("3")){
            rightText.setVisibility(View.GONE);
        }else {
            expandableListView.addFooterView(footView);
        }
        if(workType==1){
            //学生名字
            title.setText(nickname+"-考试");
        }else {
            title.setText(nickname+"-作业");
        }        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                100, getResources().getDisplayMetrics());
        title.setWidth(px);
        title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        title.setMarqueeRepeatLimit(100000);
        title.setFocusable(true);
        title.setSingleLine(true);
        title.setHorizontallyScrolling(true);
        title.setFocusableInTouchMode(true);
        groupList=new ArrayList<>();
        child=new ArrayList<>();
        ///用来保存老师的评语以及子item中的分数，二者的长度要和子item一致
        tea_word=new ArrayList<>();
        essay_score_list=new ArrayList<>();
        essay_fullscore_list=new ArrayList<>();
        id_essay=new ArrayList<>();
        expandAdapter=new ExpandAdapter();
        expandableListView.setAdapter(expandAdapter);
        initOnevents();
        gethttpRequstOne();
        Log.d("zw--size",groupListNew.size()+"&&"+childNew.size());
        search_jiazai_layout.setVisibility(View.VISIBLE);

    }
    //获取各个题型中相应试题结果的集合
    private CheckWorkBean checkWorkBean;
    private void gethttpRequstOne() {
        //获取每题学生答题情况1001
        new HttpGetBuilder(CheckStuWorkActivity.this).setHttpResult(this)
                .setUrl(isCourse ? UrlsNew.TEST_RESULT : UrlsNew.TEST_RESULT_SERVICE)
                .setClassObj(CheckWorkBean.class)
                .addQueryParams("page","1")
                .addQueryParams("pageSize","200")
                .setPath(resultId)
                .setRequestCode(1001)
                .build();

    }
    //获取各个题型的顺序
    private void gethttpRequstmodel() {
        new HttpGetBuilder(CheckStuWorkActivity.this).setHttpResult(this)
                .setUrl(isCourse ? UrlsNew.TEST_PAPER_PUBLISH:UrlsNew.TEST_PAPER_PUBLISH_SERVICE)
                .setClassObj(WorkOrderBean.class)
                .setPath(checkWorkBean.getItem().getTest().getTest_paper_id()+"")
                .setRequestCode(1002)
                .build();
    }
    //获取各个题型的集合
    private void gethttpRequstTwo() {
        new HttpGetBuilder(CheckStuWorkActivity.this).setHttpResult(this)
                .setUrl(isCourse ? UrlsNew.TEST_PAPER_QUESTION:UrlsNew.TEST_PAPER_QUESTION_SERVICE)
                .setClassObj(null)
                .addQueryParams("test_paper_id",checkWorkBean.getItem().getTest().getTest_paper_id()+"")
                .setRequestCode(1003)
                .build();

    }
    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if(requestCode==1001){
            checkWorkBean= (CheckWorkBean) resultBean;
            Log.d("zw--",checkWorkBean.getItem().getDetail().size()+"---1001");
            //获取大题型排列顺序1002
            gethttpRequstmodel();
        }
        if(requestCode==1002){
            WorkOrderBean workOrderBean= (WorkOrderBean) resultBean;
//            for(int i=0;i<workOrderBean.getItem().getQuestion_type_sort().size();i++){
//                question_type_sort.add(workOrderBean.getItem().getQuestion_type_sort().get(i));
//            }
            question_type_sort.add(1);
            question_type_sort.add(2);
            question_type_sort.add(3);
            question_type_sort.add(4);
            question_type_sort.add(5);
            //获取各个小题干1003
            gethttpRequstTwo();
        }
        if(requestCode==1003){
            search_jiazai_layout.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
            JSONArray total = null;
            JSONObject singleChoice;
            JSONObject choice;
            JSONObject fill;
            JSONObject essay_object;
            JSONObject determine_object;
            try {
                JSONObject jsonObject=new JSONObject(resultBean.toString());
                single_choice_list.clear();
                fill_list.clear();
                essay.clear();
                determine.clear();
                choice_list.clear();
                total = jsonObject.getJSONArray("items");

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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(single_choice_list.size()==0){
                question_type_sort.remove(question_type_sort.indexOf(1));
            }
            if(choice_list.size()==0){
                question_type_sort.remove(question_type_sort.indexOf(2));
            }
            if(determine.size()==0){
                question_type_sort.remove(question_type_sort.indexOf(4));
            }
            if(fill_list.size()==0){
                question_type_sort.remove(question_type_sort.indexOf(3));
            }
            if(essay.size()==0){
                question_type_sort.remove(question_type_sort.indexOf(5));
            }


            for(int i=0;i<checkWorkBean.getItem().getDetail().size();i++){
                for(int j=0;j<single_choice_list.size();j++){

                    if(checkWorkBean.getItem().getDetail().get(i).getQuestion_id().trim().equals(single_choice_list.get(j).getId().trim())){
                        single_choice_list.get(j).setTest_result_id(checkWorkBean.getItem().getDetail().get(i).getTest_result_id());
                        single_choice_list.get(j).setStatus(checkWorkBean.getItem().getDetail().get(i).getStatus());
                        single_choice_list.get(j).setResult_score(checkWorkBean.getItem().getDetail().get(i).getScore());
                        single_choice_list.get(j).setAnswer_stu(checkWorkBean.getItem().getDetail().get(i).getAnswer());

                    }
                }
                for(int j=0;j<choice_list.size();j++){
                    if(checkWorkBean.getItem().getDetail().get(i).getQuestion_id().equals(choice_list.get(j).getId())){
                        choice_list.get(j).setTest_result_id(checkWorkBean.getItem().getDetail().get(i).getTest_result_id());
                        choice_list.get(j).setStatus(checkWorkBean.getItem().getDetail().get(i).getStatus());
                        choice_list.get(j).setResult_score(checkWorkBean.getItem().getDetail().get(i).getScore());
                        choice_list.get(j).setAnswer_stu(checkWorkBean.getItem().getDetail().get(i).getAnswer());
                    }
                }
                for(int j=0;j<fill_list.size();j++){
                    if(checkWorkBean.getItem().getDetail().get(i).getQuestion_id().equals(fill_list.get(j).getId())){
                        fill_list.get(j).setTest_result_id(checkWorkBean.getItem().getDetail().get(i).getTest_result_id());
                        fill_list.get(j).setStatus(checkWorkBean.getItem().getDetail().get(i).getStatus());
                        fill_list.get(j).setResult_score(checkWorkBean.getItem().getDetail().get(i).getScore());
                        fill_list.get(j).setAnswer_stu(checkWorkBean.getItem().getDetail().get(i).getAnswer());
                    }
                }
                for(int j=0;j<determine.size();j++){
                    if(checkWorkBean.getItem().getDetail().get(i).getQuestion_id().equals(determine.get(j).getId())){
                        determine.get(j).setTest_result_id(checkWorkBean.getItem().getDetail().get(i).getTest_result_id());
                        determine.get(j).setStatus(checkWorkBean.getItem().getDetail().get(i).getStatus());
                        determine.get(j).setResult_score(checkWorkBean.getItem().getDetail().get(i).getScore());
                        determine.get(j).setAnswer_stu(checkWorkBean.getItem().getDetail().get(i).getAnswer());
                    }
                }
                for(int j=0;j<essay.size();j++){
                    if(checkWorkBean.getItem().getDetail().get(i).getQuestion_id().equals(essay.get(j).getId())){
                        essay.get(j).setTest_result_id(checkWorkBean.getItem().getDetail().get(i).getTest_result_id());
                        essay.get(j).setStatus(checkWorkBean.getItem().getDetail().get(i).getStatus());
                        essay.get(j).setResult_score(checkWorkBean.getItem().getDetail().get(i).getScore());
                        essay.get(j).setAnswer_stu(checkWorkBean.getItem().getDetail().get(i).getAnswer());
                        essay.get(j).setQuestion_id(Integer.valueOf(checkWorkBean.getItem().getDetail().get(i).getId().trim()));
                        essay.get(j).setTeacher_say(checkWorkBean.getItem().getDetail().get(i).getTeacher_say()==null?"":checkWorkBean.getItem().getDetail().get(i).getTeacher_say());
                    }
                }
            }

            //给每个小题设置所需属性
            if(single_choice_list.size()>0){
                childNew.add(single_choice_list);
            }
            if(choice_list.size()>0){
                childNew.add(choice_list);
            }

            if(fill_list.size()>0){
                childNew.add(fill_list);
            }
            if(determine.size()>0){
                childNew.add(determine);
            }
            if(essay.size()>0){
                childNew.add(essay);
            }
            Log.d("zw--ss",essay.size()+"essay"+question_type_sort.size());
            //然后通过小题设置group的属性
            for(int i=0;i<question_type_sort.size();i++){
                if(question_type_sort.get(i)==1){
                    StuWorkGroup stuWorkGroup=new StuWorkGroup();
                    float total_score = 0;
                    float num_score=0;
                    for(int s=0;s<single_choice_list.size();s++){
                        total_score+=Float.valueOf(single_choice_list.get(s).getScore());
                        num_score+=Float.valueOf(single_choice_list.get(s).getResult_score());
                    }
                    String lable="";
                    switch (i){
                        case 0:
                            lable="一";
                            break;
                        case 1:
                            lable="二";
                            break;
                        case 2:
                            lable="三";
                            break;
                        case 3:
                            lable="四";
                            break;
                        case 4:
                            lable="五";
                            break;
                    }

                    stuWorkGroup.setType("single_choice");
                    stuWorkGroup.setCount(String.valueOf(single_choice_list.size()));
                    stuWorkGroup.setNum(String.valueOf(num_score));
                    stuWorkGroup.setTotalScore(String.valueOf(total_score));
                    stuWorkGroup.setName(lable+".单选题");
                    stuWorkGroup.setLable(lable);
                    if(single_choice_list.size()>0){
                        groupListNew.add(stuWorkGroup);
                    }
                }
                if(question_type_sort.get(i)==2){
                    StuWorkGroup stuWorkGroup=new StuWorkGroup();
                    float total_score = 0;
                    float num_score=0;
                    for(int s=0;s<choice_list.size();s++){
                        total_score+=Float.valueOf(choice_list.get(s).getScore());
                        num_score+=Float.valueOf(choice_list.get(s).getResult_score());
                    }
                    String lable="";
                    switch (i){
                        case 0:
                            lable="一";
                            break;
                        case 1:
                            lable="二";
                            break;
                        case 2:
                            lable="三";
                            break;
                        case 3:
                            lable="四";
                            break;
                        case 4:
                            lable="五";
                            break;
                    }

                    stuWorkGroup.setType("choice");
                    stuWorkGroup.setCount(String.valueOf(choice_list.size()));
                    stuWorkGroup.setNum(String.valueOf(num_score));
                    stuWorkGroup.setTotalScore(String.valueOf(total_score));
                    stuWorkGroup.setName(lable+".多选题");
                    stuWorkGroup.setLable(lable);
                    if(choice_list.size()>0){
                        groupListNew.add(stuWorkGroup);
                    }
                }
                if(question_type_sort.get(i)==4){
                    StuWorkGroup stuWorkGroup=new StuWorkGroup();
                    float total_score = 0;
                    float num_score=0;
                    for(int s=0;s<determine.size();s++){
                        total_score+=Float.valueOf(determine.get(s).getScore());
                        num_score+=Float.valueOf(determine.get(s).getResult_score());
                    }
                    String lable="";
                    switch (i){
                        case 0:
                            lable="一";
                            break;
                        case 1:
                            lable="二";
                            break;
                        case 2:
                            lable="三";
                            break;
                        case 3:
                            lable="四";
                            break;
                        case 4:
                            lable="五";
                            break;
                    }

                    stuWorkGroup.setType("determine");
                    stuWorkGroup.setCount(String.valueOf(determine.size()));
                    stuWorkGroup.setNum(String.valueOf(num_score));
                    stuWorkGroup.setTotalScore(String.valueOf(total_score));
                    stuWorkGroup.setName(lable+".判断题");
                    stuWorkGroup.setLable(lable);
                    if(determine.size()>0){
                        groupListNew.add(stuWorkGroup);
                    }
                }
                if(question_type_sort.get(i)==3){
                    Log.d("zw--size",fill_list.size()+"aaaaaaaa");
                    StuWorkGroup stuWorkGroup=new StuWorkGroup();
                    float total_score = 0;
                    float num_score=0;
                    for(int s=0;s<fill_list.size();s++){
                        total_score+=Float.valueOf(fill_list.get(s).getScore());
                        num_score+=Float.valueOf(fill_list.get(s).getResult_score());
                    }
                    String lable="";
                    switch (i){
                        case 0:
                            lable="一";
                            break;
                        case 1:
                            lable="二";
                            break;
                        case 2:
                            lable="三";
                            break;
                        case 3:
                            lable="四";
                            break;
                        case 4:
                            lable="五";
                            break;
                    }

                    stuWorkGroup.setType("fill");
                    stuWorkGroup.setCount(String.valueOf(fill_list.size()));
                    stuWorkGroup.setNum(String.valueOf(num_score));
                    stuWorkGroup.setTotalScore(String.valueOf(total_score));
                    stuWorkGroup.setName(lable+".填空题");
                    stuWorkGroup.setLable(lable);
                    if(fill_list.size()>0){
                        groupListNew.add(stuWorkGroup);
                    }
                }
                if(question_type_sort.get(i)==5){
                    StuWorkGroup stuWorkGroup=new StuWorkGroup();
                    float total_score = 0;
                    float num_score=0;
                    for(int s=0;s<essay.size();s++){
                        total_score+=Float.valueOf(essay.get(s).getScore());
                        num_score+=Float.valueOf(essay.get(s).getResult_score());
                        essay_score_list.add(String.valueOf(essay.get(s).getResult_score()));
                        essay_fullscore_list.add(String.valueOf(essay.get(s).getScore()));
                        tea_word.add(essay.get(s).getTeacher_say());
                        id_essay.add(String.valueOf(essay.get(s).getQuestion_id()));
                    }
                    String lable="";
                    switch (i){
                        case 0:
                            lable="一";
                            break;
                        case 1:
                            lable="二";
                            break;
                        case 2:
                            lable="三";
                            break;
                        case 3:
                            lable="四";
                            break;
                        case 4:
                            lable="五";
                            break;
                    }

                    stuWorkGroup.setType("essay");
                    stuWorkGroup.setCount(String.valueOf(essay.size()));
                    stuWorkGroup.setNum(String.valueOf(num_score));
                    stuWorkGroup.setTotalScore(String.valueOf(total_score));
                    stuWorkGroup.setName(lable+".问答题");
                    stuWorkGroup.setLable(lable);
                    if(essay.size()>0){
                        groupListNew.add(stuWorkGroup);
                    }
                }
            }
            Log.d("zw----",childNew.size()+"essay"+groupListNew.size());
            expandAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
            //重新登录
            NetworkUtil.httpRestartLogin(CheckStuWorkActivity.this, activity_tea_check_work);
        } else {
            search_jiazai_layout.setVisibility(View.GONE);
            expandableListView.setVisibility(View.GONE);
            load_fail_layout.setVisibility(View.VISIBLE);
            NetworkUtil.httpNetErrTip(CheckStuWorkActivity.this, activity_tea_check_work);
        }

    }

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



    private void initOnevents() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                essay_length=essay.size();
                JSONArray jsonArray=new JSONArray();
                for(int i=0;i<essay_length;i++){
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id",id_essay.get(i));
                        if(i<tea_word.size()){
                            object.put("teacher_say",tea_word.get(i));
                        }else {
                            object.put("teacher_say","");
                        }
                        object.put("score",essay_score_list.get(i));
                        object.put("status","1");//必须传个字段，任意值，后台不判断
                        jsonArray.put(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                sumbit_result(jsonArray.toString());
                Log.d("json--",jsonArray.toString());
            }
        });
        back_title_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(status.equals("3")){
//                    Intent intent=new Intent(CheckStuWorkActivity.this,MyWorkActivity.class);
//                    intent.putExtra("testId",testid);
//                    intent.putExtra("title",work_name);
//                    intent.putExtra("workType",workType);
//                    intent.putExtra(Constants.IS_CENTER,iscenter);
//                    startActivity(intent);
                    finish();
                }else {
                    dialog();
                }

            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_fail_layout.setVisibility(View.GONE);
                search_jiazai_layout.setVisibility(View.GONE);
                gethttpRequstOne();
            }
        });
    }

    private void sumbit_result(String result) {
        search_jiazai_layout.setVisibility(View.VISIBLE);
        new HttpPutBuilder(this).setClassObj(null).setUrl(isCourse?UrlsNew.TEST_CHECK:UrlsNew.TEST_CHECK_SERVICE)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        search_jiazai_layout.setVisibility(View.GONE);
                        setResult(200);
                        finish();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        search_jiazai_layout.setVisibility(View.GONE);
                        ToastUtils.makeText(CheckStuWorkActivity.this,msg);
                    }
                }).setPath(resultId).addBodyParam(result).build();
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckStuWorkActivity.this,R.style.custom_dialog);
        builder.setMessage("您还未完成批阅，是否退出？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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



    public class ExpandAdapter extends BaseExpandableListAdapter {
        @Override
        public int getGroupCount() {
            return groupListNew.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childNew.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupListNew.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childNew.get(groupPosition).get(childPosition);
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
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.stu_work_group,null);
            TextView textView= (TextView) view.findViewById(R.id.group_name);
            TextView num_total= (TextView) view.findViewById(R.id.num_total);
            TextView score_total= (TextView) view.findViewById(R.id.score_total);
            ImageView jiantou_image= (ImageView) view.findViewById(R.id.jiantou_image);
            if(isExpanded){
                jiantou_image.setImageResource(R.drawable.department_up);
            }else {
                jiantou_image.setImageResource(R.drawable.department_down);
            }
            textView.setText(groupListNew.get(groupPosition).getName());
            num_total.setText(groupListNew.get(groupPosition).getCount());
            DecimalFormat decimalFormat=new DecimalFormat("0.0");
            score_total.setText(groupListNew.get(groupPosition).getNum()+"/"+decimalFormat.format(Float.valueOf(groupListNew.get(groupPosition).getTotalScore().trim())));
            return view;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View  view= LayoutInflater.from(parent.getContext()).inflate(R.layout.stu_work_child,null);
            LinearLayout essay_layout= (LinearLayout) view.findViewById(R.id.essay_layout);
            LinearLayout four_type= (LinearLayout) view.findViewById(R.id.four_type);
            if(groupListNew.get(groupPosition).getType().equals("single_choice")||groupListNew.get(groupPosition).getType().equals("choice")){
                String []model={"A","B","C","D","E","F","G","H","I","J","K"};
                essay_layout.setVisibility(View.GONE);
                four_type.setVisibility(View.VISIBLE);
                TextView text_content= (TextView) view.findViewById(R.id.content);
                TextView text_choices= (TextView) view.findViewById(R.id.choices);
                TextView text_answer= (TextView) view.findViewById(R.id.answer);
                text_answer.setVisibility(View.VISIBLE);
                TextView text_result= (TextView) view.findViewById(R.id.result);
                text_result.setVisibility(View.VISIBLE);
                ImageView answer_image= (ImageView) view.findViewById(R.id.answer_image);
                ImageView result_image= (ImageView) view.findViewById(R.id.result_image);
                answer_image.setVisibility(View.GONE);
                result_image.setVisibility(View.GONE);
                String answer_stu="未填写答案";
                if(childNew.get(groupPosition).get(childPosition).getAnswer_stu().size()>0){
                    answer_stu="";
                }
                if(stu==1||stu==2){
                    TextView stu_answer= (TextView) view.findViewById(R.id.stu_answer);
                    stu_answer.setText("我的答案: ");
                }
                for(int i=0;i<childNew.get(groupPosition).get(childPosition).getAnswer_stu().size();i++){
                    if(!TextUtils.isEmpty(childNew.get(groupPosition).get(childPosition).getAnswer_stu().get(i).trim())){
                        answer_stu+=model[Integer.valueOf(childNew.get(groupPosition).get(childPosition).getAnswer_stu().get(i).trim())];

                    }
                }

                text_result.setText(answer_stu);
                TextView score= (TextView) view.findViewById(R.id.score);
                ImageView status_image= (ImageView) view.findViewById(R.id.status_image);
                if(childNew.get(groupPosition).get(childPosition).getStatus()==1){
                    text_result.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_green));
                    status_image.setImageResource(R.drawable.answer_right);
                }else {
                    text_result.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_black));
                    status_image.setImageResource(R.drawable.answer_error);
                }
                score.setText("得分"+childNew.get(groupPosition).get(childPosition).getResult_score());
                text_content.setText(childPosition+1+"."+childNew.get(groupPosition).get(childPosition).getStem_content());
                String choices = "";
                for(int i=0;i<childNew.get(groupPosition).get(childPosition).getChoices().size();i++){
                    choices+=model[Integer.valueOf(i)]+"."+childNew.get(groupPosition).get(childPosition).getChoices().get(i)+"\n";
                }
                text_choices.setText(choices);
                String answer="";
                for(int i=0;i<childNew.get(groupPosition).get(childPosition).getAnswer().size();i++){
                    answer+=model[Integer.valueOf(childNew.get(groupPosition).get(childPosition).getAnswer().get(i).trim())];
                }
                text_answer.setText(answer);
            }
            if(groupListNew.get(groupPosition).getType().equals("determine")){
                essay_layout.setVisibility(View.GONE);
                four_type.setVisibility(View.VISIBLE);
                TextView text_content= (TextView) view.findViewById(R.id.content);
                TextView text_choices= (TextView) view.findViewById(R.id.choices);
                text_choices.setVisibility(View.GONE);
                ImageView answer_image= (ImageView) view.findViewById(R.id.answer_image);
                TextView text_answer= (TextView) view.findViewById(R.id.answer);
                text_answer.setVisibility(View.GONE);
                answer_image.setVisibility(View.VISIBLE);
                TextView text_result= (TextView) view.findViewById(R.id.result);
                ImageView result_image= (ImageView) view.findViewById(R.id.result_image);
                text_result.setVisibility(View.GONE);
                result_image.setVisibility(View.VISIBLE);
                //答案是true还是false的答案图标
                if(childNew.get(groupPosition).get(childPosition).getAnswer().get(0).equals("1")){
                    answer_image.setImageResource(R.drawable.right_image);
                }else {
                    answer_image.setImageResource(R.drawable.error_image);

                }
                if(stu==1||stu==2){
                    TextView stu_answer= (TextView) view.findViewById(R.id.stu_answer);
                    stu_answer.setText("我的答案: ");
                }
                //本题状态图片
                ImageView status_image= (ImageView) view.findViewById(R.id.status_image);
                //如果得分
                if(childNew.get(groupPosition).get(childPosition).getStatus()==1){
                    if(childNew.get(groupPosition).get(childPosition).getAnswer().get(0).equals("1")){
                        result_image.setImageResource(R.drawable.right_image);
                        status_image.setImageResource(R.drawable.answer_right);
                    }
                    if(childNew.get(groupPosition).get(childPosition).getAnswer().get(0).equals("0")){
                        result_image.setImageResource(R.drawable.error_image);
                        status_image.setImageResource(R.drawable.answer_right);
                    }
                }else {//打错
                    if(childNew.get(groupPosition).get(childPosition).getAnswer().get(0).equals("1")){
                        result_image.setImageResource(R.drawable.stu_error_image);
                        status_image.setImageResource(R.drawable.answer_error);
                    }
                    if(childNew.get(groupPosition).get(childPosition).getAnswer().get(0).equals("0")){
                        result_image.setImageResource(R.drawable.error_stu_right);
                        status_image.setImageResource(R.drawable.answer_error);
                    }
                }

                TextView score= (TextView) view.findViewById(R.id.score);
                score.setText("得分"+childNew.get(groupPosition).get(childPosition).getResult_score());
                text_content.setText(childPosition+1+"."+childNew.get(groupPosition).get(childPosition).getStem_content());
            }
            if(groupListNew.get(groupPosition).getType().equals("fill")){
                essay_layout.setVisibility(View.GONE);
                four_type.setVisibility(View.VISIBLE);
                TextView text_content= (TextView) view.findViewById(R.id.content);
                TextView text_choices= (TextView) view.findViewById(R.id.choices);
                text_choices.setVisibility(View.GONE);
                TextView text_answer= (TextView) view.findViewById(R.id.answer);
                text_answer.setVisibility(View.VISIBLE);
                TextView text_result= (TextView) view.findViewById(R.id.result);
                text_result.setVisibility(View.VISIBLE);
                String answer = "未填写答案";
                if(childNew.get(groupPosition).get(childPosition).getAnswer_stu().size()>0){
                    answer="";
                }
                if(stu==1||stu==2){
                    TextView stu_answer= (TextView) view.findViewById(R.id.stu_answer);
                    stu_answer.setText("我的答案: ");
                }
                for(int i=0;i<childNew.get(groupPosition).get(childPosition).getAnswer_stu().size();i++){
                    answer+=childNew.get(groupPosition).get(childPosition).getAnswer_stu().get(i).trim()+";";
                }

                text_result.setText(answer);
                TextView score= (TextView) view.findViewById(R.id.score);
                score.setText("得分"+childNew.get(groupPosition).get(childPosition).getResult_score());
                text_content.setText(childPosition+1+"."+childNew.get(groupPosition).get(childPosition).getStem_content());
                String answers = "";
                for(int i=0;i<childNew.get(groupPosition).get(childPosition).getAnswer().size();i++){
                    answers+=childNew.get(groupPosition).get(childPosition).getAnswer().get(i).trim()+";";
                }
                text_answer.setText(answers);
                ImageView status_image= (ImageView) view.findViewById(R.id.status_image);
                if(childNew.get(groupPosition).get(childPosition).getStatus()==1){
                    text_result.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_green));
                    status_image.setImageResource(R.drawable.answer_right);
                }else{
                    status_image.setImageResource(R.drawable.answer_error);
                    text_result.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_black));
                }
            }
            if(groupListNew.get(groupPosition).getType().equals("essay")){
                Log.d("zw--11-",essay.size()+"essay");
                essay_layout.setVisibility(View.VISIBLE);
                four_type.setVisibility(View.GONE);
                EditText tea_answer= (EditText) view.findViewById(R.id.tea_answer);
                TextView text_content= (TextView) view.findViewById(R.id.content);
                TextView finish_answer= (TextView) view.findViewById(R.id.finish_answer);
                //给分的布局
                LinearLayout score_layout= (LinearLayout) view.findViewById(R.id.scroe_layout);

                final TextView answer_essay= (TextView) view.findViewById(R.id.answer_essay);
                final TextView result_eaasy= (TextView) view.findViewById(R.id.result_eaasy);

                if(stu==1||stu==2){//学生查看自己完成的
                    result_eaasy.setText("我的答案");
                }
                TextView score= (TextView) view.findViewById(R.id.score);
                if(status.equals("3")){
                    score_layout.setVisibility(View.GONE);
                    score.setText("得分: "+essay_score_list.get(childPosition));
                    tea_answer.setVisibility(View.GONE);
                    finish_answer.setVisibility(View.VISIBLE);
                    if(stu==2){
                        if(childNew.get(groupPosition).get(childPosition).getScore().contains(".0")){
                            score.setText("总分: "+childNew.get(groupPosition).get(childPosition).getScore()+"分");
                        }else {
                            score.setText("总分: "+childNew.get(groupPosition).get(childPosition).getScore()+".0分");
                        }
                    }
                }else {
                    score_layout.setVisibility(View.VISIBLE);
                    finish_answer.setVisibility(View.GONE);
                    tea_answer.setVisibility(View.VISIBLE);
                    score.setText("题分: "+childNew.get(groupPosition).get(childPosition).getScore());
                }
                ///增减问答分数
                final TextView dele_score= (TextView) view.findViewById(R.id.dele_score);
                final TextView score_essay= (TextView) view.findViewById(R.id.score_essay);
                final TextView add_score= (TextView) view.findViewById(R.id.add_score);
                //隐藏选择选项布局
                TextView text_choices= (TextView) view.findViewById(R.id.choices);
                text_choices.setVisibility(View.GONE);
                //问答的布局view
                final String answerUrl="";
                final TextView essay_text= (TextView) view.findViewById(R.id.essay_text);
                final ImageView essay_image= (ImageView) view.findViewById(R.id.essay_image);
                text_content.setText(childPosition+1+"."+childNew.get(groupPosition).get(childPosition).getStem_content());
                if(childNew.get(groupPosition).get(childPosition).getAnswer_stu().size()>0){
                    essay_text.setText(childNew.get(groupPosition).get(childPosition).getAnswer_stu().get(0));
                }else {
                    essay_text.setText("未填写答案");
                }
                score_essay.setText(essay_score_list.get(childPosition));
                int po=0;
                answer_essay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        answer_essay.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_white));
                        answer_essay.setBackgroundResource(R.drawable.choice_type);
                        result_eaasy.setBackgroundResource(R.drawable.choice_type1);
                        result_eaasy.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_black));
                        essay_text.setText(childNew.get(groupPosition).get(childPosition).getAnswer().get(0));
                        if(!TextUtils.isEmpty(answerUrl)){
//                            imageLoader.displayImage(answerUrl,
//                                    essay_image, topicSquare);
                            essay_image.setVisibility(View.VISIBLE);
                        }
                    }
                });
                result_eaasy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        essay_image.setVisibility(View.GONE);
                        answer_essay.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_black));
                        answer_essay.setBackgroundResource(R.drawable.choice_type1);
                        result_eaasy.setBackgroundResource(R.drawable.choice_type);
                        result_eaasy.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_white));
                        if(childNew.get(groupPosition).get(childPosition).getAnswer_stu().size()>0){
                            essay_text.setText(childNew.get(groupPosition).get(childPosition).getAnswer_stu().get(0));
                        }else {
                            essay_text.setText("未填写答案");
                        }
                    }
                });
                if(Float.parseFloat(score_essay.getText().toString())>0){
                    dele_score.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_red));

                }
                add_score.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dele_score.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_red));
                        if(Float.parseFloat(score_essay.getText().toString())<Float.parseFloat(childNew.get(groupPosition).get(childPosition).getScore())){
                            DecimalFormat decimalFormat=new DecimalFormat("0.0");
                            score_essay.setText(decimalFormat.format(Float.parseFloat(score_essay.getText().toString())+0.1)+"");
                            essay_score_list.set(childPosition,decimalFormat.format(Float.parseFloat(score_essay.getText().toString()))+"");
                        }
                        if(Float.parseFloat(score_essay.getText().toString())==Float.parseFloat(childNew.get(groupPosition).get(childPosition).getScore())){
                            add_score.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_gray));
                        }
                    }
                });
                dele_score.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add_score.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_blue));
                        if(Float.parseFloat(score_essay.getText().toString())-0.1>=0){
//                            score_essay.setText(Float.parseFloat(score_essay.getText().toString())-0.1+"");
//                            essay_score_list.set(childPosition,score_essay.getText().toString());
                            DecimalFormat decimalFormat=new DecimalFormat("0.0");
                            score_essay.setText(decimalFormat.format(Float.parseFloat(score_essay.getText().toString())-0.1)+"");
                            essay_score_list.set(childPosition,decimalFormat.format(Float.parseFloat(score_essay.getText().toString()))+"");
                        }
                        if(Float.parseFloat(score_essay.getText().toString())==0){
                            dele_score.setTextColor(ContextCompat.getColor(CheckStuWorkActivity.this,R.color.font_gray));
                        }
                    }
                });
                tea_answer.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent event) {

                        // 在TOUCH的UP事件中，要保存当前的行下标，因为弹出软键盘后，整个画面会被重画
                        // 在getView方法的最后，要根据index和当前的行下标手动为EditText设置焦点
                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            index= childPosition;

                        }
                        return false;
                    }
                });
                tea_answer.setText(tea_word.get(childPosition));
                finish_answer.setText(tea_word.get(childPosition));
                tea_answer.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tea_word.set(childPosition,s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                tea_answer.clearFocus();
                if(index!= -1 && index == childPosition) {
                    // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
                    tea_answer.requestFocus();

                }
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

            if(stu==1||stu==2){
                for(int i = 0; i < getGroupCount(); i++){
                    expandableListView.expandGroup(i);
                }
            }else {
                if(question_type_sort.contains(5)){
                    expandableListView.expandGroup(getGroupCount()-1);
                }
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i("TAG--J","1");
            //返回事件
            if(status.equals("3")){
                finish();
            }else {
                dialog();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Constants.LOGIN_BACK){
            gethttpRequstOne();
        }

    }
}
