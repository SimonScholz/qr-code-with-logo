package io.github.simonscholz.extension

import org.w3c.dom.Document
import java.io.File
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

fun Document.toFile(fileToSave: File) {
    val transformerFactory = TransformerFactory.newInstance()
    val transformer = transformerFactory.newTransformer()
    val source = DOMSource(this)
    val result = StreamResult(fileToSave)
    transformer.transform(source, result)
}

fun Document.toXmlString(prettyPrint: Boolean = true): String {
    val transformerFactory = TransformerFactory.newInstance()
    val transformer = transformerFactory.newTransformer()

    if (prettyPrint) {
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
    }

    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
    transformer.setOutputProperty(OutputKeys.METHOD, "xml")
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")

    val writer = StringWriter()
    transformer.transform(DOMSource(this), StreamResult(writer))
    return writer.toString()
}
