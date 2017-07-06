package com.coder.kzxt.classe.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPostFileBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.buildwork.views.ContainsEmojiEditText;
import com.coder.kzxt.login.beans.AvatarBean;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Utils;
import com.coder.kzxt.views.CustomNewDialog;
import com.coder.kzxt.views.MyGridView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by wangtingshun on 2017/6/13.
 * 发表话题
 */
public class PublicTopicActivity extends BaseActivity {

    private Toolbar toolbar;
    private ContainsEmojiEditText etTitle;  // 标题
    private Button publicTopic; //发表话题
    private ContainsEmojiEditText contentText; //话题内容
    private PermissionsUtil permissionsUtil;
    private String path;
    private RelativeLayout myLayout;
    private CustomNewDialog exitEditDialog;
    private String classId;
    private String topicTitle; //话题title
    private String topicContent; //话题内容
    private MyGridView imgs_gv;
    private ImageBucketAdapter adapter;
    private LinearLayout jiazai_layout;
    private TextView titleSum; //标题当前数量
    private TextView contentSum;  //标题总数量
    private int titleLength;
    private int contentLength;
    private int pageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_topic_layout);
        if (savedInstanceState != null) {
            path = savedInstanceState.getString(Constants.IMAGE_FILE_PATH);
        }
        permissionsUtil = new PermissionsUtil(this);
        classId = getIntent().getStringExtra("classId");
        pageIndex = getIntent().getIntExtra("pageIndex",-1);
        initView();
        initListener();
    }

    private void initView() {
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.publish_topic));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etTitle = (ContainsEmojiEditText) findViewById(R.id.et_title);
        contentText = (ContainsEmojiEditText) findViewById(R.id.content_et);
        publicTopic = (Button) findViewById(R.id.tv_public_topic);
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);
        imgs_gv = (MyGridView) findViewById(R.id.imgs_gv);
        titleSum = (TextView) findViewById(R.id.titleSum);
        contentSum = (TextView) findViewById(R.id.contentSum);
        adapter = new ImageBucketAdapter();
        imgs_gv.setAdapter(adapter);
        publicTopic.setEnabled(false);
        publicTopic.setAlpha(0.5f);
        popupSoftKeybord(etTitle);
    }

    private void initListener() {
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                topicTitle = etTitle.getText().toString().trim();
                titleLength = topicTitle.toString().length();
                if (titleLength == 20) {
                    titleSum.setTextColor(getResources().getColor(R.color.font_red));
                } else {
                    titleSum.setTextColor(getResources().getColor(R.color.font_gray));
                }
                titleSum.setText(String.valueOf(titleLength));
                changePublicState();
            }
        });

        contentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                topicContent = contentText.getText().toString().trim();
                contentLength = topicContent.toString().length();
                if (contentLength == 20) {
                    titleSum.setTextColor(getResources().getColor(R.color.font_red));
                } else {
                    titleSum.setTextColor(getResources().getColor(R.color.font_gray));
                }
                contentSum.setText(String.valueOf(contentLength));
                changePublicState();
            }
        });

         //发表话题
        publicTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isFastClick()){
                    return;
                }
                compoundData(topicTitle,topicContent, Bimp.drr);
            }
        });

        imgs_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openPictureSelector();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.IMAGE_FILE_PATH, path);
    }

    private void changePublicState() {
          if(titleLength > 0 && contentLength > 0){
              publicTopic.setEnabled(true);
              publicTopic.setAlpha(1.0f);
          } else {
              publicTopic.setEnabled(false);
              publicTopic.setAlpha(0.5f);
          }
    }

    private void compoundData(String title,String topicContent, List<String> drr) {
        jiazai_layout.setVisibility(View.VISIBLE);
        HashMap<String, String> postImages = new HashMap<>();
        if (drr != null && drr.size() > 0) {
            for (int i = 0; i < drr.size(); i++) {
                postImages.put("file" + i, drr.get(i));
            }

            if (postImages.size() > 0) {
                uploadImage(topicTitle,topicContent,postImages);
            }

        } else {
            //不带图片的上传
            createTopic(compoundJsonData(topicTitle,topicContent, null));
        }
    }


    /**
     * 上传图文数据
     * @param content
     * @param postImages
     */
    private void uploadImage(final String title,final String content, HashMap<String, String> postImages) {
                new HttpPostFileBuilder(PublicTopicActivity.this)
                .setClassObj(AvatarBean.class)
                .setHttpResult(new HttpCallBack() {
                @Override
                public void setOnSuccessCallback(int requestCode, Object resultBean) {
                    AvatarBean avatarBean = (AvatarBean) resultBean;
                    ArrayList<String> items = avatarBean.getItems();
                    String jsonData = compoundJsonData(title,content, items);
                    createTopic(jsonData);
                }

                @Override
                public void setOnErrorCallback(int requestCode, int code, String msg) {
                    jiazai_layout.setVisibility(View.GONE);
                    ToastUtils.makeText(PublicTopicActivity.this,msg);
                  }
              })
              .setFileNames(postImages)
              .setUrl(UrlsNew.URLHeader + "/system/file")
              .addQueryParams("type", "avatar")
              .build();
    }

    /**
     * 组装json数据
     * @param content
     * @param items
     */
    private String compoundJsonData(String title, String content, ArrayList<String> items) {
        if (TextUtils.isEmpty(title)) {
            ToastUtils.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (TextUtils.isEmpty(content)) {
            ToastUtils.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        if (!TextUtils.isEmpty(content)&&!TextUtils.isEmpty(title) && items != null) {      //图文组合
            try {
                jsonObject.put("group_id", classId);
                jsonObject.put("title", topicTitle);
                object.put("text", content);
                JSONArray jsonArray =new JSONArray();
                for(int i=0;i<items.size();i++){
                    jsonArray.put(i,items.get(i));
                }
                object.put("images",jsonArray);
                jsonObject.put("content",object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (!TextUtils.isEmpty(content)&&!TextUtils.isEmpty(title) && items == null) { //只传文字
            try {
                object.put("text", content);
                jsonObject.put("title", topicTitle);
                jsonObject.put("group_id", classId);
                jsonObject.put("content",object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

        }
        return jsonObject.toString();
    }


    private void createTopic(String content) {
               if(TextUtils.isEmpty(content)){
                   return;
               }
                new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_CREATE_TOPIC)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        ToastUtils.makeText(PublicTopicActivity.this,"话题发表成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("pageIndex",pageIndex);
                        setResult(10,intent);
                        finish();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        jiazai_layout.setVisibility(View.GONE);
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(PublicTopicActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(PublicTopicActivity.this, myLayout);
                        }
                        ToastUtils.makeText(PublicTopicActivity.this,msg, Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(null)
                .addBodyParam( content)
                .build();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //返回
                if (!TextUtils.isEmpty(topicTitle) || !TextUtils.isEmpty(topicContent)) {
                    exitEditDialog();
                    return true;
                }
                clearCacheBimp();
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!TextUtils.isEmpty(topicTitle) || !TextUtils.isEmpty(topicContent)) {
                exitEditDialog();
                return true;
            }
            clearCacheBimp();
            finish();
        }
        return true;
    }


    private void exitEditDialog() {
        exitEditDialog = new CustomNewDialog(this);
        exitEditDialog.show();
        initEditView();
        initEditListener();
    }

    private void initEditView() {
        exitEditDialog.setMessage("退出此次编辑？");
        exitEditDialog.setRightText(getResources().getString(R.string.btn_sure));
        exitEditDialog.setLeftText(getResources().getString(R.string.cancel));
    }

    private void initEditListener() {
        //取消
        exitEditDialog.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exitEditDialog.isShowing()){
                    exitEditDialog.cancel();
                }
            }
        });
        //确定
        exitEditDialog.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exitEditDialog.isShowing()){
                    exitEditDialog.cancel();
                }
                finish();
            }
        });
    }

    /**
     * 弹出软键盘
     * @param edit
     */
    private void popupSoftKeybord(final EditText edit) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager manager = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                manager.showSoftInput(edit, 0);
            }
        }, 500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsUtil.permissionsResult(requestCode, permissions, grantResults)) {
               openPictureSelector();
        }
    }

    private void openPictureSelector() {
        //获取颜色值
        String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(PublicTopicActivity.this, R.color.first_theme));
        //设置最多选择几张图片
        AndroidImagePicker.getInstance().setSelectLimit(3 - Bimp.bmp.size());
        AndroidImagePicker.getInstance().pickMulti(PublicTopicActivity.this, true, themeColor, new AndroidImagePicker.OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(List<ImageItem> items) {
                try {
                    //动态控制gridview 宽高

                    if (items != null && items.size() > 0) {
                        for (int i = 0; i < items.size(); i++) {
                            // 保存图片到sd卡
                            String filename = System.currentTimeMillis() + "";
                            Bitmap bm = Bimp.revitionImageSize(items.get(i).path);
                            path = Bimp.saveBitmap(bm, "" + filename);
                            if (Bimp.drr.size() < 3) {
                                Bimp.bmp.add(bm);
                                if (!TextUtils.isEmpty(path)) {
                                    File file = new File(path);
                                    if (file.exists()) {
                                        Bimp.drr.add(path);
                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        clearCacheBimp();
        super.onDestroy();
    }

    /**
     * 清除缓存图片
     */
    private void clearCacheBimp() {
        if(Bimp.drr != null){
            Bimp.drr.clear();
        }
        if(Bimp.bmp != null){
            Bimp.bmp.clear();
        }
    }
    class ImageBucketAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return (Bimp.bmp.size()) + 1;
        }

        @Override
        public Object getItem(int position) {
            return Bimp.bmp.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(PublicTopicActivity.this).inflate(R.layout.add_topic_photo, null);
            }
            ImageView item_grida_image = (ImageView) convertView.findViewById(R.id.one_image);
            ImageView dele_img = (ImageView) convertView.findViewById(R.id.one_delete);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)item_grida_image.getLayoutParams();
            String mode = Build.MODEL;
            if (position == (Bimp.bmp.size())) {
                if (position <= 2) {
                    params.width = 50;
                    params.height = 50;
                    params.setMargins(10,0,0,0);
                    dele_img.setVisibility(View.GONE);
                    item_grida_image.setImageBitmap(null);
                    item_grida_image.setBackgroundResource(R.drawable.iv_public_topic_picture);
                    item_grida_image.setVisibility(View.VISIBLE);
                } else {
                    dele_img.setVisibility(View.GONE);
                    item_grida_image.setVisibility(View.GONE);
                }
            } else {
                if (mode.equals("SM-N9002")) {
                    params.width = 90;
                    params.height = 90;
                } else {
                    params.width = 70;
                    params.height = 70;
                }
                params.setMargins(0,0,0,0);
                dele_img.setVisibility(View.VISIBLE);
                item_grida_image.setImageBitmap(Bimp.bmp.get(position));
                item_grida_image.setBackgroundResource(R.color.transparent);
                item_grida_image.setVisibility(View.VISIBLE);
            }
            item_grida_image.setLayoutParams(params);
            dele_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 从集合中移除
                    Bimp.bmp.get(position).recycle();
                    Bimp.bmp.remove(position);
                    Bimp.drr.remove(position);
                    // 总数减去1
                    if(Bimp.max>0){
                        Bimp.max -= 1;
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

    }
}
