package io.github.simonscholz.ui

import io.github.simonscholz.service.ImageService
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
    fun createImagePanel(
        imageService: ImageService,
        fileUI: FileUI,
    ): Pair<JPanel, (BufferedImage) -> Unit> {
        val imageContainer = JPanel(MigLayout("", "[center]"))
        imageContainer.background = Color.WHITE

        val image = imageService.renderImage()

        val imageDrawPanel =
            ImagePanel().apply {
                setImage(image)
            }
        imageContainer.add(imageDrawPanel, "wrap")
        createPopupMenu(fileUI, imageDrawPanel)

        val setImage = (imageDrawPanel::setImage as (BufferedImage) -> Unit)

        imageContainer.add(JLabel("This image is just a preview of the actual qr code."), "wrap")
        imageContainer.add(JLabel("Caution! Certain color combinations won't work!"), "wrap")

        return Pair(imageContainer, setImage)
    }

    private fun createPopupMenu(
        fileUI: FileUI,
        imagePanel: ImagePanel,
    ) {
        val contextMenu = JPopupMenu()

        contextMenu.add(saveQrCodeImageMenuItem(fileUI))
        contextMenu.add(saveSvgQrCodeImageMenuItem(fileUI))
        contextMenu.add(copyQrCodeImageMenuItem(fileUI))
        contextMenu.add(copyBase64QrCodeImageMenuItem(fileUI))
        contextMenu.add(copySvgQrCodeImageMenuItem(fileUI))

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

    private fun saveQrCodeImageMenuItem(fileUI: FileUI): JMenuItem {
        val saveImageMenuItem = JMenuItem("Save Qr Code Image")
        // Add the keybinding for Save (Ctrl + S)
        saveImageMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)
        saveImageMenuItem.addActionListener {
            fileUI.saveQrCodeImageFile()
        }
        return saveImageMenuItem
    }

    private fun saveSvgQrCodeImageMenuItem(fileUI: FileUI): JMenuItem {
        val saveSvgImageMenuItem = JMenuItem("Save Qr Code Image as SVG")
        // Add the keybinding for Save (Ctrl + K)
        saveSvgImageMenuItem.addActionListener {
            fileUI.saveQrCodeSvgFile()
        }
        return saveSvgImageMenuItem
    }

    private fun copyQrCodeImageMenuItem(fileUI: FileUI): JMenuItem {
        val copyImageMenuItem = JMenuItem("Copy Qr Code Image to clipboard")
        // Add the keybinding for Save (Ctrl + C)
        copyImageMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK)
        copyImageMenuItem.addActionListener {
            fileUI.copyImageToClipboard()
        }
        return copyImageMenuItem
    }

    private fun copyBase64QrCodeImageMenuItem(fileUI: FileUI): JMenuItem {
        val copyImageMenuItem = JMenuItem("Copy Base64 Qr Code Image to clipboard")
        // Add the keybinding for Save (Ctrl + B)
        copyImageMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK)
        copyImageMenuItem.addActionListener {
            fileUI.copyBase64ImageToClipboard()
        }
        return copyImageMenuItem
    }

    private fun copySvgQrCodeImageMenuItem(fileUI: FileUI): JMenuItem {
        val copySvgImageMenuItem = JMenuItem("Copy Svg Qr Code Image to clipboard")
        // Add the keybinding for Save (Ctrl + C)
        copySvgImageMenuItem.addActionListener {
            fileUI.copySvgImageToClipboard()
        }
        return copySvgImageMenuItem
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
