package com.coder.kzxt.im.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;


/**
 * 发送语音提示控件
 */
public class VoiceSendingView extends RelativeLayout
{


    private AnimationDrawable frameAnimation;
    private TextView text;
    private ImageView img;
    private ImageView cancelImage;
    private Context context;
    private Boolean isRecording;

    public VoiceSendingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.voice_sending, this);
        cancelImage = (ImageView) findViewById(R.id.cancelImage);
        img = (ImageView) findViewById(R.id.microphone);
        img.setBackgroundResource(R.drawable.chat_animation_voice);
        text = (TextView) findViewById(R.id.text);
        frameAnimation = (AnimationDrawable) img.getBackground();
        this.context = context;
        isRecording = true;

    }

    public void showRecording()
    {
        if (!isRecording)
        {
            isRecording = true;
            frameAnimation.start();
            img.setVisibility(VISIBLE);
            cancelImage.setVisibility(GONE);
            text.setText(context.getResources().getString(R.string.chat_up_finger));
        }
    }

    public void showCancel()
    {
        if (isRecording)
        {
            isRecording = false;
            frameAnimation.stop();
            img.setVisibility(GONE);
            cancelImage.setVisibility(VISIBLE);
            text.setText(context.getResources().getString(R.string.chat_release_cancel_send));

        }
    }


    public void release()
    {
        frameAnimation.stop();
        isRecording = false;
    }

    public Boolean getRecording()
    {
        return isRecording;
    }
}
