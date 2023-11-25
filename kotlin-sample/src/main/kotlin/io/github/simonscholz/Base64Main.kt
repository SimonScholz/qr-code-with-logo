package io.github.simonscholz

import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory

/**
 * This is a sample to demonstrate the usage of the qr-code library with a base64 encoded image.
 *
 * It creates a qr code as a base64 encoded image and prints it to the console.
 */
fun main() {
    val base64QrCodeImage = QrCodeFactory.createQrCodeApi().createBase64QrCodeImage(
        QrCodeConfig.Builder("https://simonscholz.github.io/").build(),
    )

    println(base64QrCodeImage)
}
