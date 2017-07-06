package com.coder.kzxt.question.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.question.fragment.CourseQuestionFragment;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.video.beans.CourseRoleResult;

import java.util.ArrayList;
import java.util.List;

public class CourseQuestionsActivity extends BaseActivity implements HttpCallBack {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private int isJoin;
    private List<String>typeList=new ArrayList<>();
    private int courseId;
    private Pagerdapter pagerdapter;
    private MyReceiver myReceiver;
    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_questions);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout= (TabLayout) findViewById(R.id.question_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.question_viewpager);
        toolbar.setTitle(R.string.questions);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isJoin=getIntent().getIntExtra("isJoin",0);
        myReceiver=new MyReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Constants.QUESTION_UPDATE);
        intentFilter.addAction("from_activity");
        registerReceiver(myReceiver,intentFilter);
        sp=new SharedPreferencesUtil(this);
        courseId=getIntent().getIntExtra("courseId",4);
        getPagerData();
        pagerdapter=new Pagerdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(pagerdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(fragments.size()-1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragments.get(position).refresh();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Constants.RES_BACK_AC);
                finish();
            }
        });
    }
    private List<CourseQuestionFragment> fragments = new ArrayList<>();
    private void getPagerData() {
        if(!TextUtils.isEmpty(sp.getIsLogin())){
            if(isJoin == 2 || isJoin == 3){
                for(int i=0;i<7;i++){
                    CourseQuestionFragment  courseQuestionFragment=new CourseQuestionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("courseId", courseId);
                    bundle.putInt("type", i);
                    bundle.putInt("isJoin", isJoin);
                    courseQuestionFragment.setArguments(bundle);
                    fragments.add(courseQuestionFragment);
                }
                typeList.add("全部");
                typeList.add("已解决");
                typeList.add("未解决");
                typeList.add("精华");
                typeList.add("我发表的");
                typeList.add("我收藏的");
                typeList.add("我回复的");
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }else {
                if(isJoin==1){
                    for(int i=0;i<7;i++){
                        CourseQuestionFragment  courseQuestionFragment=new CourseQuestionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("courseId", courseId);
                        bundle.putInt("type", i);
                        bundle.putInt("isJoin", isJoin);
                        courseQuestionFragment.setArguments(bundle);
                        fragments.add(courseQuestionFragment);
                    }
                    typeList.add("全部");
                    typeList.add("已解决");
                    typeList.add("未解决");
                    typeList.add("精华");
                    typeList.add("我发表的");
                    typeList.add("我收藏的");
                    typeList.add("我回复的");
                    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                }else {
                    typeList.add("全部");
                    typeList.add("已解决");
                    typeList.add("未解决");
                    typeList.add("精华");
                    typeList.add("我收藏的");
                    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                    for(int i=0;i<typeList.size();i++){
                        CourseQuestionFragment  courseQuestionFragment=new CourseQuestionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("courseId", courseId);
                        if(i==typeList.size()-1){
                            bundle.putInt("type", 5);
                        }else {
                            bundle.putInt("type", i);
                        }
                        bundle.putInt("isJoin", isJoin);
                        courseQuestionFragment.setArguments(bundle);
                        fragments.add(courseQuestionFragment);
                    }
                }
            }
            }else {
            typeList.add("全部");
            typeList.add("已解决");
            typeList.add("未解决");
            typeList.add("精华");
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            for(int i=0;i<typeList.size();i++){
                CourseQuestionFragment  courseQuestionFragment=new CourseQuestionFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("courseId", courseId);
                bundle.putInt("type", i);
                bundle.putInt("isJoin", isJoin);
                courseQuestionFragment.setArguments(bundle);
                fragments.add(courseQuestionFragment);
            }
        }
    }



    public class Pagerdapter extends FragmentStatePagerAdapter
    {

        private List<CourseQuestionFragment> data;

        public Pagerdapter(FragmentManager fm, List<CourseQuestionFragment> data)
        {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position)
        {
            return data.get(position);
        }

        @Override
        public int getCount()
        {
            return data != null ? data.size() : 0;
        }
        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return PagerAdapter.POSITION_NONE;
        }
        /**
         * 与TabLayout进行联动，需重写此方法
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position)
        {
            return typeList.get(position);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tea_work_menu, menu);
        menu.findItem(R.id.action_setting).setTitle("我要提问").setIcon(null);
        menu.findItem(R.id.action_search).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_setting){
            if(TextUtils.isEmpty(sp.getIsLogin())){
                startActivityForResult(new Intent(CourseQuestionsActivity.this, LoginActivity.class), 1);
            }else if(isJoin==0){
                ToastUtils.makeText(CourseQuestionsActivity.this,getResources().getString(R.string.remind_join));
            }else {
                startActivityForResult(new Intent(this, PublishQuestionActivity.class).putExtra("commitType","question").putExtra("courseId",courseId).putExtra("isJoin",isJoin),1);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.d("zw--",requestCode+"++"+resultCode);
        if(requestCode==1&&resultCode==1){
            fragments.get(mViewPager.getCurrentItem()).refresh();
        }
        if(requestCode==1&&resultCode==Constants.LOGIN_BACK){
            sendBroadcast(new Intent().setAction(Constants.QUESTION_LOGIN));
            new HttpGetBuilder(CourseQuestionsActivity.this)
                    .setHttpResult(this)
                    .setClassObj(CourseRoleResult.class)
                    .setUrl(UrlsNew.GET_COURSE_ROLE)
                    .addQueryParams("course_id", courseId+"")
                    .setRequestCode(2)
                    .build();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        CourseRoleResult courseClassBean = (CourseRoleResult) resultBean;
        //0未加入 1学生 2老师 3创建课程的老师
        isJoin = courseClassBean.getItem().getRole();
        fragments.clear();
        typeList.clear();
        getPagerData();
        pagerdapter.notifyDataSetChanged();
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {

    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }

    public class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.QUESTION_UPDATE)){
                    fragments.get(mViewPager.getCurrentItem()).refresh();//只刷新当前选择的item，其它的page切换时候自动刷新
            }
            if(intent.getAction().equals("from_activity")){
                Log.d("zw----",isJoin+"++");
                isJoin=intent.getIntExtra("isJoin",isJoin);
                fragments.clear();
                typeList.clear();
                getPagerData();
                pagerdapter.notifyDataSetChanged();
            }
        }
    }
}
