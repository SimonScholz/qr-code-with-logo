package io.github.simonscholz.qrcode

import java.awt.image.BufferedImage
import java.io.Writer

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
     * Write the qr code using a Writer.
     * The format can be extended via ServiceLoader/SPI, but by default only BufferedImage and Base64 are supported.
     *
     * @param qrCodeConfig configuration about the qr code to be generated
     * @param writer Writer to write the qr code to
     * @param format format of the qr code, defaults to BufferedImage. Can be extended via ServiceLoader/SPI.
     */
    fun writeQrCode(
        qrCodeConfig: QrCodeConfig,
        writer: Writer,
        format: String = FORMAT_BUFFERED_IMAGE,
    )

    companion object {
        const val FORMAT_BUFFERED_IMAGE = "BufferedImage"
        const val FORMAT_BASE64 = "Base64"
    }
}
