package com.coder.kzxt.setting.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.order.beans.AddressBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.ValidatorUtil;
import com.coder.kzxt.views.CustomNewDialog;
import com.coder.kzxt.wheel.widget.OnWheelChangedListener;
import com.coder.kzxt.wheel.widget.WheelView;
import com.coder.kzxt.wheel.widget.adapters.ArrayWheelAdapter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 地址编辑
 * Created by wangtingshun on 2017/3/9.
 */

public class AddressEditActivity extends BaseActivity implements AMapLocationListener, OnWheelChangedListener
{

    private EditText recipients; //收件人
    private EditText phone_number; //电话号码
    private TextView order_area; //地区
    private EditText detail_address;//详细地址
    private TextView addressArea;
    private CustomNewDialog localDialog;
    private TextView title; //标题
    private ImageView leftImage; //返回键
    private TextView save;
    private int type;
    private int receiveId; //收货人id
    private SharedPreferencesUtil spu;
    private String flag;
    private Dialog asyDialog;
    private double yLatitude;
    private double xLongitude;
    // 定位
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private CustomNewDialog addressDialog; //选择地址对话框
    private WheelView mViewProvince, mViewCity, mViewDistrict;
    private TextView tv_cancle; //取消
    private TextView tv_sure; //确定
    private String receiver; //收件人姓名
    private String phoneNumber;  //电话号码
    private String totalArea; //全区域
    private String detailAddress; //详细地址
    private String province; //省
    private String city;  //市
    private String district;  //区，县级市
    private String publicCourse;  //区分本校还是公开课
    //    private ImageView iv_recipients_delete,iv_phone_delete,iv_position,iv_detail_delete;
    private AddressBean.Address addressBean;
    private LinearLayout myLayout;

    private AddressParserActivity addressParser;
    private static final String TAG = AddressEditActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_edit_layout);
        spu = new SharedPreferencesUtil(this);
        flag = getIntent().getStringExtra(Constants.ADD_ADDRESS);
        addressBean = (AddressBean.Address) getIntent().getSerializableExtra("address");
        publicCourse = getIntent().getStringExtra(Constants.IS_CENTER);
        addressParser = new AddressParserActivity(this);
        initView();
        initListener();
        popupSoftKeybord();
    }

    private void initView()
    {
        recipients = (EditText) findViewById(R.id.et_address_recipients);
        phone_number = (EditText) findViewById(R.id.et_phone_number);
        order_area = (TextView) findViewById(R.id.et_order_area);
        detail_address = (EditText) findViewById(R.id.et_detail_address);
        addressArea = (TextView) findViewById(R.id.tv_area);
        title = (TextView) findViewById(R.id.title);
        myLayout = (LinearLayout) findViewById(R.id.ll_edit_layout);
        leftImage = (ImageView) findViewById(R.id.leftImage);
        save = (TextView) findViewById(R.id.rightText);
        recipients.setFocusable(true);
        recipients.setFocusableInTouchMode(true);
        recipients.requestFocus();
        recipients.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40), new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                if (source.equals(" "))
                {
                    return "";
                } else
                {
                    return null;
                }
            }
        }});
        phone_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        detail_address.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200), new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                if (source.equals(" "))
                {
                    return "";
                } else
                {
                    return null;
                }
            }
        }});
        save.setText(getResources().getString(R.string.save));

//        iv_recipients_delete = (ImageView) findViewById(R.id.iv_recipients_delete);
//        iv_phone_delete = (ImageView) findViewById(R.id.iv_phone_delete);
//        iv_position = (ImageView) findViewById(R.id.iv_position);
//        iv_detail_delete = (ImageView) findViewById(R.id.iv_detail_delete);

        mlocationClient = new AMapLocationClient(this.getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        mlocationClient.setLocationListener(this);
        mLocationOption.setOnceLocation(true);

        initTitle();

        if (flag.equals("editAddress") && addressBean != null)
        {
            receiver = addressBean.getReceiver();
            phoneNumber = addressBean.getMobile();
            detailAddress = addressBean.getAddress_detail();
            province = addressBean.getProvince();
            city = addressBean.getCity();
            district = addressBean.getDistrict();
            if (TextUtils.equals(province, city))
            {
                totalArea = province + district;
            } else
            {
                totalArea = province + city + district;
            }
            receiveId = Integer.parseInt(addressBean.getId());
            initContent(receiver, phoneNumber, totalArea, detailAddress);
            setCursor(recipients);
            save.setTextColor(getResources().getColor(R.color.font_white));
//            changeState();
        } else
        {
            save.setTextColor(getResources().getColor(R.color.color_btn));
        }
    }

    /**
     * 设置光标
     *
     * @param edit
     */
    private void setCursor(EditText edit)
    {
        Editable text = edit.getText();
        Spannable spanText = text;
        Selection.setSelection(spanText, text.length());
    }

    private void popupSoftKeybord()
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                InputMethodManager manager = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                manager.showSoftInput(recipients, 0);
            }
        }, 500);
    }


    private void initTitle()
    {
        if (flag.equals("newAddress"))
        {
            type = 1;
            title.setText("新增地址");
        } else if (flag.equals("editAddress"))
        {
            type = 2;
            title.setText(getResources().getString(R.string.edit_address));
        } else
        {
            title.setText(getResources().getString(R.string.new_address));
        }
    }

//    private void changeState() {
//        changeReceiverVisibility();
//        changeNumberVisibility();
//        changeAreaVisibility();
//        changeAddressVisibility();
//    }

    private void initContent(String receive, String number, String addressArea, String addressDetail)
    {
        recipients.setText(receive);
        phone_number.setText(number);
        order_area.setText(addressArea);
        detail_address.setText(addressDetail);
    }

    private void initListener()
    {
        leftImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
//        iv_recipients_delete.setOnClickListener(listener);
//        iv_phone_delete.setOnClickListener(listener);
//        iv_detail_delete.setOnClickListener(listener);
//        iv_position.setOnClickListener(listener);

        save.setOnClickListener(new View.OnClickListener()
        {   //保存地址
            @Override
            public void onClick(View v)
            {
                if (changeSaveButtonState())
                {
                    checkInfo(receiver, phoneNumber, totalArea, detailAddress);
                }
            }
        });

        recipients.addTextChangedListener(new TextWatcher()
        {     //收件人
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int mTextMaxlenght = 0;
                Editable editable = recipients.getText();
                String str = editable.toString().trim();
                //得到最初字段的长度大小，用于光标位置的判断
                int selEndIndex = Selection.getSelectionEnd(editable);
                // 取出每个字符进行判断，如果是字母数字和标点符号则为一个字符加1,如果是汉字则为两个字符
                for (int i = 0; i < str.length(); i++)
                {
                    char charAt = str.charAt(i);
                    //32-122包含了空格，大小写字母，数字和一些常用的符号，
                    //如果在这个范围内则算一个字符，
                    //如果不在这个范围比如是汉字的话就是两个字符
                    if (charAt >= 32 && charAt <= 122)
                    {
                        mTextMaxlenght++;
                    } else
                    {
                        mTextMaxlenght += 2;
                    }
                    // 当最大字符大于40时，进行字段的截取，并进行提示字段的大小
                    if (mTextMaxlenght >= 40)
                    {
                        // 截取最大的字段
                        String newStr = str.substring(0, i);
                        recipients.setText(newStr);
                        // 得到新字段的长度值
                        editable = recipients.getText();
                        int newLen = editable.length();
                        if (selEndIndex > newLen)
                        {
                            selEndIndex = editable.length();
                        }
                        // 设置新光标所在的位置
                        Selection.setSelection(editable, selEndIndex);
                        Toast.makeText(AddressEditActivity.this, "最大长度为40个字符或20个汉字！", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                receiver = recipients.getText().toString().trim();
                changeSaveButtonState();
//                if(recipients.length() >= 20){
//                    Toast.makeText(AddressEditActivity.this,"限20个字符以内",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                changeReceiverVisibility();
            }
        });

        phone_number.addTextChangedListener(new TextWatcher()
        {   //手机号
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                phoneNumber = phone_number.getText().toString();
                changeSaveButtonState();
//                changeNumberVisibility();
            }
        });

        order_area.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                collapseSoftInputMethod(recipients);
                collapseSoftInputMethod(phone_number);
                collapseSoftInputMethod(detail_address);
                alertAddressSelectDialog();
                changeSaveButtonState();
            }
        });

        detail_address.addTextChangedListener(new TextWatcher()
        {     //详细地址
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                detailAddress = detail_address.getText().toString();
                changeSaveButtonState();
                if (detailAddress.length() >= 200)
                {
                    Toast.makeText(AddressEditActivity.this, "限200个字符以内", Toast.LENGTH_SHORT).show();
                    return;
                }
//                changeAddressVisibility();
            }
        });

        detail_address.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        //定位位置
//        position.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 开启定位
//                mlocationClient.startLocation();
//            }
//        });

    }

    /**
     * 改变保存按钮状态
     */
    private boolean changeSaveButtonState()
    {
        if (!TextUtils.isEmpty(receiver) && !TextUtils.isEmpty(phoneNumber)
                && !TextUtils.isEmpty(totalArea) && !TextUtils.isEmpty(detailAddress))
        {
            save.setTextColor(getResources().getColor(R.color.font_white));
            return true;
        } else
        {
            save.setTextColor(getResources().getColor(R.color.color_btn));
            return false;
        }
    }


    /**
     * 收起软键盘并设置提示文字
     */
    public void collapseSoftInputMethod(EditText editText)
    {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(editText.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void checkInfo(String receiver, String phoneNumber, String totalArea, String detailAddress)
    {

        if (TextUtils.isEmpty(receiver))
        {
            Toast.makeText(AddressEditActivity.this, "请输入收货人", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(phoneNumber))
        {
            if (ValidatorUtil.isMobile(phoneNumber))
            {

            } else
            {
                ToastUtils.makeText(AddressEditActivity.this, getResources().getString(R.string.phone_legal_number), Toast.LENGTH_SHORT).show();
                return;
            }
        } else
        {
            Toast.makeText(AddressEditActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(order_area.getText().toString()))
        {
            Toast.makeText(AddressEditActivity.this, "请输入收货地区", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(detailAddress))
        {
            Toast.makeText(AddressEditActivity.this, "请输入详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, "receiver=" + receiver + "phoneNumber=" + phoneNumber + "totalArea=" + totalArea + "detailAddress=" + detailAddress);
        submitSaveAddressData();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation)
    {
        if (aMapLocation != null)
        {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0)
            {
                yLatitude = aMapLocation.getLatitude();// 获取纬度
                xLongitude = aMapLocation.getLongitude();// 获取经度
                order_area.setText(aMapLocation.getAddress());
                // 停止定位
                mlocationClient.stopLocation();
            } else if (NetworkUtil.isNetworkAvailable(this))
            {
                // 12是缺少定位权限
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                if (aMapLocation.getErrorCode() == 12)
                {
                    showLocalDialog();
                }

            }
        }
    }

    private void showLocalDialog()
    {
        if (localDialog == null || !localDialog.isShowing())
        {
            localDialog = new CustomNewDialog(this);
            localDialog.setMessage("您未打开GPS或未获取到定位权限");
            localDialog.setButtonOne(true);
            localDialog.show();
        }
    }

    private void alertAddressSelectDialog()
    {
        init();
        initWheelView();
        initWheelListener();
        setUpData();
    }

    private void init()
    {
        addressDialog = new CustomNewDialog(this, R.layout.address_select_dialog);
        tv_sure = (TextView) addressDialog.findViewById(R.id.tv_sure);
        tv_cancle = (TextView) addressDialog.findViewById(R.id.tv_cancle);
        Window dialogWindow = addressDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        addressDialog.show();
    }


    private void initWheelView()
    {
        mViewProvince = (WheelView) addressDialog.findViewById(R.id.main_wheelview);
        mViewCity = (WheelView) addressDialog.findViewById(R.id.sub_wheelview);
        mViewDistrict = (WheelView) addressDialog.findViewById(R.id.child_wheelview);
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
                if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(district))
                {
                    totalArea = province + " " + city + " " + district;
                    order_area.setText(totalArea);
                }
//                if (order_area.getText().length() > 0) {
//                    iv_position.setVisibility(View.VISIBLE);
//                } else {
//                    iv_position.setVisibility(View.GONE);
//                }
                if (addressDialog.isShowing())
                {
                    addressDialog.cancel();
                }
            }
        });

        tv_cancle.setOnClickListener(new View.OnClickListener()
        { //取消
            @Override
            public void onClick(View v)
            {
                if (addressDialog.isShowing())
                {
                    addressDialog.cancel();
                }
            }
        });

    }

    private void setUpData()
    {
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(AddressEditActivity.this, addressParser.getmProvinceDatas()));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateProvince();
        updateCity();
        updateDistrict();
    }


    /**
     * 提交保存地址数据
     */
    private void submitSaveAddressData()
    {
        asyDialog = MyPublicDialog.createLoadingDialog(this);
        asyDialog.show();
        if (type == 2)
        {
            updateAddress();
        } else
        {
            saveNewAddress();
        }
    }

    /**
     * 更新地址信息
     */
    private void updateAddress()
    {
        new HttpPutBuilder(this)
                .setUrl(UrlsNew.PUT_UPDATE_ADDRESS)
                .setHttpResult(new HttpCallBack()
                {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean)
                    {
                        saveSuccess();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg)
                    {
                        if (asyDialog != null && asyDialog.isShowing())
                        {
                            asyDialog.cancel();
                        }
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
                        {
                            NetworkUtil.httpRestartLogin(AddressEditActivity.this, myLayout);
                        } else
                        {
                            NetworkUtil.httpNetErrTip(AddressEditActivity.this, myLayout);
                        }
                        saveFial();
                    }
                })
                .setClassObj(null)
                .addBodyParam("receiver", receiver)
                .addBodyParam("province", province)
                .addBodyParam("city", city)
                .addBodyParam("district", district)
                .addBodyParam("address_detail", detailAddress)
                .addBodyParam("mobile", phoneNumber)
                .setPath(addressBean.getId())
                .build();
    }

    /**
     * 保存新建地址
     */
    private void saveNewAddress()
    {
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_SAVE_ADDRESS)
                .setHttpResult(new HttpCallBack()
                {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean)
                    {
                        if (asyDialog != null && asyDialog.isShowing())
                        {
                            asyDialog.cancel();
                        }
                        saveSuccess();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg)
                    {
                        if (asyDialog != null && asyDialog.isShowing())
                        {
                            asyDialog.cancel();
                        }
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
                        {
                            NetworkUtil.httpRestartLogin(AddressEditActivity.this, myLayout);
                        } else
                        {
                            NetworkUtil.httpNetErrTip(AddressEditActivity.this, myLayout);
                        }
                        if (code == 8026)
                        {
                            ToastUtils.makeText(AddressEditActivity.this, "详细地址请输入5-150个字符", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            saveFial();
                        }
                    }
                })
                .setClassObj(null)
                .addBodyParam("receiver", receiver)
                .addBodyParam("province", province)
                .addBodyParam("city", city)
                .addBodyParam("district", district)
                .addBodyParam("address_detail", detailAddress)
                .addBodyParam("mobile", phoneNumber)
                .build();
    }

    /**
     * 保存地址失败
     */
    private void saveFial()
    {
        if (type == 2)
        {
            ToastUtils.makeText(AddressEditActivity.this, "更新地址失败", Toast.LENGTH_SHORT).show();
        } else
        {
            ToastUtils.makeText(AddressEditActivity.this, "保存地址失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 保存地址成功
     */
    private void saveSuccess()
    {
        if (flag.equals("addAddress"))
        {
            Intent intent = new Intent(AddressEditActivity.this, MyAddressActivity.class);
            startActivityForResult(intent, 10);
//            setResult(10);
        } else
        {
            setResult(Constants.RESULT_CODE);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 10 && resultCode == 100)
        {
            setResult(100);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
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
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
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
        addressParser.setmCurrentZipCode(addressParser.mZipcodeDatasMap.get(district));
    }


    public View.OnClickListener listener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.iv_recipients_delete:
                    recipients.setText("");
                    break;
                case R.id.iv_phone_delete:
                    phone_number.setText("");
                    break;
                case R.id.iv_position:
                    order_area.setText("");
//                    iv_position.setVisibility(View.GONE);
                    break;
                case R.id.iv_detail_delete:
                    detail_address.setText("");
                    break;

                default:
                    break;
            }
        }
    };

}
