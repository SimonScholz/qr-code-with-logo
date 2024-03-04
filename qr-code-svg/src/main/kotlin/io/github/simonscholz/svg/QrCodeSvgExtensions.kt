package io.github.simonscholz.svg

import org.apache.batik.svggen.SVGGraphics2D
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.awt.Color

fun Document.getSVGDimensions(): Pair<Double, Double>? {
    val svgElements: NodeList = this.getElementsByTagName("svg")
    if (svgElements.length > 0) {
        val svgElement = svgElements.item(0) as Element
        val viewBox = svgElement.getAttribute("viewBox")
        if (viewBox.isNotBlank()) {
            val dimensions = viewBox.split(" ")
            if (dimensions.size == 4) {
                val width = dimensions[2].toDouble()
                val height = dimensions[3].toDouble()
                return Pair(width, height)
            }
        }
        // If viewBox is not specified or incorrect, try getting width and height attributes
        val widthAttribute = svgElement.getAttribute("width").toDoubleOrNull()
        val heightAttribute = svgElement.getAttribute("height").toDoubleOrNull()
        if (widthAttribute != null && heightAttribute != null) {
            return Pair(widthAttribute, heightAttribute)
        }
    }

    return null
}

fun Document.applySvgGraphics(svgGraphics: SVGGraphics2D) {
    val root = this.documentElement
    val svgContent = svgGraphics.root
    val svgChildren = svgContent.childNodes
    for (i in 0 until svgChildren.length) {
        val importedNode = this.importNode(svgChildren.item(i), true)
        root.appendChild(importedNode)
    }
}

fun Color.toRgbString(): String = "rgb(${this.red}, ${this.green}, ${this.blue})"
