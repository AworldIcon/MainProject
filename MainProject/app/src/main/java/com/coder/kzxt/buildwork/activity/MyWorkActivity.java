package com.coder.kzxt.buildwork.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.entity.FinishNum;
import com.coder.kzxt.buildwork.fragment.FinishWorkFragment;
import com.coder.kzxt.buildwork.fragment.UnfinishFragment;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class MyWorkActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener
{
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FinishNum finishNum;
    private RelativeLayout activity_my_work;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button load_fail_button;
    private TextView title;
    private String isCenter = "";
    private String testId = "";
    private String name = "",class_name;
    private int workType;
    private RadioGroup radiogroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private int totalNum;
    private int unfinish_num;
    private int finish_num;
    private MyReceiver myReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_work);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setOnMenuItemClickListener(this);
        myReceiver=new MyReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("update_work_finishnum");
        intentFilter.addAction("update_work_unfinishnum");
        registerReceiver(myReceiver,intentFilter);
        initView();
    }

    private void initView()
    {
        isCenter = getIntent().getStringExtra(Constants.IS_CENTER);
        unfinish_num = getIntent().getIntExtra("unfinish_num",0);
        finish_num = getIntent().getIntExtra("finish_num",0);
        totalNum=finish_num+unfinish_num;
        testId = getIntent().getStringExtra("testId");
        name = getIntent().getStringExtra("name");
        class_name = getIntent().getStringExtra("class_name")!=null?getIntent().getStringExtra("class_name"):"";
        workType = getIntent().getIntExtra("workType", 2);
        search_jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        activity_my_work = (RelativeLayout) findViewById(R.id.activity_my_work);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        mTabLayout = (TabLayout) findViewById(R.id.teach_tablayout);
        radiogroup= (RadioGroup) findViewById(R.id.radiogroup);
        radioButton1= (RadioButton) findViewById(R.id.radio_left);
        radioButton2= (RadioButton) findViewById(R.id.radio_right);
        title = (TextView) findViewById(R.id.toolbar_title);
        mViewPager = (ViewPager) findViewById(R.id.teach_viewpager);
//        mViewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });
        if (workType == 1)
        {
            title.setText(name + "-考试");

        } else {
            title.setText(name + "-作业");
        }
        getWorkData();
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radio_left){
                    mViewPager.setCurrentItem(0);
                }else {
                    mViewPager.setCurrentItem(1);
                }
            }
        });
    }
    private Adapter adapter;
    private FinishWorkFragment finishWorkFragment;
    private UnfinishFragment unfinishFragment;
    private void getWorkData()
    {
        List<Fragment> data = new ArrayList<>();
         finishWorkFragment = new FinishWorkFragment();
         unfinishFragment = new UnfinishFragment();
        // 传递数据到Fragment
        Bundle bundle = new Bundle();
        bundle.putString("testId", testId);
        bundle.putString("name", name);
        bundle.putString("class_name", class_name);
        bundle.putInt("workType", workType);
        finishWorkFragment.setArguments(bundle);
        unfinishFragment.setArguments(bundle);
        data.add(finishWorkFragment);
        data.add(unfinishFragment);
        // 实例化Adapter
         adapter = new Adapter(getSupportFragmentManager(), data);
        mViewPager.setAdapter(adapter);
        // 使ViewPager与TabLayout进行联动
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.tea_work_menu, menu);
        menu.findItem(R.id.action_setting).setVisible(false);
//        if (workType == 1)
//        {
//            menu.findItem(R.id.action_setting).setTitle("查看考试");
//        } else
//        {
//            menu.findItem(R.id.action_setting).setTitle("查看作业");
//        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_setting:
//                Intent intent = new Intent(MyWorkActivity.this, PublishWorkActivity.class);
//                intent.putExtra("testId", testId);
//                intent.putExtra("detail", "my_work");
//                intent.putExtra("title", name);
//                intent.putExtra("workType", workType);
//                intent.putExtra(Constants.IS_CENTER, isCenter);
//                startActivity(intent);
                break;
            case R.id.action_search:
                Intent intent1 = new Intent(MyWorkActivity.this, TeaSearchStuActivity.class);
                intent1.putExtra("testId", testId);
                intent1.putExtra("name", name);
                intent1.putExtra("class_name", class_name);
                intent1.putExtra("workType", workType);
                intent1.putExtra("position", mTabLayout.getSelectedTabPosition());
                startActivity(intent1);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v)
    {
        finish();
    }

    public class Adapter extends FragmentPagerAdapter
    {

        private List<Fragment> data;

        public Adapter(FragmentManager fm, List<Fragment> data)
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

        /**
         * 与TabLayout进行联动，需重写此方法
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position)
        {
            return position == 0 ? "已交"+finish_num + "人" : "未交" + unfinish_num+ "人";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==200){
            finishWorkFragment.update();
        }
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("update_work_finishnum")){
                finish_num=intent.getIntExtra("num",finish_num);
                mTabLayout.getTabAt(0).setText( "已交"+finish_num + "人");
                mTabLayout.getTabAt(1).setText("未交" + (totalNum-finish_num)+ "人");
            }
            if(intent.getAction().equals("update_work_unfinishnum")){
                unfinish_num=intent.getIntExtra("num",unfinish_num);
                mTabLayout.getTabAt(0).setText( "已交"+(totalNum-unfinish_num) + "人");
                mTabLayout.getTabAt(1).setText("未交" + unfinish_num+ "人");
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }
}
