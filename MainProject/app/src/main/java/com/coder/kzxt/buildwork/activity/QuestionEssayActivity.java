package com.coder.kzxt.buildwork.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.buildwork.entity.SingleChoice;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.MyApplication;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;

public class QuestionEssayActivity extends BaseActivity
{
    private ImageView posters_img;
    private ImageView posters_img1;
    private String courseId="";
    private String testId="";
    private String isCenter="";
    private String changPath;
    private String changPath1;
    private EditText posters_text;
    private EditText posters_text1;
    private ImageView replace_tx,dele_photo;
    private ImageView replace_tx1,dele_photo1;
    private Bitmap bm;
    private String path;
    private String path1;
    private Uri imageUri;
    private Uri imageUri1;
    private int num=0;
    private double scoreNum=5.0;
    private LinearLayout jiazai_layout;
    private Button save_btn;
    private int type=0;
    private TextView deleScore,addScore,scrore;
    private TextView essay,normal,hard;
    private String difficulty="1";
    private ImageView leftImage;
    private String title_name;
    private TextView title;
    private TextView picture_text;
    private RelativeLayout photo_layout;
    private TextView picture_text1;
    private RelativeLayout photo_layout1;
    private String itemId="";
    private String imageUrl="";
    private String answerUrl="";
    private int workType;
    private TextView notice_photo;
    private TextView notice_photo1;
    private SingleChoice singleChoice;
    private String details;
    private int status;
    private RelativeLayout activity_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_essay);
        activity_layout= (RelativeLayout) findViewById(R.id.activity_question_essay);
        singleChoice= (SingleChoice) getIntent().getSerializableExtra("detail");
        courseId=getIntent().getStringExtra("courseId");
        testId=getIntent().getStringExtra("testId");
        title_name=getIntent().getStringExtra("title");
        workType=getIntent().getIntExtra("workType",2);
        status=getIntent().getIntExtra("status",2);
        details=getIntent().getStringExtra("details")!=null?getIntent().getStringExtra("details"):"self";

        isCenter="0";
        posters_img = (ImageView) findViewById(R.id.posters_img);
        title= (TextView) findViewById(R.id.title);
        if(workType==1){
            title.setText(title_name+"-作业");
        }else {
            title.setText(title_name+"-考试");
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
        posters_text = (EditText)findViewById(R.id.posters_text);
        replace_tx = (ImageView)findViewById(R.id.replace_tx);
        dele_photo = (ImageView)findViewById(R.id.dele_photo);
        picture_text = (TextView)findViewById(R.id.picture_text);
        photo_layout= (RelativeLayout)findViewById(R.id.photo_layout);
        notice_photo= (TextView) findViewById(R.id.notice_photo);
        posters_img1 = (ImageView) findViewById(R.id.posters_img1);
        posters_text1 = (EditText)findViewById(R.id.posters_text1);
        replace_tx1 = (ImageView)findViewById(R.id.replace_tx1);
        dele_photo1 = (ImageView)findViewById(R.id.dele_photo1);
        picture_text1 = (TextView)findViewById(R.id.picture_text1);
        photo_layout1= (RelativeLayout)findViewById(R.id.photo_layout1);
        notice_photo1= (TextView) findViewById(R.id.notice_photo1);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        save_btn= (Button) findViewById(R.id.save_btn);

        GradientDrawable myGrad = (GradientDrawable) save_btn.getBackground();
        myGrad.setColor(getResources().getColor(R.color.first_theme));// 设置内部颜色

        deleScore= (TextView)findViewById(R.id.dele_score);
        addScore= (TextView)findViewById(R.id.add_score);
        scrore= (TextView)findViewById(R.id.score);
        leftImage= (ImageView) findViewById(R.id.leftImage);
        essay = (TextView) findViewById(R.id.essay);
        normal = (TextView)findViewById(R.id.normal);
        hard = (TextView)findViewById(R.id.hard);
//        pu = new PublicUtils(QuestionEssayActivity.this);
//        if (savedInstanceState != null) {
//            changPath = savedInstanceState.getString(Constants.IMAGE_FILE_PATH);
//        }
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
            posters_text.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            posters_text1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            LinearLayout foot= (LinearLayout) findViewById(R.id.foot);
            foot.setVisibility(View.GONE);
            dele_photo.setVisibility(View.GONE);
            dele_photo1.setVisibility(View.GONE);
            save_btn.setVisibility(View.GONE);
            addScore.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_gray));
            deleScore.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_gray));
        }else {
            posters_text.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if ((v.getId() == R.id.posters_text && canVerticalScroll(posters_text))) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                    return false;
                }
            });
            posters_text1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if ((v.getId() == R.id.posters_text1 && canVerticalScroll(posters_text1))) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                    return false;
                }
            });
        }
        if(singleChoice!=null){
            imageUrl=singleChoice.getImgURL();
            imageUrl="";
            answerUrl=singleChoice.getAnswerUrl();
            answerUrl="";
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
//            //此处设置查看试题详情跳转过来的传值
//            imageLoader.displayImage(imageUrl,
//                    posters_img, topicSquare);
//            imageLoader.displayImage(answerUrl,
//                    posters_img1, topicSquare);
            if(!TextUtils.isEmpty(imageUrl)){
                photo_layout.setVisibility(View.VISIBLE);
                notice_photo.setVisibility(View.VISIBLE);
                picture_text.setVisibility(View.GONE);
                replace_tx.setClickable(false);
            }
            if(!TextUtils.isEmpty(answerUrl)){
                photo_layout1.setVisibility(View.VISIBLE);
                notice_photo1.setVisibility(View.VISIBLE);
                picture_text1.setVisibility(View.GONE);
                replace_tx1.setClickable(false);
            }
            itemId=getIntent().getStringExtra("itemId");
            posters_text.setText(singleChoice.getStem_content());
            posters_text1.setText(singleChoice.getAnswer().get(0));
            scrore.setText(singleChoice.getScore()+"分");
            scoreNum=Double.parseDouble(singleChoice.getScore());
            difficulty=singleChoice.getDiffculty();
            if(difficulty.equals("1")){
                essay.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_white));
                essay.setBackgroundResource(R.drawable.choice_type);
                normal.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_black));
                normal.setBackgroundResource(R.drawable.choice_type1);
                hard.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_black));
                hard.setBackgroundResource(R.drawable.choice_type1);
            }
            if(difficulty.equals("2")){
                essay.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_black));
                essay.setBackgroundResource(R.drawable.choice_type1);
                normal.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_white));
                normal.setBackgroundResource(R.drawable.choice_type);
                hard.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_black));
                hard.setBackgroundResource(R.drawable.choice_type1);
            }
            if(difficulty.equals("3")){
                essay.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_black));
                essay.setBackgroundResource(R.drawable.choice_type1);
                normal.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_black));
                normal.setBackgroundResource(R.drawable.choice_type1);
                hard.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_white));
                hard.setBackgroundResource(R.drawable.choice_type);
            }
        }



        replace_tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                num=1;
              //  showDialog();
            }
        });
        replace_tx1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                num=2;
               // showDialog();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jiazai_layout.setVisibility(View.VISIBLE);
                Log.i("path-path1",path+"*"+path1);
                if(TextUtils.isEmpty(posters_text.getText().toString())||TextUtils.isEmpty(posters_text1.getText().toString())){
                    Toast.makeText(QuestionEssayActivity.this,R.string.no_emptyContent,Toast.LENGTH_SHORT).show();
                }else {
                    jiazai_layout.setVisibility(View.VISIBLE);
                    if(singleChoice==null){
                        sumbit_Answer();
                    }else {
                        updateTestQuestion(getJsonData(),singleChoice.getId());
                    }
                }
            }
        });
        dele_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                //picture_text.setVisibility(View.VISIBLE);
                photo_layout.setVisibility(View.GONE);
                notice_photo.setVisibility(View.GONE);
                replace_tx.setClickable(true);
                imageUrl="";
            }
        });
        dele_photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                //picture_text1.setVisibility(View.VISIBLE);
                photo_layout1.setVisibility(View.GONE);
                notice_photo1.setVisibility(View.GONE);
                replace_tx1.setClickable(true);
                answerUrl="";
            }
        });
        addScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                deleScore.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_red));
                if(scoreNum<100.0){
                    scoreNum+=0.1;
                    DecimalFormat decimalFormat=new DecimalFormat("0.0");
                    scrore.setText(decimalFormat.format(scoreNum)+"分");                  }
                if(scoreNum==100.0){
                    addScore.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_gray));
                }

            }
        });
        deleScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                addScore.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_blue));
                if(scoreNum>0.1){
                    scoreNum-=0.1;
                    DecimalFormat decimalFormat=new DecimalFormat("0.0");
                    scrore.setText(decimalFormat.format(scoreNum)+"分");                  }
                if(scoreNum==0.1){
                    deleScore.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_gray));
                }

            }
        });
        essay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                difficulty="1";
                essay.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_white));
                essay.setBackgroundResource(R.drawable.choice_type);
                normal.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_black));
                normal.setBackgroundResource(R.drawable.choice_type1);
                hard.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_black));
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
                essay.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_black));
                essay.setBackgroundResource(R.drawable.choice_type1);
                normal.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_white));
                normal.setBackgroundResource(R.drawable.choice_type);
                hard.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_black));
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
                essay.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_black));
                essay.setBackgroundResource(R.drawable.choice_type1);
                normal.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_black));
                normal.setBackgroundResource(R.drawable.choice_type1);
                hard.setTextColor(ContextCompat.getColor(QuestionEssayActivity.this,R.color.font_white));
                hard.setBackgroundResource(R.drawable.choice_type);

            }
        });
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
    }
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i("TAG--J","1");
            //返回事件
            if(!details.equals("not_self")){
                dialog();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionEssayActivity.this,R.style.custom_dialog);
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
    protected void onDestroy() {
        if (bm != null && !bm.isRecycled()) {
            bm.recycle(); // 回收图片所占的内存
            System.gc(); // 提醒系统及时回收
        }
        super.onDestroy();
    }
    public JSONObject getJsonData(){
        String stem=posters_text.getText().toString();
        JSONObject jsonObject=new JSONObject();
        JSONArray option=new JSONArray();
        JSONArray answera=new JSONArray();
        try {
            jsonObject.put("type","5");
            jsonObject.put("course_id",courseId);
            jsonObject.put("score",scoreNum);
            jsonObject.put("title",stem);
            answera.put(posters_text1.getText().toString().trim());
            jsonObject.put("answer",answera);
            jsonObject.put("difficulty",difficulty);
            jsonObject.put("analysis","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    private void sumbit_Answer() {
        jiazai_layout.setVisibility(View.VISIBLE);
        new HttpPostBuilder(this).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                String question_id="";
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
                    ToastUtils.makeText(QuestionEssayActivity.this,msg);
                }
                if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    //重新登录
                    NetworkUtil.httpRestartLogin(QuestionEssayActivity.this, activity_layout);
                }
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
                        intent1.putExtra("poType", "essay");

                    }
                    sendBroadcast(intent1);
                }

                if(getIntent().getStringExtra("from")!=null){
                    Intent intent=new Intent(QuestionEssayActivity.this,PublishWorkActivity.class);
                    intent.putExtra("courseId",courseId);
                    intent.putExtra("testId",testId);
                    intent.putExtra("title",title_name);
                    intent.putExtra("workType",workType);
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
                    ToastUtils.makeText(QuestionEssayActivity.this,msg);
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
                        intent1.putExtra("poType", "essay");
                    }
                    sendBroadcast(intent1);
                }

                if(getIntent().getStringExtra("from")!=null){
                    Intent intent=new Intent(QuestionEssayActivity.this,PublishWorkActivity.class);
                    intent.putExtra("courseId",courseId);
                    intent.putExtra("testId",testId);
                    intent.putExtra("title",title_name);
                    intent.putExtra("workType",workType);
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
                    ToastUtils.makeText(QuestionEssayActivity.this,msg);
                }
                if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    //重新登录
                    NetworkUtil.httpRestartLogin(QuestionEssayActivity.this, activity_layout);
                }
            }
        })
                .setUrl(UrlsNew.URLHeader+"/test_paper/question")
                .addBodyParam(jsonObject.toString())
                .setPath(id)
                .setClassObj(null)
                .build();
    }

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
        //StatService.onPause(this);
    }
}
