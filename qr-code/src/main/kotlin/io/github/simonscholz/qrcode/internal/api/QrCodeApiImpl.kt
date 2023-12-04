package io.github.simonscholz.qrcode.internal.api

import io.github.simonscholz.qrcode.QrCodeApi
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.imageFromBase64
import io.github.simonscholz.qrcode.internal.border.BorderGraphics
import io.github.simonscholz.qrcode.internal.logo.LogoGraphics
import io.github.simonscholz.qrcode.internal.qr.QrCodeCreator
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import kotlin.math.floor

internal class QrCodeApiImpl : QrCodeApi {
    override fun createQrCodeImage(qrCodeConfig: QrCodeConfig): BufferedImage {
        val image = BufferedImage(qrCodeConfig.qrCodeSize, qrCodeConfig.qrCodeSize, BufferedImage.TYPE_4BYTE_ABGR)
        val graphics = image.graphics as Graphics2D
        return try {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

            qrCodeConfig.qrBorderConfig?.let {
                BorderGraphics.drawBorder(
                    graphics = graphics,
                    borderColor = it.color,
                    size = qrCodeConfig.qrCodeSize,
                    relativeBorderRound = it.relativeBorderRound,
                    borderWidth = relativeSize(qrCodeConfig.qrCodeSize, it.relativeSize),
                )
            }

            val qrCodeCreator = QrCodeCreator()
            val qrCode =
                qrCodeCreator.createQrImageWithPositionals(
                    qrCodeText = qrCodeConfig.qrCodeText,
                    size = qrCodeConfig.qrCodeSize,
                    circularPositionals = qrCodeConfig.qrPositionalSquaresConfig.isCircleShaped,
                    relativePositionalsRound = qrCodeConfig.qrPositionalSquaresConfig.relativeSquareBorderRound,
                    fillColor = qrCodeConfig.qrCodeColorConfig.fillColor,
                    bgColor = qrCodeConfig.qrCodeColorConfig.bgColor,
                    outerBorderColor = qrCodeConfig.qrPositionalSquaresConfig.outerBorderColor,
                    outerSquareColor = qrCodeConfig.qrPositionalSquaresConfig.outerSquareColor,
                    innerSquareColor = qrCodeConfig.qrPositionalSquaresConfig.innerSquareColor,
                    centerColor = qrCodeConfig.qrPositionalSquaresConfig.centerColor,
                    quietZone = qrCodeConfig.qrBorderConfig?.let { 1 } ?: 0, // have a quietZone if we have a border
                    borderWidth = qrCodeConfig.qrBorderConfig?.let { relativeSize(qrCodeConfig.qrCodeSize, it.relativeSize) } ?: 0,
                    relativeBorderRound = qrCodeConfig.qrBorderConfig?.relativeBorderRound ?: .0,
                    customDotStyler = qrCodeConfig.qrCodeDotStyler::createDot,
                )

            graphics.drawImage(qrCode, 0, 0, null)

            qrCodeConfig.qrLogoConfig?.also { qrCodeLogoConfig ->
                if (qrCodeLogoConfig.base64Logo != null) {
                    runCatching {
                        qrCodeLogoConfig.base64Logo.imageFromBase64()
                    }.onSuccess {
                        LogoGraphics.drawLogo(
                            graphics,
                            qrCodeConfig.qrCodeSize,
                            it,
                            qrCodeLogoConfig.relativeSize,
                            qrCodeLogoConfig.bgColor,
                            qrCodeLogoConfig.shape,
                        )
                    }
                } else if (qrCodeLogoConfig.logo != null) {
                    LogoGraphics.drawLogo(
                        graphics,
                        qrCodeConfig.qrCodeSize,
                        qrCodeLogoConfig.logo,
                        qrCodeLogoConfig.relativeSize,
                        qrCodeLogoConfig.bgColor,
                        qrCodeLogoConfig.shape,
                    )
                }
            }
            image
        } finally {
            graphics.dispose()
        }
    }

    private fun relativeSize(
        size: Int,
        percentage: Double,
    ): Int {
        require(percentage in 0.0..1.0)
        return floor(size * percentage).toInt()
    }
}
