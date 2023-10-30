package io.github.simonscholz.extension

import io.github.simonscholz.observables.DocumentObservable
import org.eclipse.core.databinding.observable.value.IObservableValue
import javax.swing.text.JTextComponent

fun JTextComponent.toObservable(): IObservableValue<String> =
    DocumentObservable(this)
