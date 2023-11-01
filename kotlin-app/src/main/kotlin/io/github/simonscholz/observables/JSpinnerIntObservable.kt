package io.github.simonscholz.observables

import org.eclipse.core.databinding.observable.Diffs
import org.eclipse.core.databinding.observable.value.AbstractObservableValue
import javax.swing.JSpinner

class JSpinnerIntObservable(
    private val jSpinner: JSpinner,
) : AbstractObservableValue<Int>() {

    private var value: Int = jSpinner.value as Int

    init {
        jSpinner.addChangeListener {
            val oldValue = value
            value = doGetValue()
            fireValueChange(Diffs.createValueDiff(oldValue, value))
        }
    }

    override fun doGetValue(): Int =
        jSpinner.value as Int

    override fun getValueType(): Any = Int::class.java

    override fun doSetValue(value: Int?) {
        jSpinner.value = value
    }
}
