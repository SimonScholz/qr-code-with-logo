package io.github.simonscholz.ui.properties

import io.github.simonscholz.extension.toDoubleObservable
import io.github.simonscholz.extension.toObservable
import io.github.simonscholz.extension.toSelectedItemObservable
import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.qrcode.LogoShape
import io.github.simonscholz.ui.CustomItems
import net.miginfocom.swing.MigLayout
import org.eclipse.core.databinding.DataBindingContext
import java.io.File
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JFileChooser
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JTextField
import javax.swing.SpinnerNumberModel
import javax.swing.border.TitledBorder
import javax.swing.filechooser.FileNameExtensionFilter

object LogoPropertiesUI {

    fun createLogoPropertiesUI(dataBindingContext: DataBindingContext, qrCodeConfigViewModel: QrCodeConfigViewModel): JPanel {
        val logoPropertiesPanel = JPanel(MigLayout())

        logoPropertiesPanel.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Qr Code Logo",
                TitledBorder.LEFT,
                TitledBorder.TOP,
            ),
        )

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
