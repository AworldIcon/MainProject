package com.coder.kzxt.course.activity;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.image.ImageLoad;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;

public class TeacherBriefActivity extends BaseActivity {
    private Toolbar toolbar;
    private String title,briefs,faceUrl,model_key,name;
    private ImageView bgImage, userFace;
    private TextView userName, brief;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_brief);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title=getIntent().getStringExtra("title")!=null?getIntent().getStringExtra("title"):"";
        briefs=getIntent().getStringExtra("brief")!=null?getIntent().getStringExtra("brief"):"";
        faceUrl=getIntent().getStringExtra("faceUrl")!=null?getIntent().getStringExtra("faceUrl"):"";
        model_key=getIntent().getStringExtra("model_key")!=null?getIntent().getStringExtra("model_key"):"";
        name=getIntent().getStringExtra("name")!=null?getIntent().getStringExtra("name"):"";
        getSupportActionBar().setTitle(title);
        bgImage = (ImageView) findViewById(R.id.bgImage);
        userFace = (ImageView) findViewById(R.id.userFace);
        userName = (TextView) findViewById(R.id.userName);
        brief = (TextView) findViewById(R.id.brief);
        brief.setText(briefs);
        userName.setText(name);
        if(model_key.equals("FAMOUS_TEACHER_ONE")){
            ImageLoad.loadCirCleImage(this,faceUrl,R.drawable.default_teacher_header,userFace);
        }else {
            Glide.with(this).load(faceUrl).asBitmap().override(180, 180).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_teacher_header_fang).error(R.drawable.default_teacher_header_fang).centerCrop().into(new BitmapImageViewTarget(userFace) {

                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCornerRadius(25);
                    userFace.setImageDrawable(circularBitmapDrawable);
                }

            });
        }
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
    }

}
