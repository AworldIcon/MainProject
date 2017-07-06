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
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.buildwork.entity.SingleChoice;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.MyApplication;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class QuestionDetermineActivity extends BaseActivity
{
    private ImageView posters_img;
    private String courseId="";
    private String testId="";
    private String isCenter="";
    private String changPath;
    private EditText posters_text;
    private ImageView replace_tx,dele_photo,true_image,false_image,back_title_button;
    private Bitmap bm;
    private String path;
    private Uri imageUri;
    private int num=0;
    private double scoreNum=5.0;
    private LinearLayout jiazai_layout;
    private Button save_btn;
    private TextView title,essay,normal,hard,deleScore,addScore,scrore;
    private String answer="1";
    private String difficulty="1";
    private String title_name;
    private TextView picture_text;
    private RelativeLayout photo_layout;
    private boolean success=false;
    private String itemId="";
    private String imageUrl="";
    private RelativeLayout true_image_layout,false_image_layout;
    private int workType;
    private TextView notice_photo;
    private SingleChoice singleChoice;
    private String details;
    private int status;
    private RelativeLayout activity_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_determine);
        activity_layout= (RelativeLayout) findViewById(R.id.activity_question_determine);
        singleChoice= (SingleChoice) getIntent().getSerializableExtra("detail");
        courseId=getIntent().getStringExtra("courseId");
        testId=getIntent().getStringExtra("testId");
        title_name=getIntent().getStringExtra("title");
        workType=getIntent().getIntExtra("workType",2);
        status=getIntent().getIntExtra("status",2);
        details=getIntent().getStringExtra("details")!=null?getIntent().getStringExtra("details"):"self";

        isCenter="0";
        title = (TextView) findViewById(R.id.title);
        essay = (TextView) findViewById(R.id.essay);
        normal = (TextView) findViewById(R.id.normal);
        hard = (TextView) findViewById(R.id.hard);
        deleScore= (TextView)findViewById(R.id.dele_score);
        addScore= (TextView)findViewById(R.id.add_score);
        scrore= (TextView)findViewById(R.id.score);
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
        posters_img = (ImageView) findViewById(R.id.posters_img);
        posters_text = (EditText)findViewById(R.id.posters_text);
        replace_tx = (ImageView)findViewById(R.id.replace_tx);
        dele_photo = (ImageView)findViewById(R.id.dele_photo);
        picture_text = (TextView)findViewById(R.id.picture_text);
        photo_layout= (RelativeLayout)findViewById(R.id.photo_layout);
        notice_photo= (TextView) findViewById(R.id.notice_photo);
        true_image = (ImageView)findViewById(R.id.true_image);
        false_image = (ImageView)findViewById(R.id.false_image);
        true_image_layout= (RelativeLayout) findViewById(R.id.true_image_layout);
        false_image_layout= (RelativeLayout) findViewById(R.id.false_image_layout);
        back_title_button = (ImageView) findViewById(R.id.leftImage);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        save_btn= (Button) findViewById(R.id.save_btn);

        GradientDrawable myGrad = (GradientDrawable) save_btn.getBackground();
        myGrad.setColor(getResources().getColor(R.color.first_theme));// 设置内部颜色

//        pu = new PublicUtils(QuestionDetermineActivity.this);
        if (savedInstanceState != null) {
//            changPath = savedInstanceState.getString(Constants.IMAGE_FILE_PATH);
        }
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
            LinearLayout linearLayout= (LinearLayout) findViewById(R.id.bottom);
            linearLayout.setVisibility(View.GONE);
            posters_text.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            replace_tx.setClickable(false);
            dele_photo.setVisibility(View.GONE);
            save_btn.setVisibility(View.GONE);
            addScore.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_gray));
            deleScore.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_gray));

        }
        if(singleChoice!=null){
            //imageUrl=singleChoice.getImgURL();
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
//            //此处设置查看试题详情跳转过来的传值
//            imageLoader.displayImage(imageUrl,
//                    posters_img, topicSquare);
            if(!TextUtils.isEmpty(imageUrl)){
                photo_layout.setVisibility(View.VISIBLE);
                notice_photo.setVisibility(View.VISIBLE);
                picture_text.setVisibility(View.GONE);
                replace_tx.setClickable(false);
            }
            itemId=getIntent().getStringExtra("itemId");
            posters_text.setText(singleChoice.getStem_content());
            scrore.setText(singleChoice.getScore()+"分");
            scoreNum=Double.parseDouble(singleChoice.getScore());
            difficulty=singleChoice.getDiffculty();
            answer=singleChoice.getAnswer().get(0);
            if(answer.equals("1")){
                true_image.setImageResource(R.drawable.img_radio_button_checkon);
                false_image.setImageResource(R.drawable.img_radio_button_checkoff);
            }
            if(answer.equals("0")){
                true_image.setImageResource(R.drawable.img_radio_button_checkoff);
                false_image.setImageResource(R.drawable.img_radio_button_checkon);
            }
            if(difficulty.equals("1")){
                essay.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_white));
                essay.setBackgroundResource(R.drawable.choice_type);
                normal.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_black));
                normal.setBackgroundResource(R.drawable.choice_type1);
                hard.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_black));
                hard.setBackgroundResource(R.drawable.choice_type1);
            }
            if(difficulty.equals("2")){
                essay.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_black));
                essay.setBackgroundResource(R.drawable.choice_type1);
                normal.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_white));
                normal.setBackgroundResource(R.drawable.choice_type);
                hard.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_black));
                hard.setBackgroundResource(R.drawable.choice_type1);
            }
            if(difficulty.equals("3")){
                essay.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_black));
                essay.setBackgroundResource(R.drawable.choice_type1);
                normal.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_black));
                normal.setBackgroundResource(R.drawable.choice_type1);
                hard.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_white));
                hard.setBackgroundResource(R.drawable.choice_type);
            }
        }
        intEvents();
    }

    private void intEvents() {
        addScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                deleScore.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_red));
                if(scoreNum<100.0){
                    scoreNum+=0.1;
                    DecimalFormat decimalFormat=new DecimalFormat("0.0");
                    scrore.setText(decimalFormat.format(scoreNum)+"分");
                }
                if(scoreNum==100.0){
                    addScore.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_gray));
                }

            }
        });
        deleScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                addScore.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_blue));
                if(scoreNum>0.1){
                    scoreNum-=0.1;
                    DecimalFormat decimalFormat=new DecimalFormat("0.0");
                    scrore.setText(decimalFormat.format(scoreNum)+"分");                 }
                if(scoreNum==0.1){
                    deleScore.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_gray));
                }

            }
        });
        replace_tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // showDialog();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(posters_text.getText().toString())){
                    Toast.makeText(QuestionDetermineActivity.this,R.string.no_emptyContent,Toast.LENGTH_SHORT).show();
                }else {
                    if(singleChoice==null){
                        sumbit_Answer();
                    }else {
                        updateTestQuestion(getJsonData(),singleChoice.getId());
                    }                    //photo();
                }
            }
        });
        dele_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo_layout.setVisibility(View.GONE);
                // picture_text.setVisibility(View.VISIBLE);
                notice_photo.setVisibility(View.GONE);
                imageUrl="";
                replace_tx.setClickable(true);
            }
        });
        true_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                answer="1";
                true_image.setImageResource(R.drawable.img_radio_button_checkon);
                false_image.setImageResource(R.drawable.img_radio_button_checkoff);
            }
        });
        false_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    return;
                }
                answer="0";
                true_image.setImageResource(R.drawable.img_radio_button_checkoff);
                false_image.setImageResource(R.drawable.img_radio_button_checkon);
            }
        });
        back_title_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.equals("not_self")){
                    finish();
                }else {
                    dialog();
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
                essay.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_white));
                essay.setBackgroundResource(R.drawable.choice_type);
                normal.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_black));
                normal.setBackgroundResource(R.drawable.choice_type1);
                hard.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_black));
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
                essay.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_black));
                essay.setBackgroundResource(R.drawable.choice_type1);
                normal.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_white));
                normal.setBackgroundResource(R.drawable.choice_type);
                hard.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_black));
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
                essay.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_black));
                essay.setBackgroundResource(R.drawable.choice_type1);
                normal.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_black));
                normal.setBackgroundResource(R.drawable.choice_type1);
                hard.setTextColor(ContextCompat.getColor(QuestionDetermineActivity.this,R.color.font_white));
                hard.setBackgroundResource(R.drawable.choice_type);

            }
        });
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionDetermineActivity.this,R.style.custom_dialog);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString(Constants.IMAGE_FILE_PATH, changPath);
//        if(!success){
//            outState.putString("editText",posters_text.getText().toString());
//        }
    }
    public JSONObject getJsonData(){
        ArrayList<String>answer_list=new ArrayList<>();
        answer_list.add(answer.trim());
        String stem=posters_text.getText().toString();
        L.i("TAG--J",answer_list.toString());
        JSONObject jsonObject=new JSONObject();
        JSONArray option=new JSONArray();
        JSONArray answera=new JSONArray();
        try {
            jsonObject.put("type","4");
            jsonObject.put("course_id",courseId);
            jsonObject.put("score",scoreNum);
            jsonObject.put("title",stem);
            answera.put(answer.trim());
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
                    ToastUtils.makeText(QuestionDetermineActivity.this,msg);
                }
                if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    //重新登录
                    NetworkUtil.httpRestartLogin(QuestionDetermineActivity.this, activity_layout);
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
                        intent1.putExtra("poType", "determine");

                    }
                    sendBroadcast(intent1);
                }

                if(getIntent().getStringExtra("from")!=null){
                    Intent intent=new Intent(QuestionDetermineActivity.this,PublishWorkActivity.class);
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
                    ToastUtils.makeText(QuestionDetermineActivity.this,msg);
                }
            }
        })
                .setUrl(UrlsNew.URLHeader+"/test_paper/question")
                .addBodyParam(jsonObject.toString())
                .setClassObj(null)
                .build();
    }
    public void updateTestQuestion(JSONObject jsonObject,String id){
        jiazai_layout.setVisibility(View.VISIBLE);
        new HttpPutBuilder(this).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                jiazai_layout.setVisibility(View.GONE);
                if(getIntent().getStringExtra("from")==null){
                    Intent intent1=new Intent();
                    intent1.setAction("com.coder.kzxt.activity.PublishWorkActivity");
                    if(singleChoice==null) {
                        intent1.putExtra("poType", "determine");

                    }
                    sendBroadcast(intent1);
                }

                if(getIntent().getStringExtra("from")!=null){
                    Intent intent=new Intent(QuestionDetermineActivity.this,PublishWorkActivity.class);
                    intent.putExtra("courseId",courseId);
                    intent.putExtra("testId",testId);
                    intent.putExtra("title",title_name);
                    intent.putExtra("workType",workType);
                    startActivity(intent);
                }
                for(int i = 0; i< MyApplication.workList.size(); i++){
                    MyApplication.workList.get(i).finish();
                }
                finish();

            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                jiazai_layout.setVisibility(View.GONE);
                if(code==3531){
                    ToastUtils.makeText(QuestionDetermineActivity.this,msg);
                }
                if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    //重新登录
                    NetworkUtil.httpRestartLogin(QuestionDetermineActivity.this, activity_layout);
                }
            }
        })
                .setUrl(UrlsNew.URLHeader+"/test_paper/question")
                .addBodyParam(jsonObject.toString())
                .setPath(id)
                .setClassObj(null)
                .build();
    }


//    private void showPic(String path,STGVImageView imageView) {
//        if (!TextUtils.isEmpty(path)) {
//            try {
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
////    private void showDialog() {
////
////        final CustomListDialog customDialog = new CustomListDialog(QuestionDetermineActivity.this);
////        customDialog.addData(getResources().getString(R.string.take_photo), getResources().getString(R.string.photo),getResources().getString(R.string.cancel));
////        customDialog.show();
////        customDialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
////
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int ps, long id) {
////                // 判断是否挂载了SD卡
////                String storageState = Environment.getExternalStorageState();
////                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
////                    File savedir = new File(Constants.POST_ORIGINAL_PHOTO);
////                    if (!savedir.exists()) {
////                        savedir.mkdirs();
////                    }
////                } else {
////                    PublicUtils.showToast(getApplicationContext(), getResources().getString(R.string.take_photo_no_card), 0);
////                    return;
////                }
////
////                if (ps == 0) {
////                    photo();
////                } else if (ps == 1) {
////                    startActionPickCrop();
////                }else if (ps == 2) {
////
////                }
////                customDialog.cancel();
////            }
////        });
////    }
////    /**
////     * 照相
////     */
//    public void photo() {
//        String status = Environment.getExternalStorageState();
//        if (status.equals(Environment.MEDIA_MOUNTED)) {
//            File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/zw/HBSI_post_img/");
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            File file = new File(dir, String.valueOf(System.currentTimeMillis()) + ".jpg");
//            changPath = file.getPath();
//            imageUri = Uri.fromFile(file);
//            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            //boolean intentAvailable = pu.isIntentAvailable(QuestionDetermineActivity.this, openCameraIntent);
//            if (true) {
//                startActivityForResult(openCameraIntent, 0);
//            } else {
//                Toast.makeText(QuestionDetermineActivity.this, getResources().getString(R.string.take_photo_error), Toast.LENGTH_SHORT).show();
//            }
//
//        } else {
//            Toast.makeText(QuestionDetermineActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
//        }
//    }
////
////    /**
////     * 选择图片
////     */
////    private void startActionPickCrop() {
////        String status = Environment.getExternalStorageState();
////        if (status.equals(Environment.MEDIA_MOUNTED)) {
////            Intent intent = new Intent(Intent.ACTION_PICK, null);
////            intent.setType("image/*");
////            startActivityForResult(intent, Constants.ALBUM_PICTURE);
////        } else {
////            Toast.makeText(QuestionDetermineActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
////        }
////
////    }
////
////    /**
////     * 调用系统图片编辑进行裁剪
////     */
////    public void startPhotoCrop(Uri uri) {
////
////        String status = Environment.getExternalStorageState();
////        if (status.equals(Environment.MEDIA_MOUNTED)) {
////            File dir = new File(Constants.POST_ORIGINAL_PHOTO);
////            if (!dir.exists()) {
////                dir.mkdirs();
////            }
////
////            Intent intent = new Intent("com.android.camera.action.CROP");
////            intent.setDataAndType(uri, "image/*");
////
////            File file = new File(dir, String.valueOf(System.currentTimeMillis()) + ".jpg");
////            path = file.getPath();
////            Uri imageUri = Uri.fromFile(file);
////
////            intent.putExtra("crop", "true");
////            intent.putExtra("scale", true);
////            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
////            intent.putExtra("return-data", false);
////            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
////            intent.putExtra("noFaceDetection", true); // no face detection
////            startActivityForResult(intent, Constants.CROP_PICTURE);
////        } else {
////            Toast.makeText(QuestionDetermineActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
////        }
////    }
////    public void onActivityResult(int requestCode, int resultCode, Intent data) {
////        if (requestCode == 1 && resultCode == 1) {
////        } else if (requestCode == Constants.TAKE_PICTURE) {
////            File file = new File(changPath);
////            if (file.exists()) {
////                startPhotoCrop(imageUri);
////                //showPic(changPath);
////            }
////
////        } else if (requestCode == Constants.ALBUM_PICTURE) {
////            if (data != null) {
////                Uri selectedImage = data.getData(); // 获取系统返回的照片的Uri
////                startPhotoCrop(selectedImage);
////            }
////        } else if (requestCode == Constants.CROP_PICTURE) {
////            File file = new File(path);
////            if (file.exists()) {
////                // num++;
////                // 更改图片
////                if (!TextUtils.isEmpty(path)) {
////                    if(num==0){
////                        showPic(path,posters_img);
//////                        replace_tx.setVisibility(View.GONE);
////                        //只允许上传一张图片
////                        replace_tx.setClickable(false);
////                    }
////
////                }
////
////            }
////        }
////        super.onActivityResult(requestCode, resultCode, data);
////    }
//
////    @Override
////    public boolean dispatchKeyEvent(KeyEvent event) {
////        switch (event.getKeyCode()) {
////            case KeyEvent.KEYCODE_DPAD_CENTER:// 屏蔽editext输入的回车键
////            case KeyEvent.KEYCODE_ENTER:
////                return true;
////        }
////        return super.dispatchKeyEvent(event);
////    }

    @Override
    protected void onDestroy() {
        if (bm != null && !bm.isRecycled()) {
            bm.recycle(); // 回收图片所占的内存
            System.gc(); // 提醒系统及时回收
        }
        super.onDestroy();
    }

//    public void sumbit_Answer() {
//        String newStr="";
//        if(!TextUtils.isEmpty(path)){
//            newStr= path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
//        }
//        File file = new File(Constants.POST_PHOTO + newStr + ".JPEG");
//        ArrayList<String> list=new ArrayList<>();
//        list.add(answer);
//        Log.i("code--u","list-"+list.toString());
//        RequestParams params = new RequestParams();
//        params.put("mid", pu.getUid() + "");
//        params.put("oauth_token", pu.getOauth_token());
//        params.put("oauth_token_secret", pu.getOauth_token_secret());
//        params.put("answer",list.toString());
//        params.put("stem",posters_text.getText().toString());
//        params.put("type","determine");
//        params.put("difficulty",difficulty);
//        params.put("score",scoreNum+"");
//        params.put("itemId",itemId);
//        params.put("target","course-"+courseId);
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
//        HttpUtil.post(Constants.BASE_URL + "ManuallyAddQuestionAction?" + "deviceId=" + pu.getImeiNum(), params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(String jsonStr) {
//                super.onSuccess(jsonStr);
//                jiazai_layout.setVisibility(View.GONE);
//                String msg,code,photoUrl="";
//                try {
//                    String jsonStr_deco = Decrypt_Utils.decode(Constants.HTTP_KEY, jsonStr);
//                    JSONObject jsonObject = new JSONObject(jsonStr_deco);
//                    msg = jsonObject.getString("msg");
//                    code = jsonObject.getString("code");
//                    photoUrl = jsonObject.getJSONObject("data").getString("photoUrl");
//                    Log.i("code--", code + "**msg--" + msg + "&uid--" + pu.getUid()+"^^"+"url"+photoUrl);
//                    Toast.makeText(QuestionDetermineActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    if(code.equals("1000")){
//                        success=true;
//                        if(getIntent().getStringExtra("from")==null){
//                            Intent intent1 = new Intent();
//                            intent1.setAction("com.coder.kzxt.activity.PublishWorkActivity");
//                            if(singleChoice==null) {
//                                intent1.putExtra("poType", "determine");
//                            }
//                            sendBroadcast(intent1);
//
//                        }
//                        if(getIntent().getStringExtra("from")!=null){
//                            Intent intent=new Intent(QuestionDetermineActivity.this,PublishWorkActivity.class);
//                            intent.putExtra("courseId",courseId);
//                            intent.putExtra("testId",testId);
//                            intent.putExtra("title",title_name);
//                            intent.putExtra("workType",workType);
//                            intent.putExtra(Constants.IS_CENTER,isCenter);
//                            startActivity(intent);
//                        }
//                        for(int i=0;i< MyApplication.list.size();i++){
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
//                Toast.makeText(QuestionDetermineActivity.this, arg1, Toast.LENGTH_SHORT).show();
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
