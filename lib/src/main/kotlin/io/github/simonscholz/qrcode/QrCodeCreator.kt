package io.github.simonscholz.qrcode

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.client.j2se.MatrixToImageConfig
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.Encoder
import com.google.zxing.qrcode.encoder.QRCode
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.floor

enum class FileTypes(val value: String) {
    PNG("png"),
}

fun main() {
    val qrCodeCreator = QrCodeCreator()
//    val bufferedImage: BufferedImage = qrCodeCreator.createQrImage(
//        qrCodeText = "https://simonscholz.github.io/",
//        onColor = Color(0x0063, 0x000B, 0x00A5).rgb
//    )
    // ImageIO.write(bufferedImage, FileTypes.PNG.value, File("/home/simon/Pictures/qr-codes/qr-code-5.png"));

    qrCodeCreator.createQrImageWithPositionals("https://simonscholz.github.io/", circularPositionals = true, relativePositionalsRound = 0.5).let {
        ImageIO.write(it, FileTypes.PNG.value, File("/home/simon/Pictures/qr-codes/qr-positional-2.png"))
    }
}

class QrCodeCreator {

    fun createQrImageWithPositionals(qrCodeText: String, size: Int = 200, circularPositionals: Boolean = false, relativePositionalsRound: Double = 0.5): BufferedImage {
        val qrCode: QRCode = Encoder.encode(qrCodeText, ErrorCorrectionLevel.H, encodeHintTypes())
        val (positionals, dataSquares) = PositionalsUtil.renderResult(qrCode, size, size, 0)

        val image = BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR_PRE)
        val graphics = image.graphics as Graphics2D
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

        // Data Squares
        dataSquares.forEach { s ->
            if (s.black) {
                graphics.color = Color.BLACK
                graphics.fillRect(s.x, s.y, s.size, s.size)
            } else {
                graphics.color = Color.WHITE
                graphics.fillRect(s.x, s.y, s.size, s.size)
            }
        }

        // Positionals
        positionals.forEach { p ->
            val r: Int = p.size
            var cx: Int = p.left
            var cy: Int = p.top
            val wr: Int = r - 2 * p.onColor
            val ir: Int = r - 2 * p.onColor - 2 * p.offColor

            // White External Circle
            graphics.color = Color.WHITE
            drawPositional(graphics, cx - 2, cy - 2, r + 4, r + 4, circularPositionals, relativePositionalsRound)

            // Black External Circle
            graphics.color = Color.BLACK
            drawPositional(graphics, cx, cy, r, r, circularPositionals, relativePositionalsRound)
            cx += p.onColor
            cy += p.onColor
            // White Internal Circle
            graphics.color = Color.WHITE
            drawPositional(graphics, cx, cy, wr, wr, circularPositionals, relativePositionalsRound)
            cx += p.offColor
            cy += p.offColor
            // Black Internal Circle
            graphics.color = Color.ORANGE
            drawPositional(graphics, cx, cy, ir, ir, circularPositionals, relativePositionalsRound)
        }

        return image
    }

    private fun drawPositional(graphics: Graphics2D, x: Int, y: Int, width: Int, height: Int, circularPositionals: Boolean, relativePositionalsRound: Double) {
        if (circularPositionals) {
            graphics.fillArc(x, y, width, height, 0, 360)
        } else {
            graphics.fillRoundRect(x, y, width, height, (width * relativePositionalsRound).toInt(), (height * relativePositionalsRound).toInt())
        }
    }

    /**
     * @param qrCodeText the text to encode as a QR Code, must be non-null
     * @param size the side length of the QR Code, must be non-negative
     * @param onColor pixel on color, specified as an ARGB value as an int
     * @param offColor pixel off color, specified as an ARGB value as an int
     */
    fun createQrImage(
        qrCodeText: String,
        size: Int = 200,
        onColor: Int = MatrixToImageConfig.BLACK,
        offColor: Int = MatrixToImageConfig.WHITE,
    ): BufferedImage {
        require(size >= 0)
        require(onColor != offColor)

        val qrCodeWriter = QRCodeWriter()
        val byteMatrix: BitMatrix = qrCodeWriter.encode(
            qrCodeText,
            BarcodeFormat.QR_CODE,
            size,
            size,
            encodeHintTypes(),
        )
        return MatrixToImageWriter.toBufferedImage(byteMatrix, MatrixToImageConfig(onColor, offColor))
    }

    private fun relativeSize(size: Int, percentage: Double): Int {
        require(percentage in 0.0..1.0)
        return floor(size * percentage).toInt()
    }

    private fun encodeHintTypes() = mapOf(
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H,
        EncodeHintType.MARGIN to 0,
        EncodeHintType.CHARACTER_SET to "UTF-8",
    )
}
