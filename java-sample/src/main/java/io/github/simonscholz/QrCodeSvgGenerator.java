package io.github.simonscholz;

import io.github.simonscholz.qrcode.LogoShape;
import io.github.simonscholz.qrcode.QrCodeDotShape;
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig;
import io.github.simonscholz.svg.QrCodeSvgConfig;
import io.github.simonscholz.svg.QrCodeSvgFactory;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.String;
import java.util.Base64;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class QrCodeSvgGenerator {
  public static void main(final String[] args) throws IOException, TransformerException,
      ParserConfigurationException, SAXException {
    final var qrCodeGenerator = new QrCodeSvgGenerator();
    final var logo = parseBase64EncodedStringToDocument("Replace-by-Base64-encoded-SVGImage");
    final var qrCodeImage = qrCodeGenerator.generateQrCode(logo);
    final var transformerFactory = TransformerFactory.newInstance();
    final var transformer = transformerFactory.newTransformer();
    final var source = new DOMSource(qrCodeImage);
    final var result = new StreamResult(new File("qr-code.svg"));
    transformer.transform(source, result);
  }

  public static Document parseBase64EncodedStringToDocument(final String base64String) throws
      IOException, ParserConfigurationException, SAXException {
    final var xmlString = Base64.getDecoder().decode(base64String);
    final var factory = DocumentBuilderFactory.newInstance();
    final var builder = factory.newDocumentBuilder();
    final var inputSource = new InputSource(new ByteArrayInputStream(xmlString));
    return builder.parse(inputSource);
  }

  public Document generateQrCode(Document svgLogo) {
    final var qrPositionalSquaresConfig = new QrPositionalSquaresConfig.Builder()
            .circleShaped(true)
            .relativeSquareBorderRound(0.05)
            .centerColor(new Color(255, 0, 0))
            .innerSquareColor(new Color(255, 255, 255))
            .outerSquareColor(new Color(0, 0, 0))
            .outerBorderColor(new Color(255, 255, 255))
            .build();
    final var qrCodeConfig = new QrCodeSvgConfig.Builder("https://simonscholz.dev/}")
            .qrCodeSize(800)
            .qrCodeColorConfig(new Color(255, 255, 255), new Color(0, 0, 0))
            .qrLogoConfig(svgLogo, 0.2, new Color(255, 255, 255), LogoShape.CIRCLE)
            .qrBorderConfig(new Color(0, 0, 0), 0.05, 0.2)
            .qrPositionalSquaresConfig(qrPositionalSquaresConfig)
            .qrCodeDotStyler(QrCodeDotShape.SQUARE)
            .build();
    return QrCodeSvgFactory.createQrCodeApi().createQrCodeSvg(qrCodeConfig);
  }

  public byte[] createQrCodeByteArray(Document svgLogo) throws IOException, TransformerException {
    final var qrCodeGenerator = new QrCodeSvgGenerator();
    final var qrCodeImage = qrCodeGenerator.generateQrCode(svgLogo);
    final var byteArrayOutputStream = new ByteArrayOutputStream();
    final var transformer = TransformerFactory.newInstance().newTransformer();
    transformer.transform(new DOMSource(qrCodeImage), new StreamResult(byteArrayOutputStream));
    return byteArrayOutputStream.toByteArray();
  }
}
