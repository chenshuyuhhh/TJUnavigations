package com.chenshuyusc.tjunavigations.homeview

import com.chenshuyusc.tjunavigations.entity.Info

class TJUMapContract {

    interface TJUMapView {
        fun onSuccess(ns: List<Info>,n1:Int,n2:Int)
        fun onNull()
    }

    interface TJUMapPresenter {
        fun getNavigation(n1: String, n2: String, kind: String)
    }
}