package io.github.simonscholz.svg

import org.w3c.dom.Document

interface QrCodeSvgApi {
    fun createQrCodeSvg(qrCodeSvgConfig: QrCodeSvgConfig): Document
}
