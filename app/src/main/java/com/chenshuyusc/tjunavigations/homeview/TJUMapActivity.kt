package com.chenshuyusc.tjunavigations.homeview

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import com.amap.api.location.AMapLocation
import com.amap.api.maps.model.CustomMapStyleOptions
import com.chenshuyusc.tjunavigations.R
import java.io.IOException
import java.io.InputStream
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.*


class TJUMapActivity : Activity(), View.OnClickListener, LocationSource,AMapLocationListener {

    private lateinit var mapView: MapView
    private lateinit var aMap: AMap
    private lateinit var basicmap: Button
    private lateinit var rsmap: Button
    private lateinit var nightmap: Button
    private lateinit var navimap: Button

    private lateinit var mStyleCheckbox: CheckBox


    private val mapStyleOptions = CustomMapStyleOptions()

    private var mListener: LocationSource.OnLocationChangedListener? = null
    private var mlocationClient: AMapLocationClient? = null
    private lateinit var mLocationOption: AMapLocationClientOption

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tjumap)
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其
        // 他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        //  MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
//        val aOptions = AMapOptions()
//        aOptions.camera(CameraPosition(centerPoint, 10f, 0f, 0f))
        mapView = findViewById<View>(R.id.map) as MapView
     //   mapView = MapView(this,aOptions)
        mapView.onCreate(savedInstanceState)// 此方法必须重写
//        mParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT)
//        mContainerLayout.addView(mapView, mParams)
        init()

    }

    /**
     * 初始化AMap对象
     */
    private fun init() {
        aMap = mapView.map
        // 设置定位监听
        aMap.setLocationSource(this)
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.isMyLocationEnabled = true
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE)



        setMapCustomStyleFile(this)
        basicmap = findViewById<View>(R.id.basicmap) as Button
        basicmap.setOnClickListener(this)
        rsmap = findViewById<View>(R.id.rsmap) as Button
        rsmap.setOnClickListener(this)
        nightmap = findViewById<View>(R.id.nightmap) as Button
        nightmap.setOnClickListener(this)
        navimap = findViewById<View>(R.id.navimap) as Button
        navimap.setOnClickListener(this)

        val marker1 = LatLng(38.997401,117.311948)
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1))
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f))
    }

//    /**
//    * 监听amap地图加载成功事件回调
//    */
//    @Override
//    public fun onMapLoaded() {
//        val marker1 = LatLng(38.997401,117.311948)
//        //设置中心点和缩放比例
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1))
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(14f))
//    }

    private fun setMapCustomStyleFile(context: Context) {
        val styleName = "style_new.data"
        var inputStream: InputStream? = null
        try {
            inputStream = context.assets.open(styleName)
            val b = ByteArray(inputStream!!.available())
            inputStream.read(b)

            // 设置自定义样式
            mapStyleOptions.styleData = b

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }

    override fun deactivate() {
//        mListener = null
//        if (mlocationClient != null) {
//            mlocationClient.stopLocation()
//            mlocationClient.onDestroy()
//        }
        mlocationClient = null
    }

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        mListener = listener
        //初始化定位
        mlocationClient = AMapLocationClient(this)
        //初始化定位参数
        mLocationOption = AMapLocationClientOption()
        //设置定位回调监听
//        mlocationClient.setLocationListener(this)
//        //设置为高精度定位模式
//        mLocationOption.locationMode = AMapLocationMode.Hight_Accuracy
//        //设置定位参数
//        mlocationClient.setLocationOption(mLocationOption)
//        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//        // 在定位结束后，在合适的生命周期调用onDestroy()方法
//        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//        mlocationClient.startLocation()//启动定位

    }

    override fun onLocationChanged(p0: AMapLocation?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    /**
     * 方法必须重写
     */
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    /**
     * 方法必须重写
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    /**
     * 方法必须重写
     */
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.basicmap -> aMap.mapType = AMap.MAP_TYPE_NORMAL// 矢量地图模式
            R.id.rsmap -> aMap.mapType = AMap.MAP_TYPE_SATELLITE// 卫星地图模式
            R.id.nightmap -> aMap.mapType = AMap.MAP_TYPE_NIGHT//夜景地图模式
            R.id.navimap -> aMap.mapType = AMap.MAP_TYPE_NAVI//导航地图模式
        }

        mStyleCheckbox.isChecked = false
    }

}