package io.github.simonscholz.qrcode.api

import io.github.simonscholz.qrcode.QrCodeApi
import io.github.simonscholz.qrcode.border.BorderGraphics
import io.github.simonscholz.qrcode.logo.LogoGraphics
import io.github.simonscholz.qrcode.qr.QrCodeCreator
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage


internal class QrCodeApiImpl : QrCodeApi {
    override fun createQrImage(
        qrCodeText: String,
        size: Int,
        circularPositionals: Boolean,
        relativePositionalsRound: Double,
        fillColor: Color?,
        bgColor: Color,
        internalCircleColor: Color,
        quiteZone: Int,
        logo: BufferedImage?,
    ): BufferedImage {
        val image = BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR)
        val graphics = image.graphics as Graphics2D
        return try {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

            BorderGraphics.drawBorder(
                graphics = graphics,
                borderColor = Color.BLACK,
                bgColor = bgColor,
                size = size,
                relativeBorderRound = .2,
                relativeBorderSize = .05
            )

            val qrCodeCreator = QrCodeCreator()
            val qrCode = qrCodeCreator.createQrImageWithPositionals(
                qrCodeText = qrCodeText,
                size = size,
                circularPositionals = circularPositionals,
                relativePositionalsRound = relativePositionalsRound,
                fillColor = fillColor,
                bgColor = bgColor,
                internalCircleColor = internalCircleColor,
                quiteZone = quiteZone
            )

            graphics.drawImage(qrCode, 2, 2, null)

            logo?.let {
                LogoGraphics.drawLogo(graphics, it, size, .2)

            }
            image
        } finally {
            graphics.dispose()
        }
    }
}
