package io.github.simonscholz

import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeDotShape
import io.github.simonscholz.qrcode.QrCodeFactory
import java.awt.Color
import java.awt.Graphics2D
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val path = Paths.get(System.getProperty("user.home"), "qr-code-samples")
    Files.createDirectories(path)
    val qrCodeDir = path.toAbsolutePath().toString()
    val qrCodeApi = QrCodeFactory.createQrCodeApi()
    QrCodeConfig.Builder("https://simonscholz.github.io/")
        .qrCodeDotStyler(QrCodeDotShape.CIRCLE)
        .qrCodeSize(800)
        .build()
        .run {
            qrCodeApi.createQrCodeImage(this)
                .toFile(File(qrCodeDir, "/qr-with-CIRCLE-dots-kotlin.png"))
        }

    QrCodeConfig.Builder("https://simonscholz.github.io/")
        .qrCodeDotStyler(QrCodeDotShape.ROUNDED_SQUARE)
        .qrCodeSize(800)
        .build()
        .run {
            qrCodeApi.createQrCodeImage(this)
                .toFile(File(qrCodeDir, "/qr-with-ROUND-SQUARE-dots-kotlin.png"))
        }

    QrCodeConfig.Builder("https://simonscholz.github.io/")
        .qrCodeDotStyler(QrCodeDotShape.HEXAGON)
        .qrCodeSize(800)
        .build()
        .run {
            qrCodeApi.createQrCodeImage(this)
                .toFile(File(qrCodeDir, "/qr-with-HEXAGON-dots-kotlin.png"))
        }

    QrCodeConfig.Builder("https://simonscholz.github.io/")
        .qrCodeDotStyler(QrCodeDotShape.TRIANGLE)
        .qrCodeSize(800)
        .build()
        .run {
            qrCodeApi.createQrCodeImage(this)
                .toFile(File(qrCodeDir, "/qr-with-TRIANGLE-dots-kotlin.png"))
        }

    QrCodeConfig.Builder("https://simonscholz.github.io/")
        .qrCodeDotStyler(QrCodeDotShape.HEART)
        .qrCodeSize(800)
        .build()
        .run {
            qrCodeApi.createQrCodeImage(this)
                .toFile(File(qrCodeDir, "/qr-with-HEART-dots-kotlin.png"))
        }

    QrCodeConfig.Builder("https://simonscholz.github.io/")
        .qrCodeDotStyler(QrCodeDotShape.HOUSE)
        .qrCodeSize(800)
        .build()
        .run {
            qrCodeApi.createQrCodeImage(this)
                .toFile(File(qrCodeDir, "/qr-with-HOUSE-dots-kotlin.png"))
        }

    QrCodeConfig.Builder("https://simonscholz.github.io/")
        .qrCodeDotStyler(::drawColorfulHouseWithDoorAndWindow)
        .qrCodeSize(800)
        .build()
        .run {
            qrCodeApi.createQrCodeImage(this)
                .toFile(File(qrCodeDir, "/qr-with-COLORFUL-HOUSE-dots-kotlin.png"))
        }
}

private fun drawColorfulHouseWithDoorAndWindow(x: Int, y: Int, size: Int, graphic: Graphics2D) {
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