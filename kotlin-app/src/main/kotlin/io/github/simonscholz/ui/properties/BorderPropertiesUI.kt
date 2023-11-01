package io.github.simonscholz.ui.properties

import io.github.simonscholz.extension.toBackgroundColorObservable
import io.github.simonscholz.extension.toObservable
import io.github.simonscholz.model.QrCodeConfigViewModel
import net.miginfocom.swing.MigLayout
import org.eclipse.core.databinding.DataBindingContext
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JColorChooser
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.border.TitledBorder

object BorderPropertiesUI {
    fun createBorderPropertiesUI(dataBindingContext: DataBindingContext, qrCodeConfigViewModel: QrCodeConfigViewModel): JPanel {
        val borderPropertiesPanel = JPanel(MigLayout())

        borderPropertiesPanel.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Qr Code Border",
                TitledBorder.LEFT,
                TitledBorder.TOP,
            ),
        )

        borderPropertiesPanel.add(JLabel("Relative Border Size:"))
        val borderSizeTextField = JTextField()
        dataBindingContext.bindValue(borderSizeTextField.toObservable(), qrCodeConfigViewModel.relativeBorderSize)
        borderPropertiesPanel.add(borderSizeTextField, "wrap, growx, span 3, width 200:220:300")

        borderPropertiesPanel.add(JLabel("Border Color:"))
        val colorPicker = JButton("Choose Color").apply {
            isFocusPainted = false
        }
        colorPicker.addActionListener {
            qrCodeConfigViewModel.borderColor.value = JColorChooser.showDialog(borderPropertiesPanel.parent, "Choose a color", Color.WHITE)
        }
        dataBindingContext.bindValue(colorPicker.toBackgroundColorObservable(), qrCodeConfigViewModel.borderColor)
        borderPropertiesPanel.add(colorPicker, "wrap, growx, span 3, width 200:220:300")

        borderPropertiesPanel.add(JLabel("Border Radius:"))
        val borderRadiusTextField = JTextField()
        dataBindingContext.bindValue(borderRadiusTextField.toObservable(), qrCodeConfigViewModel.borderRadius)
        borderPropertiesPanel.add(borderRadiusTextField, "wrap, growx, span 3, width 200:220:300")

        return borderPropertiesPanel
    }
}
