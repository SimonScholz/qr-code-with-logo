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
        contextMenu.add(copyQrCodeImageMenuItem(fileUI))
        contextMenu.add(copyBase64QrCodeImageMenuItem(fileUI))
        contextMenu.add(saveKotlinCodeMenuItem(fileUI))
        contextMenu.add(saveSvgKotlinCodeMenuItem(fileUI))
        contextMenu.add(saveJavaCodeMenuItem(fileUI))
        contextMenu.add(saveSvgJavaCodeMenuItem(fileUI))

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

    private fun saveKotlinCodeMenuItem(fileUI: FileUI): JMenuItem {
        val saveImageMenuItem = JMenuItem("Copy Kotlin code to clipboard")
        // Add the keybinding for Save (Ctrl + K)
        saveImageMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK)
        saveImageMenuItem.addActionListener {
            fileUI.copyKotlinCodeToClipboard()
        }
        return saveImageMenuItem
    }

    private fun saveSvgKotlinCodeMenuItem(fileUI: FileUI): JMenuItem {
        val saveImageMenuItem = JMenuItem("Copy SVG Kotlin code to clipboard")
        // Add the keybinding for Save (Ctrl + K)
        saveImageMenuItem.addActionListener {
            fileUI.copySvgKotlinCodeToClipboard()
        }
        return saveImageMenuItem
    }

    private fun saveJavaCodeMenuItem(fileUI: FileUI): JMenuItem {
        val saveImageMenuItem = JMenuItem("Copy Java code to clipboard")
        // Add the keybinding for Save (Ctrl + J)
        saveImageMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_DOWN_MASK)
        saveImageMenuItem.addActionListener {
            fileUI.copyJavaCodeToClipboard()
        }
        return saveImageMenuItem
    }

    private fun saveSvgJavaCodeMenuItem(fileUI: FileUI): JMenuItem {
        val saveImageMenuItem = JMenuItem("Copy SVG Java code to clipboard")
        // Add the keybinding for Save (Ctrl + J)
        saveImageMenuItem.addActionListener {
            fileUI.copySvgJavaCodeToClipboard()
        }
        return saveImageMenuItem
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
