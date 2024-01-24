package io.github.simonscholz.qrcode

import java.awt.image.BufferedImage
import java.io.OutputStream

interface QrCodeApi {
    /**
     * Create a BufferedImage of a qr code based on the given QrCodeConfig
     *
     * @param qrCodeConfig configuration about the qr code to be generated
     * @return BufferedImage
     */
    fun createQrCodeImage(qrCodeConfig: QrCodeConfig): BufferedImage

    /**
     * Create a base64 encoded String of a qr code based on the given QrCodeConfig
     *
     * @param qrCodeConfig configuration about the qr code to be generated
     * @return base64 encoded String
     */
    fun createBase64QrCodeImage(qrCodeConfig: QrCodeConfig): String = createQrCodeImage(qrCodeConfig).toBase64()

    /**
     * Write the qr code using an OutputStream.
     * The format can be extended via ServiceLoader/SPI, but by default only BufferedImage and Base64 are supported.
     * The qr-code-svg module adds support for the "svg" format.
     *
     * @param qrCodeConfig configuration about the qr code to be generated
     * @param outputStream OutputStream to output the qr code
     * @param format format of the qr code, defaults to BufferedImage. Can be extended via ServiceLoader/SPI.
     */
    fun outputQrCode(
        qrCodeConfig: QrCodeConfig,
        outputStream: OutputStream,
        format: String = FORMAT_BUFFERED_IMAGE,
    )

    companion object {
        const val FORMAT_BUFFERED_IMAGE = "BufferedImage"
        const val FORMAT_BASE64 = "Base64"
    }
}
