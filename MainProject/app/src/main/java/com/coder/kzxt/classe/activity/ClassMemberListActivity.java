package com.coder.kzxt.classe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpDeleteBuilder;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.classe.beans.ClassMemberBean;
import com.coder.kzxt.classe.contacts.ChineseToEnglish;
import com.coder.kzxt.classe.contacts.CompareSort;
import com.coder.kzxt.classe.contacts.User;
import com.coder.kzxt.classe.contacts.UserAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.views.CustomNewDialog;

import java.util.ArrayList;
import java.util.Collections;


/**
 * 班级成员列表
 * Created by wangtingshun on 2017/6/10
 */

public class ClassMemberListActivity extends BaseActivity implements UserAdapter.OnClassMemberInfterface
{

    private Toolbar mToolbar;
    private RelativeLayout myLayout;
    private ListView mListview;
    private UserAdapter mAdapter;
    private TextView mTip;
    //  private SideBarView sideBarView;
    private String classId; //班级id
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button loadFailBtn;//加载失败
    private RelativeLayout contactsLayout;
    private CustomNewDialog deleteDialog;
    private ArrayList<ClassMemberBean.ClassMember> items;
    private String userId; //用户id
    private TextView addMember; //添加成员


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_member_list);
        classId = getIntent().getStringExtra("groupId");
        userId = getIntent().getStringExtra("userId");
        initView();
        initListener();
        requestData();
    }

    private void initView()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.class_member));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addMember = (TextView) mToolbar.findViewById(R.id.tv_add);
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);
        mListview = (ListView) findViewById(R.id.list_view);
        mTip = (TextView) findViewById(R.id.tip);
//        sideBarView = (SideBarView) findViewById(R.id.sidebar_view);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        loadFailBtn = (Button) findViewById(R.id.load_fail_button);
        contactsLayout = (RelativeLayout) findViewById(R.id.contacts_layout);
    }

    private void initListener()
    {
//        sideBarView.setOnLetterSelectListen(this);
        //重新加载
        loadFailBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadingPage();
                getClassMember();
            }
        });

        /**
         * 添加成员
         */
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClassMember();
            }
        });
    }

    private void requestData()
    {
        loadingPage();
        getClassMember();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home: //返回
                finish();
                break;
            case R.id.right_item: //右侧成员
//                addClassMember();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 添加班级成员
     */
    private void addClassMember()
    {
        Intent intent = new Intent(ClassMemberListActivity.this, SearchAddClassMemberActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bundle", items);
        intent.putExtra("classId", classId);
        intent.putExtra("memberList", bundle);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.right_menu, menu);
//        menu.findItem(R.id.right_item).setTitle(getResources().getString(R.string.button_add));
//        return super.onCreateOptionsMenu(menu);
//    }

    private void getClassMember()
    {
                 new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_CLASS_MEMBER)
                .setHttpResult(new HttpCallBack()
                {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean)
                    {
                        ClassMemberBean memberBean = (ClassMemberBean) resultBean;
                        items = memberBean.getItems();
                        if (items != null && items.size() > 0)
                        {
                            visibleData();
                            adapterData(items);
                        } else
                        {
                            noDataPage();
                        }
                        hideLoadingView();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg)
                    {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
                        {
                            NetworkUtil.httpRestartLogin(ClassMemberListActivity.this, myLayout);
                        } else
                        {
                            NetworkUtil.httpNetErrTip(ClassMemberListActivity.this, myLayout);
                        }
                        hideLoadingView();
                        loadFailPage();
                    }
                })
                .setClassObj(ClassMemberBean.class)
                .addQueryParams("group_id", classId)
                .build();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }


    //创建者id 只有我是创建者的时候才会赋值
    private String myCreaterId = "";

    public void adapterData(ArrayList<ClassMemberBean.ClassMember> classMembers)
    {
        int length = classMembers.size();
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < length; i++)
        {
            User user = new User();
            ClassMemberBean.ClassMember member = classMembers.get(i);
            ClassMemberBean.ClassMember.Profile profile = member.getProfile();
            if (profile != null)
            {
                String nickname = profile.getNickname();

                user.setClassMemberId(member.getId());
                user.setName(nickname);
                user.setIcon(profile.getAvatar());
                user.setRole(member.getRole());
                user.setUserId(member.getUser_id());

                String firstSpell = ChineseToEnglish.getFirstSpell(nickname);
                String substring = firstSpell.substring(0, 1).toUpperCase();
                if (substring.matches("[A-Z]"))
                {
                    user.setLetter(substring);
                } else
                {
                    user.setLetter("#");
                }
                users.add(user);

                if (userId.equals(member.getUser_id()) && member.getRole().equals("2"))
                {
                    myCreaterId = member.getUser_id();
                }
            }
        }

        for (int i = 0; i < length; i++)
        {
            ClassMemberBean.ClassMember classMember = classMembers.get(i);
            if (classMember.getRole().equals("2"))
            {
                ClassMemberBean.ClassMember.Profile profile = classMember.getProfile();
                if (profile != null)
                {
                    User user = new User();
                    user.setClassMemberId(classMember.getId());
                    user.setName(profile.getNickname());
                    user.setIcon(profile.getAvatar());
                    user.setLetter("@");
                    user.setRole(classMember.getRole());
                    user.setUserId(classMember.getUser_id());
                    users.add(user);
                }

                if(userId.equals(classMember.getUser_id())){
                    addMember.setEnabled(true);
                    addMember.setVisibility(View.VISIBLE);
                } else {
                    addMember.setEnabled(false);
                    addMember.setVisibility(View.GONE);
                }
            }
        }

        //排序
        Collections.sort(users, new CompareSort());
        //设置数据
        mAdapter = new UserAdapter(this, users, userId);
        mAdapter.setOnClassMemberListener(this);
        mListview.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


    /**
     * 暂无数据
     */
    private void noDataPage()
    {
        load_fail_layout.setVisibility(View.GONE);
        contactsLayout.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败页
     */
    private void loadFailPage()
    {
        load_fail_layout.setVisibility(View.VISIBLE);
        contactsLayout.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    /**
     * 显示数据
     */
    private void visibleData()
    {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        contactsLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载页
     */
    private void loadingPage()
    {
        jiazai_layout.setVisibility(View.VISIBLE);
        contactsLayout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    @Override
    public void onClassMemberItem(User user)
    {
        alterDeleteDialog(user);
    }

    private void alterDeleteDialog(final User user)
    {

        if (myCreaterId.equals("") || userId.equals(user.getUserId()))return;

        deleteDialog = new CustomNewDialog(this);
        deleteDialog.setMessage("确定删除此成员？");
        deleteDialog.setLeftText(getResources().getString(R.string.cancel));
        deleteDialog.setRightText(getResources().getString(R.string.btn_sure));
        deleteDialog.setLeftClick(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (deleteDialog.isShowing())
                {
                    deleteDialog.cancel();
                }
            }
        });

        deleteDialog.setRightClick(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (deleteDialog.isShowing())
                {
                    deleteDialog.cancel();
                }
                showLoadingView();
                deleteClassMember(user.getClassMemberId());
            }
        });
        deleteDialog.show();
    }

    private void deleteClassMember(String memberId)
    {
        new HttpDeleteBuilder(this)
                .setUrl(UrlsNew.GET_CLASS_MEMBER)
                .setHttpResult(new HttpCallBack()
                {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean)
                    {
                        getClassMember();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg)
                    {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
                        {
                            NetworkUtil.httpRestartLogin(ClassMemberListActivity.this, myLayout);
                        } else
                        {
                            NetworkUtil.httpNetErrTip(ClassMemberListActivity.this, myLayout);
                        }
                        ToastUtils.makeText(ClassMemberListActivity.this, msg, Toast.LENGTH_SHORT).show();
                        hideLoadingView();
                    }
                })
                .setClassObj(null)
                .setPath(memberId)
                .build();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == 9)
        {
            getClassMember();
        }
    }
}
