package io.github.simonscholz

import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.observables.SwingRealm
import io.github.simonscholz.service.RenderImageService.renderImage
import io.github.simonscholz.ui.ImageUI
import io.github.simonscholz.ui.MainMenu
import io.github.simonscholz.ui.MainUI
import io.github.simonscholz.ui.PropertiesUI
import org.eclipse.core.databinding.DataBindingContext
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import java.io.File
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

fun main() {
    //GraalVM Fix
    if (System.getProperty("java.home") == null) {
        println("No Java Home set, assuming that we are running from GraalVM. Fixing...");
        System.setProperty("java.home", File(".").absolutePath);
    }

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

        var alreadyAppliedOnce = false
        val alreadyAppliedOnceDelegate = { alreadyAppliedOnce }
        val qrCodeConfigViewModel = QrCodeConfigViewModel()
        MainMenu.createFrameMenu(frame, qrCodeConfigViewModel, alreadyAppliedOnceDelegate)

        val (imagePanel, setImage) = ImageUI.createImagePanel()
        val (propertiesPanel, applyOnChange) = PropertiesUI.createPropertiesUI(qrCodeConfigViewModel, dataBindingContext) {
            onPropertyApply(qrCodeConfigViewModel, frame, setImage, imagePanel)
            alreadyAppliedOnce = true
        }

        val mainPanel = MainUI.createMainPanel(imagePanel, propertiesPanel)
        frame.add(mainPanel)

        frame.pack()
        frame.isVisible = true

        dataBindingContext.bindings.forEach {
            it.model.addChangeListener {
                if (applyOnChange()) {
                    onPropertyApply(qrCodeConfigViewModel, frame, setImage, imagePanel)
                    alreadyAppliedOnce = true
                }
            }
        }
    }
}

private fun onPropertyApply(qrCodeConfigViewModel: QrCodeConfigViewModel, frame: JFrame, setImage: (BufferedImage) -> Unit, imagePanel: JPanel) {
    if (qrCodeConfigViewModel.qrCodeContent.value.isNotBlank()) {
        val qrCodeImage = renderImage(qrCodeConfigViewModel, frame)
        setImage(qrCodeImage)
        imagePanel.revalidate()
    }
}
