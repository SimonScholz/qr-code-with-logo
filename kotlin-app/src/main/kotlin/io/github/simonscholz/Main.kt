package io.github.simonscholz

import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.ui.ImageUI
import io.github.simonscholz.ui.MainUI
import io.github.simonscholz.ui.PropertiesUI
import org.eclipse.core.databinding.DataBindingContext
import javax.swing.JFrame

fun main() {
    val frame = JFrame("QR Code AWT/Swing UI")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    val dataBindingContext = DataBindingContext()
    frame.addWindowListener(object : java.awt.event.WindowAdapter() {
        override fun windowClosing(windowEvent: java.awt.event.WindowEvent?) {
            dataBindingContext.dispose()
        }
    },)

    val qrCodeConfigViewModel = QrCodeConfigViewModel()

    val imagePanel = ImageUI.createImagePanel()
    val propertiesPanel = PropertiesUI.createPropertiesUI(qrCodeConfigViewModel, dataBindingContext)

    val mainPanel = MainUI.createMainPanel(imagePanel, propertiesPanel)
    frame.add(mainPanel)

    frame.pack()
    frame.isVisible = true
}
