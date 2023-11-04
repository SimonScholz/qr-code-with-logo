package io.github.simonscholz.ui

import io.github.simonscholz.service.RenderImageService.renderInitialImage
import net.miginfocom.swing.MigLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JLabel
import javax.swing.JPanel

object ImageUI {
    fun createImagePanel(): Pair<JPanel, (BufferedImage) -> Unit> {
        val imageContainer = JPanel(MigLayout("", "[center]"))
        imageContainer.background = Color.WHITE

        val image = renderInitialImage()

        val imageDrawPanel = ImagePanel().apply {
            setImage(image)
        }
        imageContainer.add(imageDrawPanel, "wrap")

        val setImage = (imageDrawPanel::setImage as (BufferedImage) -> Unit)

        imageContainer.add(JLabel("This image is just a preview of the actual qr code."), "wrap")

        return Pair(imageContainer, setImage)
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
                g.color = this.parent.background
                g.fillRect(0, 0, width, height)
                g.drawImage(it, 0, 0, width, height, null)
            }
        }
    }
}
