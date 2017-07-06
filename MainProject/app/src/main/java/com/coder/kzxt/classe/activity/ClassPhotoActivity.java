package com.coder.kzxt.classe.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.app.http.HttpGetOld;
import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.adapter.PhotoWaterFallAdapter;
import com.coder.kzxt.classe.beans.PhotoBeanResult;
import com.coder.kzxt.classe.delegate.PhotoWaterFallDelegate;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Urls;
import com.coder.kzxt.utils.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班级相册
 * Created by wangtinshun on 2017/3/14.
 */

public class ClassPhotoActivity extends BaseActivity implements PhotoWaterFallDelegate.OnItemClickCallback, SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private Toolbar mToolBar;
    private String classId;  //班级id
    private String identity;  //角色
    private String isMyPhoto = "0";// 0  全部    1   我的
    private int page = 1;
    private String path;
    private Dialog mydialog;
    private File jpgFile;
    private SharedPreferencesUtil spu;
    private boolean isUpRefresh = false; //是否下拉刷新
    private Button load_fail_button;
    private LinearLayout jiazai_layout;
    private LinearLayout no_info_layout;
    private LinearLayout load_fail_layout;
    private RelativeLayout myLayout;
    private MyPullSwipeRefresh mySwipeRefresh;
    private MyPullRecyclerView pullRecyclerView;
    private PhotoWaterFallAdapter adapter;
    private PhotoWaterFallDelegate delegate;
    private PermissionsUtil permission;
    private List<PhotoBeanResult.PhotoBean> data = new ArrayList<>();
    private ArrayList<PhotoBeanResult.PhotoBean> photoBeenList;
    private final static String FILE_SAVEPATH = Constants.CLASS_PHOTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_photo);
        if (savedInstanceState != null) {
            path = savedInstanceState.getString(Constants.IMAGE_FILE_PATH);
        }
        mContext = this;
        classId = getIntent().getStringExtra("classId");
        identity = getIntent().getStringExtra("identity");
        spu = new SharedPreferencesUtil(this);
        permission = new PermissionsUtil(this);
        initView();
        initListener();
        initData();
        requestPhotoTask();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.IMAGE_FILE_PATH, path);
    }

    private void initView() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);
        pullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        mySwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        pullRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    }

    private void initData() {
        delegate = new PhotoWaterFallDelegate(this, classId, identity);
        delegate.setOnItemClickListener(this);
        adapter = new PhotoWaterFallAdapter(this, data, delegate);
        pullRecyclerView.setAdapter(adapter);
        adapter.setSwipeRefresh(mySwipeRefresh);
    }

    private void initListener() {
        mySwipeRefresh.setOnRefreshListener(this);
        pullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                adapter.addPageIndex();
                requestPhotoTask();
            }
        });
    }

    private void requestPhotoTask() {
        showLoadingView();
        getPhotoTask();
    }

    private void getPhotoTask() {
        HttpGetOld httpget = new HttpGetOld(this, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                hideLoadingView();
                mySwipeRefresh.setRefreshing(false);
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    PhotoBeanResult photoBean = (PhotoBeanResult) baseBean;
                    photoBeenList = photoBean.getData();
                    if(photoBeenList != null && photoBeenList.size() > 0){
                        adapter.setTotalPage(photoBean.getTotalPages());
                        adapter.setPullData(photoBeenList);
                        adapter.notifyDataSetChanged();
                        no_info_layout.setVisibility(View.GONE);
                    }  else {
                        no_info_layout.setVisibility(View.VISIBLE);
                    }
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(mContext, myLayout);
                    if(!NetworkUtil.isNetworkAvailable(mContext)){
                        showLoadFailView(myLayout);
                        setOnLoadFailClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestPhotoTask();
                            }
                        });
                    }
                } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    no_info_layout.setVisibility(View.VISIBLE);
                    NetworkUtil.httpRestartLogin(ClassPhotoActivity.this, myLayout);
                } else {

                }
            }
        }, PhotoBeanResult.class, Urls.GET_CLASS_PHOTO_ACTION, classId, String.valueOf(page), "20", isMyPhoto);
        httpget.excute(1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.class_photo_menu, menu);
        final MenuItem icon = menu.findItem(R.id.change_menu);
        MenuItem upload = menu.findItem(R.id.uploading);
        if (isMyPhoto.equals("0")) {
            isMyPhoto = "1";
            getSupportActionBar().setTitle(getResources().getString(R.string.all_photo));
        }
        upload.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startUpload();
                return true;
            }
        });

        icon.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (isMyPhoto.equals("0")) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.all_photo));
                    icon.setIcon(R.drawable.poseter_water);
                    isMyPhoto = "1";
                } else {
                    getSupportActionBar().setTitle(getResources().getString(R.string.my_photo));
                    icon.setIcon(R.drawable.poseter_list);
                    isMyPhoto = "0";
                }
                requestPhotoTask();
                return true;
            }
        });

        MenuItemCompat.setShowAsAction(icon, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startUpload() {
        if (TextUtils.isEmpty(spu.getIsLogin())) {
            Intent intent = new Intent(ClassPhotoActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            final CustomListDialog customDialog = new CustomListDialog(ClassPhotoActivity.this);
            customDialog.addData(getResources().getString(R.string.take_photo), getResources().getString(R.string.photo), getResources().getString(R.string.cancel));
            customDialog.show();
            customDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (position == 0) {
                        if(permission.camearPermissions()){
                            photo();
                        }
                    } else if (position == 1) {
                        startActionPickCrop();
                    }
                    if(customDialog.isShowing()){
                        customDialog.cancel();
                    }
                }
            });

        }
    }

    /**
     * 打开图库
     */
    private void startActionPickCrop() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setType("image/*");
            startActivityForResult(intent, Constants.ALBUM_PICTURE);
        } else {
            ToastUtils.makeText(mContext, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 照相
     */
    public void photo() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(FILE_SAVEPATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            jpgFile = new File(dir, String.valueOf(System.currentTimeMillis()) + "classphoto.jpg");
            path = jpgFile.getPath();
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(jpgFile));
            boolean intentAvailable = Utils.isIntentAvailable(ClassPhotoActivity.this, openCameraIntent);
            if (intentAvailable) {
                startActivityForResult(openCameraIntent, Constants.TAKE_PICTURE);
            } else {
                Toast.makeText(ClassPhotoActivity.this, getResources().getString(R.string.take_photo_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ClassPhotoActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
        }
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
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 删除班级相册照片
     * @param classId
     * @param photoId
     * @param position
     */
    @Override
    public void deletePhoto(String classId, String photoId, final int position) {
        showLoadingView();
        HttpPostOld httpPost = new HttpPostOld(this, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                hideLoadingView();
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    photoBeenList.remove(position);
                    if(photoBeenList.size() > 0) {
                        no_info_layout.setVisibility(View.GONE);
                    } else {
                        no_info_layout.setVisibility(View.VISIBLE);
                    }
                    adapter.setPullData(photoBeenList);
                    adapter.notifyDataSetChanged();
                    ToastUtils.makeText(mContext,mContext.getResources().getString(R.string.delete_success),Toast.LENGTH_SHORT).show();
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(mContext, myLayout);
                    ToastUtils.makeText(mContext,mContext.getResources().getString(R.string.delete_fail),Toast.LENGTH_SHORT).show();
                } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    NetworkUtil.httpRestartLogin(ClassPhotoActivity.this, myLayout);
                    ToastUtils.makeText(mContext,mContext.getResources().getString(R.string.delete_fail),Toast.LENGTH_SHORT).show();
                } else {
                    ToastUtils.makeText(mContext,mContext.getResources().getString(R.string.delete_fail),Toast.LENGTH_SHORT).show();
                }
            }
        }, null, Urls.POST_DELETE_CLASS_PHOTO_ACTION,classId,photoId);
        httpPost.excute(1000);
    }

    /**
     * 点赞照片
     * @param photoId
     */
    @Override
    public void setLikePhoto(String photoId) {
        HttpPostOld httpPost = new HttpPostOld(this, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {

                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(mContext, myLayout);

                } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    NetworkUtil.httpRestartLogin(ClassPhotoActivity.this, myLayout);

                }
            }
        }, null, Urls.POST_APPRAISE_ACTION,"photo","photo"+photoId);
        httpPost.excute(1000);
    }

    @Override
    public void onRefresh() {
        getPhotoTask();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.LOGIN_BACK){
            requestPhotoTask();
        } else if(requestCode == Constants.ALBUM_PICTURE){
            if (data != null) {
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                startPhotoCrop(selectedImage);
            }
        } else if(requestCode == Constants.CROP_PICTURE){
            File file = new File(path);
            if (file.exists()) {
                // 发送压缩后的图到服务器
                uploadPhoto(file,"", classId);
            }
        } else if(requestCode == Constants.TAKE_PICTURE){
            startPhotoCrop(Uri.fromFile(new File(path)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(permission.permissionsResult(requestCode,permissions,grantResults)){
            photo();
        }
        Log.i("test","权限回调码="+grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 开始上传
     * @param file
     * @param s
     * @param classId
     */
    private void uploadPhoto(File file, String s, String classId) {
       showLoadingView();
        Map<String, String> pictureMap = new HashMap<>();
        pictureMap.put("photo", path);
        new HttpPostOld(ClassPhotoActivity.this, ClassPhotoActivity.this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_success), Toast.LENGTH_LONG).show();
                    getPhotoTask();
                } else {
                    hideLoadingView();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_fail) + msg, Toast.LENGTH_LONG).show();
                }
            }
        }, null, pictureMap, Urls.POST_UPLOAD_CLASS_PHOTO_ACTION,classId,"").excute(1000);
    }

    /**
     * 调用系统图片编辑进行裁剪
     */
    public void startPhotoCrop(Uri uri) {

        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(FILE_SAVEPATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");

            File file = new File(dir, String.valueOf(System.currentTimeMillis()) + "classphoto.jpg");
            path = file.getPath();
            Uri imageUri = Uri.fromFile(file);

            intent.putExtra("crop", "true");
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true); // no face detection
            startActivityForResult(intent, Constants.CROP_PICTURE);

        } else {
            Toast.makeText(ClassPhotoActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_LONG).show();
        }
    }
}
