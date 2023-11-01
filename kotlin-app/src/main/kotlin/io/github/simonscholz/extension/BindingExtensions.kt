package io.github.simonscholz.extension

import io.github.simonscholz.observables.BackgroundColorObservable
import io.github.simonscholz.observables.DocumentObservable
import io.github.simonscholz.observables.JSpinnerIntObservable
import java.awt.Color
import java.awt.Component
import org.eclipse.core.databinding.observable.value.IObservableValue
import javax.swing.JSpinner
import javax.swing.text.JTextComponent

fun JTextComponent.toObservable(): IObservableValue<String> =
    DocumentObservable(this)

fun JSpinner.toIntObservable(): IObservableValue<Int> = JSpinnerIntObservable(this)

fun Component.toBackgroundColorObservable(): IObservableValue<Color> =
    BackgroundColorObservable(this)
