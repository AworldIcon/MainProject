package com.coder.kzxt.classe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.classe.beans.ClassBean;
import com.coder.kzxt.classe.beans.ClassDetailBean;
import com.coder.kzxt.classe.beans.MyCreateClass;
import com.coder.kzxt.login.beans.AvatarBean;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.permissionUtils.PermissionsUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Utils;
import com.coder.kzxt.views.CustomNewDialog;
import com.coder.kzxt.wheel.widget.OnWheelChangedListener;
import com.coder.kzxt.wheel.widget.WheelView;
import com.coder.kzxt.wheel.widget.adapters.ArrayWheelAdapter;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Created by wangtingshun on 2017/6/7.
 * 创建班级
 */

public class CreateClassActivity extends BaseActivity implements OnWheelChangedListener {

    private Toolbar mToolbar;
    private EditText etClassName;  //班级名称
    private RelativeLayout orgStructureLayout; //组织架构
    private RelativeLayout enrollmentYear;   //入学年份
    private RelativeLayout addLimitLayout; //加入限制
    private RelativeLayout classIconLayout;   //班级图标
    private String className;
    private CustomNewDialog classIconDialog;
    private TextView cancle;
    private TextView photo;
    private String path;
    private TextView photoPicture;
    private PermissionsUtil permission;
    private ImageView headImage; //班级头像
    private LinearLayout myLayout;
    private String headUrl;
    private int limitFlag;
    private TextView addLimit;  //加入限制
    private CustomNewDialog enrollmentDialog; //入学年份dialog
    private TextView enroCancle; //取消
    private TextView enroSure;   //确定
    private WheelView enroWheelView;
    private String enrolllmentLevel; //级数
    private TextView tv_enrollment;
    private TextView organizationView; //组织架构
    private TextView year;      //入学年份
    private String organizationContent; //组织架构content
    private String categoryId; //分类id
    private String classFlag;  //班级标记
    private ClassDetailBean.ClassDetail item; //详情bean
    private MenuItem comleted;
    private TextView tv_save; //保存
    private boolean nameFlag = false;
    private boolean orgFlag = false;
    private String newYear = null;
    private String[] enrolllmentDatas = new String[]{
            "2017", "2016", "2015", "2014", "2013", "2012",
            "2011", "2010",
    };
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private ArrayWheelAdapter arrayWheelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_class_layout);
        permission = new PermissionsUtil(this);
        classFlag = getIntent().getStringExtra("classFlag");
        if (!TextUtils.isEmpty(classFlag) && classFlag.equals("manager")) {
            item = (ClassDetailBean.ClassDetail) getIntent().getSerializableExtra("detailBean");
        }
        if (savedInstanceState != null) {
            path = savedInstanceState.getString(Constants.IMAGE_FILE_PATH);
        }
        initView();
        initData();
        initListener();
        changeTextColor();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolBar);
        if (classFlag.equals("create")) {
            mToolbar.setTitle(getResources().getString(R.string.create_class));
        } else if (classFlag.equals("manager")) {
            mToolbar.setTitle(getResources().getString(R.string.class_manager));
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myLayout = (LinearLayout) findViewById(R.id.my_layout);
        etClassName = (EditText) findViewById(R.id.et_class_name);
        orgStructureLayout = (RelativeLayout) findViewById(R.id.organizational_structure_layout);
        organizationView = (TextView) findViewById(R.id.organization_content);
        enrollmentYear = (RelativeLayout) findViewById(R.id.enrollment_year_layout);
        year = (TextView) findViewById(R.id.tv_enrollment_year);
        addLimitLayout = (RelativeLayout) findViewById(R.id.add_limit_layout);
        classIconLayout = (RelativeLayout) findViewById(R.id.class_icon_layout);
        headImage = (ImageView) findViewById(R.id.iv_class_icon);
        addLimit = (TextView) findViewById(R.id.tv_add_limit);
        tv_enrollment = (TextView) findViewById(R.id.tv_enrollment_year);
        tv_save = (TextView) findViewById(R.id.tv_save);
    }

    private void initListener() {
        etClassName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        etClassName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                className = etClassName.getText().toString().trim();
                if (className.length() > 0) {
                    nameFlag = true;
                } else {
                    nameFlag = false;
                }
                changeTextColor();
            }
        });
        //组织架构
        orgStructureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateClassActivity.this, OrganizationActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE);
            }
        });
        //入学年份
        enrollmentYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertEnrollmentYearDialog();
            }
        });
        //加入限制
        addLimitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateClassActivity.this, JoinLimitActivity.class);
                intent.putExtra("joinFlag", limitFlag);
                startActivityForResult(intent, Constants.REQUEST_CODE);
            }
        });
        //班级图标
        classIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
//                classIconPhoto();
            }
        });
        //保存
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (classFlag.equals("create")) {
                    postCreateClassData();
                } else if (classFlag.equals("manager")) {
                    getSaveClassData();
                }
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.IMAGE_FILE_PATH, path);
    }

    private void classIconPhoto() {
        if (!permission.isNeedPermissions(PERMISSIONS)) {
            //获取颜色值
            String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(CreateClassActivity.this, R.color.first_theme));
            //设置最多选择几张图片
            AndroidImagePicker.getInstance().setSelectLimit(1);
            AndroidImagePicker.getInstance().pickMulti(CreateClassActivity.this, true, themeColor, new AndroidImagePicker.OnImagePickCompleteListener() {
                @Override
                public void onImagePickComplete(List<ImageItem> items) {
                    try {
                        if (items != null && items.size() > 0) {
                            for (int i = 0; i < items.size(); i++) {
                                // 保存图片到sd卡
                                String filename = System.currentTimeMillis() + "";
                                Bitmap bm = Bimp.revitionImageSize(items.get(i).path);
                                path = Bimp.saveBitmap(bm, "" + filename);
                                GlideUtils.loadHeaderOfClass(CreateClassActivity.this, path, headImage);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void changeTextColor() {
        if(classFlag.equals("create")){
            if (nameFlag && orgFlag) {
                tv_save.setClickable(true);
                tv_save.setAlpha(1.0f);
            } else {
                tv_save.setClickable(false);
                tv_save.setAlpha(0.5f);
            }
        }
    }

    private void initData() {
        if (classFlag.equals("manager") && item != null){
            year.setText(item.getYear());
            String name = item.getName();
            etClassName.setText(name);
            organizationView.setText(item.getCategory_name());
            GlideUtils.loadHeaderOfClass(this,item.getLogo(),headImage);
            String status = item.getJoin_status();
            limitFlag = Integer.parseInt(status);
            if (status.equals("0")) {
                addLimit.setText(getResources().getString(R.string.add_limit));
            } else if (status.equals("1")) {
                addLimit.setText(getResources().getString(R.string.allow_join));
            } else if (status.equals("2")) {
                addLimit.setText(getResources().getString(R.string.joinin_ban));
            }
            etClassName.setSelection(name.length());
        } else if(classFlag.equals("create")){
             limitFlag = 2;
             enrolllmentLevel = "2017";
             tv_enrollment.setText(enrolllmentLevel);
             addLimit.setText(getResources().getString(R.string.joinin_ban));
        }
    }

    /**
     * 弹出入学年份选择dialog
     */
    private void alertEnrollmentYearDialog() {
        initDialog();
        initWheelView();
        initWheelViewListener();
        setUpData();
    }

    private void initDialog() {
        enrollmentDialog = new CustomNewDialog(this, R.layout.enrollment_year_dialog);
        enroCancle = (TextView) enrollmentDialog.findViewById(R.id.tv_cancle);
        enroSure = (TextView) enrollmentDialog.findViewById(R.id.tv_sure);
        Window dialogWindow = enrollmentDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        enrollmentDialog.show();
    }

    private void initWheelView() {
        enroWheelView = (WheelView) enrollmentDialog.findViewById(R.id.enro_wheelview);
    }

    private void initWheelViewListener() {
        enroWheelView.addChangingListener(this);
        enroSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enrollmentDialog.isShowing()) {
                    enrollmentDialog.cancel();
                }
                tv_enrollment.setText(enrolllmentLevel);
            }
        });
        enroCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enrollmentDialog.isShowing()) {
                    enrollmentDialog.cancel();
                }
            }
        });
    }

    private void setUpData() {
        arrayWheelAdapter = new ArrayWheelAdapter<String>(CreateClassActivity.this, enrolllmentDatas);
        enroWheelView.setViewAdapter(arrayWheelAdapter);
        enroWheelView.setVisibleItems(5);
        enrolllmentLevel = enrolllmentDatas[0];
    }

    private void takePhoto() {
        classIconDialog = new CustomNewDialog(this, R.layout.class_icon_dialog_item);
        cancle = (TextView) classIconDialog.findViewById(R.id.tv_cancle);
        photo = (TextView) classIconDialog.findViewById(R.id.photo);
        photoPicture = (TextView) classIconDialog.findViewById(R.id.photo_picture);
        classIconDialog.show();
        initAfterDialogListener();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!permission.isNeedPermissions(PERMISSIONS)) {
            photo();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void initAfterDialogListener() {
        //取消
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classIconDialog.isShowing()) {
                    classIconDialog.cancel();
                }
            }
        });

        //打开相机
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classIconDialog.isShowing()) {
                    classIconDialog.cancel();
                }
                if (!permission.isNeedPermissions(PERMISSIONS)) {
                    photo();
                }
            }
        });

        //打开图库
        photoPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classIconDialog.isShowing()) {
                    classIconDialog.cancel();
                }
                startActionPickCrop();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //返回
                finish();
                break;
            case R.id.right_item:  //完成
//                if (classFlag.equals("create")) {
//                    postCreateClassData();
//                } else if (classFlag.equals("manager")) {
//                    postSaveClassData(getSaveClassData());
//                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取保存班级的数据
     * @return
     */
    private void getSaveClassData() {
        className = etClassName.getText().toString().trim();
        if (TextUtils.isEmpty(organizationContent)) {
            organizationContent = item.getCategory_name();
        }
        if (TextUtils.isEmpty(enrolllmentLevel)) {
            newYear = item.getYear();
        } else {
            newYear = enrolllmentLevel;
        }
        if (TextUtils.isEmpty(String.valueOf(limitFlag))) {
            limitFlag = Integer.parseInt(item.getJoin_status());
        }
        if (TextUtils.isEmpty(path)) {
            path = item.getLogo();
        }
        updateUserHead(path);
    }

    @NonNull
    private String saveCompleteData(String headUrl) {
        JSONObject object = new JSONObject();
        try {
            object.put("name", className);
            object.put("year", newYear);
            object.put("category_id", categoryId);
            object.put("join_status", limitFlag);
            if (!TextUtils.isEmpty(headUrl)) {
                object.put("logo", headUrl);
            }
            object.put("announcement",item.getAnnouncement());
        } catch (Exception e) {

        }
        return object.toString();
    }


    /**
     * 提交创建班级数据
     */
    public void postCreateClassData() {
        showLoadingView();
        updateUserHead(path);
    }

    /**
     * 创建班级完成
     */
    private void createClassComplete(String data) {
                 new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_CREATE_CLASS)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        hideLoadingView();
                        ClassBean bean = (ClassBean) resultBean;
                        jumpClassDetailActivity(bean.getItem().getId());
                        ToastUtils.makeText(CreateClassActivity.this,getResources().getString(R.string.create_success),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        ToastUtils.makeText(CreateClassActivity.this,msg, Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(ClassBean.class)
                .addBodyParam(data)
                .build();
    }

    private void jumpClassDetailActivity(String id) {
        Intent intent = new Intent(CreateClassActivity.this,ClassDetailActivity.class);
        MyCreateClass.CreateClassBean bean = new MyCreateClass.CreateClassBean();
        bean.setId(id);
        intent.putExtra("create","create");
        intent.putExtra("class",bean);
        startActivityForResult(intent,Constants.REQUEST_CODE);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.right_menu, menu);
//        comleted = menu.findItem(R.id.right_item);
//        if (classFlag.equals("create")) {
//            comleted.setTitle(getResources().getString(R.string.save));
//        } else if (classFlag.equals("manager")) {
//            comleted.setTitle(getResources().getString(R.string.save));
//        }
//        MenuItemCompat.setShowAsAction(comleted, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 提交保存数据
     */
    private void postSaveClassData(String data) {
                showLoadingView();
                new HttpPutBuilder(this)
                .setUrl(UrlsNew.PUT_UPDATE_CLASS)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        hideLoadingView();
                        ToastUtils.makeText(CreateClassActivity.this,"修改班级成功",Toast.LENGTH_SHORT).show();
                        setResult(6);
                        finish();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(CreateClassActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(CreateClassActivity.this, myLayout);
                        }
                        ToastUtils.makeText(CreateClassActivity.this,msg,Toast.LENGTH_SHORT).show();
                        hideLoadingView();
                    }
                })
                .setClassObj(null)
                .setPath(item.getId())
                .addBodyParam(data)
                .build();
    }


    /**
     * 照相
     */
    public void photo() {
        try {
            String status = Environment.getExternalStorageState();
            if (status.equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(Constants.HEAD_PIC_URL);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(dir, "yunketang_head.png");
                path = file.getPath();
                Uri imageUri = Uri.fromFile(file);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                boolean intentAvailable = Utils.isIntentAvailable(CreateClassActivity.this, openCameraIntent);
                if (intentAvailable) {
                    startActivityForResult(openCameraIntent, Constants.TAKE_PICTURE);
                } else {
                    ToastUtils.makeText(CreateClassActivity.this, getResources().getString(R.string.take_photo_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                ToastUtils.makeText(CreateClassActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统图片编辑进行裁剪
     */
    public void startPhotoCrop(Uri uri) {
        try {
            String status = Environment.getExternalStorageState();
            if (status.equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(Constants.HEAD_PIC_URL);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                File jpegFile = new File(dir, String.valueOf(System.currentTimeMillis()) + "yunketang_head.png");
                path = jpegFile.getPath();
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(jpegFile));
                intent.putExtra("return-data", false);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, Constants.CROP_PICTURE);
            } else {
                ToastUtils.makeText(CreateClassActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 选择相册
     */
    private void startActionPickCrop() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setType("image/*");
            startActivityForResult(intent, Constants.ALBUM_PICTURE);
        } else {
            ToastUtils.makeText(CreateClassActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == 10086) {
            if (data != null) {
                limitFlag = data.getIntExtra("joinFlag", 0);
                limitCondition(limitFlag);
            }
        } else if (requestCode == Constants.REQUEST_CODE && resultCode == 8) { //组织架构数据
            categoryId = data.getStringExtra("categoryId");
            organizationContent = data.getStringExtra("content");
            organizationView.setText(organizationContent);
            orgFlag = true;
            changeTextColor();
        } else if(requestCode == Constants.REQUEST_CODE && resultCode == 12){ //由话题详情跳回
            setResult(7);
            finish();
        }

        if (requestCode == Constants.TAKE_PICTURE) {
            if(!TextUtils.isEmpty(path)){
                File file = new File(path);
                startPhotoCrop(Uri.fromFile(file));
            }
        }
        if (requestCode == Constants.ALBUM_PICTURE) {
            if (data != null) {
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                startPhotoCrop(selectedImage);
            }
        }
        if (requestCode == Constants.CROP_PICTURE) {
            GlideUtils.loadHeaderOfClass(CreateClassActivity.this, path, headImage);
        }
    }

    /**
     * 限制条件
     *
     * @param limitFlag
     */
    private void limitCondition(int limitFlag) {
        if (limitFlag == 0) {
            addLimit.setText(getResources().getString(R.string.needed_check));
        } else if (limitFlag == 1) {
            addLimit.setText(getResources().getString(R.string.allow_join));
        } else if (limitFlag == 2) {
            addLimit.setText(getResources().getString(R.string.joinin_ban));
        }
    }

    /**
     * 上传头像
     *
     * @param path
     */
    public void updateUserHead(final String path) {
               if (classFlag.equals("create")) {
                  if (TextUtils.isEmpty(path)) {
                      String data = compountData();
                      createClassComplete(data);
                      return;
                  }
                }
                 HashMap<String, String> postImages = new HashMap<>();
                 postImages.put("file0", path);
                 new HttpPostFileBuilder(this)
                .setUrl(UrlsNew.POST_SYSTEM_FILE)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        AvatarBean avatarBean = (AvatarBean) resultBean;
                        ArrayList<String> items = avatarBean.getItems();
                        hideLoadingView();
                        headUrl = items.get(0);
                        //创建
                        if (classFlag.equals("create")) {
                            String data = compountData();
                            createClassComplete(data);
                        } else if (classFlag.equals("manager")) {
                            String editData = saveCompleteData(headUrl);
                            postSaveClassData(editData);
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == 4000 && msg.contains("FileNotFoundException")) {
                            String editData = saveCompleteData(headUrl);
                            postSaveClassData(editData);
                        } else {
                            ToastUtils.makeText(CreateClassActivity.this, msg, Toast.LENGTH_SHORT).show();
                            hideLoadingView();
                        }
                    }
                })
                .setClassObj(AvatarBean.class)
                .addQueryParams("type", "avatar")
                .setFileNames(postImages)
                .build();
    }

    /**
     * 组合数据
     */
    private String compountData() {
        String limit = String.valueOf(limitFlag);
        String dYear = enrolllmentLevel.toString();
//        String dYear = s.substring(0, s.length()-1);
        JSONObject object = new JSONObject();
        try {
            object.put("name", className);
            object.put("year", dYear);
            object.put("category_id", categoryId);
            object.put("join_status", limit);
            if(!TextUtils.isEmpty(headUrl)){
                object.put("logo", headUrl);
            } else {
                object.put("logo",path);
            }
        } catch (Exception e) {

        }
        return object.toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == enroWheelView) {
            updateEnrollmentYearData();
        }
    }

    /**
     * 更新年份数据
     */
    private void updateEnrollmentYearData() {
        int pCurrent = enroWheelView.getCurrentItem();
        enrolllmentLevel = enrolllmentDatas[pCurrent];
    }
}
