package com.chenshuyusc.tjunavigations.entity

import android.util.Log
import com.chenshuyusc.tjunavigations.service.RetrofitFactory
import com.chenshuyusc.tjunavigations.service.awaitAndHandle
import com.chenshuyusc.tjunavigations.util.ConstValue
import com.chenshuyusc.tjunavigations.util.NodeUtils.getNodeByNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.nio.charset.Charset

/**
 * 边
 * 有两点和两点间的距离
 */
class Edge(private val n1: Int, private val n2: Int, private val p: String) {
    val node1: Node = getNodeByNumber(n1)!!
    val node2: Node = getNodeByNumber(n2)!!
    var distance: Double = 0.0
        private set
    var time: Double = 0.0
        private set

    init {
        getDistance()
    }

    private fun getDistance() {
        if (node1 != null && node2 != null) {
            GlobalScope.launch(Dispatchers.Unconfined) {
                RetrofitFactory.api.getDistance(ConstValue.KEY, node1.location, node2.location).awaitAndHandle {
                    File("app/src/main/res/raw/edges.csv").writeText(
                        "$p,${node1.number},${node2.number},ERROR",
                        Charsets.UTF_8
                    )
                }?.let {
                    distance = it.route.paths[0].distance.toDouble()
                    time = it.route.paths[0].duration.toDouble()
                    println("$p,${node1.number},${node2.number},$distance")
                    File("app/src/main/res/raw/edges.csv").appendText(
                        "$p,${node1.number},${node2.number},$distance,$time\n",
                        Charsets.UTF_8
                    )
                }
            }
        }
    }
}