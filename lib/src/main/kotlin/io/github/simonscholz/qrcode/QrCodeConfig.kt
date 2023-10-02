package io.github.simonscholz.qrcode

import java.awt.Color
import java.awt.image.BufferedImage

data class QrCodeConfig @JvmOverloads constructor(val qrCodeText: String, val qrCodeSize: Int, val qrCode: QrCode = QrCode(), val border: Border = Border(), val positionalSquares: PositionalSquares = PositionalSquares(), val logo: Logo? = null)

data class QrCode @JvmOverloads constructor(val bgColor: Color = Color.WHITE, val fillColor: Color = Color.BLACK)

data class Border @JvmOverloads constructor(val color: Color = Color.BLACK, val relativeSize: Double = .05) {
    init {
        require(relativeSize in 0.0..1.0) { "relativeSize must be in between 0 and 1." }
    }
}

data class PositionalSquares @JvmOverloads constructor(
    val rindColor: Color = Color.BLACK,
    val centerColor: Color = Color.BLACK,
    val outerColor: Color = Color.WHITE,
    val isCircleShaped: Boolean = false,
    val relativeSquareBorderRound: Double = .5,
) {
    init {
        require(relativeSquareBorderRound in 0.0..1.0) { "relativeSquareBorderRound must be in between 0 and 1." }
    }
}

data class Logo @JvmOverloads constructor(val logo: BufferedImage, val relativeSize: Double = .2) {
    init {
        require(relativeSize in 0.0..1.0) { "relativeSize must be in between 0 and 1." }
    }
}
