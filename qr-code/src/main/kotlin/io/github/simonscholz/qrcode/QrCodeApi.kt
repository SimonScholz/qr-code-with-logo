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
}
