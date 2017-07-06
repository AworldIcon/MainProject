package com.coder.kzxt.main.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.InterfaceHttpResult;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.app.utils.L;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.classe.activity.ScanClassActivity;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.dialog.util.CustomDialog;
import com.coder.kzxt.download.DownloadManager;
import com.coder.kzxt.download.DownloadService;
import com.coder.kzxt.download.Download_Center_Activity;
import com.coder.kzxt.download.NotificationUpdateService;
import com.coder.kzxt.http.utils.CheckVersion;
import com.coder.kzxt.im.business.ImLoginBusiness;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.main.beans.BottomBar;
import com.coder.kzxt.main.fragment.AllCourseFragment;
import com.coder.kzxt.main.fragment.DiscoveryFragment;
import com.coder.kzxt.main.fragment.LivePlayingFragment;
import com.coder.kzxt.main.fragment.MainFragment;
import com.coder.kzxt.main.fragment.PersonCentreFragment;
import com.coder.kzxt.message.activity.MessageMainActivity;
import com.coder.kzxt.setting.beans.CheckVersionBean;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.FileUtil;
import com.coder.kzxt.utils.MyApplication;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.PublicUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.views.CustomNewDialog;
import com.coder.kzxt.webview.activity.TextView_Link_Activity;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMUserStatusListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener, InterfaceHttpResult, HttpCallBack
{
    private Integer i ;
    private static final int HTTP_RESULT_CODE_TEST = 1000;
    private BottomNavigationBar bottomNavigationBar;
    private FrameLayout frameLayout;
    private ArrayList<Fragment> fragments;
    private MainFragment mainFragment;
    private AllCourseFragment allCourseFragment;
    private DiscoveryFragment discoveryFragment;
    private PersonCentreFragment personCentreFragment;
    private LivePlayingFragment livePlayingFragment;
    private SharedPreferencesUtil spu;
    private Fragment mContent;
    private Toolbar mToolbarView;
    private SharedPreferencesUtil preferencesUtil;
    private PermissionsUtil permissionsUtil;
    private CustomNewDialog dialog;
    //    private List<String> chan = new ArrayList<>();//渠道表示，是否显示广告
    private DownloadManager downloadManager;
    private long mExitTime = 0;
    private List<BottomNavigationItem> navigationItemList = new ArrayList<>();
    private HashMap<String, Fragment> fragmentHashMap = new HashMap<>();//存放本地五种fragment
    private List<BottomBar.ItemsBean> nameList = new ArrayList<>();//存放服务器返回的bar类型
    private List<Fragment> myfragments = new ArrayList<>();//存放实际显示的fragment
    private boolean un_sucess = true;
    private MenuItem scanItem;  //扫码框
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 200:
                    //网络加载超过一秒执行
                    if (un_sucess)
                    {
                        un_sucess = false;
                        readBottomBarResourse();
                        updateBottomBar();
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spu = new SharedPreferencesUtil(this);
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarView);
        frameLayout = (FrameLayout) findViewById(R.id.bottom_nav_content);
        permissionsUtil = new PermissionsUtil(this);
        preferencesUtil = new SharedPreferencesUtil(this);

        if (permissionsUtil.storagePermissions())
        {
            // 自动往外置卡创建文件夹
            this.getExternalFilesDir(null);
            // 创建临时图片文件夹
            File dir = new File(Constants.POST_ORIGINAL_PHOTO);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            //把开屏广告图片存储到本地
            if (!TextUtils.isEmpty(preferencesUtil.getSpread()))
            {
                Bimp.saveImgToSd(preferencesUtil.getSpread(), Constants.SPREAD);
            }

        }
        downloadManager = DownloadService.getDownloadManager(MainActivity.this);
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setNavigationIcon(R.drawable.qrcode_sao);
        fragments = new ArrayList<>();
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        //Bottom navigation，有两种Mode：分别是Fixed 固定大小 Shifting不固定大小
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        getFirstPageRequst();
        bottomNavigationBar.setTabSelectedListener(this);


        ArrayList<Callback.Cancelable> getallhandler = downloadManager.getAllCacebles();
        ArrayList<String> query_All_DownStatus = DataBaseDao.getInstance(MainActivity.this).query_All_DownStatus();
        // 如果getallhandler.size()为0并且有数据库中所有视频下载状态有1存在一定是下载线程自动销毁，需要继续下载该视频
        if (getallhandler.size() == 0 && query_All_DownStatus.contains("1"))
        {
            HashMap<String, String> query_Downing_Download = DataBaseDao.getInstance(MainActivity.this).query_Downing_Download();
            downloadManager.addCourseVideoDownload(query_Downing_Download.get("url_content"), query_Downing_Download.get("treeid"), query_Downing_Download.get("tid")
                    , query_Downing_Download.get("id"), Integer.parseInt(query_Downing_Download.get("mapKey"))
                    , query_Downing_Download.get("name"), Long.parseLong(query_Downing_Download.get("downloadedSize")), query_Downing_Download.get("video_type"));
        }

        //检查版本
        //httpCheckVersion();
        //
//        httpCutSpread();
        //im相关
        im();

    }


    private void im()
    {
        //互踢下线逻辑
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener()
        {
            @Override
            public void onForceOffline()
            {
//                if(UserInfo.getInstance().getId() != null)
//                {
//                    //抢登
//                    ImLoginBusiness.getInstance(MainActivity.this).logout(false);
//                }
            }

            @Override
            public void onUserSigExpired()
            {
                //票据过期，需要重新登录
                new ImLoginBusiness().logout(true);
            }
        });


        //注册接收新消息
        TIMManager.getInstance().addMessageListener(new TIMMessageListener()
        {
            @Override
            public boolean onNewMessages(List<TIMMessage> list)
            {
                initUnReadMessage();
                return false;
            }
        });

    }


    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        StringBuffer idStr = new StringBuffer();
        ArrayList<HashMap<String, String>> ids = spu.getlessinIdList();
        if (ids.size() > 0)
        {
            for (int i = 0; i < ids.size(); i++)
            {
                HashMap<String, String> map = ids.get(i);
                String id = map.get("id");
                if (ids.size() > 1)
                {
                    if (i == ids.size() - 1)
                    {
                        idStr.append(id);
                    } else
                    {
                        idStr.append(id).append(",");
                    }
                } else
                {
                    idStr.append(id);
                }

            }
            L.v("tangcy", "上报的视频id" + idStr.toString());
            httpPostVideoId(idStr.toString());
        }

        initUnReadMessage();
        //判断是否需要显示
        if(spu.getIsLogin().equals("1")){
            getNoticeUnReadNum();
        }else {
            showUnReadNumber = false;
            invalidateOptionsMenu();
        }

        //检测百度推送是否断开
        super.onResume();
    }

    private Boolean showUnReadNumber;

    //监控未读取的数量
    private void initUnReadMessage()
    {
        showUnReadNumber = false;
        if (spu.getIsLogin().equals("1"))
        {
            List<TIMConversation> list = TIMManager.getInstance().getConversionList();
            for (TIMConversation conversation : list)
            {
                // 私聊  群聊
                if (conversation.getType() == TIMConversationType.C2C || conversation.getType() == TIMConversationType.Group)
                {
                    if (conversation.getUnreadMessageNum() > 0)
                    {
                        showUnReadNumber = true;
                        break;
                    }
                }
            }
        }

        invalidateOptionsMenu();

    }
    //获取用户通知已读数量
    private void getNoticeUnReadNum(){
        showUnReadNumber = false;
        new HttpGetBuilder(this).setUrl(UrlsNew.GET_NOTIFY_COUNT).setClassObj(null).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                try {
                    int noticeNum;
                    JSONObject object=new JSONObject(resultBean.toString());
                    noticeNum=object.getJSONObject("item").getInt("num");
                    if(noticeNum>0){
                        showUnReadNumber = true;
                    }
                    invalidateOptionsMenu();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {

            }
        }).build();
    }

    //上报看完视频的id
    private void httpPostVideoId(String ids)
    {
        new HttpPostBuilder(MainActivity.this).setClassObj(BaseBean.class).setHttpResult(new HttpCallBack()
        {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean)
            {
                spu.clearReceiveList();
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg)
            {

            }
        }).setUrl(UrlsNew.POST_LESSON_FINISH)
                .addBodyParam("id", ids)
                .build();
    }


    //实际网络请求后拿到数据操作
    private void getFirstPageRequst()
    {
        handler.sendEmptyMessageDelayed(200, 1200);
        new HttpGetBuilder(MainActivity.this).setHttpResult(this).setClassObj(BottomBar.class).setRequestCode(2010).setUrl(UrlsNew.BOTTOM_BAR).build();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        if (requestCode == 2010)
        {
            handler.removeMessages(200);
            BottomBar bottomBar = (BottomBar) resultBean;
            String bottomModelEn = "";
            String bottomModelCn = "";
            for (int i = 0; i <  bottomBar.getItems().size(); i++)
            {
                bottomModelEn +=  bottomBar.getItems().get(i).getName_en() + ",";
                bottomModelCn +=  bottomBar.getItems().get(i).getName_cn() + ",";
            }
            //先判断是否符合条件再存入本地
            if (!TextUtils.isEmpty(bottomModelEn) && bottomModelCn.split(",").length == bottomModelEn.split(",").length &&  bottomBar.getItems().size() > 0)
            {
                preferencesUtil.setBottomBarModelEn(bottomModelEn);
                preferencesUtil.setBottomBarModelCn(bottomModelCn);
            }

            //1秒之内的成功执行
            if (un_sucess)
            {
                un_sucess = false;
                //然后从本地获取(因为请求成功有可能获取的数据有问题)，本地存放的才是符合条件的
                readBottomBarResourse();
                L.d("zw--", "sucess" + nameList.size());
                updateBottomBar();
            }

        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        //1秒之内的网络加载失败执行
        if (un_sucess)
        {
            un_sucess = false;
            readBottomBarResourse();
            updateBottomBar();
        }
    }

    //读取本地存储的数据给nameList赋值
    private void readBottomBarResourse()
    {
        //清空一下,读取本地重新赋值
        nameList.clear();
        String bottomEn = preferencesUtil.getBottomBarModelEn();
        String bottomCn = preferencesUtil.getBottomBarModelCn();
        String[] modelEn = bottomEn.split(",");
        String[] modelCn = bottomCn.split(",");
        for (int i = 0; i < modelEn.length; i++)
        {
            BottomBar.ItemsBean itemsBean = new BottomBar.ItemsBean();
            itemsBean.setName_en(modelEn[i]);
            itemsBean.setName_cn(modelCn[i]);
            nameList.add(itemsBean);
        }
    }

    //请求成功后调取和请求失败后调取此方法
    private void updateBottomBar()
    {
        //此处namelist从本地文件中得到的内容
        for (int i = 0; i < nameList.size(); i++)
        {
            switch (nameList.get(i).getName_en())
            {
                case "HOMEPAGE":
                    navigationItemList.add(new BottomNavigationItem(R.drawable.tab_main_up, nameList.get(i).getName_cn())
                            .setInActiveColorResource(R.color.font_dark_black)
                            .setActiveColorResource(R.color.first_theme));
                    break;
                case "CATEGORY":
                    navigationItemList.add(new BottomNavigationItem(R.drawable.tab_classify_up, nameList.get(i).getName_cn())
                            .setInActiveColorResource(R.color.font_dark_black)
                            .setActiveColorResource(R.color.first_theme));
                    break;
                case "DISCOVER":
                    navigationItemList.add(new BottomNavigationItem(R.drawable.tab_posters_up, nameList.get(i).getName_cn())
                            .setInActiveColorResource(R.color.font_dark_black)
                            .setActiveColorResource(R.color.first_theme));
                    break;
                case "ME":
                    navigationItemList.add(new BottomNavigationItem(R.drawable.tab_my_up, nameList.get(i).getName_cn())
                            .setInActiveColorResource(R.color.font_dark_black)
                            .setActiveColorResource(R.color.first_theme));
                    break;
                case "LIVE":
                    navigationItemList.add(new BottomNavigationItem(R.drawable.tab_my_live, nameList.get(i).getName_cn())
                            .setInActiveColorResource(R.color.font_dark_black)
                            .setActiveColorResource(R.color.first_theme));
                    break;
                default:
                    break;
            }
        }

        for (int i = 0; i < navigationItemList.size(); i++)
        {
            bottomNavigationBar.addItem(navigationItemList.get(i));
        }
        L.d("zw--listsize-",nameList.size()+"**"+navigationItemList.size());
        bottomNavigationBar.setFirstSelectedPosition(0)
                .initialise();

        mainFragment = MainFragment.newInstance(getResources().getString(R.string.main_page));
        allCourseFragment = AllCourseFragment.newInstance(getResources().getString(R.string.main_classify));
        discoveryFragment = DiscoveryFragment.newInstance(getResources().getString(R.string.main_find));
        personCentreFragment = PersonCentreFragment.newInstance(getResources().getString(R.string.main_i));
        livePlayingFragment = LivePlayingFragment.newInstance(getResources().getString(R.string.live));

        fragmentHashMap.put("HOMEPAGE", mainFragment);
        fragmentHashMap.put("CATEGORY", allCourseFragment);
        fragmentHashMap.put("DISCOVER", discoveryFragment);
        fragmentHashMap.put("ME", personCentreFragment);
        fragmentHashMap.put("LIVE", livePlayingFragment);
        //将实际显示的fragment存入集合
        for (int i = 0; i < nameList.size(); i++)
        {
            if (fragmentHashMap.containsKey(nameList.get(i).getName_en()))
            {
                myfragments.add(fragmentHashMap.get(nameList.get(i).getName_en()));
            }
        }
        fragments = getFragments();
        setDefaultFragment();
    }

    //请求插屏广告
//    private void httpCutSpread()
//    {
//        new HttpGetOld(MainActivity.this, MainActivity.this, MainActivity.this
//                , CutSpreadBean.class, Urls.CUT_SPREAD).excute(1);
//    }

    private ArrayList<Fragment> getFragments()
    {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < myfragments.size(); i++)
        {
            fragments.add(myfragments.get(i));
        }
        return fragments;
    }

    private void setDefaultFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.bottom_nav_content, myfragments.get(0));
        transaction.show(myfragments.get(0));
        transaction.commitAllowingStateLoss();
        mContent = myfragments.get(0);
        bottomNavigationBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTabSelected(int position)
    {
        if (mContent != fragments.get(position))
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            if (!fragments.get(position).isAdded())
            { // 先判断是否被add过
                transaction.hide(mContent).add(R.id.bottom_nav_content, fragments.get(position)).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(mContent).show(fragments.get(position)).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }

        }

        mContent = fragments.get(position);
        setMyFragment(mContent);
    }

    private void setMyFragment(final Fragment mContent)
    {
        if (mContent instanceof PersonCentreFragment)
        {
            mToolbarView.setClickable(true);
            mToolbarView.setTitle(getResources().getString(R.string.sets));
            ((PersonCentreFragment) mContent).setLoginState();
            mToolbarView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    ((PersonCentreFragment) mContent).onSetClick();
                }
            });
        } else
        {
            mToolbarView.setTitle(getResources().getString(R.string.app_name));
            mToolbarView.setClickable(false);
        }
        controlScanDialog(mContent);
    }

    private void controlScanDialog(Fragment mContent) {
        if(mContent != null){
            if (mContent instanceof MainFragment) {
                mToolbarView.setNavigationIcon(R.drawable.qrcode_sao);
            } else {
                mToolbarView.setNavigationIcon(null);
            }
        }
    }


    @Override
    public void onTabUnselected(int position)
    {
    }

    @Override
    public void onTabReselected(int position)
    {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        MenuItem downloadItem = menu.findItem(R.id.menu_download);
        MenuItem messageItem = menu.findItem(R.id.menu_message);
        if (showUnReadNumber)
        {
            messageItem.setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.message_red));
        } else
        {
            messageItem.setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.message));
        }

        //1.alaways:这个值会使菜单项一直显示在ActionBar上。
        //2.ifRoom:如果有足够的空间,这个值会使菜单显示在ActionBar上。
        //3.never:这个值菜单永远不会出现在ActionBar是。
        //4.withText:这个值使菜单和它的图标，菜单文本一起显示。
        MenuItemCompat.setShowAsAction(searchItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setShowAsAction(downloadItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setShowAsAction(messageItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.menu_search:
                startActivity(new Intent(MainActivity.this, SearchAllActivity.class));
                //           startActivity(new Intent(MainActivity.this, InformationListActivity.class));
                //设置要添加，加载失败的父布局
//                showLoadFailView(frameLayout);
//                //设置加载失败button的点击事件
//                setOnLoadFailClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        Utils.makeToast(MainActivity.this, "重新加载");
//                        hideLoadFailView();
//                    }
//                });
//                //获取颜色值
//                String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(MainActivity.this, R.color.first_theme));
//                //设置最多选择几张图片
//                AndroidImagePicker.getInstance().setSelectLimit(1);
//                AndroidImagePicker.getInstance().pickCamera(MainActivity.this, themeColor, new AndroidImagePicker.OnImagePickCompleteListener()
//                {
//                    @Override
//                    public void onImagePickComplete(List<ImageItem> items)
//                    {
//
//                        try
//                        {
//                            if (items != null && items.size() > 0)
//                            {
//                                L.d("onImagePickComplete:  " +items.size());
//                            }
//                        } catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
                break;

            case R.id.menu_download:

                if (TextUtils.isEmpty(preferencesUtil.getIsLogin()))
                {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else
                {
                    Intent intent = new Intent(MainActivity.this, Download_Center_Activity.class);
                    startActivity(intent);
                }
                break;
            case R.id.menu_message:

                if (TextUtils.isEmpty(preferencesUtil.getIsLogin())) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    startActivity(new Intent(MainActivity.this, MessageMainActivity.class));
                }

                break;
            case android.R.id.home: //扫一扫
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ScanClassActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void getCallback(int requestCode, int code, String msg, Object baseBean)
    {
//        if (requestCode == 1)
//        {
//            if (code == Constants.HTTP_CODE_SUCCESS)
//            {
//                CutSpreadBean spreadBean = (CutSpreadBean) baseBean;
//
//                String image = spreadBean.getData().getImage();
//                String startTime = spreadBean.getData().getStartTime();
//                String endTime = spreadBean.getData().getEndTime();
//                int periodStart = spreadBean.getData().getPeriodStart();
//                int periodEnd = spreadBean.getData().getPeriodEnd();
//                String isClosed = spreadBean.getData().getIsClosed();
//                String joinUrl = spreadBean.getData().getJoinUrl();
//
//                if (!TextUtils.isEmpty(isClosed) && isClosed.equals("0")
//                        && (Integer.parseInt(startTime) - System.currentTimeMillis() / 1000 < 0 && Integer.parseInt(endTime) - System.currentTimeMillis() / 1000 > 0)
//                        && (periodStart - System.currentTimeMillis() / 1000 < 0 && periodEnd - System.currentTimeMillis() / 1000 > 0)
//                        && ((chan.size() > 0 && chan.contains(ChannelUtil.getChannel(MainActivity.this))) || ChannelUtil.getChannel(MainActivity.this).equals("market_yd_guanwang")))
//                {
//                    //满足条件显示广告
//                    showCutImage(image, joinUrl);
//                }
//            }
//        }
    }

    private void showCutImage(String imgUrl, final String joinUrl)
    {
        dialog = new CustomNewDialog(MainActivity.this, R.layout.cut_ad_dialog);
        dialog.setCanceledOnTouchOutside(false);
        final ImageView cut_ad = (ImageView) dialog.findViewById(R.id.cut_ad);
        TextView close_ad = (TextView) dialog.findViewById(R.id.close_ad);

        Glide.with(MainActivity.this).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>()
        {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
            {
                cut_ad.setImageBitmap(resource);
                dialog.show();
            }
        });

        close_ad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.cancel();
            }
        });
        cut_ad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String ul = "";
                if (!TextUtils.isEmpty(joinUrl))
                {
                    if (joinUrl.startsWith("http://") || joinUrl.startsWith("https://"))
                    {
                        ul = joinUrl;
                    } else
                    {
                        ul = "http://" + joinUrl;
                    }
                    Intent intent = new Intent(MainActivity.this, TextView_Link_Activity.class);
                    intent.putExtra("web_url", ul);
                    intent.putExtra("title", "");
                    startActivity(intent);
                }
            }
        });

    }

    private void httpCheckVersion()
    {
        new CheckVersion(MainActivity.this, new HttpCallBack()
        {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                CheckVersionBean checkVersionBean = (CheckVersionBean) resultBean;
                String name = checkVersionBean.getItem().getName();// 名称
                String version = checkVersionBean.getItem().getVersion(); // 版本
                String function = checkVersionBean.getItem().getFunction();// 新加功能
                //String publicTm = checkVersionBean.getItem().getPublicTm(); // 发布时间
                final String url = checkVersionBean.getItem().getUrl(); // 下载页面
                final int isForceUpdate = checkVersionBean.getItem().getIsForceUpdate(); // 是否强制升级

                int versionCodetwo = PublicUtils.getVersionCodetwo(MainActivity.this);
                double parseDouble = Double.parseDouble(version);
                double anotherDoubleValue = Math.floor(parseDouble);
                int intValue = (int) anotherDoubleValue;
                // 如果本地版本小于服务器版本提示升级
                if (versionCodetwo < intValue)
                {
                    final CustomDialog customDialog = new CustomDialog(MainActivity.this);
                    customDialog.setTitleVisibility(getResources().getString(R.string.update_warn));
                    //customDialog.setMessage(publicTm);

                    if (isForceUpdate == 1)
                    {
                        customDialog.setCanceledOnTouchOutside(false);
                        customDialog.setCancelable(false);
                    } else
                    {
                        customDialog.setCanceledOnTouchOutside(true);
                        customDialog.setCancelable(true);
                    }
                    if (!TextUtils.isEmpty(function))
                    {
                        customDialog.setMessage(function);
                    }

                    if (isForceUpdate == 0)
                    {
                        customDialog.setRightText(getResources().getString(R.string.update_next));
                    } else
                    {
                        customDialog.setRightText(getResources().getString(R.string.exit_app));
                    }
                    customDialog.setLeftText(getResources().getString(R.string.update_now));
                    customDialog.show();

                    customDialog.setLeftClick(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(MainActivity.this, NotificationUpdateService.class);
                            intent.putExtra("apkUrl", url);
                            startService(intent);
                            if (isForceUpdate == 0)
                            {
                                customDialog.dismiss();
                            } else
                            {
                            }
                            preferencesUtil.setShowLauncher(false);
                        }
                    });

                    customDialog.setRightClick(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            // 判断是否强制升级
                            if (isForceUpdate == 1)
                            {
                                MyApplication.getInstance().exit();
                            } else
                            {
                                customDialog.dismiss();
                            }
                        }
                    });
                }
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {

            }

        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        if (permissionsUtil.permissionsResult(requestCode, permissions, grantResults))
        {
            // 自动往外置卡创建文件夹
            this.getExternalFilesDir(null);
            // 创建临时图片文件夹
            File dir = new File(Constants.POST_ORIGINAL_PHOTO);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            //把开屏广告图片存储到本地
            if (!TextUtils.isEmpty(preferencesUtil.getSpread()))
            {
                Bimp.saveImgToSd(preferencesUtil.getSpread(), Constants.SPREAD + "wel_ad.jpg");
            }

        } else
        {

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 停止下载
     */
    private void stop_Dwonload()
    {
        ArrayList<Callback.Cancelable> cancelables = downloadManager.getAllCacebles();
        if (cancelables.size() >= 0)
        {
            for (int i = 0; i < cancelables.size(); i++)
            {
                Callback.Cancelable cancelable = cancelables.get(i);
                if (cancelable != null && !cancelable.isCancelled())
                {
                    cancelable.cancel();
                }
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void destroyResource()
    {
        FileUtil.deleteFolder(Constants.POST_ORIGINAL_PHOTO);
        FileUtil.deleteFolder(Constants.RECORD);
        // 销毁高德地图
//        MyApplication.mapView.onDestroy();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {

            if ((System.currentTimeMillis() - mExitTime) > 2000)
            {

                ToastUtils.makeText(getApplicationContext(), getResources().getString(R.string.clickagain_to_exit_app), Toast.LENGTH_SHORT).show();
                // 更新mExitTime
                mExitTime = System.currentTimeMillis();
            } else
            {
                ArrayList<String> query_All_DownStatus = DataBaseDao.getInstance(this).query_All_DownStatus();
                if (query_All_DownStatus.contains("1"))
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.custom_dialog);
                    builder.setTitle(getResources().getString(R.string.download_warn));
                    builder.setMessage(getResources().getString(R.string.downloading_video));
                    builder.setPositiveButton(getResources().getString(R.string.shut_down_loading), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            stop_Dwonload();
                            HashMap<String, String> query_Downing_Download = DataBaseDao.getInstance(MainActivity.this).query_Downing_Download();
                            String download_id = query_Downing_Download.get("id");
                            DataBaseDao.getInstance(MainActivity.this).updata_DownStatus(download_id, 0);// 中断下载把在这状态改为等待状态
                            // 停止服务
                            DownloadService.stopService(MainActivity.this);
                            dialog.dismiss();
                            // finish();
                            destroyResource();
//							updateSetCacheListener.onMainUpSetCache();
                            moveTaskToBack(true);
                        }
                    });

                    builder.setNegativeButton(getResources().getString(R.string.download_back), new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            destroyResource();
//							updateSetCacheListener.onMainUpSetCache();
                            moveTaskToBack(true);
                            // finish();
                        }
                    });
                    AlertDialog dlg = builder.create();
                    dlg.show();
                } else
                {
                    boolean serviceRunning = DownloadService.isServiceRunning(MainActivity.this);
                    if (serviceRunning)
                    {
                        // 停止服务
                        DownloadService.stopService(MainActivity.this);
                    }
                    // finish();
                    destroyResource();
//					updateSetCacheListener.onMainUpSetCache();
                    moveTaskToBack(true);
                }
            }
            return true;
        }
        return false;
    }

}
