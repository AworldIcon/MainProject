package com.coder.kzxt.stuwork.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.buildwork.entity.CheckWorkBean;
import com.coder.kzxt.buildwork.entity.SingleChoice;
import com.coder.kzxt.buildwork.entity.WorkOrderBean;
import com.coder.kzxt.stuwork.entity.AnswerSheet;
import com.coder.kzxt.stuwork.entity.CommitAnswer;
import com.coder.kzxt.stuwork.entity.QuestionChoicesContent;
import com.coder.kzxt.stuwork.entity.QuestionContent;
import com.coder.kzxt.stuwork.entity.QuestionType;
import com.coder.kzxt.stuwork.fragment.ChoiceFragment;
import com.coder.kzxt.stuwork.fragment.DetemineFragment;
import com.coder.kzxt.stuwork.fragment.EssayFragment;
import com.coder.kzxt.stuwork.fragment.FillFragment;
import com.coder.kzxt.stuwork.fragment.SingleChoiceFragment;
import com.coder.kzxt.stuwork.util.CommitTestAnswer;
import com.coder.kzxt.stuwork.util.DownloadData;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.Counter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TestPageActivity extends BaseActivity implements HttpCallBack {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageView leftImage;
    private TextView title,up_work,down_work;
    private TextView rightText;
    private String resultId;
    private String commitType;
    private Boolean isCourse; //区分课程 服务
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private Button load_fail_button;
    private LinearLayout gesturesLy;
    private DisplayMetrics dm;
    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    private ArrayList<String> userChannelList = new ArrayList<String>();
    private Adapter viewPageAdapter;
    private String showType;
    private int limitTime;//限时时间
    private String limit;//是否限时
    private Counter counter;
    private String isCenter;
    private int workType;
    // 题目类型
    private ArrayList<QuestionType> questionTypes = new ArrayList<QuestionType>();
    // 单选题内容s
    private ArrayList<QuestionContent> single_choiceList = new ArrayList<QuestionContent>();
    // 多选题内容
    private ArrayList<QuestionContent> choiceList = new ArrayList<QuestionContent>();
    // 不定向选择题内容
    private ArrayList<QuestionContent> uncertain_choiceList = new ArrayList<QuestionContent>();
    // 判断题内容
    private ArrayList<QuestionContent> determine_choiceList = new ArrayList<QuestionContent>();
    // 添空题内容
    private ArrayList<QuestionContent> fill_choiceList = new ArrayList<QuestionContent>();
    // 问答题内容
    private ArrayList<QuestionContent> essay_choiceList = new ArrayList<QuestionContent>();
    // 材料题内容
    private ArrayList<QuestionContent> material_choiceList = new ArrayList<QuestionContent>();
    // 答题卡内容
    private ArrayList<AnswerSheet> answerSheets = new ArrayList<AnswerSheet>();
    private SharedPreferences sp;
    private ArrayList<CommitAnswer> answers; // 储存提交的答案对象

    private int beforePosition = 0;

    public static final String TEST_PAGE_STATUS_TEST = "test";
    public static final String TEST_PAGE_STATUS_PREVIEW = "preview";
    public static final String TEST_PAGE_STATUS_RWSULT = "result";
    private MyReceiver receiver;
    private RelativeLayout activity_test_page;
    private TextView answer_card;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            ViewPager pager=fragments.get(mViewPager.getCurrentItem()).getPager();
            int arg=msg.what;
            if(arg==200){
                if(mViewPager.getCurrentItem()<mViewPager.getAdapter().getCount()-1||pager.getCurrentItem()<pager.getAdapter().getCount()-1){
                    down_work.setVisibility(View.VISIBLE);
                }
                if(mViewPager.getCurrentItem()==mViewPager.getAdapter().getCount()-1&&pager.getCurrentItem()==pager.getAdapter().getCount()-1){
                    down_work.setVisibility(View.GONE);
                    L.d("zw--2",mViewPager.getCurrentItem()+"mmm"+pager.getCurrentItem());
                    answer_card.setText("提交");
                }else {
                    answer_card.setText("答题卡");
                }
                if(pager.getCurrentItem()>0||mViewPager.getCurrentItem()>0){
                    up_work.setVisibility(View.VISIBLE);
                }
                if(pager.getCurrentItem()==0&&mViewPager.getCurrentItem()==0){
                    up_work.setVisibility(View.GONE);
                }
            }

            super.handleMessage(msg);
        }
    };
    public Handler getHandler(){
        return handler;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_page);
        single_choice_list=new ArrayList<>();
        choice_list=new ArrayList<>();
        determine=new ArrayList<>();
        fill_list=new ArrayList<>();
        essay=new ArrayList<>();
        mTabLayout = (TabLayout) findViewById(R.id.teach_tablayout);
        activity_test_page= (RelativeLayout) findViewById(R.id.activity_test_page);
        mViewPager = (ViewPager) findViewById(R.id.teach_viewpager);
        // pu = new PublicUtils(this);
        showType = getIntent().getStringExtra("showType");
        answers = new ArrayList<CommitAnswer>();
        resultId = getIntent().getStringExtra("test_result_id");
        test_paper_id=getIntent().getStringExtra("test_paper_id");
        limitTime=getIntent().getIntExtra("limit_time",600);
        workType=getIntent().getIntExtra("workType",2);
        isCenter = getIntent().getStringExtra(Constants.IS_CENTER) == null ? "0" : getIntent().getStringExtra(Constants.IS_CENTER);
        title = (TextView) findViewById(R.id.title);
        commitType = getIntent().getStringExtra("commitType");
        //是否是课程 区分服务
        isCourse=getIntent().getBooleanExtra("isCourse",true);
        rightText = (TextView) findViewById(R.id.rightText);
        leftImage = (ImageView) findViewById(R.id.leftImage);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        gesturesLy = (LinearLayout) findViewById(R.id.gesturesLy);
        dm = getResources().getDisplayMetrics();
        viewPageAdapter = new Adapter(getSupportFragmentManager());
        answer_card= (TextView) findViewById(R.id.answer_card);
        if(workType==1){
            rightText.setVisibility(View.VISIBLE);
        }else {
            rightText.setVisibility(View.GONE);
        }

        mTabLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);

        up_work= (TextView) findViewById(R.id.up_work);
        down_work= (TextView) findViewById(R.id.down_work);


        sp = getSharedPreferences("start", Context.MODE_WORLD_WRITEABLE);
        if (sp.getBoolean("isFirstTestPage", true)) {
            gesturesLy.setVisibility(View.VISIBLE);
        }

        InintEvent();
        jiazai_layout.setVisibility(View.VISIBLE);
        if(workType==1){
                // 如果是考试,先上报服务器学生开始考试了，如果上报过一次再上报就会失败，此时在失败的回调中根据code重新拉去答题
            startHttpRequest();
        }else {//开始作业，不需要上报服务器
            getHttpRequstOne();
        }

        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.MY_REFRESG_TESTPAGER);
//        filter.addAction(Constants.MY_TIME_TO_COMPLETE);
        // 注册广播
        registerReceiver(receiver, filter);

    }
    private void startHttpRequest(){
        new HttpPutBuilder(this).setClassObj(null).setUrl(isCourse?UrlsNew.TEST_START:UrlsNew.TEST_START_SERVICE)
                .setHttpResult(this)
                .setPath(resultId)
                .setRequestCode(100)
                .build();
    }
    private void InintEvent() {
        leftImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (showType.equals(TEST_PAGE_STATUS_TEST)) {
                    DownloadData.answerList.clear();
                }
                TestPageActivity.this.finish();
            }
        });

        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        down_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                up_work.setVisibility(View.VISIBLE);
                ViewPager pager=fragments.get(mViewPager.getCurrentItem()).getPager();
                if(pager!=null&&pager.getCurrentItem()<pager.getAdapter().getCount()-1){
                    pager.setCurrentItem(pager.getCurrentItem()+1);
                }else {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                }
//                if(mViewPager.getCurrentItem()==mViewPager.getAdapter().getCount()-1&&pager.getCurrentItem()==pager.getAdapter().getCount()-1){
//                    down_work.setVisibility(View.GONE);
//                    L.d("zw--3",mViewPager.getCurrentItem()+"mmm"+pager.getCurrentItem());
//                    answer_card.setText("提交");
//                }else {
//                    answer_card.setText("答题卡");
//                }
            }
        });
        answer_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int firstPo,secondPo;
                secondPo=fragments.get(mViewPager.getCurrentItem()).getPager().getCurrentItem();
                firstPo=mViewPager.getCurrentItem();
                int type=question_type_sort.get(firstPo);
                if(type==1){
                    singleChoiceFragment.getAdapter().Commit(secondPo,workType,true);
                }
                if(type==2){
                    choiceFragment.getAdapter().Commit(secondPo,workType,true);
                }
                if(type==3){
                    fillFragment.getAdapter().Commit(secondPo,workType,true);
                }
                if(type==4){
                    detemineFragment.getAdapter().Commit(secondPo,workType,true);
                }
                if(type==5){
                    essayFragment.getAdapter().Commit(secondPo,workType,true);
                }

            }
        });
        up_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                down_work.setVisibility(View.VISIBLE);
                answer_card.setText("答题卡");
                ViewPager pager=fragments.get(mViewPager.getCurrentItem()).getPager();
                if(pager!=null&&pager.getCurrentItem()<pager.getAdapter().getCount()&&pager.getCurrentItem()>0){
                    pager.setCurrentItem(pager.getCurrentItem()-1);
                }else {
                    if(mViewPager.getCurrentItem()>0){
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
                    }
                }
                if(mViewPager.getCurrentItem()==0&&pager.getCurrentItem()==0){
                    up_work.setVisibility(View.GONE);
                }
            }
        });

        load_fail_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                load_fail_layout.setVisibility(View.GONE);
                getHttpRequstOne();
            }
        });

        gesturesLy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                gesturesLy.setVisibility(View.GONE);
                sp.edit().putBoolean("isFirstTestPage", false).commit();
            }
        });
    }

    private List<Integer> question_type_sort=new ArrayList<>();//代表此时卷有几种题型以及题型展示顺序
    private List<List<SingleChoice>>lists=new ArrayList<>();
    private static String score_full="";
    private String test_paper_id="";
    private CheckWorkBean checkWorkBean;
    private List<SingleChoice> single_choice_list;
    private List<SingleChoice>choice_list;
    private List<SingleChoice>determine;
    private List<SingleChoice>fill_list;
    private List<SingleChoice>essay;
    private ArrayList<List<SingleChoice>>childNew=new ArrayList<>();
    private HashMap<String,List<SingleChoice>>childMap=new HashMap<>();
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
    //先获取大题型顺序
    private void getHttpRequstOne(){
        new HttpGetBuilder(this).setHttpResult(this)
                .setUrl(isCourse?UrlsNew.TEST_PAPER_PUBLISH:UrlsNew.TEST_PAPER_PUBLISH_SERVICE)
                .setClassObj(WorkOrderBean.class)
                .setPath(test_paper_id)
                .setRequestCode(1001)
                .build();
    }
    //获取学生答题内容
    private void getHttpRequstTwo(){
        new HttpGetBuilder(this).setHttpResult(this)
                .setUrl(isCourse?UrlsNew.TEST_RESULT:UrlsNew.TEST_RESULT_SERVICE)
                .setClassObj(CheckWorkBean.class)
                .addQueryParams("page","1")
                .addQueryParams("pageSize","20000")
                .setPath(resultId)
                .setRequestCode(1002)
                .build();
    }
    //获取试题内容
    private void getHttpRequstThree(){
        new HttpGetBuilder(this).setHttpResult(this)
                .setUrl(isCourse?UrlsNew.TEST_PAPER_QUESTION:UrlsNew.TEST_PAPER_QUESTION_SERVICE)
                .setClassObj(null)
                .addQueryParams("test_paper_id",test_paper_id)
                .setRequestCode(1003)
                .build();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if(requestCode==100){
            getHttpRequstOne();
        }
            if(requestCode==1001){
                WorkOrderBean workOrderBean= (WorkOrderBean) resultBean;
                score_full=workOrderBean.getItem().getScore()+"";
//                for(int i=0;i<workOrderBean.getItem().getQuestion_type_sort().size();i++){
//                    question_type_sort.add(workOrderBean.getItem().getQuestion_type_sort().get(i));
//                }
                question_type_sort.add(1);
                question_type_sort.add(2);
                question_type_sort.add(3);
                question_type_sort.add(4);
                question_type_sort.add(5);
                getHttpRequstTwo();
            }
            if(requestCode==1002){
                checkWorkBean= (CheckWorkBean) resultBean;
                L.d("zw--",checkWorkBean.getItem().getDetail().size()+"---1001");
                getHttpRequstThree();
            }
            if(requestCode==1003){
                jiazai_layout.setVisibility(View.GONE);
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
                }else {
                    QuestionType questionTypeEntity = new QuestionType();
                    String questionType="" ;// 名称
                    float total_score = 0;
                    for(int s=0;s<single_choice_list.size();s++){
                        total_score+=Float.valueOf(single_choice_list.get(s).getScore());
                    }
                    questionTypeEntity.setQuestionScore(total_score+"");
                    questionTypeEntity.setQuestionNumber(single_choice_list.size()+"");
                    questionType="single_choice";
                    questionTypeEntity.setQuestionTitle(getResources().getString(R.string.single_choice));
                    questionTypeEntity.setQuestionType(questionType);
                    questionTypes.add(questionTypeEntity);

                }
                if(choice_list.size()==0){
                    question_type_sort.remove(question_type_sort.indexOf(2));
                }else {
                    QuestionType questionTypeEntity = new QuestionType();
                    String questionType="" ;// 名称
                    float total_score = 0;
                    for(int s=0;s<choice_list.size();s++){
                        total_score+=Float.valueOf(choice_list.get(s).getScore());
                    }
                    questionTypeEntity.setQuestionScore(total_score+"");
                    questionTypeEntity.setQuestionNumber(choice_list.size()+"");
                    questionType="choice";
                    questionTypeEntity.setQuestionTitle(getResources().getString(R.string.multiple_choice));
                    questionTypeEntity.setQuestionType(questionType);
                    questionTypes.add(questionTypeEntity);
                }
                if(fill_list.size()==0){
                    question_type_sort.remove(question_type_sort.indexOf(3));
                }else {
                    QuestionType questionTypeEntity = new QuestionType();
                    String questionType="" ;// 名称
                    float total_score = 0;
                    for(int s=0;s<fill_list.size();s++){
                        total_score+=Float.valueOf(fill_list.get(s).getScore());
                    }
                    questionTypeEntity.setQuestionScore(total_score+"");
                    questionTypeEntity.setQuestionNumber(fill_list.size()+"");

                    questionType="fill";
                    questionTypeEntity.setQuestionTitle(getResources().getString(R.string.gap_filling));
                    questionTypeEntity.setQuestionType(questionType);

                    questionTypes.add(questionTypeEntity);
                }
                if(determine.size()==0){
                    question_type_sort.remove(question_type_sort.indexOf(4));
                }else {
                    QuestionType questionTypeEntity = new QuestionType();
                    String questionType="" ;// 名称
                    float total_score = 0;
                    for(int s=0;s<determine.size();s++){
                        total_score+=Float.valueOf(determine.get(s).getScore());
                    }
                    questionTypeEntity.setQuestionScore(total_score+"");
                    questionTypeEntity.setQuestionNumber(determine.size()+"");
                    questionType="determine";
                    questionTypeEntity.setQuestionTitle(getResources().getString(R.string.judge_topic));
                    questionTypeEntity.setQuestionType(questionType);
                    questionTypes.add(questionTypeEntity);

                }

                if(essay.size()==0){
                    question_type_sort.remove(question_type_sort.indexOf(5));
                }else {
                    QuestionType questionTypeEntity = new QuestionType();
                    String questionType="" ;// 名称
                    float total_score = 0;
                    for(int s=0;s<essay.size();s++){
                        total_score+=Float.valueOf(essay.get(s).getScore());
                    }
                    questionTypeEntity.setQuestionScore(total_score+"");
                    questionTypeEntity.setQuestionNumber(essay.size()+"");
                    questionType="essay";
                    questionTypeEntity.setQuestionTitle(getResources().getString(R.string.questions_topic));
                    questionTypeEntity.setQuestionType(questionType);

                    questionTypes.add(questionTypeEntity);

                }


                for(int i=0;i<checkWorkBean.getItem().getDetail().size();i++){
                    for(int j=0;j<single_choice_list.size();j++){

                        if(checkWorkBean.getItem().getDetail().get(i).getQuestion_id().trim().equals(single_choice_list.get(j).getId().trim())){
                            single_choice_list.get(j).setTest_result_id(checkWorkBean.getItem().getDetail().get(i).getTest_result_id());
                            single_choice_list.get(j).setStatus(checkWorkBean.getItem().getDetail().get(i).getStatus());
                            single_choice_list.get(j).setQuestion_id(Integer.valueOf(checkWorkBean.getItem().getDetail().get(i).getId().trim()));
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
                            choice_list.get(j).setQuestion_id(Integer.valueOf(checkWorkBean.getItem().getDetail().get(i).getId().trim()));

                        }
                    }
                    for(int j=0;j<fill_list.size();j++){
                        if(checkWorkBean.getItem().getDetail().get(i).getQuestion_id().equals(fill_list.get(j).getId())){
                            fill_list.get(j).setTest_result_id(checkWorkBean.getItem().getDetail().get(i).getTest_result_id());
                            fill_list.get(j).setStatus(checkWorkBean.getItem().getDetail().get(i).getStatus());
                            fill_list.get(j).setResult_score(checkWorkBean.getItem().getDetail().get(i).getScore());
                            fill_list.get(j).setAnswer_stu(checkWorkBean.getItem().getDetail().get(i).getAnswer());
                            fill_list.get(j).setQuestion_id(Integer.valueOf(checkWorkBean.getItem().getDetail().get(i).getId().trim()));

                        }
                    }
                    for(int j=0;j<determine.size();j++){
                        if(checkWorkBean.getItem().getDetail().get(i).getQuestion_id().equals(determine.get(j).getId())){
                            determine.get(j).setTest_result_id(checkWorkBean.getItem().getDetail().get(i).getTest_result_id());
                            determine.get(j).setStatus(checkWorkBean.getItem().getDetail().get(i).getStatus());
                            determine.get(j).setResult_score(checkWorkBean.getItem().getDetail().get(i).getScore());
                            determine.get(j).setAnswer_stu(checkWorkBean.getItem().getDetail().get(i).getAnswer());
                            determine.get(j).setQuestion_id(Integer.valueOf(checkWorkBean.getItem().getDetail().get(i).getId().trim()));

                        }
                    }
                    for(int j=0;j<essay.size();j++){
                        if(checkWorkBean.getItem().getDetail().get(i).getQuestion_id().equals(essay.get(j).getId())){
                            essay.get(j).setTest_result_id(checkWorkBean.getItem().getDetail().get(i).getTest_result_id());
                            essay.get(j).setStatus(checkWorkBean.getItem().getDetail().get(i).getStatus());
                            essay.get(j).setResult_score(checkWorkBean.getItem().getDetail().get(i).getScore());
                            essay.get(j).setAnswer_stu(checkWorkBean.getItem().getDetail().get(i).getAnswer());
                            essay.get(j).setQuestion_id(Integer.valueOf(checkWorkBean.getItem().getDetail().get(i).getId().trim()));

                        }
                    }
                }
                //给每个小题设置所需属性
                if(single_choice_list.size()>0){
                    childMap.put("1",single_choice_list);
                    analysisChoice("single_choice");
                }
                if(choice_list.size()>0){
                    childMap.put("2",choice_list);
                    analysisChoice("choice");
                }
                if(fill_list.size()>0){
                    childMap.put("3",fill_list);
                    analysisFill("fill");

                }
                if(determine.size()>0){
                    childMap.put("4",determine);
                    analysisDetermine("determine");

                }
                if(essay.size()>0){
                    childMap.put("5",essay);
                    analysisEssay("essay");

                }
                L.d("zw--",essay.size()+"essay");
                //然后通过小题设置group的属性

                L.d("zw----",childNew.size()+"essay");
                load_fail_layout.setVisibility(View.GONE);
                if (workType==1) {
                    //数据设置完毕开始计时
                    rightText.setVisibility(View.VISIBLE);
                    limitTime=checkWorkBean.getItem().getLimit_time();
                    SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss",
                            Locale.CHINA);
                    Date date;
                    String times = null;
                        date = new Date();
                        long l = date.getTime();
                    L.d("zw--ssss",l+"LLLLL"+limitTime);
                        String stf = String.valueOf(l);
                        times = stf.substring(0, 10);
                    if(checkWorkBean.getItem().getStart_time()!=0){
                        limitTime=limitTime-(Integer.valueOf(times)-checkWorkBean.getItem().getStart_time());
                        L.d("zw--ssss",limitTime+"ttttt"+(Integer.valueOf(times)-checkWorkBean.getItem().getStart_time()));
                    }
                    if(limitTime>0){
                        counter = new Counter(TestPageActivity.this,  limitTime * 1000, 1000, new Counter.CounterTick()
                        {
                            @Override
                            public void finish()
                            {
                                // 计时完成 强制提交全部答案
                                new HttpPutBuilder(TestPageActivity.this).setClassObj(null).setUrl(isCourse?UrlsNew.TEST_FINISH:UrlsNew.TEST_FINISH_SERVICE)
                                        .setHttpResult(new HttpCallBack() {
                                            @Override
                                            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                                                dialog();
                                            }

                                            @Override
                                            public void setOnErrorCallback(int requestCode, int code, String msg) {
                                                //即使提交失败，服务端会根据限时结束考试
                                                dialog();
                                            }
                                        }).setPath(resultId).build();
                            }

                            @Override
                            public void tickTime(long day, long hour, long minute, long second)
                            {
                                rightText.setText(hour+":"+minute+":"+second);
                            }
                        });
                        counter.start();
                    }
                } else {
                    rightText.setVisibility(View.GONE);
                }

                mTabLayout.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
                initFragment();
                initAnswerSheet();
                title.setText("");
                title.setTextColor(getResources().getColor(R.color.font_black));
            }
        }


    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        jiazai_layout.setVisibility(View.GONE);
        if(code==3521){//因为已经开始过的试卷再重新请求开始答题接口会失败
            jiazai_layout.setVisibility(View.VISIBLE);
            getHttpRequstOne();
        }
    }


    private void analysisChoice(String mark) {
        List<SingleChoice>list_single;
        try {
            if(mark.equals("single_choice")){
                list_single=childMap.get("1");
            }else {
                list_single=childMap.get("2");
            }
            for (int i = 0; i < list_single.size(); i++) {
                QuestionContent questionContent = new QuestionContent();
                String questionType = mark;// 试题类型
                String questionId = list_single.get(i).getQuestion_id()+""; // 题目id
                String testId = resultId;// 试卷id
                String favorites = "0"; // 是否收藏//
                // 0是未收藏1是收藏
                questionContent.setQuestionId(questionId);
                questionContent.setTestId(testId);
                questionContent.setFavorites(favorites);

                // 创建要提交的答案对象
                CommitAnswer commitAnswer = new CommitAnswer();
                commitAnswer.setQuestionType(questionType);
                L.d("zw--ffff",questionId);
                commitAnswer.setQuestionId(questionId);

                commitAnswer.setResultId(resultId);

                commitAnswer.setTestId(testId);

                String score = list_single.get(i).getScore();
                questionContent.setScore(score);

                // 解析试题内容
                    ArrayList<HashMap<String, String>> questionList = new ArrayList<HashMap<String, String>>();
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("type_msg", "text");
                            hashMap.put("content", list_single.get(i).getStem_content()!=null?list_single.get(i).getStem_content():"");
                            questionList.add(hashMap);
                    questionContent.setQuestionList(questionList);



                    // 解析试题选项内容
                        ArrayList<QuestionChoicesContent> choicesList = new ArrayList<QuestionChoicesContent>();
                String []model={"A","B","C","D","E","F","G","H","I","J","K"};
                for (int m = 0; m < list_single.get(i).getChoices().size(); m++) {
                            QuestionChoicesContent choicesContent = new QuestionChoicesContent();
                            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                            choicesContent.setKey(model[m]);
                            choicesContent.setIsChoices("0");// 默认设置未选中
                                HashMap<String, String> hashMap1 = new HashMap<String, String>();
                                hashMap1.put("type_msg", "text");
                                hashMap1.put("content", list_single.get(i).getChoices().get(m));
                                list.add(hashMap1);
                            choicesContent.setArrayList(list);
                            choicesList.add(choicesContent);
                        }
                        questionContent.setChoicesList(choicesList);


                // 解析此题的回答内容
                if (list_single.get(i).getAnswer().size()>0) {
                    ArrayList<String> list = new ArrayList<String>();
                        for (int k = 0; k < list_single.get(i).getAnswer().size(); k++) {
                            String string_answer =list_single.get(i).getAnswer().get(k);
                            list.add(string_answer);
                        }
                    questionContent.setAnswers(list);
                } else {
                    ArrayList<String> list = new ArrayList<String>();
                    questionContent.setAnswers(list);
                }

                // 把答案添加到集合
                DownloadData.answerList.add(commitAnswer);

                // 解析我添的答案
                if (list_single.get(i).getAnswer_stu().size()>0) {
                        ArrayList<String> list = new ArrayList<String>();
                            for (int c = 0; c < list_single.get(i).getAnswer_stu().size(); c++) {
                                String string_answer = list_single.get(i).getAnswer_stu().get(c);
                                list.add(string_answer);
                            }
                        questionContent.setMyAnswers(list);

                        commitAnswer.setResultList(list);
                } else {
                    // 如果没有做过此题添加空数组
                    ArrayList<String> list = new ArrayList<String>();
                    questionContent.setMyAnswers(list);
                }

                // 解析此题的解析内容
//                if (questionObject.has("analysis")) {
//                    String analysis = questionObject.getString("analysis");
//                    if (!TextUtils.isEmpty(analysis)) {
//                        ArrayList<HashMap<String, String>> analysisList = new ArrayList<HashMap<String, String>>();
//                        JSONArray analysisArray = new JSONArray(analysis);
//                        if (analysisArray.length() > 0) {
//                            for (int d = 0; d < analysisArray.length(); d++) {
//                                HashMap<String, String> hashMap = new HashMap<String, String>();
//                                JSONObject analysisObject = analysisArray.getJSONObject(d);
//                                String definite_type = analysisObject.getString("type");
//                                String definite_content = analysisObject.getString("content");
//                                hashMap.put("type_msg", definite_type);
//                                hashMap.put("content", definite_content);
//                                analysisList.add(hashMap);
//                            }
//                        }
//                        questionContent.setAnalysisList(analysisList);
//                    } else {
//                        ArrayList<HashMap<String, String>> analysisList = new ArrayList<HashMap<String, String>>();
//                        questionContent.setAnalysisList(analysisList);
//                    }
//
//                } else {
//                    ArrayList<HashMap<String, String>> analysisList = new ArrayList<HashMap<String, String>>();
//                    questionContent.setAnalysisList(analysisList);
//                }
                ArrayList<HashMap<String, String>> analysisList = new ArrayList<HashMap<String, String>>();
                questionContent.setAnalysisList(analysisList);
                if (mark.equals("single_choice")) {
                    single_choiceList.add(questionContent);
                }
                if (mark.equals("choice")) {
                    choiceList.add(questionContent);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    private void analysisFill(String mark) {

        try {
            List<SingleChoice>list_single=childMap.get("3");
            for (int i = 0; i < list_single.size(); i++) {
                QuestionContent questionContent = new QuestionContent();
                String questionType = mark;// 试题类型
                String questionId = list_single.get(i).getQuestion_id()+""; // 题目id
                String testId = resultId;// 试卷id
                String favorites = "0"; // 是否收藏//
                // 0是未收藏1是收藏
                questionContent.setQuestionId(questionId);
                questionContent.setTestId(testId);
                questionContent.setFavorites(favorites);

                // 创建要提交的答案对象
                CommitAnswer commitAnswer = new CommitAnswer();
                commitAnswer.setQuestionType(questionType);
                commitAnswer.setQuestionId(questionId);

                commitAnswer.setResultId(resultId);

                commitAnswer.setTestId(testId);

                String score = list_single.get(i).getScore();
                questionContent.setScore(score);

                // 解析试题内容
                ArrayList<HashMap<String, String>> questionList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("type_msg", "text");
                hashMap.put("content", list_single.get(i).getStem_content()!=null?list_single.get(i).getStem_content():"");
                questionList.add(hashMap);
                questionContent.setQuestionList(questionList);


                // 解析此题的回答内容
                if (list_single.get(i).getAnswer().size()>0) {
                    ArrayList<String> list = new ArrayList<String>();
                    for (int k = 0; k < list_single.get(i).getAnswer().size(); k++) {
                        String string_answer =list_single.get(i).getAnswer().get(k);
                        list.add(string_answer);
                    }
                    questionContent.setAnswers(list);
                } else {
                    ArrayList<String> list = new ArrayList<String>();
                    questionContent.setAnswers(list);
                }

                // 把答案添加到集合
                DownloadData.answerList.add(commitAnswer);

                // 解析我添的答案
                if (list_single.get(i).getAnswer_stu().size()>0) {
                    ArrayList<String> list = new ArrayList<String>();
                    for (int c = 0; c < list_single.get(i).getAnswer_stu().size(); c++) {
                        String string_answer = list_single.get(i).getAnswer_stu().get(c);
                        list.add(string_answer);
                    }
                    questionContent.setMyAnswers(list);

                    commitAnswer.setResultList(list);
                } else {
                    // 如果没有做过此题添加空数组
                    ArrayList<String> list = new ArrayList<String>();
                    questionContent.setMyAnswers(list);
                }


                ArrayList<HashMap<String, String>> analysisList = new ArrayList<HashMap<String, String>>();
                questionContent.setAnalysisList(analysisList);
                if (mark.equals("fill")) {
                    fill_choiceList.add(questionContent);
                }            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    private void analysisEssay(String mark) {

        try {
            List<SingleChoice>list_single=childMap.get("5");
            for (int i = 0; i < list_single.size(); i++) {
                QuestionContent questionContent = new QuestionContent();
                String questionType = mark;// 试题类型
                String questionId = list_single.get(i).getQuestion_id()+""; // 题目id
                L.d("zw--essay",questionId);
                String testId = resultId;// 试卷id
                String favorites = "0"; // 是否收藏//
                // 0是未收藏1是收藏
                questionContent.setQuestionId(questionId);
                questionContent.setTestId(testId);
                questionContent.setFavorites(favorites);

                // 创建要提交的答案对象
                CommitAnswer commitAnswer = new CommitAnswer();
                commitAnswer.setQuestionType(questionType);
                commitAnswer.setQuestionId(questionId);

                commitAnswer.setResultId(resultId);

                commitAnswer.setTestId(testId);

                String score = list_single.get(i).getScore();
                questionContent.setScore(score);

                // 解析试题内容
                ArrayList<HashMap<String, String>> questionList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("type_msg", "text");
                hashMap.put("content", list_single.get(i).getStem_content()!=null?list_single.get(i).getStem_content():"");
                questionList.add(hashMap);
                questionContent.setQuestionList(questionList);


                // 解析此题的回答内容
                if (list_single.get(i).getAnswer().size()>0) {
                    ArrayList<String> list = new ArrayList<String>();
                    for (int k = 0; k < list_single.get(i).getAnswer().size(); k++) {
                        String string_answer =list_single.get(i).getAnswer().get(k);
                        list.add(string_answer);
                    }
                    questionContent.setAnswers(list);
                } else {
                    ArrayList<String> list = new ArrayList<String>();
                    questionContent.setAnswers(list);
                }

                // 把答案添加到集合
                DownloadData.answerList.add(commitAnswer);

                // 解析我添的答案
                if (list_single.get(i).getAnswer_stu().size()>0) {
                    ArrayList<String> list = new ArrayList<String>();
                    for (int c = 0; c < list_single.get(i).getAnswer_stu().size(); c++) {
                        String string_answer = list_single.get(i).getAnswer_stu().get(c);
                        list.add(string_answer);
                        L.d("zw--essay",string_answer);
                    }
                    questionContent.setMyAnswers(list);
                    L.d("zw--essay","^^^^^");
                    commitAnswer.setResultList(list);
                } else {
                    L.d("zw--essay","()()()");

                    // 如果没有做过此题添加空数组
                    ArrayList<String> list = new ArrayList<String>();
                    questionContent.setMyAnswers(list);
                }


                ArrayList<HashMap<String, String>> analysisList = new ArrayList<HashMap<String, String>>();
                questionContent.setAnalysisList(analysisList);
                if (mark.equals("essay")) {
                    essay_choiceList.add(questionContent);
                }            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    private void analysisDetermine(String mark) {

        try {
            List<SingleChoice>list_single=childMap.get("4");
            for (int i = 0; i < list_single.size(); i++) {
                QuestionContent questionContent = new QuestionContent();
                String questionType = mark;// 试题类型
                String questionId = list_single.get(i).getQuestion_id()+""; // 题目id
                String testId = resultId;// 试卷id
                String favorites = "0"; // 是否收藏//
                // 0是未收藏1是收藏
                questionContent.setQuestionId(questionId);
                questionContent.setTestId(testId);
                questionContent.setFavorites(favorites);

                // 创建要提交的答案对象
                CommitAnswer commitAnswer = new CommitAnswer();
                commitAnswer.setQuestionType(questionType);
                commitAnswer.setQuestionId(questionId);

                commitAnswer.setResultId(resultId);

                commitAnswer.setTestId(testId);

                String score = list_single.get(i).getScore();
                questionContent.setScore(score);

                // 解析试题内容
                ArrayList<HashMap<String, String>> questionList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("type_msg", "text");
                hashMap.put("content", list_single.get(i).getStem_content()!=null?list_single.get(i).getStem_content():"");
                questionList.add(hashMap);
                questionContent.setQuestionList(questionList);



                // 解析试题选项内容
                // 如果为空是判断题
                ArrayList<QuestionChoicesContent> choicesList = new ArrayList<QuestionChoicesContent>();

                QuestionChoicesContent choicesContent_right = new QuestionChoicesContent();
                ArrayList<HashMap<String, String>> list_right = new ArrayList<HashMap<String, String>>();

                choicesContent_right.setKey("对");
                HashMap<String, String> hashMap_right = new HashMap<String, String>();
                hashMap_right.put("type_msg", "text");
                hashMap_right.put("content", "对");
                list_right.add(hashMap_right);
                choicesContent_right.setArrayList(list_right);
                choicesList.add(choicesContent_right);

                QuestionChoicesContent choicesContent_error = new QuestionChoicesContent();
                ArrayList<HashMap<String, String>> list_error = new ArrayList<HashMap<String, String>>();
                choicesContent_error.setKey("错");
                HashMap<String, String> hashMap_error = new HashMap<String, String>();
                hashMap_right.put("type_msg", "text");
                hashMap_right.put("content", "错");
                list_error.add(hashMap_error);
                choicesContent_error.setArrayList(list_error);
                choicesList.add(choicesContent_error);


                // 解析此题的回答内容
                if (list_single.get(i).getAnswer().size()>0) {
                    ArrayList<String> list = new ArrayList<String>();
                    for (int k = 0; k < list_single.get(i).getAnswer().size(); k++) {
                        String string_answer =list_single.get(i).getAnswer().get(k);
                        list.add(string_answer);
                    }
                    questionContent.setAnswers(list);
                } else {
                    ArrayList<String> list = new ArrayList<String>();
                    questionContent.setAnswers(list);
                }

                // 把答案添加到集合
                DownloadData.answerList.add(commitAnswer);

                // 解析我添的答案
                if (list_single.get(i).getAnswer_stu().size()>0) {
                    ArrayList<String> list = new ArrayList<String>();
                    for (int c = 0; c < list_single.get(i).getAnswer_stu().size(); c++) {
                        String string_answer = list_single.get(i).getAnswer_stu().get(c);
                        list.add(string_answer);
                    }
                    questionContent.setMyAnswers(list);

                    commitAnswer.setResultList(list);
                } else {
                    // 如果没有做过此题添加空数组
                    ArrayList<String> list = new ArrayList<String>();
                    questionContent.setMyAnswers(list);
                }

                // 解析此题的解析内容
//                if (questionObject.has("analysis")) {
//                    String analysis = questionObject.getString("analysis");
//                    if (!TextUtils.isEmpty(analysis)) {
//                        ArrayList<HashMap<String, String>> analysisList = new ArrayList<HashMap<String, String>>();
//                        JSONArray analysisArray = new JSONArray(analysis);
//                        if (analysisArray.length() > 0) {
//                            for (int d = 0; d < analysisArray.length(); d++) {
//                                HashMap<String, String> hashMap = new HashMap<String, String>();
//                                JSONObject analysisObject = analysisArray.getJSONObject(d);
//                                String definite_type = analysisObject.getString("type");
//                                String definite_content = analysisObject.getString("content");
//                                hashMap.put("type_msg", definite_type);
//                                hashMap.put("content", definite_content);
//                                analysisList.add(hashMap);
//                            }
//                        }
//                        questionContent.setAnalysisList(analysisList);
//                    } else {
//                        ArrayList<HashMap<String, String>> analysisList = new ArrayList<HashMap<String, String>>();
//                        questionContent.setAnalysisList(analysisList);
//                    }
//
//                } else {
//                    ArrayList<HashMap<String, String>> analysisList = new ArrayList<HashMap<String, String>>();
//                    questionContent.setAnalysisList(analysisList);
//                }
                ArrayList<HashMap<String, String>> analysisList = new ArrayList<HashMap<String, String>>();
                questionContent.setAnalysisList(analysisList);
                if (mark.equals("determine")) {
                    determine_choiceList.add(questionContent);
                }            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    private SingleChoiceFragment singleChoiceFragment;
    private ChoiceFragment choiceFragment;
    private  DetemineFragment detemineFragment;
    private FillFragment fillFragment;
    private EssayFragment essayFragment;
    private void initFragment() {
        //L.d("zw--","$$$$$$$$%%%%%%%%%%"+questionTypes.size());
        fragments.clear();// 清空
        for (int i = 0; i < questionTypes.size(); i++) {

            QuestionType questionType = questionTypes.get(i);
            String questionType2 = questionType.getQuestionType();

            if (questionType2.equals("single_choice")) {
                userChannelList.add(questionType.getQuestionTitle());
                Bundle data = new Bundle();
                data.putString("resultId", resultId);
                data.putString("score", questionType.getQuestionScore()); // 传递总分
                data.putString("number", questionType.getQuestionNumber()); // 传递数量
                data.putString("showType", showType); // 传递页面展现类型
                data.putString("commitType", commitType); // 传递此试卷是考试还是作业或练习
                data.putSerializable("contents", single_choiceList); // 具体内容
                data.putString("showFinish", "0");
                data.putBoolean("isCourse", isCourse);

                 singleChoiceFragment = new SingleChoiceFragment();
                singleChoiceFragment.setArguments(data);
                fragments.add(singleChoiceFragment);
                if(single_choiceList.size()>1){
                    down_work.setVisibility(View.VISIBLE);
                }
            }
            if (questionType2.equals("choice")) {
                userChannelList.add(questionType.getQuestionTitle());
                Bundle data = new Bundle();
                data.putString("resultId", resultId);
                data.putString("score", questionType.getQuestionScore()); // 传递总分
                data.putString("number", questionType.getQuestionNumber()); // 传递数量
                data.putString("showType", showType); // 传递页面展现类型
                data.putString("commitType", commitType); // 传递此试卷是考试还是作业或练习
                data.putSerializable("contents", choiceList); // 具体内容
                data.putString("showFinish", "0");
                data.putBoolean("isCourse", isCourse);

                 choiceFragment = new ChoiceFragment();
                choiceFragment.setArguments(data);
                fragments.add(choiceFragment);
                if(choiceList.size()>1){
                    down_work.setVisibility(View.VISIBLE);
                }
            }



            // 填空题
            if (questionType2.equals("fill")) {
                userChannelList.add(questionType.getQuestionTitle());

                Bundle data = new Bundle();
                data.putString("resultId", resultId);
                data.putString("score", questionType.getQuestionScore()); // 传递总分
                data.putString("number", questionType.getQuestionNumber()); // 传递数量
                data.putString("showType", showType); // 传递页面展现类型
                data.putString("commitType", commitType); // 传递此试卷是考试还是作业或练习
                data.putSerializable("contents", fill_choiceList); // 具体内容
                data.putString("showFinish", "0");
                data.putBoolean("isCourse", isCourse);

                 fillFragment = new FillFragment();
                fillFragment.setArguments(data);
                fragments.add(fillFragment);
                if(fill_choiceList.size()>1){
                    down_work.setVisibility(View.VISIBLE);
                }
            }
            // 判断
            if (questionType2.equals("determine")) {
                userChannelList.add(questionType.getQuestionTitle());
                Bundle data = new Bundle();
                data.putString("resultId", resultId);
                data.putString("score", questionType.getQuestionScore()); // 传递总分
                data.putString("number", questionType.getQuestionNumber()); // 传递数量
                data.putString("showType", showType); // 传递页面展现类型
                data.putString("commitType", commitType); // 传递此试卷是考试还是作业或练习
                data.putSerializable("contents", determine_choiceList); // 具体内容
                data.putString("showFinish", "0");
                data.putBoolean("isCourse", isCourse);

                detemineFragment = new DetemineFragment();
                detemineFragment.setArguments(data);
                fragments.add(detemineFragment);
                if(determine_choiceList.size()>1){
                    down_work.setVisibility(View.VISIBLE);
                }
            }
            // 问答题
            if (questionType2.equals("essay")) {
                userChannelList.add(questionType.getQuestionTitle());

                Bundle data = new Bundle();
                data.putString("resultId", resultId);
                data.putString("score", questionType.getQuestionScore()); // 传递总分
                data.putString("number", questionType.getQuestionNumber()); // 传递数量
                data.putString("showType", showType); // 传递页面展现类型
                data.putString("commitType", commitType); // 传递此试卷是考试还是作业或练习
                data.putSerializable("contents", essay_choiceList); // 具体内容
                     // 传递是否显示完成考试按钮
                data.putString("showFinish", "0");
                data.putBoolean("isCourse", isCourse);

                essayFragment = new EssayFragment();
                essayFragment.setArguments(data);
                fragments.add(essayFragment);
                if(essay_choiceList.size()>1){
                    down_work.setVisibility(View.VISIBLE);
                }

            }


        }

        mViewPager.setAdapter(viewPageAdapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mTabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    beforePosition = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
// 如果是考试过程滑动时把题目答案上报服务器
                if (showType.equals("test")) {

                    if (position > beforePosition) {
                        answers.clear();
                        for (int i = 0; i < DownloadData.answerList.size(); i++) {
                            CommitAnswer commitAnswer = DownloadData.answerList.get(i);
                            String questionType = commitAnswer.getQuestionType();
                            if (questionType.equals(questionTypes.get(position - 1).getQuestionType())) {
                                answers.add(commitAnswer);
                            }
                        }
                        new CommitTestAnswer(TestPageActivity.this).commit_Answer(answers, answers.size() - 1,isCenter,isCourse);
                    }else {
                        answers.clear();
                        int type=question_type_sort.get(position+1);
                        if(type==1){
                            singleChoiceFragment.getAdapter().Commit(0,workType,false);
                        }
                        if(type==2){
                            choiceFragment.getAdapter().Commit(0,workType,false);
                        }
                        if(type==3){
                            fillFragment.getAdapter().Commit(0,workType,false);
                        }
                        if(type==4){
                            detemineFragment.getAdapter().Commit(0,workType,false);
                        }
                        if(type==5){
                            essayFragment.getAdapter().Commit(0,workType,false);
                        }
                    }

                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }

                if(position>0){
                    up_work.setVisibility(View.VISIBLE);
                    ViewPager pager=fragments.get(mViewPager.getCurrentItem()).getPager();
                    if(position==mViewPager.getAdapter().getCount()-1&&pager.getCurrentItem()==pager.getAdapter().getCount()-1){
                        down_work.setVisibility(View.GONE);
                        L.d("zw--1",mViewPager.getCurrentItem()+"mmm"+pager.getCurrentItem());
                        answer_card.setText("提交");
                    }else {
                        answer_card.setText("答题卡");
                    }
                    if(position<mViewPager.getAdapter().getCount()-1){
                        down_work.setVisibility(View.VISIBLE);
                    }
                }
                if(position==0&&fragments.get(mViewPager.getCurrentItem()).getPager().getCurrentItem()==0){
                    up_work.setVisibility(View.GONE);
                    if(mViewPager.getAdapter().getCount()>1||fragments.get(mViewPager.getCurrentItem()).getPager().getAdapter().getCount()>1){
                        down_work.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(fragments.size()==1&&questionTypes.get(0).getQuestionNumber().equals("1")){
            down_work.setVisibility(View.GONE);
        }else {
            down_work.setVisibility(View.VISIBLE);
        }
    }
    private void initAnswerSheet() {

        /**
         * 初始化答题卡信息
         */
        for (int i = 0; i < questionTypes.size(); i++) {
            AnswerSheet answerSheet = new AnswerSheet();
            answerSheet.setQuestionTitle(questionTypes.get(i).getQuestionTitle());
            String questionType = questionTypes.get(i).getQuestionType();
            answerSheet.setQuestionType(questionType);

            String questionNumber = questionTypes.get(i).getQuestionNumber();
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

            for (int j = 0; j < Integer.parseInt(questionNumber); j++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("istag", "0");// 是否标记 0代表没有标记

                QuestionContent questionContent = null;

                if (questionType.equals("single_choice")) {
                    // 获得单选题对象
                    questionContent = single_choiceList.get(j);
                    ArrayList<QuestionChoicesContent> choicesList = questionContent.getChoicesList();
                    ArrayList<String> myAnswers = questionContent.getMyAnswers();
                    if (myAnswers.size() > 0) {
                        String choicesIndex = myAnswers.get(0);// 获取我选择的是哪个选项
                        if (!TextUtils.isEmpty(choicesIndex)) {
                            if (choicesList != null) {
                                choicesList.get(Integer.parseInt(choicesIndex)).setIsChoices("1");
                            }
                        }
                    }
                } else if (questionType.equals("choice")) {
                    questionContent = choiceList.get(j);
                    ArrayList<QuestionChoicesContent> choicesList = questionContent.getChoicesList();
                    ArrayList<String> myAnswers = questionContent.getMyAnswers();

                    if (myAnswers.size() > 0) {
                        for (int p = 0; p < myAnswers.size(); p++) {
                            String choicesIndex = myAnswers.get(p);// 获取我选择的是哪个选项
                            if (!TextUtils.isEmpty(choicesIndex)) {
                                choicesList.get(Integer.parseInt(choicesIndex)).setIsChoices("1");
                            }

                        }
                    }
                } else if (questionType.equals("uncertain_choice")) {
                    questionContent = uncertain_choiceList.get(j);

                    ArrayList<QuestionChoicesContent> choicesList = questionContent.getChoicesList();
                    ArrayList<String> myAnswers = questionContent.getMyAnswers();

                    if (myAnswers.size() > 0) {
                        for (int p = 0; p < myAnswers.size(); p++) {
                            String choicesIndex = myAnswers.get(p);// 获取我选择的是哪个选项
                            if (!TextUtils.isEmpty(choicesIndex)) {
                                choicesList.get(Integer.parseInt(choicesIndex)).setIsChoices("1");
                            }
                        }
                    }
                } else if (questionType.equals("determine")) {

                    questionContent = determine_choiceList.get(j);
                    ArrayList<String> myAnswers = questionContent.getMyAnswers();
                    if (myAnswers.size() > 0) {
                        questionContent.setMyDeteminChoiceAnswer(myAnswers.get(0));
                    }

                } else if (questionType.equals("fill")) {
                    questionContent = fill_choiceList.get(j);

                } else if (questionType.equals("essay")) {
                    questionContent = essay_choiceList.get(j);
                    ArrayList<String> myAnswers = questionContent.getMyAnswers();
                    if (myAnswers.size() > 0) {
                        questionContent.setMyEssayAnswer(myAnswers.get(0));
                    }

                } else if (questionType.equals("material")) {

                }

                // 我回答此题的答案信息
                ArrayList<String> myAnswers = questionContent.getMyAnswers();
                if (myAnswers.size() == 0) {
                    hashMap.put("isdone", "0");// 是否做过 0代表没有做过
                } else {
                    hashMap.put("isdone", "1");// 是否做过 0代表没有做过
                }

                arrayList.add(hashMap);
            }

            answerSheet.setArrayList(arrayList);
            answerSheets.add(answerSheet);
        }

        DownloadData.answerSheets = answerSheets;

    }

    public class Adapter extends FragmentPagerAdapter {

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return userChannelList != null ? userChannelList.size() : 0;
        }

        /**
         * 与TabLayout进行联动，需重写此方法
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return userChannelList.get(position);
        }
    }
    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(Constants.MY_REFRESG_TESTPAGER)) {
                int pagerGroupPosition = intent.getIntExtra("pagerGroupPosition", 0);
                int pagerChildPosition = intent.getIntExtra("pagerChildPosition", 0);

                mViewPager.setCurrentItem(pagerGroupPosition);
                fragments.get(pagerGroupPosition).getArguments().putInt("pagerChildPosition", pagerChildPosition);

            }
//            if (action.equals(Constants.MY_TIME_TO_COMPLETE)) {
//
//            }
        }

    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        builder.setMessage("您的考试时间已到，已为您自动交卷！");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setResult(2);
                finish();
            }
        });
        builder.setCancelable(false);
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                diaL.dismiss();
//            }
//        });
        builder.create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1 && resultCode == 1) {
            int pagerGroupPosition = intent.getIntExtra("pagerGroupPosition", 0);
            int pagerChildPosition = intent.getIntExtra("pagerChildPosition", 0);

            mViewPager.setCurrentItem(pagerGroupPosition);
            fragments.get(pagerGroupPosition).getArguments().putInt("pagerChildPosition", pagerChildPosition);

        } else if (requestCode == 1 && resultCode == 2) {
            DownloadData.answerList.clear();
            setResult(2);
            finish();
        }else if(resultCode==Constants.LOGIN_BACK){
            getHttpRequstOne();
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            if (showType.equals(TEST_PAGE_STATUS_TEST)) {
                DownloadData.answerList.clear();
            }

            finish();
            return true;
        }

        return false;
    }
    @Override
    protected void onDestroy() {
        if (counter != null) {
            counter.cancel(); // 停止计时
        }

        unregisterReceiver(receiver);

        super.onDestroy();
    }

}
