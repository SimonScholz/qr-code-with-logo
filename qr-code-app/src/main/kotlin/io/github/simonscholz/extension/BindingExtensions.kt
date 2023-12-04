package io.github.simonscholz.extension

import io.github.simonscholz.observables.BackgroundColorObservable
import io.github.simonscholz.observables.ButtonSelectedObservable
import io.github.simonscholz.observables.ComponentEnabledObservable
import io.github.simonscholz.observables.ComponentInvertedEnabledObservable
import io.github.simonscholz.observables.DocumentObservable
import io.github.simonscholz.observables.JComboBoxSelectedItemObservable
import io.github.simonscholz.observables.JSpinnerDoubleObservable
import io.github.simonscholz.observables.JSpinnerIntObservable
import org.eclipse.core.databinding.observable.value.IObservableValue
import java.awt.Color
import java.awt.Component
import javax.swing.AbstractButton
import javax.swing.JComboBox
import javax.swing.JSpinner
import javax.swing.text.JTextComponent

fun JTextComponent.toObservable(): IObservableValue<String> = DocumentObservable(this)

fun JSpinner.toIntObservable(): IObservableValue<Int> = JSpinnerIntObservable(this)

fun JSpinner.toDoubleObservable(): IObservableValue<Double> = JSpinnerDoubleObservable(this)

fun Component.toBackgroundColorObservable(): IObservableValue<Color> = BackgroundColorObservable(this)

fun Component.toEnabledObservable(): IObservableValue<Boolean> = ComponentEnabledObservable(this)

fun Component.toEnabledInvertedObservable(): IObservableValue<Boolean> = ComponentInvertedEnabledObservable(this)

fun AbstractButton.toButtonSelectedObservable(): IObservableValue<Boolean> = ButtonSelectedObservable(this)

fun <E> JComboBox<E>.toSelectedItemObservable(): IObservableValue<E> = JComboBoxSelectedItemObservable(this)
