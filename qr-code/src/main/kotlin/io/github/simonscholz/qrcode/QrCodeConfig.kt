package io.github.simonscholz.qrcode

import java.awt.Color
import java.awt.Image

const val DEFAULT_IMG_SIZE = 300

/**
 * Object to be passed to QrCodeApi.
 *
 * @param qrCodeText - the text to be returned when the qr codes is scanned
 * @param qrCodeSize - the size of the qr code in pixels
 * @param qrLogoConfig - configuration of the logo to be rendered in the middle of the qr code, may be null
 * @param qrCodeColorConfig - configuration of the colors of the qr code
 * @param qrPositionalSquaresConfig - configure the positional squares on the qr code
 * @param qrCodeDotStyler - configure the shape of the dots in the qr code, also see [QrCodeDotShape]
 * @param qrBorderConfig - configure the border of the qr code
 */
class QrCodeConfig
    @JvmOverloads
    constructor(
        val qrCodeText: String,
        val qrCodeSize: Int = DEFAULT_IMG_SIZE,
        val qrLogoConfig: QrLogoConfig? = null,
        val qrCodeColorConfig: QrCodeColorConfig = QrCodeColorConfig(),
        val qrPositionalSquaresConfig: QrPositionalSquaresConfig = QrPositionalSquaresConfig(),
        val qrCodeDotStyler: QrCodeDotStyler = QrCodeDotShape.SQUARE,
        val qrBorderConfig: QrBorderConfig? = null,
    ) {
        init {
            require(qrCodeText.isNotBlank()) { "qrCodeText must not be blank." }
            require(qrCodeSize > 0) { "qrCodeSize must be greater than 0." }
        }

        class Builder(private val qrCodeText: String) {
            private var qrCodeSize: Int = DEFAULT_IMG_SIZE
            private var qrLogoConfig: QrLogoConfig? = null
            private var qrCodeColorConfig: QrCodeColorConfig = QrCodeColorConfig()
            private var qrPositionalSquaresConfig: QrPositionalSquaresConfig = QrPositionalSquaresConfig()
            private var qrBorderConfig: QrBorderConfig? = null
            private var qrCodeDotStyler: QrCodeDotStyler = QrCodeDotShape.SQUARE

            fun qrCodeSize(qrCodeSize: Int) = apply { this.qrCodeSize = qrCodeSize }

            @JvmOverloads fun qrLogoConfig(
                logo: Image,
                relativeSize: Double = .2,
                bgColor: Color? = null,
                shape: LogoShape = LogoShape.CIRCLE,
            ) = apply {
                this.qrLogoConfig = QrLogoConfig(logo = logo, base64Logo = null, relativeSize = relativeSize, bgColor = bgColor, shape = shape)
            }

            @JvmOverloads fun qrLogoConfig(
                base64Logo: String,
                relativeSize: Double = .2,
                bgColor: Color? = null,
                shape: LogoShape = LogoShape.CIRCLE,
            ) = apply {
                this.qrLogoConfig = QrLogoConfig(base64Logo = base64Logo, relativeSize = relativeSize, bgColor = bgColor, shape = shape)
            }

            @JvmOverloads fun qrCodeColorConfig(
                bgColor: Color = Color.WHITE,
                fillColor: Color = Color.BLACK,
            ) = apply {
                this.qrCodeColorConfig = QrCodeColorConfig(bgColor, fillColor)
            }

            fun qrPositionalSquaresConfig(qrPositionalSquaresConfig: QrPositionalSquaresConfig) =
                apply {
                    this.qrPositionalSquaresConfig = qrPositionalSquaresConfig
                }

            @JvmOverloads fun qrBorderConfig(
                color: Color = Color.BLACK,
                relativeSize: Double = .05,
                relativeBorderRound: Double = 0.2,
            ) = apply {
                this.qrBorderConfig = QrBorderConfig(color, relativeSize, relativeBorderRound)
            }

            fun qrCodeDotStyler(qrCodeDotStyler: QrCodeDotStyler) =
                apply {
                    this.qrCodeDotStyler = qrCodeDotStyler
                }

            fun build() =
                QrCodeConfig(
                    qrCodeText = qrCodeText,
                    qrCodeSize = qrCodeSize,
                    qrLogoConfig = qrLogoConfig,
                    qrCodeColorConfig = qrCodeColorConfig,
                    qrPositionalSquaresConfig = qrPositionalSquaresConfig,
                    qrCodeDotStyler = qrCodeDotStyler,
                    qrBorderConfig = qrBorderConfig,
                )
        }
    }

/**
 * Pass a logo as BufferedImage or base64 encoded image,
 * specify the relativeSize of the logo in the qr code and choose the logo shape.
 *
 * @param logo - [Image] to be rendered as logo in the center of the qr code
 * @param base64Logo - base64 encoded image to be rendered as logo in the center of the qr code
 * @param relativeSize - relative size of the logo, defaults to 0.2
 * @param bgColor - specify the background color of the logo, defaults to null
 * @param shape - specify the shape of the logo, defaults to [LogoShape.CIRCLE]
 */
class QrLogoConfig
    @JvmOverloads
    constructor(
        val logo: Image? = null,
        val base64Logo: String? = null,
        val relativeSize: Double = .2,
        val bgColor: Color? = null,
        val shape: LogoShape = LogoShape.CIRCLE,
    ) {
        init {
            require(relativeSize in .1..1.0) { "relativeSize must be in between 0.1 and 1." }
            require(logo != null || base64Logo != null) { "Either logo or base64Logo must be set." }
        }
    }

/**
 * Specify the shape of the logo.
 */
enum class LogoShape {
    /**
     * The logo will be rendered as a circle.
     **/
    CIRCLE,

    /**
     * The logo will be rendered as an ellipse.
     */
    ELLIPSE,

    /**
     * The logo will be rendered as a square.
     */
    SQUARE,

    /**
     * The logo will be rendered as the original shape.
     */
    ORIGINAL,
}

/**
 * Specify the colors in the qr code.
 *
 * Be cautions with this and remember to choose a high contrast between the colors to ensure a proper recognition of the qr code.
 *
 * @param bgColor - specify the background color of the qr code - may also be transparent, defaults to Color.WHITE
 * @param fillColor - specify the fill or foreground color of the qr code - should not be transparent, defaults to Color.BLACK
 */
class QrCodeColorConfig
    @JvmOverloads
    constructor(
        val bgColor: Color = Color.WHITE,
        val fillColor: Color = Color.BLACK,
    )

/**
 * Specify the colors and shapes of the positional squares of the qr code.
 *
 * @param outerBorderColor - specify the outer border color, defaults to Color.WHITE
 * @param outerSquareColor - specify the outer square color, defaults to Color.BLACK
 * @param innerSquareColor - specify the inner square color, defaults to Color.WHITE
 * @param centerColor - specify the color of the center of the positional squares, defaults to Color.BLACK
 * @param isCircleShaped - specify whether the shape shall be a circle or a square, defaults to false
 * @param relativeSquareBorderRound - in case isCircleShaped==false the borders of the square may be round at the edges, defaults to 0.05
 */
class QrPositionalSquaresConfig
    @JvmOverloads
    constructor(
        val isCircleShaped: Boolean = false,
        val relativeSquareBorderRound: Double = .0,
        val centerColor: Color = Color.BLACK,
        val innerSquareColor: Color = Color.WHITE,
        val outerSquareColor: Color = Color.BLACK,
        val outerBorderColor: Color = Color.WHITE,
    ) {
        init {
            require(relativeSquareBorderRound in 0.0..1.0) { "relativeSquareBorderRound must be in between 0 and 1." }
        }

        class Builder {
            private var isCircleShaped: Boolean = false
            private var relativeSquareBorderRound: Double = .0
            private var centerColor: Color = Color.BLACK
            private var innerSquareColor: Color = Color.WHITE
            private var outerSquareColor: Color = Color.BLACK
            private var outerBorderColor: Color = Color.WHITE

            fun circleShaped(isCircleShaped: Boolean) = apply { this.isCircleShaped = isCircleShaped }

            fun relativeSquareBorderRound(relativeSquareBorderRound: Double) =
                apply { this.relativeSquareBorderRound = relativeSquareBorderRound }

            fun centerColor(centerColor: Color) = apply { this.centerColor = centerColor }

            fun innerSquareColor(innerSquareColor: Color) = apply { this.innerSquareColor = innerSquareColor }

            fun outerSquareColor(outerSquareColor: Color) = apply { this.outerSquareColor = outerSquareColor }

            fun outerBorderColor(outerBorderColor: Color) = apply { this.outerBorderColor = outerBorderColor }

            fun build() =
                QrPositionalSquaresConfig(
                    isCircleShaped = isCircleShaped,
                    relativeSquareBorderRound = relativeSquareBorderRound,
                    centerColor = centerColor,
                    innerSquareColor = innerSquareColor,
                    outerSquareColor = outerSquareColor,
                    outerBorderColor = outerBorderColor,
                )
        }
    }

/**
 * Specify color and relative size of the border.
 *
 * @param color - color of the border
 * @param relativeSize - relative size of the border, defaults to 0.05
 * @param relativeBorderRound - relative border round, defaults to 0.2
 */
class QrBorderConfig
    @JvmOverloads
    constructor(
        val color: Color = Color.BLACK,
        val relativeSize: Double = .05,
        val relativeBorderRound: Double = 0.2,
    ) {
        init {
            require(relativeSize in .0..1.0) { "relativeSize must be in between 0.01 and 1." }
            require(relativeBorderRound in .0..1.0) { "relativeSize must be in between 0.01 and 1." }
        }
    }
