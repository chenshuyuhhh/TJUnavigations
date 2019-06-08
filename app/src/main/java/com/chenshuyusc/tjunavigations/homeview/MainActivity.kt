package com.chenshuyusc.tjunavigations.homeview

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.chenshuyusc.tjunavigations.R
import java.io.*
import java.util.ArrayList

class MainActivity : Activity() {
    private lateinit var mapView: MapView
    private lateinit var aMap: AMap

    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView = MapView(this)
        setContentView(mapView)

        mapView.onCreate(savedInstanceState)// 此方法必须重写

        //初始化AMap对象
        aMap = mapView.map

        //添加一个Marker用来展示海量点点击效果
        marker =
            aMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))

        val bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.mark_dot)

        val overlayOptions = MultiPointOverlayOptions()
        overlayOptions.icon(bitmapDescriptor)
        overlayOptions.anchor(0.5f, 0.5f)

        val multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions)
        aMap.setOnMultiPointClickListener { pointItem ->
            android.util.Log.i("amap ", "onPointClick")

            if (marker!!.isRemoved) {
                //调用amap clear之后会移除marker，重新添加一个
                marker = aMap.addMarker(
                    MarkerOptions().icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_RED
                        )
                    )
                )
            }
            //添加一个Marker用来展示海量点点击效果
            marker!!.position = pointItem.latLng
            marker!!.setToTop()
            false
        }

        aMap.moveCamera(CameraUpdateFactory.zoomTo(3f))

        showProgressDialog()
        //开启子线程读取文件
        Thread(Runnable {
            val list = ArrayList<MultiPointItem>()
            val outputStream: FileOutputStream? = null
            var inputStream: InputStream? = null
            val filePath: String? = null
            try {
                inputStream = this@MainActivity.resources.openRawResource(R.raw.mytjumap)
                val bufferedReader = inputStream.bufferedReader(Charsets.UTF_8)
                var lines = bufferedReader.readLines()
                lines.forEach { line ->
                    if (line != lines[0]) {
                        if (isDestroy) {
                            //已经销毁地图了，退出循环
                            return@Runnable
                        }

                        val str = line.split(",")
                        val latLng = LatLng(str[2].toDouble(), str[1].toDouble(), false)//保证经纬度没有问题的时候可以填false

                        val multiPointItem = MultiPointItem(latLng)
                        list.add(multiPointItem)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    inputStream?.close()

                    outputStream?.close()

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            if (multiPointOverlay != null) {
                multiPointOverlay.setItems(list)
                multiPointOverlay.setEnable(true)
            }

            dissmissProgressDialog()

            //
        }).start()

    }


    private var isDestroy = false

    /**
     * 初始化AMap对象
     */
    private fun init() {
        aMap = mapView.map
    }

    private lateinit var progDialog: ProgressDialog // 添加海量点时
    /**
     * 显示进度框
     */
    private fun showProgressDialog() {
        progDialog = ProgressDialog(this)
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progDialog.isIndeterminate = false
        progDialog.setCancelable(true)
        progDialog.setMessage("正在解析添加海量点中，请稍后...")
        progDialog.show()
    }

    /**
     * 隐藏进度框
     */
    private fun dissmissProgressDialog() {
        progDialog.dismiss()
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
        dissmissProgressDialog()
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
        isDestroy = true
    }
}