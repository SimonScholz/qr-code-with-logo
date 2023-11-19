package io.github.simonscholz

import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.observables.SwingRealm
import io.github.simonscholz.service.ConfigService
import io.github.simonscholz.service.ImageService
import io.github.simonscholz.ui.ImageUI
import io.github.simonscholz.ui.MainMenu
import io.github.simonscholz.ui.MainUI
import io.github.simonscholz.ui.PropertiesUI
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.core.databinding.observable.value.IObservableValue
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import java.io.File
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

fun main() {
    // GraalVM Fix
    if (System.getProperty("java.home") == null) {
        println("No Java Home set, assuming that we are running from GraalVM. Fixing...")
        System.setProperty("java.home", File(".").absolutePath)
    }

    SwingUtilities.invokeLater {
        val frame = JFrame("QR Code AWT/Swing UI")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val dataBindingContext = DataBindingContext(SwingRealm())
        val qrCodeConfigViewModel = QrCodeConfigViewModel()
        val configService = ConfigService(qrCodeConfigViewModel)
        frame.addWindowListener(
            object : WindowAdapter() {
                override fun windowClosing(windowEvent: WindowEvent) {
                    configService.saveConfig()
                    dataBindingContext.dispose()
                }
            },
        )

        var alreadyAppliedOnce = false
        val alreadyAppliedOnceDelegate = { alreadyAppliedOnce }
        val imageService = ImageService(qrCodeConfigViewModel, alreadyAppliedOnceDelegate)
        MainMenu.createFrameMenu(frame, qrCodeConfigViewModel.qrCodeContent, imageService)

        val (imagePanel, setImage) = ImageUI.createImagePanel(imageService)
        val (propertiesPanel, applyOnChange) = PropertiesUI.createPropertiesUI(qrCodeConfigViewModel, dataBindingContext) {
            onPropertyApply(qrCodeConfigViewModel.qrCodeContent, imageService, setImage, imagePanel)
            alreadyAppliedOnce = true
        }

        val mainPanel = MainUI.createMainPanel(imagePanel, propertiesPanel)
        frame.add(mainPanel)

        frame.pack()
        frame.isVisible = true

        dataBindingContext.bindings.forEach {
            it.model.addChangeListener {
                if (applyOnChange()) {
                    onPropertyApply(qrCodeConfigViewModel.qrCodeContent, imageService, setImage, imagePanel)
                    alreadyAppliedOnce = true
                }
            }
        }
        configService.loadConfig()
    }
}

private fun onPropertyApply(qrCodeContentObservable: IObservableValue<String>, imageService: ImageService, setImage: (BufferedImage) -> Unit, imagePanel: JPanel) {
    if (qrCodeContentObservable.value.isNotBlank()) {
        val qrCodeImage = imageService.renderImage()
        setImage(qrCodeImage)
        imagePanel.revalidate()
    }
}
