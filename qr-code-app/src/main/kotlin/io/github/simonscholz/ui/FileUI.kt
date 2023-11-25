package io.github.simonscholz.ui

import io.github.simonscholz.service.CodeGeneratorService
import io.github.simonscholz.service.ConfigService
import io.github.simonscholz.service.ImageService
import org.jdesktop.swingx.graphics.GraphicsUtilities
import java.awt.Image
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
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

    fun copyImageToClipboard() {
        val qrCodeImage = if (alreadyAppliedOnceDelegate()) {
            imageService.renderImage()
        } else {
            imageService.renderInitialImage()
        }
        val transferableImage = ImageTransferable(qrCodeImage)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(transferableImage, null)
    }

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

    private class ImageTransferable(
        private val img: Image,
        private val exportName: String = "qr-code",
        private val exportFormat: String = "png",
    ) : Transferable {
        private var files: MutableList<File>? = null
        override fun getTransferDataFlavors(): Array<DataFlavor> {
            return arrayOf(
                DataFlavor.imageFlavor,
                DataFlavor.javaFileListFlavor,
            )
        }

        override fun isDataFlavorSupported(flavor: DataFlavor): Boolean {
            return if (flavor === DataFlavor.imageFlavor) {
                true
            } else {
                flavor === DataFlavor.javaFileListFlavor
            }
        }

        override fun getTransferData(flavor: DataFlavor): Any {
            // log.fine("doing get trans data: " + flavor);
            if (flavor === DataFlavor.imageFlavor) {
                return img
            }
            if (flavor === DataFlavor.javaFileListFlavor) {
                if (files == null) {
                    val file = File.createTempFile(exportName, ".$exportFormat")
                    // log.fine("writing to: " + file);
                    ImageIO.write(
                        GraphicsUtilities.convertToBufferedImage(img),
                        exportFormat,
                        file,
                    )
                    files = listOf(file).toMutableList()
                }
                // log.fine("returning: " + files);
                return files as MutableList<File>
            }
            return emptyList<File>()
        }
    }
}
