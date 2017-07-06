package com.coder.kzxt.poster.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPostFileBuilder;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.login.beans.AvatarBean;
import com.coder.kzxt.poster.adapter.PosterClassifyPageAdapter;
import com.coder.kzxt.poster.beans.PosterCategory;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.views.AutoScrollViewPager;
import com.coder.kzxt.views.StgImageView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublishImagePosterActivity extends BaseActivity  implements HttpCallBack {

    private String path;
    private StgImageView posters_img;
    private TextView replace_tx;
    private EditText posters_text;
    private TextView cu_number_con;
    private ScrollView my_srcoll;
    private AutoScrollViewPager viewpa;
    private LinearLayout viewGroup;
    private ImageView[] imageViewpoints;
    private ArrayList<ArrayList<PosterCategory.ItemsBean>> classifyList;// 分类数据
    private SharedPreferencesUtil spu;
    private String cid = "";
    private ArrayList<PosterCategory.ItemsBean> categorys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_publish);
        spu = new SharedPreferencesUtil(this);
        initFindViewById();
        initclassify();
        click();
    }


    private void initFindViewById() {

        classifyList = new ArrayList<ArrayList<PosterCategory.ItemsBean>>();
        path = getIntent().getStringExtra("path");
        categorys = (ArrayList<PosterCategory.ItemsBean>) getIntent().getSerializableExtra("category");
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(getResources().getString(R.string.create_poster));

        my_srcoll = (ScrollView) findViewById(R.id.my_srcoll);
        posters_img = (StgImageView) findViewById(R.id.posters_img);
        posters_text = (EditText) findViewById(R.id.posters_text);
        cu_number_con = (TextView) findViewById(R.id.cu_number_con);
        replace_tx = (TextView) findViewById(R.id.replace_tx);
        viewpa = (AutoScrollViewPager) findViewById(R.id.viewpager);
        viewGroup = (LinearLayout) findViewById(R.id.viewGroup);
        if (path != null) {
            GlideUtils.loadPorstersImg(PublishImagePosterActivity.this, path, posters_img);
        }

    }

    private void click() {

        posters_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 500) {
                    cu_number_con.setText(s.length() + "");
                    cu_number_con.setTextColor(ContextCompat.getColor(PublishImagePosterActivity.this, R.color.font_gray));
                } else {
                    cu_number_con.setText(s.length() + "");
                    cu_number_con.setTextColor(ContextCompat.getColor(PublishImagePosterActivity.this, R.color.font_red));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        replace_tx.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
                  //获取颜色值
                  String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(PublishImagePosterActivity.this, R.color.first_theme));
                  //设置最多选择几张图片
                  AndroidImagePicker.getInstance().setSelectLimit(1);
                  AndroidImagePicker.getInstance().pickMulti(PublishImagePosterActivity.this, true, themeColor, new AndroidImagePicker.OnImagePickCompleteListener() {
                      @Override
                      public void onImagePickComplete(List<ImageItem> items) {
                          L.v("tangcy" ,"当前选择的图片"+path);
                          try {
                              if (items != null && items.size() > 0) {
                                  // 保存图片到sd卡
                                  String filename = System.currentTimeMillis() + "";
                                  path = Bimp.saveBitmap(Bimp.revitionImageSize(items.get(0).path), "" + filename);
                                  GlideUtils.loadPorstersImg(PublishImagePosterActivity.this, path, posters_img);
                              }
                          } catch (Exception e) {
                              e.printStackTrace();
                          }

                      }
                  });
              }
            }

        );

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    // 获取海报类型数据
    private void initclassify() {
//        int a =categorys.size();
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
//            cid =categorys.get(0).getId() + "";
//            viewGroup.setVisibility(View.VISIBLE);
//        } else {
//            viewGroup.setVisibility(View.GONE);
//        }
//        imageViewpoints = new ImageView[classifyList.size()];

//        for (int i = 0; i < imageViewpoints.length; i++) {
//            imageViewpoints[i] = new ImageView(PublishImagePosterActivity.this);
//            LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
        viewpa.setAdapter(new PosterClassifyPageAdapter(PublishImagePosterActivity.this, classifyList).setInfiniteLoop(false));
        viewpa.stopAutoScroll();
        viewpa.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if(requestCode==1000){
            AvatarBean avatarBean = (AvatarBean) resultBean;
            String  url = avatarBean.getItems().get(0);
            new HttpPostFileBuilder(this)
                    .setUrl(UrlsNew.GET_POSTER)
                    .setClassObj(BaseBean.class)
                    .setHttpResult(this)
                    .addBodyParam("type","1")
                    .addBodyParam("desc",posters_text.getText().toString())
                    .addBodyParam("poster_category_id",cid)
                    .addBodyParam("user_id",spu.getUid())
                    .addBodyParam("status","1")
                    .addBodyParam("pic",url)
                    .setRequestCode(1001)
                    .build();
        }

        if(requestCode==1001){
            hideLoadingView();
            setResult(4);
            ToastUtils.makeText(getApplicationContext(),getResources().getString(R.string.publish_success), Toast.LENGTH_LONG).show();
            finish();
        }

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
        menu.findItem(R.id.right_item).setTitle(getResources().getString(R.string.publish));
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

              if(!Bimp.getBitmapWH(path)){
                    ToastUtils.makeText(getApplicationContext(),"请发送宽度大于320的图片", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(cid)){
                  ToastUtils.makeText(getApplicationContext(),getResources().getString(R.string.choice_poster_type), Toast.LENGTH_LONG).show();
               } else {
                    showLoadingView();
                    HashMap<String,String> postImages=new HashMap<>();
                    postImages.put("file" + 0 ,path);

                    new HttpPostFileBuilder(this)
                            .setUrl(UrlsNew.POST_SYSTEM_FILE)
                            .setFileNames(postImages)
                            .setClassObj(AvatarBean.class)
                            .setHttpResult(this)
                            .addQueryParams("type","avatar")
                            .setRequestCode(1000)
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