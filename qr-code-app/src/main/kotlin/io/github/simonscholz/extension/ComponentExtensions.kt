package io.github.simonscholz.extension

import javax.swing.JComboBox

fun JComboBox<*>.changeItemOnScroll() {
    addMouseWheelListener { e ->
        val scrollAmount = e.wheelRotation
        val component = e.component
        if (component is JComboBox<*>) {
            val selectedIndex: Int = component.getSelectedIndex()
            val newSelectedIndex = 0.coerceAtLeast((component.itemCount - 1).coerceAtMost(selectedIndex + scrollAmount))
            component.setSelectedIndex(newSelectedIndex)
        }
    }
}
