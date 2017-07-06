package com.coder.kzxt.classe.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpDeleteBuilder;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPostFileBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.buildwork.views.ContainsEmojiEditText;
import com.coder.kzxt.classe.adapter.TopicReplyAdapter;
import com.coder.kzxt.classe.beans.TopicDetailBean;
import com.coder.kzxt.classe.beans.TopicReplyBean;
import com.coder.kzxt.classe.delegate.TopicReplyDelegate;
import com.coder.kzxt.login.beans.AvatarBean;
import com.coder.kzxt.permissionUtils.PermissionsUtil;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.decoration.DividerItemDecoration;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Utils;
import com.coder.kzxt.views.CustomNewDialog;
import com.coder.kzxt.views.MyGridView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangtingshun on 2017/6/14.
 * 话题详情
 */
public class TopicDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, TopicReplyDelegate.DeleteTopicReplyInterface {


    private Toolbar toolbar;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myRecyclerView;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button loadFailBtn;
    private RelativeLayout myLayout;
    private String topicId; //班级话题id
    private int pageNum;
    private TopicDetailBean.TopicDetail detailBean;
    private ArrayList<TopicReplyBean.TopicReply> data = new ArrayList<>();
    private CustomNewDialog essenceStickDialog;
    private TopicReplyDelegate delegate;
    private TopicReplyAdapter pullRefreshAdapter;
    private ArrayList<TopicReplyBean.TopicReply> topicReplys;
    private int currentZanCount; //当前点赞个数
    private int currentCollectCount; //当前收藏个数
    private RelativeLayout replyLayout;  //话题回复
    private TextView replyText; //发表回复
    private CustomNewDialog topicReplyDialog; //话题回复的dialog
    private CustomNewDialog deleteDialog; //删除dialog
    private String path; //图片路径
    private PermissionsUtil permissionsUtil;
    private String replyContent;   //回复内容
    private String selfRole;
    private String joinState;
    private boolean hasOneLevel = false;
    private int pageIndex;
    private SharedPreferencesUtil spu;
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_detail_layout);
        if (savedInstanceState != null) {
            path = savedInstanceState.getString(Constants.IMAGE_FILE_PATH);
        }
        spu = new SharedPreferencesUtil(this);
        permissionsUtil = new PermissionsUtil(this);
        topicId = getIntent().getStringExtra("topicId");
        selfRole = getIntent().getStringExtra("selfRole");
        joinState = getIntent().getStringExtra("joinState");
        pageIndex = getIntent().getIntExtra("pageIndex",-1);
        initView();
        initData();
        initListener();
        requestData();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.topic_particulars));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //去除recyclerView默认刷新动画
        myRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) myRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);
        loadFailBtn = (Button) findViewById(R.id.load_fail_button);
        replyText = (TextView) findViewById(R.id.et_reply);
        replyLayout = (RelativeLayout) findViewById(R.id.public_reply_layout);
        initDetailView();
    }

    private void initDetailView() {
        if (selfRole.equals("0")) { //未加入
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) myRecyclerView.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
            myRecyclerView.setLayoutParams(layoutParams);
            replyLayout.setVisibility(View.GONE);
        } else  {
            replyLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        myPullSwipeRefresh.setOnRefreshListener(this);

        //加载更多
        myRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                pullRefreshAdapter.addPageIndex();
                getSingleTopicDetail();
            }
        });

        //加载失败
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestData();
            }
        });

        //话题回复
        replyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasOneLevel = false;
                alertTopicReplyDialog(null,null);
            }
        });
    }

    private TextView message;
    private TextView leftTextView;
    private TextView rightTextView;
    private TextView topicTitle;

    private void alertDeleteTopicDialog() {
        deleteDialog = new CustomNewDialog(this);
        topicTitle = (TextView) deleteDialog.findViewById(R.id.title);
        message = (TextView) deleteDialog.findViewById(R.id.message);
        leftTextView = (TextView) deleteDialog.findViewById(R.id.leftTextView);
        rightTextView = (TextView) deleteDialog.findViewById(R.id.rightTextView);
        topicTitle.setVisibility(View.GONE);
        message.setText("确定删除此详情吗");
        deleteDialog.show();
        initDeleteDialog();
    }

    private void initDeleteDialog() {
        leftTextView.setOnClickListener(new View.OnClickListener() {  //取消
            @Override
            public void onClick(View view) {
                if (deleteDialog.isShowing()) {
                    deleteDialog.cancel();
                }
            }
        });

        rightTextView.setOnClickListener(new View.OnClickListener() {  //确定
            @Override
            public void onClick(View view) {
                if (deleteDialog.isShowing()) {
                    deleteDialog.cancel();
                }
                deleteClassTopic(topicId);
            }
        });
    }

    /**
     * 删除班级话题
     *
     * @param topicId
     */
    private void deleteClassTopic(String topicId) {
                new HttpDeleteBuilder(this)
                .setUrl(UrlsNew.DELETE_CLASS_TOPIC)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        jumpOtherPage();
                        finish();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == 1009) {
                            ToastUtils.makeText(TopicDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setPath(topicId)
                .build();
    }

    public void jumpOtherPage() {
        Intent intent = new Intent();
        intent.putExtra("pageIndex",pageIndex);
        setResult(10,intent);
    }

    private void requestData() {
        loadingPage();
        getSingleTopicDetail();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.IMAGE_FILE_PATH, path);
    }

    private void initData() {
        delegate = new TopicReplyDelegate(this,selfRole,spu.getUid());
        delegate.setOnDeleteTopicReplyListener(this);
        pullRefreshAdapter = new TopicReplyAdapter(this, data, delegate);
        pullRefreshAdapter.setHeaderCount(1);
        myRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //返回
                clearCacheBimp();
                jumpOtherPage();
                finish();
                break;
            case R.id.right_item: //弹出
                alertStickEssenceDialog();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private ContainsEmojiEditText replyEditText; //回复内容
    private Button btnCancle;  //取消
    private Button btnPublic;  //发表
    private MyGridView imgs_gv;
    private ImageBucketAdapter adapter;
    /**
     * 弹出话题回复dialog
     */
    private void alertTopicReplyDialog(String topicId,Object object) {
        initTopicReplyView();
        popupSoftKeybord(replyEditText);
        initTopicReplyListener(topicId,object);
    }

    private void initTopicReplyView() {
        topicReplyDialog = new CustomNewDialog(this, R.layout.topic_public_reply_dialog);
        btnCancle = (Button) topicReplyDialog.findViewById(R.id.btn_cancle);
        btnPublic = (Button) topicReplyDialog.findViewById(R.id.btn_public_topic);
        replyEditText = (ContainsEmojiEditText) topicReplyDialog.findViewById(R.id.content_et);
        imgs_gv = (MyGridView) topicReplyDialog.findViewById(R.id.imgs_gv);
        replyEditText.setText("");
        adapter = new ImageBucketAdapter();
        Bimp.bmp.clear();
        Bimp.drr.clear();
        imgs_gv.setAdapter(adapter);
        Window dialogWindow = topicReplyDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        btnPublic.setEnabled(false);
        btnPublic.setBackgroundColor(getResources().getColor(R.color.bg_gray));
        topicReplyDialog.show();
    }

    private void initTopicReplyListener(final String topicReplyId,final Object bean) {

        //取消
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topicReplyDialog.isShowing()) {
                    topicReplyDialog.cancel();
                }
            }
        });

        //发表
        btnPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isFastClick()){
                    return;
                }
                if (!hasOneLevel) {   // 一级回复
                    compoundData(replyContent, topicId, "0", "0", Bimp.drr);
                } else {              //二级回复
                    if (bean != null) {
                        if(bean instanceof TopicReplyBean.TopicReply){
                            TopicReplyBean.TopicReply replyBean = (TopicReplyBean.TopicReply) bean;
                            publicReply(compoundJsonData(replyContent, null, replyBean.getGroup_topic_id(), replyBean.getId(), replyBean.getUser_id()));
                        } else if(bean instanceof  TopicReplyBean.Conmment){
                            TopicReplyBean.Conmment conmment = (TopicReplyBean.Conmment) bean;
                            publicReply(compoundJsonData(replyContent, null, topicReplyId, conmment.getReply_id(), conmment.getUser_id()));
                        }
                    }
                }
            }
        });

        //回复内容
        replyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                replyContent = replyEditText.getText().toString().trim();
                changeButtonState();
            }
        });

        imgs_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openPicture();
            }
        });

    }

    private void changeButtonState() {
        int length = 0;
        int size = Bimp.bmp.size();
        if (!TextUtils.isEmpty(replyContent)) {
            length = replyContent.length();
        }
        if (length > 0 || size > 0) {
            btnPublic.setEnabled(true);
            btnPublic.setBackgroundColor(getResources().getColor(R.color.first_theme));
        } else {
            btnPublic.setEnabled(false);
            btnPublic.setBackgroundColor(getResources().getColor(R.color.bg_gray));
        }
    }

    /**
     * 组装数据
     * @param replyContent
     * @param drr
     */
    private void compoundData(String replyContent,String groupId,String replyId,String toUserId,List<String> drr) {
        if (topicReplyDialog.isShowing()) {
            topicReplyDialog.cancel();
        }
        jiazai_layout.setVisibility(View.VISIBLE);
        HashMap<String, String> postImages = new HashMap<>();
        if (drr != null && drr.size() > 0) {
            for (int i = 0; i < drr.size(); i++) {
                postImages.put("file" + i, drr.get(i));
            }
            if (postImages.size() > 0) {
                uploadImage(replyContent, groupId, replyId, toUserId, postImages);
            }
        } else {
            //不带图片的上传
            publicReply(compoundJsonData(replyContent, null,groupId,replyId,toUserId));
        }
    }

    /**
     * 上传照片
     * @param replyContent
     * @param postImages
     */
    private void uploadImage(final String replyContent,final String groupId,final String replyId,final String toUserId,
                             HashMap<String, String> postImages) {
                new HttpPostFileBuilder(TopicDetailActivity.this)
                .setClassObj(AvatarBean.class)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        AvatarBean avatarBean = (AvatarBean) resultBean;
                        ArrayList<String> items = avatarBean.getItems();
                        publicReply(compoundJsonData(replyContent, items,groupId,replyId,toUserId));
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        jiazai_layout.setVisibility(View.GONE);
                        ToastUtils.makeText(TopicDetailActivity.this,msg);
                    }
                })
                .setFileNames(postImages)
                .setUrl(UrlsNew.URLHeader + "/system/file")
                .addQueryParams("type", "avatar")
                .build();
    }

    /**
     * 话题回复
     * @param jsonData
     */
    private void publicReply(String jsonData) {
                new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_TOPIC_DETAIL_REPLY)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        jiazai_layout.setVisibility(View.GONE);
                        replyContent = "";
                        getReplyDetailList();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        jiazai_layout.setVisibility(View.GONE);
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(TopicDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(TopicDetailActivity.this, myLayout);
                        }
                        ToastUtils.makeText(TopicDetailActivity.this,msg, Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(null)
                .addBodyParam(jsonData)
                .build();
        
    }

    private String compoundJsonData(String replyContent, ArrayList<String> items,String groupId,String replyId,String toUserId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        if (!TextUtils.isEmpty(replyContent) && items != null) {      //图文组合
            try {
                jsonObject.put("group_topic_id", groupId);
                jsonObject.put("reply_id", replyId);
                jsonObject.put("user_id", toUserId);
                object.put("text", replyContent);
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < items.size(); i++) {
                    jsonArray.put(i, items.get(i));
                }
                object.put("images", jsonArray);
                jsonObject.put("content", object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (!TextUtils.isEmpty(replyContent) && items == null) { //只传文字
            try {
                object.put("text", replyContent);
                jsonObject.put("group_topic_id", groupId);
                jsonObject.put("reply_id", replyId);
                jsonObject.put("to_user_id", toUserId);
                jsonObject.put("content", object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                jsonObject.put("group_topic_id", groupId);
                jsonObject.put("reply_id", replyId);
                jsonObject.put("user_id", toUserId);
                object.put("text", replyContent);
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < items.size(); i++) {
                    jsonArray.put(i, items.get(i));
                }
                object.put("images", jsonArray);
                jsonObject.put("content", object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu, menu);
        MenuItem item = menu.findItem(R.id.right_item);
        if (selfRole.equals("0") || selfRole.equals("1")) {
            item.setIcon(null);
            item.setTitle("");
            item.setEnabled(false);
        } else {
            item.setEnabled(true);
            item.setIcon(R.drawable.topic_detail_three_point);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 获取单条话题详情
     */
    private void getSingleTopicDetail() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_SINGLE_TOPIC_DETAIL)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        myPullSwipeRefresh.setRefreshing(false);
                        TopicDetailBean topicDetailBean = (TopicDetailBean) resultBean;
                        detailBean = topicDetailBean.getItem();
                        if (!TextUtils.isEmpty(detailBean.getLike_count())) {
                            currentZanCount = Integer.parseInt(detailBean.getLike_count());
                        } else {
                            currentZanCount = 0;
                        }
                        if (!TextUtils.isEmpty(detailBean.getCollect_count())) {
                            currentCollectCount = Integer.parseInt(detailBean.getCollect_count());
                        } else {
                            currentCollectCount = 0;
                        }
                        if (detailBean != null) {
                            visibleData();
                            if(delegate != null){
                                delegate.setTopicDetailData(detailBean);
                            }
                            getReplyDetailList();
                        } else {
                            hideLoadingView();
                            noDataPage();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(TopicDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(TopicDetailActivity.this, myLayout);
                        }
                        myPullSwipeRefresh.setRefreshing(false);
                        hideLoadingView();
                        loadFailPage();
                    }
                })
                .setClassObj(TopicDetailBean.class)
                .setPath(topicId)
                .build();
    }


    private RelativeLayout cancleLayout; //取消
    private ImageView essenceImage;//设置精华
    private ImageView stickImage; //设为置顶

    /**
     * 弹出置顶精华对话框
     */
    private void alertStickEssenceDialog() {
        essenceStickDialog = new CustomNewDialog(this, R.layout.essence_stick_dialog);
        cancleLayout = (RelativeLayout) essenceStickDialog.findViewById(R.id.cancle_layout);
        essenceImage = (ImageView) essenceStickDialog.findViewById(R.id.iv_essence);
        stickImage = (ImageView) essenceStickDialog.findViewById(R.id.iv_stick);
        essenceStickDialog.show();
        initEssenceStickData();
        initStickListener();
    }

    /**
     * 初始化精华置顶数据
     */
    private void initEssenceStickData() {
        String isStick = detailBean.getIs_stick();
        String isTop = detailBean.getIs_top();
        if (isStick.equals("1")) {   //精华
            essenceImage.setBackgroundResource(R.drawable.question_elite);
        } else if (isStick.equals("0")) {
            essenceImage.setBackgroundResource(R.drawable.question_un_elite);
        }

        if(isTop.equals("1")){
            stickImage.setBackgroundResource(R.drawable.topic_set_stick_red);
        } else if(isTop.equals("0")){
            stickImage.setBackgroundResource(R.drawable.topic_set_stick_white);
        }
    }

    private void initStickListener() {
        essenceImage.setOnClickListener(new View.OnClickListener() {  //设为精华
            @Override
            public void onClick(View view) {
                String isStick = detailBean.getIs_stick();
                if (isStick.equals("1")) {
                    changeTopicRequest("0", null);
                } else if (isStick.equals("0")) {
                    changeTopicRequest("1", null);
                }
            }
        });

        stickImage.setOnClickListener(new View.OnClickListener() {  //设为置顶
            @Override
            public void onClick(View view) {
                String isTop = detailBean.getIs_top();
                if (isTop.equals("1")) {
                    changeTopicRequest(null, "0");
                } else if (isTop.equals("0")) {
                    changeTopicRequest(null, "1");
                }
            }
        });

        cancleLayout.setOnClickListener(new View.OnClickListener() {  //取消
            @Override
            public void onClick(View view) {
                if (essenceStickDialog.isShowing()) {
                    essenceStickDialog.cancel();
                }
            }
        });
    }

    /**
     * 修改班级话题
     */
    private void changeTopicRequest(final String stick,final String top) {
        HttpPutBuilder builder = new HttpPutBuilder(this);
        builder.setUrl(UrlsNew.PUT_CALSS_TOPIC);
        builder.setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                if (!TextUtils.isEmpty(stick)) {
                    if (stick.equals("0")) {
                        detailBean.setIs_stick("0");
                    } else if (stick.equals("1")) {
                        detailBean.setIs_stick("1");
                    }
                } else if (!TextUtils.isEmpty(top)) {
                    if (top.equals("0")) {
                        detailBean.setIs_top("0");
                    } else if (top.equals("1")) {
                        detailBean.setIs_top("1");
                    }
                }
                initEssenceStickData();
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                if (code == 1009) {
                    ToastUtils.makeText(TopicDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (!TextUtils.isEmpty(stick)) {
            builder.addBodyParam("is_stick", stick);
        } else if (!TextUtils.isEmpty(top)) {
            builder.addBodyParam("is_top", top);
        }
        builder.setPath(topicId);
        builder.build();
    }

    /**
     * 获取回复详情列表
     */
    public void getReplyDetailList() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_GROUP_TOPIC_REPLY)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        hideLoadingView();
                        myPullSwipeRefresh.setRefreshing(false);
                        TopicReplyBean topicRepkyBean = (TopicReplyBean) resultBean;
                        topicReplys = topicRepkyBean.getItems();
                        if (topicReplys != null && topicReplys.size() > 0) {
                            visibleData();
                            pullRefreshAdapter.setTotalPage(topicRepkyBean.getPaginate().getPageNum());
                            pullRefreshAdapter.setPullData(topicReplys);
                        } else {
                            pullRefreshAdapter.clear();
                        }
                        if (topicReplyDialog != null && topicReplyDialog.isShowing()) {
                            topicReplyDialog.cancel();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(TopicDetailActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(TopicDetailActivity.this, myLayout);
                        }
                        myPullSwipeRefresh.setRefreshing(false);
                        hideLoadingView();
                        loadFailPage();
                    }
                })
                .setClassObj(TopicReplyBean.class)
                .addQueryParams("page", pullRefreshAdapter.getPageIndex() + "")
                .addQueryParams("pageSize", "20")
                .addQueryParams("orderBy", "create_time asc")
                .addQueryParams("group_topic_id", topicId)
                .build();
    }


    /**
     * 暂无数据
     */
    private void noDataPage() {
        load_fail_layout.setVisibility(View.GONE);
        myPullSwipeRefresh.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        load_fail_layout.setVisibility(View.VISIBLE);
        myPullSwipeRefresh.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    /**
     * 显示数据
     */
    private void visibleData() {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        myPullSwipeRefresh.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载页
     */
    private void loadingPage() {
        jiazai_layout.setVisibility(View.VISIBLE);
        myPullSwipeRefresh.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearCacheBimp();
    }

    private void clearCacheBimp() {
        if (Bimp.drr != null) {
            Bimp.drr.clear();
        }
        if (Bimp.bmp != null) {
            Bimp.bmp.clear();
        }
    }


    @Override
    public void onRefresh() {
        pullRefreshAdapter.resetPageIndex();
        getSingleTopicDetail();
    }

    /**
     * 弹出软键盘
     *
     * @param edit
     */
    private void popupSoftKeybord(final EditText edit) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager manager = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                manager.showSoftInput(edit, 0);
            }
        }, 100);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(topicReplyDialog != null && topicReplyDialog.isShowing()){
                topicReplyDialog.dismiss();
                return true;
            } else {
                clearCacheBimp();
                jumpOtherPage();
                finish();
            }
        }
        return false;
    }

    @Override
    public void deleteTopicReply(String id) {
                showLoadingView();
                new HttpDeleteBuilder(this)
                .setUrl(UrlsNew.DELETE_TOPIC_REPLY)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        getReplyDetailList();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == 1009) {
                            ToastUtils.makeText(TopicDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        hideLoadingView();
                    }
                })
                .setPath(id)
                .build();
    }

    @Override
    public void onOneLevelItem(TopicReplyBean.TopicReply replyBean) {   //点击一级item
        alertTopicReplyDialog(null,replyBean);
        hasOneLevel = true;
        replyEditText.setHint(getResources().getString(R.string.reply) + replyBean.getUser_profile().getNickname());
    }

    @Override
    public void onReplyToSb(String topicId,TopicReplyBean.Conmment replyBean) {
        alertTopicReplyDialog(topicId,replyBean);
        hasOneLevel = true;
        replyEditText.setHint(getResources().getString(R.string.reply) + replyBean.getUser_profile().getNickname());
    }

    @Override
    public void zanRequest(final ImageView zanImage, final TextView zanCount) {
                new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_TOPIC_LIKE)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        zanImage.setBackgroundResource(R.drawable.topic_detail_zan_red);
                        currentZanCount = currentZanCount + 1;
                        detailBean.setLike_count(String.valueOf(currentZanCount));
                        detailBean.setHas_like("1");
                        if (delegate != null) {
                            delegate.setTopicDetailData(detailBean);
                        }
                        if (pullRefreshAdapter != null) {
                            pullRefreshAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {

                    }
                })
                .addBodyParam("group_topic_id", topicId)
                .build();
    }

    @Override
    public void cancleZanRequest(final ImageView zanImage,final TextView zanCount) {
                 new HttpDeleteBuilder(this)
                .setUrl(UrlsNew.DELETE_TOPIC_LIKE)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        zanImage.setBackgroundResource(R.drawable.topic_detail_zan_white);
                        currentZanCount = currentZanCount - 1;
                        if (currentZanCount <= 0) {
                            currentZanCount = 0;
                        }
                        detailBean.setLike_count(String.valueOf(currentZanCount));
                        detailBean.setHas_like("0");
                        if (delegate != null) {
                            delegate.setTopicDetailData(detailBean);
                        }
                        if (pullRefreshAdapter != null) {
                            pullRefreshAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {

                    }
                })
                .setPath(topicId)
                .build();
    }

    @Override
    public void adddCollect(final ImageView collectImage, final TextView collectCount) {
                new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_TOPIC_COLLECT)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        collectImage.setBackgroundResource(R.drawable.topic_detail_collect_red);
                        currentCollectCount = currentCollectCount + 1;
                        detailBean.setCollect_count(String.valueOf(currentCollectCount));
                        detailBean.setHas_collect("1");
                        if (delegate != null) {
                            delegate.setTopicDetailData(detailBean);
                        }
                        if (pullRefreshAdapter != null) {
                            pullRefreshAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {

                    }
                })
                .addBodyParam("group_topic_id", topicId)
                .build();
    }

    @Override
    public void cancleCollect(final ImageView collectImage, final TextView collectCount) {
                new HttpDeleteBuilder(this)
                .setUrl(UrlsNew.DELETE_TOPIC_COLLECT)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        collectImage.setBackgroundResource(R.drawable.topic_detail_collect_white);
                        currentCollectCount = currentCollectCount - 1;
                        if (currentCollectCount <= 0) {
                            currentCollectCount = 0;
                        }
                        detailBean.setCollect_count(String.valueOf(currentCollectCount));
                        detailBean.setHas_collect("0");
                        if (delegate != null) {
                            delegate.setTopicDetailData(detailBean);
                        }
                        if (pullRefreshAdapter != null) {
                            pullRefreshAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {

                    }
                })
                .setPath(topicId)
                .build();
    }

    @Override
    public void deleteTopicDetail() {
        alertDeleteTopicDialog();
    }


    public void openPicture() {
        if (!permissionsUtil.isNeedPermissions(PERMISSIONS)) {
            //获取颜色值
            String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(TopicDetailActivity.this, R.color.first_theme));
            //设置最多选择几张图片
            AndroidImagePicker.getInstance().setSelectLimit(3 - Bimp.bmp.size());
            AndroidImagePicker.getInstance().pickMulti(TopicDetailActivity.this, true, themeColor, new AndroidImagePicker.OnImagePickCompleteListener() {
                @Override
                public void onImagePickComplete(List<ImageItem> items) {
                    try {
                        if (items != null && items.size() > 0) {
                            for (int i = 0; i < items.size(); i++) {
                                // 保存图片到sd卡
                                String filename = System.currentTimeMillis() + "";
                                Bitmap bm = Bimp.revitionImageSize(items.get(i).path);
                                path = Bimp.saveBitmap(bm, "" + filename);
                                if (Bimp.drr.size() < 3) {
                                    Bimp.bmp.add(bm);
                                    if (!TextUtils.isEmpty(path)) {
                                        File file = new File(path);
                                        if (file.exists()) {
                                            Bimp.drr.add(path);
                                        }
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                            changeButtonState();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                }
                }
            });
        }
    }


    class ImageBucketAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return (Bimp.bmp.size()) + 1;
        }

        @Override
        public Object getItem(int position) {
            return Bimp.bmp.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(TopicDetailActivity.this).inflate(R.layout.add_topic_photo, null);
            }
            ImageView item_grida_image = (ImageView) convertView.findViewById(R.id.one_image);
            ImageView dele_img = (ImageView) convertView.findViewById(R.id.one_delete);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)item_grida_image.getLayoutParams();
            String mode = Build.MODEL;
            if (position == (Bimp.bmp.size())) {
                if (position <= 2) {
                    dele_img.setVisibility(View.GONE);
                    item_grida_image.setImageBitmap(null);
                    params.width = 50;
                    params.height = 50;
                    params.setMargins(10,0,0,0);
                    item_grida_image.setBackgroundResource(R.drawable.iv_public_topic_picture);
                    item_grida_image.setVisibility(View.VISIBLE);
                } else {
                    dele_img.setVisibility(View.GONE);
                    item_grida_image.setVisibility(View.GONE);
                }
            } else {
                if (mode.equals("SM-N9002")) {
                    params.width = 90;
                    params.height = 90;
                } else {
                    params.width = 70;
                    params.height = 70;
                }
                params.setMargins(0,0,0,0);
                dele_img.setVisibility(View.VISIBLE);
                item_grida_image.setImageBitmap(Bimp.bmp.get(position));
                item_grida_image.setBackgroundResource(R.color.transparent);
                item_grida_image.setVisibility(View.VISIBLE);
            }
            item_grida_image.setLayoutParams(params);
            if (!hasOneLevel) {
                item_grida_image.setVisibility(View.VISIBLE);
            } else {
                item_grida_image.setVisibility(View.GONE);
            }

            dele_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 从集合中移除
                    Bimp.bmp.get(position).recycle();
                    Bimp.bmp.remove(position);
                    Bimp.drr.remove(position);
                    // 总数减去1
                    if (Bimp.max > 0) {
                        Bimp.max -= 1;
                    }
                    changeButtonState();
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsUtil.PERMISSION_REQUEST_CODE) {
            if (permissionsUtil.isRequestAllPermissions(grantResults)) {
                openPicture();
            } else {
                permissionsUtil.setMessage("未获得应用运行所需的基本权限(拍照，文件读写缺一不可)，请在设置中开启权限后再使用");
                permissionsUtil.showDialog();
            }
        }
    }
}
