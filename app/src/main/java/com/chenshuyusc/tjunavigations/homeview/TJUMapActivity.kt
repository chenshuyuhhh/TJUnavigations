package com.chenshuyusc.tjunavigations.homeview

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import cn.edu.twt.retrox.recyclerviewdsl.withItems
import com.chenshuyusc.tjunavigations.R
import com.chenshuyusc.tjunavigations.base.NoScrollViewPager
import com.chenshuyusc.tjunavigations.model.Graph
import com.chenshuyusc.tjunavigations.util.ConstValue
import com.chenshuyusc.tjunavigations.util.EdgeUtils
import com.chenshuyusc.tjunavigations.util.NodeUtils
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class TJUMapActivity : AppCompatActivity() {

    private lateinit var begin: EditText
    private lateinit var end: EditText
    private lateinit var navigation: ImageView
    private lateinit var swap: ImageView
    private lateinit var recyclerView: RecyclerView
    private var linearLayoutManager = LinearLayoutManager(this)
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: NoScrollViewPager
    private var viewPagerAdapter: TJUMapPageAdapter = TJUMapPageAdapter(supportFragmentManager)
    private val list = arrayListOf(ConstValue.WALK, ConstValue.BIKE, ConstValue.DRIVER)

    private var clickP: String = ConstValue.CLICK_BEGIN

    private var isReady = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tjumap)
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其
        // 他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        //  MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
//        val aOptions = AMapOptions()
//        aOptions.camera(CameraPosition(centerPoint, 10f, 0f, 0f))
        bindID()
        //   mapView = MapView(this,aOptions)
//        mParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT)
//        mContainerLayout.addView(mapView, mParams)
        initView()

        end.setOnClickListener {
            clickP = ConstValue.CLICK_END
            showSearch()
        }

        begin.setOnClickListener {
            clickP = ConstValue.CLICK_BEGIN
            showSearch()
        }

        navigation.setOnClickListener {
            navigationTry()
        }

        // 交换起始地点
        swap.setOnClickListener {
            val beginText = begin.text.toString()
            val endText = end.text.toString()
            begin.setText(endText)
            end.setText(beginText)
        }
        getModelInit()
    }

    private fun bindID() {
        begin = findViewById(R.id.begin_place)
        end = findViewById(R.id.end_place)
        swap = findViewById(R.id.navigation_swap)

        viewPager = findViewById<NoScrollViewPager>(R.id.navigation_vp)
        tabLayout = findViewById(R.id.navigation_tl_tabs)

        navigation = findViewById(R.id.navigation_iv)
        recyclerView = findViewById(R.id.navigation_rv)
        recyclerView.layoutManager = linearLayoutManager

        // 刚开始是看到地图，要点击之后才能看到搜索列表
        showMap()
    }

    private fun initView() {

        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 2
        list.forEach {
            viewPagerAdapter.addFragment(TJUMapFragment.newInstance(it), it)
            tabLayout.addTab(tabLayout.newTab().setText(it)) // 设置 tab 的标题
        }
        tabLayout.setupWithViewPager(viewPager) // 将 tab 和 viewpager 关联

        getSearchList()
    }

    private fun showSearch() {
        recyclerView.visibility = View.VISIBLE
        navigation.visibility = View.VISIBLE
        viewPager.visibility = View.INVISIBLE
        tabLayout.visibility = View.INVISIBLE
    }

    private fun showMap() {
        recyclerView.visibility = View.INVISIBLE
        navigation.visibility = View.INVISIBLE
        viewPager.visibility = View.VISIBLE
        tabLayout.visibility = View.VISIBLE
    }

    private fun getSearchList() {
        // 开一个协程来完成 rv
        GlobalScope.launch {
            recyclerView.withItems {
                val inputStream = this@TJUMapActivity.resources.openRawResource(R.raw.mytjumap)
                val bufferedReader = inputStream.bufferedReader(Charsets.UTF_8)
                val lines = bufferedReader.readLines()
                lines.forEach {
                    if (it != lines[0]) {
                        val strs = it.split(",")
                        addPlace(strs[3], "${strs[1]},${strs[2]}") { name ->
                            // 点击之后将名字填充到相应的 ET
                            when (clickP) {
                                ConstValue.CLICK_BEGIN -> {
                                    begin.setText(name)
                                }
                                ConstValue.CLICK_END -> {
                                    end.setText(name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getModelInit() {
        navigation.visibility = View.INVISIBLE
        GlobalScope.launch {
            isReady = GlobalScope.async {
                val inputStream0 = this@TJUMapActivity.resources.openRawResource(R.raw.mytjumap)
                val bufferedReader0 = inputStream0.bufferedReader(Charsets.UTF_8)
                val lines0 = bufferedReader0.readLines()
                NodeUtils.getNodes(lines0)
                val inputStream1 = this@TJUMapActivity.resources.openRawResource(R.raw.edges)
                val bufferedReader1 = inputStream1.bufferedReader(Charsets.UTF_8)
                val lines1 = bufferedReader1.readLines()
                EdgeUtils.getEdges(lines1)
                Graph.get()
            }.await()
            navigation.visibility = View.VISIBLE
        }
    }

    private fun navigationTry() {
        val beginText = begin.text.toString()
        val endText = end.text.toString()
        if (beginText == endText) {
            Toasty.warning(this, "起始地点不能相同哦～", Toast.LENGTH_LONG, true).show()
        } else {
            // 首先是展示地图
            showMap()
            viewPagerAdapter.getNavigation(beginText, endText)
        }
    }
}