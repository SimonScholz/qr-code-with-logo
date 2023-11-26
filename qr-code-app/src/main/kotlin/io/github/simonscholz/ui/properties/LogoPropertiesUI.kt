package io.github.simonscholz.ui.properties

import io.github.simonscholz.extension.toButtonSelectedObservable
import io.github.simonscholz.extension.toDoubleObservable
import io.github.simonscholz.extension.toEnabledInvertedObservable
import io.github.simonscholz.extension.toEnabledObservable
import io.github.simonscholz.extension.toObservable
import io.github.simonscholz.extension.toSelectedItemObservable
import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.qrcode.LogoShape
import io.github.simonscholz.ui.CustomItems
import net.miginfocom.swing.MigLayout
import org.eclipse.core.databinding.DataBindingContext
import java.io.File
import javax.swing.ButtonGroup
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JFileChooser
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JSpinner
import javax.swing.JTextField
import javax.swing.SpinnerNumberModel
import javax.swing.filechooser.FileNameExtensionFilter

object LogoPropertiesUI {

    fun createLogoPropertiesUI(dataBindingContext: DataBindingContext, qrCodeConfigViewModel: QrCodeConfigViewModel): JPanel {
        val logoPropertiesPanel = JPanel(MigLayout())

        logoPropertiesPanel.add(JLabel("Logo source:"))

        val radioPanel = JPanel(MigLayout("nogrid"))
        val useFileButton = JRadioButton("Use file")
        val useBase64Button = JRadioButton("Use base 64")
        ButtonGroup().run {
            add(useFileButton)
            add(useBase64Button)
        }
        radioPanel.add(useFileButton)
        radioPanel.add(useBase64Button)
        dataBindingContext.bindValue(useBase64Button.toButtonSelectedObservable(), qrCodeConfigViewModel.useBase64Logo)
        logoPropertiesPanel.add(radioPanel, "wrap")

        logoPropertiesPanel.add(JLabel("Logo:"))
        val logoTextField = JTextField()
        dataBindingContext.bindValue(logoTextField.toObservable(), qrCodeConfigViewModel.logo)
        logoPropertiesPanel.add(logoTextField, "growx, width 200:220:300")
        val chooseFile = JButton("...")
        chooseFile.addActionListener {
            val fileChooser = JFileChooser()
            fileChooser.fileFilter = FileNameExtensionFilter("Image files", "png", "jpg", "jpeg")
            fileChooser.showOpenDialog(logoPropertiesPanel.parent).let {
                if (it == JFileChooser.APPROVE_OPTION) {
                    val file: File = fileChooser.selectedFile
                    logoTextField.text = file.absolutePath
                }
            }
        }
        logoPropertiesPanel.add(chooseFile, "wrap, growx, width 30:30:30")
        dataBindingContext.bindValue(logoTextField.toEnabledInvertedObservable(), qrCodeConfigViewModel.useBase64Logo)
        dataBindingContext.bindValue(chooseFile.toEnabledInvertedObservable(), qrCodeConfigViewModel.useBase64Logo)

        logoPropertiesPanel.add(JLabel("Base64 encoded Logo:"))
        val base64LogoTextField = JTextField().apply {
            dataBindingContext.bindValue(this.toObservable(), qrCodeConfigViewModel.logoBase64)
            logoPropertiesPanel.add(this, "growx, width 200:220:300")
            dataBindingContext.bindValue(this.toEnabledObservable(), qrCodeConfigViewModel.useBase64Logo)
        }

        val deleteBase64LogoTextField = JButton()
        deleteBase64LogoTextField.icon = ImageIcon(LogoPropertiesUI::class.java.classLoader.getResource("dustbin_remove16.png"))
        deleteBase64LogoTextField.addActionListener {
            base64LogoTextField.text = ""
        }
        logoPropertiesPanel.add(deleteBase64LogoTextField, "wrap, growx, width 30:30:30")

        logoPropertiesPanel.add(JLabel("Relative Logo Size:"))
        val sizeSpinnerModel = SpinnerNumberModel(.2, .0, 1.0, 0.01)
        val sizeSpinner = JSpinner(sizeSpinnerModel)
        logoPropertiesPanel.add(sizeSpinner, "wrap, growx, span 3, width 200:220:300")
        dataBindingContext.bindValue(sizeSpinner.toDoubleObservable(), qrCodeConfigViewModel.logoRelativeSize)

        CustomItems.createColorPickerItem(logoPropertiesPanel, "Logo Background Color:", qrCodeConfigViewModel.logoBackgroundColor, dataBindingContext, "wrap, growx, span 2")

        logoPropertiesPanel.add(JLabel("Logo Shape:"))
        val logoShapes = LogoShape.entries.toTypedArray()
        val shapeComboBox = JComboBox(logoShapes)
        dataBindingContext.bindValue(shapeComboBox.toSelectedItemObservable(), qrCodeConfigViewModel.logoShape)
        logoPropertiesPanel.add(shapeComboBox, "wrap, growx, span 3, width 200:220:300")

        return logoPropertiesPanel
    }
}
