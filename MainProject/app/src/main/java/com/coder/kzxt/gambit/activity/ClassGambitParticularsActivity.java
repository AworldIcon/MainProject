package com.coder.kzxt.gambit.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpGetOld;
import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.baidu.mobstat.StatService;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.gambit.activity.adapter.GambitCommentAdapter;
import com.coder.kzxt.gambit.activity.adapter.GambitCommentDelegate;
import com.coder.kzxt.gambit.activity.adapter.GambitPraAdapter;
import com.coder.kzxt.gambit.activity.adapter.GambitPraDelegate;
import com.coder.kzxt.gambit.activity.bean.Comment;
import com.coder.kzxt.gambit.activity.bean.Gambit;
import com.coder.kzxt.gambit.activity.bean.GambitEntity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
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
import java.util.HashMap;

public class ClassGambitParticularsActivity extends AppCompatActivity implements InterfaceHttpResult {
    private SharedPreferencesUtil pu;
    public String gambitId;
    private String from;
    private String isCenter;
    private ImageView leftImage;
    private ImageView rightImage;
    private TextView title;

    //底部发送布局
    public RelativeLayout reply_ly;
    public ImageView reply_iv;
    public EditText reply_et;
    public TextView reply_tx;

    private ArrayList<Gambit> comList;
    public ArrayList<HashMap<String, String>> likeList;
    private ArrayList<Gambit> arrayList;

    //公共显示布局
    public LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button load_fail_button;
    private ListView my_listview;

    //activity中的悬停tab布局

    //headerView布局
    private View headerView;
    private LinearLayout header_ly;
    private ImageView user_head_img;
    private TextView user_name_tx;
    private TextView praise_status_tx;
    private TextView is_top_tx;
    private TextView content_tx;
    private TextView gambit_content_tx;

    private LinearLayout imgs_ly;
    private ImageView single_img_iv;
    private ImageView one_img_iv;
    private ImageView two_img_iv;
    private ImageView tr_img_iv;

    private AnimationDrawable animationDrawable;
    private RelativeLayout voice_ly;
    private RelativeLayout voice_bj_layout;
    private ImageView voice_img;
    private TextView voice_time_tx;
    private ProgressBar voice_progress_header;
    public TextView time_tx;

    //headerView悬停tab布局
    private LinearLayout hover_tab_ly;
    private TextView com_tx;
    private TextView com_num_tx;
    private View com_v;
    private TextView pra_tx;
    private TextView pra_num_tx;
    private View pra_v;
    private RelativeLayout comment_ly;
    private RelativeLayout praise_ly;
    private int page = 1;
    private int totalpage=1;
    private GambitEntity gambitEntity;
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private Boolean isRefreshLastPage = false;//是否刷新上一个页面
    public String ispra;//赞或取消

    private String showTab = "com"; //当前显示的tab

    public boolean isreply = false;
    public String replayGid;
    public String replayGpostId;
    private int replayPostion;
    private static final int BIGGER = 1;
    private static final int SMALLER = 2;
    private static final int MSG_RESIZE = 1;
    private InputHandler mHandler;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    public MyPullRecyclerView myPullRecyclerView;
    public GambitPraAdapter gambitPraAdapter;
    private GambitPraDelegate gambitPraDelegate;
    public GambitCommentAdapter gambitCommentAdapter;
    private GambitCommentDelegate gambitCommentDelegate;
    public static int selection;
    private RelativeLayout activity_class_gambit_particulars;
    class InputHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_RESIZE:
                    if (msg.arg1 == BIGGER) {
                        // 关闭
                        isreply = false;
                        reply_et.setText("");
                        reply_et.setHint(getResources().getString(R.string.input_comment_hint));
                    } else {
                    }
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_gambit_particulars);
        activity_class_gambit_particulars= (RelativeLayout) findViewById(R.id.my_layout);
        pu=new SharedPreferencesUtil(this);
        mHandler = new InputHandler();
        gambitId = getIntent().getStringExtra("gambitId");
        from = getIntent().getStringExtra("from");
        isCenter = getIntent().getStringExtra(Constants.IS_CENTER) == null ? "0" : getIntent().getStringExtra(Constants.IS_CENTER);
        comList = new ArrayList<>();
        likeList = new ArrayList<>();
        arrayList = new ArrayList<>();
        jiazai_layout= (LinearLayout) findViewById(R.id.jiazai_layout);
        leftImage = (ImageView) findViewById(R.id.leftImage);
        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.topic_particulars));

        rightImage = (ImageView) findViewById(R.id.rightImage);
        rightImage.setBackgroundResource(R.drawable.pics_menu);
        rightImage.setVisibility(View.GONE);

        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        my_listview = (ListView) findViewById(R.id.pullToRefreshView);
        my_listview.setDivider(null);
        my_listview.setCacheColorHint(getResources().getColor(R.color.transparent));
        my_listview.setSelector(R.color.transparent);

        reply_ly = (RelativeLayout) findViewById(R.id.reply_ly);
        reply_iv = (ImageView) findViewById(R.id.reply_iv);
        reply_et = (EditText) findViewById(R.id.reply_et);
        reply_tx = (TextView) findViewById(R.id.reply_tx);

        headerView = getLayoutInflater().inflate(R.layout.gambit_particulars_header, null);
        header_ly = (LinearLayout) headerView.findViewById(R.id.header_ly);
        user_head_img = (ImageView) headerView.findViewById(R.id.user_head_img);
        user_name_tx = (TextView) headerView.findViewById(R.id.user_name_tx);
        praise_status_tx = (TextView) headerView.findViewById(R.id.praise_status_tx);
        praise_status_tx.setVisibility(View.GONE);
        is_top_tx = (TextView) headerView.findViewById(R.id.is_top_tx);
        content_tx = (TextView) headerView.findViewById(R.id.content_tx);
        gambit_content_tx = (TextView) headerView.findViewById(R.id.gambit_content_tx);
        imgs_ly = (LinearLayout) headerView.findViewById(R.id.imgs_ly);
        single_img_iv = (ImageView) headerView.findViewById(R.id.single_img_iv);
        one_img_iv = (ImageView) headerView.findViewById(R.id.one_img_iv);
        two_img_iv = (ImageView) headerView.findViewById(R.id.two_img_iv);
        tr_img_iv = (ImageView) headerView.findViewById(R.id.tr_img_iv);
        voice_ly = (RelativeLayout) headerView.findViewById(R.id.voice_ly);
        voice_bj_layout = (RelativeLayout) headerView.findViewById(R.id.voice_bj_layout);
        voice_img = (ImageView) headerView.findViewById(R.id.voice_img);
        voice_time_tx = (TextView) headerView.findViewById(R.id.voice_time_tx);
        voice_progress_header = (ProgressBar) headerView.findViewById(R.id.voice_progress_header);
        time_tx = (TextView) headerView.findViewById(R.id.time_tx);

        hover_tab_ly = (LinearLayout) headerView.findViewById(R.id.hover_tab_ly);
        hover_tab_ly.setVisibility(View.VISIBLE);
        com_tx = (TextView) headerView.findViewById(R.id.com_tx);
        com_num_tx = (TextView) headerView.findViewById(R.id.com_num_tx);
        com_v = headerView.findViewById(R.id.com_v);

        pra_tx = (TextView) headerView.findViewById(R.id.pra_tx);
        pra_num_tx = (TextView) headerView.findViewById(R.id.pra_num_tx);
        pra_v = headerView.findViewById(R.id.pra_v);

        comment_ly = (RelativeLayout) headerView.findViewById(R.id.comment_ly);
        praise_ly = (RelativeLayout) headerView.findViewById(R.id.praise_ly);

        my_listview.addHeaderView(headerView);
        myPullSwipeRefresh= (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView= (MyPullRecyclerView) findViewById(R.id.myRecyclerView);



        ResizeLayout layout = (ResizeLayout) findViewById(R.id.my_layout);
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



        initEvents();
        httpRequest();
    }

    private void initEvents() {
        leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRefreshLastPage) {
                    Intent intent = new Intent();
                    intent.putExtra("freshId", gambitEntity.getId());
                    intent.putExtra("ispra", ispra);
                    intent.putExtra("istop", gambitEntity.getIsTop());
                    setResult(1, intent);
                }
                finish();
            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_fail_button.setVisibility(View.GONE);
                load_fail_layout.setVisibility(View.GONE);
                index=1;
                httpRequest();
            }
        });
        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(ClassGambitParticularsActivity.this, R.layout.posters_item_popupwindow, null);
                final PopupWindow customPopWindow = new PopupWindow(ClassGambitParticularsActivity.this);
                customPopWindow.setContentView(view);
                customPopWindow.setWidth((int)getResources().getDimension(R.dimen.woying_140_dip));
                customPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                customPopWindow.setBackgroundDrawable(new BitmapDrawable());
                customPopWindow.setFocusable(true);
                customPopWindow.setOutsideTouchable(true);
                customPopWindow.setAnimationStyle(R.style.AnimationPopup);
                customPopWindow.showAsDropDown(rightImage, -10,20);
                RelativeLayout bt1 = (RelativeLayout) view.findViewById(R.id.item_one_ly);
                RelativeLayout bt2 = (RelativeLayout) view.findViewById(R.id.item_two_ly);
                RelativeLayout bt3 = (RelativeLayout) view.findViewById(R.id.item_tr_ly);
                bt3.setVisibility(View.VISIBLE);

                TextView item_one_tx = (TextView) view.findViewById(R.id.item_one_tx);
                TextView item_two_tx = (TextView) view.findViewById(R.id.item_two_tx);
                TextView item_tr_tx = (TextView) view.findViewById(R.id.item_tr_tx);

                View fenge1 = view.findViewById(R.id.fenge1);
                fenge1.setVisibility(View.VISIBLE);

                Drawable drawable = getResources().getDrawable(R.drawable.gambit_menu_top);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                item_one_tx.setCompoundDrawables(drawable, null, null, null);

                if (gambitEntity.getIsTop().equals("0")) {
                    item_one_tx.setText(getResources().getString(R.string.make_first));
                } else {
                    item_one_tx.setText(getResources().getString(R.string.cancel_first));
                }

                Drawable drawable1 = getResources().getDrawable(R.drawable.gambit_menu_ess);
                drawable1.setBounds(0, 0, drawable.getMinimumWidth(), drawable1.getMinimumHeight());
                item_two_tx.setCompoundDrawables(drawable1, null, null, null);

                if (gambitEntity.getIsElite().equals("0")) {
                    item_two_tx.setText(getResources().getString(R.string.make_best));
                } else {
                    item_two_tx.setText(getResources().getString(R.string.cancel_best));
                }

                Drawable drawable2 = getResources().getDrawable(R.drawable.question_del);
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                item_tr_tx.setCompoundDrawables(drawable2, null, null, null);
                item_tr_tx.setText(getResources().getString(R.string.delete));

                bt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jiazai_layout.setVisibility(View.VISIBLE);
                        String opType;
                        if (gambitEntity.getIsTop().equals("0")) {
                            opType = "top";
                        } else {
                            opType = "unTop";
                        }

                        HttpPostOld httpPost=new HttpPostOld(ClassGambitParticularsActivity.this, this, new InterfaceHttpResult() {
                            @Override
                            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                                if(code==Constants.HTTP_CODE_SUCCESS){
                                    httpRequest();
                                }else {
                                    ToastUtils.makeText(getApplicationContext(),  getResources().getString(R.string.opera_fail), Toast.LENGTH_LONG).show();

                                }
                            }
                        },null,Urls.TRIGGER_CLASS_THREAD,gambitId,opType);
                        httpPost.excute();
                        customPopWindow.dismiss();
                    }
                });

                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String opType;
                        jiazai_layout.setVisibility(View.VISIBLE);
                        if (gambitEntity.getIsElite().equals("0")) {
                            opType = "elite";
                        } else {
                            opType = "unElite";
                        }
                        HttpPostOld httpPost=new HttpPostOld(ClassGambitParticularsActivity.this, this, new InterfaceHttpResult() {
                            @Override
                            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                                if(code==Constants.HTTP_CODE_SUCCESS){
                                    httpRequest();
                                }else {
                                    ToastUtils.makeText(getApplicationContext(),  getResources().getString(R.string.opera_fail), Toast.LENGTH_LONG).show();

                                }
                            }
                        },null,Urls.TRIGGER_CLASS_THREAD,gambitId,opType);
                        httpPost.excute();
                        customPopWindow.dismiss();
                    }
                });

                bt3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HttpPostOld httpPost=new HttpPostOld(ClassGambitParticularsActivity.this, this, new InterfaceHttpResult() {
                            @Override
                            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                                if(code==Constants.HTTP_CODE_SUCCESS){
                                    setResult(2);
                                    finish();
                                }else {
                                    ToastUtils.makeText(getApplicationContext(),  getResources().getString(R.string.opera_fail), Toast.LENGTH_LONG).show();

                                }

                            }
                        },null,Urls.CLOSE_CLASS_THREAD,gambitId);
                        httpPost.excute();
                        customPopWindow.dismiss();
                    }
                });
//
            }
        });
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isloadmore=false;
                index=1;
                gambitCommentAdapter.resetPageIndex();
                httpRequest();
            }
        });
        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                isloadmore=true;
                gambitCommentAdapter.addPageIndex();
                if(index<totalpage){
                    index++;
                }
                httpRequest();

            }
        });
    }


    /**
     * 获得话题详情
     */
    private HttpGetOld httpGet;
    public boolean ok=false;
    public int index=1;
    private boolean isloadmore=false;
    public void httpRequest(){
        jiazai_layout.setVisibility(View.VISIBLE);
        httpGet=new HttpGetOld(this,this,this,null, Urls.GET_CLASS_THREAD_APPRAISE,gambitId,"20",String.valueOf(index),"3","1","0");
        httpGet.excute();
    }
    @Override
    public void getCallback(int requestCode, int code, String msg, Object baseBean) {
        jiazai_layout.setVisibility(View.GONE);
        //myPullSwipeRefresh.setRefreshing(false);
        ArrayList<String> list = new ArrayList<String>();
        if(code==Constants.HTTP_CODE_SUCCESS){
            try {
                JSONObject jsonO = new JSONObject(httpGet.getStringData());
                //解析话题内容
                String string_data = jsonO.getString("data");
                JSONObject jsonObject = new JSONObject(string_data);
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                String contentText = jsonObject.getString("contentText");
//							String hitNum = jsonObject.getString("hitNum");
                String postNum = jsonObject.getString("postNum");
                String className = jsonObject.getString("className");
                String classId = jsonObject.getString("classId");
                String createdTime = jsonObject.getString("createdTime");
                String userId = jsonObject.getString("userId");
                String userGender = jsonObject.getString("userGender");
                String isLiked = jsonObject.getString("isLiked");
                String likeNum = jsonObject.getString("likeNum");
                String userName = jsonObject.getString("userName");
                String userFace = jsonObject.getString("userFace");
//						    String audioId = jsonObject.getString("audioId");
                String audioDuration = jsonObject.getString("audioDuration");
                String audioUrl = jsonObject.getString("audioUrl");
                String isTop = jsonObject.getString("isTop");
                String isElite = jsonObject.getString("isElite");
                String hasAccess = jsonObject.getString("hasAccess");
                gambitEntity = new GambitEntity();
                gambitEntity.setId(id);
                gambitEntity.setTitle(title);
                gambitEntity.setContentText(contentText);
//							gambitEntity.setHitNum(hitNum);
                gambitEntity.setIsLiked(isLiked);
                gambitEntity.setLikeNum(likeNum);
                gambitEntity.setPostNum(postNum);
                gambitEntity.setClassName(className);
                gambitEntity.setClassId(classId);
                gambitEntity.setCreatedTime(createdTime);
                gambitEntity.setUserId(userId);
                gambitEntity.setUserGender(userGender);
                gambitEntity.setUserName(userName);
                gambitEntity.setUserFace(userFace);
                gambitEntity.setAudioDuration(audioDuration);
                gambitEntity.setAudioUrl(audioUrl);
                gambitEntity.setIsTop(isTop);
                gambitEntity.setIsElite(isElite);
                gambitEntity.setHasAccess(hasAccess);

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


                if (jsonObject.has("likeList")) {
                    String likes = jsonObject.getString("likeList");
                    JSONArray array = new JSONArray(likes);
                    if (array.length() > 0) {
                            likeList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            HashMap<String, String> likeMap = new HashMap<String, String>();
                            JSONObject likeObject = array.getJSONObject(i);
                            String likeFace = likeObject.getString("userFace");
                            String likeGender = likeObject.getString("userGender");
                            String likeId = likeObject.getString("userId");
                            String likeName = likeObject.getString("userName");
                            likeMap.put("likeFace", likeFace);
                            likeMap.put("likeGender", likeGender);
                            likeMap.put("likeId", likeId);
                            likeMap.put("likeName", likeName);
                            likeList.add(likeMap);
                        }
                    }
                }

                // 解析回复内容
                if (jsonObject.has("post")) {
                    String post = jsonObject.getString("post");
                    if (!TextUtils.isEmpty(post)) {
                        // 创建消息对象
                        JSONObject jsonPost = new JSONObject(post);
                        // 获得总页数
                        if (jsonPost.has("totalPages")) {
                            totalpage = jsonPost.getInt("totalPages");
                        }

                        String postData = jsonPost.getString("data");
                        JSONArray array = new JSONArray(postData);
                        arrayList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            ArrayList<String> imgs = new ArrayList<>();
                            // 回复信息和话题信息结构一样，放在话题对象里展示
                            Gambit postGambit = new Gambit();
                            JSONObject jsonObject2 = array.getJSONObject(i);
                            String post_id = jsonObject2.getString("id");// 回复id
                            String post_contentText = jsonObject2.getString("contentText");// 回复内容
                            String post_userId = jsonObject2.getString("userId");// 回复者id
                            String post_threadId = jsonObject2.getString("threadId");// 回复的话题id
                            String post_pathUrl = jsonObject2.getString("audioUrl");// 回复语音播放地址
                            String post_duration = jsonObject2.getString("audioDuration");// 语音播放时间
                            String post_ctime = jsonObject2.getString("createdTime");// 回复的时间
                            String post_username = jsonObject2.getString("userName");// 回复的昵称
                            String post_userface = jsonObject2.getString("userFace");// 回复的头像
                            String post_isLiked = jsonObject.getString("isLiked");
                            String post_likeNum = jsonObject.getString("likeNum");
                            String post_userGender = jsonObject2.getString("userGender");// 回复的性别
                            String post_commentNum = jsonObject2.getString("commentNum");// 回复的评论数

                            if (jsonObject2.has("img")) {
                                String img = jsonObject2.getString("img");
                                JSONArray imgArray = new JSONArray(img);
                                if (imgArray.length() > 0) {
                                    for (int j = 0; j < imgArray.length(); j++) {
                                        String imgUrl = imgArray.getString(j);
                                        imgs.add(imgUrl);
                                    }
                                }
                            }

                            postGambit.setGpostId(post_id);
                            postGambit.setGcontentText(post_contentText);
                            postGambit.setGid(post_threadId);
                            postGambit.setGisLiked(post_isLiked);
                            postGambit.setGlikeNum(post_likeNum);
                            postGambit.setGpostUserId(post_userId);
                            postGambit.setGaudioUrl(post_pathUrl);
                            postGambit.setGaudioDuration(post_duration);
                            postGambit.setGaudioPlaying("0");
                            postGambit.setGaudioProgress("0");
                            postGambit.setGcreatedTime(post_ctime);
                            postGambit.setGuserName(post_username);
                            postGambit.setGuserFace(post_userface);
                            postGambit.setGuserGender(post_userGender);
                            postGambit.setGpostCommentNum(post_commentNum);
                            postGambit.setGimgList(imgs);

                            // 解析回复的评论内容
                            if (jsonObject2.has("comment")) {
                                String comment = jsonObject2.getString("comment");
                                // Log.v("tangcy", "评论的信息:"+comment);
                                if (!TextUtils.isEmpty(comment)) {
                                    ArrayList<Comment> comments = new ArrayList<Comment>();
                                    JSONArray array_replay = new JSONArray(comment);
                                    for (int k = 0; k < array_replay.length(); k++) {

                                        Comment entityComment = new Comment();
                                        JSONObject jsonObject3 = array_replay.getJSONObject(k);
                                        String comment_id = jsonObject3.getString("id");// 评论id
                                        String comment_userId = jsonObject3.getString("userId");// 评论者id
                                        String comment_ctime = jsonObject3.getString("createdTime");// 评论的时间
                                        String comment_username = jsonObject3.getString("userName");// 评论的昵称
                                        String comment_replyname = jsonObject3.getString("replyName");// 被回复的昵称
                                        String comment_content = jsonObject3.getString("content");// 评论的内容

                                        entityComment.setCid(comment_id);
                                        entityComment.setCuserId(comment_userId);
                                        entityComment.setCcreatedTime(comment_ctime);
                                        entityComment.setCuserName(comment_username);
                                        entityComment.setCcontent(comment_content);
                                        entityComment.setCreplyName(comment_replyname);

                                        comments.add(entityComment);
                                    }
                                    postGambit.setComments(comments);
                                }
                            }
                            // 添加回复信息到集合
                            arrayList.add(postGambit);
                        }
                    }
                }
                gambitEntity.setImgs(list);

                if (gambitEntity.getHasAccess().equals("1")) {
                    rightImage.setVisibility(View.VISIBLE);
                } else {
                    rightImage.setVisibility(View.GONE);
                }
                if(!isloadmore){
                    comList.clear();
                    comList .addAll(arrayList);
                }else {
                    comList .addAll(arrayList);
                }
                isloadmore=false;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(gambitCommentDelegate==null){
                gambitCommentDelegate=new GambitCommentDelegate(this,from,gambitEntity);
                gambitPraDelegate=new GambitPraDelegate(this,from,gambitEntity);
                gambitPraAdapter=new GambitPraAdapter(this,likeList,gambitPraDelegate);
                gambitPraAdapter.setHeaderCount(1);
                gambitCommentAdapter=new GambitCommentAdapter(this,comList,gambitCommentDelegate);
                gambitCommentAdapter.setHeaderCount(1);
                gambitCommentAdapter.setSwipeRefresh(myPullSwipeRefresh);
                myPullRecyclerView.setAdapter(gambitCommentAdapter);
            }

            //必须重新更改头部数据
            gambitCommentDelegate.setGambitEntity(gambitEntity);
            gambitCommentAdapter.setTotalPage(totalpage);
            gambitCommentAdapter.setPullData(arrayList);
//            gambitCommentAdapter.notifyDataSetChanged();
//            if(myPullSwipeRefresh.isRefreshing()){
//                myPullSwipeRefresh.setRefreshing(false);
//            }
            if(ok){
                //回复后滚动之原位置,在delegate中
                myPullRecyclerView.scrollToPosition(selection);
                selection=0;
                ok=false;
            }

        }else if(code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
            //重新登录
            NetworkUtil.httpRestartLogin(ClassGambitParticularsActivity.this,activity_class_gambit_particulars);
        }else {
            //加载失败
            load_fail_layout.setVisibility(View.VISIBLE);
            NetworkUtil.httpNetErrTip(ClassGambitParticularsActivity.this,activity_class_gambit_particulars);
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

    /**
     * 停止播放音频文件
     */
    public void stopMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    /**
     * 播放头部的音频文件
     */
    public void playHeaderMusic(final ImageView video_img, String name, final ProgressBar progresssbar) {
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
                }
            });
            //播放错误监听
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    progresssbar.setVisibility(View.GONE);
                    animationDrawable = (AnimationDrawable) video_img.getDrawable();
                    animationDrawable.stop();
                    video_img.setImageResource(R.drawable.play_voice_false);
                    Toast.makeText(ClassGambitParticularsActivity.this, getResources().getString(R.string.no_radio), Toast.LENGTH_LONG).show();
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放列表音频文件
     */
    public void playMusic(final Gambit gambit, String name) {
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }

            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(name);
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
                    gambit.setGaudioProgress("0");
                    gambit.setGaudioPlaying("1");
                    gambitCommentAdapter.notifyDataSetChanged();
                }
            });

            // 播放完成监听
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    stopMusic();
                    gambit.setGaudioPlaying("0");
                    gambitCommentAdapter.notifyDataSetChanged();
                }
            });

            // 播放错误监听
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    gambit.setGaudioProgress("0");
                    gambit.setGaudioPlaying("0");
                    gambitCommentAdapter.notifyDataSetChanged();
                    Toast.makeText(ClassGambitParticularsActivity.this, getResources().getString(R.string.no_radio), Toast.LENGTH_LONG).show();
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //给menu设置图文，暂时无法成功
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.gambit_menu,menu);
//        if(gambitEntity!=null) {
//
//
//            if (gambitEntity.getIsTop().equals("0")) {
//                menu.findItem(R.id.action_setting1).setTitle(R.string.make_first);
//            } else {
//                menu.findItem(R.id.action_setting1).setTitle(R.string.cancel_first);
//            }
//            if (gambitEntity.getIsElite().equals("0")) {
//                menu.findItem(R.id.action_setting2).setTitle(R.string.make_best);
//            } else {
//                menu.findItem(R.id.action_setting2).setTitle(R.string.cancel_best);
//            }
//            menu.findItem(R.id.action_setting2).setTitle(R.string.delete);
//            setIconEnable(menu, true);
//        }
//        return super.onCreateOptionsMenu(menu);
//    }
//    private void setIconEnable(Menu menu, boolean enable) {
//        try
//        {
//            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
//            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
//            m.setAccessible(true);
//
//            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
//            m.invoke(menu, enable);
//
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isRefreshLastPage) {
                Intent intent = new Intent();
                intent.putExtra("freshId", gambitEntity.getId());
                intent.putExtra("ispra", ispra);
                intent.putExtra("istop", gambitEntity.getIsTop());
                setResult(1, intent);
            }
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        mMediaPlayer.release();
        super.onDestroy();
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