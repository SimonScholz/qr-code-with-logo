package io.github.simonscholz.ui.properties

import io.github.simonscholz.extension.toCheckboxObservable
import io.github.simonscholz.extension.toDoubleObservable
import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.ui.CustomItems
import net.miginfocom.swing.MigLayout
import org.eclipse.core.databinding.DataBindingContext
import javax.swing.BorderFactory
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import javax.swing.border.TitledBorder

object PositionalSquaresPropertiesUI {
    fun createPositionalSquarePropertiesUI(dataBindingContext: DataBindingContext, qrCodeConfigViewModel: QrCodeConfigViewModel): JPanel {
        val positionalSquaresPropertiesPanel = JPanel(MigLayout())

        positionalSquaresPropertiesPanel.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Qr Code Positional Squares",
                TitledBorder.LEFT,
                TitledBorder.TOP,
            ),
        )

        positionalSquaresPropertiesPanel.add(JLabel("Is Circle Shape:"))
        val isCircleShaped = JCheckBox()
        positionalSquaresPropertiesPanel.add(isCircleShaped, "wrap, growx, width 200:220:300")
        dataBindingContext.bindValue(isCircleShaped.toCheckboxObservable(), qrCodeConfigViewModel.positionalSquareIsCircleShaped)

        positionalSquaresPropertiesPanel.add(JLabel("Positional Sqaure Border Radius:"))
        val relativeSquareBorderRoundSpinnerModel = SpinnerNumberModel(.2, .0, 1.0, 0.01)
        val relativePositionalSquareBorderRoundSpinner = JSpinner(relativeSquareBorderRoundSpinnerModel)
        dataBindingContext.bindValue(relativePositionalSquareBorderRoundSpinner.toDoubleObservable(), qrCodeConfigViewModel.positionalSquareRelativeBorderRound)
        positionalSquaresPropertiesPanel.add(relativePositionalSquareBorderRoundSpinner, "wrap, growx, width 200:220:300")

        CustomItems.createColorPickerItem(positionalSquaresPropertiesPanel, "Center Color:", qrCodeConfigViewModel.positionalSquareCenterColor, dataBindingContext)
        CustomItems.createColorPickerItem(positionalSquaresPropertiesPanel, "Inner Square Color:", qrCodeConfigViewModel.positionalSquareInnerSquareColor, dataBindingContext)
        CustomItems.createColorPickerItem(positionalSquaresPropertiesPanel, "Outer Square Color:", qrCodeConfigViewModel.positionalSquareOuterSquareColor, dataBindingContext)
        CustomItems.createColorPickerItem(positionalSquaresPropertiesPanel, "Outer Border Color:", qrCodeConfigViewModel.positionalSquareOuterBorderColor, dataBindingContext)

        return positionalSquaresPropertiesPanel
    }
}
