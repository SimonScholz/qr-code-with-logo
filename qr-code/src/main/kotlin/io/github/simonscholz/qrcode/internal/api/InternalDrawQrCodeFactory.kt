package io.github.simonscholz.qrcode.internal.api

/**
 * Used to avoid exposing the implementation of the complete [QrCodeApiImpl] to clients.
 * Clients should use the [QrCodeApi] interface and [QrCodeFactory] to interact with the library.
 */
object InternalDrawQrCodeFactory {
    fun createInternalDrawQrCode(): InternalDrawQrCode = QrCodeApiImpl()
}
