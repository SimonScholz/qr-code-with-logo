package io.github.simonscholz

import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.svg.QrCodeSvgFactory
import org.w3c.dom.Document
import java.io.File
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

fun main() {
    val svgConfig = QrCodeConfig.Builder("https://simonscholz.dev/").build()
    val qrCodeSvg = QrCodeSvgFactory.createQrCodeApi().createQrCodeSvg(svgConfig)
    qrCodeSvg.toFile(File("svg-qr-code.svg"))
}

fun Document.toFile(fileToSave: File) {
    val transformerFactory = TransformerFactory.newInstance()
    val transformer = transformerFactory.newTransformer()
    val source = DOMSource(this)
    val result = StreamResult(fileToSave)
    transformer.transform(source, result)
}
