package io.github.simonscholz.observables

import org.eclipse.core.databinding.observable.Diffs
import org.eclipse.core.databinding.observable.value.AbstractObservableValue
import javax.swing.AbstractButton

class ButtonSelectedObservable(
    private val button: AbstractButton,
) : AbstractObservableValue<Boolean>() {

    private var value: Boolean = button.isSelected

    init {
        button.addItemListener {
            val oldValue = value
            value = (it.item as AbstractButton).isSelected
            fireValueChange(Diffs.createValueDiff(oldValue, value))
        }
    }

    override fun doGetValue(): Boolean = button.isSelected

    override fun getValueType(): Any = Boolean::class.java

    override fun doSetValue(value: Boolean) {
        button.isSelected = value
    }
}
