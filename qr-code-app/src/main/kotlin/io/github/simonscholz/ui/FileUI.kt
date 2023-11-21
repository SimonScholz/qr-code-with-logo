package io.github.simonscholz.ui

import io.github.simonscholz.service.CodeGeneratorService
import io.github.simonscholz.service.ConfigService
import io.github.simonscholz.service.ImageService
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter

class FileUI(
    private val codeGeneratorService: CodeGeneratorService,
    private val configService: ConfigService,
    private val imageService: ImageService,
    private val alreadyAppliedOnceDelegate: () -> Boolean,
) {

    fun copyJavaCodeToClipboard() {
        val generateKotlinCode = codeGeneratorService.generateJavaCode()
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val copyString = StringSelection(generateKotlinCode)
        clipboard.setContents(copyString, null)
    }

    fun copyKotlinCodeToClipboard() {
        val generateKotlinCode = codeGeneratorService.generateKotlinCode()
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val copyString = StringSelection(generateKotlinCode)
        clipboard.setContents(copyString, null)
    }

    fun saveQrCodeImageFile() {
        val fileChooser = JFileChooser()
        fileChooser.fileFilter = FileNameExtensionFilter("Png Image Files (*.png)", "png")
        val result = fileChooser.showSaveDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            val fileToSave = if (fileChooser.selectedFile.name.endsWith(".png")) {
                fileChooser.selectedFile
            } else {
                File("${fileChooser.selectedFile.absolutePath}.png")
            }

            val qrCodeImage = if (alreadyAppliedOnceDelegate()) {
                imageService.renderImage()
            } else {
                imageService.renderInitialImage()
            }
            ImageIO.write(qrCodeImage, "png", fileToSave)
        }
    }

    fun saveConfig() {
        val fileChooser = JFileChooser()
        fileChooser.fileFilter = FileNameExtensionFilter("Json Files (*.json)", "json")
        val result = fileChooser.showSaveDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            val fileToSave = if (fileChooser.selectedFile.name.endsWith(".json")) {
                fileChooser.selectedFile
            } else {
                File("${fileChooser.selectedFile.absolutePath}.json")
            }

            runCatching {
                configService.saveConfigFile(fileToSave.absolutePath)
            }.onFailure {
                JOptionPane.showMessageDialog(
                    null,
                    "The file to be saved must be a valid json config file.\n Fehler: ${it.message}",
                    "Config Saving Error",
                    JOptionPane.ERROR_MESSAGE,
                )
            }
        }
    }

    fun loadConfig() {
        val fileChooser = JFileChooser()
        fileChooser.fileFilter = FileNameExtensionFilter("Json Files (*.json)", "json")
        val result = fileChooser.showOpenDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            runCatching {
                configService.loadConfigFile(fileChooser.selectedFile.absolutePath)
            }.onFailure {
                JOptionPane.showMessageDialog(
                    null,
                    "The file to be loaded must be a valid json config file.\n Fehler: ${it.message}",
                    "Config Loading Error",
                    JOptionPane.ERROR_MESSAGE,
                )
            }
        }
    }
}