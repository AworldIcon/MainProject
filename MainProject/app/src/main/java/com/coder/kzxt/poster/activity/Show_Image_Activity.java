package com.coder.kzxt.poster.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.gesture.view.PhotoViewAttacher;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.Constants;

import java.io.IOException;

public class Show_Image_Activity extends BaseActivity
{

    private ImageView show_img;
    private ImageView fail_img;
    private String imgurl;
    private PhotoViewAttacher mAttacher;
    private ImageView save_img;
    private TextView save_tx;
    private Bitmap tmpBitmap = null;

    public static void gotoActivity(Context context, String imgUrl)
    {
        context.startActivity(new Intent(context, Show_Image_Activity.class).putExtra("imgurl",imgUrl));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__image_);

        show_img = (ImageView) findViewById(R.id.show_img);
        fail_img = (ImageView) findViewById(R.id.fail_img);
        save_tx = (TextView) findViewById(R.id.save_tx);
        imgurl = getIntent().getStringExtra("imgurl");
        save_img = (ImageView) findViewById(R.id.save_img);
        mAttacher = new PhotoViewAttacher(show_img);

        save_tx.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (tmpBitmap == null)
                {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.picture_save_fail), Toast.LENGTH_SHORT).show();
                    return;
                }
                Long startVoiceT = System.currentTimeMillis();
                try
                {
                    Bimp.saveBitmapToFile(tmpBitmap, Constants.SAVE_PICTURE + startVoiceT + ".jpg", false);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.picture_saved_to) + Constants.SAVE_PICTURE + getResources().getString(R.string.picture_file), Toast.LENGTH_SHORT).show();
                } catch (IOException e)
                {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.picture_save_fail), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


        Glide.with(Show_Image_Activity.this).load(imgurl).asBitmap().into(new SimpleTarget<Bitmap>()
        {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
            {
                tmpBitmap = resource;
                if (tmpBitmap != null)
                {
                    show_img.setImageBitmap(tmpBitmap);
                } else
                {
                    fail_img.setVisibility(View.VISIBLE);
                }
                mAttacher = new PhotoViewAttacher(show_img);
            }
        });

    }


    @Override
    protected void onDestroy()
    {
        mAttacher.cleanup();
        super.onDestroy();
    }
}
