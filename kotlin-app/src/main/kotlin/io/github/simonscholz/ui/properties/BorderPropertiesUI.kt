package io.github.simonscholz.ui.properties

import io.github.simonscholz.extension.toBackgroundColorObservable
import io.github.simonscholz.extension.toDoubleObservable
import io.github.simonscholz.model.QrCodeConfigViewModel
import net.miginfocom.swing.MigLayout
import org.eclipse.core.databinding.DataBindingContext
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JColorChooser
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
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
        val relativeSizeSpinnerModel = SpinnerNumberModel(.2, .0, 1.0, 0.01)
        val borderSizeSpinner = JSpinner(relativeSizeSpinnerModel)
        dataBindingContext.bindValue(borderSizeSpinner.toDoubleObservable(), qrCodeConfigViewModel.relativeBorderSize)
        borderPropertiesPanel.add(borderSizeSpinner, "wrap, growx, width 200:220:300")

        borderPropertiesPanel.add(JLabel("Border Radius:"))
        val borderRadiusSpinnerModel = SpinnerNumberModel(.05, .0, 1.0, 0.01)
        val borderRadiusSpinner = JSpinner(borderRadiusSpinnerModel)
        dataBindingContext.bindValue(borderRadiusSpinner.toDoubleObservable(), qrCodeConfigViewModel.borderRadius)
        borderPropertiesPanel.add(borderRadiusSpinner, "wrap, growx, width 200:220:300")

        borderPropertiesPanel.add(JLabel("Border Color:"))
        val colorPicker = JButton("Choose Color").apply {
            isFocusPainted = false
        }
        colorPicker.addActionListener {
            qrCodeConfigViewModel.borderColor.value = JColorChooser.showDialog(borderPropertiesPanel.parent, "Choose a color", Color.WHITE)
        }
        dataBindingContext.bindValue(colorPicker.toBackgroundColorObservable(), qrCodeConfigViewModel.borderColor)
        borderPropertiesPanel.add(colorPicker, "wrap, growx, width 200:220:300")

        return borderPropertiesPanel
    }
}
