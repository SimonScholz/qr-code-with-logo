package io.github.simonscholz.observables

import org.eclipse.core.databinding.observable.Diffs
import org.eclipse.core.databinding.observable.value.AbstractObservableValue
import javax.swing.JComboBox

class JComboBoxSelectedItemObservable<E>(
    private val jComboBox: JComboBox<E>,
) : AbstractObservableValue<E>() {

    private var value: E = doGetValue()

    init {
        jComboBox.addActionListener {
            val oldValue = value
            value = doGetValue()
            fireValueChange(Diffs.createValueDiff(oldValue, value))
        }
    }

    override fun getValueType(): Any = Any::class.java

    override fun doGetValue(): E = jComboBox.selectedItem as E

    override fun doSetValue(value: E?) {
        jComboBox.selectedItem = value
    }
}
