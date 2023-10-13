package io.github.simonscholz.qrcode.internal.border

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Stroke

internal object BorderGraphics {
    fun drawBorder(graphics: Graphics2D, borderColor: Color, size: Int, relativeBorderRound: Double, borderWidth: Int) {
        graphics.color = borderColor

        val oldStroke: Stroke = graphics.stroke
        graphics.stroke = BasicStroke(borderWidth.toFloat())
        graphics.drawRoundRect(
            borderWidth / 2,
            borderWidth / 2,
            size - borderWidth,
            size - borderWidth,
            (size * relativeBorderRound).toInt(),
            (size * relativeBorderRound).toInt(),
        )
        graphics.stroke = oldStroke
    }
}
