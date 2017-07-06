package com.coder.kzxt.poster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.bumptech.glide.Glide;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.poster.adapter.MyPromotionAdapter;
import com.coder.kzxt.poster.adapter.MyPromotionDelegate;
import com.coder.kzxt.poster.adapter.MyPromotionStageAdapter;
import com.coder.kzxt.poster.adapter.MyPromotionStageDelegate;
import com.coder.kzxt.poster.adapter.PosterTypeDelegate;
import com.coder.kzxt.poster.beans.PosterBeans;
import com.coder.kzxt.poster.beans.PosterCategory;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.recyclerview.decoration.DividerGridItemDecoration;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 海报的首页
 */
public class PosterActivity extends BaseActivity implements HttpCallBack {

    private RelativeLayout my_layout;
    private Toolbar toolbar;
    //搜索的点击跳转区域
    private LinearLayout searchLy;
    private LinearLayout jiazai_layout;

    //海报类型
    private LinearLayout typeLy;
    private MyPullRecyclerView typeGridView;
    private PullRefreshAdapter pullRefreshAdapter;
    private PosterTypeDelegate posterTypeDelegate;
    private List<PosterCategory.ItemsBean> categoryData = new ArrayList<>();

    //下拉刷新 瀑布流和普通的
    private MyPullSwipeRefresh myPullSwipeRefresh, myStagePullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView, myStagePullRecyclerView;


    private MyPromotionDelegate orderListDelegate;
    private MyPromotionAdapter adapter;

    private MyPromotionStageDelegate orderListStageDelegate;
    private MyPromotionStageAdapter stageadapter;


    private List<PosterBeans.ItemsBean> posterData = new ArrayList<>();

    private String pageSize = "20";// 每页请求条数
    private String typeId;
    /**
     * 缓存显示类型 false是单排 true是瀑布流
     */
    private Boolean posterType;


    private SharedPreferencesUtil spu;
    private PermissionsUtil permissionsUtil;
    private  PosterBeans posterBeans;
    private  PosterCategory posterCategory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //获取从其他页面获取的变量 getIntent
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poseter);
        spu = new SharedPreferencesUtil(this);
        //初始化 view findviewbyid
        initFindViewById();
        //需要初始化的数据
        initData();
        //响应事件click
        initClick();
        //网络请求
        httpRequestType();
    }

    private void initFindViewById() {
        my_layout = (RelativeLayout) findViewById(R.id.my_layout);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        jiazai_layout.setVisibility(View.VISIBLE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myStagePullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myStagePullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        myStagePullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myStagePullRecyclerView);

        searchLy = (LinearLayout) findViewById(R.id.searchLy);
        typeLy = (LinearLayout) findViewById(R.id.typeLy);

        typeGridView = (MyPullRecyclerView) findViewById(R.id.typeGridView);
        typeGridView.addItemDecoration(new DividerGridItemDecoration(this, 1));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.poseter));

        posterType = spu.getPostersType();
        invalidateOptionsMenu();

    }

    private void initData() {
        posterTypeDelegate  = new PosterTypeDelegate(this);
        pullRefreshAdapter = new PullRefreshAdapter(PosterActivity.this,categoryData, posterTypeDelegate);
        typeGridView.setAdapter(pullRefreshAdapter);


        typeId = "";
        //普通list的初始化
        orderListDelegate = new MyPromotionDelegate(this);
        adapter = new MyPromotionAdapter(this, posterData, orderListDelegate);
        myPullRecyclerView.setAdapter(adapter);
        adapter.setSwipeRefresh(myPullSwipeRefresh);

        //stage的初始化
        orderListStageDelegate = new MyPromotionStageDelegate(this);
        stageadapter = new MyPromotionStageAdapter(this, posterData, orderListStageDelegate);
        myStagePullRecyclerView.setAdapter(stageadapter);
        stageadapter.setSwipeRefresh(myStagePullSwipeRefresh);
    }

    private void initClick() {

        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.resetPageIndex();
                stageadapter.resetPageIndex();
                httpRequestType();
            }
        });


        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                if(adapter.getPageIndex()<adapter.getTotalPage()){
                    adapter.addPageIndex();
                    stageadapter.addPageIndex();
                    httpRequestPoster();
                }

            }
        });

        myPullRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                typeLy.setVisibility(View.GONE);
                return false;
            }
        });


        myStagePullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               stageadapter.resetPageIndex();
                adapter.resetPageIndex();
                httpRequestType();

            }
        });

        myStagePullRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                typeLy.setVisibility(View.GONE);
                return false;
            }
        });

        myStagePullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                if(stageadapter.getPageIndex()<stageadapter.getTotalPage()){
                    stageadapter.addPageIndex();
                    adapter.addPageIndex();
                    httpRequestPoster();
                }
            }
        });

        searchLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PosterActivity.this, SearchPosterActivity.class));
            }
        });


        stageadapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                typeLy.setVisibility(View.GONE);
                Intent intent = new Intent(PosterActivity.this, Posters_Particulars_Activity.class);
                intent.putExtra("bean", posterData.get(position));
                intent.putExtra("position", position);
                startActivityForResult(intent, 4);
            }
        });

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                typeLy.setVisibility(View.GONE);
                Intent intent = new Intent(PosterActivity.this, Posters_Particulars_Activity.class);
                intent.putExtra("bean", posterData.get(position));
                intent.putExtra("position", position);
                startActivityForResult(intent, 4);
            }
        });

    }

    private void httpRequestType() {
       //调用分类接口
        new HttpGetBuilder(this)
                .setHttpResult(this)
                .setClassObj(PosterCategory.class)
                .setUrl(UrlsNew.GET_POSTER_CATEGORY)
                .setRequestCode(1000)
                .build();

        httpRequestPoster();
    }
    //调用列表接口
    private void httpRequestPoster(){

        HttpGetBuilder httpGetBuilder = new HttpGetBuilder(this);

        httpGetBuilder.setHttpResult(this);
        httpGetBuilder.setClassObj(PosterBeans.class);
        httpGetBuilder.setUrl(UrlsNew.GET_POSTER);
        httpGetBuilder.setRequestCode(1001);
        if(posterType){
            httpGetBuilder.addQueryParams("page",stageadapter.getPageIndex()+"");
        }else {
            httpGetBuilder.addQueryParams("page",adapter.getPageIndex()+"");
        }

        httpGetBuilder.addQueryParams("pageSize",pageSize);
        httpGetBuilder.addQueryParams("orderBy","create_time desc");
        httpGetBuilder.addQueryParams("poster_category_id",typeId);
        httpGetBuilder.build();
      }


    //设置titleBar的内容
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.poseter_menu, menu);
        MenuItem statusMenu = menu.findItem(R.id.menu_status);
        MenuItem typeMenu = menu.findItem(R.id.menu_type);
        MenuItem publishMenu = menu.findItem(R.id.menu_publish);

        //1.alaways:这个值会使菜单项一直显示在ActionBar上。
        //2.ifRoom:如果有足够的空间,这个值会使菜单显示在ActionBar上。
        //3.never:这个值菜单永远不会出现在ActionBar是。
        //4.withText:这个值使菜单和它的图标，菜单文本一起显示。
        MenuItemCompat.setShowAsAction(typeMenu, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setShowAsAction(statusMenu, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setShowAsAction(publishMenu, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_type);
        if (posterType) {
            myPullSwipeRefresh.setVisibility(View.GONE);
            myStagePullSwipeRefresh.setVisibility(View.VISIBLE);
            if(posterBeans!=null){
                stageadapter.setTotalPage(posterBeans.getPaginate().getPageNum());
            }

            item.setIcon(R.drawable.poseter_list);
        } else {
            myPullSwipeRefresh.setVisibility(View.VISIBLE);
            if(posterBeans!=null){
                adapter.setTotalPage(posterBeans.getPaginate().getPageNum());
            }
            myStagePullSwipeRefresh.setVisibility(View.GONE);


            item.setIcon(R.drawable.poseter_water);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    //设置titleBar的点击事件
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (typeLy.getVisibility() == View.VISIBLE) {
                    SetTypeViewGone();
                }else {
                    Glide.get(PosterActivity.this).clearMemory();
                    finish();
                }
                break;

            //海报的何种形态
            case R.id.menu_status:
                JudgeTypeView();
                break;

            //海报的展示的类型 瀑布流还是列表
            case R.id.menu_type:
                posterType = !posterType;
                spu.setPostersType(posterType);
                invalidateOptionsMenu();
                break;
            //发布
            case R.id.menu_publish:

                if (TextUtils.isEmpty(spu.getIsLogin())) {
                    startActivity(new Intent(PosterActivity.this, LoginActivity.class));
                } else {
                    typeLy.setVisibility(View.GONE);
                    permissionsUtil = new PermissionsUtil(PosterActivity.this);
                    if (permissionsUtil.storagePermissions()) {
                        showSelectDialog();
                    }
                }
                break;

            default:
                break;
        }
        return true;
    }

    private void showSelectDialog() {

        final CustomListDialog customDialog = new CustomListDialog(PosterActivity.this);
        customDialog.addData(getResources().getString(R.string.image_word_poster), getResources().getString(R.string.only_words), getResources().getString(R.string.cancel));
        customDialog.show();
        customDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int ps) {
                if (ps == 0) {
                    //获取颜色值
                    String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(PosterActivity.this, R.color.first_theme));
                    //设置最多选择几张图片
                    AndroidImagePicker.getInstance().setSelectLimit(1);
                    AndroidImagePicker.getInstance().pickMulti(PosterActivity.this, true, themeColor, new AndroidImagePicker.OnImagePickCompleteListener() {
                        @Override
                        public void onImagePickComplete(List<ImageItem> items) {
                            try {
                                if (items != null && items.size() > 0) {
                                    // 保存图片到sd卡
                                    String filename = System.currentTimeMillis() + "";
                                    String path = Bimp.saveBitmap(Bimp.revitionImageSize(items.get(0).path), "" + filename);

                                    // 发送图片版海报
                                    Intent intent = new Intent(PosterActivity.this, PublishImagePosterActivity.class);
                                    intent.putExtra("path", path);
                                    intent.putExtra("category", (Serializable) posterCategory.getItems());
                                    startActivityForResult(intent, 4);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else if (ps == 1) {
                    // 发送文字版海报
                    Intent intent = new Intent(PosterActivity.this, PublishTextPosterActivity.class);
                    intent.putExtra("category", (Serializable) posterCategory.getItems());
                    startActivityForResult(intent, 4);
                }

                customDialog.cancel();
            }
        });
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
       if(requestCode==1000){
              posterCategory = (PosterCategory) resultBean;
              pullRefreshAdapter.setPullData(posterCategory.getItems());
              PosterCategory.ItemsBean itemsBean = new PosterCategory.ItemsBean();
              itemsBean.setId(100);
              itemsBean.setName("全部");
              categoryData.add(0,itemsBean);
              pullRefreshAdapter.notifyDataSetChanged();


       }
       if(requestCode==1001){
            jiazai_layout.setVisibility(View.GONE);
            posterBeans = (PosterBeans) resultBean;

           if(posterType){
               stageadapter.setTotalPage(posterBeans.getPaginate().getPageNum());
               stageadapter.setPullData(posterBeans.getItems());
           }else {
               adapter.setTotalPage(posterBeans.getPaginate().getPageNum());
               adapter.setPullData(posterBeans.getItems());
           }

       }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        jiazai_layout.setVisibility(View.GONE);
        NetworkUtil.httpNetErrTip(PosterActivity.this,my_layout);
        if(code== Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
            NetworkUtil.httpRestartLogin(PosterActivity.this,my_layout);
        }
        ToastUtils.makeText(getApplicationContext(),code+msg, Toast.LENGTH_LONG).show();
    }

    //显示类型选择布局
    private void JudgeTypeView() {
        if (typeLy.getVisibility() == View.VISIBLE) {
            SetTypeViewGone();
        } else {
            Animation top_inan_imation = AnimationUtils.loadAnimation(PosterActivity.this, R.anim.enter_dropdown);
            typeGridView.startAnimation(top_inan_imation);
            typeLy.setVisibility(View.VISIBLE);
        }
    }

    //隐藏类型布局
    private void SetTypeViewGone() {
        if (typeLy.getVisibility() == View.VISIBLE) {
            Animation top_out_animation = AnimationUtils.loadAnimation(PosterActivity.this, R.anim.exit_dropup);
            typeGridView.startAnimation(top_out_animation);
            top_out_animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    typeLy.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    //设置poseter的类型 隐藏选择布局 刷新数据
    public void setPoseterType(String name, int id) {
        getSupportActionBar().setTitle(name);
        jiazai_layout.setVisibility(View.VISIBLE);
        if(id==100){
            typeId= "";
        }else {
            typeId = id + "";
        }
        SetTypeViewGone();
        httpRequestType();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (permissionsUtil.permissionsResult(requestCode, permissions, grantResults)) {
            showSelectDialog();

        } else {

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 4 && resultCode == 4) {
            adapter.resetPageIndex();
            stageadapter.resetPageIndex();
            httpRequestType();
        } else if (requestCode == 4 && resultCode == 5) {
//            // 如果点赞 发生变化 进行修改
            int position = data.getIntExtra("position", 0);
               PosterBeans.ItemsBean itemBean =  posterData.get(position);
               int likeNum = Integer.valueOf(itemBean.getPoster_like());
               if (itemBean.getIs_like().equals("1")) {
                   itemBean.setIs_like("0");
                   itemBean.setPoster_like((likeNum - 1) + "");
               } else {
                   itemBean.setIs_like("1");
                   itemBean.setPoster_like((likeNum +1) + "");
               }

            adapter.notifyDataSetChanged();
            stageadapter.notifyDataSetChanged();
        }else if(requestCode == 4 && resultCode == 6){
            int replayNum = data.getIntExtra("replayNum", 0);
            int position = data.getIntExtra("position", 0);
            PosterBeans.ItemsBean itemBean =  posterData.get(position);
            int comNum = Integer.valueOf(itemBean.getComment_count());
            itemBean.setComment_count(comNum+replayNum+"");
            adapter.notifyDataSetChanged();
            stageadapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            if (typeLy.getVisibility() == View.VISIBLE) {
                typeLy.setVisibility(View.GONE);
            }else {
                Glide.get(PosterActivity.this).clearMemory();
                finish();
            }

            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
