package com.coder.kzxt.poster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.poster.beans.PosterCategory;
import com.coder.kzxt.poster.fragment.PostersLikeFragment;
import com.coder.kzxt.poster.fragment.PostersPublishFragment;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.ToastUtils;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 我的海报
 *
 * @author pc
 */
public class MyPosterActivity extends BaseActivity implements HttpCallBack{

    private TabLayout tabs;
    private ViewPager pager;
    private ImageView leftImage;
    private ImageView rightImage;
    private DisplayMetrics dm;

    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private PostersPublishFragment publish_Fragment;
    private PostersLikeFragment like_Fragment;
    private ArrayList<String> userChannelList = new ArrayList<String>();
    private QuestionAdapter adapter;
    private CustomListDialog customDialog;
    private List<PosterCategory.ItemsBean> categoryData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poseter_my);
        initView();
        initListener();
        httpRequestType();
        initFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //控件
    private void initView() {
        leftImage = (ImageView) findViewById(R.id.leftImage);
        rightImage = (ImageView) findViewById(R.id.rightImage);
        rightImage.setBackgroundResource(R.drawable.poseter_publish);
        dm = getResources().getDisplayMetrics();
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        userChannelList.add(getResources().getString(R.string.publish_poster));
        userChannelList.add(getResources().getString(R.string.like));
        adapter = new QuestionAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(1);
    }

    //监听器
    private void initListener() {

        leftImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rightImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                  if(categoryData.size()==0){
                      ToastUtils.makeText(getApplicationContext() ,"获取海报分类失败", Toast.LENGTH_LONG).show();
                      return;
                  }
                customDialog = new CustomListDialog(MyPosterActivity.this);
                customDialog.addData(getResources().getString(R.string.image_word_poster), getResources().getString(R.string.only_words), getResources().getString(R.string.cancel));
                customDialog.show();
                customDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int ps) {
                        if (ps == 0) {
                            //获取颜色值
                            String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(MyPosterActivity.this, R.color.first_theme));
                            //设置最多选择几张图片
                            AndroidImagePicker.getInstance().setSelectLimit(1);
                            AndroidImagePicker.getInstance().pickMulti(MyPosterActivity.this, true, themeColor, new AndroidImagePicker.OnImagePickCompleteListener() {
                                @Override
                                public void onImagePickComplete(List<ImageItem> items) {
                                    try {
                                        if (items != null && items.size() > 0) {
                                            // 保存图片到sd卡
                                            String filename = System.currentTimeMillis() + "";
                                            String path = Bimp.saveBitmap(Bimp.revitionImageSize(items.get(0).path), "" + filename);

                                            // 发送图片版海报
                                            Intent intent = new Intent(MyPosterActivity.this, PublishImagePosterActivity.class);
                                            intent.putExtra("path", path);
                                            intent.putExtra("category", (Serializable) categoryData);
                                            startActivityForResult(intent, 4);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } else if (ps == 1) {
                            // 发送文字版海报
                            Intent intent = new Intent(MyPosterActivity.this, PublishTextPosterActivity.class);
                            intent.putExtra("category", (Serializable) categoryData);
                            startActivityForResult(intent, 4);
                        }

                        customDialog.cancel();

                    }
                });

            }
        });

    }

    private void httpRequestType() {
        //调用分类接口
        new HttpGetBuilder(this)
                .setHttpResult(this)
                .setClassObj(PosterCategory.class)
                .setUrl(UrlsNew.GET_POSTER_CATEGORY)
                .build();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();// 清空
        publish_Fragment = new PostersPublishFragment();
        like_Fragment = new PostersLikeFragment();
        fragments.add(publish_Fragment);
        fragments.add(like_Fragment);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        PosterCategory posterCategory = (PosterCategory) resultBean;
        for (int i = 0; i <posterCategory.getItems().size() ; i++) {
            categoryData.add(posterCategory.getItems().get(i));
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {

    }

    public class QuestionAdapter extends FragmentPagerAdapter {
        public QuestionAdapter(FragmentManager fm) {
            super(fm);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 4 && resultCode == 4) {
            // 刷新列表
            publish_Fragment.refreshHttpRequest();
        } else if (requestCode == 4 && resultCode == 5) {
            //刷新喜欢人数


//            String likeNum = data.getStringExtra("likeNum");
//            String postersId = data.getStringExtra("postersId");
//            publish_Fragment.onPostersLikeNumChange(postersId, likeNum);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}