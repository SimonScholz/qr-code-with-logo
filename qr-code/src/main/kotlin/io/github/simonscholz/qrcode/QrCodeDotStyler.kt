package io.github.simonscholz.qrcode

import io.github.simonscholz.qrcode.internal.graphics.CustomQrCodeDotStyler
import java.awt.Graphics2D

/**
 * Funtional interface to apply a custom style for the QR code dots.
 *
 * @see QrCodeDotShape - for predefined shapes
 */
fun interface QrCodeDotStyler {
    fun createDot(
        x: Int,
        y: Int,
        dotSize: Int,
        graphics: Graphics2D,
    )
}

/**
 * Predefined shapes for the QR code dots.
 */
enum class QrCodeDotShape : QrCodeDotStyler {
    SQUARE {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            graphics.fillRect(x, y, dotSize, dotSize)
        }
    },
    ROUNDED_SQUARE {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            graphics.fillRoundRect(x, y, dotSize, dotSize, 10, 10)
        }
    },
    CIRCLE {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            graphics.fillArc(x, y, dotSize, dotSize, 0, 360)
        }
    },
    HEXAGON {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            CustomQrCodeDotStyler.drawHexagon(x, y, dotSize, graphics)
        }
    },
    TRIANGLE {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            CustomQrCodeDotStyler.drawEquilateralTriangle(x, y, dotSize, graphics)
        }
    },
    HEART {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            CustomQrCodeDotStyler.drawHeart(x, y, dotSize, graphics)
        }
    },
    HOUSE {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            CustomQrCodeDotStyler.drawHouse(x, y, dotSize, graphics)
        }
    },
    STAR {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            CustomQrCodeDotStyler.drawStar(x, y, dotSize, graphics)
        }
    },
    DIAMOND {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            CustomQrCodeDotStyler.drawDiamond(x, y, dotSize, graphics)
        }
    },
    CROSS {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            CustomQrCodeDotStyler.drawCross(x, y, dotSize, graphics)
        }
    },
    SMILEY {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            CustomQrCodeDotStyler.drawSmiley(x, y, dotSize, graphics)
        }
    },
    FLOWER {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            CustomQrCodeDotStyler.drawFlower(x, y, dotSize, graphics)
        }
    },
    FLOWER_2 {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            CustomQrCodeDotStyler.drawFlower(
                x = x,
                y = y,
                size = dotSize,
                graphics = graphics,
                petalLength = dotSize * 0.65,
                petalWidth = dotSize * 0.3,
                centerDotSize = dotSize * 0.25,
            )
        }
    },
    FLOWER_3 {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            CustomQrCodeDotStyler.drawFlower(
                x = x,
                y = y,
                size = dotSize,
                graphics = graphics,
                petalLength = dotSize * 0.65,
                petalWidth = dotSize * 0.3,
                centerDotSize = dotSize * 0.35,
            )
        }
    },
    EASTER_EGG {
        override fun createDot(
            x: Int,
            y: Int,
            dotSize: Int,
            graphics: Graphics2D,
        ) {
            CustomQrCodeDotStyler.drawEasterEgg(x, y, dotSize, graphics)
        }
    },
}
