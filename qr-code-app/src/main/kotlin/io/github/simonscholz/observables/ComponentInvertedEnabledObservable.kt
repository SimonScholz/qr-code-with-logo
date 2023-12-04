package io.github.simonscholz.observables

import org.eclipse.core.databinding.observable.value.AbstractObservableValue
import java.awt.Component

class ComponentInvertedEnabledObservable(
    private val component: Component,
) : AbstractObservableValue<Boolean>() {
    override fun doGetValue(): Boolean = !component.isEnabled

    override fun getValueType(): Any = Boolean::class.java

    override fun doSetValue(value: Boolean) {
        component.isEnabled = !value
    }
}
