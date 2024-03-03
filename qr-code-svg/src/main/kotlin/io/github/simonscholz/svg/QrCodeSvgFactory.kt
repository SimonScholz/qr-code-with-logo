package io.github.simonscholz.svg

import io.github.simonscholz.svg.internal.QrCodeSvgApiImpl

/**
 * Entry point for this library to create qr codes.
 */
object QrCodeSvgFactory {
    /**
     * Obtain an instance of the QrCodeApi to generate qr codes.
     *
     * @return QrCodeApi, which can be used to generate BufferedImages of custom qr codes.
     */
    @JvmStatic
    fun createQrCodeApi(): QrCodeSvgApi {
        return QrCodeSvgApiImpl()
    }
}
