package com.chenshuyusc.tjunavigations.homeview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.edu.twt.retrox.recyclerviewdsl.Item
import cn.edu.twt.retrox.recyclerviewdsl.ItemController
import com.chenshuyusc.tjunavigations.R

class PlaceItem(val place: String, val position: String, val click: (String) -> Unit) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
            return PlaceHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as PlaceHolder
            item as PlaceItem
            holder.apply {
                place.text = item.place
                position.text = item.position
                itemView.setOnClickListener {
                    item.click(place.text.toString())
                }
            }
        }
    }

    class PlaceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val place = itemView.findViewById<TextView>(R.id.search_tv_place)
        val position = itemView.findViewById<TextView>(R.id.search_tv_position)
    }

    override val controller: ItemController get() = Controller
}

fun MutableList<Item>.addPlace(place: String, position: String, click: (String) -> Unit) =
    add(PlaceItem(place, position, click))
