package com.coder.kzxt.poster.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.http.utils.CancelPraise;
import com.coder.kzxt.http.utils.PraisePoster;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.poster.adapter.PosterParticularsAdapter;
import com.coder.kzxt.poster.adapter.PosterParticularsDelegate;
import com.coder.kzxt.poster.beans.PosterBeans;
import com.coder.kzxt.poster.beans.PostersCommentBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.stuwork.util.TextUitl;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ShareSdkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.SnackbarUtils;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.views.ResizeLayout;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class Posters_Particulars_Activity extends BaseActivity implements HttpCallBack {

    private ResizeLayout layout;
    private Toolbar mToolbarView;
    private RelativeLayout bottom_ly;
    private EditText comment_ed;
    private TextView post_button;
    private LinearLayout imgs_ly;
    private ImageView posters_like;
    private ImageView posters_share;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private InputHandler mHandler;
    private PosterParticularsDelegate postersmotionDelegate;
    private PosterParticularsAdapter particularsAdapter;
    private List<PostersCommentBean.ItemsBean> data = new ArrayList<>();
    private PosterBeans.ItemsBean bean;
    private int position;
    private String postersId;
    private String pageSize = "20";// 每页请求条数
    private SharedPreferencesUtil spu;
    private String shareUrl;
    private MenuItem delItem;
    private MenuItem reportItem;
    private int canDelete;//是否有删除权限
    private SnackbarUtils snackbarUtils;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private Dialog comDialog;
    private Button load_fail_button;
    private boolean isResItemLike = false;
    private boolean isResItemReplyNum = false;
    private String reply_uid;//被回复用户id
    private String pid;//回复父级评论id
    private boolean isPostReply = false; //是否是回复
    private int replayNum =0;//用户在这页回复了几次


    class InputHandler extends android.os.Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.MSG_RESIZE:
                    if (msg.arg1 == Constants.BIGGER) {
                        // 关闭
                        isPostReply = false;
                        comment_ed.setText("");
                        comment_ed.setHint(getResources().getString(R.string.input_comment_hint));
                        post_button.setVisibility(View.GONE);
                        imgs_ly.setVisibility(View.VISIBLE);
                    } else {
                        post_button.setVisibility(View.VISIBLE);
                        imgs_ly.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posters_particulars);
        mHandler = new InputHandler();
        spu = new SharedPreferencesUtil(this);
        snackbarUtils = new SnackbarUtils(this);
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(R.string.particulars);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarView.setOverflowIcon(getResources().getDrawable(R.drawable.pics_menu));

        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        jiazai_layout.setVisibility(View.VISIBLE);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        bean = (PosterBeans.ItemsBean) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            position = getIntent().getIntExtra("position", 0);
            postersId = bean.getId();
        } else {
            postersId = getIntent().getStringExtra("postersId");
        }
        layout = (ResizeLayout) findViewById(R.id.my_layout);
        layout.setOnResizeListener(new ResizeLayout.OnResizeListener() {

            @Override
            public void OnResize(int w, int h, int oldw, int oldh) {
                int change = Constants.BIGGER;
                if (h < oldh) {
                    change = Constants.SMALLER;
                }

                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = change;
                mHandler.sendMessage(msg);

            }
        });

         comDialog = MyPublicDialog.createLoadingDialog(this);
         initView();
         postersmotionDelegate = new PosterParticularsDelegate(Posters_Particulars_Activity.this,bean);
         particularsAdapter = new PosterParticularsAdapter(Posters_Particulars_Activity.this, data, 1, postersmotionDelegate);
         myPullRecyclerView.setAdapter(particularsAdapter);
         particularsAdapter.setSwipeRefresh(myPullSwipeRefresh);
         httpParticulars();
         httpRequest();
         initClick();
    }

    private void initView() {
        bottom_ly = (RelativeLayout) findViewById(R.id.bottom_ly);
        comment_ed = (EditText) findViewById(R.id.comment_ed);
        post_button = (TextView) findViewById(R.id.post_button);
        GradientDrawable myGrad = (GradientDrawable) post_button.getBackground();
        myGrad.setColor(getResources().getColor(R.color.first_theme));// 设置内部颜色

        imgs_ly = (LinearLayout) findViewById(R.id.imgs_ly);
        posters_like = (ImageView) findViewById(R.id.posters_like);
        posters_share = (ImageView) findViewById(R.id.posters_share);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        if(bean.getIs_like().equals("1")){
            posters_like.setImageResource(R.drawable.like_select);
        }else {
            posters_like.setImageResource(R.drawable.like_unselect);
        }

    }

    private void initClick() {
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                particularsAdapter.resetPageIndex();
                httpRequest();
            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                particularsAdapter.addPageIndex();
                httpRequest();
            }
        });


        posters_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 分享
                if (!TextUtils.isEmpty(shareUrl)) {
                    ShareSdkUtil shareSdkUtil = new ShareSdkUtil(spu);
                    shareSdkUtil.shareSDK(Posters_Particulars_Activity.this, shareUrl
                            , getResources().getString(R.string.share_before) + getResources().getString(R.string.app_name)
                                    + getResources().getString(R.string.look_over_stu_poster));
                }

            }
        });


        particularsAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position!=-1){
                    PostersCommentBean.ItemsBean itemBean  = data.get(position);
                    isPostReply = true;
                    comment_ed.setFocusable(true);
                    comment_ed.setFocusableInTouchMode(true);
                    comment_ed.requestFocus();
                    reply_uid = itemBean.getUser_id();
                    if(itemBean.getPid().equals("0")){
                        pid = itemBean.getId();
                    }else {
                        pid = itemBean.getPid();
                    }
                    InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(comment_ed,InputMethodManager.SHOW_FORCED);
                    comment_ed.setText("");
                    comment_ed.setHint(getResources().getString(R.string.reply) +itemBean.getUser().getNickname());
                }
            }
        });


        //提交评论
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(spu.getIsLogin())) {
                    Intent intent = new Intent(Posters_Particulars_Activity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (NetworkUtil.isNetworkAvailable(Posters_Particulars_Activity.this)) {
                        if (TextUitl.containsEmoji(comment_ed.getText().toString().trim())) {
                            snackbarUtils.Short(layout, getResources().getString(R.string.not_support_emoji)).show();
                        } else {
                            if (TextUtils.isEmpty(comment_ed.getText().toString().trim())) {
                                snackbarUtils.Short(layout, getResources().getString(R.string.input_comment_content_hint)).show();
                            } else {
                                httpRequestCommitComment();
                            }
                        }

                    } else {
                        snackbarUtils.Short(layout, getResources().getString(R.string.net_inAvailable)).backColor(getResources().getColor(R.color.bg_red)).show();
                    }
                }
            }
        });

        posters_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用喜欢接口
                if (TextUtils.isEmpty(spu.getIsLogin())) {
                    Intent intent = new Intent(Posters_Particulars_Activity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    int likeNum = Integer.valueOf(bean.getPoster_like());
                    if (bean.getIs_like().equals("1")) {
                        bean.setIs_like("0");
                        bean.setPoster_like((likeNum - 1) + "");
                        posters_like.setImageResource(R.drawable.posters_like);
                        Animation animation = AnimationUtils.loadAnimation(Posters_Particulars_Activity.this, R.anim.img_scale_in);
                        posters_like.startAnimation(animation);
                        httpCanceLike();
                        isResItemLike  =true;
                    } else {
                        bean.setIs_like("1");
                        bean.setPoster_like((likeNum +1) + "");
                        posters_like.setImageResource(R.drawable.posters_like_down);
                        Animation animation = AnimationUtils.loadAnimation(Posters_Particulars_Activity.this, R.anim.img_scale_in);
                        posters_like.startAnimation(animation);
                        httpLike();
                        isResItemLike  =true;
                    }
                    particularsAdapter.notifyDataSetChanged();
                }
            }
        });

        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequest();
            }
        });

    }

    //海报详情接口
    private  void httpParticulars(){
        new HttpGetBuilder(Posters_Particulars_Activity.this)
                .setHttpResult(this)
                .setClassObj(BaseBean.class)
                .setPath(postersId)
                .setUrl(UrlsNew.GET_POSTER)
                .setRequestCode(1003)
                .build();
    }


    //请求评论接口
    private void httpRequest() {
        new HttpGetBuilder(Posters_Particulars_Activity.this)
                .setHttpResult(this)
                .setClassObj(PostersCommentBean.class)
                .setUrl(UrlsNew.GET_POSTER_COMMENT)
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("page",particularsAdapter.getPageIndex()+"")
                .addQueryParams("pageSize",pageSize)
                .addQueryParams("posterId",postersId)
                .addQueryParams("orderBy","create_time desc")
                .setRequestCode(1000)
                .build();
    }


    //提交评论
    private void httpRequestCommitComment() {
        if (comDialog != null && !comDialog.isShowing()) {
            comDialog.show();
        }

        if(!isPostReply){
            new HttpPostBuilder(this)
                    .setHttpResult(this)
                    .setClassObj(BaseBean.class)
                    .setUrl(UrlsNew.GET_POSTER_COMMENT)
                    .setRequestCode(1001)
                    .addBodyParam("user_id",spu.getUid())
                    .addBodyParam("content",comment_ed.getText().toString().trim())
                    .addBodyParam("type","poster")
                    .addBodyParam("pid","0")
                    .addBodyParam("posterId",postersId)
                    .build();
        }else {
            new HttpPostBuilder(this)
                    .setHttpResult(this)
                    .setClassObj(BaseBean.class)
                    .setUrl(UrlsNew.GET_POSTER_COMMENT)
                    .setRequestCode(1001)
                    .addBodyParam("user_id",spu.getUid())
                    .addBodyParam("content",comment_ed.getText().toString().trim())
                    .addBodyParam("type","poster")
                    .addBodyParam("pid",pid)
                    .addBodyParam("reply_uid",reply_uid)
                    .addBodyParam("posterId",postersId)
                    .build();
           }
    }

    //删除海报
    private void httpDeletelPoster() {
        if (comDialog != null && !comDialog.isShowing()) {
            comDialog.show();
        }
        new HttpDeleteBuilder(this).setPath(postersId).setRequestCode(1002).setUrl(UrlsNew.GET_POSTER).setHttpResult(this).build();

    }

    //喜欢
    private void httpLike() {
        new PraisePoster(Posters_Particulars_Activity.this,spu.getUid(),bean.getId());
    }

    //取消喜欢
    private void httpCanceLike() {
        new CancelPraise(Posters_Particulars_Activity.this,spu.getUid(),bean.getId());
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if(requestCode==1000){
            if(comDialog!=null&&comDialog.isShowing()){
                comDialog.cancel();
            }
            loginHideSoftInputWindow();
            myPullRecyclerView.setVisibility(View.VISIBLE);
            jiazai_layout.setVisibility(View.GONE);
            load_fail_layout.setVisibility(View.GONE);
            PostersCommentBean postersCommentBean = (PostersCommentBean) resultBean;
            particularsAdapter.setTotalPage(postersCommentBean.getPaginate().getPageNum());
            particularsAdapter.setPullData(postersCommentBean.getItems());
            if(!TextUtils.isEmpty(spu.getIsLogin())&&bean.getUser().getId().equals(spu.getUid())){
                delItem.setVisible(true);
            }
        }
        if(requestCode==1001){
            isResItemReplyNum = true;
            replayNum ++;
            isPostReply = false;
            particularsAdapter.resetPageIndex();
            int comNum = Integer.parseInt(bean.getComment_count());
            bean.setComment_count(comNum+1+"");
            particularsAdapter.notifyDataSetChanged();
            httpRequest();
        }

        if(requestCode==1002){
            if(comDialog!=null&&comDialog.isShowing()){
                comDialog.cancel();
            }
            ToastUtils.makeText(getApplicationContext(),getResources().getString(R.string.delete_success),Toast.LENGTH_LONG).show();
            setResult(4);
            finish();
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        if(requestCode==1000){
            load_fail_layout.setVisibility(View.VISIBLE);
            jiazai_layout.setVisibility(View.GONE);
            myPullRecyclerView.setVisibility(View.GONE);
            if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                NetworkUtil.httpRestartLogin(Posters_Particulars_Activity.this, layout);
            } else {
                NetworkUtil.httpNetErrTip(Posters_Particulars_Activity.this, layout);
            }
        }

        if(requestCode ==1001){
            ToastUtils.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        }
        if(requestCode==1002){
            ToastUtils.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.poster_particulars_menu, menu);
        delItem = menu.findItem(R.id.action_del);
        delItem.setVisible(false);//默认隐藏
        reportItem = menu.findItem(R.id.action_report);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(isResItemLike){
                    Intent intent =new Intent();
                    intent.putExtra("position",position);
                    setResult(5,intent);
                }

                if(isResItemReplyNum){
                    Intent intent =new Intent();
                    intent.putExtra("position",position);
                    intent.putExtra("replayNum",replayNum);
                    setResult(6,intent);
                }
                finish();
                break;
            case R.id.action_del:
                AlertDialog.Builder builder = new AlertDialog.Builder(Posters_Particulars_Activity.this, R.style.custom_dialog);
                builder.setMessage(getResources().getString(R.string.sure_to_delete));
                builder.setPositiveButton(getResources().getString(R.string.password_dialog_ensure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //调用删除接口
                        dialog.dismiss();
                        httpDeletelPoster();

                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

                break;
            case R.id.action_report:

                if (TextUtils.isEmpty(spu.getIsLogin())) {
                    Intent intent = new Intent(Posters_Particulars_Activity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.makeText(getApplicationContext(),getResources().getString(R.string.report_success), Toast.LENGTH_LONG).show();
                }


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //隐藏键盘
    private void loginHideSoftInputWindow() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        //通过反射方法让图标显示
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(isResItemLike){
                Intent intent =new Intent();
                intent.putExtra("position",position);
                setResult(5,intent);
            }
            if(isResItemReplyNum){
                Intent intent =new Intent();
                intent.putExtra("position",position);
                intent.putExtra("replayNum",replayNum);
                setResult(6,intent);
            }
            finish();
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        if (comDialog != null && comDialog.isShowing()) {
            comDialog.cancel();
        }
        super.onDestroy();
    }
}
