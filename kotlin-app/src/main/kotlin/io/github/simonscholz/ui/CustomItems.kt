package io.github.simonscholz.ui

import io.github.simonscholz.extension.toBackgroundColorObservable
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.core.databinding.observable.value.IObservableValue
import java.awt.Color
import javax.swing.JButton
import javax.swing.JColorChooser
import javax.swing.JLabel
import javax.swing.JPanel

object CustomItems {
    fun createColorPickerItem(propertiesPanel: JPanel, labelText: String, model: IObservableValue<Color>, dataBindingContext: DataBindingContext) {
        propertiesPanel.add(JLabel(labelText))
        val colorPicker = JButton("Choose Color").apply {
            isFocusPainted = false
        }
        colorPicker.addActionListener {
            model.value = JColorChooser.showDialog(propertiesPanel.parent, "Choose a color", Color.WHITE)
        }
        dataBindingContext.bindValue(colorPicker.toBackgroundColorObservable(), model)
        propertiesPanel.add(colorPicker, "wrap, growx")
    }
}
