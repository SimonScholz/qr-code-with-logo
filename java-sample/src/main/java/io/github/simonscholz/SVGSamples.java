package io.github.simonscholz;


import io.github.simonscholz.svg.QrCodeSvgConfig;
import io.github.simonscholz.svg.QrCodeSvgFactory;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class SVGSamples {
    public static void main(final String[] args) throws TransformerException {
        var qrCodeConfig = new QrCodeSvgConfig.Builder("https://simonscholz.dev/}").build();
        var qrCodeDocument = QrCodeSvgFactory.createQrCodeApi().createQrCodeSvg(qrCodeConfig);
        var transformerFactory = TransformerFactory.newInstance();
        var transformer = transformerFactory.newTransformer();
        var source = new DOMSource(qrCodeDocument);
        var result = new StreamResult(new File("qr-code.svg"));
        transformer.transform(source, result);
    }
}
