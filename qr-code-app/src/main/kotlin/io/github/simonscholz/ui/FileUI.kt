package io.github.simonscholz.ui

import io.github.simonscholz.extension.toFile
import io.github.simonscholz.extension.toXmlString
import io.github.simonscholz.qrcode.toBase64
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
) {
    fun copyBase64ImageToClipboard() {
        val qrCodeImage = imageService.renderImage()
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val copyString = StringSelection(qrCodeImage.toBase64())
        clipboard.setContents(copyString, null)
    }

    fun copyImageToClipboard() {
        val qrCodeImage = imageService.renderImage()
        val transferableImage = ImageTransferable(qrCodeImage)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(transferableImage, null)
    }

    fun copySvgImageToClipboard() {
        val qrCodeImage = imageService.generateSvg()
        val transferableImage = StringSelection(qrCodeImage.toXmlString())
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(transferableImage, null)
    }

    fun copyJavaCodeToClipboard() {
        val generateKotlinCode = codeGeneratorService.generateRasterImageJavaCode()
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val copyString = StringSelection(generateKotlinCode)
        clipboard.setContents(copyString, null)
    }

    fun copySvgJavaCodeToClipboard() {
        val generateKotlinCode = codeGeneratorService.generateSvgImageJavaCode()
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val copyString = StringSelection(generateKotlinCode)
        clipboard.setContents(copyString, null)
    }

    fun copyKotlinCodeToClipboard() {
        val generateKotlinCode = codeGeneratorService.generateRasterImageKotlinCode()
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val copyString = StringSelection(generateKotlinCode)
        clipboard.setContents(copyString, null)
    }

    fun copySvgKotlinCodeToClipboard() {
        val generateKotlinCode = codeGeneratorService.generateSvgImageKotlinCode()
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val copyString = StringSelection(generateKotlinCode)
        clipboard.setContents(copyString, null)
    }

    fun saveQrCodeImageFile() {
        val fileChooser = JFileChooser()
        fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
        fileChooser.fileFilter = FileNameExtensionFilter("Png Image Files (*.png)", "png")
        fileChooser.currentDirectory = configService.getLastUsedDirectory("saveQrCodeImageFile")
        val result = fileChooser.showSaveDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            val fileToSave =
                if (fileChooser.selectedFile.name.endsWith(".png")) {
                    fileChooser.selectedFile
                } else {
                    File("${fileChooser.selectedFile.absolutePath}.png")
                }
            configService.saveLastUsedDirectory("saveQrCodeImageFile", fileToSave.parentFile)

            val qrCodeImage = imageService.renderImage()
            ImageIO.write(qrCodeImage, "png", fileToSave)
        }
    }

    fun saveQrCodeSvgFile() {
        val fileChooser = JFileChooser()
        fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
        fileChooser.fileFilter = FileNameExtensionFilter("Svg Image Files (*.svg)", "svg")
        fileChooser.currentDirectory = configService.getLastUsedDirectory("saveQrCodeSvgFile")
        val result = fileChooser.showSaveDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            val fileToSave =
                if (fileChooser.selectedFile.name.endsWith(".svg")) {
                    fileChooser.selectedFile
                } else {
                    File("${fileChooser.selectedFile.absolutePath}.svg")
                }
            configService.saveLastUsedDirectory("saveQrCodeSvgFile", fileToSave.parentFile)

            val qrCodeSvgImage = imageService.generateSvg()
            qrCodeSvgImage.toFile(fileToSave)
        }
    }

    fun saveConfig() {
        val fileChooser = JFileChooser()
        fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
        fileChooser.fileFilter = FileNameExtensionFilter("Json Files (*.json)", "json")
        fileChooser.currentDirectory = configService.getLastUsedDirectory("saveConfig")
        val result = fileChooser.showSaveDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            val fileToSave =
                if (fileChooser.selectedFile.name.endsWith(".json")) {
                    fileChooser.selectedFile
                } else {
                    File("${fileChooser.selectedFile.absolutePath}.json")
                }

            runCatching {
                configService.saveConfigFile(fileToSave.absolutePath)
                configService.saveLastUsedDirectory("saveConfig", fileToSave.parentFile)
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
        fileChooser.currentDirectory = configService.getLastUsedDirectory("loadConfig")
        val result = fileChooser.showOpenDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            runCatching {
                configService.loadConfigFile(fileChooser.selectedFile.absolutePath)
                configService.saveLastUsedDirectory("loadConfig", fileChooser.selectedFile.parentFile)
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

        override fun getTransferDataFlavors(): Array<DataFlavor> =
            arrayOf(
                DataFlavor.imageFlavor,
                DataFlavor.javaFileListFlavor,
            )

        override fun isDataFlavorSupported(flavor: DataFlavor): Boolean =
            if (flavor === DataFlavor.imageFlavor) {
                true
            } else {
                flavor === DataFlavor.javaFileListFlavor
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
