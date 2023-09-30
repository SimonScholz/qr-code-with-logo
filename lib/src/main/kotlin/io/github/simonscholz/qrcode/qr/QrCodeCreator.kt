/**
 * MIT License
 *
 * Copyright (c) 2021 lome, Simon Scholz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.simonscholz.qrcode.qr

import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.Encoder
import com.google.zxing.qrcode.encoder.QRCode
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage

internal class QrCodeCreator {

    fun createQrImageWithPositionals(
        qrCodeText: String,
        size: Int = 200,
        circularPositionals: Boolean = false,
        relativePositionalsRound: Double = 0.5,
        fillColor: Color? = Color.BLACK,
        bgColor: Color = Color(0f, 0f, 0f, 0f),
        internalCircleColor: Color = Color.BLACK,
        quiteZone: Int = 1,
    ): BufferedImage {
        val qrCode: QRCode = Encoder.encode(qrCodeText, ErrorCorrectionLevel.H, encodeHintTypes())
        val (positionalSquares, dataSquares) = PositionalsUtil.renderResult(qrCode, size, quiteZone)

        val image = BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR_PRE)
        val graphics = image.graphics as Graphics2D

        return try {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

            // Data Squares
            dataSquares.forEach { s ->
                if (s.isFilled) {
                    graphics.color = fillColor
                    graphics.fillRect(s.x, s.y, s.size, s.size)
                } else {
                    graphics.color = bgColor
                    graphics.fillRect(s.x, s.y, s.size, s.size)
                }
            }

            positionalSquares.forEach {
                val r: Int = it.size
                var cx: Int = it.left
                var cy: Int = it.top
                val wr: Int = r - 2 * it.fillColorBorderWidth
                val ir: Int = r - 2 * it.fillColorBorderWidth - 2 * it.bgColorBorderWidth

                // White External Circle
                graphics.color = bgColor
                drawPositionalSquare(graphics, cx - 2, cy - 2, r + 4, r + 4, circularPositionals, relativePositionalsRound)

                // Black External Circle
                graphics.color = fillColor
                drawPositionalSquare(graphics, cx, cy, r, r, circularPositionals, relativePositionalsRound)
                cx += it.fillColorBorderWidth
                cy += it.fillColorBorderWidth
                // White Internal Circle
                graphics.color = bgColor
                drawPositionalSquare(graphics, cx, cy, wr, wr, circularPositionals, relativePositionalsRound)
                cx += it.bgColorBorderWidth
                cy += it.bgColorBorderWidth
                // Black Internal Circle
                graphics.color = internalCircleColor
                drawPositionalSquare(graphics, cx, cy, ir, ir, circularPositionals, relativePositionalsRound)
            }
            image
        } finally {
            graphics.dispose()
        }
    }

    private fun drawPositionalSquare(graphics: Graphics2D, x: Int, y: Int, width: Int, height: Int, circularPositionalSquares: Boolean, relativePositionalSquareRound: Double) {
        if (circularPositionalSquares) {
            graphics.fillArc(x, y, width, height, 0, 360)
        } else {
            graphics.fillRoundRect(x, y, width, height, (width * relativePositionalSquareRound).toInt(), (height * relativePositionalSquareRound).toInt())
        }
    }

    private fun encodeHintTypes() = mapOf(
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H,
        EncodeHintType.MARGIN to 0,
        EncodeHintType.CHARACTER_SET to "UTF-8",
    )
}
