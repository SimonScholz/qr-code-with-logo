package io.github.simonscholz;

import io.github.simonscholz.qrcode.LogoShape;
import io.github.simonscholz.qrcode.QrCodeConfig;
import io.github.simonscholz.qrcode.QrCodeDotShape;
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig;
import io.github.simonscholz.svg.QrCodeSvgFactory;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.String;
import java.util.Base64;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import io.github.simonscholz.svg.QrSvgLogoConfig;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class QrCodeSvgGenerator {
  public static void main(final String[] args) throws IOException, TransformerException,
      ParserConfigurationException, SAXException {
    final QrCodeSvgGenerator qrCodeGenerator = new QrCodeSvgGenerator();
    final Document logo = parseBase64EncodedStringToDocument("Replace-by-Base64-encoded-SVGImage");
    final Document qrCodeImage = qrCodeGenerator.generateQrCode(logo);
    final TransformerFactory transformerFactory = TransformerFactory.newInstance();
    final Transformer transformer = transformerFactory.newTransformer();
    final DOMSource source = new DOMSource(qrCodeImage);
    final StreamResult result = new StreamResult(new File("qr-code.svg"));
    transformer.transform(source, result);
  }

  public static Document parseBase64EncodedStringToDocument(final String base64String) throws
      IOException, ParserConfigurationException, SAXException {
    final byte[] xmlString = Base64.getDecoder().decode(base64String);
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder builder = factory.newDocumentBuilder();
    final InputSource inputSource = new InputSource(new ByteArrayInputStream(xmlString));
    return builder.parse(inputSource);
  }

  public Document generateQrCode(Document svgLogo) {
    final QrPositionalSquaresConfig qrPositionalSquaresConfig = new QrPositionalSquaresConfig.Builder()
            .circleShaped(true)
            .relativeSquareBorderRound(0.05)
            .centerColor(new Color(255, 0, 0))
            .innerSquareColor(new Color(255, 255, 255))
            .outerSquareColor(new Color(0, 0, 0))
            .outerBorderColor(new Color(255, 255, 255))
            .build();
    final QrCodeConfig qrCodeConfig = new QrCodeConfig.Builder("https://simonscholz.dev/")
            .qrCodeSize(800)
            .qrCodeColorConfig(new Color(255, 255, 255), new Color(0, 0, 0))
            .qrLogoConfig(new QrSvgLogoConfig(svgLogo,0.2, new Color(255, 255, 255), LogoShape.CIRCLE))
            .qrBorderConfig(new Color(0, 0, 0), 0.05, 0.2)
            .qrPositionalSquaresConfig(qrPositionalSquaresConfig)
            .qrCodeDotStyler(QrCodeDotShape.SQUARE)
            .build();
    return QrCodeSvgFactory.createQrCodeApi().createQrCodeSvg(qrCodeConfig);
  }

  public byte[] createQrCodeByteArray(Document svgLogo) throws IOException, TransformerException {
    final QrCodeSvgGenerator qrCodeGenerator = new QrCodeSvgGenerator();
    final Document qrCodeImage = qrCodeGenerator.generateQrCode(svgLogo);
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    final Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.transform(new DOMSource(qrCodeImage), new StreamResult(byteArrayOutputStream));
    return byteArrayOutputStream.toByteArray();
  }
}
