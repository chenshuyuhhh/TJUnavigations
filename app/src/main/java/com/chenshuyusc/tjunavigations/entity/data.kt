package com.chenshuyusc.tjunavigations.entity

/**
 * 数据结构
 */


/**
 * 边的详细信息类
 */
data class EdgeInfo(
    val p: String,
    val node1: Node,
    val node2: Node,
    val distance: String,
    val duration: String
)

/**
 * 邻接矩阵
 */
data class AdjacencyNode(
    val node: Node,
    val map: HashMap<Node, Double>  // 存有这个结点所有相邻结点的编号和对应的路径长度
)

/**
 * 简化信息的邻接矩阵,不带权值
 */
data class SimpleAdj(
    val node: Int,
    val nodes: Set<Int>
)

/**
 * 存放上一个结点
 * 和权重
 */
data class Info(
    val node: Int,
    val w: Double,
    val t:Double
)

/**
 * 存放Json解析对应的数据类
 */

//---- getDistance 网络请求 ----//
/**
 * 每个点的名称、对应的编号、坐标
 */
data class Node(
    val number: Int,
    val location: String,
    val name: String
)

data class Distance(
    val count: String,
    val info: String,
    val infocode: String,
    val route: Route,
    val status: String
)

data class Route(
    val destination: String,
    val origin: String,
    val paths: List<Path>
)

data class Path(
    val distance: String,
    val duration: String,
    val steps: Any
)