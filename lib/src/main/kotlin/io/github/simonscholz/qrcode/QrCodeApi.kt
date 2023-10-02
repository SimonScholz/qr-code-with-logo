package io.github.simonscholz.qrcode

import java.awt.image.BufferedImage

interface QrCodeApi {

    fun createQrImage(
        qrCodeConfig: QrCodeConfig,
    ): BufferedImage
}
