package io.github.simonscholz.ui

import javax.swing.JComponent
import javax.swing.JPanel
import net.miginfocom.swing.MigLayout


object MainUI {

    fun createMainPanel(imagePanel: JComponent, propertiesPanel: JComponent): JPanel {
        val mainPanel = JPanel(MigLayout("fill, wrap 2", "[70%][30%]"))
        mainPanel.add(imagePanel, "grow, push, w 70%")

        mainPanel.add(propertiesPanel, "grow, push, w 30%")
        return mainPanel
    }
}
