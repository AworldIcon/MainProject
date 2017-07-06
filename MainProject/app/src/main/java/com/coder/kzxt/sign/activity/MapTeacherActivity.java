package com.coder.kzxt.sign.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.sign.adapter.MapLocationDelegate;
import com.coder.kzxt.utils.MyApplication;
import com.coder.kzxt.utils.PublicUtils;
import com.coder.kzxt.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by MaShiZhao on 2017/4/18
 * 地图定位 老师端
 */

public class MapTeacherActivity extends BaseActivity implements AMap.OnCameraChangeListener, AMapLocationListener
{

    private android.support.v7.widget.Toolbar toolbar;
    private MapView mapView;
    private LinearLayout map_ly;
    private MyRecyclerView list;
    private AMap aMap;
    private ProgressBar progressBar;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationSource.OnLocationChangedListener mListener;
    private Marker marker2;// 有跳动效果的marker对象
    private LatLng locationLatLng;
    private LatLonPoint latLonPoint;
    private boolean fri;
    private PublicUtils pu;
    private ArrayList<PoiItem> pois;
    private BaseRecyclerAdapter mapLocationAdapter;

    public static String POSITION = "location";
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";
    public static int RESULTCODE_SIGNMANAGE = 1001;

    private void initVariable()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_teacher);
        //初始化 view findViewById
        initFindViewById(savedInstanceState);
        //需要初始化的数据
        initData();
        //响应事件click
        initClick();
        //网络请求
        httpRequest();
    }


    private void initFindViewById(Bundle savedInstanceState)
    {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mapView = MyApplication.mapView;
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        map_ly = (LinearLayout) findViewById(R.id.map_ly);
        list = (MyRecyclerView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mapView.setLayoutParams(params);
        ViewGroup parent = (ViewGroup) mapView.getParent();
        if (parent != null)
        {
            parent.removeAllViews();
            map_ly.addView(mapView);
        } else
        {
            map_ly.addView(mapView);
        }
        toolbar.setTitle(getResources().getString(R.string.map));
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void httpRequest()
    {
    }

    private void initData()
    {

        if (aMap == null)
        {
            aMap = MyApplication.aMap;
//			aMap = mapView.getMap();
            aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
            aMap.setLocationSource(locationSource);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.getUiSettings().setZoomControlsEnabled(true);// 设置默认的缩放按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        }

        pois = new ArrayList<PoiItem>();
        mapLocationAdapter = new BaseRecyclerAdapter(MapTeacherActivity.this, pois, new MapLocationDelegate(MapTeacherActivity.this));
        list.setAdapter(mapLocationAdapter);
    }

    private void initClick()
    {
        mapLocationAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                Intent data = new Intent();
//                if (position == 0)
//                {
//                    data.putExtra(MapTeacherActivity.POSITION, formatAddress);
//                    data.putExtra(MapTeacherActivity.LONGITUDE, geoLng + "");
//                    data.putExtra(MapTeacherActivity.LATITUDE, geoLat + "");
//                } else
//                {
                PoiItem item = pois.get(position);
                data.putExtra(MapTeacherActivity.POSITION, item.getSnippet());
                data.putExtra(MapTeacherActivity.LONGITUDE, item.getLatLonPoint().getLongitude() + "");
                data.putExtra(MapTeacherActivity.LATITUDE, item.getLatLonPoint().getLatitude() + "");
//                }
                setResult(RESULTCODE_SIGNMANAGE, data);
                finish();
            }
        });
    }


    private void addMarker(LatLng latLng)
    {
        if (!fri)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
            markerOptions.draggable(true);
            // markerOptions.snippet(desc);
            marker2 = aMap.addMarker(markerOptions);
            fri = true;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
        locationSource.deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        aMap.clear();
//		mapView.onDestroy();
    }


    @Override
    public void onCameraChange(CameraPosition position)
    {
        if (position != null)
        {
            LatLng target = position.target;
            marker2.setPosition(target);
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition position)
    {
        if (marker2 != null)
        {
            final LatLng target = position.target;// 目标位置的屏幕中心点经纬度坐标。
            latLonPoint = new LatLonPoint(target.latitude, target.longitude);
            GeocodeSearch geocoderSearch = new GeocodeSearch(MapTeacherActivity.this);
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener()
            {
                @Override
                public void onRegeocodeSearched(RegeocodeResult result, int rCode)
                {
                    if (rCode == 1000)
                    {
                        if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null)
                        {
                            // 当前地址
                            formatAddress = result.getRegeocodeAddress().getFormatAddress();
                            // 当前地址的街道
                            // StreetNumber streetNumber =
                            // result.getRegeocodeAddress().getStreetNumber();
                            // String street = streetNumber.getStreet();
                            // String number = streetNumber.getNumber();
                            // PublicUtils.makeToast(MapTeacherActivity.this, street
                            // + number);
                            // 附近兴趣点地址
                            pois.clear();
                            pois.addAll(result.getRegeocodeAddress().getPois());
                            mapLocationAdapter.notifyDataSetChanged();

                            if (mapLocationAdapter.getShowCount() > 1)
                            {
                                progressBar.setVisibility(View.GONE);
                                list.setVisibility(View.VISIBLE);
                            } else
                            {
                                ToastUtils.makeText(MapTeacherActivity.this, getResources().getString(R.string.get_location_fail));
                            }
                        } else
                        {
                            ToastUtils.makeText(MapTeacherActivity.this, getResources().getString(R.string.found_no_content));
                        }
                    } else if (rCode == 27)
                    {
                        ToastUtils.makeText(MapTeacherActivity.this, getResources().getString(R.string.check_network));
                    } else if (rCode == 32)
                    {
                        ToastUtils.makeText(MapTeacherActivity.this, getResources().getString(R.string.key_wrong));
                    } else
                    {
                        ToastUtils.makeText(MapTeacherActivity.this, getResources().getString(R.string.another_wrong) + rCode);
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult arg0, int arg1)
                {
                }
            });
        }
    }

    private Double geoLat;
    private Double geoLng;
    private String formatAddress;

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation)
    {
        if (mListener != null && amapLocation != null)
        {
            if (amapLocation != null && amapLocation.getErrorCode() == 0)
            {
                // mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                geoLat = amapLocation.getLatitude();// 获取纬度
                geoLng = amapLocation.getLongitude();// 获取经度
                // String address =
                // amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                // String district = amapLocation.getDistrict();//城区信息
                // String cityCode = amapLocation.getCityCode();//城市编码
                // String adCode = amapLocation.getAdCode();//地区编码
                // String city = amapLocation.getCity();//城市信息
                // String country = amapLocation.getCountry();//国家信息
                // String poiName = amapLocation.getPoiName();//兴趣点
                // String road = amapLocation.getRoad();//街道信息
                // String province = amapLocation.getProvince();//省信息
                locationLatLng = new LatLng(geoLat, geoLng);
                addMarker(locationLatLng);
                // marker2.showInfoWindow();
                // 设置地图显示的位置
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 30));
                // aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng,
                // 30));
                locationSource.deactivate();
            } else
            {
//				 String errText = getResources().getString(R.string.location_fail) + amapLocation.getErrorCode() + ": "
                // + amapLocation.getErrorInfo();
                // Log.e("tangcy", errText);
                // PublicUtils.makeToast(MapTeacherActivity.this, errText);
            }
        }
    }

    LocationSource locationSource = new LocationSource()
    {

        @Override
        public void deactivate()
        {
            mListener = null;
            if (mlocationClient != null)
            {
                mlocationClient.stopLocation();
                mlocationClient.onDestroy();
            }
            mlocationClient = null;
        }

        @Override
        public void activate(OnLocationChangedListener listener)
        {
            mListener = listener;
            if (mlocationClient == null)
            {
                mlocationClient = new AMapLocationClient(MapTeacherActivity.this);
                mLocationOption = new AMapLocationClientOption();
                // 设置定位监听
                mlocationClient.setLocationListener(MapTeacherActivity.this);
                // 设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                mLocationOption.setInterval(4000);
                // 设置定位参数
                mlocationClient.setLocationOption(mLocationOption);
                // 只定位一次
                // mLocationOption.setOnceLocation(true);
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用onDestroy()方法
                // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                mlocationClient.startLocation();
            }
        }
    };

}
