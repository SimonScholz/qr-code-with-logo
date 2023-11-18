package io.github.simonscholz.model

import io.github.simonscholz.qrcode.DEFAULT_IMG_SIZE
import io.github.simonscholz.qrcode.LogoShape
import org.eclipse.core.databinding.observable.value.WritableValue
import java.awt.Color

class QrCodeConfigViewModel {
    val qrCodeContent: WritableValue<String> = WritableValue("https://simonscholz.github.io/", String::class.java)
    val size: WritableValue<Int> = WritableValue(DEFAULT_IMG_SIZE, Int::class.java)
    val backgroundColor: WritableValue<Color> = WritableValue(Color.WHITE, Color::class.java)
    val foregroundColor: WritableValue<Color> = WritableValue(Color.BLACK, Color::class.java)

    val logo: WritableValue<String> = WritableValue("", String::class.java)
    val logoRelativeSize: WritableValue<Double> = WritableValue(.2, Double::class.java)
    val logoBackgroundColor: WritableValue<Color> = WritableValue(Color.WHITE, Color::class.java)
    val logoShape: WritableValue<LogoShape> = WritableValue(LogoShape.CIRCLE, LogoShape::class.java)

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
        "QrCodeConfigViewModel(qrCodeContent=$qrCodeContent, size=$size, backgroundColor=$backgroundColor, foregroundColor=$foregroundColor, logo=$logo, logoRelativeSize=$logoRelativeSize, logoBackgroundColor=$logoBackgroundColor, borderColor=$borderColor, relativeBorderSize=$relativeBorderSize, borderRadius=$borderRadius, positionalSquareIsCircleShaped=$positionalSquareIsCircleShaped, positionalSquareRelativeBorderRound=$positionalSquareRelativeBorderRound, positionalSquareCenterColor=$positionalSquareCenterColor, positionalSquareInnerSquareColor=$positionalSquareInnerSquareColor, positionalSquareOuterSquareColor=$positionalSquareOuterSquareColor, positionalSquareOuterBorderColor=$positionalSquareOuterBorderColor)"
}
