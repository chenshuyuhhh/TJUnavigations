package com.chenshuyusc.tjunavigations.home

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.amap.api.maps.MapView
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.LatLngBounds
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.amap.api.location.AMapLocationClientOption
import com.chenshuyusc.tjunavigations.R

/**
 * 继承了Activity，实现Android6.0的运行时权限检测
 * 需要进行运行时权限检测的Activity可以继承这个类
 *
 * @创建时间：2019年6月4日
 * @项目名称： tjunavigations
 * @author chenshuyu
 * @文件名称：PermissionsChecker.java
 * @类型名称：PermissionsChecker
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mMapView: MapView
    private lateinit var aMap: AMap

    // 西南坐标
    private val southwestLatLng = LatLng(39.674949, 115.932873)
    // 东北坐标
    private val northeastLatLng = LatLng(40.159453, 116.767834)

    //声明AMapLocationClient类对象
    private lateinit var locationClient: AMapLocationClient
    //声明定位回调监听器
    private val locationListener: AMapLocationListener = TjuLocationListener()
    //AMapLocationClientOption对象用来设置发起定位的模式和相关参数。
    private val option: AMapLocationClientOption = AMapLocationClientOption()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //获取地图控件引用
        mMapView = findViewById(R.id.map)
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState)
        //初始化地图控制器对象
        init()

        aMap.addMarker(MarkerOptions().position(southwestLatLng))
        aMap.addMarker(MarkerOptions().position(northeastLatLng))

        aMap.moveCamera(CameraUpdateFactory.zoomTo(8f))

        // 请在主线程中声明AMapLocationClient类对象，需要传Context类型的参数。推荐用getApplicationContext()方法获取全进程有效的context。
        //初始化定位
        locationClient = AMapLocationClient(applicationContext)
        //设置定位回调监听
        locationClient.setLocationListener(locationListener)
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         * 在此应用场景中选择出行
         */
        option.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.Transport
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        option.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        option.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        if (null != locationClient) {
            locationClient.setLocationOption(option)
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            locationClient.stopLocation()
            locationClient.startLocation()
        }
    }

    /**
     * 初始化AMap对象
     */
    private fun init() {
        if (aMap == null) {
            aMap = mMapView.map
        }
    }

    /**
     * 设置限制区域
     * @param view
     */
    fun set(view: View) {
        val latLngBounds = LatLngBounds(southwestLatLng, northeastLatLng)
        aMap.setMapStatusLimits(latLngBounds)

    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState)
    }
}
