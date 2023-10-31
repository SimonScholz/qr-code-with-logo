package io.github.simonscholz.model

import org.eclipse.core.databinding.observable.value.WritableValue
import java.awt.Color

class QrCodeConfigViewModel {
    val qrCodeContent: WritableValue<String> = WritableValue("https://simonscholz.github.io/", String::class.java)
    val size: WritableValue<Int> = WritableValue(200, Int::class.java)
    private val backgroundColor: WritableValue<Color> = WritableValue(Color.WHITE, Color::class.java)
    private val foregroundColor: WritableValue<Color> = WritableValue(Color.BLACK, Color::class.java)

    private val logo: WritableValue<String> = WritableValue("", String::class.java)

    private val borderColor: WritableValue<Color> = WritableValue(Color.BLACK, Color::class.java)
    private val relativeBorderSize: WritableValue<Float> = WritableValue()
    private val borderRadius: WritableValue<Float> = WritableValue()

    override fun toString(): String =
        "QrCodeConfigViewModel(qrCodeContent=${qrCodeContent.value}, size=${size.value}, backgroundColor=${backgroundColor.value}, foregroundColor=${foregroundColor.value}, logo=${logo.value}, borderColor=${borderColor.value}, relativeBorderSize=${relativeBorderSize.value}, borderRadius=${borderRadius.value})"
}
