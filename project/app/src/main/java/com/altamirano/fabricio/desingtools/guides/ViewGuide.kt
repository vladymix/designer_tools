package com.altamirano.fabricio.desingtools.guides

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.altamirano.fabricio.desingtools.AppLogic.getAsDp
import java.math.RoundingMode
import java.text.DecimalFormat

class ViewGuide(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var horizontalColor: Int = Color.RED
    var verticalColor: Int = Color.RED
    private var numLines = 10
    private val indicatorHorizontal = NumbersGraphics()
    private val indicatorVertical = NumbersGraphics()
    private val touchLines = TouchLines()

    init {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchLines.getLineNearby(it.x, it.y).let { line ->
                        if (line.typeDisplace == LineToDraw.TypeDisplace.Vertical) {
                            displaceVertical(it.x, line)
                        } else {
                            displaceHorizontal(it.y, line)
                        }
                    }
                    invalidate()

                }
                MotionEvent.ACTION_MOVE -> {
                    touchLines.getLineMoving()?.let { line ->
                        if (line.typeDisplace == LineToDraw.TypeDisplace.Vertical) {
                            displaceVertical(it.x, line)
                            indicatorHorizontal.startY = it.y
                            indicatorHorizontal.startX = it.x
                            indicatorHorizontal.text =
                                this.getDistance(LineToDraw.TypeDisplace.Horizontal)
                        } else {
                            displaceHorizontal(it.y, line)
                            indicatorVertical.startY = it.y
                            indicatorVertical.startX = it.x
                            indicatorVertical.text =
                                this.getDistance(LineToDraw.TypeDisplace.Vertical)
                        }
                    }

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

    private fun getDistance(type: LineToDraw.TypeDisplace): String {
        val value = this.context.getAsDp(touchLines.getPointToDistance(type))
        return DecimalFormat("#.##").apply { this.roundingMode = RoundingMode.DOWN }.format(value)
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
            indicatorHorizontal.draw(it)
            indicatorVertical.draw(it)
        }
    }

    fun setColorGuides(vertical: Int, horizontal: Int) {
        this.verticalColor = vertical
        this.horizontalColor = horizontal
        touchLines.colorVertical(vertical)
        touchLines.colorHorizontal(horizontal)

        indicatorHorizontal.setColor(vertical)
        indicatorVertical.setColor(horizontal)
        invalidate()
    }
}