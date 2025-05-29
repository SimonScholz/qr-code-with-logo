package io.github.simonscholz;


import io.github.simonscholz.qrcode.QrCodeConfig;
import io.github.simonscholz.svg.QrCodeSvgFactory;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class SVGSamples {
    public static void main(final String[] args) throws TransformerException {
        QrCodeConfig qrCodeConfig = new QrCodeConfig.Builder("https://simonscholz.dev/").build();
        Document qrCodeDocument = QrCodeSvgFactory.createQrCodeApi().createQrCodeSvg(qrCodeConfig);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(qrCodeDocument);
        StreamResult result = new StreamResult(new File("qr-code.svg"));
        transformer.transform(source, result);
    }
}
