package com.coder.kzxt.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.setting.activity.AddressParserActivity;
import com.coder.kzxt.wheel.widget.OnWheelChangedListener;
import com.coder.kzxt.wheel.widget.WheelView;
import com.coder.kzxt.wheel.widget.adapters.ArrayWheelAdapter;


/**
 * Created by MaShiZhao on 2017/6/20
 */

public class SelectAddressDialog extends Dialog implements OnWheelChangedListener
{

    private Context context;
    private WheelView mViewProvince, mViewCity, mViewDistrict;
    private TextView tv_cancle; //取消
    private TextView tv_sure; //确定
    private String province; //省
    private String city;  //市
    private String district;  //区，县级市
    private AddressParserActivity addressParser;
    private SelectAddressClickListener selectAddressClickListener;

    public SelectAddressDialog(final Context context)
    {
        super(context, R.style.custom_dialog);
        this.setContentView(R.layout.address_select_dialog);
        final Window dialogWindow = this.getWindow();
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setBackgroundDrawableResource(R.color.trans_eight);
        this.setCanceledOnTouchOutside(true);
        this.context = context;

        addressParser = new AddressParserActivity(context);
        initWheelView();
        initWheelListener();
        setUpData();
    }

    public void setSelectAddressClickListener(SelectAddressClickListener selectAddressClickListener)
    {
        this.selectAddressClickListener = selectAddressClickListener;

    }


    private void initWheelView()
    {
        mViewProvince = (WheelView) findViewById(R.id.main_wheelview);
        mViewCity = (WheelView) findViewById(R.id.sub_wheelview);
        mViewDistrict = (WheelView) findViewById(R.id.child_wheelview);

        tv_sure = (TextView) findViewById(R.id.tv_sure);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
    }

    private void initWheelListener()
    {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        tv_sure.setOnClickListener(new View.OnClickListener()
        { //确定
            @Override
            public void onClick(View v)
            {
//                if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
//                    totalArea = province + " " + city + " " + district;
//                    order_area.setText(totalArea);
//                }
//                if (order_area.getText().length() > 0) {
//                    iv_position.setVisibility(View.VISIBLE);
//                } else {
//                    iv_position.setVisibility(View.GONE);
//                }
                if (selectAddressClickListener != null)
                {
                    selectAddressClickListener.onClick(province, city, district);
                }
                cancel();

            }
        });

        tv_cancle.setOnClickListener(new View.OnClickListener()
        { //取消
            @Override
            public void onClick(View v)
            {
                cancel();
            }
        });

    }

    private void setUpData()
    {
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, addressParser.getmProvinceDatas()));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateProvince();
        updateCity();
        updateDistrict();
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue)
    {
        if (wheel == mViewProvince)
        {
            updateProvince();
        } else if (wheel == mViewCity)
        {
            updateCity();
        } else if (wheel == mViewDistrict)
        {
            updateDistrict();
        }
    }


    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateCity()
    {
        int pCurrent = mViewCity.getCurrentItem();
        city = addressParser.getmCitisDatasMap().get(province)[pCurrent];
        String[] areas = addressParser.getmDistrictDatasMap().get(city);

        if (areas == null)
        {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
        mViewDistrict.setCurrentItem(0);
        updateDistrict();
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateProvince()
    {
        int pCurrent = mViewProvince.getCurrentItem();
        province = addressParser.getmProvinceDatas()[pCurrent];
        String[] cities = addressParser.getmCitisDatasMap().get(province);
        if (cities == null)
        {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
        mViewCity.setCurrentItem(0);
        updateCity();
    }

    /**
     * 根据当前的市更新区
     */
    private void updateDistrict()
    {
        int pCurrent = mViewDistrict.getCurrentItem();
        district = addressParser.getmDistrictDatasMap().get(city)[pCurrent];
        addressParser.setmCurrentZipCode(addressParser.getmZipcodeDatasMap().get(district));
    }


    public interface SelectAddressClickListener
    {
        public void onClick(String province, String city, String district);

    }


}
