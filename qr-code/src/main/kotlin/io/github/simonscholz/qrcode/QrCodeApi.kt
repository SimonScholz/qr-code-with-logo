package io.github.simonscholz.qrcode

import java.awt.image.BufferedImage

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
     * Create a base64 encoded String of a qr code based on the given QrCodeConfig
     *
     * The imageFormatName can be changed to any supported format, e.g., jpg and others.
     * Be aware that the supported formats can also be enhanced via SPI.
     * Also see https://github.com/haraldk/TwelveMonkeys
     *
     * @param qrCodeConfig configuration about the qr code to be generated
     * @param imageFormatName pass in desired image format, defaults to "png"
     * @return base64 encoded String
     */
    fun createBase64QrCodeImage(
        qrCodeConfig: QrCodeConfig,
        imageFormatName: String = "png",
    ): String = createQrCodeImage(qrCodeConfig).toBase64(imageFormatName)
}
