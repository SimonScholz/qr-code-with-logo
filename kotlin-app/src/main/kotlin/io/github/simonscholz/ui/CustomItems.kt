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
    fun createColorPickerItem(propertiesPanel: JPanel, labelText: String, model: IObservableValue<Color>, dataBindingContext: DataBindingContext, buttonLayoutConstraints: String = "wrap, growx") {
        propertiesPanel.add(JLabel(labelText))
        val colorPicker = JButton("Choose Color").apply {
            isFocusPainted = false
        }
        colorPicker.addActionListener {_ ->
            JColorChooser.showDialog(propertiesPanel.parent, "Choose a color", Color.WHITE)?.let {
                model.value = it
            }
        }
        dataBindingContext.bindValue(colorPicker.toBackgroundColorObservable(), model)
        propertiesPanel.add(colorPicker, buttonLayoutConstraints)
    }
}
