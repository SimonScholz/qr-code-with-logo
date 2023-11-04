package io.github.simonscholz.ui

import net.miginfocom.swing.MigLayout
import javax.swing.JPanel

object MainUI {

    fun createMainPanel(imagePanel: JPanel, propertiesPanel: JPanel): JPanel {
        val mainPanel = JPanel(MigLayout("fill, wrap 2", "[70%][30%]"))
        mainPanel.add(imagePanel, "grow, push, w 70%")
        mainPanel.add(propertiesPanel, "grow, push, w 30%")
        return mainPanel
    }
}
