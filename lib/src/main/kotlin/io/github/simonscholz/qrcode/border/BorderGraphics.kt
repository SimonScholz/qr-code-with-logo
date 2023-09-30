package io.github.simonscholz.qrcode.border

import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.floor

internal object BorderGraphics {
    fun drawBorder(graphics: Graphics2D, borderColor: Color, size: Int, relativeBorderRound: Double, relativeBorderSize: Double) {
        graphics.color = borderColor
        graphics.fillRoundRect(
            0,
            0,
            size,
            size,
            (size * relativeBorderRound).toInt(),
            (size * relativeBorderRound).toInt(),
        )
        graphics.color = Color.ORANGE
        val relativeSize = relativeSize(size, relativeBorderSize)
        graphics.fillRoundRect(
            relativeSize / 2,
            relativeSize / 2,
            size - relativeSize,
            size - relativeSize,
            ((size - relativeSize) * relativeBorderRound).toInt(),
            ((size - relativeSize) * relativeBorderRound).toInt(),
        )
    }

    private fun relativeSize(size: Int, percentage: Double): Int {
        require(percentage in 0.0..1.0)
        return floor(size * percentage).toInt()
    }
}
