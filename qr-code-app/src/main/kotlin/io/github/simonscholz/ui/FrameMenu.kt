package io.github.simonscholz.ui

import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.service.RenderImageService
import java.awt.Desktop
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.net.URI
import javax.imageio.ImageIO
import javax.swing.AbstractAction
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.KeyStroke
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.system.exitProcess

object FrameMenu {
    fun createFrameMenu(frame: JFrame, qrCodeConfigViewModel: QrCodeConfigViewModel, alreadyAppliedOnceDelegate: () -> Boolean) {
        // Create and set the menu bar
        val menuBar = JMenuBar()
        frame.jMenuBar = menuBar

        // Create the File menu
        val fileMenu = JMenu("File")
        menuBar.add(fileMenu)

        // Create menu items for File menu
        val saveMenuItem = JMenuItem("Save Qr Code Image")
        // Add the keybinding for Save (Ctrl + S)
        saveMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)
        frame.rootPane.actionMap.put(
            "SaveAction",
            object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    saveFile(frame, qrCodeConfigViewModel, alreadyAppliedOnceDelegate)
                }
            },
        )
        val exitMenuItem = JMenuItem("Exit")
        exitMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK)
        frame.rootPane.actionMap.put(
            "ExitAction",
            object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    exitApplication()
                }
            },
        )

        // Add ActionListeners to the menu items
        saveMenuItem.addActionListener { saveFile(frame, qrCodeConfigViewModel, alreadyAppliedOnceDelegate) }
        exitMenuItem.addActionListener { exitApplication() }

        // Add menu items to the File menu
        fileMenu.add(saveMenuItem)
        fileMenu.addSeparator()
        fileMenu.add(exitMenuItem)

        // Create the File menu
        val helpMenu = JMenu("Help")
        menuBar.add(helpMenu)

        val gitHubRepoMenuItem = JMenuItem("GitHub Repository")
        val readmeMenuItem = JMenuItem("README")
        val issueMenuItem = JMenuItem("Open Issue")

        gitHubRepoMenuItem.addActionListener { openURL(frame, "https://github.com/SimonScholz/qr-code-with-logo") }
        readmeMenuItem.addActionListener { openURL(frame, "https://github.com/SimonScholz/qr-code-with-logo/blob/main/README.adoc") }
        issueMenuItem.addActionListener { openURL(frame, "https://github.com/SimonScholz/qr-code-with-logo/issues") }

        helpMenu.add(gitHubRepoMenuItem)
        helpMenu.add(readmeMenuItem)
        helpMenu.add(issueMenuItem)
    }

    private fun openURL(frame: JFrame, url: String) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI(url))
            } else {
                // Handle the case where the Desktop class is not supported
                JOptionPane.showMessageDialog(frame, "Desktop is not supported on this platform.", "Error", JOptionPane.ERROR_MESSAGE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle any exceptions that may occur when opening the URL
            JOptionPane.showMessageDialog(frame, "Error opening URL: ${e.message}", "Error", JOptionPane.ERROR_MESSAGE)
        }
    }

    private fun saveFile(frame: JFrame, qrCodeConfigViewModel: QrCodeConfigViewModel, alreadyAppliedOnceDelegate: () -> Boolean) {
        val fileChooser = JFileChooser()
        fileChooser.fileFilter = FileNameExtensionFilter("Png Image Files (*.png)", "png")
        val result = fileChooser.showSaveDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            if (fileChooser.selectedFile.extension.endsWith("png")) {
                val selectedFile = fileChooser.selectedFile
                val qrCodeImage = if (alreadyAppliedOnceDelegate()) {
                    RenderImageService.renderImage(qrCodeConfigViewModel = qrCodeConfigViewModel, component = frame)
                } else {
                    RenderImageService.renderInitialImage()
                }
                ImageIO.write(qrCodeImage, "png", selectedFile)
            } else {
                JOptionPane.showMessageDialog(frame, "The file to be saved must have the png extension", "Image Saving Error", JOptionPane.ERROR_MESSAGE)
            }
        }
    }

    private fun exitApplication() {
        exitProcess(0)
    }
}
