package io.github.simonscholz

import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.observables.SwingRealm
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory
import io.github.simonscholz.ui.ImageUI
import io.github.simonscholz.ui.MainUI
import io.github.simonscholz.ui.PropertiesUI
import org.eclipse.core.databinding.DataBindingContext
import java.awt.Component
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        val frame = JFrame("QR Code AWT/Swing UI")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val dataBindingContext = DataBindingContext(SwingRealm())
        frame.addWindowListener(
            object : WindowAdapter() {
                override fun windowClosing(windowEvent: WindowEvent) {
                    dataBindingContext.dispose()
                }
            },
        )

        val qrCodeConfigViewModel = QrCodeConfigViewModel()

        val (imagePanel, setImage) = ImageUI.createImagePanel()
        val (propertiesPanel, applyOnChange) = PropertiesUI.createPropertiesUI(qrCodeConfigViewModel, dataBindingContext) {
            if (qrCodeConfigViewModel.qrCodeContent.value.isNotBlank()) {
                val qrCodeImage = renderImage(qrCodeConfigViewModel, frame)
                setImage(qrCodeImage)
            }
        }

        val mainPanel = MainUI.createMainPanel(imagePanel, propertiesPanel)
        frame.add(mainPanel)

        frame.pack()
        frame.isVisible = true

        dataBindingContext.bindings.forEach {
            it.model.addChangeListener {
                if (applyOnChange()) {
                    if (qrCodeConfigViewModel.qrCodeContent.value.isNotBlank()) {
                        val qrCodeImage = renderImage(qrCodeConfigViewModel, frame)
                        setImage(qrCodeImage)
                    }
                }
            }
        }
    }
}

private fun renderImage(qrCodeConfigViewModel: QrCodeConfigViewModel, component: Component): BufferedImage {
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
    if (qrCodeConfigViewModel.logo.value.isNotBlank() && File(qrCodeConfigViewModel.logo.value).exists()) {
        runCatching {
            ImageIO.read(File(qrCodeConfigViewModel.logo.value)).let {
                builder.qrLogoConfig(it)
            }
        }.onFailure { _ ->
            JOptionPane.showMessageDialog(component, "You did not select a proper image", "Image Loading Error", JOptionPane.ERROR_MESSAGE)
        }
    }
    val qrCodeConfig = builder.build()
    return QrCodeFactory.createQrCodeApi().createQrCodeImage(qrCodeConfig)
}
