package com.coder.kzxt.setting.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpDeleteBuilder;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.order.beans.AddressBean;
import com.coder.kzxt.setting.activity.AddressEditActivity;
import com.coder.kzxt.setting.adapter.MyAddressItemAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;

import java.util.ArrayList;


/**
 * 本校地址
 * Created by wangtingshun on 2017/3/9.
 */

public class SchoolAddressFragment extends BaseFragment implements MyAddressItemAdapter.OnItemClickListener{

    private Activity mContext;
    private static int mpage;
    public static final String ARGS_PAGE = "args_page";
    private static final String DATA = "args_data";
    private RecyclerView recyclerView;
    private LinearLayout newAddress;
    private MyAddressItemAdapter adapter;
    private SharedPreferencesUtil spu;
    /** 是否已经请求了网络数据 */
    private boolean isPrepared = false;
    private boolean hasLoad = false;
    private View rootView;
    private String isCenter = "0";
    private int localSwitch = -1;  //本站地址开关 0：关闭 1：开启
    private String userInfo; //用户信息页面数据
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button loadFailBtn;
    private RelativeLayout mylayout;
    //是否是默认或者删除地址
    private boolean hadNormalDelete = false;
    private Dialog asyDialog;
    private RelativeLayout rl_address;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(mContext);
        localSwitch = getArguments().getInt(Constants.LOCAL_SWITCH,-1);
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
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyAddressTask();
            }
        });
    }

    private void initView() {
        newAddress = (LinearLayout) rootView.findViewById(R.id.ll_new_address);
        jiazai_layout = (LinearLayout) rootView.findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) rootView.findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) rootView.findViewById(R.id.no_info_layout);
        loadFailBtn = (Button) rootView.findViewById(R.id.load_fail_button);
        mylayout = (RelativeLayout) rootView.findViewById(R.id.rl_layout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        rl_address = (RelativeLayout) rootView.findViewById(R.id.rl_address);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void requestData() {
        loadingPage();
        getMyAddressTask();
    }

    private void loadDialog(){
        asyDialog = MyPublicDialog.createLoadingDialog(mContext);
        asyDialog.show();
    }

    public void refreshData(){
        loadDialog();
        getMyAddressTask();
    }

    /**
     * 获取我的地址
     */
    private void getMyAddressTask() {
                new HttpGetBuilder(mContext)
                .setUrl(UrlsNew.GET_USER_ADDRESS_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        visibleData();
                        cancleDialog();
                        AddressBean addressBean = (AddressBean) resultBean;
                        ArrayList<AddressBean.Address> items =  addressBean.getItems();
                        AddressBean.Paginate paginate = addressBean.getPaginate();
                        int totalNum = paginate.getTotalNum();
                        if(totalNum > 0){
                            adapter = new MyAddressItemAdapter(mContext, items);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(SchoolAddressFragment.this);
                        } else {
                            noDataPage();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(SchoolAddressFragment.this, mylayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mContext, mylayout);
                        }
                        loadFailPage();
                        cancleDialog();
                    }
                })
                .setClassObj(AddressBean.class)
                .addQueryParams("user_id",spu.getUid())
                .build();

    }

    private void cancleDialog() {
        if (asyDialog != null && asyDialog.isShowing()) {
            asyDialog.cancel();
        }
    }

    @Override
    public void onItemClick(AddressBean.Address addressBean) {
        if(addressBean != null){

        }
    }

    @Override
    public void onEditClick(AddressBean.Address addressBean, int position) {
        if (addressBean != null) {  //跳转到编辑页面
            Intent intent = new Intent(mContext, AddressEditActivity.class);
            intent.putExtra(Constants.ADD_ADDRESS, "editAddress");
            intent.putExtra("address", addressBean);
            startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    }

    @Override
    public void onNormalAddressClick(CheckBox checkBox, AddressBean.Address addressBean) {
        loadDialog();
        setDefaultAddress(checkBox,addressBean);
    }

    /**
     * 设置默认地址
     * @param addressBean
     */
    private void setDefaultAddress(final CheckBox checkBox, AddressBean.Address addressBean) {
                new HttpPutBuilder(mContext)
                .setUrl(UrlsNew.PUT_DEFAULT_ADDRESS)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        getMyAddressTask();
                        setDefaultAddressSuccess(checkBox);
                    }
                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleDialog();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(SchoolAddressFragment.this, mylayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mContext, mylayout);
                        }
                    }
                })
                .setClassObj(null)
                .setPath(addressBean.getId())
                .build();
    }

    /**
     * 设置默认地址成功
     */
    private void setDefaultAddressSuccess(CheckBox checkBox) {
        if (!checkBox.isChecked()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    @Override
    public void onAddressClick(AddressBean.Address addressBean) {
        if(!TextUtils.isEmpty(userInfo) && userInfo.equals("order")){
            Intent intent = new Intent();
            intent.putExtra("addressBean",addressBean);
            mContext.setResult(Constants.RESULT_CODE,intent);
            mContext.finish();
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
               deleteAddress(addressBean);
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
     * 删除地址
     */
    private void deleteAddress(AddressBean.Address addressBean) {
                loadDialog();
                new HttpDeleteBuilder(mContext)
                .setUrl(UrlsNew.DELETE_ADDRESS)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        getMyAddressTask();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        cancleDialog();
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(SchoolAddressFragment.this, mylayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mContext, mylayout);
                        }
                        ToastUtils.makeText(mContext,getResources().getString(R.string.delete_fail), Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(null)
                .setPath(addressBean.getId())
                .build();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == Constants.RESULT_CODE) {
            getMyAddressTask();
        } else if (requestCode == Constants.REQUEST_CODE && resultCode == 100) {

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
