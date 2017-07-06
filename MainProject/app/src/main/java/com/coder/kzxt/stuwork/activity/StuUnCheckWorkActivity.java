package com.coder.kzxt.stuwork.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.entity.CheckWorkBean;
import com.coder.kzxt.buildwork.entity.SingleChoice;
import com.coder.kzxt.buildwork.entity.StuWorkChild;
import com.coder.kzxt.buildwork.entity.StuWorkGroup;
import com.coder.kzxt.buildwork.entity.WorkOrderBean;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.app.http.UrlsNew;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StuUnCheckWorkActivity extends BaseActivity implements HttpCallBack {
    private RelativeLayout activity_layout;
    private ImageView back_title_button;
    private TextView title;
    private TextView rightText;
    private Button load_fail_button;
    private RelativeLayout network_set_layout;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private ExpandableListView expandableListView;
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
    private ExpandAdapter expandAdapter;
    private String testid;//跳转页面时候用
    private List<SingleChoice> single_choice_list;
    private List<SingleChoice>choice_list;
    private List<SingleChoice>determine;
    private List<SingleChoice>fill_list;
    private List<SingleChoice>essay;
    private List<Integer>question_type_sort=new ArrayList<>();//代表此时卷有几种题型以及题型展示顺序
    private int workType,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_stu_work);
        // pu=new PublicUtils(this);
        resultId=getIntent().getStringExtra("resultId");
        nickname=getIntent().getStringExtra("nickname");
        testid=getIntent().getStringExtra("testid");
        workType=getIntent().getIntExtra("workType",1);
        status=getIntent().getIntExtra("status",1);
        single_choice_list=new ArrayList<>();
        choice_list=new ArrayList<>();
        determine=new ArrayList<>();
        fill_list=new ArrayList<>();
        essay=new ArrayList<>();

        activity_layout= (RelativeLayout) findViewById(R.id.activity_check_stu_work);
        back_title_button = (ImageView) findViewById(R.id.leftImage);
        title = (TextView) findViewById(R.id.title);
        rightText = (TextView) findViewById(R.id.rightText);
        search_jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        network_set_layout = (RelativeLayout) findViewById(R.id.network_set_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        expandableListView= (ExpandableListView) findViewById(R.id.expandlist);
        rightText.setVisibility(View.GONE);

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
        initOnevents();
        search_jiazai_layout.setVisibility(View.VISIBLE);
        gethttpRequstOne();
        Log.d("zw--size",groupListNew.size()+"&&"+childNew.size());
        expandableListView.setAdapter(expandAdapter);

    }
    //获取各个题型中相应试题结果的集合
    private CheckWorkBean checkWorkBean;
    private  int firstCode;
    private void gethttpRequstOne() {
        if(resultId.equals("0")){
            firstCode=1000;
            gethttpRequstmodel();
        }else {
            firstCode=1001;
            //如果学生未做过这个试卷则这个id是0，不用请求这个接口，而且此页面目前没有展示学生答案（当id不是0时候请求这个接口了，只是没有用到里面的数据）
            //这个接口可以得到学生所答题的情况
            new HttpGetBuilder(StuUnCheckWorkActivity.this).setHttpResult(this)
                    .setUrl(UrlsNew.TEST_RESULT)
                    .setClassObj(CheckWorkBean.class)
                    .addQueryParams("page","1")
                    .addQueryParams("pageSize","200")
                    .setPath(resultId)
                    .setRequestCode(firstCode)
                    .build();
        }
        //获取每题学生答题情况1001


    }
    //获取各个题型的顺序
    private void gethttpRequstmodel() {
        new HttpGetBuilder(StuUnCheckWorkActivity.this).setHttpResult(this)
                .setUrl(UrlsNew.TEST_PAPER_PUBLISH)
                .setClassObj(WorkOrderBean.class)
                .setPath(testid)
                .setRequestCode(1002)
                .build();
    }
    //获取各个题型的集合
    private void gethttpRequstTwo() {
        new HttpGetBuilder(StuUnCheckWorkActivity.this).setHttpResult(this)
                .setUrl(UrlsNew.TEST_PAPER_QUESTION)
                .setClassObj(null)
                .addQueryParams("test_paper_id",testid)
                .setRequestCode(1003)
                .build();

    }
    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
//        if(firstCode==1000){
//           // checkWorkBean=new CheckWorkBean();
//
//        }
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

            if(checkWorkBean!=null){
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
                        }
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
            Log.d("zw--",essay.size()+"essay");
            //然后通过小题设置group的属性
            for(int i=0;i<question_type_sort.size();i++){
                if(question_type_sort.get(i)==1){
                    StuWorkGroup stuWorkGroup=new StuWorkGroup();
                    float total_score = 0;
                    float num_score=0;
                    for(int s=0;s<single_choice_list.size();s++){
                        total_score+=Float.valueOf(single_choice_list.get(s).getScore());
                        //num_score+=Float.valueOf(single_choice_list.get(s).getResult_score());
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
                        //num_score+=Float.valueOf(choice_list.get(s).getResult_score());
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
                        //num_score+=Float.valueOf(determine.get(s).getResult_score());
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
                        //num_score+=Float.valueOf(fill_list.get(s).getResult_score());
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
                        //num_score+=Float.valueOf(essay.get(s).getResult_score());
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
            Log.d("zw----",childNew.size()+"essay");
            expandAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        search_jiazai_layout.setVisibility(View.GONE);

        if(code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
            //重新登录
            NetworkUtil.httpRestartLogin(StuUnCheckWorkActivity.this,activity_layout);
        }else {
            //加载失败
            load_fail_layout.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.GONE);
            NetworkUtil.httpNetErrTip(StuUnCheckWorkActivity.this,activity_layout);
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
        back_title_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_fail_layout.setVisibility(View.GONE);
                search_jiazai_layout.setVisibility(View.VISIBLE);
                gethttpRequstOne();
            }
        });
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
            DecimalFormat decimalFormat=new DecimalFormat("0.0");

            if(isExpanded){
                jiantou_image.setImageResource(R.drawable.department_up);
            }else {
                jiantou_image.setImageResource(R.drawable.department_down);
            }
            textView.setText(groupListNew.get(groupPosition).getName());
            num_total.setText(groupListNew.get(groupPosition).getCount());
            score_total.setText(Float.valueOf(groupListNew.get(groupPosition).getTotalScore().trim())+"");
            score_total.setText(decimalFormat.format(Float.valueOf(groupListNew.get(groupPosition).getTotalScore().trim()))+"");
            return view;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View  view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_uncheck_work,null);
            LinearLayout answer_layout= (LinearLayout) view.findViewById(R.id.answer_layout);
            LinearLayout true_answer_layout= (LinearLayout) view.findViewById(R.id.true_answer_layout);
            TextView my_answer= (TextView) view.findViewById(R.id.result);
            TextView true_result= (TextView) view.findViewById(R.id.true_result);
            if(status==3){
                answer_layout.setVisibility(View.GONE);
                true_answer_layout.setVisibility(View.VISIBLE);
            }
            if(status==22){
                answer_layout.setVisibility(View.VISIBLE);
                true_answer_layout.setVisibility(View.GONE);
            }
            TextView text_content= (TextView) view.findViewById(R.id.content);
            TextView score= (TextView) view.findViewById(R.id.score);
            score.setText("总分"+Float.valueOf(childNew.get(groupPosition).get(childPosition).getScore().trim())+"分");
            text_content.setText(childPosition+1+"."+childNew.get(groupPosition).get(childPosition).getStem_content());

            if(groupListNew.get(groupPosition).getType().equals("single_choice")||groupListNew.get(groupPosition).getType().equals("choice")){
                String []model={"A","B","C","D","E","F","G","H","I","J","K"};
                TextView choice= (TextView) view.findViewById(R.id.choices);
                choice.setVisibility(View.VISIBLE);
                String choices = "";
                for(int i=0;i<childNew.get(groupPosition).get(childPosition).getChoices().size();i++){
                    choices+=model[Integer.valueOf(i)]+"."+childNew.get(groupPosition).get(childPosition).getChoices().get(i)+"\n";
                }
                if(checkWorkBean!=null){
//学生答案
                    if(childNew.get(groupPosition).get(childPosition).getAnswer_stu().size()>0){
                        String answer_stu="";

                        for(int i=0;i<childNew.get(groupPosition).get(childPosition).getAnswer_stu().size();i++){
                            if(!TextUtils.isEmpty(childNew.get(groupPosition).get(childPosition).getAnswer_stu().get(i).trim())){
                                answer_stu+=model[Integer.valueOf(childNew.get(groupPosition).get(childPosition).getAnswer_stu().get(i).trim())];
                            }
                        }
                        my_answer.setText(answer_stu);
                    }else {
                        my_answer.setText("未填写答案");
                    }
                }

                //参考答案
                if(childNew.get(groupPosition).get(childPosition).getAnswer().size()>0){
                    String answers="";
                    for(int i=0;i<childNew.get(groupPosition).get(childPosition).getAnswer().size();i++){
                        if(!TextUtils.isEmpty(childNew.get(groupPosition).get(childPosition).getAnswer().get(i).trim())){
                            answers+=model[Integer.valueOf(childNew.get(groupPosition).get(childPosition).getAnswer().get(i).trim())];
                        }
                    }
                    true_result.setText(answers);
                }else {
                    true_result.setText("没有参考答案");
                }

                choice.setText(choices);
            }
            if(groupListNew.get(groupPosition).getType().equals("determine")){
                if(checkWorkBean!=null){
                    if(childNew.get(groupPosition).get(childPosition).getAnswer_stu().size()>0){
                        if(childNew.get(groupPosition).get(childPosition).getAnswer_stu().get(0).equals("1")){
                            my_answer.setText("正确");
                        }else {
                            my_answer.setText("错误");
                        }
                    }else {
                        my_answer.setText("未填写答案");
                    }
                }

                if(childNew.get(groupPosition).get(childPosition).getAnswer().size()>0){
                    if(childNew.get(groupPosition).get(childPosition).getAnswer().get(0).equals("1")){
//                        true_result.setText("正确");
                        true_result.setBackgroundDrawable(getDrawable(R.drawable.right_image));
                    }else {
                       // true_result.setText("错误");
                        true_result.setBackgroundDrawable(getDrawable(R.drawable.error_image));
                    }
                }else {
                    true_result.setText("没有参考答案");
                }

            }
            if(groupListNew.get(groupPosition).getType().equals("fill")){
                if(checkWorkBean!=null){
                    if(childNew.get(groupPosition).get(childPosition).getAnswer_stu().size()>0){
                        String answer = "";
                        for(int i=0;i<childNew.get(groupPosition).get(childPosition).getAnswer_stu().size();i++){
                            answer+=childNew.get(groupPosition).get(childPosition).getAnswer_stu().get(i).trim()+";";
                        }
                        my_answer.setText(answer);
                    }else {
                        my_answer.setText("未填写答案");
                    }
                }

                //参考答案
                if(childNew.get(groupPosition).get(childPosition).getAnswer().size()>0){
                    String answer = "";
                    for(int i=0;i<childNew.get(groupPosition).get(childPosition).getAnswer().size();i++){
                        answer+=childNew.get(groupPosition).get(childPosition).getAnswer().get(i).trim()+";";
                    }
                    true_result.setText(answer);
                }else {
                    true_result.setText("没有参考答案");
                }

            }
            if(groupListNew.get(groupPosition).getType().equals("essay")){
                if(checkWorkBean!=null){
                    if(childNew.get(groupPosition).get(childPosition).getAnswer_stu().size()>0){
                        my_answer.setText(childNew.get(groupPosition).get(childPosition).getAnswer_stu().get(0));
                    }else {
                        my_answer.setText("未填写答案");
                    }
                }

                if(childNew.get(groupPosition).get(childPosition).getAnswer().size()>0){
                    true_result.setText(childNew.get(groupPosition).get(childPosition).getAnswer().get(0));
                }else {
                    true_result.setText("没有参考答案");
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
            for(int i = 0; i < getGroupCount(); i++){
                expandableListView.expandGroup(i);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i("TAG--J","1");
            //返回事件
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
