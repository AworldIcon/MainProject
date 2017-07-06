package com.coder.kzxt.classe.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.http.HttpGetOld;
import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.adapter.ClassMemberAdapter;
import com.coder.kzxt.classe.beans.ClassDetailResult;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.gambit.activity.ClassGambitsActivity;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Urls;
import com.coder.kzxt.utils.Utils;
import com.coder.kzxt.views.CustomNewDialog;
import com.jauker.widget.BadgeView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.coder.kzxt.activity.R.id.className;


/**
 * 班级详情
 * Created by wangtingshun on 2017/3/13.
 */

public class ClassParticularsActivity extends BaseActivity {

    private LinearLayout classTopic;  //话题
    private LinearLayout classCourse;  //课程
    private LinearLayout courseTable;  //课程表
    private LinearLayout classPhoto; //相册
    private LinearLayout classFile; //文件
    private BadgeView badgeView;
    private String classid; //班级id
    private String myClassState; //班级状态
    private SharedPreferencesUtil spu;
    private ScrollView scrollView;
    private RecyclerView myRecyclerView;
    private LinearLayout loadLayout; //加载
    private LinearLayout loadFailLayout; //加载失败
    private String role; //角色
    private Context context;
    private Button loadFailBtn; //加载失败button
    private LinearLayout noInfoLayout;//暂无信息
    private RelativeLayout classTeacherView;
    private TextView classMoreMember;//班级更多成员
    private TextView classDetailInfo;//班级详情信息
    private TextView classBlackBoard;//班级黑板报
    private TextView addClassBtn;//加入班级
    private TextView classMore; //更多
    private TextView checkClassBtn;//待审核
    private TextView forbidAddClass;//禁止加入班级
    private RelativeLayout includeClassParticulars;
    private View includeClassParticularsDown;
    private ImageView classParticularsImg;
    private TextView classParticularsNameText;
    private TextView classCreateText;
    private TextView classParticularsProfessional;
    private ImageView classApplyParticularsNum;
    private RelativeLayout headerTotalLayout;
    private RelativeLayout classSetting; //班级设置
    private RelativeLayout classApplyLayout; //申请列表
    private RelativeLayout classMemberLayout;
    private TextView classMemberText;
    private RelativeLayout classParticularsTotalLayout;
    private Button classParticularsBack;
    private RelativeLayout classLayout;
    private ClassMemberAdapter memberAdapter;//成员数据
    private CustomNewDialog customNewDialog;
    //加入班级验证信息
    private EditText nameinfo;
    private EditText mobileinfo;
    private EditText studnuminfo;
    private ImageView photoinfo;
    private TextView commitinfo;
    private TextView cancleinfo;
    private TextView student_teacher;
    private String mobile;
    private String studentNum;
    private File jpgFile;
    private String path;
    private Bitmap bitMap = null;
    private PermissionsUtil permission;
    private Dialog mydialog;
    private final static String FILE_SAVEPATH = Constants.ID_PICTURE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_particulars);
        context = this;
        spu = new SharedPreferencesUtil(context);
        permission = new PermissionsUtil(ClassParticularsActivity.this);
        badgeView = new BadgeView(context);
        classid = getIntent().getStringExtra("classId");
        myClassState = getIntent().getStringExtra("myClassState");
        initView();
        initListener();
        getParticularData();
    }

    private void initView() {
        classTopic = (LinearLayout) findViewById(R.id.class_topic);
        classCourse = (LinearLayout) findViewById(R.id.class_course);
        courseTable = (LinearLayout) findViewById(R.id.class_table);
        classPhoto = (LinearLayout) findViewById(R.id.class_photo);
        classFile = (LinearLayout) findViewById(R.id.class_file);

        scrollView = (ScrollView) findViewById(R.id.activity_class_scrollView);
        myRecyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        myRecyclerView.setLayoutManager(new GridLayoutManager(context, 6));

        loadLayout = (LinearLayout) findViewById(R.id.jiazai_layout);
        noInfoLayout = (LinearLayout) findViewById(R.id.no_info_layout);
        loadFailLayout = (LinearLayout) findViewById(R.id.load_fail_layout);
        loadFailBtn = (Button) findViewById(R.id.load_fail_button);

        classLayout = (RelativeLayout) findViewById(R.id.class_particulars_layout);
        classTeacherView = (RelativeLayout) findViewById(R.id.class_teacher_view);
        classMoreMember = (TextView) findViewById(R.id.class_member_more);
        classDetailInfo = (TextView) findViewById(R.id.class_description_text);
        classBlackBoard = (TextView) findViewById(R.id.class_blackboard_text);
        addClassBtn = (TextView) findViewById(R.id.add_class_button);
        classMore = (TextView) findViewById(R.id.tv_class_more);
        checkClassBtn = (TextView) findViewById(R.id.check_class_button);
        forbidAddClass = (TextView) findViewById(R.id.forbid_class_button);

        includeClassParticulars = (RelativeLayout) findViewById(R.id.include_class_particulars);
        includeClassParticularsDown = (View) findViewById(R.id.include_class_particulars_down);
        classParticularsImg = (ImageView) findViewById(R.id.class_particulars_img);
        classParticularsNameText = (TextView) findViewById(R.id.class_particulars_nametext);
        classCreateText = (TextView) findViewById(R.id.class_create_text);
        classParticularsProfessional = (TextView) findViewById(R.id.class_particulars_professional);
        classApplyParticularsNum = (ImageView) findViewById(R.id.classapply_particulars_num);
        badgeView.setTargetView(classApplyParticularsNum);
        badgeView.setBackgroundResource(R.drawable.no_read_msg);
        badgeView.setBadgeGravity(Gravity.CENTER);
        badgeView.setText("");
        badgeView.setTextSize(15);
        badgeView.setVisibility(View.GONE);// 默认隐藏
        headerTotalLayout = (RelativeLayout) findViewById(R.id.header_zong_layout);
        classSetting = (RelativeLayout) findViewById(R.id.classsetting_particulars_layout);
        classApplyLayout = (RelativeLayout) findViewById(R.id.classapply_particulars_layout);
        classMemberLayout = (RelativeLayout) findViewById(R.id.class_member_la);
        classMemberText = (TextView) findViewById(R.id.class_member_te);
        classParticularsTotalLayout = (RelativeLayout) findViewById(R.id.class_particulars_zong_layout);

        classParticularsBack = (Button) findViewById(R.id.class_particulars_back_button);
        classTeacherView.setVisibility(View.VISIBLE);
    }

    private void initListener() {
        classParticularsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myClassFlag || selectClassFlag) {
//                    setResult(1);
//                }
                finish();
            }
        });


        //加载失败
        loadFailBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getParticularData();
            }
        });

        //话题
        classTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, ClassGambitsActivity.class);
                intent.putExtra("classId", classid);
                intent.putExtra("identity", role);
                startActivityForResult(intent, 1);
            }
        });
        //课程
        classCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, ClassCourseActivity.class);
                intent.putExtra("classId", classid);
                startActivityForResult(intent, 1);
            }
        });
        //课程表
        courseTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, CourseTableActivity.class);
                intent.putExtra("classId", classid);
                startActivityForResult(intent, 1);
            }
        });
        //相册
        classPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, ClassPhotoActivity.class);
                intent.putExtra("classId", classid);
                intent.putExtra("identity", role);
                startActivityForResult(intent, 1);
            }
        });

        //文件
        classFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, ClassFileActivity.class);
                intent.putExtra("identity", role);
                intent.putExtra("classId", classid);
                startActivityForResult(intent, 1);
            }
        });
        //加入班级
        addClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(spu.getIsLogin())) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("from", "classparticulars");
                    startActivityForResult(intent, 1);
                    return;
                }
                showInfo();
            }
        });
        //班级设置
        classSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ClassSettingActivity.class);
                intent.putExtra("classId", classid);
                startActivityForResult(intent, 1);

            }
        });
        //申请列表
        classApplyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ApplyListActivity.class);
                intent.putExtra("classId", classid);
                intent.putExtra("className",className);
                startActivityForResult(intent, Constants.APPLY_LSIT_REFRESH);
            }
        });

        //退出班级
        classMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.custom_dialog);
                builder.setTitle(getResources().getString(R.string.dialog_livelesson_prompt));
                builder.setMessage(getResources().getString(R.string.dialog_exit_class));
                builder.setPositiveButton(getResources().getString(R.string.password_dialog_ensure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitClassTask();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dlg = builder.create();
                dlg.show();
            }
        });
    }


    private void showInfo() {
        customNewDialog = new CustomNewDialog(this, R.layout.completeinfo_view);
        nameinfo = (EditText) customNewDialog.findViewById(R.id.nameinfo);
        mobileinfo = (EditText) customNewDialog.findViewById(R.id.mobileinfo);
        TextView mobilemail = (TextView) customNewDialog.findViewById(R.id.mobilemail);
        LinearLayout studnuminfo_ly = (LinearLayout) customNewDialog.findViewById(R.id.studnuminfo_ly);
        student_teacher = (TextView) customNewDialog.findViewById(R.id.student_teacher);
        studnuminfo = (EditText) customNewDialog.findViewById(R.id.studnuminfo);
        photoinfo = (ImageView) customNewDialog.findViewById(R.id.photoinfo);
        commitinfo = (TextView) customNewDialog.findViewById(R.id.commitinfo);
        cancleinfo = (TextView) customNewDialog.findViewById(R.id.cancleinfo);
        nameinfo.setText(spu.getNickName());
        customNewDialog.show();
        if (spu.getUserType().equals("1")) {
            student_teacher.setText(R.string.tea_photo);
            studnuminfo_ly.setVisibility(View.GONE);
            studentNum = "";
            if (spu.getSex().equals("male")) {
                  photoinfo.setImageDrawable(getResources()
                        .getDrawable(R.drawable.join_photo_male));
            } else if (spu.getSex().equals("female")) {
                photoinfo.setImageDrawable(getResources().getDrawable(R.drawable.join_photo_fmale));
            }
            photoinfo.setClickable(false);
            if (TextUtils.isEmpty(spu.getPhone())) {
                mobilemail.setText(getResources().getString(R.string.email));
                mobileinfo.setText(spu.getEmail());
            } else {
                mobileinfo.setText(spu.getPhone());
            }
            mobileinfo.setEnabled(false);
        } else if (spu.getUserType().equals("0")) {
            if (TextUtils.isEmpty(spu.getPhone())) {
                mobile = mobileinfo.getText().toString().trim();
            } else {
                mobile = spu.getPhone();
                mobileinfo.setText(spu.getPhone());
                mobileinfo.setEnabled(false);
            }
            if (TextUtils.isEmpty(spu.getStudentNum())) {
                studentNum = studnuminfo.getText().toString().trim();
            } else {
                studentNum = spu.getStudentNum();
                studnuminfo.setText(spu.getStudentNum());
                studnuminfo.setEnabled(false);
            }

            if (!TextUtils.isEmpty(spu.getIdPhoto())) {
                GlideUtils.loadCredentialsImg(this, spu.getIdPhoto(), photoinfo);
                try {
                    Utils.saveBitmapToFile(bitMap, Constants.ID_PICTURE + "class_member_idphoto.jpg", false);
                    jpgFile = new File(Constants.ID_PICTURE + "class_member_idphoto.jpg");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showImageTask();
                photoinfo.setEnabled(false);
            } else {
                photoinfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        photoDialog();
                    }
                });
            }
        }
        cancleinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customNewDialog.cancel();
            }
        });
        commitinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spu.getUserType().equals("1")) {
                    joinClass(classid, mobile, studentNum, path);
                } else {
                    jdugeInfo();
                }
            }
        });

    }

    private void jdugeInfo() {
        mobile = mobileinfo.getText().toString().trim();
        studentNum = studnuminfo.getText().toString().trim();

        boolean flag = false;
        boolean flag2 = false;
        boolean flag3 = false;
        if (TextUtils.isEmpty(mobile)) {
            mobileinfo.setError(getResources().getString(R.string.input_phone_hint));
            return;
        } else {
            flag = true;
        }
        if (TextUtils.isEmpty(studentNum)) {
            studnuminfo.setError(getResources().getString(R.string.input_stu_num_hint));
            return;
        } else {
            flag2 = true;
        }
        if (jpgFile == null && TextUtils.isEmpty(spu.getIdPhoto())) {
            student_teacher.setText(R.string.mus_post_photo);
            student_teacher.setTextColor(getResources().getColor(R.color.font_red));
            return;
        } else {
            flag3 = true;
        }
        if (flag && flag2 && flag3) {
            joinClass(classid, mobile, studentNum, path);
        } else {
            ToastUtils.makeText(context, getResources().getString(R.string.complete_info), Toast.LENGTH_SHORT).show();
        }
    }

    //加入班级
    private void joinClass(String classid, final String mobile, final String studentNum, String path) {
        mydialog = MyPublicDialog.createLoadingDialog(context);
        mydialog.show();
        Map<String, String> pictureMap = new HashMap<>();
        pictureMap.put("picture", path);

        new HttpPostOld(ClassParticularsActivity.this, ClassParticularsActivity.this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (mydialog != null && mydialog.isShowing()) {
                    mydialog.dismiss();
                }
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    spu.setPhone(mobile);
                    spu.setStudentNum(studentNum);
                    ToastUtils.makeText(getApplicationContext(), getResources().getString(R.string.apply_class_success), Toast.LENGTH_LONG).show();
                    setResult(2);
                    customNewDialog.cancel();
                } else {
                    ToastUtils.makeText(getApplicationContext(), getResources().getString(R.string.apply_class_fail) + msg, Toast.LENGTH_LONG).show();
                    customNewDialog.cancel();
                }
            }
        }, BaseBean.class, pictureMap, Urls.POST_JOINZ_CLASS_ACTION, classid, mobile, studentNum).excute(1000);

    }

    /**
     * 弹出学生证的对话框
     */
    private void photoDialog() {
        final CustomListDialog dialog = new CustomListDialog(context);
        dialog.addData(getResources().getString(R.string.photo), getResources().getString(R.string.take_photo), getResources().getString(R.string.cancel));
        dialog.show();
        dialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 2) {
                    dialog.cancel();
                } else if (position == 1) {
                    if (permission.camearPermissions()) {
                        startActionCamera();
                    }
                } else if (position == 0) {
                    startActionPickCrop();
                }
                dialog.cancel();
            }
        });
    }

    /**
     * 选择打开图库
     */
    private void startActionPickCrop() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            openPhoto();
        } else {
            ToastUtils.makeText(this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
        }
    }

    private void openPhoto() {
        String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.first_theme));
        //设置最多选择几张图片
        AndroidImagePicker.getInstance().setSelectLimit(1);
        AndroidImagePicker.getInstance().pickMulti(ClassParticularsActivity.this, true, themeColor, new AndroidImagePicker.OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(List<ImageItem> items) {
                try {
                    if (items != null && items.size() > 0) {
                        // 保存图片到sd卡
                        String filename = System.currentTimeMillis() + "";
                        path = Bimp.saveBitmap(Bimp.revitionImageSize(items.get(0).path), "" + filename);
                        Uri uri = Uri.fromFile(new File(path));
                        startPhotoCrop(uri);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 相机拍照
     */
    private void startActionCamera() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(FILE_SAVEPATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 调相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            jpgFile = new File(dir, String.valueOf(System.currentTimeMillis()) + "class_member_idphoto.jpg");
            path = jpgFile.getPath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(jpgFile));
            boolean intentAvailable = Utils.isIntentAvailable(this, intent);
            if (intentAvailable) {
                startActivityForResult(intent, Constants.TAKE_PICTURE);
            } else {
                ToastUtils.makeText(getApplicationContext(), getResources().getString(R.string.take_photo_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            ToastUtils.makeText(this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
        }
    }

    private void showImageTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String photoUrl = spu.getIdPhoto();
                    if (!TextUtils.isEmpty(photoUrl)) {
                        URL url = new URL(photoUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        bitMap = BitmapFactory.decodeStream(input);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 退出班级
     */
    private void exitClassTask() {
        loadLayout.setVisibility(View.VISIBLE);
        HttpGetOld httpGet = new HttpGetOld(this, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                loadLayout.setVisibility(View.GONE);
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    exitClassSuccess();
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(ClassParticularsActivity.this, classLayout);
                    exitFail(msg);
                } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    NetworkUtil.httpRestartLogin(ClassParticularsActivity.this, classLayout);
                    exitFail(msg);
                } else {
                    exitFail(msg);
                }
            }
        }, null, Urls.GET_EXIT_CLASS_ACTION, classid, spu.getUid(), spu.getIsLogin(), spu.getTokenSecret(), spu.getDevicedId());
        httpGet.excute(1000);

    }

    /**
     * 退出失败
     *
     * @param msg
     */
    private void exitFail(String msg) {
        ToastUtils.makeText(context, getResources().getString(R.string.exit_fail) + msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 退出班级成功
     */
    private void exitClassSuccess() {
        ToastUtils.makeText(context, getResources().getString(R.string.exit_success), Toast.LENGTH_SHORT).show();
        addClassBtn.setVisibility(View.VISIBLE);
        classMore.setVisibility(View.GONE);
        if (TextUtils.isEmpty(myClassState)) {
            spu.setClassState(true);
        }
        Intent intent = new Intent();
        intent.setAction(Constants.REFRESHBROADCAST);
        sendBroadcast(intent);
    }

    /**
     * 获取详情数据
     */
    private void getParticularData() {
        loadLayout.setVisibility(View.VISIBLE);
        HttpGetOld httpGet = new HttpGetOld(this, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                loadLayout.setVisibility(View.GONE);
                visibleDetailPage();
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    ClassDetailResult detailResult = (ClassDetailResult) baseBean;
                    ClassDetailResult.ClassDetailBean detailBean = detailResult.getData();
                    role = detailBean.getRole();
                    changeRole(role);
                    adapterMember(detailBean, msg);
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(ClassParticularsActivity.this, classLayout);
                    otherProcess();
                } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    NetworkUtil.httpRestartLogin(ClassParticularsActivity.this, classLayout);
                } else {
                    otherProcess();
                }
            }
        }, ClassDetailResult.class, Urls.GET_CLASS_DETAIL_ACTION, classid, spu.getUid(), spu.getIsLogin(), spu.getTokenSecret(), spu.getDevicedId());
        httpGet.excute(1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.LOGIN_BACK) {
            getParticularData();
        } else if (requestCode == Constants.TAKE_PICTURE) {
            startPhotoCrop(Uri.fromFile(new File(path)));
        } else if (requestCode == Constants.ALBUM_PICTURE) {
            if (data != null) {
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                startPhotoCrop(selectedImage);
            }
        } else if (requestCode == Constants.CROP_PICTURE) {
            jpgFile = new File(path);
            GlideUtils.loadCredentialsImg(this, "file://" + Uri.fromFile(jpgFile).getPath(), photoinfo);
        } else if(requestCode == Constants.APPLY_LSIT_REFRESH){
            getParticularData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 开始裁剪照片
     *
     * @param uri
     */
    private void startPhotoCrop(Uri uri) {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(FILE_SAVEPATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            jpgFile = new File(dir, "class_member_idphoto.jpg");
            intent.putExtra("crop", "true");
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(jpgFile));
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true); // no face detection
            startActivityForResult(intent, Constants.CROP_PICTURE);
        } else {
            ToastUtils.makeText(context, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 其他处理
     */
    private void otherProcess() {
        myRecyclerView.setVisibility(View.GONE);
        includeClassParticulars.setVisibility(View.GONE);
        loadFailLayout.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
    }

    /**
     * 适配成员数据
     *
     * @param detailBean
     */
    private void adapterMember(ClassDetailResult.ClassDetailBean detailBean, String msg) {
        ArrayList<ClassDetailResult.ClassDetailBean.MemberBean> memberList = detailBean.getMember();
        if (memberList.size() == 0) {
            includeClassParticulars.setVisibility(View.GONE);
        } else {
            includeClassParticulars.setVisibility(View.VISIBLE);
        }
        classCreateText.setText(detailBean.getCreater().getUserName());
        spu.setClassImg(detailBean.getLogo());
        headerTotalLayout.setVisibility(View.VISIBLE);
        includeClassParticulars.setVisibility(View.VISIBLE);
        classMemberLayout.setVisibility(View.VISIBLE);
        GlideUtils.loadCircleHeaderOfCommon(this, detailBean.getLogo(), classParticularsImg);
        classMemberText.setText(getResources().getString(R.string.member) + "(" + memberList.size() + ")");

        if (memberList.size() > 6) {
            classMoreMember.setVisibility(View.VISIBLE);
        }

        setData(detailBean);
        changeRightTitle(detailBean, msg);
    }

    /**
     * 修改标题右侧
     *
     * @param detailBean
     * @param msg
     */
    private void changeRightTitle(ClassDetailResult.ClassDetailBean detailBean, String msg) {
        String owner = detailBean.getOwner();
        String joinStatus = detailBean.getAccess().getJoinStatus();
        if (TextUtils.isEmpty(owner)) {
            ToastUtils.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        } else if (owner.equals("0")) {
            classTeacherView.setVisibility(View.GONE);
            if (joinStatus.equals("0")) {
                addClassBtn.setVisibility(View.VISIBLE);
                classMore.setVisibility(View.GONE);
                checkClassBtn.setVisibility(View.GONE);
                forbidAddClass.setVisibility(View.GONE);
            } else if (joinStatus.equals("1")) {
                addClassBtn.setVisibility(View.GONE);
                classMore.setVisibility(View.VISIBLE);
                checkClassBtn.setVisibility(View.GONE);
                forbidAddClass.setVisibility(View.GONE);
            } else if (joinStatus.equals("2")) {
                addClassBtn.setVisibility(View.GONE);
                classMore.setVisibility(View.GONE);
                checkClassBtn.setVisibility(View.VISIBLE);
                forbidAddClass.setVisibility(View.GONE);
            } else if (joinStatus.equals("3")) {
                addClassBtn.setVisibility(View.GONE);
                classMore.setVisibility(View.GONE);
                checkClassBtn.setVisibility(View.GONE);
                forbidAddClass.setVisibility(View.VISIBLE);
            }
        } else {
            classTeacherView.setVisibility(View.VISIBLE);
            addClassBtn.setVisibility(View.GONE);
            classMore.setVisibility(View.GONE);
            checkClassBtn.setVisibility(View.GONE);
            forbidAddClass.setVisibility(View.GONE);
        }
    }

    private void setData(ClassDetailResult.ClassDetailBean detailBean) {
        String applyNum = detailBean.getApplyNum();
        String className = detailBean.getClassName();
        String categoryName = detailBean.getCategoryName();
        String about = detailBean.getAbout();
        String announcement = detailBean.getAnnouncement();

        if (TextUtils.isEmpty(applyNum) || applyNum.equals("0")) {
            badgeView.setVisibility(View.GONE);
        } else {
            spu.setApplyNum(applyNum);
            badgeView.setVisibility(View.VISIBLE);
            badgeView.setText(spu.getApplyNum());
        }

        if (!TextUtils.isEmpty(className)) {
            if (className.length() > 20) {
                classParticularsNameText.setText(className.subSequence(0, 19));
            } else {
                classParticularsNameText.setText(className);
            }
        } else {
            classParticularsNameText.setText(getResources().getString(R.string.have_no_class_name));
        }
        classParticularsProfessional.setText(getResources().getString(R.string.category) + categoryName);
        spu.setProfessionalName(categoryName);

        if (!TextUtils.isEmpty(about)) {
            classDetailInfo.setText(about);
        } else {
            classDetailInfo.setText(getResources().getString(R.string.no_info));
        }

        if (!TextUtils.isEmpty(announcement)) {
            classBlackBoard.setText(announcement);
        } else {
            classBlackBoard.setText(getResources().getString(R.string.no_info));
        }
        spu.setAboutText(about);
        spu.setBlackboardText(announcement);
    }

    /**
     * 1 学生，未加入不可见
     * 2 老师,班委,创建者可见且可操作
     */
    private void changeRole(String role) {
        if (role.equals("header") || role.equals("admin") || role.equals("owner")) {
            classTeacherView.setVisibility(View.VISIBLE);
        } else if (role.equals("member") || role.equals("guest") || role.equals("")) {
            classTeacherView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示详情页
     */
    private void visibleDetailPage() {
        scrollView.setVisibility(View.VISIBLE);
        loadFailLayout.setVisibility(View.GONE);
        classParticularsTotalLayout.setVisibility(View.VISIBLE);
        includeClassParticularsDown.setVisibility(View.VISIBLE);
    }

}
