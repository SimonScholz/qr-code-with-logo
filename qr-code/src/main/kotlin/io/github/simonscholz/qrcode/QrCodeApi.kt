package io.github.simonscholz.qrcode

import java.awt.image.BufferedImage

interface QrCodeApi {

    /**
     * Create a BufferedImage of a qr code based on the given QrCodeConfig
     *
     * @param qrCodeConfig configuration about the qr code to be generated
     * @return BufferedImage
     */
    fun createQrCodeImage(
        qrCodeConfig: QrCodeConfig,
    ): BufferedImage

    /**
     * Create a base64 encoded String of a qr code based on the given QrCodeConfig
     *
     * @param qrCodeConfig configuration about the qr code to be generated
     * @return base64 encoded String
     */
    fun createBase64QrCodeImage(
        qrCodeConfig: QrCodeConfig,
    ): String = createQrCodeImage(qrCodeConfig).toBase64()
}
