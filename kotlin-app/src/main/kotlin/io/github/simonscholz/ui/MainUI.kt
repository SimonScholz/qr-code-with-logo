package io.github.simonscholz.ui

import net.miginfocom.swing.MigLayout
import javax.swing.JPanel

object MainUI {

    fun createMainPanel(imagePanel: JPanel, propertiesPanel: JPanel): JPanel {
        val mainPanel = JPanel(MigLayout())
        mainPanel.add(imagePanel, "grow")
        mainPanel.add(propertiesPanel, "grow")
        return mainPanel
    }
}
