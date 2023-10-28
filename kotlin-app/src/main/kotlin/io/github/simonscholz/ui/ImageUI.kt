package io.github.simonscholz.ui

import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.JPanel


object ImageUI {
    fun createImagePanel(): JPanel {
        val imageContainer = JPanel()
        imageContainer.background = Color.WHITE

        val image = decentRedColor()

        val imageDrawPanel = ImagePanel().apply {
            setImage(image)
        }
        imageContainer.add(imageDrawPanel)

        return imageContainer
    }


    internal class ImagePanel : JPanel(true) {
        private var image: BufferedImage? = null

        fun setImage(bufferedImage: BufferedImage) {
            this.image = bufferedImage
            this.preferredSize = Dimension(bufferedImage.width, bufferedImage.height)
            this.repaint()
        }

        override fun paintComponent(g: Graphics) {
            image?.let {
                g.drawImage(it, 0, 0, width, height, null)
            }
        }
    }


    private fun decentRedColor(): BufferedImage {
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
