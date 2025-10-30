package io.github.simonscholz.qrcode.internal.api

import io.github.simonscholz.qrcode.QrCodeApi
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrLogoConfig
import io.github.simonscholz.qrcode.imageFromBase64
import io.github.simonscholz.qrcode.internal.LoggerFactory
import io.github.simonscholz.qrcode.internal.border.BorderGraphics
import io.github.simonscholz.qrcode.internal.logo.LogoGraphics
import io.github.simonscholz.qrcode.internal.qr.QrCodeCreator
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import kotlin.math.floor

private val logger = LoggerFactory.forClass<QrCodeApiImpl>()

internal class QrCodeApiImpl :
    QrCodeApi,
    InternalDrawQrCode {
    override fun createQrCodeImage(qrCodeConfig: QrCodeConfig): BufferedImage {
        val image = BufferedImage(qrCodeConfig.qrCodeSize, qrCodeConfig.qrCodeSize, BufferedImage.TYPE_4BYTE_ABGR)
        val graphics = image.graphics as Graphics2D

        try {
            drawQrCodeOnGraphics2D(qrCodeConfig, graphics)
        } finally {
            graphics.dispose()
        }

        return image
    }

    override fun drawQrCodeOnGraphics2D(
        qrCodeConfig: QrCodeConfig,
        graphics: Graphics2D,
    ) {
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
        // have a quietZone if we have a border
        qrCodeCreator.drawQrCodeWithPositionals(
            graphics = graphics,
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
            quietZone = qrCodeConfig.qrBorderConfig?.let { 1 } ?: 0,
            borderWidth = qrCodeConfig.qrBorderConfig?.let { relativeSize(qrCodeConfig.qrCodeSize, it.relativeSize) } ?: 0,
            relativeBorderRound = qrCodeConfig.qrBorderConfig?.relativeBorderRound ?: .0,
            customDotStyler = qrCodeConfig.qrCodeDotStyler::createDot,
            colorAdjustmentPatterns = qrCodeConfig.qrPositionalSquaresConfig.colorAdjustmentPatterns,
        )

        qrCodeConfig.qrLogoConfig?.let { logoConfig ->
            when (logoConfig) {
                is QrLogoConfig.Base64Image -> {
                    runCatching {
                        logoConfig.base64Image.imageFromBase64()
                    }.onSuccess { decodedImage ->
                        LogoGraphics.drawLogo(
                            graphics,
                            qrCodeConfig.qrCodeSize,
                            decodedImage,
                            logoConfig.relativeSize,
                            logoConfig.bgColor,
                            logoConfig.shape,
                        )
                    }.onFailure {
                        logger.severe {
                            "Failed to decode base64 image for logo: ${logoConfig.base64Image}, error: ${it.message}"
                        }
                    }
                }
                is QrLogoConfig.Bitmap -> {
                    LogoGraphics.drawLogo(
                        graphics,
                        qrCodeConfig.qrCodeSize,
                        logoConfig.image,
                        logoConfig.relativeSize,
                        logoConfig.bgColor,
                        logoConfig.shape,
                    )
                }
            }
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
