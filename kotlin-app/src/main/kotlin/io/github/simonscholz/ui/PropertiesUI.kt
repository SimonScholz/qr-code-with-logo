package io.github.simonscholz.ui

import io.github.simonscholz.extension.toIntObservable
import io.github.simonscholz.extension.toObservable
import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.ui.properties.BorderPropertiesUI
import net.miginfocom.swing.MigLayout
import org.eclipse.core.databinding.DataBindingContext
import java.awt.Color
import java.awt.event.ActionListener
import java.util.function.Supplier
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JColorChooser
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.SpinnerNumberModel

object PropertiesUI {

    private const val WIDTH = "width 200:220:300"

    fun createPropertiesUI(qrCodeConfigViewModel: QrCodeConfigViewModel, dataBindingContext: DataBindingContext, clickedApply: () -> Unit): Pair<JPanel, () -> Boolean> {
        val propertiesPanel = JPanel(MigLayout())

        propertiesPanel.add(JLabel("QR Code Content:"))
        val contentTextArea = JTextArea()
        contentTextArea.text = "https://simonscholz.github.io/"
        propertiesPanel.add(contentTextArea, "wrap, growx, span 3, $WIDTH, height 100:120:300")
        dataBindingContext.bindValue(contentTextArea.toObservable(), qrCodeConfigViewModel.qrCodeContent)

        propertiesPanel.add(JLabel("Size (px):"))
        val sizeSpinnerModel = SpinnerNumberModel(200, 0, 20000, 1)
        val sizeSpinner = JSpinner(sizeSpinnerModel)
        propertiesPanel.add(sizeSpinner, "wrap, growx, span 3, $WIDTH")
        dataBindingContext.bindValue(sizeSpinner.toIntObservable(), qrCodeConfigViewModel.size)

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
        propertiesPanel.add(applyOnChange, "wrap, growx, span 3, gaptop 25")

        val applyButton = JButton("Apply")
        propertiesPanel.add(applyButton, "wrap, growx, span 3")
        applyButton.addActionListener {
            clickedApply()
        }

        // TODO allow to save config
        // propertiesPanel.add(JButton("Save Config"), "wrap, growx, span 3, gaptop 25")

        return Pair(propertiesPanel, applyOnChange::isSelected)
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
