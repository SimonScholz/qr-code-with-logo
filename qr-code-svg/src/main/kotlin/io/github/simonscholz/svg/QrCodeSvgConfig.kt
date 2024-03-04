package io.github.simonscholz.svg

import io.github.simonscholz.qrcode.DEFAULT_IMG_SIZE
import io.github.simonscholz.qrcode.LogoShape
import io.github.simonscholz.qrcode.QrBorderConfig
import io.github.simonscholz.qrcode.QrCodeColorConfig
import io.github.simonscholz.qrcode.QrCodeDotShape
import io.github.simonscholz.qrcode.QrCodeDotStyler
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig
import org.w3c.dom.Document
import java.awt.Color
import java.awt.Image

/**
 * Configuration for the qr code svg generation.
 *
 * @param qrCodeText - text to be encoded in the qr code
 * @param qrCodeSize - size of the qr code, defaults to [DEFAULT_IMG_SIZE]
 * @param qrLogoConfig - configuration for the logo in the qr code, defaults to null
 * @param qrCodeColorConfig - configuration for the qr code colors, defaults to [QrCodeColorConfig]
 * @param qrPositionalSquaresConfig - configuration for the positional squares, defaults to [QrPositionalSquaresConfig]
 * @param qrCodeDotStyler - configuration for the qr code dot styler, defaults to [QrCodeDotShape.SQUARE]
 * @param qrBorderConfig - configuration for the qr code border, defaults to null
 */
class QrCodeSvgConfig
    @JvmOverloads
    constructor(
        val qrCodeText: String,
        val qrCodeSize: Int = DEFAULT_IMG_SIZE,
        val qrLogoConfig: QrSvgLogoConfig? = null,
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
            private var qrLogoConfig: QrSvgLogoConfig? = null
            private var qrCodeColorConfig: QrCodeColorConfig = QrCodeColorConfig()
            private var qrPositionalSquaresConfig: QrPositionalSquaresConfig = QrPositionalSquaresConfig()
            private var qrBorderConfig: QrBorderConfig? = null
            private var qrCodeDotStyler: QrCodeDotStyler = QrCodeDotShape.SQUARE

            fun qrCodeSize(qrCodeSize: Int) = apply { this.qrCodeSize = qrCodeSize }

            @JvmOverloads fun qrLogoConfig(
                logo: Document,
                relativeSize: Double = .2,
                bgColor: Color? = null,
                shape: LogoShape = LogoShape.CIRCLE,
            ) = apply {
                this.qrLogoConfig =
                    QrSvgLogoConfig(
                        logo = null,
                        base64Logo = null,
                        svgLogoDocument = logo,
                        relativeSize = relativeSize,
                        bgColor = bgColor,
                        shape = shape,
                    )
            }

            @JvmOverloads fun qrLogoConfig(
                logo: Image,
                relativeSize: Double = .2,
                bgColor: Color? = null,
                shape: LogoShape = LogoShape.CIRCLE,
            ) = apply {
                this.qrLogoConfig =
                    QrSvgLogoConfig(
                        logo = logo,
                        relativeSize = relativeSize,
                        bgColor = bgColor,
                        shape = shape,
                    )
            }

            @JvmOverloads fun qrLogoConfig(
                base64Logo: String,
                relativeSize: Double = .2,
                bgColor: Color? = null,
                shape: LogoShape = LogoShape.CIRCLE,
            ) = apply {
                this.qrLogoConfig =
                    QrSvgLogoConfig(
                        base64Logo = base64Logo,
                        relativeSize = relativeSize,
                        bgColor = bgColor,
                        shape = shape,
                    )
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
                QrCodeSvgConfig(
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
 * Pass a logo as BufferedImage or base64 encoded image or svg document to be rendered in the center of the qr code,
 * Specify the relativeSize of the logo in the qr code and choose the logo shape.
 * The svgLogoDocument takes precedence over the logo and base64Logo, when you'd like to create a svg document.
 *
 * @param logo - [Image] to be rendered as logo in the center of the qr code
 * @param base64Logo - base64 encoded image to be rendered as logo in the center of the qr code
 * @param svgLogoDocument - svg document to be rendered as logo in the center of the qr code
 * @param relativeSize - relative size of the logo, defaults to 0.2
 * @param bgColor - specify the background color of the logo, defaults to null
 * @param shape - specify the shape of the logo, defaults to [LogoShape.CIRCLE]
 */
class QrSvgLogoConfig
    @JvmOverloads
    constructor(
        val logo: Image? = null,
        val base64Logo: String? = null,
        val svgLogoDocument: Document? = null,
        val relativeSize: Double = .2,
        val bgColor: Color? = null,
        val shape: LogoShape = LogoShape.CIRCLE,
    ) {
        init {
            require(relativeSize in .1..1.0) { "relativeSize must be in between 0.1 and 1." }
            require(
                logo != null || base64Logo != null || svgLogoDocument != null,
            ) { "Either logo or base64Logo or svgLogoDocument must be set." }
        }
    }
