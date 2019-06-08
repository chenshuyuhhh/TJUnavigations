package com.chenshuyusc.tjunavigations.model

import com.chenshuyusc.tjunavigations.entity.EdgeInfo
import com.chenshuyusc.tjunavigations.entity.Node
import com.chenshuyusc.tjunavigations.util.ConstValue.CAR
import com.chenshuyusc.tjunavigations.util.EdgeUtils
import com.chenshuyusc.tjunavigations.util.EdgeUtils.edges

/**
 * 根据天大校区的情况生成图
 */
object Graph {

    /**
     * 邻接矩阵图
     */
    // 步行图
    val adjsWalk = HashMap<Node, HashMap<Node, String>>()

    // 驾车图
    val adjsCar = HashMap<Node, HashMap<Node, String>>()

    /**
     * 获得步行的邻接矩阵图和驾车的邻接图
     */
    fun get(): Boolean {
        repeat(edges.size) { i ->
            // 不论什么标志位，步行一定可以
            val edge = edges[i]
            adjsWalk.addEdge(edge)

            // 判断有车的标志位，才加入车中
            if (edge.p == CAR) {
                adjsCar.addEdge(edge)
            }
        }
        return true
    }

    /**
     * HashMap<Node, HashMap<Node, String>>的扩展函数，根据边的信息构建邻接矩阵
     */
    fun HashMap<Node, HashMap<Node, String>>.addEdge(edge: EdgeInfo) {
        val node1 = edge.node1
        val node2 = edge.node2
        val dd = "${edge.distance},${edge.duration}"

        // 结点未被存过
        if (this[node1] == null) {
            val temp = HashMap<Node, String>()
            temp[node2] = dd
            this[node1] = temp
        }

        if (this[node2] == null) {
            val temp = HashMap<Node, String>()
            temp[node1] = dd
            this[node2] = temp
        }

        // 如果这个结点1已经被存过了
        this[node1]?.let {
            it.put(node2, dd)
        }

        // 如果这个结点2已经被存过了
        this[node2]?.let {
            it.put(node1, dd)
        }
    }
}