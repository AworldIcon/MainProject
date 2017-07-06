package com.coder.kzxt.buildwork.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.buildwork.entity.QuestionBank;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.MyApplication;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class QuestionBankActivity extends BaseActivity implements  HttpCallBack {
    private RelativeLayout layout_left,layout_right;
    private RelativeLayout activity_question_bank;
    private String courseId,isCenter,testId;
    private String type="";
    private String difficulty="";
    private ListView questionList;
    private List<QuestionBank.ItemsBean> list=new ArrayList<>();
    private List<QuestionBank.ItemsBean> beanList=new ArrayList<>();
    private ArrayList<String>ids;
    private QuestionPopAdapter popAdapter;
    private TextView text_simple,text_normal,text_diffcult,text_all,type_text,diffcult_text,rightText,title;
    private View rightPopView;
    private Button load_fail_button;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private TextView no_info_text;
    private ImageView jiantou_image,jiantou_image1,jiantou_imag,jiantou_imag1;
    private ImageView leftImage;
    private int workType;
    private String title_name="";
    private HashMap<String,Boolean> hashMap;
    private PopupWindow pop1;
    private PopupWindow pop2;
    private List<String> rightList;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private WorkQuestionBankDelegate bankDelegate;
    private PullRefreshAdapter pullRefreshAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank);
        courseId=getIntent().getStringExtra("courseId");
        testId=getIntent().getStringExtra("testId");
        title_name=getIntent().getStringExtra("title");
        workType=getIntent().getIntExtra("workType",workType);
        isCenter=getIntent().getStringExtra(Constants.IS_CENTER);
        isCenter="0";
        hashMap=new HashMap<>();
        list = new ArrayList<>();
        ids=new ArrayList<>();
        initview();
        clickEvents();
    }



    private void initview() {
        activity_question_bank= (RelativeLayout) findViewById(R.id.activity_question_bank);
        leftImage= (ImageView) findViewById(R.id.leftImage);
        title = (TextView) findViewById(R.id.title);
        if(workType==1){
            title.setText(title_name+"-题库");
        }else {
            title.setText(title_name+"-题库");
        }
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                100, getResources().getDisplayMetrics());
        title.setWidth(px);
        title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        title.setMarqueeRepeatLimit(100000);
        title.setFocusable(true);
        title.setSingleLine(true);
        title.setHorizontallyScrolling(true);
        title.setFocusableInTouchMode(true);
        rightText = (TextView) findViewById(R.id.rightText);
        rightText.setText("添加");
        rightText.setVisibility(View.VISIBLE);

        bankDelegate=new WorkQuestionBankDelegate();
        pullRefreshAdapter = new PullRefreshAdapter(this, beanList,  bankDelegate);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);

        layout_left= (RelativeLayout) findViewById(R.id.work_type);
        layout_right= (RelativeLayout) findViewById(R.id.diffcult_type);
        jiantou_image= (ImageView) findViewById(R.id.jiantou_image);
        jiantou_imag= (ImageView) findViewById(R.id.jiantou_imag);
        jiantou_image1= (ImageView) findViewById(R.id.jiantou_image1);
        jiantou_imag1= (ImageView) findViewById(R.id.jiantou_imag1);
        diffcult_text= (TextView) findViewById(R.id.diffcult_text);
        type_text= (TextView) findViewById(R.id.type_text);
        search_jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        myPullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);

        questionBankData();
        rightList=new ArrayList<>();
        rightList.add("全部");
        rightList.add("单选题");
        rightList.add("多选题");
        rightList.add("判断题");
        rightList.add("填空题");
        rightList.add("问答题");
        popAdapter=new QuestionPopAdapter();
    }
    private int point=0;
    private int ringhtPoint=0;
    private void clickEvents() {
        leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ids.size()==0){
                    Toast.makeText(QuestionBankActivity.this,R.string.choose_work,Toast.LENGTH_SHORT).show();
                }else {
                    sumbit_ids();
                }
            }
        });
        layout_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pop1!=null){
                    pop1.dismiss();
                    pop1=null;
                }
                if(pop2!=null){
                    pop2.dismiss();
                    pop2=null;
                }else {
                    typePopWindow();
                }

                // }
            }
        });
        layout_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pop2!=null){
                    pop2.dismiss();
                    pop2=null;
                }
                if(pop1!=null){
                    pop1.dismiss();
                    pop1=null;
                }else{
                    diffcultyPopWindow();
                }

            }

        });
//
        load_fail_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNetFailGone();
                pullRefreshAdapter.resetPageIndex();
                questionBankData();
            }
        });
    myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            pullRefreshAdapter.resetPageIndex();
            questionBankData();
        }
    });
        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                pullRefreshAdapter.addPageIndex();
                questionBankData();
            }
        });
    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionBankActivity.this,R.style.custom_dialog);
        builder.setMessage(R.string.exit_no_add_work);
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

    private void diffcultyPopWindow() {
        diffcult_text.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_blue));
        jiantou_image1.setImageResource(R.drawable.up_jianou);
        jiantou_imag1.setVisibility(View.VISIBLE);
        pop1=new PopupWindow(QuestionBankActivity.this);
        if(pop1!=null){
            pop1.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    diffcult_text.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
                    jiantou_image1.setImageResource(R.drawable.time_down);
                    jiantou_imag1.setVisibility(View.GONE);
                }
            });
        }
        View view= LayoutInflater.from(QuestionBankActivity.this).inflate(R.layout.question_type_pop,null);
        text_simple= (TextView) view.findViewById(R.id.simple);
        text_normal= (TextView) view.findViewById(R.id.normal);
        text_diffcult= (TextView) view.findViewById(R.id.hard);
        text_all= (TextView) view.findViewById(R.id.all);
        if(point==1){
            text_simple.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_blue));
            text_normal.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
            text_diffcult.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
            text_all.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
        }else if(point==2){
            text_simple.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
            text_normal.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_blue));
            text_diffcult.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
            text_all.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
        }else if(point==3){
            text_simple.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
            text_normal.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
            text_diffcult.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_blue));
            text_all.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
        }else if(point==0){
            text_simple.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
            text_normal.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
            text_diffcult.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
            text_all.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_blue));
        }
        pop1.setContentView(view);
        pop1.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop1.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop1.setBackgroundDrawable(getResources().getDrawable(R.color.trans_half));
        pop1.setFocusable(false);
        pop1.setOutsideTouchable(false);
        //pop1.setAnimationStyle(R.style.mypopwindow_anim_style);
        view.setAnimation(AnimationUtils.loadAnimation(QuestionBankActivity.this,R.anim.push_top_in));
        pop1.showAsDropDown(findViewById(R.id.title_type),0,0);
        //pop1.showAtLocation(findViewById(R.id.title_type), Gravity.BOTTOM, 0, 0);
        text_simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point=1;
                difficulty="1";
                pop1.dismiss();
                pullRefreshAdapter.resetPageIndex();
                questionBankData();
            }
        });
        text_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty="2";
                point=2;
                pop1.dismiss();
                pullRefreshAdapter.resetPageIndex();
                questionBankData();
            }
        });
        text_diffcult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point=3;
                difficulty="3";
                pop1.dismiss();
                pullRefreshAdapter.resetPageIndex();
                questionBankData();
            }
        });
        text_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point=0;
                difficulty="";
                pop1.dismiss();
                pullRefreshAdapter.resetPageIndex();
                questionBankData();
            }
        });

    }

    private void typePopWindow() {
        type_text.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_blue));
        jiantou_image.setImageResource(R.drawable.up_jianou);
        jiantou_imag.setVisibility(View.VISIBLE);
        pop2=new PopupWindow(QuestionBankActivity.this);
        if(pop2!=null){
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    type_text.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_black));
                    jiantou_image.setImageResource(R.drawable.time_down);
                    jiantou_imag.setVisibility(View.GONE);
                }
            });
        }
        rightPopView=LayoutInflater.from(QuestionBankActivity.this).inflate(R.layout.question_diff_pop,null);
        pop2.setContentView(rightPopView);
        pop2.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop2.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop2.setBackgroundDrawable(getResources().getDrawable(R.color.trans_half));
        pop2.setFocusable(false);
        pop2.setOutsideTouchable(false);

        rightPopView.setAnimation(AnimationUtils.loadAnimation(QuestionBankActivity.this,R.anim.push_top_in));
        pop2.showAsDropDown(QuestionBankActivity.this.findViewById(R.id.title_type),0,0);
        questionList= (ListView) rightPopView.findViewById(R.id.pop_question_list);
        questionList.setAdapter(popAdapter);
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ringhtPoint=position;
                String[]typeArr=new String[]{"","1","2","4","3","5"};
                type=typeArr[position];
                pop2.dismiss();
                pullRefreshAdapter.resetPageIndex();
                questionBankData();

            }
        });


    }
    private void questionBankData(){
        new HttpGetBuilder(this).setClassObj(QuestionBank.class).setUrl(UrlsNew.URLHeader+"/test/question")
                .setHttpResult(this)
                .addQueryParams("course_id",courseId)
                .addQueryParams("type",type)
                .addQueryParams("difficulty",difficulty)
                .addQueryParams("orderBy", "create_time desc")
                .addQueryParams("page",pullRefreshAdapter.getPageIndex()+"")
                .addQueryParams("pageSize","20")
                .build();
    }
    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        QuestionBank questionBank= (QuestionBank) resultBean;
//        list.clear();
        list.addAll(questionBank.getItems());
        for(int i=0;i<list.size();i++){
            if(!hashMap.containsKey(list.get(i).getId()+"")){
                hashMap.put(list.get(i).getId()+"",false);
            }
        }
        pullRefreshAdapter.setTotalPage(questionBank.getPaginate().getPageNum());
        pullRefreshAdapter.setPullData(questionBank.getItems());
        if (list.size() != 0) {
            no_info_layout.setVisibility(View.GONE);
        } else {
            no_info_layout.setVisibility(View.VISIBLE);
        }
        setNetFailGone();
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        setNetFailVisible();
        if(code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
            //重新登录
            NetworkUtil.httpRestartLogin(QuestionBankActivity.this,activity_question_bank);
        }else {
            //加载失败
            load_fail_layout.setVisibility(View.VISIBLE);
            NetworkUtil.httpNetErrTip(QuestionBankActivity.this,activity_question_bank);
        }
    }

    private void setNetFailGone() {
        search_jiazai_layout.setVisibility(View.GONE);
        myPullRecyclerView.setVisibility(View.VISIBLE);
        load_fail_layout.setVisibility(View.GONE);
    }
    private void setNetFailVisible() {
        search_jiazai_layout.setVisibility(View.GONE);
        myPullRecyclerView.setVisibility(View.GONE);
    }



    private class MyAdapter extends BaseAdapter {
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
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.question_bank_list_item,null);

            return view;
        }
    }
    public class WorkQuestionBankDelegate extends PullRefreshDelegate<QuestionBank.ItemsBean> {
        public WorkQuestionBankDelegate() {
            super(R.layout.question_bank_list_item);
        }

        @Override
        public void initCustomView(BaseViewHolder holder, final List<QuestionBank.ItemsBean> data, final int position) {
            TextView title= (TextView) holder.findViewById(R.id.title);
            TextView teacher_name= (TextView) holder.findViewById(R.id.teacher_name);
            TextView time_creat= (TextView) holder.findViewById(R.id.time_creat);
            final ImageView choose_image= (ImageView) holder.findViewById(R.id.choose_image);
            ImageView choos_image= (ImageView) holder.findViewById(R.id.choos_image);
            title.setText(data.get(position).getTitle());
            teacher_name.setText("创建老师: "+data.get(position).getCreator().getNickname());
            SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
            Long i = data.get(position).getCreate_time();
            String times = sdr.format(new Date(i * 1000L));
            time_creat.setText("创建时间: "+times);
            choos_image.setVisibility(View.GONE);
            choose_image.setVisibility(View.VISIBLE);

            if(hashMap.get(data.get(position).getId()+"")){
                choose_image.setImageResource(R.drawable.img_radio_button_checkon);
            }else {
                choose_image.setImageResource(R.drawable.img_radio_button_checkoff);
            }
            choose_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(hashMap.get(data.get(position).getId()+"")){
                        hashMap.put(data.get(position).getId()+"",false);
                        ids.remove(data.get(position).getId()+"");
                        choose_image.setImageResource(R.drawable.img_radio_button_checkoff);
                    }else {
                        hashMap.put(data.get(position).getId()+"",true);
                        choose_image.setImageResource(R.drawable.img_radio_button_checkon);
                        ids.add(data.get(position).getId()+"");
                    }
                }
            });
            TextView one_score = (TextView) holder.findViewById(R.id.one_score);
            TextView diffculty_type = (TextView) holder.findViewById(R.id.diffculty_type);
            String work_type=data.get(position).getType()+"";
            String type_diffcult=data.get(position).getDifficulty()+"";
            if(work_type.equals("1")){
                one_score.setText("单选");
            }
            if(work_type.equals("2")){
                one_score.setText("多选");
            }
            if(work_type.equals("4")){
                one_score.setText("判断");
            }
            if(work_type.equals("3")){
                one_score.setText("填空");
            }
            if(work_type.equals("5")){
                one_score.setText("问答");
            }
            if(type_diffcult.equals("1")){
                diffculty_type.setText("简单");
                diffculty_type.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_green));
                diffculty_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.simple_type_green));
            }
            if(type_diffcult.equals("2")){
                diffculty_type.setText("一般");
                diffculty_type.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_blue));
                diffculty_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_type_blue));
            }
            if(type_diffcult.equals("3")){
                diffculty_type.setText("困难");
                diffculty_type.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_red));
                diffculty_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.diffcult_type_red));
            }
        }
    }
    private class QuestionPopAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return rightList.size();
        }

        @Override
        public Object getItem(int position) {
            return rightList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.question_diff_item,null);
            TextView text= (TextView) view.findViewById(R.id.question_diff);
            text.setText(rightList.get(position));
            if(position==ringhtPoint){
                text.setTextColor(ContextCompat.getColor(QuestionBankActivity.this,R.color.font_blue));
            }
            return view;
        }
    }
    public void sumbit_ids() {
     search_jiazai_layout.setVisibility(View.VISIBLE);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("test_paper_id",testId);
            jsonObject.put("question_ids",new JSONArray(ids.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpPostBuilder(this).setClassObj(null).setUrl(UrlsNew.URLHeader+"/test_paper/question/batch/")
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        search_jiazai_layout.setVisibility(View.GONE);

                        if(getIntent().getStringExtra("from")!=null) {
                            Intent intent = new Intent(QuestionBankActivity.this, PublishWorkActivity.class);
                            intent.putExtra("courseId", courseId);
                            intent.putExtra("testId", testId);
                            intent.putExtra("title", title_name);
                            intent.putExtra("workType", workType);
                            intent.putExtra("status", 2);
                            startActivity(intent);
                        }else {
                            Intent intent1=new Intent();
                            intent1.setAction("com.coder.kzxt.activity.PublishWorkActivity");
                            sendBroadcast(intent1);
                        }
                        for(int i = 0; i< MyApplication.workList.size(); i++){
                            MyApplication.workList.get(i).finish();
                            if(i<MyApplication.workList.size()){
                                MyApplication.workList.remove(i);
                            }
                        }
                        finish();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        search_jiazai_layout.setVisibility(View.GONE);
                        if(code==3531){
                            ToastUtils.makeText(QuestionBankActivity.this,msg);
                        }
                        if(code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
                            //重新登录
                            NetworkUtil.httpRestartLogin(QuestionBankActivity.this,activity_question_bank);
                        }
                    }
                })
                .addBodyParam(jsonObject.toString())
                .build();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //返回事件
            dialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        //StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        //StatService.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Constants.LOGIN_BACK){
            questionBankData();
        }
    }
}
