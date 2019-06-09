package com.chenshuyusc.tjunavigations.homeview

import com.chenshuyusc.tjunavigations.entity.Info
import com.chenshuyusc.tjunavigations.model.Graph
import com.chenshuyusc.tjunavigations.util.ConstValue
import com.chenshuyusc.tjunavigations.util.NodeUtils
import com.chenshuyusc.tjunavigations.util.SPFA

class TJUMapPresenterImp(private val tjuMapView: TJUMapContract.TJUMapView) : TJUMapContract.TJUMapPresenter {

    override fun getNavigation(n1: String, n2: String, kind: String) {
        val n1temp = NodeUtils.getNumberByName(n1)
        val n2temp = NodeUtils.getNumberByName(n2)
        // 先获得无向图的邻接图
        Graph.get()
        val infos = mutableListOf<Info>()
        when (kind) {
            ConstValue.WALK -> {
                infos.clear()
                infos.addAll(SPFA.getPaths(Graph.adjsWalk, n1temp, n2temp))
            }
            ConstValue.BIKE -> {
                infos.clear()
                infos.addAll(SPFA.getPaths(Graph.adjsWalk, n1temp, n2temp))
            }
            ConstValue.DRIVER -> {
                infos.clear()
                infos.addAll(SPFA.getPaths(Graph.adjsCar, n1temp, n2temp))
            }
        }
        if (infos.isEmpty()) {
            tjuMapView.onNull()
        } else {
            tjuMapView.onSuccess(infos, n1temp, n2temp)
        }
    }
}