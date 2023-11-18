package io.github.simonscholz.service

import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig
import io.github.simonscholz.ui.ImageUI
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter

class ImageService(private val qrCodeConfigViewModel: QrCodeConfigViewModel, private val alreadyAppliedOnceDelegate: () -> Boolean) {
    fun renderImage(): BufferedImage {
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
                    val scaledLogo = getScaledLogo(it, qrCodeConfigViewModel)

                    builder.qrLogoConfig(
                        logo = scaledLogo,
                        relativeSize = qrCodeConfigViewModel.logoRelativeSize.value,
                        bgColor = qrCodeConfigViewModel.logoBackgroundColor.value,
                        shape = qrCodeConfigViewModel.logoShape.value,
                    )
                }
            }.onFailure { _ ->
                JOptionPane.showMessageDialog(null, "You did not select a proper image", "Image Loading Error", JOptionPane.ERROR_MESSAGE)
            }
        }
        val qrCodeConfig = builder.build()
        return QrCodeFactory.createQrCodeApi().createQrCodeImage(qrCodeConfig)
    }

    private fun getScaledLogo(logo: Image, qrCodeConfigViewModel: QrCodeConfigViewModel): Image {
        val maxLogoSize = (qrCodeConfigViewModel.size.value * qrCodeConfigViewModel.logoRelativeSize.value).toInt()
        if(logo.getWidth(null) <= maxLogoSize && logo.getHeight(null) <= maxLogoSize) {
            return logo
        }

        if(logo.getWidth(null) > logo.getHeight(null)) {
            val ratio = logo.getHeight(null).toDouble() / logo.getWidth(null).toDouble()
            return logo.getScaledInstance(maxLogoSize, (maxLogoSize*ratio).toInt(), Image.SCALE_SMOOTH)
        }

        val ratio = logo.getWidth(null).toDouble() / logo.getHeight(null).toDouble()
        return logo.getScaledInstance((maxLogoSize*ratio).toInt(), maxLogoSize, Image.SCALE_SMOOTH)
    }

    fun saveFile() {
        val fileChooser = JFileChooser()
        fileChooser.fileFilter = FileNameExtensionFilter("Png Image Files (*.png)", "png")
        val result = fileChooser.showSaveDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            if (fileChooser.selectedFile.extension.endsWith("png")) {
                val selectedFile = fileChooser.selectedFile
                val qrCodeImage = if (alreadyAppliedOnceDelegate()) {
                    renderImage()
                } else {
                    renderInitialImage()
                }
                ImageIO.write(qrCodeImage, "png", selectedFile)
            } else {
                JOptionPane.showMessageDialog(null, "The file to be saved must have the png extension", "Image Saving Error", JOptionPane.ERROR_MESSAGE)
            }
        }
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
