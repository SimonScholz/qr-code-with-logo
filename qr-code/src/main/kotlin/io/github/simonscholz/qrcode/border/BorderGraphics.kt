package io.github.simonscholz.qrcode.border

import java.awt.Color
import java.awt.Graphics2D

internal object BorderGraphics {
    fun drawBorder(graphics: Graphics2D, borderColor: Color, bgColor: Color, size: Int, relativeBorderRound: Double, borderWidth: Int) {
        graphics.color = borderColor
        graphics.fillRoundRect(
            0,
            0,
            size,
            size,
            (size * relativeBorderRound).toInt(),
            (size * relativeBorderRound).toInt(),
        )
        graphics.color = bgColor
        graphics.fillRoundRect(
            borderWidth / 2,
            borderWidth / 2,
            size - borderWidth,
            size - borderWidth,
            ((size - borderWidth) * relativeBorderRound).toInt(),
            ((size - borderWidth) * relativeBorderRound).toInt(),
        )
    }
}
