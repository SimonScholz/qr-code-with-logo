package io.github.simonscholz.svg

import io.github.simonscholz.qrcode.LogoShape
import io.github.simonscholz.qrcode.QrLogoConfig
import org.w3c.dom.Document
import java.awt.Color

/**
 * Configuration for a logo in an SVG QR code.
 *
 * @property svgLogo The SVG document representing the logo.
 * @property relativeSize The size of the logo relative to the QR code, must be between 0.1 and 1.0.
 * @property bgColor Optional background color for the logo.
 * @property shape The shape of the logo, defaults to [LogoShape.CIRCLE].
 */
class QrSvgLogoConfig
    @JvmOverloads
    constructor(
        val svgLogo: Document,
        override val relativeSize: Double = 0.2,
        override val bgColor: Color? = null,
        override val shape: LogoShape = LogoShape.CIRCLE,
    ) : QrLogoConfig {
        init {
            require(relativeSize in 0.1..1.0) { "relativeSize must be in between 0.1 and 1." }
        }
    }
