package com.coder.kzxt.classe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.classe.adapter.ClassTopicAdapter;
import com.coder.kzxt.classe.beans.MyCreateClass;
import com.coder.kzxt.classe.fragment.AllTopicFragment;
import com.coder.kzxt.classe.fragment.CollectTopicFragment;
import com.coder.kzxt.classe.fragment.MyPublicTopicFragment;
import com.coder.kzxt.classe.fragment.TopicEssenceFragment;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/12.
 * 班级话题
 */

public class ClassTopicActivity extends BaseActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private ArrayList<String> tips = new ArrayList<>();
    private ClassTopicAdapter topicAdapter;
    private SharedPreferencesUtil spu;
    private ArrayList<Fragment> fragments = new ArrayList<>();
//    private ClassTopicFragment topFragment;
    private Fragment fragment;
    private AllTopicFragment allTopicFragment; //全部
    private MyPublicTopicFragment myPublicTopicFragment; //我发表的
    private CollectTopicFragment collectTopicFragment; //收藏的
    private TopicEssenceFragment topicEssenceFragment; //话题精华
    private MyCreateClass.CreateClassBean classBean;
    private RelativeLayout publicTopic;
    private String selfRole; //
    private String joinState;
    private int pageIndex = 0;
    private RelativeLayout pagerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_topic_layout);
        spu = new SharedPreferencesUtil(this);
        classBean = (MyCreateClass.CreateClassBean) getIntent().getSerializableExtra("classBean");
        selfRole = getIntent().getStringExtra("selfRole");
        joinState = getIntent().getStringExtra("joinState");
        initTips();
        initFragment();
        initView();
        initListener();
    }

    private void initTips() {
        tips.add("全部话题");
        if (!selfRole.equals("0")) {
            tips.add("我发表的");
            tips.add("我收藏的");
            tips.add("精华");
        }
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.gambit));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout= (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        publicTopic = (RelativeLayout) findViewById(R.id.public_topic_layout);
        pagerLayout = (RelativeLayout) findViewById(R.id.pagerLayout);

        topicAdapter = new ClassTopicAdapter(getSupportFragmentManager(),fragments,tips);
        mViewPager.setAdapter(topicAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(fragments.size() - 1);

        if (selfRole.equals("0")) {
            publicTopic.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams =( RelativeLayout.LayoutParams)pagerLayout.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
            pagerLayout.setLayoutParams(layoutParams);
        } else {
            publicTopic.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageIndex = position;
                fragment = fragments.get(position);
                changeFragment();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //发表话题
        publicTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassTopicActivity.this,PublicTopicActivity.class);
                intent.putExtra("classId",classBean.getId());
                intent.putExtra("pageIndex",pageIndex);
                startActivityForResult(intent, Constants.REQUEST_CODE);
            }
        });
    }

    public void changeFragment() {
        if (fragment instanceof AllTopicFragment) {
            allTopicFragment.updataData();
        } else if (fragment instanceof MyPublicTopicFragment) {
            myPublicTopicFragment.updataData();
        } else if (fragment instanceof CollectTopicFragment) {
            collectTopicFragment.updataData();
        } else if (fragment instanceof TopicEssenceFragment) {
            topicEssenceFragment.updataData();
        }
    }


    private void initFragment() {
//        for (int i = 0; i < tips.size(); i++) {
//             topFragment = new ClassTopicFragment();
//             Bundle bundle = new Bundle();
//             bundle.putString("selfRole",selfRole);
//             bundle.putString("joinState",joinState);
//             bundle.putSerializable("classBean",classBean);
//             topFragment.setArguments(bundle);
//             fragments.add(topFragment);
//        }
        Bundle bundle = new Bundle();
        bundle.putString("selfRole",selfRole);
        bundle.putString("joinState",joinState);
        bundle.putSerializable("classBean",classBean);
        allTopicFragment = new AllTopicFragment();
        allTopicFragment.setArguments(bundle);
        fragments.add(allTopicFragment);
        if(!selfRole.equals("0")){
            myPublicTopicFragment = new MyPublicTopicFragment();
            myPublicTopicFragment.setArguments(bundle);
            fragments.add(myPublicTopicFragment);
            collectTopicFragment = new CollectTopicFragment();
            collectTopicFragment.setArguments(bundle);
            fragments.add(collectTopicFragment);
            topicEssenceFragment = new TopicEssenceFragment();
            topicEssenceFragment.setArguments(bundle);
            fragments.add(topicEssenceFragment);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //返回
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == 10) {
            int pageIndice = data.getIntExtra("pageIndex", -1);
            if (pageIndice >= 0) {
                fragment = fragments.get(pageIndice);
            }
            if (fragment instanceof AllTopicFragment) {
                allTopicFragment.onRefresh();
            } else if (fragment instanceof MyPublicTopicFragment) {
                myPublicTopicFragment.onRefresh();
            } else if (fragment instanceof CollectTopicFragment) {
                collectTopicFragment.onRefresh();
            } else if (fragment instanceof TopicEssenceFragment) {
                topicEssenceFragment.onRefresh();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
