package io.github.simonscholz

import io.github.simonscholz.ui.ImageUI
import io.github.simonscholz.ui.MainUI
import io.github.simonscholz.ui.PropertiesUI
import javax.swing.JFrame

fun main() {
    val frame = JFrame("QR Code AWT/Swing UI")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    val imagePanel = ImageUI.createImagePanel()
    val propertiesPanel = PropertiesUI.createPropertiesUI()

    val mainPanel = MainUI.createMainPanel(imagePanel, propertiesPanel)
    frame.add(mainPanel)

    frame.pack()
    frame.isVisible = true
}
