package com.coder.kzxt.message.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.classe.activity.ShareClassQrcode;
import com.coder.kzxt.classe.beans.ClassBean;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.im.beans.GroupInfo;
import com.coder.kzxt.message.adapter.ManagerTeacherDelegate;
import com.coder.kzxt.message.beans.ManagerUserProfile;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.ToastUtils;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMGroupMemberResult;
import com.tencent.TIMGroupMemberRoleType;
import com.tencent.TIMGroupReceiveMessageOpt;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * 老师的消息管理界面
 */
public class ManagerTeacherActivity extends BaseActivity
{

    private android.support.v7.widget.Toolbar toolbar;
    private LinearLayout qr_code_ly;
    private TextView tv_className;
    private LinearLayout stop_talk_ly;
    private Switch talk_switch;
    private LinearLayout notify_ly;
    private Switch notify_switch;
    private com.coder.kzxt.recyclerview.MyRecyclerView myRecyclerView;


    private ManagerTeacherDelegate orderListStageDelegate;
    private BaseRecyclerAdapter stageadapter;

    private List<TIMUserProfile> postersBeanList = new ArrayList<>();
    private List<ManagerUserProfile> silenceSeconds = new ArrayList<>();

    private String groupId;
    private RelativeLayout qrcodeLayout;
    private String qrcodeContent;
    private String className;
    private String classId;

    public static void gotoActivity(Context context, String groupId,String content,String className,String classId)
    {
        Intent intent = new Intent(context, ManagerTeacherActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("qrcontent",content);
        intent.putExtra("classId",classId);
        intent.putExtra("className",className);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_manager_teacher);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        qr_code_ly = (LinearLayout) findViewById(R.id.qr_code_ly);
        tv_className = (TextView) findViewById(R.id.className);
        stop_talk_ly = (LinearLayout) findViewById(R.id.stop_talk_ly);
        talk_switch = (Switch) findViewById(R.id.talk_switch);
        notify_ly = (LinearLayout) findViewById(R.id.notify_ly);
        notify_switch = (Switch) findViewById(R.id.notify_switch);
        myRecyclerView = (com.coder.kzxt.recyclerview.MyRecyclerView) findViewById(R.id.myRecyclerView);
        qrcodeLayout = (RelativeLayout) findViewById(R.id.class_qrcode_layout);

        initVariable();
        initData();
        inintEvent();
        requestClassStatus();
    }

    private void initVariable()
    {
        groupId = getIntent().getStringExtra("groupId");
        className = getIntent().getStringExtra("className");
        classId = getIntent().getStringExtra("classId");
        qrcodeContent = getIntent().getStringExtra("qrcontent");
    }

    private void initData()
    {

        toolbar.setTitle(getResources().getString(R.string.message_manager));
        setSupportActionBar(toolbar);

        orderListStageDelegate = new ManagerTeacherDelegate(this, silenceSeconds);
        stageadapter = new BaseRecyclerAdapter(this, postersBeanList, orderListStageDelegate);
        myRecyclerView.setAdapter(stageadapter);

        if (GroupInfo.getInstance().getMessageOpt(groupId) == TIMGroupReceiveMessageOpt.ReceiveAndNotify)
        {
            notify_switch.setChecked(false);
        } else
        {
            notify_switch.setChecked(true);
        }

        showLoadingView();
        //获取群组成员信息
        TIMGroupManager.getInstance().getGroupMembers(groupId, cb);

    }


    private void inintEvent()
    {
        // 消息免打扰
        notify_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                TIMGroupReceiveMessageOpt opt;
                if (isChecked)
                {
                    opt = TIMGroupReceiveMessageOpt.ReceiveNotNotify;
                } else
                {
                    opt = TIMGroupReceiveMessageOpt.ReceiveAndNotify;
                }

                //设置接收状态
                TIMGroupManager.getInstance().modifyReceiveMessageOpt(groupId, opt, selenceCb);
            }
        });

        stageadapter.setOnItemLongClickListener(new BaseRecyclerAdapter.setOnItemLongClickListener()
        {
            @Override
            public void onItemLongClick(View view, int position)
            {
                showListDialog(position);
            }
        });
        //班级二维码
        qrcodeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerTeacherActivity.this, ShareClassQrcode.class);
                intent.putExtra("className",className);
                intent.putExtra("qrcode",qrcodeContent);
                startActivity(intent);
            }
        });
    }

    private int selectPosition;
    private CustomListDialog customListDialog;
    private long time;

    private void showListDialog(int position)
    {

        if (silenceSeconds.get(selectPosition).getRoleType() == TIMGroupMemberRoleType.Owner || silenceSeconds.get(selectPosition).getRoleType() == TIMGroupMemberRoleType.Admin)
        {
            ToastUtils.makeText(ManagerTeacherActivity.this,getResources().getString(R.string.cannot_shut_up_teacher));
            return;
        }

        selectPosition = position;
        customListDialog = new CustomListDialog(ManagerTeacherActivity.this);
//            customListDialog.addData(getResources().getString(R.string.stop_one_talk), getResources().getString(R.string.delete));
        if (silenceSeconds.get(position).getShutUp())
        {
            customListDialog.addData(getResources().getString(R.string.cancle_stop_one_talk));
            time = 0;
        } else
        {
            customListDialog.addData(getResources().getString(R.string.stop_one_talk));
            time = 9223372036854775806L - 1;
        }
        customListDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                List<String> memlist = new ArrayList<>();
                memlist.add(postersBeanList.get(selectPosition).getIdentifier());
                if (position == 0)
                {

                    TIMGroupManager.getInstance().modifyGroupMemberInfoSetSilence(groupId, memlist.get(0), time, selenceCb);
                } else
                {
                    TIMGroupManager.getInstance().deleteGroupMember(groupId, memlist, deletCb);
                }
                customListDialog.dismiss();
            }
        });

        customListDialog.show();

    }

    // 禁言
    TIMCallBack selenceCb = new TIMCallBack()
    {
        @Override
        public void onError(int i, String s)
        {
            ToastUtils.makeText(ManagerTeacherActivity.this, getResources().getString(R.string.opera_fail) + s);
        }

        @Override
        public void onSuccess()
        {

            silenceSeconds.get(selectPosition).setShutUp(!silenceSeconds.get(selectPosition).getShutUp());
            stageadapter.notifyDataSetChanged();
            ToastUtils.makeText(ManagerTeacherActivity.this, getResources().getString(R.string.set_success));
        }
    };

    //删除群成员
    TIMValueCallBack<List<TIMGroupMemberResult>> deletCb = new TIMValueCallBack<List<TIMGroupMemberResult>>()
    {
        @Override
        public void onError(int i, String s)
        {
            ToastUtils.makeText(ManagerTeacherActivity.this, s);
        }

        @Override
        public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults)
        {

            postersBeanList.remove(selectPosition);
            stageadapter.notifyDataSetChanged();
            toolbar.setTitle(getResources().getString(R.string.message_manager) + "(" + postersBeanList.size() + ")");
        }
    };

    long silenceSecondTime = System.currentTimeMillis() / 1000;

    //获取群成员
    TIMValueCallBack<List<TIMGroupMemberInfo>> cb = new TIMValueCallBack<List<TIMGroupMemberInfo>>()
    {
        @Override
        public void onError(int code, String desc)
        {
        }

        @Override
        public void onSuccess(List<TIMGroupMemberInfo> infoList)
        {
            //参数返回群组成员信息


            silenceSeconds.clear();
            List<String> users = new ArrayList<>();
            for (TIMGroupMemberInfo info : infoList)
            {
                ManagerUserProfile managerUserProfile = new ManagerUserProfile();
                managerUserProfile.setShutUp(info.getSilenceSeconds() > silenceSecondTime);
                managerUserProfile.setRoleType(info.getRole());
                silenceSeconds.add(managerUserProfile);
                users.add(info.getUser());
            }
            TIMFriendshipManager.getInstance().getUsersProfile(users, usersCallBack);
        }
    };

    TIMValueCallBack<List<TIMUserProfile>> usersCallBack = new TIMValueCallBack<List<TIMUserProfile>>()
    {
        @Override
        public void onError(int i, String s)
        {

        }

        @Override
        public void onSuccess(List<TIMUserProfile> timUserProfiles)
        {
            postersBeanList.clear();
            postersBeanList.addAll(timUserProfiles);
            stageadapter.notifyDataSetChanged();
            toolbar.setTitle(getResources().getString(R.string.message_manager) + "(" + postersBeanList.size() + ")");
            hideLoadingView();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取班级状态
     */
    private void requestClassStatus(){
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_COURSE_CLASS)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        ClassBean bean = (ClassBean) resultBean;
                        String status = bean.getItem().getQrcode();
                        if (status.equals("0")) {
                            qrcodeLayout.setVisibility(View.GONE);
                        } else if(status.equals("1")){
                            qrcodeLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {

                    }
                })
                .setClassObj(ClassBean.class)
                .setPath(classId)
                .build();
    }

}