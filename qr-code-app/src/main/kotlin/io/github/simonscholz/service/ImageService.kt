package io.github.simonscholz.service

import io.github.simonscholz.model.DotShapes
import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeDotShape
import io.github.simonscholz.qrcode.QrCodeDotStyler
import io.github.simonscholz.qrcode.QrCodeFactory
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig
import io.github.simonscholz.svg.QrCodeSvgFactory
import org.w3c.dom.Document
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class ImageService(
    private val qrCodeConfigViewModel: QrCodeConfigViewModel,
) {
    fun generateSvg(): Document {
        val qrCodeConfig = qrCodeConfig()
        return QrCodeSvgFactory.createQrCodeApi().createQrCodeSvg(qrCodeConfig)
    }

    fun renderImage(): BufferedImage {
        val qrCodeConfig = qrCodeConfig()
        return QrCodeFactory.createQrCodeApi().createQrCodeImage(qrCodeConfig)
    }

    private fun qrCodeConfig(): QrCodeConfig {
        val builder =
            QrCodeConfig
                .Builder(qrCodeConfigViewModel.qrCodeContent.value)
                .qrCodeSize(qrCodeConfigViewModel.size.value)
                .qrCodeColorConfig(
                    bgColor = qrCodeConfigViewModel.backgroundColor.value,
                    fillColor = qrCodeConfigViewModel.foregroundColor.value,
                ).qrCodeDotStyler(mapCustomDotStyler(qrCodeConfigViewModel.dotShape.value))
                .qrBorderConfig(
                    color = qrCodeConfigViewModel.borderColor.value,
                    relativeSize = qrCodeConfigViewModel.relativeBorderSize.value,
                    relativeBorderRound = qrCodeConfigViewModel.borderRadius.value,
                ).qrPositionalSquaresConfig(
                    qrPositionalSquaresConfig =
                        QrPositionalSquaresConfig(
                            isCircleShaped = qrCodeConfigViewModel.positionalSquareIsCircleShaped.value,
                            relativeSquareBorderRound = qrCodeConfigViewModel.positionalSquareRelativeBorderRound.value,
                            centerColor = qrCodeConfigViewModel.positionalSquareCenterColor.value,
                            innerSquareColor = qrCodeConfigViewModel.positionalSquareInnerSquareColor.value,
                            outerSquareColor = qrCodeConfigViewModel.positionalSquareOuterSquareColor.value,
                            outerBorderColor = qrCodeConfigViewModel.positionalSquareOuterBorderColor.value,
                            colorAdjustmentPatterns = !qrCodeConfigViewModel.positionalSquareColorAdjustmentPatterns.value,
                        ),
                )
        if (qrCodeConfigViewModel.logoBase64.value.isNotBlank()) {
            builder.qrLogoConfig(
                base64Logo = qrCodeConfigViewModel.logoBase64.value,
                relativeSize = qrCodeConfigViewModel.logoRelativeSize.value,
                bgColor = qrCodeConfigViewModel.logoBackgroundColor.value,
                shape = qrCodeConfigViewModel.logoShape.value,
            )
        }
        val qrCodeConfig = builder.build()
        return qrCodeConfig
    }

    private fun mapCustomDotStyler(value: DotShapes): QrCodeDotStyler =
        when (value) {
            DotShapes.SQUARE -> QrCodeDotShape.SQUARE
            DotShapes.CIRCLE -> QrCodeDotShape.CIRCLE
            DotShapes.ROUNDED_SQUARE -> QrCodeDotShape.ROUNDED_SQUARE
            DotShapes.HEXAGON -> QrCodeDotShape.HEXAGON
            DotShapes.TRIANGLE -> QrCodeDotShape.TRIANGLE
            DotShapes.HEART -> QrCodeDotShape.HEART
            DotShapes.HOUSE -> QrCodeDotShape.HOUSE
            DotShapes.STAR -> QrCodeDotShape.STAR
            DotShapes.DIAMOND -> QrCodeDotShape.DIAMOND
            DotShapes.CROSS -> QrCodeDotShape.CROSS
            DotShapes.FLOWER -> QrCodeDotShape.FLOWER
            DotShapes.FLOWER_2 -> QrCodeDotShape.FLOWER_2
            DotShapes.FLOWER_3 -> QrCodeDotShape.FLOWER_3
            DotShapes.EASTER_EGG -> QrCodeDotShape.EASTER_EGG
            DotShapes.HOUSE_WITH_DOOR_AND_WINDOW ->
                QrCodeDotStyler { x, y, size, graphic ->
                    drawColorfulHouseWithDoorAndWindow(x, y, size, graphic)
                }
            DotShapes.SMILEY ->
                QrCodeDotStyler { x, y, size, graphic ->
                    drawSmiley(x, y, size, graphic)
                }
            DotShapes.PUMPKIN ->
                QrCodeDotStyler { x, y, size, graphic ->
                    drawPumpkin(x, y, size, graphic)
                }
            DotShapes.CHRISTMAS_TREE ->
                QrCodeDotStyler { x, y, size, graphic ->
                    drawChristmasTree(x, y, size, graphic)
                }
            DotShapes.CAR ->
                QrCodeDotStyler { x, y, size, graphic ->
                    drawCar(x, y, size, graphic)
                }
        }

    private fun drawSmiley(
        x: Int,
        y: Int,
        dotSize: Int,
        graphics: Graphics2D,
    ) {
        drawDotImage(x, y, dotSize, graphics, "smiley_fill.png")
    }

    private fun drawPumpkin(
        x: Int,
        y: Int,
        dotSize: Int,
        graphics: Graphics2D,
    ) {
        drawDotImage(x, y, dotSize, graphics, "halloween_pumpkin.png")
    }

    private fun drawChristmasTree(
        x: Int,
        y: Int,
        dotSize: Int,
        graphics: Graphics2D,
    ) {
        drawDotImage(x, y, dotSize, graphics, "christmas_tree.png")
    }

    private fun drawCar(
        x: Int,
        y: Int,
        dotSize: Int,
        graphics: Graphics2D,
    ) {
        drawDotImage(x, y, dotSize, graphics, "car.png")
    }

    private fun drawDotImage(
        x: Int,
        y: Int,
        dotSize: Int,
        graphics: Graphics2D,
        image: String,
    ) {
        val resource = ImageService::class.java.getClassLoader().getResource(image)
        resource?.let {
            val imageDot = ImageIO.read(it)
            graphics.drawImage(imageDot, x, y, dotSize, dotSize, null)
        }
    }

    private fun drawColorfulHouseWithDoorAndWindow(
        x: Int,
        y: Int,
        size: Int,
        graphic: Graphics2D,
    ) {
        val roofHeight = size / 2
        val houseWidth = size
        val houseHeight = size - roofHeight

        // Draw the base of the house
        graphic.color = Color.RED
        graphic.fillRect(x, y + roofHeight, houseWidth, houseHeight)

        // Draw the roof
        graphic.color = Color.BLUE
        val roofXPoints = intArrayOf(x, x + houseWidth / 2, x + houseWidth)
        val roofYPoints = intArrayOf(y + roofHeight, y, y + roofHeight)
        graphic.fillPolygon(roofXPoints, roofYPoints, 3)

        // Draw the door
        val doorWidth = size / 5
        val doorHeight = size / 2 - 1
        val doorX = x + (houseWidth - size / 5) / 2 + size / 10
        val doorY = y + roofHeight + houseHeight - doorHeight + 1
        graphic.color = Color.GREEN
        graphic.fillRect(doorX, doorY, doorWidth, doorHeight)

        // Draw the window
        val windowSize = size / 5
        val windowX = x + size / 5
        val windowY = y + roofHeight + size / 5
        graphic.color = Color.YELLOW
        graphic.fillRect(windowX, windowY, windowSize, windowSize)
    }
}
