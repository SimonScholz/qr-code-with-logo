package io.github.simonscholz.model

import org.eclipse.core.databinding.observable.value.WritableValue
import java.awt.Color

class QrCodeConfigViewModel {
    val qrCodeContent: WritableValue<String> = WritableValue("https://simonscholz.github.io/", String::class.java)
    val size: WritableValue<Int> = WritableValue(200, Int::class.java)
    val backgroundColor: WritableValue<Color> = WritableValue(Color.WHITE, Color::class.java)
    val foregroundColor: WritableValue<Color> = WritableValue(Color.BLACK, Color::class.java)

    val logo: WritableValue<String> = WritableValue("", String::class.java)
    val logoRelativeSize: WritableValue<Double> = WritableValue(.2, Double::class.java)
    val logoBackgroundColor: WritableValue<Color> = WritableValue(Color.WHITE, Color::class.java)

    val borderColor: WritableValue<Color> = WritableValue(Color.BLACK, Color::class.java)
    val relativeBorderSize: WritableValue<Double> = WritableValue(.05, Double::class.java)
    val borderRadius: WritableValue<Double> = WritableValue(.2, Double::class.java)

    val positionalSquareIsCircleShaped: WritableValue<Boolean> = WritableValue(true, Boolean::class.java)
    val positionalSquareRelativeBorderRound: WritableValue<Double> = WritableValue(.05, Double::class.java)
    val positionalSquareCenterColor: WritableValue<Color> = WritableValue(Color.RED, Color::class.java)
    val positionalSquareInnerSquareColor: WritableValue<Color> = WritableValue(Color.WHITE, Color::class.java)
    val positionalSquareOuterSquareColor: WritableValue<Color> = WritableValue(Color.BLACK, Color::class.java)
    val positionalSquareOuterBorderColor: WritableValue<Color> = WritableValue(Color.WHITE, Color::class.java)

    override fun toString(): String =
        "QrCodeConfigViewModel(qrCodeContent=${qrCodeContent.value}, size=${size.value}, backgroundColor=${backgroundColor.value}, foregroundColor=${foregroundColor.value}, logo=${logo.value}, borderColor=${borderColor.value}, relativeBorderSize=${relativeBorderSize.value}, borderRadius=${borderRadius.value})"
}