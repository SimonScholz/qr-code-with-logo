package io.github.simonscholz.qrcode.spi

import java.io.OutputStream

/**
 * SPI to provide a Graphics2D instance for a given format, e.g., SVGGraphics2D from Apache Batik for svg.
 */
interface Graphics2DSpi {
    fun supportsFormat(format: String): Boolean

    /**
     * Get a Graphics2D instance for the given format.
     *
     * @param delegate to draw the qr code on the Graphics2D instance
     * @param outputStream to write the qr code to
     */
    fun createQrCode(
        delegate: Graphics2DDelegate,
        outputStream: OutputStream,
    )
}
