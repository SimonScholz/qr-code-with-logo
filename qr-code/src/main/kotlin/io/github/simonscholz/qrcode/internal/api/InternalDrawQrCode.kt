package io.github.simonscholz.qrcode.internal.api

import io.github.simonscholz.qrcode.QrCodeConfig
import java.awt.Graphics2D

/**
 * Do not use this in client code, unless you intend to create an extension.
 * This interface might change in the further development of this library and should not be considered stable.
 * For now the only purpose of this interface is to expose this functionality to the qr-code-svg module.
 */
fun interface InternalDrawQrCode {
    /**
     * Draw the qr code on the given Graphics2D instance, e.g., SVGGraphics2D.
     */
    fun drawQrCodeOnGraphics2D(
        qrCodeConfig: QrCodeConfig,
        graphics: Graphics2D,
    )
}
