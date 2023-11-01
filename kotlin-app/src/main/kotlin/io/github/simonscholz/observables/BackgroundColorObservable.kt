package io.github.simonscholz.observables

import java.awt.Color
import java.awt.Component
import org.eclipse.core.databinding.observable.Diffs
import org.eclipse.core.databinding.observable.value.AbstractObservableValue

class BackgroundColorObservable(
    private val component: Component,
) : AbstractObservableValue<Color>() {

    private var value: Color = component.background

    init {
        component.addPropertyChangeListener("background") {
            val oldValue = value
            value = doGetValue()
            fireValueChange(Diffs.createValueDiff(oldValue, value))
        }
    }

    override fun doGetValue(): Color = component.background

    override fun getValueType(): Any = Color::class.java

    override fun doSetValue(value: Color?) {
        component.background = value
    }
}
