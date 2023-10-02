package io.github.simonscholz.qrcode

import io.github.simonscholz.qrcode.api.QrCodeApiImpl

object QrCodeFactory {

    @JvmStatic
    fun createQrCodeApi(): QrCodeApi {
        return QrCodeApiImpl()
    }
}
