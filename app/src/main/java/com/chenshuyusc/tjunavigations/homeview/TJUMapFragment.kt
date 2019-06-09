package com.chenshuyusc.tjunavigations.homeview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.chenshuyusc.tjunavigations.R
import com.chenshuyusc.tjunavigations.base.TjuLocationListener
import com.chenshuyusc.tjunavigations.entity.Info
import com.chenshuyusc.tjunavigations.entity.Node
import com.chenshuyusc.tjunavigations.util.ConstValue.ARG_KIND
import com.chenshuyusc.tjunavigations.util.ConstValue.CLICK_BEGIN
import com.chenshuyusc.tjunavigations.util.ConstValue.CLICK_END
import com.chenshuyusc.tjunavigations.util.ConstValue.NORMAL
import com.chenshuyusc.tjunavigations.util.NodeUtils
import es.dmoral.toasty.Toasty
import java.io.IOException
import java.io.InputStream

class TJUMapFragment : Fragment(),
    TJUMapContract.TJUMapView,
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

    private lateinit var text: TextView
    private lateinit var cardView: CardView

    private val mapStyleOptions = CustomMapStyleOptions()

    //声明AMapLocationClient类对象
    private var locationClient: AMapLocationClient? = null
    //声明定位回调监听器
    private lateinit var locationListener: AMapLocationListener
    //AMapLocationClientOption对象用来设置发起定位的模式和相关参数。
    private var option: AMapLocationClientOption? = AMapLocationClientOption()

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

        locationListener = TjuLocationListener(context!!,aMap)
        // 设置定位监听
        // 请在主线程中声明AMapLocationClient类对象，需要传Context类型的参数。推荐用getApplicationContext()方法获取全进程有效的context。
        //初始化定位
        locationClient = AMapLocationClient(activity!!.applicationContext)
        //设置定位回调监听
        locationClient?.setLocationListener(locationListener)
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         * 在此应用场景中选择出行
         */
        option?.apply {
            httpTimeOut = 30000
            isMockEnable = true
            locationPurpose = AMapLocationClientOption.AMapLocationPurpose.Transport
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            isOnceLocation = true
        }
        locationClient?.apply {
            setLocationOption(option)
            stopLocation()
            startLocation()
        }

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

            text = findViewById(R.id.gps_tv_content)
            cardView = findViewById(R.id.gps_cv)
            cardView.visibility = View.INVISIBLE
        }

        val marker1 = LatLng(38.997401, 117.311948)
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1))
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f))
    }


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

//    override fun deactivate() {
//        mListener = null
//        mlocationClient?.let {
//            it.stopLocation()
//            it.onDestroy()
//        }
//        mlocationClient = null
//    }
//
//    override fun activate(listener: LocationSource.OnLocationChangedListener) {
//        mListener = listener
//        if (mlocationClient == null) {
//            //初始化定位
//            mlocationClient = AMapLocationClient(context)
//            //初始化定位参数
//            mLocationOption = AMapLocationClientOption()
//            //设置定位回调监听
//            mlocationClient?.let {
//                it.setLocationListener(this)
//                mLocationOption?.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
//                //设置定位参数
//                it.setLocationOption(mLocationOption)
//                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//                // 在定位结束后，在合适的生命周期调用onDestroy()方法
//                // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//                it.startLocation()//启动定位
//            }
//        }
//
//    }
//
//    override fun onLocationChanged(amapLocation: AMapLocation?) {
//        if (mListener != null && amapLocation != null) {
//            if (amapLocation.errorCode == 0) {
//                mListener?.onLocationChanged(amapLocation)// 显示系统小蓝点
//            } else {
//                Toasty.error(context!!, "定位失败了T_T", Toast.LENGTH_LONG, true).show()
//            }
//        }
//    }

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
        destroyLocation()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.basicmap -> aMap.mapType = AMap.MAP_TYPE_NORMAL// 矢量地图模式
            R.id.rsmap -> aMap.mapType = AMap.MAP_TYPE_SATELLITE// 卫星地图模式
            R.id.nightmap -> aMap.mapType = AMap.MAP_TYPE_NIGHT//夜景地图模式
            R.id.navimap -> aMap.mapType = AMap.MAP_TYPE_NAVI//导航地图模式
        }
    }

    fun getNavigation(n1: String, n2: String) {
        if (hasPoly) {
            polyline.remove()
            hasPoly = false
            val marker = aMap.mapScreenMarkers
            marker.forEach {
                it.remove()
            }
            marker.clear()
            cardView.visibility = View.INVISIBLE
        }
        tjuMapPresenter.getNavigation(n1, n2, kind)
    }

    override fun onSuccess(ns: List<Info>, n1: Int, n2: Int) {
        val str = StringBuffer("")
        var time = 0.0
        val node1 = NodeUtils.getNodeByNumber(n1)
        // 绘制起点
        node1?.drawMarker(CLICK_BEGIN)
        str.append("${node1?.name}-- ")

        // 绘制线
        val latLngs = arrayListOf<LatLng>()
        repeat(ns.size) { i ->
            val nstemp = ns[i]
            val tempNode = NodeUtils.getNodeByNumber(nstemp.node)
            tempNode?.let {
                val ll = tempNode.location.split(",")
                latLngs.add(LatLng(ll[1].toDouble(), ll[0].toDouble()))
                if (i == 0) {
                    str.append("${nstemp.w} m --> ")
                } else {
                    str.append("${tempNode.name} --> ${nstemp.w - ns[i - 1].w} m -- ")
                    it.drawMarker(NORMAL)
                }
            }
            time += nstemp.t
        }
        val lastNode = NodeUtils.getNodeByNumber(n2)
        lastNode?.let {
            val ll = it.location.split(",")
            val latlng = LatLng(ll[1].toDouble(), ll[0].toDouble())
            latLngs.add(latlng)

            str.append("${it.name}\n总路程：${ns.last().w} m\n总时长：$time s")

            // 绘制终点
            it.drawMarker(CLICK_END)
        }
        polyline = aMap.addPolyline(
            PolylineOptions().addAll(latLngs).width(10f).color(resources.getColor(R.color.blue))
        )
        polyline.isVisible = true
        hasPoly = true

        text.text = str.toString()
        cardView.visibility = View.VISIBLE
        cardView.bringToFront()
    }

    override fun onNull() {
        cardView.visibility = View.INVISIBLE
        Toasty.error(context!!, "抱歉，${kind}方式在北洋园校区内不能达到", Toast.LENGTH_LONG, true).show()
    }

    private fun Node.drawMarker(p: String) {
        val ll = this.location.split(",")
        val latlng = LatLng(ll[1].toDouble(), ll[0].toDouble())
        // 绘制终点
        val markerOption = MarkerOptions()
        markerOption.position(latlng)
        markerOption.title(this.name).snippet(this.location)

        markerOption.draggable(true)//设置Marker可拖动
        when (p) {
            CLICK_END -> markerOption.icon(
                BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory
                        .decodeResource(resources, R.drawable.ic_end).changeSize()
                )
            )
            CLICK_BEGIN -> markerOption.icon(
                BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(resources, R.drawable.ic_begin).changeSize()
                )
            )
            NORMAL -> markerOption.icon(
                BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(resources, R.drawable.ic_normal1).changeSize()
                )
            )
        }
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.isFlat = true//设置marker平贴地图效果
        aMap.addMarker(markerOption)
    }

    private fun Bitmap.changeSize(): Bitmap {
        //设置想要的大小
        val newWidth = 100
        val newHeight = 100

        //计算压缩的比率
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height

        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     */
    private fun destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient?.onDestroy()
            locationClient = null
            option = null
        }
    }

}