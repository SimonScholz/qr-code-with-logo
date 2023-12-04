package io.github.simonscholz.qrcode.internal.graphics

import java.awt.Graphics2D
import java.awt.Point
import java.awt.Polygon
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

internal object CustomQrCodeDotStyler {

    fun drawHouse(x: Int, y: Int, size: Int, graphic: Graphics2D) {
        val roofHeight = size / 2
        val houseWidth = size - 1 // -1 to have a gap between the houses
        val houseHeight = size - roofHeight

        // Draw the base of the house
        graphic.fillRect(x, y + roofHeight, houseWidth, houseHeight)

        // Draw the roof
        val roofXPoints = intArrayOf(x, x + houseWidth / 2, x + houseWidth)
        val roofYPoints = intArrayOf(y + roofHeight, y, y + roofHeight)
        graphic.fillPolygon(roofXPoints, roofYPoints, 3)
    }

    fun drawHeart(x: Int, y: Int, size: Int, graphic: Graphics2D) {
        val heartWidth = handleRoundingIssues(size)
        val heartHeight = handleRoundingIssues(size)
        val gap = heartWidth / 4

        // Draw the left arc of the heart
        graphic.fillArc(x, y, heartWidth / 2, heartHeight / 2, 0, 180)

        // Draw the right arc of the heart
        graphic.fillArc(x + heartWidth / 2, y, heartWidth / 2, heartHeight / 2, 0, 180)

        // Draw the bottom triangle of the heart
        val triangleXPoints = intArrayOf(x, x + heartWidth / 2, x + heartWidth, x)
        val triangleYPoints = intArrayOf(y + heartHeight / 2 - gap, y + heartHeight - gap, y + heartHeight / 2 - gap, y + heartHeight / 2 - gap)
        graphic.fillPolygon(triangleXPoints, triangleYPoints, 4)
    }

    private fun handleRoundingIssues(size: Int): Int =
        if (size % 2 == 0) {
            size
        } else {
            size - 1
        }

    fun drawHexagon(x: Int, y: Int, size: Int, graphic: Graphics2D) {
        val hexRadius = size / 2
        graphic.fillPolygon(createHexagon(Point(x + hexRadius, y + hexRadius), hexRadius))
    }

    private fun createHexagon(center: Point, radius: Int): Polygon {
        val polygon = Polygon()
        for (i in 0..5) {
            polygon.addPoint(
                (center.x + radius * cos(i * 2 * Math.PI / 6.0)).toInt(),
                (center.y + radius * sin(i * 2 * Math.PI / 6.0)).toInt(),
            )
        }
        return polygon
    }

    fun drawEquilateralTriangle(x: Int, y: Int, size: Int, graphic: Graphics2D) {
        val triangleHeight = (sqrt(3.0) / 2 * size).toInt()

        val triangleXPoints = intArrayOf(x, x + size / 2, x + size, x)
        val triangleYPoints = intArrayOf(y + triangleHeight, y, y + triangleHeight, y + triangleHeight)

        graphic.fillPolygon(triangleXPoints, triangleYPoints, 3)
    }

    fun drawStar(x: Int, y: Int, size: Int, graphic: Graphics2D) {
        val outerRadius: Double = (size / 2).toDouble()
        val innerRadius: Double = (size / 4).toDouble()

        val xPoints = IntArray(10)
        val yPoints = IntArray(10)

        for (i in 0 until 10) {
            val angle = Math.PI / 5 * i
            val radius = if (i % 2 == 0) outerRadius else innerRadius
            xPoints[i] = (x + size / 2 + radius * cos(angle)).toInt()
            yPoints[i] = (y + size / 2 + radius * sin(angle)).toInt()
        }

        val star = Polygon(xPoints, yPoints, 10)

        graphic.fill(star)
    }

    fun drawDiamond(x: Int, y: Int, size: Int, graphic: Graphics2D) {
        val halfSize = size / 2

        val xPoints = intArrayOf(x + halfSize, x + size, x + halfSize, x)
        val yPoints = intArrayOf(y, y + halfSize, y + size, y + halfSize)

        val diamond = Polygon(xPoints, yPoints, 4)

        graphic.fill(diamond)
    }

    fun drawCross(x: Int, y: Int, size: Int, graphic: Graphics2D) {
        graphic.fillRect(x, y + size / 4, size, size / 2)
        graphic.fillRect(x + size / 4, y, size / 2, size)
    }
}
