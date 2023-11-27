package io.github.simonscholz.qrcode

import io.github.simonscholz.qrcode.internal.graphics.CustomQrCodeDotStyler
import java.awt.Graphics2D

interface QrCodeDotStyler {
    fun createDot(x: Int, y: Int, dotSize: Int, graphics: Graphics2D)
}

enum class QrCodeDotShape : QrCodeDotStyler {
    SQUARE {
        override fun createDot(x: Int, y: Int, dotSize: Int, graphics: Graphics2D) {
            graphics.fillRect(x, y, dotSize, dotSize)
        }
    },
    ROUNDED_SQUARE {
        override fun createDot(x: Int, y: Int, dotSize: Int, graphics: Graphics2D) {
            graphics.fillRoundRect(x, y, dotSize, dotSize, 10, 10)
        }
    },
    CIRCLE {
        override fun createDot(x: Int, y: Int, dotSize: Int, graphics: Graphics2D) {
            graphics.fillArc(x, y, dotSize, dotSize, 0, 360)
        }
    },
    HEXAGON {
        override fun createDot(x: Int, y: Int, dotSize: Int, graphics: Graphics2D) {
            CustomQrCodeDotStyler.drawHexagon(x, y, dotSize, graphics)
        }
    },
    TRIANGLE {
        override fun createDot(x: Int, y: Int, dotSize: Int, graphics: Graphics2D) {
            CustomQrCodeDotStyler.drawEquilateralTriangle(x, y, dotSize, graphics)
        }
    },
    HEART {
        override fun createDot(x: Int, y: Int, dotSize: Int, graphics: Graphics2D) {
            CustomQrCodeDotStyler.drawHeart(x, y, dotSize, graphics)
        }
    },
    HOUSE {
        override fun createDot(x: Int, y: Int, dotSize: Int, graphics: Graphics2D) {
            CustomQrCodeDotStyler.drawHouse(x, y, dotSize, graphics)
        }
    },
    ;
}