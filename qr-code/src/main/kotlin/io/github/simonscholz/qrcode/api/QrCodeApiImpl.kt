package io.github.simonscholz.qrcode.api

import io.github.simonscholz.qrcode.QrCodeApi
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.border.BorderGraphics
import io.github.simonscholz.qrcode.logo.LogoGraphics
import io.github.simonscholz.qrcode.qr.QrCodeCreator
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import kotlin.math.floor

internal class QrCodeApiImpl : QrCodeApi {
    override fun createQrImage(qrCodeConfig: QrCodeConfig): BufferedImage {
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
            val qrCode = qrCodeCreator.createQrImageWithPositionals(
                qrCodeText = qrCodeConfig.qrCodeText,
                size = qrCodeConfig.qrCodeSize,
                circularPositionals = qrCodeConfig.qrPositionalSquaresConfig.isCircleShaped,
                relativePositionalsRound = qrCodeConfig.qrPositionalSquaresConfig.relativeSquareBorderRound,
                fillColor = qrCodeConfig.qrCodeColorConfig.fillColor,
                bgColor = qrCodeConfig.qrCodeColorConfig.bgColor,
                internalCircleColor = qrCodeConfig.qrPositionalSquaresConfig.centerColor,
                quietZone = qrCodeConfig.qrBorderConfig?.let { 2 } ?: 0, // have a quietZone if we have a border
                borderWidth = qrCodeConfig.qrBorderConfig?.let { relativeSize(qrCodeConfig.qrCodeSize, it.relativeSize) } ?: 0,
                relativeBorderRound = qrCodeConfig.qrBorderConfig?.relativeBorderRound ?: .0,
            )

            graphics.drawImage(qrCode, 0, 0, null)

            qrCodeConfig.qrLogoConfig?.let {
                LogoGraphics.drawLogo(graphics, it.logo, qrCodeConfig.qrCodeSize, it.relativeSize)
            }
            image
        } finally {
            graphics.dispose()
        }
    }

    private fun relativeSize(size: Int, percentage: Double): Int {
        require(percentage in 0.0..1.0)
        return floor(size * percentage).toInt()
    }
}
