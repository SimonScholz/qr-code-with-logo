package io.github.simonscholz.observables

import java.awt.Component
import org.eclipse.core.databinding.observable.value.AbstractObservableValue

class ComponentInvertedEnabledObservable(
    private val component: Component,
) : AbstractObservableValue<Boolean>() {

    override fun doGetValue(): Boolean = !component.isEnabled

    override fun getValueType(): Any = Boolean::class.java

    override fun doSetValue(value: Boolean) {
        component.isEnabled = !value
    }
}
