package io.github.simonscholz.extension

import io.github.simonscholz.observables.DocumentObservable
import io.github.simonscholz.observables.JSpinnerIntObservable
import org.eclipse.core.databinding.observable.value.IObservableValue
import javax.swing.JSpinner
import javax.swing.text.JTextComponent

fun JTextComponent.toObservable(): IObservableValue<String> =
    DocumentObservable(this)

fun JSpinner.toIntObservable(): IObservableValue<Int> = JSpinnerIntObservable(this)
