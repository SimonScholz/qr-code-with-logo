package io.github.simonscholz.ui

import java.awt.BorderLayout
import javax.swing.JPanel
import net.miginfocom.swing.MigLayout

object MainUI {

    fun createMainPanel(imagePanel: JPanel, propertiesPanel: JPanel): JPanel {
        val mainPanel = JPanel(MigLayout())
        mainPanel.add(imagePanel, "grow")
        mainPanel.add(propertiesPanel, "grow")
        return mainPanel
    }
}
