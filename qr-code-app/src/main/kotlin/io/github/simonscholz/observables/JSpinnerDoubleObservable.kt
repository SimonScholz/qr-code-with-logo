package io.github.simonscholz.observables

import org.eclipse.core.databinding.observable.Diffs
import org.eclipse.core.databinding.observable.value.AbstractObservableValue
import javax.swing.JSpinner

class JSpinnerDoubleObservable(
    private val jSpinner: JSpinner,
) : AbstractObservableValue<Double>() {
    private var value: Double = doGetValue()

    init {
        jSpinner.addChangeListener {
            val oldValue = value
            value = doGetValue()
            fireValueChange(Diffs.createValueDiff(oldValue, value))
        }
    }

    override fun doGetValue(): Double = jSpinner.value as Double

    override fun getValueType(): Any = Double::class.java

    override fun doSetValue(value: Double?) {
        jSpinner.value = value
    }
}
