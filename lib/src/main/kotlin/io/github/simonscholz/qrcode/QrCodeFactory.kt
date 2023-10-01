package io.github.simonscholz.qrcode

import io.github.simonscholz.qrcode.api.QrCodeApiImpl
import java.awt.Color
import java.awt.image.BufferedImage

object QrCodeFactory {

    @JvmStatic
    fun createQrCodeApi(): QrCodeApi {
        return QrCodeApiImpl()
    }

    @JvmStatic
    @JvmOverloads
    fun createQrImage(
        qrCodeText: String,
        size: Int = 200,
        circularPositionals: Boolean = false,
        relativePositionalsRound: Double = 0.5,
        fillColor: Color? = Color.BLACK,
        bgColor: Color = Color.WHITE,
        internalCircleColor: Color = Color.BLACK,
        quiteZone: Int = 1,
        logo: BufferedImage? = null,
    ): BufferedImage = createQrCodeApi().createQrImage(
        qrCodeText,
        size,
        circularPositionals,
        relativePositionalsRound,
        fillColor,
        bgColor,
        internalCircleColor,
        quiteZone,
        logo,
    )
}
