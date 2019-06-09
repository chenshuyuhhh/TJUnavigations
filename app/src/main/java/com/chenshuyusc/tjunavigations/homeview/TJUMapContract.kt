package com.chenshuyusc.tjunavigations.homeview

import com.chenshuyusc.tjunavigations.entity.Info

/***
 * 使用 contract 来统一管理 view 层和 presenter 层的接口
 */
class TJUMapContract {
    interface TJUMapView {
        fun onSuccess(ns: List<Info>,n1:Int,n2:Int)
        fun onNull()
    }

    interface TJUMapPresenter {
        fun getNavigation(n1: String, n2: String, kind: String)
    }
}