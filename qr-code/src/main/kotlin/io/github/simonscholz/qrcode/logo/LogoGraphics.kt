package io.github.simonscholz.qrcode.logo

import java.awt.Color
import java.awt.Color.white
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import kotlin.math.floor

internal object LogoGraphics {

    fun drawLogo(graphics: Graphics2D, size: Int, logoImage: BufferedImage, relativeLogoSize: Double, logoBackgroundColor: Color?) {
        val logoSize: Int = floor(size * relativeLogoSize).toInt()
        val cx = size / 2 - logoSize / 2
        val cy = size / 2 - logoSize / 2

        logoBackgroundColor?.let {
            graphics.color = it
            graphics.fillArc(cx, cy, logoSize, logoSize, 0, 360)
        }

        val circle = Ellipse2D.Double(
            0.0,
            0.0,
            logoSize.toDouble(),
            logoSize.toDouble(),
        )
        val circleArea = Area(circle)
        val cropped = BufferedImage(logoSize, logoSize, BufferedImage.TYPE_4BYTE_ABGR)
        val croppedGraphics = cropped.graphics as Graphics2D
        croppedGraphics.clip = circleArea
        croppedGraphics.drawImage(logoImage, 0, 0, logoSize, logoSize, null)

        graphics.drawImage(cropped, cx, cy, logoSize, logoSize, null)
        croppedGraphics.dispose()
        graphics.dispose()
    }
}
