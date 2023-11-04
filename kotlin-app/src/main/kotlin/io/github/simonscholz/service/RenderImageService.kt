package io.github.simonscholz.service

import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig
import io.github.simonscholz.ui.ImageUI
import java.awt.Color
import java.awt.Component
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JOptionPane

object RenderImageService {
    fun renderImage(qrCodeConfigViewModel: QrCodeConfigViewModel, component: Component): BufferedImage {
        val builder = QrCodeConfig.Builder(qrCodeConfigViewModel.qrCodeContent.value)
            .qrCodeSize(qrCodeConfigViewModel.size.value)
            .qrCodeColorConfig(
                bgColor = qrCodeConfigViewModel.backgroundColor.value,
                fillColor = qrCodeConfigViewModel.foregroundColor.value,
            )
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
        if (qrCodeConfigViewModel.logo.value.isNotBlank() && File(qrCodeConfigViewModel.logo.value).exists()) {
            runCatching {
                ImageIO.read(File(qrCodeConfigViewModel.logo.value)).let {
                    builder.qrLogoConfig(
                        logo = it,
                        relativeSize = qrCodeConfigViewModel.logoRelativeSize.value,
                        bgColor = qrCodeConfigViewModel.logoBackgroundColor.value,
                    )
                }
            }.onFailure { _ ->
                JOptionPane.showMessageDialog(component, "You did not select a proper image", "Image Loading Error", JOptionPane.ERROR_MESSAGE)
            }
        }
        val qrCodeConfig = builder.build()
        return QrCodeFactory.createQrCodeApi().createQrCodeImage(qrCodeConfig)
    }

    fun renderInitialImage(): BufferedImage {
        val resource = ImageUI::class.java.getClassLoader().getResource("avatar-60x.png")
        val logo = ImageIO.read(resource)
        val qrCodeConfig = QrCodeConfig.Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(
                QrPositionalSquaresConfig(
                    isCircleShaped = true,
                    relativeSquareBorderRound = .2,
                    centerColor = Color.RED,
                ),
            )
            .build()
        return QrCodeFactory.createQrCodeApi().createQrCodeImage(qrCodeConfig)
    }
}
