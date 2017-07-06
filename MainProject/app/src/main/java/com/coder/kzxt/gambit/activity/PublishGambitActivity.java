package com.coder.kzxt.gambit.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.InterfaceHttpResult;
import com.app.http.builders.HttpPostFileBuilder;
import com.app.utils.L;
import com.baidu.mobstat.StatService;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.stuwork.util.TextUitl;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.PublicUtils;
import com.coder.kzxt.utils.SdcardUtils;
import com.coder.kzxt.utils.SoundMeter;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import com.coder.kzxt.views.MyGridView;
import com.coder.kzxt.views.ResizeLayout;
import com.jauker.widget.BadgeView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class PublishGambitActivity extends BaseActivity implements InterfaceHttpResult {

    private PublicUtils pu;
    private ImageView leftImage;
    private TextView title,rightText;
    private EditText content_et,title_et;
    private TextView cu_number_con,cu_number_ti;
    private LinearLayout init_replenish_ly,replenish_ly;
    private ImageView init_ask_voice_iv,init_ask_img_iv,ask_voice_iv,ask_img_iv;

    private String commitId;
    private String isCenter;
    private String commitType;

    private RelativeLayout title_ly;
    private RelativeLayout voice_ly,img_ly;
    private LinearLayout  start_voice_ly,play_voice_ly;
    private RelativeLayout record_bt;
    private ImageView input_img;

    private ImageView play_img;
    private TextView again_voice_tx;

    private MyGridView imgs_gv;
    private ImageBucketAdapter adapter;
    private String path = "";
    private Uri imageUri;

    // 录音是否超过60秒
    private Boolean isovertime = false;
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private AnimationDrawable animationDrawable;
    private int flag = 1;
    private long startVoiceT;
    private String voiceName = "";
    private SoundMeter mSensor;
    private Handler vocie_Handler = new Handler();
    private static final int POLL_INTERVAL = 300;
    private LinearLayout voice_rcd_hint_tooshort;
    private TextView voice_rcd_hint_text;
    private long endVoiceT;
    private int time;
    private LinearLayout jiazai_layout;
    private RelativeLayout playing_voice_bt;
    private ImageView inputing_img;

    private BadgeView initBadgeVoiceView;
    private BadgeView initBadgeImgView;

    private BadgeView badgeVoiceView;
    private BadgeView badgeImgView;
    private Dialog voice_dialog;

    private static final int REFRESH =1;
    private static final int OVERTIME = 5;
    private static final int BIGGER = 1;
    private static final int SMALLER = 2;
    private static final int MSG_RESIZE = 2;
    private View fenge_v;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    if(Bimp.bmp.size()>0){
                        badgeImgView.setText(Bimp.bmp.size()+"");
                        badgeImgView.setVisibility(View.VISIBLE);
                        initBadgeImgView.setText(Bimp.bmp.size()+"");
                    }else {
                        badgeImgView.setVisibility(View.GONE);
                        initBadgeImgView.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                    break;

                case MSG_RESIZE:
                    if (msg.arg1 == BIGGER) {
                        fenge_v.setVisibility(View.VISIBLE);
                        //关闭
                    } else {
                        // 开启
                        fenge_v.setVisibility(View.INVISIBLE);
                        voice_ly.setVisibility(View.GONE);
                        img_ly.setVisibility(View.GONE);
                        replenish_ly.setVisibility(View.GONE);
                        init_replenish_ly.setVisibility(View.VISIBLE);
                    }
                    break;
                case OVERTIME:
                    String downtime = msg.getData().getString("downtime");
                    Long time_long = Long.parseLong(downtime);
                    int time = (int) ((System.currentTimeMillis() - time_long) / 1000);
                    if (time > 60) {
                        isovertime = true;
                        voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                        voice_rcd_hint_text.setText(getResources().getString(R.string.record_voice_limit));
                        // 停止录音
                        stop();
                        // 停止发送消息
                        handler.removeMessages(OVERTIME);
                    } else {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("downtime", String.valueOf(startVoiceT));
                        message.setData(bundle);
                        message.what = OVERTIME;
                        handler.sendMessage(message);
                    }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        if (savedInstanceState != null) {
            path = savedInstanceState.getString(Constants.IMAGE_FILE_PATH);
        }
       // pu = new PublicUtils(this);
        mSensor = new SoundMeter();
        commitId = getIntent().getStringExtra("commitId");
        commitType = "gabmit";
        isCenter = getIntent().getStringExtra(Constants.IS_CENTER) == null ? "0" : getIntent().getStringExtra(Constants.IS_CENTER);
        badgeVoiceView = new BadgeView(this);
        badgeImgView = new BadgeView(this);
        initBadgeVoiceView = new BadgeView(this);
        initBadgeImgView = new BadgeView(this);
        jiazai_layout= (LinearLayout) findViewById(R.id.jiazai_layout);
        fenge_v = findViewById(R.id.fenge_v);
        leftImage = (ImageView) findViewById(R.id.leftImage);
        leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = (TextView) findViewById(R.id.title);
        rightText  = (TextView) findViewById(R.id.rightText);

        title_ly = (RelativeLayout) findViewById(R.id.title_ly);
        content_et = (EditText) findViewById(R.id.content_et);
        title_et = (EditText) findViewById(R.id.title_et);

        if(commitType.equals("question")){
            title_ly.setVisibility(View.GONE);
            title.setText(getResources().getString(R.string.ask));
            rightText.setText(getResources().getString(R.string.submit));
            //进入页面强制弹出输入框
            content_et.setHint(getResources().getString(R.string.input_question_clearly_hint));
            content_et.requestFocus();
            InputMethodManager inputManager =  (InputMethodManager)content_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(content_et, 0);

        }else {
            title_ly.setVisibility(View.VISIBLE);
            title.setText(getResources().getString(R.string.publish_topic));
            rightText.setText(getResources().getString(R.string.publish));
            content_et.setHint(getResources().getString(R.string.input_topic_content_hint));
            title_et.setFocusableInTouchMode(true);
            title_et.requestFocus();
            InputMethodManager inputManager =  (InputMethodManager)title_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(title_et, 0);
        }

        cu_number_con = (TextView) findViewById(R.id.cu_number_con);
        cu_number_ti = (TextView) findViewById(R.id.cu_number_ti);
        title_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 500) {
                    cu_number_ti.setText(s.length() + "");
                    cu_number_ti.setTextColor(getResources().getColor(R.color.font_gray));
                } else {
                    cu_number_ti.setText(s.length() + "");
                    cu_number_ti.setTextColor(getResources().getColor(R.color.font_red));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        content_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 500) {
                    cu_number_con.setText(s.length() + "");
                    cu_number_con.setTextColor(getResources().getColor(R.color.font_gray));
                } else {
                    cu_number_con.setText(s.length() + "");
                    cu_number_con.setTextColor(getResources().getColor(R.color.font_red));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        ResizeLayout layout = (ResizeLayout) findViewById(R.id.my_layout);
        layout.setOnResizeListener(new ResizeLayout.OnResizeListener() {

            @Override
            public void OnResize(int w, int h, int oldw, int oldh) {
                int change = BIGGER;
                if (h < oldh) {
                    change = SMALLER;
                }
                Message msg = new Message();
                msg.what = MSG_RESIZE;
                msg.arg1 = change;
                handler.sendMessage(msg);
            }
        });
        init_replenish_ly = (LinearLayout) findViewById(R.id.init_replenish_ly);
        replenish_ly =(LinearLayout) findViewById(R.id.replenish_ly);
        init_ask_voice_iv = (ImageView) findViewById(R.id.init_ask_voice_iv);
        init_ask_img_iv = (ImageView) findViewById(R.id.init_ask_img_iv);
        initBadgeVoiceView.setTargetView(init_ask_voice_iv);
        initBadgeVoiceView.setBackgroundResource(R.drawable.no_read_msg);
        initBadgeVoiceView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        initBadgeVoiceView.setText("");
        initBadgeVoiceView.setTextSize(5);
        initBadgeVoiceView.setVisibility(View.GONE);// 默认隐藏
        initBadgeImgView.setTargetView(init_ask_img_iv);
        initBadgeImgView.setBackgroundResource(R.drawable.no_read_msg);
        initBadgeImgView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        initBadgeImgView.setText("0");
        initBadgeImgView.setTextSize(10);
        initBadgeImgView.setVisibility(View.GONE);
        ask_voice_iv = (ImageView) findViewById(R.id.ask_voice_iv);
        ask_img_iv = (ImageView) findViewById(R.id.ask_img_iv);
        badgeVoiceView.setTargetView(ask_voice_iv);
        badgeVoiceView.setBackgroundResource(R.drawable.no_read_msg);
        badgeVoiceView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badgeVoiceView.setText("");
        badgeVoiceView.setTextSize(5);
        badgeVoiceView.setVisibility(View.GONE);// 默认隐藏
        badgeImgView.setTargetView(ask_img_iv);
        badgeImgView.setBackgroundResource(R.drawable.no_read_msg);
        badgeImgView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badgeImgView.setText("0");
        badgeImgView.setTextSize(10);
        badgeImgView.setVisibility(View.GONE);
        voice_ly = (RelativeLayout) findViewById(R.id.voice_ly);
        start_voice_ly = (LinearLayout) findViewById(R.id.start_voice_ly);
        play_voice_ly = (LinearLayout) findViewById(R.id.play_voice_ly);
        record_bt = (RelativeLayout) findViewById(R.id.record_bt);
        input_img = (ImageView) findViewById(R.id.input_img);
        playing_voice_bt = (RelativeLayout) findViewById(R.id.playing_voice_bt);
        inputing_img = (ImageView) findViewById(R.id.inputing_img);
        img_ly =(RelativeLayout) findViewById(R.id.img_ly);
        imgs_gv = (MyGridView) findViewById(R.id.imgs_gv);
//		img_num_tx = (TextView) findViewById(R.id.img_num_tx);
//		img_num_tx.setText("0 / 3");
        adapter = new ImageBucketAdapter();
        adapter.update_img();
        imgs_gv.setAdapter(adapter);
        play_img = (ImageView) findViewById(R.id.play_img);
        again_voice_tx = (TextView) findViewById(R.id.again_voice_tx);
        voice_rcd_hint_tooshort = (LinearLayout) findViewById(R.id.voice_rcd_hint_tooshort);
        voice_rcd_hint_text = (TextView) findViewById(R.id.voice_rcd_hint_text);
        init_ask_voice_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showVoiceMode();
            }
        });
        init_ask_img_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showImgMode();
            }
        });
        ask_voice_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoiceMode();
            }
        });
        ask_img_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImgMode();
            }
        });
        // 录音
        record_bt.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        play_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(play_img.getVisibility()==View.VISIBLE){
                    play_img.setVisibility(View.GONE);
                    playing_voice_bt.setVisibility(View.VISIBLE);
                    playMusic(inputing_img, Constants.RECORD + "//" + voiceName);
                }
            }
        });
        playing_voice_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
        again_voice_tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                badgeVoiceView.setVisibility(View.GONE);
                initBadgeVoiceView.setVisibility(View.GONE);
                playing_voice_bt.setVisibility(View.GONE);
                play_voice_ly.setVisibility(View.GONE);
                start_voice_ly.setVisibility(View.VISIBLE);
            }
        });
        imgs_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == Bimp.bmp.size()) {
                    if (Bimp.bmp.size() < 3) {
                        //获取颜色值
                        String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(PublishGambitActivity.this, R.color.first_theme));
                        //设置最多选择几张图片
                        AndroidImagePicker.getInstance().setSelectLimit(3-Bimp.bmp.size());
                        AndroidImagePicker.getInstance().pickMulti(PublishGambitActivity.this, true, themeColor, new AndroidImagePicker.OnImagePickCompleteListener()
                        {
                            @Override
                            public void onImagePickComplete(List<ImageItem> items)
                            {

                                L.d(items.get(0).path + "dddd");
                                try
                                {
                                    if (items != null && items.size() > 0) {
                                        for(int i=0;i<items.size();i++){
                                            // 保存图片到sd卡
                                            String filename = System.currentTimeMillis() + "";
                                            Bitmap bm=Bimp.revitionImageSize(items.get(i).path);
                                            path = Bimp.saveBitmap(bm, "" + filename);
                                            if (Bimp.drr.size() < 3) {
                                                Bimp.bmp.add(bm);
                                                if (!TextUtils.isEmpty(path)) {
                                                    File file = new File(path);
                                                    if (file.exists()) {
                                                        Bimp.drr.add(path);
                                                    }
                                                }
                                                adapter.update_img();
                                            }
                                        }
                                        L.d("zw--size",Bimp.bmp.size()+"size--bmp--"+Bimp.drr.size()+"--size--drr");

                                    }
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        });
//
                    }
                } else {
//                    Intent intent = new Intent(PublishGambitActivity.this, ViewPagePicsActivity.class);
//                    intent.putStringArrayListExtra("sd_urls", (ArrayList<String>) Bimp.drr);
//                    intent.putExtra("intent", "0");
//                    intent.putExtra("isShowDel", "0");
//                    startActivityForResult(intent, 2);
                }

            }

        });


        rightText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("zw--Bimp",Bimp.drr.toString());
                if(commitType.equals("question")){
                }else {
                    postGambit();
                }

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.IMAGE_FILE_PATH, path);
    }

    /**
     * 显示语音模式
     */
    private void showVoiceMode(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(PublishGambitActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        replenish_ly.setVisibility(View.VISIBLE);
        init_replenish_ly.setVisibility(View.GONE);
        ask_voice_iv.setBackgroundResource(R.drawable.gambit_voice_down);
        ask_img_iv.setBackgroundResource(R.drawable.ask_img);
        voice_ly.setVisibility(View.VISIBLE);
        if(badgeVoiceView.getVisibility()==View.GONE){
            start_voice_ly.setVisibility(View.VISIBLE);
            play_voice_ly.setVisibility(View.GONE);
        }else {
            start_voice_ly.setVisibility(View.GONE);
            play_voice_ly.setVisibility(View.VISIBLE);
        }
        img_ly.setVisibility(View.GONE);
    }

    /**
     * 显示图片模式
     */
    private void showImgMode(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(PublishGambitActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        replenish_ly.setVisibility(View.VISIBLE);
        init_replenish_ly.setVisibility(View.GONE);
        ask_img_iv.setBackgroundResource(R.drawable.ask_img_down);
        ask_voice_iv.setBackgroundResource(R.drawable.ask_voice);
        img_ly.setVisibility(View.VISIBLE);
        voice_ly.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 检查sd卡
        if (!SdcardUtils.isExistSdcard()) {
            Toast.makeText(this, getResources().getString(R.string.no_SD_card), Toast.LENGTH_LONG).show();
            return false;
        }
        // 获得控件的绝对坐标位置
        int[] location = new int[2];
        record_bt.getLocationInWindow(location); // 获取按下录音按钮在当前窗口内的绝对坐标
        int btn_rc_X = location[0];
        int btn_rc_Y = location[1];

        // 获得控件的宽高
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        record_bt.measure(w, h);
        int height = record_bt.getMeasuredHeight();
        int width = record_bt.getMeasuredWidth();

        int record_btW = btn_rc_X + width;
        int record_btH = btn_rc_Y + height;

        if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
            isovertime = false;
            // 按下检查是否有音频在播放，如果有就停止
            stopMusic();
            if (!SdcardUtils.isExistSdcard()) {
                Toast.makeText(this, getResources().getString(R.string.no_SD_card), Toast.LENGTH_LONG).show();
                return false;
            }

            if (event.getY() > btn_rc_Y && event.getY() < record_btH && event.getX() > btn_rc_X && event.getX() < record_btW) {// 判断手势按下的位置是否是语音录制按钮的范围内

                record_bt.setBackgroundResource(R.drawable.record_down);

                input_img.setVisibility(View.VISIBLE);
                input_img.setImageResource(R.drawable.input_voice);
                animationDrawable = (AnimationDrawable) input_img.getDrawable();
                animationDrawable.start();

                File path_key_file = new File(Constants.RECORD);
                if (!path_key_file.exists()) {
                    // 创建录音存放的文件夹
                    path_key_file.mkdirs();
                }
                // 开始录制的时间
                startVoiceT = System.currentTimeMillis();

                // 要录制的文件名
                voiceName = startVoiceT + ".amr";
                // 开始录制
                if(new PermissionsUtil(this).record_audioPermissions()){
                    start(voiceName);
                }
                flag = 2;

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("downtime", String.valueOf(startVoiceT));
                message.setData(bundle);
                message.what = OVERTIME;
                handler.sendMessage(message);

            }

        } else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {

            // 松开手势时执行录制完成
            record_bt.setBackgroundResource(R.drawable.record_up);
            input_img.setVisibility(View.GONE);
            voice_rcd_hint_tooshort.setVisibility(View.GONE);
            handler.removeMessages(OVERTIME);

            // 停止录音
            stop();
            // 结束录制
            flag = 1;
            // 结束录制的时间
            endVoiceT = System.currentTimeMillis();
            time = (int) ((endVoiceT - startVoiceT) / 1000);
            // 如果时间小于1秒
            if (time < 1) {
                voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                // 5秒后时间太短提示隐藏
                vocie_Handler.postDelayed(new Runnable() {
                    public void run() {
                        voice_rcd_hint_tooshort.setVisibility(View.GONE);
                    }
                }, 500);

            } else {
                // 创建录音文件
                File voiceFile = new File(Constants.RECORD + "//", voiceName);
                start_voice_ly.setVisibility(View.GONE);
                play_voice_ly.setVisibility(View.VISIBLE);
                badgeVoiceView.setVisibility(View.VISIBLE);
                initBadgeVoiceView.setVisibility(View.VISIBLE);

            }

        }

        return super.onTouchEvent(event);

    }


    private void start(String name) {
        mSensor.start(PublishGambitActivity.this, name);
        vocie_Handler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        vocie_Handler.removeCallbacks(mSleepTask);
        vocie_Handler.removeCallbacks(mPollTask);
        mSensor.stop();
    }

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            stop();
        }
    };

    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            // 根据传入的声音大小 显示不同的图片
            // updateDisplay(amp);
            vocie_Handler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };


    /**
     * 播放音频文件
     */
    private void playMusic(final ImageView video_img, String url) {

        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }

            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();// 开始在后台缓冲音频文件并返回
            // 缓冲监听
            mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                }
            });
            // 后台准备完成监听
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    // Log.v("tangcy", "缓冲完成播放音频");
                    mMediaPlayer.start();
                    video_img.setVisibility(View.VISIBLE);
                    video_img.setImageResource(R.drawable.input_voice);
                    animationDrawable = (AnimationDrawable) video_img.getDrawable();
                    animationDrawable.start();
                }
            });
            // 播放完成监听
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    stopMusic();
                    animationDrawable = (AnimationDrawable) video_img.getDrawable();
                    animationDrawable.stop();
                    video_img.setVisibility(View.GONE);
                    playing_voice_bt.setVisibility(View.GONE);
                    play_img.setVisibility(View.VISIBLE);
                }
            });
            // 播放错监听
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    animationDrawable = (AnimationDrawable) video_img.getDrawable();
                    animationDrawable.stop();
                    video_img.setVisibility(View.GONE);
                    playing_voice_bt.setVisibility(View.GONE);
                    play_img.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_radio), Toast.LENGTH_LONG).show();
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    class ImageBucketAdapter extends BaseAdapter {

        public void update_img() {
            loading_img();
        }

        @Override
        public int getCount() {
            return (Bimp.bmp.size() + 1);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(PublishGambitActivity.this).inflate(R.layout.ask_question_img_item, null);
            }
            ImageView item_grida_image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            ImageView dele_img = (ImageView) convertView.findViewById(R.id.dele_img);

            if (position == (Bimp.bmp.size())) {
                if(position<=2){
                    dele_img.setVisibility(View.GONE);
                    item_grida_image.setImageBitmap(null);
                    item_grida_image.setBackgroundResource(R.drawable.ask_add_img);
                    item_grida_image.setVisibility(View.VISIBLE);
                }else {
                    dele_img.setVisibility(View.GONE);
                    item_grida_image.setVisibility(View.GONE);
                }
            } else {
                dele_img.setVisibility(View.VISIBLE);
                item_grida_image.setImageBitmap(Bimp.bmp.get(position));
                item_grida_image.setBackgroundResource(R.color.transparent);
                item_grida_image.setVisibility(View.VISIBLE);
            }

            dele_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 从集合中移除
                    Bimp.bmp.remove(position);
                    Bimp.drr.remove(position);
                    // 总数减去1
                    if(Bimp.max>0){
                        Bimp.max -= 1;

                    }
                    notifyDataSetChanged();
                    if(Bimp.max>0){
                        if(Bimp.bmp.size()>0){
                            badgeImgView.setText(Bimp.bmp.size()+"");
                            badgeImgView.setVisibility(View.VISIBLE);
                            initBadgeImgView.setText(Bimp.bmp.size()+"");
                        }else {
                            badgeImgView.setVisibility(View.GONE);
                            initBadgeImgView.setVisibility(View.GONE);
                        }
                    }else {
                        badgeImgView.setVisibility(View.GONE);
                        initBadgeImgView.setVisibility(View.GONE);
                    }
                }
            });
            return convertView;
        }

        public void loading_img() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.drr.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            String path = Bimp.drr.get(Bimp.max);
                            Bimp.max += 1;
                            if (!TextUtils.isEmpty(path)) {
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * 停止播放音频文件
     */
    private void stopMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    private void postGambit(){

        if (NetworkUtil.isNetworkAvailable(PublishGambitActivity.this)) {
            String content = content_et.getText().toString().trim();
            String title = title_et.getText().toString().trim();

            if(!TextUtils.isEmpty(title)){
                if (TextUitl.containsEmoji(title)) {
                    Toast.makeText(PublishGambitActivity.this, getResources().getString(R.string.not_support_emoji), Toast.LENGTH_SHORT).show();
                } else {
                    File voiceFile = new File(Constants.RECORD, voiceName);
                    if (voiceFile.exists() && badgeVoiceView.getVisibility() == View.VISIBLE) {
                        if (isovertime) {
                            sumbitData("60", voiceFile.getAbsolutePath(),  content,title);
                        }else {
                            sumbitData(String.valueOf(time), voiceFile.getAbsolutePath(), content,title);
                        }
                    }else {
                        sumbitData(String.valueOf(time), null,content,title);
                    }
                }

            }else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.input_topic_introduce_hint), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_inAvailable), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getCallback(int requestCode, int code, String msg, Object baseBean) {
        jiazai_layout.setVisibility(View.GONE);
        if (code == Constants.HTTP_CODE_SUCCESS) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.opera_success), Toast.LENGTH_LONG).show();
                        content_et.setText("");
                        Bimp.max = 0;
                        Bimp.drr.clear();
                        Bimp.bmp.clear();
                        adapter.notifyDataSetChanged();
                        setResult(3);
                        finish();

        } else {
                        ToastUtils.makeText(getApplicationContext(),  getResources().getString(R.string.opera_fail), Toast.LENGTH_LONG).show();
                    }

    }
   public void sumbitData(String audioDuration,String voicePath,String content,String title) {
       jiazai_layout.setVisibility(View.VISIBLE);
       HashMap<String,String>postImages=new HashMap<>();
       for(int i=0;i<Bimp.drr.size();i++){
           postImages.put("file" + i ,Bimp.drr.get(i));
       }
       if(voicePath!=null){
           postImages.put("audioFile",voicePath);

       }
//        HttpPostOld httpPost= new HttpPostOld(PublishGambitActivity.this, PublishGambitActivity.this, PublishGambitActivity.this, BaseBean.class, postImages, Urls.ADD_CLASS_THREAD, commitId!=null?commitId:"",title, content,isCenter,audioDuration);
//        httpPost.excute();
       new HttpPostFileBuilder(this).setUrl(UrlsNew.URLHeader+"/system/file").setClassObj(null).setHttpResult(new HttpCallBack() {
           @Override
           public void setOnSuccessCallback(int requestCode, Object resultBean) {
            jiazai_layout.setVisibility(View.GONE);
           }

           @Override
           public void setOnErrorCallback(int requestCode, int code, String msg) {
               jiazai_layout.setVisibility(View.GONE);

           }
       }).setFileNames(postImages).addQueryParams("type","avatar123").build();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                adapter.notifyDataSetChanged();
                if(Bimp.bmp.size()>0){
                    badgeImgView.setText(Bimp.bmp.size()+"");
                    badgeImgView.setVisibility(View.VISIBLE);
                    initBadgeImgView.setText(Bimp.bmp.size()+"");
                }else {
                    badgeImgView.setVisibility(View.GONE);
                }

                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onPause(this);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_CENTER://屏蔽editext输入的回车键
            case KeyEvent.KEYCODE_ENTER:
                return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {

        Bimp.max = 0;
        Bimp.drr.clear();
        Bimp.bmp.clear();
        mMediaPlayer.release();
        if (voice_dialog != null && voice_dialog.isShowing()) {
            voice_dialog.dismiss();
        }
        super.onDestroy();
    }
}
