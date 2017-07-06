package com.coder.kzxt.video.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.baidu.cyberplayer.core.BVideoView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.activity.CourseSynopsisActivity;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.order.activity.OrderConfirmationActivity;
import com.coder.kzxt.order.beans.CourseClass;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.video.adapter.CatalogueAdapter;
import com.coder.kzxt.video.beans.CatalogueBean;
import com.coder.kzxt.video.beans.CourseRoleResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangcy on 2017/3/10.
 */

public class VideoViewFragment extends ScrollTabHolderFragment implements
        AbsListView.OnScrollListener, VideoViewActivity.OnMainListener, HttpCallBack {

    private View v;
    private static final String ARG_POSITION = "position";
    private int mPosition;
    private SharedPreferencesUtil spu;
    private ExpandableListView mListView;
    private VideoViewActivity mActivity;
    private BVideoView bVideoView = null;
    private ProgressBar progressBar = null;
    private FrameLayout tem_ly;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout join_ly;
    private Button load_fail_button;
    private Button ask_button;
    private Button join_button;
    private Bundle args;
    private String treeId;
    private String iscenter;
    private String courseClassId;
    private RelativeLayout my_layout;
    private CatalogueAdapter catalogueAdapter;
    private View placeHolderView;
    private TextView course_par_tv;
    private int x = 0;
    private int y = 0;
    private CatalogueBean catalogueBean;
    private String scanQrcode;
    // 根据tid和id在listview上定位上次观看的视频
    private int tid_location = 0;
    private int id_location = 0;
    private Handler handler;
    private ObjectAnimator animato;
    private boolean isMoveScl = false;//此变量判断播放器是否可以跟随列表滚动
    private int httpIndex;
    private int isJoin;
    private List<CourseRoleResult.Item.ListBean> classList = new ArrayList<>();
    private double classPrice;
    private String course_class_id;
    private ArrayList<CourseClass.ClassBean> classItems;
    private View blue_line;
    private boolean isDoubleClick = false;
    private CourseQuestionsReceiver questionsReceiver;
    public static Fragment newInstance(int position, String treeId, String courseClassId, String iscenter,String scanQrcode) {
        VideoViewFragment f = new VideoViewFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString("treeid", treeId);
        b.putString("scanQrcode",scanQrcode);
        b.putString("courseClassId", courseClassId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        args = getArguments();
        mPosition = args.getInt(ARG_POSITION);
        treeId = args != null ? args.getString("treeid") : "";
        courseClassId = args != null ? args.getString("courseClassId") : "";
        scanQrcode = args != null?args.getString("scanQrcode") :"";
        if (args != null && args.getString(Constants.IS_CENTER) != null) {
            iscenter = args.getString(Constants.IS_CENTER);
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_video_view, null);
            my_layout = (RelativeLayout) v.findViewById(R.id.my_layout);
            jiazai_layout = (LinearLayout) v.findViewById(R.id.jiazai_layout);
            jiazai_layout.setVisibility(View.VISIBLE);
            load_fail_layout = (LinearLayout) v.findViewById(R.id.load_fail_layout);
            load_fail_button = (Button) v.findViewById(R.id.load_fail_button);
            join_ly = (LinearLayout) v.findViewById(R.id.join_ly);
            tem_ly = (FrameLayout) v.findViewById(R.id.tem_ly);
            mListView = (ExpandableListView) v.findViewById(R.id.myListView);
            ask_button = (Button) v.findViewById(R.id.ask_button);
            join_button = (Button) v.findViewById(R.id.join_button);
            blue_line = v.findViewById(R.id.blue_line);

            mListView.setOnScrollListener(this);
            placeHolderView = inflater.inflate(R.layout.video_header_placeholder, mListView, false);
            course_par_tv = (TextView) placeHolderView.findViewById(R.id.course_par_tv);

            GradientDrawable myGrad = (GradientDrawable) course_par_tv.getBackground();
            myGrad.setStroke(1, getResources().getColor(R.color.first_theme));// 设置边框宽度与颜色

            questionsReceiver=new CourseQuestionsReceiver();
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction(Constants.QUESTION_LOGIN);
            intentFilter.addAction(Constants.PAY_SUCCESS_BACK);
            getContext().registerReceiver(questionsReceiver,intentFilter);
        }

        animato = ObjectAnimator.ofFloat(tem_ly, "translationY", 0F, -500F).setDuration(20);
        //实现动画的监听
        animato.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tem_ly.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });

//        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                return true;
//            }
//        });

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //点击学习课时之前判断上一个必学课程是否看看完

                List<CatalogueBean.ItemsBean> groupList = catalogueBean.getItems();

                List<CatalogueBean.ItemsBean.VideoBean> childList = catalogueBean.getItems().get(groupPosition).getVideo();

                CatalogueBean.ItemsBean.VideoBean videoBean = childList.get(childPosition);

                //没有加入状态只有试学才能看
                if (isJoin == 0) {
                    if (videoBean.getFree().equals("0")) {

                        for (int i = 0; i < groupList.size(); i++) {
                            for (int j = 0; j < catalogueBean.getItems().get(i).getVideo().size(); j++) {
                                catalogueBean.getItems().get(i).getVideo().get(j).setLast_location("0");
                            }
                        }
                        spu.setGroupId(treeId, catalogueBean.getItems().get(groupPosition).getId());
                        spu.setChildId(treeId, videoBean.getId());
                        videoBean.setLast_location("1");
                        catalogueAdapter.notifyDataSetChanged();

                        Message sendmsg = new Message();
                        sendmsg.what = Constants.PREPARE_PLAY;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("videoBean", videoBean);
                        sendmsg.setData(bundle);
                        handler.sendMessage(sendmsg);

                    } else {
                        mActivity.showJoinView();
                    }
                 //学生
                } else if(isJoin==1){
                    if (isSeeVideo(videoBean, groupPosition, childPosition)) {

                        for (int i = 0; i < groupList.size(); i++) {
                            for (int j = 0; j < catalogueBean.getItems().get(i).getVideo().size(); j++) {
                                catalogueBean.getItems().get(i).getVideo().get(j).setLast_location("0");
                            }
                        }
                        spu.setGroupId(treeId, catalogueBean.getItems().get(groupPosition).getId());
                        spu.setChildId(treeId, videoBean.getId());
                        videoBean.setLast_location("1");
                        catalogueAdapter.notifyDataSetChanged();

                        Message sendmsg = new Message();
                        sendmsg.what = Constants.PREPARE_PLAY;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("videoBean", videoBean);
                        sendmsg.setData(bundle);
                        handler.sendMessage(sendmsg);
                    } else {
                        showTipVideoDiaolg();
                    }
                  //本科教师/创建者
                }else if(isJoin==2||isJoin==3){

                    for (int i = 0; i < groupList.size(); i++) {
                        for (int j = 0; j < catalogueBean.getItems().get(i).getVideo().size(); j++) {
                            catalogueBean.getItems().get(i).getVideo().get(j).setLast_location("0");
                        }
                    }
                    spu.setGroupId(treeId, catalogueBean.getItems().get(groupPosition).getId());
                    spu.setChildId(treeId, videoBean.getId());
                    videoBean.setLast_location("1");
                    catalogueAdapter.notifyDataSetChanged();

                    Message sendmsg = new Message();
                    sendmsg.what = Constants.PREPARE_PLAY;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("videoBean", videoBean);
                    sendmsg.setData(bundle);
                    handler.sendMessage(sendmsg);
                }

                return false;
            }
        });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //手指按下的时候：初始化 x,y 值
                        x = (int) event.getX();
                        y = (int) event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int upx = (int) event.getX();
                        int upy = (int) event.getY();
                        drawTouch(upx, upy);
                        break;

                    case MotionEvent.ACTION_UP:
                    /*
                     * 手指抬起来触发 ，所以判断在这里进行
					 * 1.获得结束的x,y
					 * 2.进行判断
					 */

                        break;
                }
                return false;

            }
        });

        //咨询
        ask_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(spu.getIsLogin())) {
                    startActivityForResult(new Intent(mActivity, LoginActivity.class), 1);
                    return;
                }
            }
        });

        //加入学习
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(spu.getIsLogin())) {
                    startActivityForResult(new Intent(mActivity, LoginActivity.class), 1);
                    return;
                }
                if (classPrice == 0.0 && (classItems != null && classItems.size() == 1)) { //直接加入学习
                    if (!isDoubleClick) {    //防止多次点击，多次加入
                        isDoubleClick = true;
                        joinStudy(treeId, course_class_id);
                    }
                } else {
                    Intent intent = new Intent(mActivity, OrderConfirmationActivity.class);
                    intent.putExtra("courseId", treeId);
                    if(!TextUtils.isEmpty(scanQrcode)){
                        intent.putExtra("scanClassId",courseClassId);
                    }
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                }
            }
        });

        //课程详情
        course_par_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CourseSynopsisActivity.class);
                intent.putExtra("treeId", treeId);
                intent.putExtra("isJoin", isJoin);
                startActivityForResult(intent, 1);
            }
        });

        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiazai_layout.setVisibility(View.VISIBLE);
                load_fail_layout.setVisibility(View.GONE);
                getOrderClass();
                htttpCatalogueRequest();
            }
        });


        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }
        return v;
    }


    private void drawTouch(int upx, int upy) {
        bVideoView = mActivity.getVideoView();
        progressBar = mActivity.getProgressBar();
        //水平滑动
        if (upx - x > 100) {
            //向右滑动
        } else if (x - upx > 100) {
            //向左滑动
        } else if (upy - y > 100) {
            //向下滑动
            mActivity.changeTopCor();
            tem_ly.setVisibility(View.GONE);

        } else if (y - upy > 100) {
            isMoveScl = true;
            //向上滑动
            //向上滑动时根据播放器是否正在播放来控制 播放器view是否滑出屏幕外
            animato.start();

            if (bVideoView != null && progressBar != null
                    && !bVideoView.isPlaying()
                    && progressBar.getVisibility() == View.GONE) {
                if (mListView.getCount() >= 8) {
                    mActivity.changeTopCor2();
                }
                mActivity.isShowVideo(false);

            } else {
                mActivity.isShowVideo(true);
            }

        }
    }


    private boolean isSeeVideo(CatalogueBean.ItemsBean.VideoBean videoBean, int groupPosition, int childPosition) {

        //如果是免费或者不是必学或者第一个视频直接传递观看信息
        List<CatalogueBean.ItemsBean.VideoBean> childList = catalogueBean.getItems().get(groupPosition).getVideo();
        if ((groupPosition == 0 && childPosition == 0)) {
            return true;

        } else {
            //如果是必学视频获取上一个视频的信息
            if (childPosition == 0) {
                List<CatalogueBean.ItemsBean.VideoBean> upChildList = catalogueBean.getItems().get(groupPosition - 1).getVideo();
                if (upChildList != null && upChildList.size() > 0) {
                    CatalogueBean.ItemsBean.VideoBean upVideoBean = upChildList.get(upChildList.size() - 1);
                    if (upVideoBean.getIsShowLock() == 0) {
                        //直接观看
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                CatalogueBean.ItemsBean.VideoBean upVideoBean = childList.get(childPosition - 1);
                if (upVideoBean.getIsShowLock() == 0) {
                    //直接观看
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }


    public void showTipVideoDiaolg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.custom_dialog);
        builder.setMessage(getResources().getString(R.string.studyuplesssn));
        builder.setPositiveButton(getResources().getString(R.string.password_dialog_ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }


    private void htttpCatalogueRequest() {
        initIndex = false;
        httpIndex = 0;
        indexgroup = 10000000;
        indexchild = 10000000;

        if (TextUtils.isEmpty(spu.getIsLogin())) {
            isJoin = 0;
            httpIndex = 1;
        } else {
            //请求是否加入此课程
            new HttpGetBuilder(getActivity())
                    .setHttpResult(this)
                    .setClassObj(CourseRoleResult.class)
                    .setUrl(UrlsNew.GET_COURSE_ROLE)
                    .addQueryParams("course_id", treeId)
                    .setRequestCode(2)
                    .build();
        }

        //请求课程目录
        new HttpGetBuilder(getActivity())
                .setHttpResult(this)
                .setClassObj(CatalogueBean.class)
                .setUrl(UrlsNew.GET_COURSE_LESSON)
                .addQueryParams("orderBy", "create_time desc")
                .addQueryParams("course_id", treeId)
//                .addQueryParams("user_id",spu.getUid())
                .setRequestCode(1)
                .build();


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (VideoViewActivity) activity;
        handler = mActivity.getmHandler();
        spu = new SharedPreferencesUtil(mActivity);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isMoveScl) {
            if (mScrollTabHolder != null) {
                mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
            }
        }
    }

    @Override
    public void onSetSelectcPostion() {
        if (mListView != null && catalogueAdapter != null && catalogueBean != null) {
            isMoveScl = false;
            initAdapter();
        }

    }

    private void initAdapter() {
        mListView.setAdapter(catalogueAdapter);
        int width = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        //设置默认箭头位置
        mListView.setIndicatorBounds(width - 80, width - 10);
        List<CatalogueBean.ItemsBean> groupList = catalogueBean.getItems();

        if (groupList.size() > 0) {
            for (int i = 0; i < groupList.size(); i++) {
                mListView.expandGroup(i);
            }
            String groupId = spu.getGroupId(treeId);
            String childId = spu.getChildId(treeId);

            for (int i = 0; i < groupList.size(); i++) {
                CatalogueBean.ItemsBean itemsBean = groupList.get(i);
                String tid = itemsBean.getId();
                if (groupId.equals(tid)) {
                    tid_location = i;
                }
            }

            List<CatalogueBean.ItemsBean.VideoBean> childList = groupList.get(tid_location).getVideo();
            for (int i = 0; i < childList.size(); i++) {
                CatalogueBean.ItemsBean.VideoBean videoBean = childList.get(i);
                String id = videoBean.getId();
                if (childId.equals(id)) {
                    id_location = i;
                }
            }
            //        当前选择的播放对象
            CatalogueBean.ItemsBean.VideoBean videoBean = groupList.get(tid_location).getVideo().get(id_location);

            Message sendmsg = new Message();
            sendmsg.what = Constants.INIT_SELECT;
            Bundle bundle = new Bundle();
            bundle.putSerializable("videoBean", videoBean);
            bundle.putSerializable("list", (Serializable) classList);
            bundle.putInt("isJoin", isJoin);
            sendmsg.setData(bundle);
            handler.sendMessage(sendmsg);


            if (tid_location == 0 && id_location == 0) {
                groupList.get(0).getVideo().get(0).setLast_location("1");
            } else {
                if (mListView.getCount() >= 8) {
                    tem_ly.setVisibility(View.VISIBLE);
                }
                mListView.setSelectedChild(tid_location, id_location, true);
                groupList.get(tid_location).getVideo().get(id_location).setLast_location("1");

            }
            catalogueAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onSetLastPosition(int selectPostion) {

        String groupId = spu.getGroupId(treeId);
        String childId = spu.getChildId(treeId);
        List<CatalogueBean.ItemsBean> groupList = catalogueBean.getItems();

        for (int i = 0; i < groupList.size(); i++) {
            CatalogueBean.ItemsBean itemsBean = groupList.get(i);
            String tid = itemsBean.getId();
            if (groupId.equals(tid)) {
                tid_location = i;
            }
        }
        List<CatalogueBean.ItemsBean.VideoBean> childList = groupList.get(tid_location).getVideo();
        for (int i = 0; i < childList.size(); i++) {
            CatalogueBean.ItemsBean.VideoBean videoBean = childList.get(i);
            String id = videoBean.getId();
            if (childId.equals(id)) {
                id_location = i;
            }
        }

        if (tid_location == 0 && id_location == 0) {
            mListView.setSelectedChild(0, 0, true);
            groupList.get(0).getVideo().get(0).setLast_location("1");
        } else {
            if (mListView.getCount() >= 8 && selectPostion > 10) {
                tem_ly.setVisibility(View.VISIBLE);
            }
            mListView.setSelectedChild(tid_location, id_location, true);
            groupList.get(tid_location).getVideo().get(id_location).setLast_location("1");
        }
        catalogueAdapter.notifyDataSetChanged();
    }


    private int PlayGroupPostion;
    private int PlayChildPostion;

    @Override
    public void onResAdaterData(CatalogueBean.ItemsBean.VideoBean videoBean) {

        List<CatalogueBean.ItemsBean> groupList = catalogueBean.getItems();
        all:
        for (int i = 0; i < groupList.size(); i++) {
            List<CatalogueBean.ItemsBean.VideoBean> childList = groupList.get(i).getVideo();
            for (int j = 0; j < childList.size(); j++) {
                if (childList.get(j).getId().equals(videoBean.getId())) {
                    PlayGroupPostion = i;
                    PlayChildPostion = j;
                    childList.get(j).setIsShowLock(0);
                }
                if (i == PlayGroupPostion) {
                    if (j > PlayChildPostion) {
                        if (childList.get(j).getIs_see().equals("0")) {
                            childList.get(j).setIsShowLock(0);
                        } else {
                            break all;
                        }
                    }
                }
                if (i > PlayGroupPostion) {
                    if (childList.get(j).getIs_see().equals("1")) {
                        break all;
                    } else {
                        childList.get(j).setIsShowLock(0);
                    }
                }
            }

        }
        catalogueAdapter.notifyDataSetChanged();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void adjustScroll(int scrollHeight) {
        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
            return;
        }
        mListView.setSelectionFromTop(1, scrollHeight);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.LOGIN_BACK) {
            mActivity.stopPlay();//停止播放
            jiazai_layout.setVisibility(View.VISIBLE);
            mListView.removeHeaderView(placeHolderView);
            htttpCatalogueRequest();
        } else if (resultCode == Constants.RES_BACK_AC) {
            mActivity.stopPlay();//停止播放
            jiazai_layout.setVisibility(View.VISIBLE);
            mListView.removeHeaderView(placeHolderView);
            htttpCatalogueRequest();
        } else if (resultCode == Constants.PAY_SUCCESS) { //支付成功
//            String classId = data.getStringExtra("classId");
//            if(!TextUtils.isEmpty(classId)){
//                joinStudy(treeId, classId);
//            } else {
//                mActivity.stopPlay();//停止播放
//                jiazai_layout.setVisibility(View.VISIBLE);
//                mListView.removeHeaderView(placeHolderView);
//                htttpCatalogueRequest();
//            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void requestData() {
        getOrderClass();
        htttpCatalogueRequest();
    }

    private List<CatalogueBean.ItemsBean> headList;

    private int indexgroup = 10000000;
    private int indexchild = 10000000;
    private boolean initIndex = false;
    private ArrayList<CatalogueBean.ItemsBean.VideoBean> selects;

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        httpIndex++;
        if (requestCode == 1) {
            catalogueBean = (CatalogueBean) resultBean;
            headList = catalogueBean.getItems();
            //获取播放页选集数据
            selects = new ArrayList();
            ArrayList list = new ArrayList();
            if (headList.size() != 0) {
                for (int i = 0; i < headList.size(); i++) {
                    List<CatalogueBean.ItemsBean.VideoBean> childList = headList.get(i).getVideo();
                    for (int j = 0; j < childList.size(); j++) {
                        CatalogueBean.ItemsBean.VideoBean videoBean = childList.get(j);
                        if (!initIndex && videoBean.getIs_see().equals("1")) {
                            indexgroup = i;
                            indexchild = j;
                            initIndex = true;
                        }
                    }
                }

                for (int i = 0; i < headList.size(); i++) {
                    List<CatalogueBean.ItemsBean.VideoBean> childList = headList.get(i).getVideo();
                    for (int j = 0; j < childList.size(); j++) {
                        CatalogueBean.ItemsBean.VideoBean videoBean = childList.get(j);
                        if (i == indexgroup) {
                            if (j >= indexchild) {
                                videoBean.setIsShowLock(1);
                            }
                        }
                        if (i > indexgroup) {
                            videoBean.setIsShowLock(1);
                        }
                       if(videoBean.getType().equals("1")){
                           selects.add(videoBean);
                       }
                    }
                }

                list.add(selects);
                list.add(headList);
                Message selmsg = new Message();
                selmsg.what = Constants.LIST_DATA;
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("videosData", list);
                selmsg.setData(bundle);
                handler.sendMessage(selmsg);
            }

        } else if (requestCode == 2) {
            CourseRoleResult courseClassBean = (CourseRoleResult) resultBean;
            //0未加入 1学生 2老师 3创建课程的老师
            isJoin = courseClassBean.getItem().getRole();
            classList = courseClassBean.getItem().getList();

            Message sendmsg = new Message();
            sendmsg.what = Constants.INIT_DATA;
            Bundle bundle = new Bundle();
            bundle.putSerializable("list", (Serializable) classList);
            bundle.putInt("isJoin", isJoin);
            sendmsg.setData(bundle);
            handler.sendMessage(sendmsg);

        }

        if (httpIndex >= 2) {
            initView();
        }

    }

    private void initView() {

        catalogueAdapter = new CatalogueAdapter(mActivity, headList, isJoin);
        mListView.addHeaderView(placeHolderView);

        initAdapter();
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        if (TextUtils.isEmpty(spu.getIsLogin()) || isJoin == 0) {
            join_ly.setVisibility(View.VISIBLE);
        } else {
            join_ly.setVisibility(View.GONE);
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        isDoubleClick = false;
        jiazai_layout.setVisibility(View.GONE);
        NetworkUtil.httpNetErrTip(mActivity, my_layout);
        load_fail_layout.setVisibility(View.VISIBLE);
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004 || code == Constants.HTTP_CODE_8000) {
            NetworkUtil.httpRestartLogin(VideoViewFragment.this, my_layout);
        }
    }

    /**
     * 获取课程下关联的班级
     */
    private void getOrderClass() {
        new HttpGetBuilder(mActivity)
                .setUrl(UrlsNew.GET_COURSE_CLASS)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        CourseClass bean = (CourseClass) resultBean;
                        classItems = applyClass(bean.getItems());
                        changeJoinStudyState(classItems);
                        calculatePrice(classItems);
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(mActivity, my_layout);
                        } else {
                            NetworkUtil.httpNetErrTip(mActivity, my_layout);
                        }
                    }
                })
                .setClassObj(CourseClass.class)
                .addQueryParams("course_id", treeId)
                .build();
    }

    /**
     * 筛选加入的授课班
     *
     * @param items
     * @return
     */
    private ArrayList<CourseClass.ClassBean> applyClass(ArrayList<CourseClass.ClassBean> items) {
        ArrayList<CourseClass.ClassBean> classList = new ArrayList<>();
        if (items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                CourseClass.ClassBean classBean = items.get(i);
                if (!classBean.getApply_status().equals("0")) {
                    classList.add(classBean);
                }
            }
            return classList;
        }
        return null;
    }

    private void calculatePrice(ArrayList<CourseClass.ClassBean> classItems) {
        boolean isDefault = false;
        if (null != classItems) {
            for (int i = 0; i < classItems.size(); i++) {
                CourseClass.ClassBean classBean = classItems.get(i);
                if (classBean.getIs_default().equals("1")) {
                    isDefault = true;
                    course_class_id = classBean.getId();
                    classPrice = Double.parseDouble(classBean.getPrice());
                }
                if (!isDefault) {
                    course_class_id = classBean.getId();
                    classPrice = Double.parseDouble(classBean.getPrice());
                }
            }
        }
    }

    /**
     * 更改加入学习状态
     *
     * @param classItems
     */
    private void changeJoinStudyState(ArrayList<CourseClass.ClassBean> classItems) {
        if (classItems != null && classItems.size() > 0) {
            join_button.setClickable(true);
            blue_line.setVisibility(View.VISIBLE);
            join_button.setBackgroundResource(R.color.first_theme);
            join_button.setText(mActivity.getResources().getString(R.string.join_study));
            join_button.setTextColor(mActivity.getResources().getColor(R.color.bg_white));
        } else {
            join_button.setClickable(false);
            join_button.setBackgroundResource(R.color.bg_join_study);
            join_button.setText(getResources().getString(R.string.unable_to_join));
            blue_line.setVisibility(View.GONE);
            join_button.setTextColor(mActivity.getResources().getColor(R.color.font_no_join));
        }
    }

    private void joinStudy(String treeId, String calssId) {
               mActivity.stopPlay();//停止播放
               jiazai_layout.setVisibility(View.VISIBLE);
               new HttpPostBuilder(mActivity)
                .setUrl(UrlsNew.POST_COURSE_CLASS_MEMBER)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        mListView.removeHeaderView(placeHolderView);
                        htttpCatalogueRequest();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(mActivity, my_layout);
                        } else {
                            NetworkUtil.httpNetErrTip(mActivity, my_layout);
                        }
                        if (code == 3056) {
                            mListView.removeHeaderView(placeHolderView);
                            htttpCatalogueRequest();
                        }
                        isDoubleClick = false;
                        jiazai_layout.setVisibility(View.GONE);
                    }
                })
                .setClassObj(null)
                .addBodyParam("account", spu.getUserAccount())
                .addBodyParam("course_id", treeId)
                .addBodyParam("join_type", "1")
                .addBodyParam("course_class_id", calssId)
                .build();

    }
    public class CourseQuestionsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.QUESTION_LOGIN)){
                refresh();
            }else if(intent.getAction().equals(Constants.PAY_SUCCESS_BACK)){
                String classId = intent.getStringExtra("classId");
                paySuccessProcess(classId);
            }
        }
    }

    /**
     * 支付成功后的处理
     * @param classId
     */
    public void paySuccessProcess(String classId) {
        if(!TextUtils.isEmpty(classId)){
            joinStudy(treeId, classId);
        } else {
            mActivity.stopPlay();//停止播放
            jiazai_layout.setVisibility(View.VISIBLE);
            mListView.removeHeaderView(placeHolderView);
            htttpCatalogueRequest();
        }
    }

    public void refresh(){
            mActivity.stopPlay();//停止播放
            jiazai_layout.setVisibility(View.VISIBLE);
            mListView.removeHeaderView(placeHolderView);
            htttpCatalogueRequest();
        }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(questionsReceiver);
        super.onDestroy();
    }
}
