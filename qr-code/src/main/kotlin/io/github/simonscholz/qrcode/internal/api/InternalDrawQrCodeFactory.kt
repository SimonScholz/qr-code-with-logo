package io.github.simonscholz.qrcode.internal.api

/**
 * Used to avoid exposing the implementation of the complete [QrCodeApiImpl] to clients.
 * Clients should use the [io.github.simonscholz.qrcode.QrCodeApi] interface and [io.github.simonscholz.qrcode.QrCodeFactory] to interact with the library.
 */
object InternalDrawQrCodeFactory {
    fun createInternalDrawQrCode(): InternalDrawQrCode = QrCodeApiImpl()
}
