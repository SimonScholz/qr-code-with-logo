package io.github.simonscholz.ui

import io.github.simonscholz.extension.toObservable
import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.ui.properties.BorderPropertiesUI
import net.miginfocom.swing.MigLayout
import org.eclipse.core.databinding.DataBindingContext
import java.awt.Color
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JColorChooser
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JTextField

object PropertiesUI {

    private const val WIDTH = "width 200:220:300"

    fun createPropertiesUI(qrCodeConfigViewModel: QrCodeConfigViewModel, dataBindingContext: DataBindingContext): JPanel {
        val propertiesPanel = JPanel(MigLayout())

        propertiesPanel.add(JLabel("QR Code Content:"))
        val contentTextArea = JTextArea()
        contentTextArea.text = "https://simonscholz.github.io/"
        propertiesPanel.add(contentTextArea, "wrap, growx, span 3, $WIDTH, height 100:120:300")
        dataBindingContext.bindValue(contentTextArea.toObservable(), qrCodeConfigViewModel.qrCodeContent)

        propertiesPanel.add(JLabel("Size (px):"))
        val textField = JTextField()
        textField.text = "200"
        propertiesPanel.add(textField, "wrap, growx, span 3, $WIDTH")
        dataBindingContext.bindValue(textField.toObservable(), qrCodeConfigViewModel.size)

        createColorPickerFormItem(propertiesPanel, "Background Color:")
        createColorPickerFormItem(propertiesPanel, "Foreground Color:")

        propertiesPanel.add(JLabel("Logo:"))
        val logoTextField = JTextField()
        // TODO data binding
        propertiesPanel.add(logoTextField, "growx, $WIDTH")
        val chooseFile = JButton("...")
        chooseFile.addActionListener {
            // TODO open file chooser
        }
        propertiesPanel.add(chooseFile, "wrap, growx, width 30:30:30")

        val borderPropertiesUI = BorderPropertiesUI.createBorderPropertiesUI()
        propertiesPanel.add(borderPropertiesUI, "wrap, growx, span 3")

        val applyOnChange = JCheckBox("Apply on change")
        // TODO data binding
        propertiesPanel.add(applyOnChange, "wrap, growx, span 3, gaptop 25")

        propertiesPanel.add(JButton("Apply"), "wrap, growx, span 3")

        propertiesPanel.add(JButton("Save Config"), "wrap, growx, span 3, gaptop 25")

        return propertiesPanel
    }

    private fun createColorPickerFormItem(propertiesPanel: JPanel, labelText: String) {
        propertiesPanel.add(JLabel(labelText))
        val colorPicker = JButton("Choose Color").apply {
            isFocusPainted = false
        }
        colorPicker.addActionListener {
            val color = JColorChooser.showDialog(propertiesPanel.parent, "Choose a color", Color.WHITE)
            colorPicker.background = color
        }
        propertiesPanel.add(colorPicker, "wrap, growx, span 3, $WIDTH")
    }
}
