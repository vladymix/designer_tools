package com.altamirano.fabricio.desingtools.guides

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class NumbersGraphics {

    private val fillPaint: Paint = Paint()
    private val textPaint: Paint
    private val TEXT_SIZE = 50f
    var text = "10"

    init {
        fillPaint.color = Color.RED
        fillPaint.style = Paint.Style.FILL
        textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.textSize = TEXT_SIZE
    }

    fun setColor(color:Int){
        fillPaint.color = color
    }

   var startX:Float = 0f
   var startY:Float = 0f

    fun draw(canvas: Canvas ){

        val lineHeight = TEXT_SIZE + 2
        val textWidth = textPaint.measureText(text)

        canvas.drawRect(
            startX ,
            startY,
            startX + textWidth ,
            startY+lineHeight,
            fillPaint
        )
        canvas.drawText(text,startX,startY+lineHeight-10, textPaint )
    }

}