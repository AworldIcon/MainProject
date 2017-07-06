package com.coder.kzxt.gambit.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpGetOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.gambit.activity.bean.ClassGambitsBean;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.Urls;
import java.util.ArrayList;

public class ClassGambitsActivity extends BaseActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView title;
    private RelativeLayout activity_class_gambits;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<String> userChannelList = new ArrayList<String>();
    private String classId;
    private PagerSlidingTabStripAdapter tabStripAdapter;
    private String canSubmitTopic="";
    private MenuItem menuItem;
    private LinearLayout load_fail_layout;
    private Button load_fail_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_gambits);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        classId = getIntent().getStringExtra("classId");
        ininViews();
        httpRequest();
    }

    private void httpRequest() {
        HttpGetOld httpGet=new HttpGetOld(this, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if(code==Constants.HTTP_CODE_SUCCESS){
                    ClassGambitsBean classGambitsBean= (ClassGambitsBean) baseBean;
                    canSubmitTopic=classGambitsBean.getCanSubmitTopic();
                    if(classGambitsBean.getCanSubmitTopic().equals("0")){
                        menuItem.setTitle("");
                    }else {
                        if(menuItem!=null){
                            menuItem.setTitle(R.string.publish);
                        }

                    }
                    fragments.clear();// 清空
                    for(int i=0;i<classGambitsBean.getTypeList().size();i++){
                        String type = classGambitsBean.getTypeList().get(i).getType();
                        String name = classGambitsBean.getTypeList().get(i).getName();
                        Bundle data = new Bundle();
                        data.putString("type", type);
                        data.putString("classId", classId);
                        userChannelList.add(name);
                        ClassGambitFragment classGambitFragment = new ClassGambitFragment();
                        classGambitFragment.setArguments(data);
                        fragments.add(classGambitFragment);
                    }
                    tabStripAdapter = new PagerSlidingTabStripAdapter(getSupportFragmentManager(), userChannelList, fragments);
                    mViewPager.setAdapter(tabStripAdapter);
                    // 使ViewPager与TabLayout进行联动
                    mTabLayout.setupWithViewPager(mViewPager);
                    mViewPager.setOffscreenPageLimit(fragments.size());
                }else if(code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
                    //重新登录
                    NetworkUtil.httpRestartLogin(ClassGambitsActivity.this,activity_class_gambits);
                }else {
                    //加载失败
                    load_fail_layout.setVisibility(View.VISIBLE);
                    NetworkUtil.httpNetErrTip(ClassGambitsActivity.this,activity_class_gambits);
                }

            }
        }, ClassGambitsBean.class, Urls.GET_CLASS_THREAD_APPRAISE_ACTION,classId,"20","1","allClassTopic");
        httpGet.excute();
    }

    private void ininViews() {
        load_fail_layout= (LinearLayout) findViewById(R.id.load_fail_layout);
        activity_class_gambits= (RelativeLayout) findViewById(R.id.activity_class_gambits);
        load_fail_button= (Button) findViewById(R.id.load_fail_button);
        mTabLayout = (TabLayout) findViewById(R.id.teach_tablayout);
        title = (TextView) findViewById(R.id.toolbar_title);
        mViewPager = (ViewPager) findViewById(R.id.teach_viewpager);
        title.setText(R.string.class_gambit);
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequest();
            }
        });
    }
    public class PagerSlidingTabStripAdapter extends FragmentPagerAdapter {
        private ArrayList<String> userChannelList;
        private ArrayList<Fragment> fragments;


        public PagerSlidingTabStripAdapter(FragmentManager fm, ArrayList<String> userChannelList, ArrayList<Fragment> fragments) {
            super(fm);
            this.userChannelList = userChannelList;
            this.fragments = fragments;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return userChannelList.get(position);
        }

        @Override
        public int getCount() {
            return userChannelList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (int i = 0; i <fragments.size() ; i++) {
            ClassGambitFragment fragment = (ClassGambitFragment) fragments.get(i);
            fragment.refreshView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();

        menuInflater.inflate(R.menu.tea_work_menu,menu);
        menuItem=menu.findItem(R.id.action_setting);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(canSubmitTopic.equals("0")){

        }else {
            if(item.getItemId()==R.id.action_setting){
                Intent intent = new Intent(ClassGambitsActivity.this, PublishGambitActivity.class);
                intent.putExtra("commitId", classId);
                intent.putExtra("commitType","gambit");
                intent.putExtra(Constants.IS_CENTER, "0");
                startActivityForResult(intent, 1);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
