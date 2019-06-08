package com.chenshuyusc.tjunavigations.util


import com.chenshuyusc.tjunavigations.entity.Info
import com.chenshuyusc.tjunavigations.entity.Node
import com.chenshuyusc.tjunavigations.model.Graph
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

object SPFA {

    /**
     * 根据起点和终点获得最短路径
     * 1.存入图。可以使用链式前向星或者vocter。
     * 2.开一个队列，先将开始的节点放入。
     * 3.每次从队列中取出一个节点X，遍历与X相通的Y节点，查询比对  Y的长度 和 X的长度+ X与Y的长度
     *   如果X的长度+ X与Y的长度 > Y的长度,说明需要更新操作。
     *   1）存入最短路。
     *   2）由于改变了原有的长度，所以需要往后更新，与这个节点相连的最短路。(即：判断下是否在队列，在就不用重复，不在就加入队列，等待更新)。
     *   3）在这期间可以记录这个节点的进队次数，判断是否存在负环。
     * 4.直到队空。
     */
    @JvmStatic
    fun main(args: Array<String>) {
        Graph.get()
        getPaths(Graph.adjsCar, 1, 62)
    }

    /**
     * 邻接矩阵
     * n1：起始点
     * n2：结束点
     */
    fun getPaths(map: HashMap<Node, HashMap<Node, String>>, n1: Int, n2: Int) {

        // 权重记录表
        val table = arrayOfNulls<Info>(108)

        // 队列
        val queue = LinkedBlockingQueue<Node>()

        // 初始化队列和列表,加入源结点
        queue.add(NodeUtils.getNodeByNumber(n1))
        table[n1] = Info(n1, 0.0, 0.0)

        // 如果队列不为空，则循环
        while (queue.isNotEmpty()) {

            // 先取出队头元素并删除
            val head = queue.poll()

            // 如果队头元素不为空则继续执行
            head?.let { head ->

                // 队头元素的权重
                val headW = table[head.number]!!.w
                val headT = table[head.number]!!.t

                // 先取出这个队头元素的邻接 map
                val headMap = map[head]

                // 邻接 map 不为空则继续执行
                headMap?.forEach { temp ->
                    // 和 head 结点之间的权重
                    val dd = temp.value.split(",")
                    val dw = dd[0].toDouble()
                    val dt = dd[1].toDouble()

                    // 如果表中的该元素是null，说明是 ∞
                    if (table[temp.key.number] == null) {
                        // 更新表
                        table[temp.key.number] = Info(head.number, headW + dw, headT + dt)
                        // 将被松弛的元素加入队列中
                        queue.add(NodeUtils.getNodeByNumber(temp.key.number))
                    } else {
                        table[temp.key.number]?.let { info ->
                            // 本来的权重
                            val weight0 = info.w
                            if (weight0 > dw + headW) {
                                table[temp.key.number] = Info(head.number, dw + headW, dt + headT)
                                queue.add(NodeUtils.getNodeByNumber(temp.key.number))
                            }
                        }
                    }
                }
            }
        }
    }
}