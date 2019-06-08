package com.chenshuyusc.tjunavigations.homeview

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.MapView
import com.amap.api.maps.model.CustomMapStyleOptions
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Polyline
import com.amap.api.maps.model.PolylineOptions
import com.chenshuyusc.tjunavigations.R
import com.chenshuyusc.tjunavigations.entity.Info
import com.chenshuyusc.tjunavigations.model.Graph
import com.chenshuyusc.tjunavigations.util.ConstValue
import com.chenshuyusc.tjunavigations.util.ConstValue.ARG_KIND
import com.chenshuyusc.tjunavigations.util.EdgeUtils
import com.chenshuyusc.tjunavigations.util.NodeUtils
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream

class TJUMapFragment : Fragment(),
    TJUMapContract.TJUMapView,
    LocationSource,
    AMapLocationListener,
    View.OnClickListener {

    private lateinit var rootView: View
    lateinit var kind: String private set// 记录是四种类型中的哪一种
    private lateinit var mapView: MapView
    private lateinit var aMap: AMap
    private lateinit var basicmap: Button
    private lateinit var rsmap: Button
    private lateinit var nightmap: Button
    private lateinit var navimap: Button
    private lateinit var polyline: Polyline

    private val mapStyleOptions = CustomMapStyleOptions()

    private var mListener: LocationSource.OnLocationChangedListener? = null
    private var mlocationClient: AMapLocationClient? = null
    private lateinit var mLocationOption: AMapLocationClientOption

    private val tjuMapPresenter = TJUMapPresenterImp(this)

    private var hasPoly = false

    companion object {
        fun newInstance(kind: String): TJUMapFragment {
            val args = Bundle()
            args.putString(ARG_KIND, kind)
            val tjuMapFragment = TJUMapFragment()
            tjuMapFragment.arguments = args
            return tjuMapFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kind = arguments!!.getString(ARG_KIND)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_tjumap, container, false)
        initView(savedInstanceState)
        return rootView
    }

    private fun initView(savedInstanceState: Bundle?) {
        mapView = rootView.findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        // ------- 地图 ------- //
        aMap = mapView.map
        // 设置定位监听
        aMap.setLocationSource(this)
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.isMyLocationEnabled = true
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE)

        rootView.apply {
            setMapCustomStyleFile(context)
            basicmap = findViewById<View>(R.id.basicmap) as Button
            basicmap.setOnClickListener(this@TJUMapFragment)
            rsmap = findViewById<View>(R.id.rsmap) as Button
            rsmap.setOnClickListener(this@TJUMapFragment)
            nightmap = findViewById<View>(R.id.nightmap) as Button
            nightmap.setOnClickListener(this@TJUMapFragment)
            navimap = findViewById<View>(R.id.navimap) as Button
            navimap.setOnClickListener(this@TJUMapFragment)
        }

        val marker1 = LatLng(38.997401, 117.311948)
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
        mlocationClient = AMapLocationClient(context)
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
    }

    public fun getNavigation(n1: String, n2: String) {
        if (hasPoly) {
            polyline.remove()
            hasPoly = false
        }
        tjuMapPresenter.getNavigation(n1, n2, kind)
    }

    override fun onSuccess(ns: List<Info>, n1: Int, n2: Int) {
        val latLngs = arrayListOf<LatLng>()
        repeat(ns.size) { i ->
            val tempNode = NodeUtils.getNodeByNumber(ns[i].node)
            tempNode?.let {
                val ll = tempNode.location.split(",")
                latLngs.add(LatLng(ll[1].toDouble(), ll[0].toDouble()))
            }
        }
        val lastNode = NodeUtils.getNodeByNumber(n2)
        lastNode?.let {
            val ll = it.location.split(",")
            latLngs.add(LatLng(ll[1].toDouble(), ll[0].toDouble()))
        }
        polyline = aMap.addPolyline(
            PolylineOptions().addAll(latLngs).width(10f).color(resources.getColor(R.color.blue))
        )
        polyline.isVisible = true
        hasPoly = true
    }

    override fun onNull() {
        Toasty.error(context!!, "抱歉，${kind}方式在北洋园校区内不能达到", Toast.LENGTH_LONG, true).show()
    }
}