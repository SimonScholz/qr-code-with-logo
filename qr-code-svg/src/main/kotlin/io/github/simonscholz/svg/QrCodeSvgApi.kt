package io.github.simonscholz.svg

import io.github.simonscholz.qrcode.QrCodeConfig
import org.w3c.dom.Document

interface QrCodeSvgApi {
    fun createQrCodeSvg(qrCodeSvgConfig: QrCodeConfig): Document
}
