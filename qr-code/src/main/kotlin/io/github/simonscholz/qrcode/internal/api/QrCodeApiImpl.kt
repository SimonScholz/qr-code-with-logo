package io.github.simonscholz.qrcode.internal.api

import io.github.simonscholz.qrcode.QrCodeApi
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.imageFromBase64
import io.github.simonscholz.qrcode.internal.border.BorderGraphics
import io.github.simonscholz.qrcode.internal.logo.LogoGraphics
import io.github.simonscholz.qrcode.internal.qr.QrCodeCreator
import io.github.simonscholz.qrcode.spi.Graphics2DDelegate
import io.github.simonscholz.qrcode.spi.Graphics2DSpi
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.Writer
import java.util.ServiceLoader
import javax.imageio.ImageIO
import kotlin.math.floor

internal class QrCodeApiImpl : QrCodeApi {
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

    override fun writeQrCode(
        qrCodeConfig: QrCodeConfig,
        writer: Writer,
        format: String,
    ) {
        when (format) {
            QrCodeApi.FORMAT_BUFFERED_IMAGE -> writeQrCodeAsBufferedImage(qrCodeConfig, writer)
            QrCodeApi.FORMAT_BASE64 -> writeQrCodeAsBase64(qrCodeConfig, writer)
            else -> writeQrCodeUsingSpi(qrCodeConfig, writer, format)
        }
    }

    private fun writeQrCodeAsBufferedImage(
        qrCodeConfig: QrCodeConfig,
        writer: Writer,
    ) {
        ImageIO.createImageOutputStream(writer).use { imageOutputStream ->
            ImageIO.write(createQrCodeImage(qrCodeConfig), "png", imageOutputStream)
        }
    }

    private fun writeQrCodeAsBase64(
        qrCodeConfig: QrCodeConfig,
        writer: Writer,
    ) {
        createBase64QrCodeImage(qrCodeConfig).let {
            writer.write(it)
        }
    }

    private fun writeQrCodeUsingSpi(
        qrCodeConfig: QrCodeConfig,
        writer: Writer,
        format: String,
    ) {
        ServiceLoader.load(Graphics2DSpi::class.java).forEach {
            if (it.supportsFormat(format)) {
                it.createQrCode(
                    object : Graphics2DDelegate {
                        override fun drawQrCode(graphics: Graphics2D) {
                            drawQrCodeOnGraphics2D(qrCodeConfig, graphics)
                        }
                    },
                    writer,
                )
            }
        }
        throw IllegalStateException("No QrCodeWritingSpi found for format: $format")
    }

    private fun drawQrCodeOnGraphics2D(
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
        )

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
    }

    private fun relativeSize(
        size: Int,
        percentage: Double,
    ): Int {
        require(percentage in 0.0..1.0)
        return floor(size * percentage).toInt()
    }
}
