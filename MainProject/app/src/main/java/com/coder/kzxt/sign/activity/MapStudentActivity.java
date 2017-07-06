package com.coder.kzxt.sign.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.MyApplication;
import com.coder.kzxt.utils.ToastUtils;

/**
 * Created by MaShiZhao on 2017/4/18
 * 地图定位 学生端
 */

public class MapStudentActivity extends BaseActivity implements AMap.OnCameraChangeListener, AMapLocationListener
{

    private android.support.v7.widget.Toolbar toolbar;
    private MapView mapView;
    private LinearLayout map_ly;
    private AMap aMap;
    private TextView my_location;
    private TextView sign_location;

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private Marker marker2;// 有跳动效果的marker对象
    private LatLng locationLatLng;
    private LatLng myLocationLatLng;
    private Boolean isSigning;


    public static void gotoActivity(BaseActivity context, String latitude, String longitude, Boolean signing)
    {
        context.startActivity(new Intent(context, MapStudentActivity.class)
                .putExtra("latitude", latitude)
                .putExtra("longitude", longitude)
                .putExtra("signing", signing)
        );
    }

    private void initVariable()
    {
        String l = getIntent().getStringExtra("latitude").trim();
        String n = getIntent().getStringExtra("longitude").trim();
        isSigning = getIntent().getBooleanExtra("signing", false);

        if (!l.equals("") && !n.equals(""))
        {
            Double lat = Double.valueOf(l);
            Double lng = Double.valueOf(n);
            locationLatLng = new LatLng(lat, lng);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_student);
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
        mapView.onCreate(savedInstanceState);// 此方法必须重写'mapView
        map_ly = (LinearLayout) findViewById(R.id.map_ly);
        my_location = (TextView) findViewById(R.id.my_location);
        sign_location = (TextView) findViewById(R.id.sign_location);

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
        toolbar.setTitle(getResources().getString(R.string.location));
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
            aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
            aMap.getUiSettings().setZoomControlsEnabled(false);// 设置默认的缩放按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

            for (Marker Marker : aMap.getMapScreenMarkers())
            {
                Marker.remove();
            }
        }
        mlocationClient.startLocation();

    }

    private void initClick()
    {
        my_location.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationLatLng, 30));
            }
        });

        sign_location.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (locationLatLng == null || !isSigning)
                {
                    ToastUtils.makeText(MapStudentActivity.this, "老师没有发起点名!");
                    return;
                }
                // 设置地图显示的位置
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 30));
                // aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng,
                // 30));
            }
        });

    }

    private SensorEventHelper sensor;

    private void addMarker(LatLng latLng)
    {
        sensor = new SensorEventHelper(MapStudentActivity.this);
        sensor.registerSensorListener();


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.my_map_marker)));
        markerOptions.draggable(true);
        // markerOptions.snippet(desc);
        marker2 = aMap.addMarker(markerOptions);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationLatLng, 30));

        sensor.setCurrentMarker(marker2);
    }

    private void addMarker2(LatLng latLng)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_marker)));
        // markerOptions.snippet(desc);
        markerOptions.draggable(true);
        markerOptions.title("点名位置");
        aMap.addMarker(markerOptions).setObject(MapStudentActivity.this);
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
        if (sensor == null)
        {
            sensor.unRegisterSensorListener();
        }
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
//            LatLng target = position.target;
//            marker2.setPosition(target);
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition position)
    {
    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation)
    {
        if (amapLocation != null)
        {
            if (amapLocation.getErrorCode() == 0)
            {
                if (marker2 != null) return;
                myLocationLatLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                addMarker(myLocationLatLng);
                if (locationLatLng != null)
                {
                    addMarker2(locationLatLng);
                }
                // marker2.showInfoWindow();
                // 设置地图显示的位置
                // aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng,
                // 30));

//                locationSource.deactivate();
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
            if (mlocationClient == null)
            {
                mlocationClient = new AMapLocationClient(MapStudentActivity.this);
                mLocationOption = new AMapLocationClientOption();
                // 设置定位监听
                mlocationClient.setLocationListener(MapStudentActivity.this);
                // 设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//                mLocationOption.setInterval(4000);
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
