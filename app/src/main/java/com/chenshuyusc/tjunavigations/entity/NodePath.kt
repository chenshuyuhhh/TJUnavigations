package com.chenshuyusc.tjunavigations.entity

class NodePath(private val node: Node) {
    var weight: Double = 0.0
    lateinit var before: Node
}