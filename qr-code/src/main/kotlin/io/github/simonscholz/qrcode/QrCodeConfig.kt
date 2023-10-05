package io.github.simonscholz.qrcode

import java.awt.Color
import java.awt.image.BufferedImage

/**
 * Object to be passed to QrCodeApi.
 *
 * @param qrCodeText - the text to be returned when the qr codes is scanned
 * @param qrCodeSize - the size of the qr code in pixels
 * @param qrLogoConfig - configuration of the logo to be rendered in the middle of the qr code, may be null
 * @param qrCodeColorConfig - configuration of the colors of the qr code
 * @param qrPositionalSquaresConfig - configure the positional squares on the qr code
 * @param qrBorderConfig - configure the border of the qr code
 */
data class QrCodeConfig @JvmOverloads constructor(
    val qrCodeText: String,
    val qrCodeSize: Int,
    val qrLogoConfig: QrLogoConfig? = null,
    val qrCodeColorConfig: QrCodeColorConfig = QrCodeColorConfig(),
    val qrPositionalSquaresConfig: QrPositionalSquaresConfig = QrPositionalSquaresConfig(),
    val qrBorderConfig: QrBorderConfig = QrBorderConfig(),
) {
    init {
        require(qrCodeText.isNotBlank()) { "qrCodeText must not be blank." }
        require(qrCodeSize > 0) { "qrCodeSize must be greater than 0." }
    }

    class Builder(private val qrCodeText: String) {
        private var qrCodeSize: Int = 200
        private var qrLogoConfig: QrLogoConfig? = null
        private var qrCodeColorConfig: QrCodeColorConfig = QrCodeColorConfig()
        private var qrPositionalSquaresConfig: QrPositionalSquaresConfig = QrPositionalSquaresConfig()
        private var qrBorderConfig: QrBorderConfig = QrBorderConfig()

        fun qrCodeSize(qrCodeSize: Int) = apply { this.qrCodeSize = qrCodeSize }
        fun qrLogoConfig(logo: BufferedImage, relativeSize: Double = .2) = apply { this.qrLogoConfig = QrLogoConfig(logo, relativeSize) }
        fun qrLogoConfig(logo: BufferedImage) = apply { this.qrLogoConfig = QrLogoConfig(logo, .2) }
        fun qrCodeColorConfig(bgColor: Color = Color.WHITE, fillColor: Color = Color.BLACK) = apply { this.qrCodeColorConfig = QrCodeColorConfig(bgColor, fillColor) }
        fun qrPositionalSquaresConfig(qrPositionalSquaresConfig: QrPositionalSquaresConfig) = apply { this.qrPositionalSquaresConfig = qrPositionalSquaresConfig }
        fun qrBorderConfig(color: Color = Color.BLACK, relativeSize: Double = .05) = apply { this.qrBorderConfig = QrBorderConfig(color, relativeSize) }
        fun qrBorderConfig(relativeSize: Double = .05) = apply { this.qrBorderConfig = QrBorderConfig(Color.BLACK, relativeSize) }
        fun qrBorderConfig(color: Color = Color.BLACK) = apply { this.qrBorderConfig = QrBorderConfig(color, .05) }

        fun build() = QrCodeConfig(
            qrCodeText = qrCodeText,
            qrCodeSize = qrCodeSize,
            qrLogoConfig = qrLogoConfig,
            qrCodeColorConfig = qrCodeColorConfig,
            qrPositionalSquaresConfig = qrPositionalSquaresConfig,
            qrBorderConfig = qrBorderConfig,
        )
    }
}

/**
 * Pass a logo as BufferedImage and specify the relativeSize of the logo in the qr code.
 *
 * @param logo - BufferedImage to be rendered as logo in the center of the qr code
 * @param relativeSize - relative size of the logo, defaults to 0.2
 */
data class QrLogoConfig @JvmOverloads constructor(val logo: BufferedImage, val relativeSize: Double = .2) {
    init {
        require(relativeSize in 0.0..1.0) { "relativeSize must be in between 0 and 1." }
    }
}

/**
 * Specify the colors in the qr code.
 *
 * Be cautions with this and remember to choose a high contrast between the colors to ensure a proper recognition of the qr code.
 *
 * @param bgColor - specify the background color of the qr code - may also be transparent, defaults to Color.WHITE
 * @param fillColor - specify the fill or foreground color of the qr code - should not be transparent, defaults to Color.BLACK
 */
data class QrCodeColorConfig @JvmOverloads constructor(val bgColor: Color = Color.WHITE, val fillColor: Color = Color.BLACK)

/**
 * Specify the colors and shapes of the positional squares of the qr code.
 *
 * @param rindColor - specify the color of the ring, defaults to Color.BLACK
 * @param centerColor - specify the color of the center of the positional squares, defaults to Color.BLACK
 * @param outerColor - specify the outer color of the positional squares, defaults to Color.WHITE
 * @param isCircleShaped - specify whether the shape shall be a circle or a square, defaults to false
 * @param relativeSquareBorderRound - in case isCircleShaped==false the borders of the square may be round at the edges, defaults to 0.05
 */
data class QrPositionalSquaresConfig @JvmOverloads constructor(
    val rindColor: Color = Color.BLACK,
    val centerColor: Color = Color.BLACK,
    val outerColor: Color = Color.WHITE,
    val isCircleShaped: Boolean = false,
    val relativeSquareBorderRound: Double = .5,
) {
    init {
        require(relativeSquareBorderRound in 0.0..1.0) { "relativeSquareBorderRound must be in between 0 and 1." }
    }
}

/**
 * Specify color and relative size of the border.
 *
 * @param color - color of the border
 * @param relativeSize - relative size of the border, defaults to 0.05
 */
data class QrBorderConfig @JvmOverloads constructor(val color: Color = Color.BLACK, val relativeSize: Double = .05) {
    init {
        require(relativeSize in 0.0..1.0) { "relativeSize must be in between 0 and 1." }
    }
}
