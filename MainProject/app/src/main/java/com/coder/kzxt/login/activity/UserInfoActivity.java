package com.coder.kzxt.login.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.UserInfoBean;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.login.beans.UserInfoResult;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.setting.activity.MyAddressActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import com.coder.kzxt.utils.Utils;
import com.coder.kzxt.views.CustomNewDialog;

import java.io.File;


/**
 * 用户信息
 * Created by Administrator on 2017/3/7.
 */

public class UserInfoActivity extends BaseActivity {

    private SharedPreferencesUtil spu;
    private ImageView user_head;
    //用户信息的layout
    private RelativeLayout user_info_layout;
    //用户名
    private TextView user_name;
    private TextView nickname_content;
    private RelativeLayout userNameLayout;
    private ImageView nickname_jiantou;
    private TextView user_signature_content;
    private RelativeLayout setting_signature_layout;
    private RelativeLayout my_layout;
    private File jpegFile;
    private String path;
    private RelativeLayout rl_sex;//性别
    private CustomNewDialog sexSelectDialog;
    private PermissionsUtil permission;
    private TextView male; //男
    private TextView female; //女
    private TextView cancle; //取消
    private TextView tv_sex; //性别
    private RelativeLayout address_layout; //我的地址
    private TextView address_content;//详细地址
    private View iv_address_line;
    private int localSwitch = -1;  //本站地址开关 0：关闭 1：开启
    private int cloudSwitch = -1;  //中心地址开关 0：关闭 1：开启
    private String nickname = "";
    private String signature = "";
    private Toolbar mToolbarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        if (savedInstanceState != null) {
            path = savedInstanceState.getString(Constants.IMAGE_FILE_PATH);
        }
        spu = new SharedPreferencesUtil(this);
        permission = new PermissionsUtil(this);
        initView();
        initListener();
        getUserInfoTask();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(R.string.personal_info);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user_head = (ImageView) findViewById(R.id.iv_setting_user_photo);
        user_info_layout = (RelativeLayout) findViewById(R.id.user_info_layout);
        my_layout = (RelativeLayout) findViewById(R.id.my_layout);

        // 用户名
        user_name = (TextView) findViewById(R.id.tv_user_name);
        nickname_content = (TextView) findViewById(R.id.nickname_content);
        nickname_jiantou = (ImageView) findViewById(R.id.nickname_jiantou);
        userNameLayout = (RelativeLayout) findViewById(R.id.user_name_layout);

        //地址
        address_layout = (RelativeLayout) findViewById(R.id.address_layout);
        address_content = (TextView) findViewById(R.id.address_content);
        //个性签名
        setting_signature_layout = (RelativeLayout) findViewById(R.id.setting_signature_layout);
        user_signature_content = (TextView) findViewById(R.id.user_signature_content);
        rl_sex = (RelativeLayout) findViewById(R.id.setting_middle_second_version_layout);
        iv_address_line = findViewById(R.id.iv_setting_linestuid_arrow);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
       //修改头像
        user_info_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //个人简介
        setting_signature_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(spu.getIsLogin())) {
                    Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 3);
                } else {
                    Intent intent = new Intent(UserInfoActivity.this,EditInfoActivity.class);
                    intent.putExtra("type","signature");
                    intent.putExtra("content",user_signature_content.getText().toString());
                    startActivityForResult(intent,1);
                }
            }
        });
        //用户名
        userNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(spu.getIsLogin())) {
                    Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 3);
                } else {
                    Intent intent = new Intent(UserInfoActivity.this,EditInfoActivity.class);
                    intent.putExtra("type","name");
                    intent.putExtra("content",nickname_content.getText().toString());
                    startActivityForResult(intent,1);
                }
            }
        });

        //性别
        rl_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertSelectSex();
            }
        });

        //地址
        address_layout.setOnClickListener(new View.OnClickListener() {   //我的地址
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(spu.getIsLogin())) {
                    Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 3);
                } else {
                    Intent intent = new Intent(UserInfoActivity.this,MyAddressActivity.class);
                    intent.putExtra(Constants.LOCAL_SWITCH,localSwitch);
                    intent.putExtra(Constants.CLOUD_SWITCH,cloudSwitch);
                    intent.putExtra(Constants.USER_INFO_ENTRANCE,"user_info");
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.IMAGE_FILE_PATH, path);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                backFinish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void backFinish() {
        saveSubmitUserInfo();
    }

    /**
     * 保存提交用户信息
     */
    private void saveSubmitUserInfo() {
        String nickName = nickname_content.getText().toString();
        String userSignature = user_signature_content.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("path",path);
        intent.putExtra("nickName",nickName);
        intent.putExtra("userSignature",userSignature);
        setResult(10,intent);
        finish();
    }

    /**
     * 弹出性别提示对话框
     */
    private void alertSelectSex() {
        sexSelectDialog = new CustomNewDialog(this, R.layout.sex_select_item);
        cancle = (TextView) sexSelectDialog.findViewById(R.id.tv_cancle);
        male = (TextView) sexSelectDialog.findViewById(R.id.tv_male);
        female = (TextView) sexSelectDialog.findViewById(R.id.tv_female);
        Window dialogWindow = sexSelectDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        sexSelectDialog.show();
        initAfterDialogListener();
    }


    private void initAfterDialogListener() {
        //取消
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sexSelectDialog.isShowing()) {
                    sexSelectDialog.cancel();
                }
            }
        });

        //男
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sexSelectDialog.isShowing()) {
                    sexSelectDialog.cancel();
                }
                spu.setGender("1");
                tv_sex.setText(getResources().getString(R.string.male));
            }
        });

        //女
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sexSelectDialog.isShowing()) {
                    sexSelectDialog.cancel();
                }
                spu.setGender("2");
                tv_sex.setText(getResources().getString(R.string.female));
            }
        });
    }


    /**
     * 获取用户信息
     */
    private void getUserInfoTask() {
                 new HttpGetBuilder(this)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                            if(requestCode == 1000){
                                UserInfoBean item = ((UserInfoResult) resultBean).getItem();
                                saveLoginData(item);
                            }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(UserInfoActivity.this, my_layout);
                        } else {
                            NetworkUtil.httpNetErrTip(UserInfoActivity.this, my_layout);
                        }
                    }
                })
                .setClassObj(UserInfoResult.class)
                .setUrl(UrlsNew.USER_PROFILE)
                .setRequestCode(1000)
                .build();

    }

    private void saveLoginData(UserInfoBean info) {
        UserInfoBean.ProfileBean profile = info.getProfile();

        spu.setUid(info.getId());
        spu.setNickName(profile.getNickname());
        spu.setUserHead(profile.getAvatar()); //用户头像
        spu.setBigHead(profile.getBig_avatar());//大头像
        String birthday = profile.getBirthday();
        if (!TextUtils.isEmpty(birthday)) {
            spu.setBirthDay(birthday);
        } else {
            spu.setBirthDay("1970-01-01");
        }
        if (!TextUtils.isEmpty(info.getMobile())) {
            spu.setUserAccount(info.getMobile());
        } else if (!TextUtils.isEmpty(info.getEmail())) {
            spu.setUserAccount(info.getEmail());
        }
        spu.setMobile(info.getMobile());
        spu.setEmail(info.getEmail());
        spu.setRegisterType(info.getRegister_type()); //注册类型
        spu.setCreateTime(info.getCreate_time());
        spu.setGender(profile.getGender()); //性别
        spu.setRemark(profile.getRemark());
        spu.setMotto(profile.getDesc());
        //"是否老师:0.否 1.是"
        spu.setUserRole(info.getIs_teacher());
        resultData();
    }

    /**
     * 请求后设置数据
     */
    private void resultData(){
        GlideUtils.loadCircleHeaderOfCommon(this,spu.getUserHead(),user_head);
        nickname_content.setText(spu.getNickName());
        user_signature_content.setText(spu.getMotto());
        String gender = spu.getGender();
        if (!TextUtils.isEmpty(gender)) {
            if (gender.equals("1")) { // 1男 2女
                tv_sex.setText(getResources().getString(R.string.male));
            } else {
                tv_sex.setText(getResources().getString(R.string.female));
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(permission.permissionsResult(requestCode,permissions,grantResults)){
            photo();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 显示对话框
     */
    private void showDialog() {
        final CustomListDialog customDialog = new CustomListDialog(UserInfoActivity.this);
        customDialog.addData(getResources().getString(R.string.take_photo), getResources().getString(R.string.photo),getResources().getString(R.string.cancel));
        customDialog.show();
        customDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 判断是否挂载了SD卡
                String storageState = Environment.getExternalStorageState();
                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                    File savedir = new File(Constants.HEAD_PIC_URL);
                    if (!savedir.exists()) {
                        savedir.mkdirs();
                    }
                } else {
                    ToastUtils.makeText(getApplicationContext(), getResources().getString(R.string.check_sd), 0).show();
                    return;
                }
                if (position == 0) {
                    if(permission.camearPermissions()){
                        photo();
                    }
                } else if (position == 1) {
                    startActionPickCrop();
                } else if (position == 2) {

                }
                if (customDialog.isShowing()) {
                    customDialog.cancel();
                }
            }
        });

    }

    /**
     * 照相
     */
    public void photo() {
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
            boolean intentAvailable = Utils.isIntentAvailable(UserInfoActivity.this, openCameraIntent);
            if (intentAvailable) {
                startActivityForResult(openCameraIntent, Constants.TAKE_PICTURE);
            } else {
                ToastUtils.makeText(UserInfoActivity.this, getResources().getString(R.string.take_photo_error), Toast.LENGTH_SHORT).show();
            }

        } else {
            ToastUtils.makeText(UserInfoActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 调用系统图片编辑进行裁剪
     */
    public void startPhotoCrop(Uri uri) {
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
            intent.putExtra("noFaceDetection", true); // no face detection
            startActivityForResult(intent, Constants.CROP_PICTURE);
        } else {
            ToastUtils.makeText(UserInfoActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
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
            ToastUtils.makeText(UserInfoActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            if (TextUtils.isEmpty(data.getStringExtra("content"))) {
                nickname_content.setText(nickname);
            } else {
                nickname_content.setText(data.getStringExtra("content"));
//                spu.setNickName(data.getStringExtra("content"));
            }
        } else if (requestCode == 1 && resultCode == 3) {
            if (TextUtils.isEmpty(data.getStringExtra("content"))) {
                user_signature_content.setText(signature);
            } else {
                user_signature_content.setText(data.getStringExtra("content"));
                spu.setMotto(data.getStringExtra("content"));
            }
        } else if (requestCode == 1 && resultCode == 4) {
            nickname_content.setText(spu.getNickName());
            user_signature_content.setText(spu.getMotto());
        } else if (requestCode == Constants.RESTART_LOGIN &&
                resultCode == Constants.LOGIN_BACK) {
            getUserInfoTask();
        } else if (requestCode == 3 && resultCode == Constants.LOGIN_BACK) {
            getUserInfoTask();
        } else {
            if (requestCode == Constants.TAKE_PICTURE) {
                File file = new File(path);
                startPhotoCrop(Uri.fromFile(file));
            }
            if (requestCode == Constants.ALBUM_PICTURE) {
                if (data != null) {
                    Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                    startPhotoCrop(selectedImage);
                }
            }
            if (requestCode == Constants.CROP_PICTURE) {
                GlideUtils.loadCircleHeaderOfCommon(UserInfoActivity.this, path, user_head);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backFinish();
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
