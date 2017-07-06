package com.coder.kzxt.setting.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.order.beans.AddressBean;
import com.coder.kzxt.setting.activity.AddressEditActivity;
import com.coder.kzxt.setting.adapter.MyAddressItemAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.Urls;

import static com.coder.kzxt.activity.R.id.ll_new_address;
import static com.coder.kzxt.activity.R.id.load_fail_button;

/**
 * 公开课地址
 * Created by wangtingshun on 2017/3/9.
 */

public class PublicCourseAddressFragment extends BaseFragment implements MyAddressItemAdapter.OnItemClickListener {

    private Context mContext;
    private RecyclerView recyclerView;
    private LinearLayout newAddress;
    private MyAddressItemAdapter adapter;
    private SharedPreferencesUtil spu;
    /** 是否已经请求了网络数据 */
    private boolean isPrepared = false;
    private boolean hasLoad = false;
    private View rootView;
    private String isCenter = "1";
    private int cloudSwitch = -1;  //中心地址开关 0：关闭 1：开启
    private String userInfo; //用户信息页面数据
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button loadFailBtn;
    private RelativeLayout mylayout;
    //是否是默认或者删除地址
    private boolean hadNormalDelete = false;
    private Dialog asyDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(mContext);
        cloudSwitch = getArguments().getInt(Constants.CLOUD_SWITCH,-1);
        userInfo = getArguments().getString(Constants.USER_INFO_ENTRANCE,"");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.new_address_list,container,false);
            initView();
            initListener();
        }
        isPrepared = true;
        requestData();
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initListener() {
        newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddressEditActivity.class);
                intent.putExtra(Constants.ADD_ADDRESS, "newAddress");
                intent.putExtra(Constants.IS_CENTER,isCenter);
                startActivityForResult(intent,Constants.REQUEST_CODE);
            }
        });
//        load_fail_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getRequestData();
//            }
//        });
    }

    private void initView() {
        newAddress = (LinearLayout) rootView.findViewById(ll_new_address);
        jiazai_layout = (LinearLayout) rootView.findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) rootView.findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) rootView.findViewById(R.id.no_info_layout);
        loadFailBtn = (Button) rootView.findViewById(load_fail_button);
        mylayout = (RelativeLayout) rootView.findViewById(R.id.rl_address);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    protected void requestData() {
        if (!isPrepared  ) {
            return;
        }
        if (hasLoad) {
            return;
        }
        getMyAddressTask();
    }

    /**
     * 获取我的地址
     */
    private void getMyAddressTask() {
        if(!hadNormalDelete){
            loadingPage();
        }
//        new HttpGetOld(mContext, this, new InterfaceHttpResult() {
//            @Override
//            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//                hasLoad = true;
//                visibleData();
//                cancleDialog();
//                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
//                    ArrayList<AddressBean.Address> address = ((MyAddressResult) baseBean).getData();
//                    if (address != null && address.size() > 0) {
//                        adapter = new MyAddressItemAdapter(mContext, address);
//                        recyclerView.setAdapter(adapter);
//                        adapter.setOnItemClickListener(PublicCourseAddressFragment.this);
//                    } else {
//                        noDataPage();
//                    }
//                } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
//                    NetworkUtil.httpRestartLogin(PublicCourseAddressFragment.this, mylayout);
//                } else {
//                    NetworkUtil.httpNetErrTip(mContext, mylayout);
//                    loadFailPage();
//                }
//            }
//        }, MyAddressResult.class, Urls.GET_MY_ADDRESS_ACTION, spu.getUid(), spu.getIsLogin(), spu.getTokenSecret(),spu.getDevicedId(),isCenter).excute(1000);
    }

    private void cancleDialog() {
        if (asyDialog != null && asyDialog.isShowing()) {
            asyDialog.cancel();
        }
    }


    @Override
    public void onItemClick(AddressBean.Address addressBean) {
        if(addressBean != null){
            getNormalAndDeleteAddressRequest(2,addressBean);
        }
    }

    @Override
    public void onEditClick(AddressBean.Address addressBean, int position) {
        if(addressBean != null){  //跳转到编辑页面
            Intent intent = new Intent(mContext, AddressEditActivity.class);
            intent.putExtra(Constants.ADD_ADDRESS, "editAddress");
            intent.putExtra("address",addressBean);
            intent.putExtra(Constants.IS_CENTER,isCenter);
            startActivityForResult(intent,Constants.REQUEST_CODE);
        }
    }

    @Override
    public void onNormalAddressClick(CheckBox checkBox, AddressBean.Address addressBean) {
        if (!checkBox.isChecked()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        getNormalAndDeleteAddressRequest(1,addressBean);
    }

    @Override
    public void onAddressClick(AddressBean.Address addressBean) {
        if(TextUtils.isEmpty(userInfo) || !userInfo.equals("user_info")){
            Intent intent = new Intent();
            intent.putExtra("addressBean",addressBean);
//            mContext.setResult(Constants.RESULT_CODE,intent);
//            mContext.finish();
        }
    }

    @Override
    public void onDeleteClick(AddressBean.Address addressBean) {
        alertDeleteDialog(addressBean);
    }

    private void alertDeleteDialog(final AddressBean.Address addressBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.custom_dialog);
        builder.setTitle(getResources().getString(R.string.dialog_livelesson_prompt));
        builder.setMessage(getResources().getString(R.string.sure_to_delete));
        builder.setPositiveButton(getResources().getString(R.string.password_dialog_ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getNormalAndDeleteAddressRequest(2,addressBean);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 设置默认地址和删除地址
     * @param type
     * @param addressBean
     */
    private void getNormalAndDeleteAddressRequest(int type,AddressBean.Address addressBean){
        asyDialog = MyPublicDialog.createLoadingDialog(mContext);
        asyDialog.show();
        new HttpPostOld(mContext,this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    hadNormalDelete = true;
                    getMyAddressTask();
                } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    NetworkUtil.httpRestartLogin(PublicCourseAddressFragment.this, mylayout);
                } else {
                    NetworkUtil.httpNetErrTip(mContext, mylayout);
                    loadFailPage();
                }
            }
        }, null, Urls.POST_SET_DEFAULT_AND_DELADDRESS_ACTION,spu.getUid(),spu.getIsLogin(),spu.getTokenSecret(),spu.getDevicedId(),
                String.valueOf(type),addressBean.getId(),isCenter).excute(1000);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_CODE && resultCode == Constants.RESULT_CODE){
            getMyAddressTask();
        }
    }

    /**
     * 暂无数据
     */
    private void noDataPage() {
        load_fail_layout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        load_fail_layout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    /**
     * 显示数据
     */
    private void visibleData() {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * 显示加载页
     */
    private void loadingPage() {
        jiazai_layout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }
}
