package com.coder.kzxt.setting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.order.beans.AddressBean;

import java.util.ArrayList;

/**
 * Created by wangtingshun on 2016/11/15.
 * 我的地址适配器
 */
public class MyAddressItemAdapter extends RecyclerView.Adapter<MyAddressItemAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<AddressBean.Address> addressList;

    public MyAddressItemAdapter(Context context, ArrayList<AddressBean.Address> address){
        this.mContext = context;
        this.addressList = address;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.my_address_item,parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AddressBean.Address addressBean = addressList.get(position);
        String provice = addressBean.getProvince();
        String city = addressBean.getCity();
        String district = addressBean.getDistrict();
        String area = null;
        if(TextUtils.equals(provice,city)){
            area = provice + " "+ district +" ";
        }else {
            area = provice + " "+ city + " "+ district +" ";
        }
        String addressDetail = addressBean.getAddress_detail();
        holder.tv_address.setText(area + addressDetail);
        holder.tv_phone_number.setText(addressBean.getMobile());
        holder.tv_receiver.setText(addressBean.getReceiver());
        if (addressBean.getIs_default().equals("0")) {
            holder.check_normal_address.setChecked(false);
            holder.tv_normal_address.setTextColor(mContext.getResources().getColor(R.color.sign_font_black));
        } else {
            holder.check_normal_address.setChecked(true);
            holder.tv_normal_address.setTextColor(mContext.getResources().getColor(R.color.first_theme));
        }

        if (mOnItemClickListener != null) {
            holder.ll_normal_address.setOnClickListener(new View.OnClickListener() {  //设置默认地址
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onNormalAddressClick(holder.check_normal_address,addressBean);
                }
            });

            holder.ll_edit.setOnClickListener(new View.OnClickListener() {    //编辑
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onEditClick(addressBean,position);
                }
            });

            holder.ll_delete.setOnClickListener(new View.OnClickListener() {     //删除
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onDeleteClick(addressBean);
                }
            });

            //
            holder.rl_top_address.setOnClickListener(new View.OnClickListener() {  //点击地址
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onAddressClick(addressBean);
                }
            });
            holder.rl_bottom_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onAddressClick(addressBean);
                }
            });

            holder.tv_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onAddressClick(addressBean);
                }
            });
            holder.tv_receiver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onAddressClick(addressBean);
                }
            });
            holder.tv_phone_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onAddressClick(addressBean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_address; //地址
        private TextView tv_receiver; //收货人
        private TextView tv_phone_number; //电话号码
        private LinearLayout ll_normal_address; //默认地址
        private CheckBox check_normal_address; //
        private LinearLayout ll_edit;  //编辑
        private LinearLayout ll_delete; //删除
        private RelativeLayout rl_top_address;
        private RelativeLayout rl_bottom_address;
        private TextView tv_normal_address;
        private LinearLayout address_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            rl_top_address = (RelativeLayout) itemView.findViewById(R.id.rl_top_address);
            rl_bottom_address = (RelativeLayout) itemView.findViewById(R.id.rl_bottom_address);
            address_item = (LinearLayout) itemView.findViewById(R.id.address_item);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_receiver = (TextView) itemView.findViewById(R.id.tv_receiver);
            tv_phone_number = (TextView) itemView.findViewById(R.id.tv_phone_number);
            ll_normal_address = (LinearLayout) itemView.findViewById(R.id.ll_normal_address);
            check_normal_address = (CheckBox) itemView.findViewById(R.id.iv_normal_address);
            ll_edit = (LinearLayout) itemView.findViewById(R.id.ll_edit);
            ll_delete = (LinearLayout) itemView.findViewById(R.id.ll_delete);
            tv_normal_address = (TextView) itemView.findViewById(R.id.tv_normal_address);
        }

    }

    public OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(AddressBean.Address addressBean);

        void onEditClick(AddressBean.Address addressBean, int position);

        void onNormalAddressClick(CheckBox checkBox, AddressBean.Address addressBean);

        void onAddressClick(AddressBean.Address addressBean);

        void onDeleteClick(AddressBean.Address addressBean);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
