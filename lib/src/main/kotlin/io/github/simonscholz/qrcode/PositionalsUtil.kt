package io.github.simonscholz.qrcode

import com.google.zxing.client.j2se.MatrixToImageConfig
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.Version
import com.google.zxing.qrcode.encoder.ByteMatrix
import com.google.zxing.qrcode.encoder.QRCode
import kotlin.math.max
import kotlin.math.min

data class PositionalSquare(
    val top: Int,
    val left: Int,
    val size: Int,
    val onColor: Int = MatrixToImageConfig.BLACK,
    val offColor: Int = MatrixToImageConfig.WHITE,
)

data class DataSquare(
    val black: Boolean,
    val x: Int,
    val y: Int,
    val size: Int,
)

// From Appendix E. Table 1, JIS0510X:2004 (p 71). The table was double-checked by komatsu.
private val POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE = arrayOf(
    intArrayOf(-1, -1, -1, -1, -1, -1, -1),
    intArrayOf(6, 18, -1, -1, -1, -1, -1),
    intArrayOf(6, 22, -1, -1, -1, -1, -1),
    intArrayOf(6, 26, -1, -1, -1, -1, -1),
    intArrayOf(6, 30, -1, -1, -1, -1, -1),
    intArrayOf(6, 34, -1, -1, -1, -1, -1),
    intArrayOf(6, 22, 38, -1, -1, -1, -1),
    intArrayOf(6, 24, 42, -1, -1, -1, -1),
    intArrayOf(6, 26, 46, -1, -1, -1, -1),
    intArrayOf(6, 28, 50, -1, -1, -1, -1),
    intArrayOf(6, 30, 54, -1, -1, -1, -1),
    intArrayOf(6, 32, 58, -1, -1, -1, -1),
    intArrayOf(6, 34, 62, -1, -1, -1, -1),
    intArrayOf(6, 26, 46, 66, -1, -1, -1),
    intArrayOf(6, 26, 48, 70, -1, -1, -1),
    intArrayOf(6, 26, 50, 74, -1, -1, -1),
    intArrayOf(6, 30, 54, 78, -1, -1, -1),
    intArrayOf(6, 30, 56, 82, -1, -1, -1),
    intArrayOf(6, 30, 58, 86, -1, -1, -1),
    intArrayOf(6, 34, 62, 90, -1, -1, -1),
    intArrayOf(6, 28, 50, 72, 94, -1, -1),
    intArrayOf(6, 26, 50, 74, 98, -1, -1),
    intArrayOf(6, 30, 54, 78, 102, -1, -1),
    intArrayOf(6, 28, 54, 80, 106, -1, -1),
    intArrayOf(6, 32, 58, 84, 110, -1, -1),
    intArrayOf(6, 30, 58, 86, 114, -1, -1),
    intArrayOf(6, 34, 62, 90, 118, -1, -1),
    intArrayOf(6, 26, 50, 74, 98, 122, -1),
    intArrayOf(6, 30, 54, 78, 102, 126, -1),
    intArrayOf(6, 26, 52, 78, 104, 130, -1),
    intArrayOf(6, 30, 56, 82, 108, 134, -1),
    intArrayOf(6, 34, 60, 86, 112, 138, -1),
    intArrayOf(6, 30, 58, 86, 114, 142, -1),
    intArrayOf(6, 34, 62, 90, 118, 146, -1),
    intArrayOf(6, 30, 54, 78, 102, 126, 150),
    intArrayOf(6, 24, 50, 76, 102, 128, 154),
    intArrayOf(6, 28, 54, 80, 106, 132, 158),
    intArrayOf(6, 32, 58, 84, 110, 136, 162),
    intArrayOf(6, 26, 54, 82, 110, 138, 166),
    intArrayOf(6, 30, 58, 86, 114, 142, 170),
)

object PositionalsUtil {

    fun renderResult(qrCode: QRCode, width: Int, height: Int, quietZone: Int): Pair<List<PositionalSquare>, List<DataSquare>> {
        require(qrCode.matrix != null) { "No matrix available on given QRCode" }

        val inputWidth: Int = qrCode.matrix.width
        val inputHeight: Int = qrCode.matrix.height
        val qrWidth = inputWidth + quietZone * 2
        val qrHeight = inputHeight + quietZone * 2
        val outputWidth = max(width, qrWidth)
        val outputHeight = max(height, qrHeight)

        val multiple = min(outputWidth / qrWidth, outputHeight / qrHeight)
        // Padding includes both the quiet zone and the extra white pixels to accommodate the requested
        // dimensions. For example, if input is 25x25 the QR will be 33x33 including the quiet zone.
        // If the requested size is 200x160, the multiple will be 4, for a QR of 132x132. These will
        // handle all the padding from 100x100 (the actual QR) up to 200x160.
        // Padding includes both the quiet zone and the extra white pixels to accommodate the requested
        // dimensions. For example, if input is 25x25 the QR will be 33x33 including the quiet zone.
        // If the requested size is 200x160, the multiple will be 4, for a QR of 132x132. These will
        // handle all the padding from 100x100 (the actual QR) up to 200x160.
        val leftPadding = (outputWidth - inputWidth * multiple) / 2
        val topPadding = (outputHeight - inputHeight * multiple) / 2

        val output = BitMatrix(outputWidth, outputHeight)

        val positionals = positionals(qrCode.version, qrCode.matrix)
        val mappedPoistionals = positionals.map {
            PositionalSquare(
                it.top * multiple + topPadding,
                it.left * multiple + leftPadding,
                it.size * multiple,
                it.onColor * multiple,
                it.offColor * multiple,
            )
        }

        val squares: List<DataSquare> = dataSquares(topPadding, inputHeight, multiple, leftPadding, inputWidth, qrCode, output)
        val removedPositionalsFromSquares: List<DataSquare> = squares.filter { !positionalsContains(positionals, it.x, it.y) }

        return Pair(mappedPoistionals, removedPositionalsFromSquares)
    }

    private fun positionalsContains(positionals: List<PositionalSquare>, x: Int, y: Int): Boolean =
        positionals
            .asSequence()
            .filter { (_, left): PositionalSquare -> left <= x }
            .filter { (_, left, size): PositionalSquare -> left + size >= x }
            .filter { (top): PositionalSquare -> top <= y }
            .filter { (top, _, size): PositionalSquare -> top + size >= y }
            .count() > 0L

    private fun dataSquares(topPadding: Int, inputHeight: Int, multiple: Int, leftPadding: Int, inputWidth: Int, qrCode: QRCode, output: BitMatrix): List<DataSquare> {
        val squares: MutableList<DataSquare> = mutableListOf()

        var inputY = 0
        var outputY = topPadding
        while (inputY < inputHeight - 1) {
            inputY++
            outputY += multiple

            var inputX = 0
            var outputX = leftPadding
            while (inputX < inputWidth - 1) {
                inputX++
                outputX += multiple

                if (qrCode.matrix.get(inputX, inputY).toInt() == 1) {
                    output.setRegion(outputX, outputY, multiple, multiple)
                    squares.add(DataSquare(true, outputX, outputY, multiple))
                } else {
                    squares.add(DataSquare(false, outputX, outputY, multiple))
                }
            }
        }
        return squares
    }

    @JvmStatic
    private fun positionals(version: Version, matrix: ByteMatrix): List<PositionalSquare> {
        val positionals: MutableList<PositionalSquare> = mutableListOf()
        val pdpWidth: Int = 7

        // Left top corner.
        positionals.add(PositionalSquare(0, 0, pdpWidth, 1, 1))
        // Right top corner.
        positionals.add(PositionalSquare(matrix.width - pdpWidth, 0, pdpWidth, 1, 1))
        // Left bottom corner.
        positionals.add(PositionalSquare(0, matrix.width - pdpWidth, pdpWidth, 1, 1))
        if (version.versionNumber < 2) { // The patterns appear if version >= 2
            return positionals
        }
        val index = version.versionNumber - 1
        val coordinates: IntArray = POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE[index]
        val numCoordinates: Int = 7
        for (i in 0 until numCoordinates) {
            for (j in 0 until numCoordinates) {
                val y = coordinates[i]
                val x = coordinates[j]
                if (x == -1 || y == -1) {
                    continue
                }
                // If the cell is unset, we embed the position adjustment pattern here.
                if (isEmpty(matrix[x, y].toInt())) {
                    // -2 is necessary since the x/y coordinates point to the center of the pattern, not the
                    // left top corner.
                    positionals.add(PositionalSquare(x - 2, y - 2, 5, 1, 1))
                }
            }
        }
        return positionals
    }

    private fun isEmpty(value: Int): Boolean = value == -1
}
