package io.github.simonscholz.qrcode

import java.awt.Color
import java.awt.image.BufferedImage

interface QrCodeApi {

    fun createQrImage(
        qrCodeText: String,
        size: Int = 200,
        circularPositionals: Boolean = false,
        relativePositionalsRound: Double = 0.5,
        fillColor: Color? = Color.BLACK,
        bgColor: Color = Color(0f, 0f, 0f, 0f),
        internalCircleColor: Color = Color.BLACK,
        quiteZone: Int = 1,
        logo: BufferedImage? = null,
    ): BufferedImage
}
