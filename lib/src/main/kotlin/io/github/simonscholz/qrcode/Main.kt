package io.github.simonscholz.qrcode

import io.github.simonscholz.qrcode.qr.QrCodeCreator
import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

internal fun main() {
    val qrCodeCreator = QrCodeCreator()
    val qrCode = qrCodeCreator.createQrImageWithPositionals(
        "https://github.com/lome/niceqr", // "https://simonscholz.github.io/",
        circularPositionals = true,
        relativePositionalsRound = 0.2,
        fillColor = Color(0x0063, 0x000B, 0x00A5),
        bgColor = Color(0f, 0f, 0f, 0f),
        internalCircleColor = Color.RED,
        quiteZone = 1,
    )

    // ImageIO.write(qrCode, "png", File("/home/simon/Pictures/qr-codes/qr-positional-30.png"))

    val logo = ImageIO.read(File("/home/simon/Pictures/qr-codes/e&v.jpg"))

    QrCodeFactory.createQrCodeApi().createQrImage(
        "https://integ.doorbit.com/listings/?origin=qr-code",
        circularPositionals = true,
        relativePositionalsRound = 0.2,
        fillColor = Color.BLACK,
        bgColor = Color.WHITE, //Color(0f, 0f, 0f, 0f),
        internalCircleColor = Color.RED,
        quiteZone = 1,
        logo = logo,
    ).let {
        ImageIO.write(it, "png", File("/home/simon/Pictures/qr-codes/e&v-doorbit-qr.png"))
    }
}
