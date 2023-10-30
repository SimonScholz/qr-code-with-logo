package io.github.simonscholz.model

import org.eclipse.core.databinding.observable.value.WritableValue
import java.awt.Color

class QrCodeConfigViewModel {
    val qrCodeContent: WritableValue<String> = WritableValue("https://simonscholz.github.io/", String::class.java)
    val size: WritableValue<Int> = WritableValue()
    private val backgroundColor: WritableValue<Color> = WritableValue()
    private val foregroundColor: WritableValue<Color> = WritableValue()

    private val logo: WritableValue<String> = WritableValue()

    private val borderColor: WritableValue<Color> = WritableValue()
    private val relativeBorderSize: WritableValue<String> = WritableValue()
    private val borderRadius: WritableValue<String> = WritableValue()
}
