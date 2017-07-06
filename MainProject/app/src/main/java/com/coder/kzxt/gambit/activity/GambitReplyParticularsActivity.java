package com.coder.kzxt.gambit.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpGetOld;
import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.gambit.activity.adapter.GambitReplyAdapter;
import com.coder.kzxt.gambit.activity.adapter.GambitReplyDelegate;
import com.coder.kzxt.gambit.activity.bean.Comment;
import com.coder.kzxt.gambit.activity.bean.Gambit;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.stuwork.util.TextUitl;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Urls;
import com.coder.kzxt.views.ResizeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 话题回复详情
 * */
public class GambitReplyParticularsActivity extends BaseActivity implements InterfaceHttpResult {
    public MyPullSwipeRefresh myPullSwipeRefresh;
    public MyPullRecyclerView myPullRecyclerView;
    private SharedPreferencesUtil pu;
    private ImageView leftImage;
    private TextView title;
    private String reply_id;
    private String from;
    private String gid;//话题id
    private String gtitle;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private Button load_fail_button;

    private String isCenter;


    public EditText reply_write_edit;
    private TextView reply_post_button;
    private ArrayList<Comment> comments;
    private ArrayList<Comment> comments2;
    private ArrayList<String> imgUrlList;
    private int imgUrlIndex = 0;
    private int page =1;
    private int totalpage;
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private AnimationDrawable animationDrawable;
    private ArrayList<Bitmap> bitmaps;
    private Gambit gambit;


    private static final int BIGGER = 1;
    private static final int SMALLER = 2;
    private static final int MSG_RESIZE = 1;
    private InputHandler mHandler;

    public boolean isreply =false;
    public String reply_userId;
    public String reply_user_replyname="";
    private RelativeLayout reply_post_layout;
    private RelativeLayout activity_gambit__reply__particulars_;
    private HttpGetOld httpGet;
    private boolean isloadmore=false;

    private GambitReplyDelegate gambitReplyDelegate;
    private GambitReplyAdapter gambitReplyAdapter;

    public void stopMusic(){
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }
    /**
     * 播放音频文件
     */
    public void playMusic(final ImageView video_img ,String name,final ProgressBar progresssbar) {
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(name);
            mMediaPlayer.prepareAsync();// 开始在后台缓冲音频文件并返回
            //缓冲监听
            mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                }

            });
            //后台准备完成监听
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
//					Log.v("tangcy", "缓冲完成播放音频");
                    progresssbar.setVisibility(View.GONE);
                    mMediaPlayer.start();
                    video_img.setImageResource(R.drawable.play_voice);
                    animationDrawable = (AnimationDrawable) video_img.getDrawable();
                    animationDrawable.start();
                }
            });
            //播放完成监听
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    stopMusic();
                    animationDrawable = (AnimationDrawable) video_img.getDrawable();
                    animationDrawable.stop();
                    video_img.setImageResource(R.drawable.play_voice_false);
                    gambit.setGaudioPlaying("0");
                    gambitReplyAdapter.notifyDataSetChanged();
                }
            });
            //播放错误监听
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    progresssbar.setVisibility(View.GONE);
                    gambit.setGaudioPlaying("0");
                    gambitReplyAdapter.notifyDataSetChanged();
                    animationDrawable = (AnimationDrawable) video_img.getDrawable();
                    animationDrawable.stop();
                    video_img.setImageResource(R.drawable.play_voice_false);
                    Toast.makeText(GambitReplyParticularsActivity.this, getResources().getString(R.string.no_radio), Toast.LENGTH_LONG).show();
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class InputHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_RESIZE:
                    if (msg.arg1 == BIGGER) {
                        // 关闭
                        isreply = false;
                        reply_write_edit.setText("");
                        reply_write_edit.setHint(getResources().getString(R.string.input_comment_hint));
                    } else {
                    }
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gambit__reply__particulars_);

        initView();
        initEvents();
        httpRequst();

    }

    private void initView() {
        myPullRecyclerView= (MyPullRecyclerView) findViewById(R.id.myRecyclerView);
        myPullSwipeRefresh= (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        activity_gambit__reply__particulars_= (RelativeLayout) findViewById(R.id.activity_gambit__reply__particulars_);
        reply_id = getIntent().getStringExtra("reply_id");
        from = getIntent().getStringExtra("from");
        gtitle = getIntent().getStringExtra("topicTitle");
        gid = getIntent().getStringExtra("gambitId");
        mHandler = new InputHandler();
        comments = new ArrayList<Comment>();
        comments2 = new ArrayList<Comment>();
        bitmaps = new ArrayList<Bitmap>();
        imgUrlList = new ArrayList<String>();
        pu = new SharedPreferencesUtil(this);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout= (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);

        isCenter = getIntent().getStringExtra(Constants.IS_CENTER);
        reply_write_edit = (EditText) findViewById(R.id.reply_write_edit);
        reply_post_button = (TextView) findViewById(R.id.reply_post_button);
        reply_post_layout = (RelativeLayout) findViewById(R.id.reply_post_layout);
        leftImage = (ImageView) findViewById(R.id.leftImage);
        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.topic_particulars));

    }

    private void initEvents() {
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gambitReplyAdapter.resetPageIndex();
                page=1;
                isloadmore=false;
                httpRequst();
            }
        });
        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                gambitReplyAdapter.addPageIndex();
                if(page<totalpage){
                    page++;
                    isloadmore=true;
                }
                httpRequst();
            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequst();
            }
        });
        leftImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        ResizeLayout layout = (ResizeLayout) findViewById(R.id.activity_gambit__reply__particulars_);
        layout.setOnResizeListener(new ResizeLayout.OnResizeListener() {

            @Override
            public void OnResize(int w, int h, int oldw, int oldh) {
                int change = BIGGER;
                if (h < oldh) {
                    change = SMALLER;
                }

                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = change;
                mHandler.sendMessage(msg);

            }
        });
        reply_post_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loginHideSoftInputWindow();
                if(NetworkUtil.isNetworkAvailable(GambitReplyParticularsActivity.this)){

                    if(!TextUtils.isEmpty(reply_write_edit.getText().toString().trim())){

                        if (TextUitl.containsEmoji(reply_write_edit.getText().toString().trim().toString())) {
                            Toast.makeText(GambitReplyParticularsActivity.this, getResources().getString(R.string.not_support_emoji), Toast.LENGTH_SHORT).show();
                        }else {
                            jiazai_layout.setVisibility(View.VISIBLE);
                            String gpostId = gambit.getGpostId();//话题评论id
                            if(!isreply){
                                reply_user_replyname = "";
                                HttpPostOld httpPost=new HttpPostOld(GambitReplyParticularsActivity.this, this, new InterfaceHttpResult() {
                                    @Override
                                    public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                                        jiazai_layout.setVisibility(View.GONE);
                                        if(code==Constants.HTTP_CODE_SUCCESS){
                                            httpRequst();
                                        }else {
                                            ToastUtils.makeText(GambitReplyParticularsActivity.this,getResources().getString(R.string.opera_fail), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                },null,Urls.POST_CLASS_THREAD_ACTION,gid,gpostId,"",reply_write_edit.getText().toString().trim());
                                httpPost.excute();
//                                new ReplyGambitAsyncTask(Gambit_Reply_Particulars_Activity.this,replayGambitInterface,reply_write_edit.getText().toString().trim())
//                                        .executeOnExecutor(Constants.exec, gid,gpostId,"");
                            }else {
                                //回复
                                HttpPostOld httpPost=new HttpPostOld(GambitReplyParticularsActivity.this, this, new InterfaceHttpResult() {
                                    @Override
                                    public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                                        jiazai_layout.setVisibility(View.GONE);
                                        if(code==Constants.HTTP_CODE_SUCCESS){
                                            httpRequst();
                                        }else {
                                            ToastUtils.makeText(GambitReplyParticularsActivity.this,getResources().getString(R.string.opera_fail), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                },null,Urls.POST_CLASS_THREAD_ACTION,gid,gpostId,reply_userId,reply_write_edit.getText().toString().trim());
                                httpPost.excute();
//                                new ReplyGambitAsyncTask(Gambit_Reply_Particulars_Activity.this,replayGambitInterface,reply_write_edit.getText().toString().trim())
//                                        .executeOnExecutor(Constants.exec, gid,gpostId,reply_userId);
                            }
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.has_not_content), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_inAvailable), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void httpRequst() {
        jiazai_layout.setVisibility(View.VISIBLE);
        load_fail_layout.setVisibility(View.GONE);
        httpGet=new HttpGetOld(this,this,this,null, Urls.GET_POST_CLASS_DETAIL_ACTION,reply_id,"30",String.valueOf(page),"3","1");
        httpGet.excute();
    }
    @Override
    public void getCallback(int requestCode, int code, String msg, Object baseBean) {
        jiazai_layout.setVisibility(View.GONE);
        ArrayList<String> list = new ArrayList<String>();
        if(code==Constants.HTTP_CODE_SUCCESS){
            try {
                JSONObject jsonO=new JSONObject(httpGet.getStringData());
                String string_data = jsonO.getString("data");
                JSONObject jsonObject = new JSONObject(string_data);
                //获得总页数
                if(jsonObject.has("totalPages")){
                    totalpage = jsonObject.getInt("totalPages");
                }
                //评论话题的内容
                gambit=new Gambit();
                String id=jsonObject.getString("id");//回复ID
                String userName=jsonObject.getString("userName");
                String userGender = jsonObject.getString("userGender");
                String userFace = jsonObject.getString("userFace");
                String audioId = jsonObject.getString("audioId");
                String audioDuration = jsonObject.getString("audioDuration");
                String audioUrl = jsonObject.getString("audioUrl");
                String contentText = jsonObject.getString("contentText");
                String createdTime = jsonObject.getString("createdTime");
                String isLiked = jsonObject.getString("isLiked");
                String likeNum = jsonObject.getString("likeNum");

                if (jsonObject.has("img")) {
                    String img = jsonObject.getString("img");
                    JSONArray imgArray = new JSONArray(img);
                    if (imgArray.length() > 0) {
                        for (int j = 0; j < imgArray.length(); j++) {
                            String imgUrl = imgArray.getString(j);
                            list.add(imgUrl);
                        }
                    }
                }

                gambit.setGpostId(id);
                gambit.setGcontentText(contentText);
                gambit.setGcreatedTime(createdTime);
                gambit.setGuserGender(userGender);
                gambit.setGuserName(userName);
                gambit.setGuserFace(userFace);
                gambit.setGaudioId(audioId);
                gambit.setGaudioDuration(audioDuration);
                gambit.setGaudioUrl(audioUrl);
                gambit.setGaudioPlaying("0");
                gambit.setGaudioProgress("0");
                gambit.setGimgs(list);
                gambit.setGlikeNum(likeNum);
                gambit.setGisLiked(isLiked);

                //解析回复的评论内容
                if(jsonObject.has("comment")){
                    comments.clear();
                    String comment = jsonObject.getString("comment");
                    if(!TextUtils.isEmpty(comment)){
                        JSONArray array_replay = new JSONArray(comment);
                        for(int k=0;k<array_replay.length();k++){

                            Comment entityComment = new Comment();
                            JSONObject jsonObject3 = array_replay.getJSONObject(k);
                            String comment_id=  jsonObject3.getString("id");//评论id
                            String comment_ctime =  jsonObject3.getString("createdTime");//评论的时间
                            String userId  =  jsonObject3.getString("userId");//评论者id
                            String comment_username =  jsonObject3.getString("userName");//评论的昵称
                            String comment_replyname=  jsonObject3.getString("replyName");//被回复的昵称
                            String comment_content=  jsonObject3.getString("content");//评论的内容
                            entityComment.setCid(comment_id);
                            entityComment.setCuserId(userId);
                            entityComment.setCcreatedTime(comment_ctime);
                            entityComment.setCuserName(comment_username);
                            entityComment.setCcontent(comment_content);
                            entityComment.setCreplyName(comment_replyname);
                            comments.add(entityComment);
                        }
                    }
                    if(!isloadmore){
                        comments2.clear();
                        comments2 .addAll(comments);
                    }else {
                        comments2 .addAll(comments);
                    }
                    isloadmore=false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(gambitReplyDelegate==null){
                gambitReplyDelegate=new GambitReplyDelegate(this,from,gambit,gtitle);
                gambitReplyAdapter=new GambitReplyAdapter(this,comments2,gambitReplyDelegate);
                gambitReplyAdapter.setHeaderCount(1);
                gambitReplyAdapter.setSwipeRefresh(myPullSwipeRefresh);
                myPullRecyclerView.setAdapter(gambitReplyAdapter);
            }
            gambitReplyDelegate.setGambitEntity(gambit);
            gambitReplyAdapter.setTotalPage(1);
            gambitReplyAdapter.setPullData(comments);

        }else if(code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
            //重新登录
            NetworkUtil.httpRestartLogin(GambitReplyParticularsActivity.this,activity_gambit__reply__particulars_);
        }else {
            //加载失败
            load_fail_layout.setVisibility(View.VISIBLE);
            NetworkUtil.httpNetErrTip(GambitReplyParticularsActivity.this,activity_gambit__reply__particulars_);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
    }
    //隐藏键盘
    public void loginHideSoftInputWindow()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
        {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
