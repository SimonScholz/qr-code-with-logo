package io.github.simonscholz

import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory
import io.github.simonscholz.qrcode.toBase64

/**
 * This is a sample to demonstrate the usage of the qr-code library with a base64 encoded image.
 *
 * It creates a qr code as a base64 encoded image and prints it to the console.
 */
fun main() {
    val createQrCodeApi = QrCodeFactory.createQrCodeApi()
    QrCodeConfig.Builder("https://simonscholz.dev/").build().run {
        val base64QrCodeImage = createQrCodeApi.createBase64QrCodeImage(this)
        println(base64QrCodeImage)

        val toBase64 = createQrCodeApi.createQrCodeImage(this).toBase64()
        println(toBase64)
    }
}
