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
//    @JvmStatic
//    fun main(args: Array<String>) {
//        Graph.get()
//        val infos = getPaths(Graph.adjsWalk, 93, 14)
//        println(infos.size)
//    }

    /**
     * map 为一个邻接矩阵的存储，key存的是结点，value存的是和这个结点相邻的所有结点以及路径所购成的小 map
     */

    /**
     * 邻接矩阵
     * n1：起始点
     * n2：结束点
     */
    fun getPaths(map: HashMap<Node, HashMap<Node, String>>, n1: Int, n2: Int): List<Info> {

        /**
         * map 为一个邻接矩阵的存储，key存的是结点，value存的是和这个结点相邻的所有结点以及路径所购成的小 map
         */
        // 权重记录表
        val table = arrayOfNulls<Info>(108)

        // 队列
        val queue = LinkedBlockingQueue<Node>()

        // 初始化队列和列表,加入源结点
        queue.add(NodeUtils.getNodeByNumber(n1))
        table[n1] = Info(n1, 0.0, 0.0)

        // 找出其他结点到 n1 的最短路径
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
                        table[temp.key.number] = Info(head.number, headW + dw, dt)
                        // 将被松弛的元素加入队列中
                        queue.add(NodeUtils.getNodeByNumber(temp.key.number))
                    } else {
                        table[temp.key.number]?.let { info ->
                            // 本来的权重
                            val weight0 = info.w
                            if (weight0 > dw + headW) {
                                table[temp.key.number] = Info(head.number, dw + headW, headT)
                                queue.add(NodeUtils.getNodeByNumber(temp.key.number))
                            }
                        }
                    }
                }
            }
        }

        val infos = mutableListOf<Info>()
        table.getInfos(n1, n2, infos)
        infos.reverse()
        return infos
    }

    // 回溯
    private fun Array<Info?>.getInfos(n1: Int, n2: Int, infos: MutableList<Info>) {

        val info2 = this[n2]
        if (info2 == null) {
            return
        } else {
            infos.add(info2)
            if (info2.node == n1) {
                // 如果上一个点的上一个地点是起始地点
                // 将第二个点加入后就可以返回值
                return
            } else {
                getInfos(n1, info2.node, infos)
            }
        }
    }
}