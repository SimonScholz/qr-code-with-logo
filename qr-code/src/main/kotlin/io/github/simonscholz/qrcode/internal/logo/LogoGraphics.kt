package io.github.simonscholz.qrcode.internal.logo

import io.github.simonscholz.qrcode.LogoShape
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Image
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import kotlin.math.floor

internal object LogoGraphics {

    fun drawLogo(
        graphics: Graphics2D,
        size: Int,
        logoImage: Image,
        relativeLogoSize: Double,
        logoBackgroundColor: Color?,
        shape: LogoShape,
    ) {
        val logoSize: Int = floor(size * relativeLogoSize).toInt()
        when (shape) {
            LogoShape.CIRCLE -> drawCircleShape(size, logoSize, logoBackgroundColor, graphics, logoImage)
            LogoShape.SQUARE -> drawSquareShape(size, logoSize, logoBackgroundColor, graphics, logoImage)
            LogoShape.ORIGINAL -> drawOriginalShape(size, logoSize, logoBackgroundColor, graphics, logoImage)
            LogoShape.ELLIPSE -> drawEllipseShape(size, logoSize, logoBackgroundColor, graphics, logoImage)
        }
        graphics.dispose()
    }

    private fun drawCircleShape(size: Int, logoSize: Int, logoBackgroundColor: Color?, graphics: Graphics2D, logoImage: Image) {
        val xyCenter = size / 2 - logoSize / 2

        logoBackgroundColor?.let {
            graphics.color = it
            graphics.fillArc(xyCenter, xyCenter, logoSize, logoSize, 0, 360)
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

        graphics.drawImage(cropped, xyCenter, xyCenter, logoSize, logoSize, null)
        croppedGraphics.dispose()
    }

    private fun drawSquareShape(size: Int, logoSize: Int, logoBackgroundColor: Color?, graphics: Graphics2D, logoImage: Image) {
        val xyCenter = size / 2 - logoSize / 2

        logoBackgroundColor?.let {
            graphics.color = it
            graphics.fillRect(xyCenter, xyCenter, logoSize, logoSize)
        }

        graphics.drawImage(logoImage, xyCenter, xyCenter, logoSize, logoSize, null)
    }

    private fun drawOriginalShape(size: Int, logoSize: Int, logoBackgroundColor: Color?, graphics: Graphics2D, logoImage: Image) {
        val logoWidth = logoImage.getWidth(null)
        val logoHeight = logoImage.getHeight(null)

        if (logoWidth > logoHeight) {
            drawLandscape(logoSize, logoWidth, logoHeight, size, logoBackgroundColor, graphics, logoImage)
        } else {
            drawPortrait(logoSize, logoHeight, logoWidth, size, logoBackgroundColor, graphics, logoImage)
        }
    }

    private fun drawPortrait(logoSize: Int, logoHeight: Int, logoWidth: Int, size: Int, logoBackgroundColor: Color?, graphics: Graphics2D, logoImage: Image) {
        val ratio = logoSize.toDouble() / logoHeight.toDouble()
        val newWidth = (logoWidth * ratio).toInt()
        val cx = size / 2 - newWidth / 2
        val cy = size / 2 - logoSize / 2

        logoBackgroundColor?.let {
            graphics.color = it
            graphics.fillRect(cx, cy, newWidth, logoSize)
        }

        graphics.drawImage(logoImage, cx, cy, newWidth, logoSize, null)
    }

    private fun drawLandscape(logoSize: Int, logoWidth: Int, logoHeight: Int, size: Int, logoBackgroundColor: Color?, graphics: Graphics2D, logoImage: Image) {
        val ratio = logoSize.toDouble() / logoWidth.toDouble()
        val newHeight = (logoHeight * ratio).toInt()
        val cx = size / 2 - logoSize / 2
        val cy = size / 2 - newHeight / 2

        logoBackgroundColor?.let {
            graphics.color = it
            graphics.fillRect(cx, cy, logoSize, newHeight)
        }

        graphics.drawImage(logoImage, cx, cy, logoSize, newHeight, null)
    }

    private fun drawEllipseShape(size: Int, logoSize: Int, logoBackgroundColor: Color?, graphics: Graphics2D, logoImage: Image) {
        val logoWidth = logoImage.getWidth(null)
        val logoHeight = logoImage.getHeight(null)

        if (logoWidth > logoHeight) {
            drawLandscapeEllipse(logoSize, logoWidth, logoHeight, size, logoBackgroundColor, graphics, logoImage)
        } else {
            drawPortraitEllipse(logoSize, logoHeight, logoWidth, size, logoBackgroundColor, graphics, logoImage)
        }
    }

    private fun drawPortraitEllipse(logoSize: Int, logoHeight: Int, logoWidth: Int, size: Int, logoBackgroundColor: Color?, graphics: Graphics2D, logoImage: Image) {
        val ratio = logoSize.toDouble() / logoHeight.toDouble()
        val newWidth = (logoWidth * ratio).toInt()
        val cx = size / 2 - newWidth / 2
        val cy = size / 2 - logoSize / 2

        logoBackgroundColor?.let {
            graphics.color = it
            graphics.fillArc(cx, cy, newWidth, logoSize, 0, 360)
        }

        val circle = Ellipse2D.Double(
            .0,
            .0,
            newWidth.toDouble(),
            logoSize.toDouble(),
        )
        val circleArea = Area(circle)
        val cropped = BufferedImage(newWidth, logoSize, BufferedImage.TYPE_4BYTE_ABGR)
        val croppedGraphics = cropped.graphics as Graphics2D
        croppedGraphics.clip = circleArea
        croppedGraphics.drawImage(logoImage, 0, 0, newWidth, logoSize, null)

        graphics.drawImage(cropped, cx, cy, newWidth, logoSize, null)
        croppedGraphics.dispose()
    }

    private fun drawLandscapeEllipse(logoSize: Int, logoWidth: Int, logoHeight: Int, size: Int, logoBackgroundColor: Color?, graphics: Graphics2D, logoImage: Image) {
        val ratio = logoSize.toDouble() / logoWidth.toDouble()
        val newHeight = (logoHeight * ratio).toInt()
        val cx = size / 2 - logoSize / 2
        val cy = size / 2 - newHeight / 2

        logoBackgroundColor?.let {
            graphics.color = it
            graphics.fillArc(cx, cy, logoSize, newHeight, 0, 360)
        }

        val ellipse = Ellipse2D.Double(
            .0,
            .0,
            logoSize.toDouble(),
            newHeight.toDouble(),
        )
        val circleArea = Area(ellipse)
        val cropped = BufferedImage(logoSize, newHeight, BufferedImage.TYPE_4BYTE_ABGR)
        val croppedGraphics = cropped.graphics as Graphics2D
        croppedGraphics.clip = circleArea
        croppedGraphics.drawImage(logoImage, 0, 0, logoSize, newHeight, null)

        graphics.drawImage(cropped, cx, cy, logoSize, newHeight, null)
        croppedGraphics.dispose()
    }
}
