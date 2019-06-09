package com.chenshuyusc.tjunavigations.util

import android.app.Activity
import android.util.Log
import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.PolylineOptions
import com.chenshuyusc.tjunavigations.R
import com.chenshuyusc.tjunavigations.entity.Edge
import com.chenshuyusc.tjunavigations.entity.EdgeInfo
import com.chenshuyusc.tjunavigations.util.ConstValue.CAR
import com.chenshuyusc.tjunavigations.util.ConstValue.WALKONLY
import java.io.File

/**
 * 边
 */
object EdgeUtils {

    val edges = arrayListOf<EdgeInfo>()

    fun getEdges(lines:List<String>){
        lines.forEach { line ->
            val strs = line.split(",")
            val node1 = NodeUtils.getNodeByNumber(strs[1].toInt())
            val node2 = NodeUtils.getNodeByNumber(strs[2].toInt())
            if (node1 != null && node2 != null) {
                edges.add(
                    EdgeInfo(strs[0], node1, node2, strs[3], strs[4])
                )
            } else {
                Log.d("ERROR", "node no exist")
            }
        }
    }

//    init {
//        val lines = File("app/src/main/res/raw/edges.csv").readLines(Charsets.UTF_8)
//        lines.forEach { line ->
//            val strs = line.split(",")
//            val node1 = NodeUtils.getNodeByNumber(strs[1].toInt())
//            val node2 = NodeUtils.getNodeByNumber(strs[2].toInt())
//            if (node1 != null && node2 != null) {
//                edges.add(
//                    EdgeInfo(strs[0], node1, node2, strs[3], strs[4])
//                )
//            } else {
//                Log.d("ERROR", "node no exist")
//            }
//        }
//    }

    /**
     * 获得所有连通的边
     * 最后一个参数是指车辆可以通过，还是只有步行能够通过
     * 由于每获得一次距离需要发一次网络请求，所以这个函数单独运行，生成csv文件保存网络请求的信息
     * 可以看 res/raw/marker.pdf 里面标注的边信息，黄色线为只有步行能够通过
     * 每个点的信息在 mytjumap.csv 中
     */
    @JvmStatic
    fun main(args: Array<String>) {
        Edge(1, 12, CAR)
        Edge(1, 2, CAR)
        Edge(2, 74, CAR)
        Edge(12, 6, CAR)
        Edge(6, 25, CAR)
        Edge(26, 7, CAR)
        Edge(25, 26, CAR)
        Edge(26, 79, CAR)
        Edge(79, 31, CAR)
        Edge(31, 8, CAR)
        Edge(8, 13, CAR)
        Edge(13, 14, CAR)
        Edge(25, 4, CAR)
        Edge(7, 28, CAR)
        Edge(4, 28, CAR)
        Edge(74, 4, CAR)
        Edge(74, 25, CAR)
        Edge(28, 29, CAR)
        Edge(29, 19, CAR)
        Edge(19, 22, CAR)
        Edge(22, 18, CAR)
        Edge(7, 11, WALKONLY)
        Edge(11, 29, CAR)
        Edge(31, 32, CAR)
        Edge(32, 19, CAR)
        Edge(22, 16, WALKONLY)
        Edge(15, 16, CAR)
        Edge(15, 8, CAR)
        Edge(15, 32, WALKONLY)
        Edge(16, 17, WALKONLY)
        Edge(17, 18, WALKONLY)
        Edge(17, 13, WALKONLY)
        Edge(33, 104, WALKONLY)
        Edge(37, 67, WALKONLY)
        Edge(53, 47, WALKONLY)
        Edge(100, 51, WALKONLY)
        Edge(56, 50, WALKONLY)
        Edge(2, 75, CAR)
        Edge(2, 75, CAR)
        Edge(74, 77, CAR)
        Edge(75, 77, CAR)
        Edge(77, 35, CAR)
        Edge(35, 4, CAR)
        Edge(35, 34, WALKONLY)
        Edge(28, 34, WALKONLY)
        Edge(35, 33, CAR)
        Edge(62, 76, CAR)
        Edge(76, 24, CAR)
        Edge(24, 33, CAR)
        Edge(33, 36, CAR)
        Edge(36, 37, CAR)
        Edge(37, 21, CAR)
        Edge(21, 23, CAR)
        Edge(23, 20, CAR)
        Edge(20, 45, CAR)
        Edge(45, 39, CAR)
        Edge(39, 40, CAR)
        Edge(34, 36, WALKONLY)
        Edge(29, 37, CAR)
        Edge(22, 23, CAR)
        Edge(18, 45, CAR)
        Edge(76, 77, CAR)
        Edge(75, 62, CAR)
        Edge(62, 69, CAR)
        Edge(69, 71, CAR)
        Edge(71, 68, CAR)
        Edge(52, 94, CAR)
        Edge(94, 93, CAR)
        Edge(71, 72, WALKONLY)
        Edge(72, 70, CAR)
        Edge(72, 101, CAR)
        Edge(68, 101, CAR)
        Edge(101, 105, CAR)
        Edge(105, 100, CAR)
        Edge(72, 59, WALKONLY)
        Edge(104, 60, CAR)
        Edge(60, 59, CAR)
        Edge(59, 57, CAR)
        Edge(57, 100, CAR)
        Edge(60, 67, WALKONLY)
        Edge(59, 54, WALKONLY)
        Edge(57, 56, WALKONLY)
        Edge(67, 54, WALKONLY)
        Edge(54, 56, WALKONLY)
        Edge(54, 53, WALKONLY)
        Edge(52, 85, CAR)
        Edge(85, 51, CAR)
        Edge(51, 50, CAR)
        Edge(50, 49, CAR)
        Edge(49, 81, CAR)
        Edge(81, 80, CAR)
        Edge(80, 41, CAR)
        Edge(41, 42, CAR)
        Edge(41, 40, CAR)
        Edge(41, 48, CAR)
        Edge(48, 47, CAR)
        Edge(47, 44, CAR)
        Edge(44, 49, CAR)
        Edge(47, 46, CAR)
        Edge(46, 23, CAR)
        Edge(46, 45, CAR)
        Edge(69, 70, CAR)
        Edge(70, 104, CAR)
        Edge(94, 84, CAR)
        Edge(84, 82, CAR)
        Edge(82, 87, CAR)
        Edge(87, 98, CAR)
        Edge(98, 95, CAR)
        Edge(95, 96, CAR)
        Edge(96, 81, CAR)
        Edge(85, 84, CAR)
        Edge(51, 82, CAR)
        Edge(50, 87, CAR)
        Edge(49, 98, CAR)
        Edge(93, 86, CAR)
        Edge(86, 83, CAR)
        Edge(83, 88, CAR)
        Edge(88, 89, CAR)
        Edge(107, 90, CAR)
        Edge(90, 89, CAR)
        Edge(89, 98, CAR)
        Edge(90, 97, WALKONLY)
        Edge(97, 95, WALKONLY)
        Edge(84, 86, CAR)
        Edge(82, 83, CAR)
        Edge(87, 88, CAR)
        return
    }
}
