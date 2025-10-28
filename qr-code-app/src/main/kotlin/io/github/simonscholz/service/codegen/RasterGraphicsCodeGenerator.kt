package io.github.simonscholz.service.codegen

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.qrcode.LogoShape
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeDotShape
import io.github.simonscholz.qrcode.QrCodeFactory
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.lang.model.element.Modifier

class RasterGraphicsCodeGenerator(
    private val qrCodeConfigViewModel: QrCodeConfigViewModel,
) {
    fun generateKotlinCode(): String {
        val file = FileSpec.builder("io.github.simonscholz", "QrCodeGenerator")

        file.addImport(
            "io.github.simonscholz.qrcode",
            "LogoShape",
            "QrCodeConfig",
            "QrCodeFactory",
            "QrPositionalSquaresConfig",
            "QrCodeDotShape",
        )
        file.addImport("java.awt", "Color", "image.BufferedImage")
        file.addImport("java.io", "File")
        file.addImport("javax.imageio", "ImageIO")

        val generateQrCodeFunction =
            FunSpec
                .builder("generateQrCode")
                .addParameter("base64Logo", String::class)
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
            |        base64Logo = base64Logo,
            |        relativeSize = ${qrCodeConfigViewModel.logoRelativeSize.value},
            |        bgColor = ${colorInstanceStringKotlin(qrCodeConfigViewModel.logoBackgroundColor.value)},
            |        shape = %T.${qrCodeConfigViewModel.logoShape.value},
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
                    Color::class,
                    LogoShape::class,
                    Color::class,
                ).addStatement(
                    """
            |return %T.createQrCodeApi().createQrCodeImage(qrCodeConfig)
                    """.trimMargin(),
                    QrCodeFactory::class,
                ).returns(BufferedImage::class)
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
                .addStatement("val logo = \"${qrCodeConfigViewModel.logoBase64.value}\"")
                .addStatement("val qrCodeGenerator = %T()", ClassName("io.github.simonscholz", "QrCodeGenerator"))
                .addStatement("%N.generateQrCode(logo).toFile(File(\"qr-code.png\"))", "qrCodeGenerator")
                .build(),
        )

        file.addFunction(
            FunSpec
                .builder("toFile")
                .receiver(BufferedImage::class)
                .addParameter("fileToSave", File::class)
                .addStatement("ImageIO.write(this, \"png\", fileToSave)")
                .build(),
        )

        file.addFunction(
            FunSpec
                .builder("toByteArray")
                .receiver(BufferedImage::class)
                .addStatement(
                    """
                |val byteArrayOutputStream = %T()
                |%T.write(this, "png", byteArrayOutputStream)
                |return byteArrayOutputStream.toByteArray()
                    """.trimMargin(),
                    ClassName("java.io", "ByteArrayOutputStream"),
                    ClassName("javax.imageio", "ImageIO"),
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
            .addStatement(
                "final var qrCodeGenerator = new $T()",
                com.squareup.javapoet.ClassName
                    .get("io.github.simonscholz", "QrCodeGenerator"),
            ).addStatement("final var logo = \"${qrCodeConfigViewModel.logoBase64.value}\"")
            .addStatement("final var qrCodeImage = qrCodeGenerator.generateQrCode(logo)")
            .addStatement(
                "ImageIO.write(qrCodeImage, \"png\", new $T(\"qr-code.png\"))",
                File::class.java,
            ).build()
            .let(qrCodeGenerator::addMethod)

        val generateQrCodeMethod =
            MethodSpec
                .methodBuilder("generateQrCode")
                .addModifiers(Modifier.PUBLIC)
                .addException(IOException::class.java)
                .addParameter(String::class.java, "base64Logo")
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
                |    .qrLogoConfig(base64Logo, ${qrCodeConfigViewModel.logoRelativeSize.value}, ${colorInstanceStringJava(
                        qrCodeConfigViewModel.logoBackgroundColor.value,
                    )}, $T.${qrCodeConfigViewModel.logoShape.value})
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
                    Color::class.java,
                    LogoShape::class.java,
                    Color::class.java,
                    QrCodeDotShape::class.java,
                ).addStatement(
                    "return $T.createQrCodeApi().createQrCodeImage(qrCodeConfig)",
                    QrCodeFactory::class.java,
                ).returns(BufferedImage::class.java)
                .build()

        qrCodeGenerator.addMethod(generateQrCodeMethod)

        val createQrCodeByteArrayMethod =
            MethodSpec
                .methodBuilder("createQrCodeByteArray")
                .addModifiers(Modifier.PUBLIC)
                .addException(IOException::class.java)
                .addParameter(String::class.java, "base64Logo")
                .addStatement(
                    "final var qrCodeGenerator = new $T()",
                    com.squareup.javapoet.ClassName
                        .get("io.github.simonscholz", "QrCodeGenerator"),
                ).addStatement(
                    "final var qrCodeImage = qrCodeGenerator.generateQrCode(base64Logo)",
                ).addStatement(
                    "final var byteArrayOutputStream = new $T()",
                    com.squareup.javapoet.ClassName
                        .get("java.io", "ByteArrayOutputStream"),
                ).addStatement(
                    "$T.write(qrCodeImage, \"png\", byteArrayOutputStream)",
                    com.squareup.javapoet.ClassName
                        .get("javax.imageio", "ImageIO"),
                ).addStatement(
                    "return byteArrayOutputStream.toByteArray()",
                ).returns(ByteArray::class.java)
                .build()

        qrCodeGenerator.addMethod(createQrCodeByteArrayMethod)

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
