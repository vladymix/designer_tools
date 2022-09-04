package com.altamirano.fabricio.desingtools.guides

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class ViewGuide(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var horizontalColor: Int = Color.RED
    var verticalColor: Int = Color.RED
    private var numLines = 10
    private val numbersGraphics = NumbersGraphics()
    private val touchLines = TouchLines()

    init {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    numbersGraphics.startY = it.y
                    numbersGraphics.startX = it.x

                    touchLines.getLineNearby(it.x, it.y).let { line ->
                        if (line.typeDisplace == LineToDraw.TypeDisplace.Vertical) {
                            displaceVertical(it.x, line)
                        } else {
                            displaceHorizontal(it.y, line)
                        }
                    }
                    numbersGraphics.text =
                        touchLines.getPointToDistance(LineToDraw.TypeDisplace.Horizontal).toString()
                    invalidate()

                }
                MotionEvent.ACTION_MOVE -> {
                    numbersGraphics.startY = it.y
                    numbersGraphics.startX = it.x

                    touchLines.getLineMoving()?.let { line ->
                        if (line.typeDisplace == LineToDraw.TypeDisplace.Vertical) {
                            displaceVertical(it.x, line)
                        } else {
                            displaceHorizontal(it.y, line)
                        }
                    }
                    numbersGraphics.text =
                        touchLines.getPointToDistance(LineToDraw.TypeDisplace.Horizontal).toString()

                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    touchLines.release()
                }
            }
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun displaceHorizontal(y: Float, line: LineToDraw) {
        line.startY = y
        line.stopY = y
        line.startX = 0F
        line.stopX = width.toFloat()
    }

    private fun displaceVertical(x: Float, line: LineToDraw) {
        line.startX = x
        line.stopX = x

        line.startY = 0f
        line.stopY = height.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            touchLines.setStartLines(this.height, this.width)
            touchLines.draw(it)
            numbersGraphics.draw(it)
        }
    }

    fun setColorGuides(vertical: Int, horizontal: Int) {
        this.verticalColor = vertical
        this.horizontalColor = horizontal
        touchLines.colorVertical(vertical)
        touchLines.colorHorizontal(horizontal)
        invalidate()
    }
}