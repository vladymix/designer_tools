package com.altamirano.fabricio.desingtools.guides

import android.graphics.Canvas
import android.util.Log

class TouchLines {

    val lines = ArrayList<LineToDraw>().apply {
        add(LineToDraw(LineToDraw.TypeDisplace.Horizontal)) // HorizontalTop
        add(LineToDraw(LineToDraw.TypeDisplace.Horizontal)) // HorizontalDown
        add(LineToDraw(LineToDraw.TypeDisplace.Vertical)) // VerticalLeft
        add(LineToDraw(LineToDraw.TypeDisplace.Vertical)) // VerticalRight
    }

    fun getLine(position: Position): LineToDraw {
        return when (position) {
            Position.HorizontalTop -> lines[0]
            Position.HorizontalDown -> lines[1]
            Position.VerticalLeft -> lines[2]
            Position.VerticalRight -> lines[3]
        }
    }

    fun draw(canvas: Canvas) {
        lines.forEach {
            it.draw(canvas)
        }
    }

    fun release() {
        lines.forEach { it.isMoving = false }
    }

    fun getLineNearby(x: Float, y: Float): LineToDraw {
        val line = lines.firstOrNull { it.isMoving }
        if (line != null) {
            return line
        }

        val point = Point(x, y)
        var index = 0
        var lineNearby = lines[0]
        var lastDistance = getPointToDistance(lineNearby, x, y).distanceTo(point)
        for (i in 1 until lines.size) {
            val nearby = getPointToDistance(lines[i], x, y)
            val distance = nearby.distanceTo(point)
            Log.i("Line is", "Distance $distance lastDistance:$lastDistance")
            if (distance < lastDistance) {
                index = i
                lastDistance = distance
                lineNearby = lines[i]
            }
        }

        Log.i("Line is", "Type ${lineNearby.typeDisplace} index:$index")

        return lineNearby.apply {
            isMoving = true
        }

    }

    private fun getPointToDistance(lineToDraw: LineToDraw, x: Float, y: Float): Point {
        val nearby = Point(0F, 0F)
        if (lineToDraw.typeDisplace == LineToDraw.TypeDisplace.Vertical) {
            nearby.x = lineToDraw.startX
            nearby.y = y
        } else {
            nearby.x = x
            nearby.y = lineToDraw.startY
        }
        return nearby
    }

    fun getPointToDistance(type: LineToDraw.TypeDisplace): Float {
        return when (type) {
            LineToDraw.TypeDisplace.Vertical -> {
                val line = getLine(Position.HorizontalTop)
                val line2 = getLine(Position.HorizontalDown)
                val distance =
                    Point(line.startY, line.startY).distanceTo(Point(line2.startY, line2.startY))
                distance

            }
            LineToDraw.TypeDisplace.Horizontal -> {
                val line = getLine(Position.VerticalRight)
                val line2 = getLine(Position.VerticalLeft)
                val distance =
                    Point(line.startX, line.startX).distanceTo(Point(line2.startX, line2.startX))
                distance
            }
        }

    }

    fun getLineMoving(): LineToDraw? {
        return lines.firstOrNull { it.isMoving }
    }

    fun setStartLines(height: Int, width: Int) {
        if (height > 0 && width > 0) {
            lines.forEach {
                if (it.typeDisplace == LineToDraw.TypeDisplace.Vertical) {
                    it.startY = 0f
                    it.stopY = height.toFloat()
                } else {
                    it.startX = 0f
                    it.stopX = width.toFloat()
                }
            }
        }
    }

    fun colorVertical(vertical: Int) {
        getLine(Position.VerticalLeft).paint.color = vertical
        getLine(Position.VerticalRight).paint.color = vertical
    }
    fun colorHorizontal(horizontal: Int) {
        getLine(Position.HorizontalTop).paint.color = horizontal
        getLine(Position.HorizontalDown).paint.color = horizontal
    }


    enum class Position {
        HorizontalTop, HorizontalDown, VerticalLeft, VerticalRight
    }

    data class Point(var x: Float, var y: Float) {

        fun distanceTo(nearby: Point): Float {
            val xd = nearby.x - x
            val yd = nearby.y - y
            return Math.sqrt((xd * xd).toDouble() + (yd * yd).toDouble()).toFloat()
        }
    }
}