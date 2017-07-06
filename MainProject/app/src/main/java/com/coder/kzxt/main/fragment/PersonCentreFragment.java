package com.coder.kzxt.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPostFileBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.UserInfoBean;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.classe.activity.ClassListActivity;
import com.coder.kzxt.classe.activity.MyClassStudentActivity;
import com.coder.kzxt.course.activity.LearnCourseActivity;
import com.coder.kzxt.course.activity.TeachingCourseActivity;
import com.coder.kzxt.download.Download_Center_Activity;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.login.activity.UserInfoActivity;
import com.coder.kzxt.login.beans.AvatarBean;
import com.coder.kzxt.login.beans.UserInfoResult;
import com.coder.kzxt.main.adapter.MyBottomAdapter;
import com.coder.kzxt.main.adapter.MyTopAdapter;
import com.coder.kzxt.main.beans.MyBean;
import com.coder.kzxt.main.mInterface.OnMyItemInterface;
import com.coder.kzxt.order.activity.MyOrderActivity;
import com.coder.kzxt.poster.activity.MyPosterActivity;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.service.activity.MyServiceActivity;
import com.coder.kzxt.setting.activity.SettingActivity;
import com.coder.kzxt.stuwork.activity.StuCourseWorkActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.views.HeaderWaveHelper;
import com.coder.kzxt.views.HeaderWaveView;
import com.coder.kzxt.views.ObservableScrollView;
import com.coder.kzxt.views.WrapGridLayoutManager;
import com.coder.kzxt.views.WrapLinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * 我的页面
 */
public class PersonCentreFragment extends BaseFragment implements OnMyItemInterface,SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    private Context mainActivity;
    private SharedPreferencesUtil spu;

    private ImageView userHead; //用户头像
    private ImageView userHeadBg; //头像背景
    private TextView userName; //用户名

    private RelativeLayout orderLayout;   //订单
    private RelativeLayout dowlondLayout;  //下载
    private RelativeLayout teachingCourseLayout; //在学课程

//    private RelativeLayout walletLayout; //钱包
//    private RelativeLayout serviceLayout; //服务
//    private RelativeLayout posterLayout; //海报
//    private RelativeLayout onlineCourseLayout; //在线教程
//    private RelativeLayout collectLayout; //收藏

    private RelativeLayout userTopLayput;
    private View line;

    private ObservableScrollView scrollView;
    private HeaderWaveView waveView;
    private RelativeLayout userHeadLayout;  //用户头像
    public static int LAYOUT_ITEM_NUM = 4;
    private View myLayout;
    private HeaderWaveHelper mHeaderWaveHelper;
    private MyPullSwipeRefresh myRefresh;

    private RelativeLayout topView;
    private RelativeLayout bottomView;

    private RecyclerView  topRecyclerView; //上部view
    private MyTopAdapter topAdapter;

    private RecyclerView  bottomRecyclerView; //下部view
    private MyBottomAdapter bottomAdapter;

    private LinearLayout bottomLayout;
    private String headUrl;

    private enum Item{
        TEST,HOMEWORK,ANSWER,CLASS,
        TEACHING,DOWNLOAD,POSTER,SERVICE,
        ORDER,WALLET,COLLECT,STUDYING,
    }

    public static PersonCentreFragment newInstance(String param) {
        PersonCentreFragment fragment = new PersonCentreFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(mainActivity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_person_new_center, container, false);
            initView();
            initListener();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initView() {
        scrollView = (ObservableScrollView)rootView.findViewById(R.id.my_scroll);
        waveView = (HeaderWaveView) rootView.findViewById(R.id.header_wave_view);
        //用户头像
        userHeadLayout = (RelativeLayout) rootView.findViewById(R.id.rl_user_head);
        userHead = (ImageView) rootView.findViewById(R.id.iv_user_head);
        userHeadBg = (ImageView) rootView.findViewById(R.id.bg_user_image);
        userHeadBg.getBackground().setAlpha(200);
        userName = (TextView) rootView.findViewById(R.id.user_img_name);

//        onlineCourseLayout = (RelativeLayout) rootView.findViewById(R.id.rl_online_course);
        teachingCourseLayout = (RelativeLayout) rootView.findViewById(R.id.rl_teaching_course);
        myRefresh = (MyPullSwipeRefresh) rootView.findViewById(R.id.myPullSwipeRefresh);

        dowlondLayout = (RelativeLayout) rootView.findViewById(R.id.rl_my_down);
//        posterLayout = (RelativeLayout) rootView.findViewById(R.id.rl_my_posters);
//        serviceLayout = (RelativeLayout) rootView.findViewById(R.id.rl_my_service);
        orderLayout = (RelativeLayout) rootView.findViewById(R.id.rl_my_order);
//        walletLayout = (RelativeLayout) rootView.findViewById(R.id.rl_my_wallet);
//        collectLayout = (RelativeLayout) rootView.findViewById(R.id.rl_my_collect);
        line = rootView.findViewById(R.id.line);

        topView = (RelativeLayout) rootView.findViewById(R.id.rl_top_view);
        bottomLayout = (LinearLayout) rootView.findViewById(R.id.ll_my_bottom);
        bottomView = (RelativeLayout) rootView.findViewById(R.id.rl_bottom_layout);
        userTopLayput = (RelativeLayout) rootView.findViewById(R.id.user_img_layout);

        topRecyclerView = (RecyclerView) rootView.findViewById(R.id.top_recycler_view);
        topRecyclerView.setLayoutManager(new WrapGridLayoutManager(mainActivity,LAYOUT_ITEM_NUM));
        ((SimpleItemAnimator)topRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        bottomRecyclerView = (RecyclerView) rootView.findViewById(R.id.bottom_recycler_view);
        bottomRecyclerView.setLayoutManager(new WrapLinearLayoutManager(mainActivity));
        ((SimpleItemAnimator)bottomRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
//        bottomRecyclerView.addItemDecoration(new DividerItemDecoration(mainActivity,DividerItemDecoration.VERTICAL_LIST));
        myLayout = rootView.findViewById(R.id.my_layout);
        String themeColor = Integer.toHexString(getResources().getColor(R.color.first_theme));
        String  newColor = "#4D"+themeColor.substring(2,themeColor.length());
        mHeaderWaveHelper = new HeaderWaveHelper(waveView, Color.parseColor(newColor), getResources().getColor(R.color.first_theme),null);
        mHeaderWaveHelper.start();
    }

    private void initListener() {
        myRefresh.setOnRefreshListener(PersonCentreFragment.this);

        // 账户具体信息
        userHeadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUserInfo();
            }
        });

        // 下载
        dowlondLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDownload();
            }
        });

        // 我的课程
        teachingCourseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMyCourse();

            }
        });

        //订单
        orderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMyOrder();
            }
        });


//        scrollView.setOnScrollChangeListener(new ScrollViewInterface() {
//            @Override
//            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//                if (scrollView.getScrollY() > 100) {
//                    mHeaderWaveHelper.cancel();
//                } else {
//                    mHeaderWaveHelper.start();
//                }
//            }
//        });

    }


    private void startTask() {
                String role = spu.getUserRole();
                new HttpGetBuilder(mainActivity)
                .setUrl(UrlsNew.GET_APP_ME)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        myRefresh.setRefreshing(false);
                        if (requestCode == 0) {
                            MyBean bean = (MyBean) resultBean;
                            MyBean.ItemBean item = bean.getItem();
                            if (item != null) {
                                adapterData(item);
                            } else {
                                noDataPage();
                            }
                        } else {
                            staticVisibility();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        myRefresh.setRefreshing(false);
                        staticVisibility();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(PersonCentreFragment.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mainActivity, myLayout);
                        }
                    }
                })
                .setClassObj(MyBean.class)
                 .addQueryParams("role", role)
                .build();
    }

    /**
     * 无数据页面
     */
    private void noDataPage() {
        topView.setVisibility(View.GONE);
        bottomView.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        userTopLayput.setLayoutParams(new LinearLayout.
                LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 适配数据
     * @param item
     */
    private void adapterData(MyBean.ItemBean item) {
        ArrayList<MyBean.ChildItemBean> footer = item.getFOOTER();
        ArrayList<MyBean.ChildItemBean> topic = item.getTOPIC();
        if((footer != null && footer.size() > 0) ){
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
        adapterTopData(topic);
        for (int i = 0; i < footer.size(); i++) {
            MyBean.ChildItemBean itemBean = footer.get(i);
            if (itemBean.getName().equals("WALLET")) {
                footer.remove(itemBean);
            }
        }
        adapterBottomData(footer);
    }

    /**
     * 静态显示
     */
    private void staticVisibility() {
        topView.setVisibility(View.GONE);
        bottomView.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.VISIBLE);
        userTopLayput.setLayoutParams(new LinearLayout.
                LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 动态显示
     */
    private void dynamicVisibility() {
        topView.setVisibility(View.VISIBLE);
        bottomView.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.GONE);
    }

    /**
     * 适配顶部数据
     * @param topics
     */
    private void adapterTopData( ArrayList<MyBean.ChildItemBean> topics) {
        if (topics != null && topics.size() > 0) {
            topView.setVisibility(View.VISIBLE);
            topView.setVisibility(View.VISIBLE);
            LAYOUT_ITEM_NUM = topics.size();
            topRecyclerView.setLayoutManager(new WrapGridLayoutManager(mainActivity, LAYOUT_ITEM_NUM));
            topAdapter = new MyTopAdapter(mainActivity, topics);
            topRecyclerView.setAdapter(topAdapter);
            topAdapter.setTopItemClickListener(PersonCentreFragment.this);
        } else {
            topView.setVisibility(View.GONE);
            userTopLayput.setLayoutParams(new LinearLayout.
                    LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * 适配底部数据
     * @param footers
     */
    private void adapterBottomData(ArrayList<MyBean.ChildItemBean> footers) {
        bottomLayout.setVisibility(View.GONE);
        if (footers != null && footers.size() > 0) {
            bottomView.setVisibility(View.VISIBLE);
            if (bottomAdapter == null) {
                bottomAdapter = new MyBottomAdapter(mainActivity, footers);
                bottomRecyclerView.setAdapter(bottomAdapter);
                bottomAdapter.setBottomItemClickListener(PersonCentreFragment.this);
            } else {
                bottomAdapter.setBottomItemData(footers);
                bottomAdapter.notifyDataSetChanged();
            }
        } else {
            bottomView.setVisibility(View.GONE);
        }

    }

    /**
     * 设置点击
     */
    public void onSetClick(){
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivityForResult(intent, 2);
    }


    private void setUserInfo() {
        String headPath = spu.getUserHead();
        String name = spu.getNickName();
        if (!TextUtils.isEmpty(spu.getIsLogin())) {
            if (!TextUtils.isEmpty(headPath)) {
                GlideUtils.loadCircleHeaderOfCommon(mainActivity, headPath, userHead);
            } else {
                userHead.setBackgroundResource(R.drawable.login_default_icon);
            }
        } else {
            GlideUtils.loadCircleHeaderOfCommon(mainActivity, headPath, userHead);
        }
        if(!TextUtils.isEmpty(name)){
            userName.setText(spu.getNickName());
        } else {
            userName.setText(getResources().getString(R.string.click_login));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mainActivity = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHeaderWaveHelper.start();
        setUserInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHeaderWaveHelper.cancel();
    }

    @Override
    protected void requestData() {
        startTask();
    }

    /**
     * 设置登录状态
     */
    public void setLoginState(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUserInfo();
            }
        },300);
    }

    /**
     * 点击上部item
     * @param item
     */
    @Override
    public void onMyTopItemClick(MyBean.ChildItemBean item) {
        Item mItem = Item.valueOf(item.getName());
        Intent intent = new Intent();
        switch (mItem) {
            case TEST: //考试
                toTest();
                break;
            case HOMEWORK: //作业
                toHomeWork();
                break;
            case ANSWER: //问答
                toAnswer();
                break;
            case CLASS: //班级
                toClass();
                break;
        }

    }


    /**
     * 点击底部item
     * @param item
     */
    @Override
    public void onMyBottomItemClick(MyBean.ChildItemBean item) {
        Item mItem = Item.valueOf(item.getName());
        switch (mItem) {
            case STUDYING: //我学课程
                toMyCourse();
                break;
            case TEACHING: //我教课程
                toMyTeacher();
                break;
            case DOWNLOAD: //下载
                toDownload();
                break;
            case POSTER: //海报
                toMyPoster();
                break;
            case SERVICE: //服务
                toMyService();
                break;
            case ORDER: //订单
                toMyOrder();
                break;
            case WALLET: //钱包

                break;
            case COLLECT: //收藏

                break;
        }

    }

    private void toMyCourse(){
        if (TextUtils.isEmpty(spu.getIsLogin())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 3);
        }else {
            Intent intent = new Intent(getActivity(), LearnCourseActivity.class);
            startActivityForResult(intent, 3);
        }
    }

    private void toMyTeacher(){
        if (TextUtils.isEmpty(spu.getIsLogin())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 3);
        }else {
            Intent intent = new Intent(getActivity(), TeachingCourseActivity.class);
            startActivityForResult(intent, 3);
        }
    }

    private void toDownload(){
        if (TextUtils.isEmpty(spu.getIsLogin())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 3);
        }else {
            Intent intent = new Intent(getActivity(), Download_Center_Activity.class);
            startActivityForResult(intent, 3);
        }
    }
    private void toMyPoster(){
        if (TextUtils.isEmpty(spu.getIsLogin())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 3);
        }else {
            Intent intent = new Intent(getActivity(), MyPosterActivity.class);
            startActivityForResult(intent, 3);
        }
    }

    private void toMyService(){
        if (TextUtils.isEmpty(spu.getIsLogin())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 3);
        }else {
            MyServiceActivity.gotoActivity(getActivity());
        }
    }

    private void toMyOrder() {
        if (TextUtils.isEmpty(spu.getIsLogin())) {
            Intent intent = new Intent(mainActivity, LoginActivity.class);
            startActivityForResult(intent, 3);
        } else {
            Intent intent = new Intent(mainActivity, MyOrderActivity.class);
            startActivityForResult(intent, 3);
        }

    }

    private void toCollect(){

    }

    private void toWallet(){

    }

    private void toTest(){
        if (TextUtils.isEmpty(spu.getIsLogin())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 3);
        }else {
            Intent intent = new Intent(getActivity(), StuCourseWorkActivity.class);
            intent.putExtra("workType", 1);
            intent.putExtra("course_id","4");
            startActivityForResult(intent, 3);
        }
    }

    private void toHomeWork(){
        if (TextUtils.isEmpty(spu.getIsLogin())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 3);
        }else {

            Intent intent = new Intent(getActivity(), StuCourseWorkActivity.class);
            intent.putExtra("workType", 2);
            intent.putExtra("course_id","4");
            startActivityForResult(intent, 3);
        }
    }
    private void toClass(){
        if (TextUtils.isEmpty(spu.getIsLogin())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 3);
        } else if(spu.getUserRole().equals("1")){ //老师
            Intent intent = new Intent(getActivity(), ClassListActivity.class);
            intent.putExtra("type", 2);
            startActivityForResult(intent, 3);
        } else if(spu.getUserRole().equals("0")){  //学生
            Intent intent = new Intent(getActivity(), MyClassStudentActivity.class);
            startActivity(intent);
        }

    }
    private void toAnswer(){

//        if (TextUtils.isEmpty(spu.getIsLogin())) {
//            Intent intent = new Intent(getActivity(), LoginActivity.class);
//            startActivityForResult(intent, 3);
//        }else {
//            Intent intent = new Intent(getActivity(), MyQuestionActivity.class);
//            intent.putExtra("type", "1");
//            startActivityForResult(intent, 3);
//        }
    }

    private void toUserInfo(){

        if (TextUtils.isEmpty(spu.getIsLogin())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 3);
        }else {
            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
            startActivityForResult(intent, 3);
        }
    }


    @Override
    public void onRefresh() {
        startTask();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == Constants.LOGIN_BACK) {

            startTask();
        } else if (requestCode == 3 && resultCode == 2) { //注册自动登录返回的结果
            //这里调用获取用户列表，实现注册完成后登录。
            getUserInfo();
        } else if (requestCode == 2 && resultCode == Constants.LOGIN_BACK) { //退出登录

            startTask();
        } else if (requestCode == 3 && resultCode == 10) {
            String path = data.getStringExtra("path");
            String nickName = data.getStringExtra("nickName");
            String userSignature = data.getStringExtra("userSignature");
            saveSubmitUserInfo(nickName, userSignature);
            updateUserHead(path);
        }
    }


    /**
     * 保存提交用户信息
     */
    private void saveSubmitUserInfo(final String nickName,final String userSignature) {
                new HttpPostBuilder(mainActivity)
                .setUrl(UrlsNew.POST_CHANGE_USERINFO)            // 设置url   包含queryParams
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        saveSuccess(nickName,userSignature);
                    }
                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(PersonCentreFragment.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mainActivity, myLayout);
                        }
                    }
                })
                .setClassObj(null)
                .addBodyParam("nickname", nickName)
                .addBodyParam("birthday", spu.getBirthDay())
                .addBodyParam("gender", spu.getGender())
                .addBodyParam("desc",userSignature)
                .build();

    }

    /**
     * 保存成功
     */
    private void saveSuccess(String nickName,String signature) {
        spu.setDesc(signature);
        spu.setNickName(nickName);
        String gender = spu.getGender();
        if (gender.equals("1")) {
            spu.setSex("1");
        } else {
            spu.setSex("2");
        }
    }

    /**
     * 更新用户头像
     * @param path
     */
    public void updateUserHead(final String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        HashMap<String, String> postImages = new HashMap<>();
        postImages.put("file0", path);
        new HttpPostFileBuilder(mainActivity)
                .setUrl(UrlsNew.POST_SYSTEM_FILE)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        AvatarBean avatarBean = (AvatarBean) resultBean;
                        ArrayList<String> items = avatarBean.getItems();
                        headUrl = items.get(0);
                        uploadHead(headUrl);
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        ToastUtils.makeText(mainActivity,"头像上传失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(AvatarBean.class)
                .addQueryParams("type", "avatar")
                .setFileNames(postImages)
                .build();
    }

    private void uploadHead(String headUrl) {
                new HttpPostBuilder(mainActivity)
                .setUrl(UrlsNew.POST_USER_AVATAR)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        getUserInfo();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(PersonCentreFragment.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mainActivity, myLayout);
                        }
                    }
                })
                .setClassObj(null)
                .addBodyParam("type", "1")
                .addBodyParam("avatar", headUrl)
                .build();
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
                new HttpGetBuilder(mainActivity)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (requestCode == 1000) {
                            UserInfoBean item = ((UserInfoResult) resultBean).getItem();
                            registerSuccess(item);
                            setUserInfo();
                        }
                    }
                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(PersonCentreFragment.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mainActivity, myLayout);
                        }
                    }
                })
                .setClassObj(UserInfoResult.class)
                .setUrl(UrlsNew.USER_PROFILE)
                .setRequestCode(1000)
                .build();
    }


    private void registerSuccess(UserInfoBean info) {
        UserInfoBean.ProfileBean profile = info.getProfile();
        spu.setUid(info.getId());
        spu.setNickName(profile.getNickname());
        spu.setUserHead(profile.getAvatar()); //用户头像
        spu.setBigHead(profile.getBig_avatar());//大头像
        String birthday = profile.getBirthday();
        if (!TextUtils.isEmpty(birthday)) {
            if (birthday.contains("-")) {
                spu.setBirthDay(birthday);
            } else {
                long b = Integer.parseInt(profile.getBirthday());
                String mBirthday = DateUtil.getYearMonthAndDay(b);
                spu.setBirthDay(mBirthday);
            }
        } else {
            spu.setBirthDay("1970-01-01");
        }
        spu.setMobile(info.getMobile());
        spu.setEmail(info.getEmail());
        spu.setRegisterType(info.getRegister_type()); //注册类型
        spu.setCreateTime(info.getCreate_time());
        spu.setGender(profile.getGender()); //性别
        spu.setDesc(profile.getDesc());
        spu.setRemark(profile.getRemark());
        //"是否老师:0.否 1.是"
        spu.setUserRole(info.getIs_teacher());
        spu.setIsLogin("1");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
