package com.chenshuyusc.tjunavigations.util

import com.chenshuyusc.tjunavigations.entity.Node
import java.io.File

/**
 * 初始化地图中的所有结点
 */
object NodeUtils {

    private val nodeSet = mutableSetOf<Node>()


    /**
     * 获得所有结点的信息
     */
    init {
        val lines = File("app/src/main/res/raw/mytjumap.csv").readLines(Charsets.UTF_8)
        repeat(lines.size) { i ->
            if (i > 0) {
                val strs = lines[i].split(",")
                nodeSet.add(Node(strs[0].toInt(), strs[1] + "," + strs[2], strs[3]))
            }
        }
        println(nodeSet.size)
    }

    /**
     * 根据地名获得地名对应的编号
     * 如果地名不存在，得到的编号为空
     */
    public fun getNodeByName(name: String): Node? {
        nodeSet.forEach { node ->
            if (node.name == name) return node
        }
        return null
    }

    /**
     * 根据编号获得对应的地名
     * 利用 kotlin 语言提供的 map 的 foreach 扩展函数来遍历map
     */
    public fun getNodeByNumber(number: Int): Node? {
        nodeSet.forEach { node ->
            if (node.number == number)
                return node
        }
        return null
    }
}