package io.github.simonscholz.model

import io.github.simonscholz.qrcode.DEFAULT_IMG_SIZE
import io.github.simonscholz.qrcode.LogoShape
import io.github.simonscholz.qrcode.QrCodeDotShape
import org.eclipse.core.databinding.observable.value.WritableValue
import java.awt.Color

class QrCodeConfigViewModel {
    val qrCodeContent: WritableValue<String> = WritableValue("https://simonscholz.github.io/", String::class.java)
    val size: WritableValue<Int> = WritableValue(DEFAULT_IMG_SIZE, Int::class.java)
    val backgroundColor: WritableValue<Color> = WritableValue(Color.WHITE, Color::class.java)
    val foregroundColor: WritableValue<Color> = WritableValue(Color.BLACK, Color::class.java)
    val dotShape: WritableValue<QrCodeDotShape> = WritableValue(QrCodeDotShape.SQUARE, QrCodeDotShape::class.java)

    val logo: WritableValue<String> = WritableValue("", String::class.java)
    val logoBase64: WritableValue<String> = WritableValue("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Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiCiAgICB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIKICAgIHhtbG5zOnN0RXZ0PSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VFdmVudCMiCiAgICB4bWxuczpkYz0iaHR0cDovL3B1cmwub3JnL2RjL2VsZW1lbnRzLzEuMS8iCiAgICB4bWxuczpHSU1QPSJodHRwOi8vd3d3LmdpbXAub3JnL3htcC8iCiAgICB4bWxuczp0aWZmPSJodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyIKICAgIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIKICAgeG1wTU06RG9jdW1lbnRJRD0iZ2ltcDpkb2NpZDpnaW1wOmU2OGI5YmM3LTUwYzAtNGEyYS1hNTY2LTRlYTVkODkwOTc0ZCIKICAgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDoxYjMxMDk1MS1hMDdiLTQwZWEtODU3ZC05NmQwNDI0ZGU1MDIiCiAgIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo2NjM1OWY4Yy04ZTA0LTQ5YzYtYmQzZS1lNzlmMTdmOGVlNGUiCiAgIGRjOkZvcm1hdD0iaW1hZ2UvcG5nIgogICBHSU1QOkFQST0iMi4wIgogICBHSU1QOlBsYXRmb3JtPSJMaW51eCIKICAgR0lNUDpUaW1lU3RhbXA9IjE2OTY0MDc2MjMxNDk3NzkiCiAgIEdJTVA6VmVyc2lvbj0iMi4xMC4zMCIKICAgdGlmZjpPcmllbnRhdGlvbj0iMSIKICAgeG1wOkNyZWF0b3JUb29sPSJHSU1QIDIuMTAiPgogICA8eG1wTU06SGlzdG9yeT4KICAgIDxyZGY6U2VxPgogICAgIDxyZGY6bGkKICAgICAgc3RFdnQ6YWN0aW9uPSJzYXZlZCIKICAgICAgc3RFdnQ6Y2hhbmdlZD0iLyIKICAgICAgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDpmZjgzNDY0My03M2IxLTQ1ZjUtOTdlNi1hZGNjZDZhNjQ0MDciCiAgICAgIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkdpbXAgMi4xMCAoTGludXgpIgogICAgICBzdEV2dDp3aGVuPSIyMDIzLTEwLTA0VDEwOjIwOjIzKzAyOjAwIi8+CiAgICA8L3JkZjpTZXE+CiAgIDwveG1wTU06SGlzdG9yeT4KICA8L3JkZjpEZXNjcmlwdGlvbj4KIDwvcmRmOlJERj4KPC94OnhtcG1ldGE+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAKPD94cGFja2V0IGVuZD0idyI/PpkRdz0AAAAGYktHRAD/AP8A/6C9p5MAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfnCgQIFBehKQ1GAAAQm0lEQVRo3s2byY8c53nGf99XW1d3T6+zkDMccsRFqyUZtmzZgZxFthFHcOKLLkGAADkESJA/Ikj+hxwSIBdfkgAxDDi24NjyIlhJLCmOZa2kLIrb7NM9vXd1VX1LDlXsmREpcUbkSKpBEcOeYk099a7P874UrW7PcoxHOuwz3riG6e1A1EPqBHSS/dAJ0I6PKNaQtXmKJ5bxSjPH+TiI4wBsjWZw5RJq/R0KdkKp3sRxvQ/9NzpNGHbaxLKIt/wgM2fuByE+/YAH195BXf4V1XoNvxB+pHvE4xG93pDggScpLa18OgGryZjOyz+iHkoK5co9ebio36WnAxpf+CrS8+/JPeW9uMlkd4fuz/6NhcbMPQMLEFZqzM94tH/6ryT97qcD8KS9xfjl/2DhzArSce55zEnX5cSpU/Rf/A5xb/eTBZz0O4xeeY655RXEMSSYadxJyfzpFfov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", String::class.java)
    val useBase64Logo: WritableValue<Boolean> = WritableValue(true, Boolean::class.java)
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
