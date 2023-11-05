package io.github.simonscholz.ui

import io.github.simonscholz.extension.toIntObservable
import io.github.simonscholz.extension.toObservable
import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.ui.properties.BorderPropertiesUI
import io.github.simonscholz.ui.properties.LogoPropertiesUI
import io.github.simonscholz.ui.properties.PositionalSquaresPropertiesUI
import net.miginfocom.swing.MigLayout
import org.eclipse.core.databinding.DataBindingContext
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JTextArea
import javax.swing.SpinnerNumberModel

object PropertiesUI {

    private const val WIDTH = "width 200:220:300"

    fun createPropertiesUI(qrCodeConfigViewModel: QrCodeConfigViewModel, dataBindingContext: DataBindingContext, clickedApply: () -> Unit): Pair<JPanel, () -> Boolean> {
        val propertiesPanel = JPanel(MigLayout())

        propertiesPanel.add(JLabel("QR Code Content:"))
        val contentTextArea = JTextArea()
        contentTextArea.text = "https://simonscholz.github.io/"
        propertiesPanel.add(contentTextArea, "wrap, growx, $WIDTH, height 100:120:300")
        dataBindingContext.bindValue(contentTextArea.toObservable(), qrCodeConfigViewModel.qrCodeContent)

        propertiesPanel.add(JLabel("Size (px):"))
        val sizeSpinnerModel = SpinnerNumberModel(200, 0, 20000, 1)
        val sizeSpinner = JSpinner(sizeSpinnerModel)
        propertiesPanel.add(sizeSpinner, "wrap, growx, $WIDTH")
        dataBindingContext.bindValue(sizeSpinner.toIntObservable(), qrCodeConfigViewModel.size)

        CustomItems.createColorPickerItem(propertiesPanel, "Background Color:", qrCodeConfigViewModel.backgroundColor, dataBindingContext)
        CustomItems.createColorPickerItem(propertiesPanel, "Foreground Color:", qrCodeConfigViewModel.foregroundColor, dataBindingContext)

        val logoPropertiesUI = LogoPropertiesUI.createLogoPropertiesUI(dataBindingContext, qrCodeConfigViewModel)
        propertiesPanel.add(logoPropertiesUI, "wrap, growx, span 2, gaptop 10")

        val borderPropertiesUI = BorderPropertiesUI.createBorderPropertiesUI(dataBindingContext, qrCodeConfigViewModel)
        propertiesPanel.add(borderPropertiesUI, "wrap, growx, span 2, gaptop 10")

        val positionalSquaresPropertiesUI = PositionalSquaresPropertiesUI.createPositionalSquarePropertiesUI(dataBindingContext, qrCodeConfigViewModel)
        propertiesPanel.add(positionalSquaresPropertiesUI, "wrap, growx, span 2, gaptop 10")

        val applyOnChange = JCheckBox("Apply on change")
        applyOnChange.isSelected = true
        propertiesPanel.add(applyOnChange, "wrap, growx, span 2, gaptop 25")

        val applyButton = JButton("Apply")
        propertiesPanel.add(applyButton, "wrap, growx, span 2")
        applyButton.addActionListener {
            clickedApply()
        }

        // TODO allow to save config
        // propertiesPanel.add(JButton("Save Config"), "wrap, growx, span 2, gaptop 25")

        return Pair(propertiesPanel, applyOnChange::isSelected)
    }
}
