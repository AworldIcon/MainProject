package com.coder.kzxt.classe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.beans.ClassApplyResult;
import com.coder.kzxt.classe.fragment.StudentApplyFragment;
import com.coder.kzxt.classe.fragment.TeacherApplyFragment;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.views.CustomNewDialog;

/**
 * 申请加入班级成员信息
 * Created by wangtingshun on 2017/3/18.
 */

public class ApplyMemberInfo extends BaseActivity {

    private String userName;
    private String mobile;
    private String studNum;
    private String idPhotoUrl;
    private String userGender;
    private String isTeacher;//是否为老师  1 是    0 不是
    private String email;
    private String id;

    private TextView info_refuse;
    private TextView info_agree;
    private EditText refuseReason;
    private Toolbar mTooBar;
    private String reason="";
    private CustomNewDialog customNewDialog;
    private SharedPreferencesUtil spu;
    private StudentApplyFragment studentFragment;
    private TeacherApplyFragment teacherFragment;
    private ClassApplyResult.ClassApplyBean applyBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_mem_info);
        spu = new SharedPreferencesUtil(this);

        getIntentData();
        initView();
        initListener();
    }

    private void getIntentData() {
        applyBean = (ClassApplyResult.ClassApplyBean) getIntent().getSerializableExtra("applyBean");
        userName = applyBean.getUserName();
        mobile = applyBean.getMobile();
        studNum = applyBean.getStudNum();
        idPhotoUrl = applyBean.getIdPhotoUrl();
        isTeacher = applyBean.getIsTeacher();
        userGender = applyBean.getUserGender();
        email = applyBean.getEmail();
        id = applyBean.getId();
}

    private void initView(){
        mTooBar = (Toolbar) findViewById(R.id.toolbar);
        mTooBar.setTitle(userName);
        setSupportActionBar(mTooBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView stu_idphoto_img=(ImageView) findViewById(R.id.stu_idphoto_img);
        TextView stu_name_info=(TextView)findViewById(R.id.stu_name_info);
        TextView stu_mobile=(TextView)findViewById(R.id.stu_mobile);
        TextView stu_mobile_info=(TextView)findViewById(R.id.stu_mobile_info);
        TextView stu_num_info=(TextView)findViewById(R.id.stu_num_info);
        LinearLayout studentnum=(LinearLayout)findViewById(R.id.studentnum);
        View studentnum_fenge=(View)findViewById(R.id.studentnum_fenge);
        RelativeLayout member_info=(RelativeLayout)findViewById(R.id.member_info);
        RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams)member_info.getLayoutParams();

        info_refuse=(TextView)findViewById(R.id.info_refuse);
        info_agree=(TextView)findViewById(R.id.info_agree);
        studentFragment = new StudentApplyFragment();
        teacherFragment = new TeacherApplyFragment();

        GlideUtils.loadCredentialsImg(this,idPhotoUrl,stu_idphoto_img);
        stu_name_info.setText(userName);
        stu_mobile_info.setText(mobile);
        stu_num_info.setText(studNum);
        if(isTeacher.equals("1")){
            studentnum_fenge.setVisibility(View.GONE);
            studentnum.setVisibility(View.GONE);
            layoutParams.height=getResources().getDimensionPixelSize(R.dimen.woying_340_dip);
            if(userGender.equals("male")){
                stu_idphoto_img.setImageDrawable(getResources().getDrawable(R.drawable.join_photo_male));
            }else{
                stu_idphoto_img.setImageDrawable(getResources().getDrawable(R.drawable.join_photo_fmale));
            }
            if(mobile.equals("")){
                stu_mobile.setText(getResources().getString(R.string.email));
                stu_mobile_info.setText(email);
            }
        }
    }
    private void initListener(){
       //拒绝
        info_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customNewDialog=new CustomNewDialog(ApplyMemberInfo.this,R.layout.refuse);
                refuseReason=(EditText)customNewDialog.findViewById(R.id.refuse_resaon);
                TextView cancle = (TextView) customNewDialog.findViewById(R.id.cancle);
                TextView confirm=(TextView)customNewDialog.findViewById(R.id.confirm);
                customNewDialog.show();
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customNewDialog.cancel();
                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        judgeInfo();//判断是否填写数据
                    }
                });
            }
        });
        //通过
        info_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyBean.setAgree(true);
                Intent bundleData = getBundleData();
                setResult(2, bundleData);
                finish();
            }
        });
    }

    private Intent getBundleData() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("applyBean", applyBean);
        intent.putExtra("data", bundle);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case android.R.id.home:
                    finish();
                    break;
                default:
                    break;
            }

        return super.onOptionsItemSelected(item);
    }

    private void judgeInfo() {
        reason = refuseReason.getText().toString().trim();
        if (reason.length() > 50) {
            refuseReason.setError(getResources().getString(R.string.refuse_num_fifty));
            refuseReason.setHintTextColor(getResources().getColor(R.color.font_red));
            return;
        }
        applyBean.setAgree(false);
        Intent bundleData = getBundleData();
        setResult(2, bundleData);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}
