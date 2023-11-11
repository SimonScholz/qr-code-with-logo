package io.github.simonscholz.ui

import io.github.simonscholz.service.ImageService
import io.github.simonscholz.service.RenderImageService.renderInitialImage
import net.miginfocom.swing.MigLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.JLabel
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.KeyStroke

object ImageUI {
    fun createImagePanel(imageService: ImageService): Pair<JPanel, (BufferedImage) -> Unit> {
        val imageContainer = JPanel(MigLayout("", "[center]"))
        imageContainer.background = Color.WHITE

        val image = imageService.renderInitialImage()

        val imageDrawPanel = ImagePanel().apply {
            setImage(image)
        }
        imageContainer.add(imageDrawPanel, "wrap")
        createPopupMenu(imageService, imageDrawPanel)

        val setImage = (imageDrawPanel::setImage as (BufferedImage) -> Unit)

        imageContainer.add(JLabel("This image is just a preview of the actual qr code."), "wrap")

        return Pair(imageContainer, setImage)
    }

    private fun createPopupMenu(imageService: ImageService, imagePanel: ImagePanel) {
        val saveImageMenuItem = JMenuItem("Save Qr Code Image")
        // Add the keybinding for Save (Ctrl + S)
        saveImageMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)
        saveImageMenuItem.addActionListener {
            imageService.saveFile()
        }

        val contextMenu = JPopupMenu()
        contextMenu.add(saveImageMenuItem)

        imagePanel.addMouseListener(
            object : MouseAdapter() {
                override fun mousePressed(e: MouseEvent) {
                    maybeShowPopup(e)
                }

                override fun mouseReleased(e: MouseEvent) {
                    maybeShowPopup(e)
                }

                private fun maybeShowPopup(e: MouseEvent) {
                    if (e.isPopupTrigger) {
                        contextMenu.show(e.component, e.x, e.y)
                    }
                }
            },
        )
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
