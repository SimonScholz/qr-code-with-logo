package io.github.simonscholz.qrcode

import io.github.simonscholz.qrcode.internal.api.QrCodeApiImpl

/**
 * Entry point for this library to create qr codes.
 */
object QrCodeFactory {
    /**
     * Obtain an instance of the QrCodeApi to generate qr codes.
     *
     * @return QrCodeApi, which can be used to generate BufferedImages of custom qr codes.
     */
    @JvmStatic
    fun createQrCodeApi(): QrCodeApi = QrCodeApiImpl()
}
