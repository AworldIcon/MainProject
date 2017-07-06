package com.coder.kzxt.poster.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.poster.adapter.PosterClassifyPageAdapter;
import com.coder.kzxt.poster.adapter.PosterColorDelegate;
import com.coder.kzxt.poster.beans.PosterCategory;
import com.coder.kzxt.poster.beans.TextBgColor;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.views.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class PublishTextPosterActivity extends BaseActivity implements HttpCallBack {

    private EditText posters_type_ed;
    private TextView cu_number_tx_con;
    private LinearLayout my_srcoll;
    private AutoScrollViewPager viewpa;
    private LinearLayout viewGroup;
    private MyRecyclerView myListView;

    private ArrayList<ArrayList<PosterCategory.ItemsBean>> classifyList;// 分类数据
    private ImageView[] imageViewpoints;

    private SharedPreferencesUtil spu;
    private String cid = "";
//    private String bgColorId = "1";

    private BaseRecyclerAdapter<TextBgColor.ColorBean> baseRecyclerAdapter;
    private List<TextBgColor.ColorBean> data;
    private ArrayList<PosterCategory.ItemsBean> categorys;
    private String color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_publish_text);
        spu = new SharedPreferencesUtil(this);
        classifyList = new ArrayList<ArrayList<PosterCategory.ItemsBean>>();
        data = new ArrayList<>();
        categorys = new ArrayList<>();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(getResources().getString(R.string.create_poster));
        categorys = (ArrayList<PosterCategory.ItemsBean>) getIntent().getSerializableExtra("category");
        initFindViewById();
        initData();
        click();
        initclassify();
        color = "#c24a02";
    }


    private void initData(){
        List<TextBgColor.ColorBean> beanList = new ArrayList<>();
        ArrayList<String> colors  = new ArrayList<>();
        colors.add("#c24a02");
        colors.add("#74a108");
        colors.add("#8f055d");
        colors.add("#0c8fb2");
        colors.add("#ef9f10");

         for(int i=0;i<colors.size();i++){
             TextBgColor.ColorBean colorBean = new TextBgColor.ColorBean();
             colorBean.setId(i+1);
             colorBean.setColor(colors.get(i));
             beanList.add(colorBean);
         }

        baseRecyclerAdapter = new BaseRecyclerAdapter(PublishTextPosterActivity.this, data, new PosterColorDelegate());
        myListView.setAdapter(baseRecyclerAdapter);
        baseRecyclerAdapter.resetData(beanList);

    }

    private void initFindViewById() {

        my_srcoll = (LinearLayout) findViewById(R.id.my_srcoll);
        posters_type_ed = (EditText) findViewById(R.id.posters_type_ed);
        cu_number_tx_con = (TextView) findViewById(R.id.cu_number_tx_con);
        viewpa = (AutoScrollViewPager) findViewById(R.id.viewpager);
        viewGroup = (LinearLayout) findViewById(R.id.viewGroup);
        myListView = (MyRecyclerView) findViewById(R.id.horizontalListView);

    }

    private void click() {

        posters_type_ed.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 500) {
                    cu_number_tx_con.setText(s.length()+"");
                    cu_number_tx_con.setTextColor(ContextCompat.getColor(PublishTextPosterActivity.this, R.color.font_white));
                } else {
                    cu_number_tx_con.setText(s.length()+"");
                    cu_number_tx_con.setTextColor(ContextCompat.getColor(PublishTextPosterActivity.this, R.color.font_red));
                }
                invalidateOptionsMenu();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        baseRecyclerAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                baseRecyclerAdapter.setSelectPosition(position);
                TextBgColor.ColorBean hashMap = baseRecyclerAdapter.getSelectItem();
//                bgColorId = hashMap.getId() + "";
                color = hashMap.getColor();

                posters_type_ed.setBackgroundColor(Color.parseColor(color));

                baseRecyclerAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // 获取海报类型数据
    private void initclassify() {
//        int a = categorys.size();
//        int b = a / 8;
//        if (a % 8 != 0) {
//            b = b + 1;
//        }
//        for (int j = 0; j < b; j++) {
//            ArrayList<PosterCategory.ItemsBean> arrayList = new ArrayList<PosterCategory.ItemsBean>();
//            if (j == b - 1&&a % 8 != 0) {
//                for (int p = 0; p < a % 8; p++) {
//                    arrayList.add(p, categorys.get(j * 8 + p));
//                }
//            } else {
//                for (int p = 0; p < 8; p++) {
//                    arrayList.add(p, categorys.get(j * 8 + p));
//                }
//            }
//            classifyList.add(arrayList);
//        }
        classifyList.add(categorys);
//        if (classifyList.size() > 1) {
//            cid = categorys.get(0).getId() + "";
//            viewGroup.setVisibility(View.VISIBLE);
//        } else {
//            viewGroup.setVisibility(View.GONE);
//        }
//        imageViewpoints = new ImageView[classifyList.size()];
//
//        for (int i = 0; i < imageViewpoints.length; i++) {
//            imageViewpoints[i] = new ImageView(PublishTextPosterActivity.this);
//            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, 0, (int) getResources().getDimension(R.dimen.woying_6_dip), 0);
//            params.weight = 1.0f;
//            imageViewpoints[i].setLayoutParams(params);
//            if (i == 0) {
//                imageViewpoints[i].setBackgroundResource(R.drawable.bannerblack);
//            } else {
//                imageViewpoints[i].setBackgroundResource(R.drawable.bannerwhite);
//            }
//            viewGroup.addView(imageViewpoints[i]);
//        }
        viewpa.setAdapter(new PosterClassifyPageAdapter(PublishTextPosterActivity.this, classifyList).setInfiniteLoop(false));
        viewpa.stopAutoScroll();
        viewpa.setOnPageChangeListener(new MyOnPageChangeListener());


    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        hideLoadingView();
        setResult(4);
        ToastUtils.makeText(getApplicationContext(),getResources().getString(R.string.publish_success), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        ToastUtils.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();

    }

    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            // 算出imageViews里的角标
            position = position % classifyList.size();
            for (int i = 0; i < imageViewpoints.length; i++) {
                imageViewpoints[position].setBackgroundResource(R.drawable.bannerblack);
                if (position != i) {
                    imageViewpoints[i].setBackgroundResource(R.drawable.bannerwhite);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.right_item);
        SpannableString s = new SpannableString(getResources().getString(R.string.publish));
//        if (TextUtils.isEmpty(posters_type_ed.getText().toString())) {
//            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PublishTextPosterActivity.this, R.color.font_gray)), 0, s.length(), 0);
//        } else {
//            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PublishTextPosterActivity.this, R.color.white)), 0, s.length(), 0);
//        }
        menuItem.setTitle(s);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(4);
                finish();
                break;

            case R.id.right_item:
                if(TextUtils.isEmpty(posters_type_ed.getText().toString())){
                    ToastUtils.makeText(getApplicationContext(),getResources().getString(R.string.input_poster_content),Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(cid)){
                    ToastUtils.makeText(getApplicationContext(),getResources().getString(R.string.choice_poster_type), Toast.LENGTH_LONG).show();
                } else {
                    showLoadingView();
                    new HttpPostBuilder(this)
                            .setUrl(UrlsNew.GET_POSTER)
                            .setClassObj(BaseBean.class)
                            .setHttpResult(this)
                            .addBodyParam("type","2")
                            .addBodyParam("desc",posters_type_ed.getText().toString())
                            .addBodyParam("status","1")
                            .addBodyParam("poster_category_id",cid)
                            .addBodyParam("user_id",spu.getUid())
                            .addBodyParam("bg_color",color)
                            .build();
                }

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_CENTER:// 屏蔽editext输入的回车键
            case KeyEvent.KEYCODE_ENTER:
                return true;
        }
        return super.dispatchKeyEvent(event);
    }

}