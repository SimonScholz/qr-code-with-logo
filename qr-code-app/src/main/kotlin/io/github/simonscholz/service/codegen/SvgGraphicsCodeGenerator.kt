package io.github.simonscholz.service.codegen

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.qrcode.LogoShape
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeDotShape
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig
import io.github.simonscholz.svg.QrCodeSvgFactory
import io.github.simonscholz.svg.QrSvgLogoConfig
import org.w3c.dom.Document
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.awt.Color
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import javax.lang.model.element.Modifier
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class SvgGraphicsCodeGenerator(
    private val qrCodeConfigViewModel: QrCodeConfigViewModel,
) {
    @OptIn(ExperimentalEncodingApi::class)
    fun generateKotlinCode(): String {
        val file = FileSpec.builder("io.github.simonscholz", "QrCodeGenerator")

        file.addImport(
            "io.github.simonscholz.qrcode",
            "LogoShape",
            "QrPositionalSquaresConfig",
            "QrCodeDotShape",
        )
        file.addImport("java.io", "File")

        val generateQrCodeFunction =
            FunSpec
                .builder("generateQrCode")
                .addParameter("svgLogo", Document::class)
                .addStatement(
                    """
            |val qrPositionalSquaresConfig = %T.Builder()
            |    .circleShaped(${qrCodeConfigViewModel.positionalSquareIsCircleShaped.value})
            |    .relativeSquareBorderRound(${qrCodeConfigViewModel.positionalSquareRelativeBorderRound.value})
            |    .centerColor(${colorInstanceStringKotlin(qrCodeConfigViewModel.positionalSquareCenterColor.value)})
            |    .innerSquareColor(${colorInstanceStringKotlin(qrCodeConfigViewModel.positionalSquareInnerSquareColor.value)})
            |    .outerSquareColor(${colorInstanceStringKotlin(qrCodeConfigViewModel.positionalSquareOuterSquareColor.value)})
            |    .outerBorderColor(${colorInstanceStringKotlin(qrCodeConfigViewModel.positionalSquareOuterBorderColor.value)})
            |    .colorAdjustmentPatterns(${qrCodeConfigViewModel.positionalSquareColorAdjustmentPatterns.value})
            |    .build()
                    """.trimMargin(),
                    QrPositionalSquaresConfig::class,
                    Color::class,
                    Color::class,
                    Color::class,
                    Color::class,
                ).addStatement(
                    """
            |val qrCodeConfig = %T.Builder("${qrCodeConfigViewModel.qrCodeContent.value}")
            |    .qrCodeSize(${qrCodeConfigViewModel.size.value})
            |    .qrCodeColorConfig(
            |        bgColor = ${colorInstanceStringKotlin(qrCodeConfigViewModel.backgroundColor.value)},
            |        fillColor = ${colorInstanceStringKotlin(qrCodeConfigViewModel.foregroundColor.value)},
            |    )
            |    .qrLogoConfig(
            |        %T(
            |           svgLogo = svgLogo,
            |           relativeSize = ${qrCodeConfigViewModel.logoRelativeSize.value},
            |           bgColor = ${colorInstanceStringKotlin(qrCodeConfigViewModel.logoBackgroundColor.value)},
            |           shape = %T.${qrCodeConfigViewModel.logoShape.value},
            |        ),
            |    )
            |    .qrBorderConfig(
            |        color = ${colorInstanceStringKotlin(qrCodeConfigViewModel.borderColor.value)},
            |        relativeSize = ${qrCodeConfigViewModel.relativeBorderSize.value},
            |        relativeBorderRound = ${qrCodeConfigViewModel.borderRadius.value},
            |    )
            |    .qrPositionalSquaresConfig(qrPositionalSquaresConfig)
            |    .qrCodeDotStyler(QrCodeDotShape.${qrCodeConfigViewModel.dotShape.value.name})
            |    .build()
                    """.trimMargin(),
                    QrCodeConfig::class,
                    Color::class,
                    Color::class,
                    QrSvgLogoConfig::class,
                    Color::class,
                    LogoShape::class,
                    Color::class,
                ).addStatement(
                    """
            |return %T.createQrCodeApi().createQrCodeSvg(qrCodeConfig)
                    """.trimMargin(),
                    QrCodeSvgFactory::class,
                ).returns(Document::class)
                .build()

        file.addType(
            TypeSpec
                .classBuilder("QrCodeGenerator")
                .addFunction(generateQrCodeFunction)
                .build(),
        )

        file.addFunction(
            FunSpec
                .builder("main")
                .addStatement("val logo = parseBase64EncodedStringToDocument(\"Replace-by-Base64-encoded-SVGImage\")")
                .addStatement("val qrCodeGenerator = %T()", ClassName("io.github.simonscholz", "QrCodeGenerator"))
                .addStatement("%N.generateQrCode(logo).toFile(File(\"qr-code.svg\"))", "qrCodeGenerator")
                .build(),
        )

        file.addFunction(
            FunSpec
                .builder("parseBase64EncodedStringToDocument")
                .addAnnotation(
                    AnnotationSpec
                        .builder(ClassName("kotlin", "OptIn"))
                        .addMember("%T::class", ClassName("kotlin.io.encoding", "ExperimentalEncodingApi"))
                        .build(),
                ).addParameter("base64String", String::class)
                .addStatement("val xmlString = %T.Default.encode(base64String.toByteArray())", Base64::class)
                .addStatement("val factory = %T.newInstance()", DocumentBuilderFactory::class)
                .addStatement("val builder = factory.newDocumentBuilder()")
                .addStatement("val inputSource = %T(%T(xmlString.toByteArray()))", InputSource::class, ByteArrayInputStream::class)
                .addStatement("return builder.parse(inputSource)")
                .returns(Document::class)
                .build(),
        )

        file.addFunction(
            FunSpec
                .builder("toFile")
                .receiver(Document::class)
                .addParameter("fileToSave", File::class)
                .addStatement("val transformerFactory = %T.newInstance()", TransformerFactory::class)
                .addStatement("val transformer = transformerFactory.newTransformer()")
                .addStatement("val source = %T(this)", DOMSource::class)
                .addStatement("val result = %T(fileToSave)", StreamResult::class)
                .addStatement("transformer.transform(source, result)")
                .build(),
        )

        file.addFunction(
            FunSpec
                .builder("toByteArray")
                .receiver(Document::class)
                .addStatement(
                    "val byteArrayOutputStream = %T()",
                    ByteArrayOutputStream::class,
                ).addStatement(
                    "val transformer = %T.newInstance().newTransformer()",
                    TransformerFactory::class,
                ).addStatement(
                    "transformer.transform(%T(this), %T(byteArrayOutputStream))",
                    DOMSource::class,
                    StreamResult::class,
                ).addStatement(
                    "return byteArrayOutputStream.toByteArray()",
                ).returns(ByteArray::class)
                .build(),
        )

        return StringBuilder()
            .apply {
                file.build().writeTo(this)
            }.toString()
    }

    fun generateJavaCode(): String {
        val qrCodeGenerator =
            com.squareup.javapoet.TypeSpec
                .classBuilder("QrCodeGenerator")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

        MethodSpec
            .methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(Array<String>::class.java, "args", Modifier.FINAL)
            .addException(IOException::class.java)
            .addException(TransformerException::class.java)
            .addException(ParserConfigurationException::class.java)
            .addException(SAXException::class.java)
            .addStatement(
                "final var qrCodeGenerator = new $T()",
                com.squareup.javapoet.ClassName
                    .get("io.github.simonscholz", "QrCodeGenerator"),
            ).addStatement("final var logo = parseBase64EncodedStringToDocument(\"Replace-by-Base64-encoded-SVGImage\")")
            .addStatement("final var qrCodeImage = qrCodeGenerator.generateQrCode(logo)")
            .addStatement("final var transformerFactory = $T.newInstance()", TransformerFactory::class.java)
            .addStatement("final var transformer = transformerFactory.newTransformer()")
            .addStatement("final var source = new $T(qrCodeImage)", DOMSource::class.java)
            .addStatement("final var result = new $T(new $T(\"qr-code.svg\"))", StreamResult::class.java, File::class.java)
            .addStatement("transformer.transform(source, result)")
            .build()
            .let(qrCodeGenerator::addMethod)

        MethodSpec
            .methodBuilder("parseBase64EncodedStringToDocument")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addException(IOException::class.java)
            .addException(ParserConfigurationException::class.java)
            .addException(SAXException::class.java)
            .addParameter(String::class.java, "base64String", Modifier.FINAL)
            .addStatement("final var xmlString = $T.getDecoder().decode(base64String)", java.util.Base64::class.java)
            .addStatement("final var factory = $T.newInstance()", DocumentBuilderFactory::class.java)
            .addStatement("final var builder = factory.newDocumentBuilder()")
            .addStatement("final var inputSource = new $T(new $T(xmlString))", InputSource::class.java, ByteArrayInputStream::class.java)
            .addStatement("return builder.parse(inputSource)")
            .returns(Document::class.java)
            .build()
            .let(qrCodeGenerator::addMethod)

        MethodSpec
            .methodBuilder("generateQrCode")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Document::class.java, "svgLogo")
            .addStatement(
                """
                |final var qrPositionalSquaresConfig = new $T.Builder()
                |    .circleShaped(${qrCodeConfigViewModel.positionalSquareIsCircleShaped.value})
                |    .relativeSquareBorderRound(${qrCodeConfigViewModel.positionalSquareRelativeBorderRound.value})
                |    .centerColor(${colorInstanceStringJava(qrCodeConfigViewModel.positionalSquareCenterColor.value)})
                |    .innerSquareColor(${colorInstanceStringJava(qrCodeConfigViewModel.positionalSquareInnerSquareColor.value)})
                |    .outerSquareColor(${colorInstanceStringJava(qrCodeConfigViewModel.positionalSquareOuterSquareColor.value)})
                |    .outerBorderColor(${colorInstanceStringJava(qrCodeConfigViewModel.positionalSquareOuterBorderColor.value)})
                |    .colorAdjustmentPatterns(${qrCodeConfigViewModel.positionalSquareColorAdjustmentPatterns.value})
                |    .build()
                """.trimMargin(),
                QrPositionalSquaresConfig::class.java,
                Color::class.java,
                Color::class.java,
                Color::class.java,
                Color::class.java,
            ).addStatement(
                """
                |final var qrCodeConfig = new $T.Builder("${qrCodeConfigViewModel.qrCodeContent.value}}")
                |    .qrCodeSize(${qrCodeConfigViewModel.size.value})
                |    .qrCodeColorConfig(${colorInstanceStringJava(
                    qrCodeConfigViewModel.backgroundColor.value,
                )}, ${colorInstanceStringJava(qrCodeConfigViewModel.foregroundColor.value)})
                |    .qrLogoConfig(new $T(svgLogo, ${qrCodeConfigViewModel.logoRelativeSize.value}, ${colorInstanceStringJava(
                    qrCodeConfigViewModel.logoBackgroundColor.value,
                )}, $T.${qrCodeConfigViewModel.logoShape.value}))
                |    .qrBorderConfig(${colorInstanceStringJava(
                    qrCodeConfigViewModel.borderColor.value,
                )}, ${qrCodeConfigViewModel.relativeBorderSize.value}, ${qrCodeConfigViewModel.borderRadius.value})
                |    .qrPositionalSquaresConfig(qrPositionalSquaresConfig)
                |    .qrCodeDotStyler($T.${qrCodeConfigViewModel.dotShape.value.name})
                |    .build()
                """.trimMargin(),
                QrCodeConfig::class.java,
                Color::class.java,
                Color::class.java,
                QrSvgLogoConfig::class.java,
                Color::class.java,
                LogoShape::class.java,
                Color::class.java,
                QrCodeDotShape::class.java,
            ).addStatement(
                "return $T.createQrCodeApi().createQrCodeSvg(qrCodeConfig)",
                QrCodeSvgFactory::class.java,
            ).returns(Document::class.java)
            .build()
            .let(qrCodeGenerator::addMethod)

        MethodSpec
            .methodBuilder("createQrCodeByteArray")
            .addModifiers(Modifier.PUBLIC)
            .addException(IOException::class.java)
            .addException(TransformerException::class.java)
            .addParameter(Document::class.java, "svgLogo")
            .addStatement(
                "final var qrCodeGenerator = new $T()",
                com.squareup.javapoet.ClassName
                    .get("io.github.simonscholz", "QrCodeGenerator"),
            ).addStatement(
                "final var qrCodeImage = qrCodeGenerator.generateQrCode(svgLogo)",
            ).addStatement(
                "final var byteArrayOutputStream = new $T()",
                com.squareup.javapoet.ClassName
                    .get("java.io", "ByteArrayOutputStream"),
            ).addStatement(
                "final var transformer = $T.newInstance().newTransformer()",
                TransformerFactory::class.java,
            ).addStatement(
                "transformer.transform(new $T(qrCodeImage), new $T(byteArrayOutputStream))",
                DOMSource::class.java,
                StreamResult::class.java,
            ).addStatement(
                "return byteArrayOutputStream.toByteArray()",
            ).returns(ByteArray::class.java)
            .build()
            .let(qrCodeGenerator::addMethod)

        return StringBuilder()
            .apply {
                JavaFile.builder("io.github.simonscholz", qrCodeGenerator.build()).build().writeTo(this)
            }.toString()
    }

    private fun colorInstanceStringKotlin(color: Color): String = "%T(${color.red}, ${color.green}, ${color.blue})"

    private fun colorInstanceStringJava(color: Color): String = "new $T(${color.red}, ${color.green}, ${color.blue})"

    companion object {
        // https://github.com/square/javapoet/issues/831#issuecomment-817238209
        private const val T = "\$T"
    }
}
