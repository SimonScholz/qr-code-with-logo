package io.github.simonscholz.spi

import io.github.simonscholz.qrcode.spi.Graphics2DDelegate
import io.github.simonscholz.qrcode.spi.Graphics2DSpi
import org.apache.batik.dom.GenericDOMImplementation
import org.apache.batik.svggen.SVGGeneratorContext
import org.apache.batik.svggen.SVGGraphics2D
import org.w3c.dom.DOMImplementation
import java.io.Writer

class Graphics2DSvgSpi : Graphics2DSpi {
    override fun supportsFormat(format: String): Boolean = format == "svg"

    override fun createQrCode(
        delegate: Graphics2DDelegate,
        writer: Writer,
    ) {
        val domImpl: DOMImplementation = GenericDOMImplementation.getDOMImplementation()
        val svgNS = "http://www.w3.org/2000/svg"
        val document = domImpl.createDocument(svgNS, "svg", null)
        val ctx = SVGGeneratorContext.createDefault(document)
        ctx.comment = "Generated by https://github.com/SimonScholz/qr-code-with-logo"
        val svgGraphics = SVGGraphics2D(ctx, false)
        try {
            delegate.drawQrCode(svgGraphics)
            svgGraphics.stream(writer, true)
        } finally {
            svgGraphics.dispose()
        }
    }
}