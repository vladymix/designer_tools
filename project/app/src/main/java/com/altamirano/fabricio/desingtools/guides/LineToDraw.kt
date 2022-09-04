package com.altamirano.fabricio.desingtools.guides

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint

class LineToDraw (val typeDisplace:TypeDisplace, color:Int = Color.RED) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var startX: Float = 0f
    var startY: Float = 0f
    var stopX: Float = 0f
    var stopY: Float = 0f
    var isMoving = false
    private var stroke = 2f

    init {
        paint.color = color
        paint.strokeWidth = stroke
        paint.pathEffect = DashPathEffect(listOf(5f, 5f).toFloatArray(), 10f)
    }


    fun draw(canvas: Canvas) {
        canvas.drawLine(startX, startY, stopX, stopY, paint)
    }

    enum class TypeDisplace{
        Horizontal, Vertical
    }
}