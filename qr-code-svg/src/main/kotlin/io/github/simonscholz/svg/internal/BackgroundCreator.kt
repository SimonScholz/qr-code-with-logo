package io.github.simonscholz.svg.internal

import io.github.simonscholz.qrcode.LogoShape
import io.github.simonscholz.svg.toRgbString
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.awt.Color

internal object BackgroundCreator {
    fun createBackground(
        document: Document,
        bgColor: Color,
        logoShape: LogoShape,
        x: Double,
        y: Double,
        logoMaxSize: Double,
    ): Element =
        when (logoShape) {
            LogoShape.CIRCLE -> createCircle(document, bgColor, x, y, logoMaxSize)
            LogoShape.SQUARE -> createSquare(document, bgColor, x, y, logoMaxSize)
            LogoShape.ORIGINAL -> createOriginal(document, bgColor, x, y, logoMaxSize)
            LogoShape.ELLIPSE -> createEllipse(document, bgColor, x, y, logoMaxSize)
        }

    private fun createCircle(
        document: Document,
        bgColor: Color,
        x: Double,
        y: Double,
        size: Double,
    ): Element {
        val backgroundCircle = document.createElementNS("http://www.w3.org/2000/svg", "circle")
        backgroundCircle.setAttribute("cx", (x + size / 2).toString())
        backgroundCircle.setAttribute("cy", (y + size / 2).toString())
        backgroundCircle.setAttribute("r", (size / 2).toString())
        backgroundCircle.setAttribute("fill", bgColor.toRgbString())
        return backgroundCircle
    }

    private fun createSquare(
        document: Document,
        bgColor: Color,
        x: Double,
        y: Double,
        size: Double,
    ): Element {
        val backgroundSquare = document.createElementNS("http://www.w3.org/2000/svg", "rect")
        backgroundSquare.setAttribute("x", x.toString())
        backgroundSquare.setAttribute("y", y.toString())
        backgroundSquare.setAttribute("width", size.toString())
        backgroundSquare.setAttribute("height", size.toString())
        backgroundSquare.setAttribute("fill", bgColor.toRgbString())
        return backgroundSquare
    }

    private fun createOriginal(
        document: Document,
        bgColor: Color,
        x: Double,
        y: Double,
        size: Double,
    ): Element = createSquare(document, bgColor, x, y, size)

    private fun createEllipse(
        document: Document,
        bgColor: Color,
        x: Double,
        y: Double,
        size: Double,
    ): Element {
        val backgroundEllipse = document.createElementNS("http://www.w3.org/2000/svg", "ellipse")
        backgroundEllipse.setAttribute("cx", (x + size / 2).toString())
        backgroundEllipse.setAttribute("cy", (y + size / 2).toString())
        backgroundEllipse.setAttribute("rx", (size / 2).toString())
        backgroundEllipse.setAttribute("ry", (size / 2).toString())
        backgroundEllipse.setAttribute("fill", bgColor.toRgbString())
        return backgroundEllipse
    }
}
