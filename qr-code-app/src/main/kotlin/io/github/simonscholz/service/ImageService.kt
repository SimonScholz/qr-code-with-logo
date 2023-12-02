package io.github.simonscholz.service

import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig
import java.awt.Image
import java.awt.image.BufferedImage

class ImageService(private val qrCodeConfigViewModel: QrCodeConfigViewModel) {

    fun renderImage(): BufferedImage {
        val builder = QrCodeConfig.Builder(qrCodeConfigViewModel.qrCodeContent.value)
            .qrCodeSize(qrCodeConfigViewModel.size.value)
            .qrCodeColorConfig(
                bgColor = qrCodeConfigViewModel.backgroundColor.value,
                fillColor = qrCodeConfigViewModel.foregroundColor.value,
            )
            .qrCodeDotStyler(qrCodeConfigViewModel.dotShape.value)
            .qrBorderConfig(
                color = qrCodeConfigViewModel.borderColor.value,
                relativeSize = qrCodeConfigViewModel.relativeBorderSize.value,
                relativeBorderRound = qrCodeConfigViewModel.borderRadius.value,
            )
            .qrPositionalSquaresConfig(
                qrPositionalSquaresConfig = QrPositionalSquaresConfig(
                    isCircleShaped = qrCodeConfigViewModel.positionalSquareIsCircleShaped.value,
                    relativeSquareBorderRound = qrCodeConfigViewModel.positionalSquareRelativeBorderRound.value,
                    centerColor = qrCodeConfigViewModel.positionalSquareCenterColor.value,
                    innerSquareColor = qrCodeConfigViewModel.positionalSquareInnerSquareColor.value,
                    outerSquareColor = qrCodeConfigViewModel.positionalSquareOuterSquareColor.value,
                    outerBorderColor = qrCodeConfigViewModel.positionalSquareOuterBorderColor.value,
                ),
            )
        if (qrCodeConfigViewModel.logoBase64.value.isNotBlank()) {
            builder.qrLogoConfig(
                base64Logo = qrCodeConfigViewModel.logoBase64.value,
                relativeSize = qrCodeConfigViewModel.logoRelativeSize.value,
                bgColor = qrCodeConfigViewModel.logoBackgroundColor.value,
                shape = qrCodeConfigViewModel.logoShape.value,
            )
        }
        val qrCodeConfig = builder.build()
        return QrCodeFactory.createQrCodeApi().createQrCodeImage(qrCodeConfig)
    }

    private fun getScaledLogo(logo: Image, qrCodeConfigViewModel: QrCodeConfigViewModel): Image {
        val maxLogoSize = (qrCodeConfigViewModel.size.value * qrCodeConfigViewModel.logoRelativeSize.value).toInt()
        if (logo.getWidth(null) <= maxLogoSize && logo.getHeight(null) <= maxLogoSize) {
            return logo
        }

        if (logo.getWidth(null) > logo.getHeight(null)) {
            val ratio = logo.getHeight(null).toDouble() / logo.getWidth(null).toDouble()
            return logo.getScaledInstance(maxLogoSize, (maxLogoSize * ratio).toInt(), Image.SCALE_SMOOTH)
        }

        val ratio = logo.getWidth(null).toDouble() / logo.getHeight(null).toDouble()
        return logo.getScaledInstance((maxLogoSize * ratio).toInt(), maxLogoSize, Image.SCALE_SMOOTH)
    }
}
