package io.github.simonscholz

import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.observables.SwingRealm
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory
import io.github.simonscholz.ui.ImageUI
import io.github.simonscholz.ui.MainUI
import io.github.simonscholz.ui.PropertiesUI
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import org.eclipse.core.databinding.DataBindingContext
import javax.swing.JFrame
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
            // TODO render new qr code image
            if(qrCodeConfigViewModel.qrCodeContent.value.isNotBlank()) {
                val qrCodeImage = renderImage(qrCodeConfigViewModel)
                setImage(qrCodeImage)
            }
        }

        val mainPanel = MainUI.createMainPanel(imagePanel, propertiesPanel)
        frame.add(mainPanel)

        frame.pack()
        frame.isVisible = true

        dataBindingContext.bindings.forEach {
            it.model.addChangeListener {
                if(applyOnChange()) {
                    // TODO render new qr code image
                    if(qrCodeConfigViewModel.qrCodeContent.value.isNotBlank()) {
                        val qrCodeImage = renderImage(qrCodeConfigViewModel)
                        setImage(qrCodeImage)
                    }
                }
            }
        }
    }
}

private fun renderImage(qrCodeConfigViewModel: QrCodeConfigViewModel): BufferedImage {
    // TODO render new qr code image
    println(qrCodeConfigViewModel)
    val qrCodeConfig = QrCodeConfig.Builder(qrCodeConfigViewModel.qrCodeContent.value).qrCodeSize(qrCodeConfigViewModel.size.value).build()
    return QrCodeFactory.createQrCodeApi().createQrCodeImage(qrCodeConfig)
}
