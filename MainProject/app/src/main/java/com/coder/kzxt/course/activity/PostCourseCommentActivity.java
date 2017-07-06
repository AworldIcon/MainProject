package com.coder.kzxt.course.activity;

import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.TextUitl;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;


public class PostCourseCommentActivity extends BaseActivity implements HttpCallBack {
    private SharedPreferencesUtil spu;
    private String treeId;
    private Toolbar toolbar;
    private RatingBar post_rat;
    private EditText post_et;
    private CheckBox check;
    private TextView cu_number_tv;
    private Button post_bt;
    private String is_anon = "0";
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_course_comment);
        spu = new SharedPreferencesUtil(this);
        treeId = getIntent().getStringExtra("treeId");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("写评论");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        post_rat = (RatingBar) findViewById(R.id.post_rat);
        post_et = (EditText) findViewById(R.id.post_et);
        check = (CheckBox) findViewById(R.id.check);
        cu_number_tv = (TextView) findViewById(R.id.cu_number_tv);
        post_bt = (Button) findViewById(R.id.post_bt);
        GradientDrawable myGrad = (GradientDrawable) post_bt.getBackground();
        myGrad.setColor(getResources().getColor(R.color.first_theme));// 设置内部颜色
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    is_anon= "1";
                }else {
                    is_anon= "0";
                }
            }
        });

        post_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 200) {
                    cu_number_tv.setText(s.length() + "/");
                    cu_number_tv.setTextColor(getResources().getColor(R.color.font_gray));
                } else {
                    cu_number_tv.setText(s.length() + "/");
                    cu_number_tv.setTextColor(getResources().getColor(R.color.font_red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        post_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(post_et.getText().toString().trim())){
                    ToastUtils.makeText(getApplicationContext(),getResources().getString(R.string.input_comment_content_hint), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(post_rat.getRating()==0){
                    ToastUtils.makeText(getApplicationContext(),getResources().getString(R.string.not_rat), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUitl.containsEmoji(post_et.getText().toString())) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_support_emoji), Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog = MyPublicDialog.createLoadingDialog(PostCourseCommentActivity.this);
                dialog.show();
                postRequest();

            }
        });

    }

    private void postRequest(){
        new HttpPostBuilder(this)
                .setClassObj(BaseBean.class)
                .setHttpResult(this)
                .setUrl(UrlsNew.POST_COURSE_REVIEW)
                .addBodyParam("content",post_et.getText().toString())
                .addBodyParam("score",String.valueOf(post_rat.getRating()).substring(0,1))
                .addBodyParam("is_anon",is_anon)
                .addBodyParam("course_id",treeId)
                .addBodyParam("user_id",spu.getUid())
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(dialog!=null&&dialog.isShowing()){
                    dialog.cancel();
                }

                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if(dialog!=null&&dialog.isShowing()){
            dialog.cancel();
        }
                setResult(1);
                finish();
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        if(dialog!=null&&dialog.isShowing()){
            dialog.cancel();
        }
        ToastUtils.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

    }
}
