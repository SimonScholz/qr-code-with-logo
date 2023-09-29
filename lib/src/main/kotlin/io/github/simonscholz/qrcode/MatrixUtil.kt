package io.github.simonscholz.qrcode

import com.google.zxing.WriterException
import com.google.zxing.qrcode.encoder.ByteMatrix

object MatrixUtil {

    private val POSITION_DETECTION_PATTERN = arrayOf(
        intArrayOf(1, 1, 1, 1, 1, 1, 1),
        intArrayOf(1, 0, 0, 0, 0, 0, 1),
        intArrayOf(1, 0, 1, 1, 1, 0, 1),
        intArrayOf(1, 0, 1, 1, 1, 0, 1),
        intArrayOf(1, 0, 1, 1, 1, 0, 1),
        intArrayOf(1, 0, 0, 0, 0, 0, 1),
        intArrayOf(1, 1, 1, 1, 1, 1, 1),
    )

    fun embedPositionDetectionPatternsAndSeparators(matrix: ByteMatrix) {
        // Embed three big squares at corners.
        val pdpWidth: Int = 7
        // Left top corner.
        embedPositionDetectionPattern(0, 0, matrix)
        // Right top corner.
        embedPositionDetectionPattern(matrix.width - pdpWidth, 0, matrix)
        // Left bottom corner.
        embedPositionDetectionPattern(0, matrix.width - pdpWidth, matrix)

        // Embed horizontal separation patterns around the squares.
        val hspWidth = 8
        // Left top corner.
        embedHorizontalSeparationPattern(0, hspWidth - 1, matrix)
        // Right top corner.
        embedHorizontalSeparationPattern(
            matrix.width - hspWidth,
            hspWidth - 1,
            matrix,
        )
        // Left bottom corner.
        embedHorizontalSeparationPattern(0, matrix.width - hspWidth, matrix)

        // Embed vertical separation patterns around the squares.
        val vspSize = 7
        // Left top corner.
        embedVerticalSeparationPattern(vspSize, 0, matrix)
        // Right top corner.
        embedVerticalSeparationPattern(matrix.height - vspSize - 1, 0, matrix)
        // Left bottom corner.
        embedVerticalSeparationPattern(
            vspSize,
            matrix.height - vspSize,
            matrix,
        )
    }
    private fun embedPositionDetectionPattern(xStart: Int, yStart: Int, matrix: ByteMatrix) {
        for (y in 0..6) {
            for (x in 0..6) {
                matrix.set(xStart + x, yStart + y, POSITION_DETECTION_PATTERN[y][x])
            }
        }
    }

    private fun embedHorizontalSeparationPattern(
        xStart: Int,
        yStart: Int,
        matrix: ByteMatrix,
    ) {
        for (x in 0..7) {
            if (!PositionalsUtil.isEmpty(matrix[xStart + x, yStart].toInt())) {
                throw WriterException()
            }
            matrix[xStart + x, yStart] = 0
        }
    }

    private fun embedVerticalSeparationPattern(
        xStart: Int,
        yStart: Int,
        matrix: ByteMatrix,
    ) {
        for (y in 0..6) {
            if (!PositionalsUtil.isEmpty(matrix[xStart, yStart + y].toInt())) {
                throw WriterException()
            }
            matrix[xStart, yStart + y] = 0
        }
    }

    fun embedDarkDotAtLeftBottomCorner(matrix: ByteMatrix) {
        if (matrix[8, matrix.height - 8].toInt() == 0) {
            throw WriterException()
        }
        matrix[8, matrix.height - 8] = 1
    }
}
