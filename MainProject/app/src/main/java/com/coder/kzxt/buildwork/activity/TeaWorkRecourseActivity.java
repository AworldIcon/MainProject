package com.coder.kzxt.buildwork.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.fragment.AllWorkBankFragment;
import com.coder.kzxt.buildwork.fragment.AlreadyPublishFragment;
import com.coder.kzxt.buildwork.views.ContainsEmojiEditText;
import com.coder.kzxt.dialog.util.CustomDialog;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.EToast;
import com.app.http.UrlsNew;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class TeaWorkRecourseActivity extends BaseActivity implements View.OnClickListener {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LinearLayout search_jiazai_layout;
    private TextView title;
    private int classId;//暂时没有传
    private int courseId;
    private String testId="";
    private int workType;//"1"是考试，“2”是作业
    private String isCenter = "0";
    private String pageTitle;
    private  List<Fragment> fragments = new ArrayList<>();
    private  AlreadyPublishFragment finishWorkFragment ;
    private AllWorkBankFragment unfinishFragment ;

    public static void gotoActivity(Context mContext,String course_id,String classId, String workType){

        mContext.startActivity(new Intent(mContext,TeaWorkRecourseActivity.class).putExtra("course_id",course_id).putExtra("workType",workType).putExtra("classId",classId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_work_recourse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.pics_menu));
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        initView();
        myReceiver=new MyReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.coder.kzxt.activity.time");
        registerReceiver(myReceiver,intentFilter);
    }
    private void initView()
    {
        search_jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        mTabLayout = (TabLayout) findViewById(R.id.teach_tablayout);
        title = (TextView) findViewById(R.id.toolbar_title);
        mViewPager = (ViewPager) findViewById(R.id.teach_viewpager);
        courseId = getIntent().getIntExtra("course_id", 4);
        classId = getIntent().getIntExtra("classId", 0);
        workType = getIntent().getIntExtra("workType",1);
        isCenter = getIntent().getStringExtra(Constants.IS_CENTER);
        title = (TextView) findViewById(R.id.toolbar_title);
        if(workType==1){
            title.setText(R.string.exame_manage);
            pageTitle="试卷库";
        }else {
            title.setText(R.string.workmanage);
            pageTitle="作业库";
        }


         finishWorkFragment = new AlreadyPublishFragment();
         unfinishFragment = new AllWorkBankFragment();
        // 传递数据到Fragment
        Bundle bundle = new Bundle();
        bundle.putInt("course_id", courseId);
        bundle.putInt("workType", workType);
        bundle.putInt("classId", classId);
        finishWorkFragment.setArguments(bundle);
        unfinishFragment.setArguments(bundle);
        fragments.add(finishWorkFragment);
        fragments.add(unfinishFragment);
        // 实例化Adapter
        Adapter adapter = new Adapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        // 使ViewPager与TabLayout进行联动
        mTabLayout.setupWithViewPager(mViewPager);
    }
    private CustomDialog customNewDialog;
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_setting:
                    msg += "Click setting";
                    customNewDialog = new CustomDialog(TeaWorkRecourseActivity.this);
                    View view= LayoutInflater.from(TeaWorkRecourseActivity.this).inflate(R.layout.add_work_layout,null);
                    TextView sure_btn= (TextView) view.findViewById(R.id.btn_sure);
                    TextView nagtive_btn= (TextView) view.findViewById(R.id.btn_false);
                    final ContainsEmojiEditText title= (ContainsEmojiEditText) view.findViewById(R.id.title);
                    if(workType==1){
                        title.setHint(R.string.build_exameName);
                    }else {
                        title.setHint(R.string.build_workName);
                    }
                    customNewDialog.setContentView(view);
                    customNewDialog.setRightTextColor(getResources().getColor(R.color.font_blue));
                    final Window dialogWindow = customNewDialog.getWindow();
                    dialogWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialogWindow.setGravity(Gravity.CENTER);
                    customNewDialog.show();
                    sure_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!TextUtils.isEmpty(title.getText())){
                                search_jiazai_layout.setVisibility(View.VISIBLE);
                                customNewDialog.dismiss();
                                sumbitWorkName(title.getText().toString(),String.valueOf(workType));

                            }else {
                                EToast.makeText(TeaWorkRecourseActivity.this,"标题不能为空", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    nagtive_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customNewDialog.dismiss();
                        }
                    });
                    break;
                case R.id.action_search:
                    Intent intent=new Intent(TeaWorkRecourseActivity.this,TeaSearchWorkActivity.class);
                    intent.putExtra("course_id", courseId);
                    intent.putExtra("workType", workType);
                    intent.putExtra("classId", classId);
                    intent.putExtra("position",mTabLayout.getSelectedTabPosition());
                    startActivity(intent);
                    break;
            }
            return true;
        }
    };
    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }
    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }
    private void sumbitWorkName(final String title, String type) {
        new HttpPostBuilder(this).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                search_jiazai_layout.setVisibility(View.GONE);
                String test_paper_id="";
                Log.d("zw--code",resultBean.toString());
                try {
                    test_paper_id= new JSONObject(resultBean.toString()).getJSONObject("item").optString("id","");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(TeaWorkRecourseActivity.this,BuildNewWorkActivity.class);
                intent.putExtra("courseId",courseId+"");
                intent.putExtra("testId",test_paper_id);
                intent.putExtra("workType",workType);
                intent.putExtra("name",title);
                intent.putExtra(Constants.IS_CENTER,isCenter);
                startActivity(intent);

            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                search_jiazai_layout.setVisibility(View.GONE);

            }
        })
                .setUrl(UrlsNew.TEST_PAPER_PUBLISH)
                .addBodyParam("course_id",courseId+"")
                .addBodyParam("title",title)
                .addBodyParam("type",type)
                .setClassObj(null)
                .build();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tea_work_menu, menu);
        if(workType==1){
            title.setText(R.string.exame_manage);
            menu.findItem(R.id.action_setting).setTitle(R.string.build_exame);
        }else {

            title.setText(R.string.workmanage);
            menu.findItem(R.id.action_setting).setTitle(R.string.build_work);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
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
            return position == 0 ? "已发放" : pageTitle;
        }
    }
    private MyReceiver myReceiver;
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.coder.kzxt.activity.time")){
                mViewPager.setCurrentItem(0);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Constants.LOGIN_BACK){
            finishWorkFragment.refreshData();
            unfinishFragment.refreshData();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
