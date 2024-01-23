package io.github.simonscholz.qrcode.spi

import java.io.Writer

/**
 * SPI to provide a Graphics2D instance for a given format, e.g., SVGGraphics2D from Apache Batik for svg.
 */
interface Graphics2DQrCodeSpi {
    fun supportsFormat(format: String): Boolean

    /**
     * Get a Graphics2D instance for the given format.
     *
     * @param delegate to draw the qr code on the Graphics2D instance
     */
    fun createQrCode(
        delegate: Graphics2DDelegate,
        writer: Writer,
    )
}
