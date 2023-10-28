package io.github.simonscholz.ui.properties

import io.github.simonscholz.ui.PropertiesUI
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JColorChooser
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.border.TitledBorder
import net.miginfocom.swing.MigLayout

object BorderPropertiesUI {
    fun createBorderPropertiesUI(): JPanel {
        val borderPropertiesPanel = JPanel(MigLayout())

        borderPropertiesPanel.setBorder(
            BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Qr Code Border", TitledBorder.LEFT, TitledBorder.TOP))

        borderPropertiesPanel.add(JLabel("Relative Border Size:"))
        val borderSizeTextField = JTextField()
        borderSizeTextField.text = "0.05"
        // TODO data binding
        borderPropertiesPanel.add(borderSizeTextField, "wrap, growx, span 3, width 200:220:300")

        borderPropertiesPanel.add(JLabel("Border Color:"))
        val colorPicker = JButton("Choose Color").apply {
            isFocusPainted = false
        }
        colorPicker.addActionListener {
            val color = JColorChooser.showDialog(borderPropertiesPanel.parent, "Choose a color", Color.WHITE)
            colorPicker.background = color
        }
        borderPropertiesPanel.add(colorPicker, "wrap, growx, span 3, width 200:220:300")

        borderPropertiesPanel.add(JLabel("Border Radius:"))
        val borderRadiusTextField = JTextField()
        borderRadiusTextField.text = "0.2"
        // TODO data binding
        borderPropertiesPanel.add(borderRadiusTextField, "wrap, growx, span 3, width 200:220:300")

        return borderPropertiesPanel
    }
}
