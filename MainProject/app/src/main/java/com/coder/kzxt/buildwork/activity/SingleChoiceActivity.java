package com.coder.kzxt.buildwork.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.buildwork.entity.SingleChoice;
import com.coder.kzxt.buildwork.views.ContainsEmojiEditText;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.MyApplication;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SingleChoiceActivity extends BaseActivity {
    private String path;
    private String changPath;
    private String courseId="";
    private String testId="";
    private String isCenter="";
    private String choice="";
    private ImageView leftImage,replace_tx,dele_photo;
    private TextView title;
    private TextView rightText;
    private ImageView posters_img;
//    private STGVImageView posters_img;
    private TextView addMore,scrore,addScore,deleScore;
    private ContainsEmojiEditText posters_text;
    private TextView cu_number_con;
    private ArrayList<ArrayList<HashMap<String, String>>> classifyList;// 分类数据
    private ArrayList<HashMap<String, String>> modeList;
    private Uri imageUri;
    private LinearLayout jiazai_layout;
    private Button load_fail_button;
    private LinearLayout load_fail_layout;
    private String cid = "0";
    private Dialog dialog;
    private Bitmap bm;
    private int num=0;
    private double scoreNum=5.0;
    private TextView essay,normal,hard;
    private ListView listView;
    private ArrayList<String>list;
    private ArrayList<Boolean>list1;
    private MyAdapter myAdapter;
    private Button save_btn;
    private String difficulty="1";
    private String title_name;
    private TextView picture_text;
    private RelativeLayout photo_layout;
    private String itemId="";
    private String imageUrl="";
    private int workType;
    private TextView notice_photo;
    private SingleChoice singleChoice;
    private String details,question_id;
    private int status;
    private RelativeLayout activity_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_choice);
        activity_layout= (RelativeLayout) findViewById(R.id.activity_single_choice);
        singleChoice= (SingleChoice) getIntent().getSerializableExtra("detail");
        courseId=getIntent().getStringExtra("courseId");
        testId=getIntent().getStringExtra("testId");
        choice=getIntent().getStringExtra("choice");
        workType=getIntent().getIntExtra("workType",2);
        status=getIntent().getIntExtra("status",2);
        title_name=getIntent().getStringExtra("title");
        question_id=getIntent().getStringExtra("question_id");
        details=getIntent().getStringExtra("details")!=null?getIntent().getStringExtra("details"):"self";
        isCenter="0";
        if (savedInstanceState != null) {
//            changPath = savedInstanceState.getString(Constants.IMAGE_FILE_PATH);
        }
        classifyList = new ArrayList<ArrayList<HashMap<String, String>>>();
        modeList = new ArrayList<HashMap<String, String>>();
        listView= (ListView) findViewById(R.id.list);
        leftImage = (ImageView) findViewById(R.id.leftImage);
        leftImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    finish();
                }else {
                    dialog();
                }

            }
        });
        title = (TextView) findViewById(R.id.title);
        if(workType==1){
            title.setText(title_name+"-考试");
        }else {
            title.setText(title_name+"-作业");

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
        rightText.setText("");
        View headView= LayoutInflater.from(this).inflate(R.layout.single_choice_head,null);
        View footView=LayoutInflater.from(this).inflate(R.layout.single_choice_foot,null);
        TextView choice_type= (TextView) headView.findViewById(R.id.choice_type);
        if(choice.equals("choice")){
            choice_type.setText("单选题");
        }else {
            choice_type.setText("多选题");
        }
        listView.addHeaderView(headView);
        listView.addFooterView(footView);
        addMore= (TextView) footView.findViewById(R.id.add_choice);
        deleScore= (TextView) footView.findViewById(R.id.dele_score);
        addScore= (TextView) footView.findViewById(R.id.add_score);
        scrore= (TextView) footView.findViewById(R.id.score);
        essay = (TextView) footView.findViewById(R.id.essay);
        normal = (TextView) footView.findViewById(R.id.normal);
        hard = (TextView) footView.findViewById(R.id.hard);
        save_btn= (Button) footView.findViewById(R.id.save_btn);

        GradientDrawable myGrad = (GradientDrawable) save_btn.getBackground();
        myGrad.setColor(getResources().getColor(R.color.first_theme));// 设置内部颜色

        posters_img = (ImageView) headView.findViewById(R.id.posters_img);
        posters_text = (ContainsEmojiEditText) headView.findViewById(R.id.posters_text);
        cu_number_con = (TextView)headView. findViewById(R.id.cu_number_con);
        replace_tx = (ImageView)headView.findViewById(R.id.replace_tx);
        dele_photo = (ImageView)headView.findViewById(R.id.dele_photo);
        picture_text = (TextView)headView. findViewById(R.id.picture_text);
        photo_layout= (RelativeLayout) headView.findViewById(R.id.photo_layout);
        notice_photo= (TextView) headView.findViewById(R.id.notice_photo);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        list=new ArrayList<>();
        list1=new ArrayList<>();
        //初始化选项
        for(int i=0;i<4;i++){
            list.add("");
            list1.add(false);
        }
        //初始化查看试题详情跳转来的选项数据,需要区分detail类型是否是试卷创建者，如果为空则是由创建试题方式进入
        if(singleChoice!=null){
            imageUrl=singleChoice.getImgURL();
            imageUrl="";
//            imageLoader= ImageLoader.getInstance();
//            DisplayImageOptions topicSquare = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.photo_default)
//                    .showImageForEmptyUri(R.drawable.photo_default)
//                    .showImageOnFail(R.drawable.photo_default)
//                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                    .cacheInMemory(false) // 加载图片时会在内存中加载缓存
//                    .cacheOnDisk(true)// 加载图片时会在磁盘中加载缓存
//                    .considerExifParams(true)// 启用EXIF和JPEG图像格式
//                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            //此处设置查看试题详情跳转过来的传值
            if(!TextUtils.isEmpty(imageUrl)){
//                imageLoader.displayImage(imageUrl,
//                        posters_img, topicSquare);
                photo_layout.setVisibility(View.VISIBLE);
                picture_text.setVisibility(View.GONE);
                notice_photo.setVisibility(View.VISIBLE);
                replace_tx.setClickable(false);
            }

            itemId=getIntent().getStringExtra("itemId");
            posters_text.setText(singleChoice.getStem_content());
            scrore.setText(singleChoice.getScore()+"分");
            scoreNum=Double.valueOf(singleChoice.getScore());
            difficulty=singleChoice.getDiffculty();
            if(difficulty.equals("1")){
                essay.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_white));
                essay.setBackgroundResource(R.drawable.choice_type);
                normal.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_black));
                normal.setBackgroundResource(R.drawable.choice_type1);
                hard.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_black));
                hard.setBackgroundResource(R.drawable.choice_type1);
            }
            if(difficulty.equals("2")){
                essay.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_black));
                essay.setBackgroundResource(R.drawable.choice_type1);
                normal.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_white));
                normal.setBackgroundResource(R.drawable.choice_type);
                hard.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_black));
                hard.setBackgroundResource(R.drawable.choice_type1);
            }
            if(difficulty.equals("3")){
                essay.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_black));
                essay.setBackgroundResource(R.drawable.choice_type1);
                normal.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_black));
                normal.setBackgroundResource(R.drawable.choice_type1);
                hard.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_white));
                hard.setBackgroundResource(R.drawable.choice_type);
            }
            //清空重新赋值
            list.clear();
            list1.clear();
            for(int i=0;i<singleChoice.getChoices().size();i++){
                list.add(singleChoice.getChoices().get(i));
                list1.add(false);
            }
            for(int j=0;j<singleChoice.getAnswer().size();j++){
                list1.set(Integer.parseInt(singleChoice.getAnswer().get(j).trim()),true);
            }
            Log.i("TAG--TAG1",list.toString()+"answer"+list1.toString());
        }
        myAdapter=new MyAdapter();
        listView.setAdapter(myAdapter);

        posters_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(details.equals("not_self")){
                    return true;
                }
                if ((v.getId() == R.id.posters_text && canVerticalScroll(posters_text))) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });
        posters_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 500) {
                    cu_number_con.setText(s.length() + "");
                    cu_number_con.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_gray));
                } else {
                    cu_number_con.setText(s.length() + "");
                    cu_number_con.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_red));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        replace_tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                //showDialog();
            }
        });
        if(details.equals("not_self")){
            if(workType==1){
                if(status==3){
                    ToastUtils.makeText(this,"该试卷已经发布，无法编辑",Toast.LENGTH_LONG).show();
                }else {
                    ToastUtils.makeText(this,"该试卷非本人创建，无法编辑",Toast.LENGTH_LONG).show();
                }
            }else {
                if(status==3){
                    ToastUtils.makeText(this,"该作业已经发布，无法编辑",Toast.LENGTH_LONG).show();
                }else {
                    ToastUtils.makeText(this,"该作业非本人创建，无法编辑",Toast.LENGTH_LONG).show();
                }
            }

            footView.setVisibility(View.GONE);
            dele_photo.setVisibility(View.GONE);
        }
        dele_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo_layout.setVisibility(View.GONE);
                notice_photo.setVisibility(View.GONE);
                // picture_text.setVisibility(View.VISIBLE);
                imageUrl="";
                replace_tx.setClickable(true);
            }
        });
        if(details.equals("not_self")){
            save_btn.setVisibility(View.GONE);
        }
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(posters_text.getText().toString())){
                    Toast.makeText(SingleChoiceActivity.this,R.string.no_emptyContent,Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i=0;i<list.size();i++){
                    if(TextUtils.isEmpty(list.get(i))){
                        Toast.makeText(SingleChoiceActivity.this,R.string.no_emptyChoice,Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                ArrayList<String>answer_list=new ArrayList<>();
                for(int i=0;i<list1.size();i++){
                    if(list1.get(i)){
                        answer_list.add(i+"");
                    }
                }
                if(answer_list.size()==0){
                    Toast.makeText(SingleChoiceActivity.this,"请选择正确项",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(choice.equals("choices")&&answer_list.size()<2){
                    Toast.makeText(SingleChoiceActivity.this,"至少两个正确项",Toast.LENGTH_SHORT).show();
                    return;
                }
                jiazai_layout.setVisibility(View.VISIBLE);
                Log.i("TAG--TAG",list.toString()+"answer"+list1.toString());
                if(singleChoice==null){
                    sumbit_Answers();
                }else {
                    updateTestQuestion(getJsonData(),singleChoice.getId());
                }
            }
        });


        if(details.equals("not_self")){
            addMore.setVisibility(View.GONE);
        }
        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.size()<7){
                    list.add("");
                    list1.add(false);
                    myAdapter.notifyDataSetChanged();
                    listView.setSelection(listView.getAdapter().getCount()-1);
                }
            }
        });
        if(details.equals("not_self")){
            addScore.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_gray));
        }
        addScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                deleScore.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_red));
                if(scoreNum<100.0){
                    scoreNum+=0.1;
                    DecimalFormat decimalFormat=new DecimalFormat("0.0");
                    scrore.setText(decimalFormat.format(scoreNum)+"分");
                }
                if(scoreNum==100.0){
                    addScore.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_gray));
                }

            }
        });
        if(details.equals("not_self")){
            deleScore.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_gray));
        }
        deleScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                addScore.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_blue));
                if(scoreNum>1){
                    scoreNum-=0.1;
                    DecimalFormat decimalFormat=new DecimalFormat("0.0");
                    scrore.setText(decimalFormat.format(scoreNum)+"分");                }
                if(scoreNum==1.0){
                    deleScore.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_gray));
                }

            }
        });
        //new PostersConfigurationTask().executeOnExecutor(Constants.exec);
        if(details.equals("not_self")){}
        essay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                difficulty="1";
                essay.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_white));
                essay.setBackgroundResource(R.drawable.choice_type);
                normal.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_black));
                normal.setBackgroundResource(R.drawable.choice_type1);
                hard.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_black));
                hard.setBackgroundResource(R.drawable.choice_type1);
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                difficulty="2";
                essay.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_black));
                essay.setBackgroundResource(R.drawable.choice_type1);
                normal.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_white));
                normal.setBackgroundResource(R.drawable.choice_type);
                hard.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_black));
                hard.setBackgroundResource(R.drawable.choice_type1);
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                difficulty="3";
                essay.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_black));
                essay.setBackgroundResource(R.drawable.choice_type1);
                normal.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_black));
                normal.setBackgroundResource(R.drawable.choice_type1);
                hard.setTextColor(ContextCompat.getColor(SingleChoiceActivity.this,R.color.font_white));
                hard.setBackgroundResource(R.drawable.choice_type);

            }
        });
    }
    public JSONObject getJsonData(){
        ArrayList<String>answer_list=new ArrayList<>();
        for(int i=0;i<list1.size();i++){
            if(list1.get(i)){
                answer_list.add((i+"").trim());
            }
        }
        String stem=posters_text.getText().toString();
        L.i("TAG--J",answer_list.toString());
         JSONObject jsonObject=new JSONObject();
        JSONArray option=new JSONArray();
        JSONArray answer=new JSONArray();
        try {
            if(choice.equals("choice")){
                jsonObject.put("type","1");
            }
            if(choice.equals("choices")){
                jsonObject.put("type","2");
            }
            jsonObject.put("course_id",courseId);
            jsonObject.put("score",scoreNum);
            jsonObject.put("title",stem);
            for(int i=0;i<list.size();i++){
                option.put(list.get(i).trim());
            }
            for(int i=0;i<answer_list.size();i++){
                answer.put(answer_list.get(i).trim());
            }
            jsonObject.put("option",option);
            jsonObject.put("answer",answer);
            jsonObject.put("difficulty",difficulty);
            jsonObject.put("analysis","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    private void sumbit_Answers() {
        jiazai_layout.setVisibility(View.VISIBLE);
        Log.d("zw--code0",getJsonData().toString());

        new HttpPostBuilder(this).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                String question_id="";
                Log.d("zw--code",resultBean.toString());
                try {
                    question_id= new JSONObject(resultBean.toString()).getJSONObject("item").optString("id","");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addTestQuestion(getJsonData(),question_id);
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                jiazai_layout.setVisibility(View.GONE);
                if(code==3531){
                    ToastUtils.makeText(SingleChoiceActivity.this,msg);
                }
                if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    //重新登录
                    NetworkUtil.httpRestartLogin(SingleChoiceActivity.this, activity_layout);
                }
                Log.d("zw--code1",msg);
            }
        })
                .setUrl(UrlsNew.URLHeader+"/test/question")
                .addBodyParam(getJsonData().toString())
                .setClassObj(null)
                .build();
    }

    public void addTestQuestion(JSONObject jsonObject,String id){
        jsonObject.remove("course_id");
        try {
            jsonObject.put("question_id",id);
            jsonObject.put("test_paper_id",testId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpPostBuilder(this).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                jiazai_layout.setVisibility(View.GONE);
                    if(getIntent().getStringExtra("from")==null){
                        Intent intent1=new Intent();
                        intent1.setAction("com.coder.kzxt.activity.PublishWorkActivity");
                        if(singleChoice==null) {
                            if (choice.equals("choice")) {
                                intent1.putExtra("poType", "single_choice");
                            } else {
                                intent1.putExtra("poType", "choice");
                            }
                        }
                        sendBroadcast(intent1);
                    }

                    if(getIntent().getStringExtra("from")!=null){
                        Intent intent=new Intent(SingleChoiceActivity.this,PublishWorkActivity.class);
                        intent.putExtra("courseId",courseId);
                        intent.putExtra("testId",testId);
                        intent.putExtra("title",title_name);
                        intent.putExtra("workType",workType);
                        intent.putExtra(Constants.IS_CENTER,isCenter);
                        startActivity(intent);
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
                jiazai_layout.setVisibility(View.GONE);
                if(code==3531){
                    ToastUtils.makeText(SingleChoiceActivity.this,msg);
                }
            }
        })
                .setUrl(UrlsNew.URLHeader+"/test_paper/question")
                .addBodyParam(jsonObject.toString())
                .setClassObj(null)
                .build();
    }
    public void updateTestQuestion(JSONObject jsonObject,String id){
        new HttpPutBuilder(this).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                jiazai_layout.setVisibility(View.GONE);
                    if(getIntent().getStringExtra("from")==null){
                        Intent intent1=new Intent();
                        intent1.setAction("com.coder.kzxt.activity.PublishWorkActivity");
                        if(singleChoice==null) {
                            if (choice.equals("choice")) {
                                intent1.putExtra("poType", "single_choice");
                            } else {
                                intent1.putExtra("poType", "choice");
                            }
                        }
                        sendBroadcast(intent1);
                    }

                    if(getIntent().getStringExtra("from")!=null){
                        Intent intent=new Intent(SingleChoiceActivity.this,PublishWorkActivity.class);
                        intent.putExtra("courseId",courseId);
                        intent.putExtra("testId",testId);
                        intent.putExtra("title",title_name);
                        intent.putExtra("workType",workType);
                        intent.putExtra(Constants.IS_CENTER,isCenter);
                        startActivity(intent);
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
                jiazai_layout.setVisibility(View.GONE);
                if(code==3531){
                    ToastUtils.makeText(SingleChoiceActivity.this,msg);
                }
                if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    //重新登录
                    NetworkUtil.httpRestartLogin(SingleChoiceActivity.this, activity_layout);
                }
            }
        })
                .setUrl(UrlsNew.URLHeader+"/test_paper/question")
                .addBodyParam(jsonObject.toString())
                .setPath(id)
                .setClassObj(null)
                .build();
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SingleChoiceActivity.this,R.style.custom_dialog);
        builder.setMessage(R.string.exit_build_work);
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString(Constants.IMAGE_FILE_PATH, changPath);
    }


//    private void showPic(String path,ImageView imageView) {
//        if (!TextUtils.isEmpty(path)) {
//            try {
//                imageView.setVisibility(View.VISIBLE);
//                FileInputStream f = new FileInputStream(path);
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = false;
//                options.inSampleSize = 2;
//                BufferedInputStream bis = new BufferedInputStream(f);
//                bm = BitmapFactory.decodeStream(bis, null, options);
//                int height = bm.getHeight();
//                int width = bm.getWidth();
//                if (height > width) {
//                    imageView.mHeight = 1;
//                    imageView.mWidth = 1;
//                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//                } else {
//                    imageView.mHeight = height;
//                    imageView.mWidth = width;
//                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//
//                }
//                imageView.setImageBitmap(bm);
//                imageUrl="";
//                photo_layout.setVisibility(View.VISIBLE);
//                notice_photo.setVisibility(View.VISIBLE);
//                picture_text.setVisibility(View.GONE);
//                String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
//                // 保存图片到sd卡
//                SdcardUtils.saveBitmap(bm, "" + newStr);
//
//                f.close();
//                bis.close();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    /**
//     * 显示对话框
//     */
//    private void showDialog() {
//
//        final CustomListDialog customDialog = new CustomListDialog(SingleChoiceActivity.this);
//        customDialog.addData(getResources().getString(R.string.take_photo), getResources().getString(R.string.photo),getResources().getString(R.string.cancel));
//        customDialog.show();
//        customDialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int ps, long id) {
//                // 判断是否挂载了SD卡
//                String storageState = Environment.getExternalStorageState();
//                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
//                    File savedir = new File(Constants.POST_ORIGINAL_PHOTO);
//                    if (!savedir.exists()) {
//                        savedir.mkdirs();
//                    }
//                } else {
//                    PublicUtils.showToast(getApplicationContext(), getResources().getString(R.string.take_photo_no_card), 0);
//                    return;
//                }
//
//                if (ps == 0) {
//                    photo();
//                } else if (ps == 1) {
//                    startActionPickCrop();
//                }else if (ps == 2) {
//
//                }
//                customDialog.cancel();
//            }
//        });
//    }
//
//
//
//
//
//    /**
//     * 照相
//     */
//    public void photo() {
//        String status = Environment.getExternalStorageState();
//        if (status.equals(Environment.MEDIA_MOUNTED)) {
//            File dir = new File(Constants.POST_ORIGINAL_PHOTO);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            File file = new File(dir, String.valueOf(System.currentTimeMillis()) + ".jpg");
//            changPath = file.getPath();
//            imageUri = Uri.fromFile(file);
//            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            boolean intentAvailable = pu.isIntentAvailable(SingleChoiceActivity.this, openCameraIntent);
//            if (intentAvailable) {
//                startActivityForResult(openCameraIntent, Constants.TAKE_PICTURE);
//            } else {
//                Toast.makeText(SingleChoiceActivity.this, getResources().getString(R.string.take_photo_error), Toast.LENGTH_SHORT).show();
//            }
//
//        } else {
//            Toast.makeText(SingleChoiceActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
//        }
//    }
//
//    /**
//     * 选择图片
//     */
//    private void startActionPickCrop() {
//        String status = Environment.getExternalStorageState();
//        if (status.equals(Environment.MEDIA_MOUNTED)) {
//            Intent intent = new Intent(Intent.ACTION_PICK, null);
//            intent.setType("image/*");
//            startActivityForResult(intent, Constants.ALBUM_PICTURE);
//        } else {
//            Toast.makeText(SingleChoiceActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//    /**
//     * 调用系统图片编辑进行裁剪
//     */
//    public void startPhotoCrop(Uri uri) {
//
//        String status = Environment.getExternalStorageState();
//        if (status.equals(Environment.MEDIA_MOUNTED)) {
//            File dir = new File(Constants.POST_ORIGINAL_PHOTO);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            Intent intent = new Intent("com.android.camera.action.CROP");
//            intent.setDataAndType(uri, "image/*");
//
//            File file = new File(dir, String.valueOf(System.currentTimeMillis()) + ".jpg");
//            path = file.getPath();
//            Uri imageUri = Uri.fromFile(file);
//
//            intent.putExtra("crop", "true");
//            intent.putExtra("scale", true);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            intent.putExtra("return-data", false);
//            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//            intent.putExtra("noFaceDetection", true); // no face detection
//            startActivityForResult(intent, Constants.CROP_PICTURE);
//        } else {
//            Toast.makeText(SingleChoiceActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1 && resultCode == 1) {
//        } else if (requestCode == Constants.TAKE_PICTURE) {
//            File file = new File(changPath);
//            if (file.exists()) {
//                startPhotoCrop(imageUri);
//                //showPic(changPath);
//            }
//
//        } else if (requestCode == Constants.ALBUM_PICTURE) {
//            if (data != null) {
//                Uri selectedImage = data.getData(); // 获取系统返回的照片的Uri
//                startPhotoCrop(selectedImage);
//            }
//        } else if (requestCode == Constants.CROP_PICTURE) {
//            File file = new File(path);
//            if (file.exists()) {
//                // num++;
//                // 更改图片
//                if (!TextUtils.isEmpty(path)) {
//                    if(num==0){
//                        showPic(path,posters_img);
//                        replace_tx.setClickable(false);
//                    }
////                    if(num==2){
////                        showPic(path,posters_img1);
////                        replace_tx.setVisibility(View.GONE);
////                    }
//
//                }
//
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        switch (event.getKeyCode()) {
//            case KeyEvent.KEYCODE_DPAD_CENTER:// 屏蔽editext输入的回车键
//            case KeyEvent.KEYCODE_ENTER:
//                return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }

    @Override
    protected void onDestroy() {
        if (bm != null && !bm.isRecycled()) {
            bm.recycle(); // 回收图片所占的内存
            System.gc(); // 提醒系统及时回收
        }
        super.onDestroy();
    }
    private LinearLayout.LayoutParams fillParentLayoutParams = new LinearLayout.LayoutParams(

            LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    /**
     * EditText竖直方向是否可以滚动
     *  @param editText 需要判断的EditText
     *  @return true：可以滚动  false：不可以滚动
     * */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        // 控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();    //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;
        if(scrollDifference == 0) {      return false;    }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
    private int index = -1;
    public class MyAdapter extends BaseAdapter {
        String[]choiceName=new String[]{"A","B","C","D","E","F","G","H","I","J"};
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
            if(convertView == null) {
                convertView = new LinearLayout(parent.getContext());
            }
            else{
                // 因为项目中每一行的控件究竟有什么都不确定，所以清掉layout里的所有控件，你的项目视情况而定。
                ((LinearLayout) convertView).removeAllViews();

            }
            // 不要直接new一个Layout去赋值给convertView！！那样就不是重用了，否则，后果自负～～
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_item,null);
            final ContainsEmojiEditText editText = (ContainsEmojiEditText) view.findViewById(R.id.edit);
            final CheckBox checkBox= (CheckBox) view.findViewById(R.id.text);
            if(details.equals("not_self")){
                editText.setFocusable(false);
                editText.setClickable(false);
                checkBox.setClickable(false);
                checkBox.setFocusable(false);
                checkBox.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            }
            checkBox.setChecked(list1.get(position));
            checkBox.setText(choiceName[position]);
            if(list1.get(position)){
                checkBox.setTextColor(getResources().getColor(R.color.font_white));
            }else {
                checkBox.setTextColor(getResources().getColor(R.color.font_black));
            }
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkBox.isChecked()){
                        checkBox.setTextColor(getResources().getColor(R.color.font_white));
                        list1.set(position,true);
                        if(choice.equals("choice")){
                            for(int i=0;i<list1.size();i++){
                                if(i!=position){
                                    list1.set(i,false);
                                }
                                notifyDataSetChanged();
                            }
                        }

                    }else {
                        checkBox.setTextColor(getResources().getColor(R.color.font_black));
                        list1.set(position,false);
                    }
                }
            });
            TextView textView1= (TextView) view.findViewById(R.id.text1);
            if(getCount()<=2){
                textView1.setVisibility(View.INVISIBLE);
            }
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getCount()>2){
                        list.remove(position);
                        list1.remove(position);
                        notifyDataSetChanged();
                    }

                }
            });
            if(details.equals("not_self")){
                textView1.setVisibility(View.GONE);
            }
            // 你可以试试把addView放到这个函数的return之前，我保证你会后悔的～～
            // 因为addView的先后对画面的结果是有影响的。
            ((LinearLayout) convertView).addView(view, fillParentLayoutParams);
            editText.setText(list.get(position));
            editText.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent event) {
                    if(details.equals("not_self")){
                        return true;
                    }
                    // 在TOUCH的UP事件中，要保存当前的行下标，因为弹出软键盘后，整个画面会被重画
                    // 在getView方法的最后，要根据index和当前的行下标手动为EditText设置焦点
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        index= position;

                    }
                    return false;
                }
            });
            editText.addTextChangedListener(new TextWatcher(){

                public void afterTextChanged(Editable editable) {
                }
                public void beforeTextChanged(CharSequence text, int start, int count, int after) {

                }
                public void onTextChanged(CharSequence text, int start, int before, int count) {
                    //  notifyDataSetChanged();????????????
                    // 在这个地方添加你的保存文本内容的代码，如果不保存，你就等着重新输入吧
                    list.set(position,text.toString());
                    // 而且不管你输入多少次，也不会有用的，因为getView全清了～～

                }

            });
            // 这个地方可以添加将保存的文本内容设置到EditText上的代码，会有用的～～
            editText.clearFocus();
            if(index!= -1 && index == position) {
                // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
                editText.requestFocus();

            }
            // 这个时候返回的东东，就是ListView对应行下标的那一行的内容。

            return convertView;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        myAdapter.notifyDataSetChanged();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i("TAG--J","1");
            //返回事件
            if(!details.equals("not_self")){
                dialog();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

//    public void sumbit_Answers() {
//        String newStr="";
//        if(!TextUtils.isEmpty(path)){
//            newStr= path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
//        }
//        File file = new File(Constants.POST_PHOTO + newStr + ".JPEG");
//
//        final ArrayList<String>answer_list=new ArrayList<>();
//        for(int i=0;i<list1.size();i++){
//            if(list1.get(i)){
//                answer_list.add((i+"").trim());
//            }
//        }
//        Log.i("TAG--J",answer_list.toString());
//        RequestParams params = new RequestParams();
//        params.put("mid", pu.getUid() + "");
//        params.put("oauth_token", pu.getOauth_token());
//        params.put("oauth_token_secret", pu.getOauth_token_secret());
//        params.put("answer",answer_list.toString());
//        params.put("stem",posters_text.getText().toString());
//        params.put("type","choice");
//        params.put("choices",list.toString());
//        params.put("difficulty",difficulty);
//        params.put("score",scoreNum+"");
//        params.put("target","course-"+courseId);
//        params.put("itemId",itemId);
//        params.put(Constants.IS_CENTER,isCenter);
//        params.put("testId",testId);
//        if(!TextUtils.isEmpty(imageUrl)){
//            params.put("photo",imageUrl);
//        }else {
//            if (file.exists()) {
//                Log.i("code--u","uuu");
//                try {
//                    params.put("photo", file);
//                    Log.i("code--u","u");
//                } catch (FileNotFoundException e) {
//                    Log.i("code--u","uu8u");
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        HttpUtil.post(Constants.BASE_URL + "ManuallyAddQuestionAction?" + "deviceId=" + pu.getImeiNum(), params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(String jsonStr) {
//                super.onSuccess(jsonStr);
//                jiazai_layout.setVisibility(View.GONE);
//                String msg,code;
//                try {
//                    String jsonStr_deco = Decrypt_Utils.decode(Constants.HTTP_KEY, jsonStr);
//                    JSONObject jsonObject = new JSONObject(jsonStr_deco);
//                    msg = jsonObject.getString("msg");
//                    code = jsonObject.getString("code");
//                    Log.i("code--", code + "**msg--" + msg + "&uid--" + pu.getUid()+"^^"+"testId" +"answer"+answer_list.toString());
//                    Toast.makeText(SingleChoiceActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    if(code.equals("1000")){
//                        if(getIntent().getStringExtra("from")==null){
//                            Intent intent1=new Intent();
//                            intent1.setAction("com.coder.kzxt.activity.PublishWorkActivity");
//                            if(singleChoice==null) {
//                                if (choice.equals("choice")) {
//                                    intent1.putExtra("poType", "single_choice");
//                                } else {
//                                    intent1.putExtra("poType", "choice");
//                                }
//                            }
//                            sendBroadcast(intent1);
//                        }
//
//                        if(getIntent().getStringExtra("from")!=null){
//                            Intent intent=new Intent(SingleChoiceActivity.this,PublishWorkActivity.class);
//                            intent.putExtra("courseId",courseId);
//                            intent.putExtra("testId",testId);
//                            intent.putExtra("title",title_name);
//                            intent.putExtra("workType",workType);
//                            intent.putExtra(Constants.IS_CENTER,isCenter);
//                            startActivity(intent);
//                        }
//                        for(int i = 0; i< MyApplication.list.size(); i++){
//                            MyApplication.list.get(i).finish();
//                        }
//                        finish();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable arg0, String arg1) {
//                super.onFailure(arg0, arg1);
//                jiazai_layout.setVisibility(View.GONE);
//                Log.i("code--","failure--"+arg1);
//                Toast.makeText(SingleChoiceActivity.this, arg1, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
       // StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
       // StatService.onPause(this);
    }

}
