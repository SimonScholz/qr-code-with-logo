package io.github.simonscholz.svg

import io.github.simonscholz.svg.internal.QrCodeSvgApiImpl

/**
 * Entry point for this library to create SVG qr codes.
 */
object QrCodeSvgFactory {
    /**
     * Obtain an instance of the QrCodeSvgApi to generate SVG qr codes.
     *
     * @return an instance of the QrCodeSvgApi to generate SVG qr codes as a Document.
     */
    @JvmStatic
    fun createQrCodeApi(): QrCodeSvgApi {
        return QrCodeSvgApiImpl()
    }
}
