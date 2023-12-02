package io.github.simonscholz.ui.properties

import io.github.simonscholz.extension.toDoubleObservable
import io.github.simonscholz.extension.toSelectedItemObservable
import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.qrcode.LogoShape
import io.github.simonscholz.qrcode.toBase64
import io.github.simonscholz.ui.CustomItems
import net.miginfocom.swing.MigLayout
import org.eclipse.core.databinding.DataBindingContext
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.io.File
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JFileChooser
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import javax.swing.filechooser.FileNameExtensionFilter

object LogoPropertiesUI {

    fun createLogoPropertiesUI(dataBindingContext: DataBindingContext, qrCodeConfigViewModel: QrCodeConfigViewModel): JPanel {
        val logoPropertiesPanel = JPanel(MigLayout())

        logoPropertiesPanel.add(JLabel("Logo:"))

        val buttonPanel = JPanel(MigLayout())
        val chooseFileButton = JButton()
        chooseFileButton.toolTipText = "Choose image file"
        chooseFileButton.icon = ImageIcon(LogoPropertiesUI::class.java.classLoader.getResource("image16.png"))
        chooseFileButton.addActionListener {
            val fileChooser = JFileChooser()
            fileChooser.fileFilter = FileNameExtensionFilter("Image files", "png", "jpg", "jpeg")
            fileChooser.showOpenDialog(logoPropertiesPanel.parent).let {
                if (it == JFileChooser.APPROVE_OPTION) {
                    val file: File = fileChooser.selectedFile
                    ImageIO.read(file).let { image ->
                        qrCodeConfigViewModel.logoBase64.value = image.toBase64()
                    }
                }
            }
        }
        buttonPanel.add(chooseFileButton, "width 30:30:30")

        val base64ImageButton = JButton()
        base64ImageButton.toolTipText = "Paste base64 image text"
        base64ImageButton.icon = ImageIcon(LogoPropertiesUI::class.java.classLoader.getResource("base64_16.png"))
        base64ImageButton.addActionListener {
            val base64ImageInput = JOptionPane.showInputDialog("Paste base64 image text", "")
            if (base64ImageInput != null) {
                qrCodeConfigViewModel.logoBase64.value = base64ImageInput
            }
        }
        buttonPanel.add(base64ImageButton, "width 30:30:30")

        val copyButton = JButton()
        copyButton.toolTipText = "Copy base64 image text"
        copyButton.icon = ImageIcon(LogoPropertiesUI::class.java.classLoader.getResource("copy16.png"))
        copyButton.addActionListener {
            val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val copyString = StringSelection(qrCodeConfigViewModel.logoBase64.value)
            clipboard.setContents(copyString, null)
        }
        buttonPanel.add(copyButton, "width 30:30:30")

        val deleteBase64LogoTextField = JButton()
        deleteBase64LogoTextField.toolTipText = "Remove icon"
        deleteBase64LogoTextField.icon = ImageIcon(LogoPropertiesUI::class.java.classLoader.getResource("delete16.png"))
        deleteBase64LogoTextField.addActionListener {
            qrCodeConfigViewModel.logoBase64.value = ""
        }
        buttonPanel.add(deleteBase64LogoTextField, "wrap, width 30:30:30")
        logoPropertiesPanel.add(buttonPanel, "wrap, growx, span 3")

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
