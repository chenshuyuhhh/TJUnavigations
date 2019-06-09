package com.chenshuyusc.tjunavigations.homeview

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class TJUMapPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragmentsOfHome = mutableListOf<Fragment>()
    private val fragmentTitles = mutableListOf<String>()

    override fun getCount(): Int = fragmentsOfHome.size

    override fun getItem(position: Int): Fragment = fragmentsOfHome[position]

    override fun getPageTitle(position: Int): CharSequence? = fragmentTitles[position]

    // 这样实例化 pagerAdapter 的时候不用传 fragment 和 title ，并且可以继续添加，如果以后需要删除，可以再添加一个删除方法
    fun addFragment(fragment: Fragment, title: String) {
        fragmentsOfHome.add(fragment)
        fragmentTitles.add(title)
        notifyDataSetChanged()
    }

    /**
     * 每个碎片都开始规划导航
     */
    fun getNavigation(n1: String, n2: String) {
        fragmentsOfHome.forEach { fragment ->
            fragment as TJUMapFragment
            fragment.getNavigation(n1, n2)
        }
    }
//    fun updateFragment(position: Int) {
//        try {
//            (fragmentsOfHome[position] as TJUMapPageAdapter)
//        } catch (e: IndexOutOfBoundsException) {
//            e.printStackTrace()
//        }
//    }
}
