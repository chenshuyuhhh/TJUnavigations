package com.chenshuyusc.tjunavigations.util

import android.graphics.Bitmap
import android.graphics.Matrix

fun Bitmap.changeSize(size:Float): Bitmap {
    //设置想要的大小 size

    //计算压缩的比率
    val scaleWidth = size / width
    val scaleHeight = size/ height

    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}