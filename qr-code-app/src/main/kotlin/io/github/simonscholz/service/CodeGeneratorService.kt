package io.github.simonscholz.service

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO
import javax.lang.model.element.Modifier
import com.squareup.javapoet.ClassName as JavaClassName
import com.squareup.javapoet.TypeSpec as JavaTypeSpec

class CodeGeneratorService(private val qrCodeConfigViewModel: QrCodeConfigViewModel) {

    fun generateKotlinCode(): String {
        val file = FileSpec.builder("io.github.simonscholz", "QrCodeGenerator")

        file.addImport("io.github.simonscholz.qrcode", "LogoShape", "QrCodeConfig", "QrCodeFactory", "QrPositionalSquaresConfig")
        file.addImport("java.awt", "Color", "image.BufferedImage")
        file.addImport("java.io", "File")
        file.addImport("javax.imageio", "ImageIO")

        file.addFunction(
            FunSpec.builder("toFile")
                .receiver(ClassName("java.awt.image", "BufferedImage"))
                .addParameter("file", ClassName("java.io", "File"))
                .addStatement("ImageIO.write(this, \"png\", file)")
                .build(),
        )

        file.addFunction(
            FunSpec.builder("main")
                .addStatement(
                    """
                |val logo = ImageIO.read(
                |    QrCodeGenerator::class.java
                |        .classLoader
                |        .getResource("avatar-60x.png")
                |)
                    """.trimMargin(),
                )
                .addStatement("val qrCodeGenerator = %T()", ClassName("io.github.simonscholz", "QrCodeGenerator"))
                .addStatement("%N.generateQrCode(logo).toFile(File(\"qr-code.png\"))", "qrCodeGenerator")
                .build(),
        )

        val generateQrCodeFunction = FunSpec.builder("generateQrCode")
            .addParameter("logo", ClassName("java.awt", "Image"))
            .addStatement(
                """
            |val qrPositionalSquaresConfig = %T.Builder()
            |    .circleShaped(${qrCodeConfigViewModel.positionalSquareIsCircleShaped.value})
            |    .relativeSquareBorderRound(${qrCodeConfigViewModel.positionalSquareRelativeBorderRound.value})
            |    .centerColor(${colorInstanceStringKotlin(qrCodeConfigViewModel.positionalSquareCenterColor.value)})
            |    .innerSquareColor(${colorInstanceStringKotlin(qrCodeConfigViewModel.positionalSquareInnerSquareColor.value)})
            |    .outerSquareColor(${colorInstanceStringKotlin(qrCodeConfigViewModel.positionalSquareOuterSquareColor.value)})
            |    .outerBorderColor(${colorInstanceStringKotlin(qrCodeConfigViewModel.positionalSquareOuterBorderColor.value)})
            |    .build()
                """.trimMargin(),
                ClassName("io.github.simonscholz.qrcode", "QrPositionalSquaresConfig"),
                ClassName("java.awt", "Color"),
                ClassName("java.awt", "Color"),
                ClassName("java.awt", "Color"),
                ClassName("java.awt", "Color"),
            )
            .addStatement(
                """
            |val qrCodeConfig = %T.Builder("${qrCodeConfigViewModel.qrCodeContent.value}")
            |    .qrCodeSize(${qrCodeConfigViewModel.size.value})
            |    .qrCodeColorConfig(
            |        bgColor = ${colorInstanceStringKotlin(qrCodeConfigViewModel.backgroundColor.value)},
            |        fillColor = ${colorInstanceStringKotlin(qrCodeConfigViewModel.foregroundColor.value)},
            |    )
            |    .qrLogoConfig(
            |        logo = logo,
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
            |    .build()
                """.trimMargin(),
                ClassName("io.github.simonscholz.qrcode", "QrCodeConfig"),
                ClassName("java.awt", "Color"),
                ClassName("java.awt", "Color"),
                ClassName("java.awt", "Color"),
                ClassName("io.github.simonscholz.qrcode", "LogoShape"),
                ClassName("java.awt", "Color"),
            )
            .addStatement(
                """
            |return %T.createQrCodeApi().createQrCodeImage(qrCodeConfig)
                """.trimMargin(),
                ClassName("io.github.simonscholz.qrcode", "QrCodeFactory"),
            )
            .returns(BufferedImage::class)
            .build()

        file.addType(
            TypeSpec.classBuilder("QrCodeGenerator")
                .addFunction(generateQrCodeFunction)
                .build(),
        )

        return StringBuilder().apply {
            file.build().writeTo(this)
        }.toString()
    }

    fun generateJavaCode(): String {
        val qrCodeGenerator = JavaTypeSpec.classBuilder("QrCodeGenerator")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

        MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(Array<String>::class.java, "args", Modifier.FINAL)
            .addException(IOException::class.java)
            .addStatement("final var qrCodeGenerator = new QrCodeGenerator()")
            .addStatement(
                """
                |final var qrCodeImage = qrCodeGenerator.generateQrCode(
                |        $T.read(QrCodeGenerator.class.getClassLoader()
                |       .getResource("your-logo.png"))
                |)
                """.trimMargin(),
                ImageIO::class.java,
            )
            .addStatement(
                "ImageIO.write(qrCodeImage, \"png\", new $T(\"qr-code.png\"))",
                JavaClassName.get("java.io", "File"),
            )
            .build()
            .let(qrCodeGenerator::addMethod)

        val generateQrCodeMethod = MethodSpec.methodBuilder("generateQrCode")
            .addModifiers(Modifier.PUBLIC)
            .addException(IOException::class.java)
            .addParameter(Image::class.java, "logo")
            .addStatement(
                """
                |final var qrPositionalSquaresConfig = new $T.Builder()
                |    .circleShaped(${qrCodeConfigViewModel.positionalSquareIsCircleShaped.value})
                |    .relativeSquareBorderRound(${qrCodeConfigViewModel.positionalSquareRelativeBorderRound.value})
                |    .centerColor(${colorInstanceStringJava(qrCodeConfigViewModel.positionalSquareCenterColor.value)})
                |    .innerSquareColor(${colorInstanceStringJava(qrCodeConfigViewModel.positionalSquareInnerSquareColor.value)})
                |    .outerSquareColor(${colorInstanceStringJava(qrCodeConfigViewModel.positionalSquareOuterSquareColor.value)})
                |    .outerBorderColor(${colorInstanceStringJava(qrCodeConfigViewModel.positionalSquareOuterBorderColor.value)})
                |    .build()
                """.trimMargin(),
                QrPositionalSquaresConfig::class.java,
                Color::class.java,
                Color::class.java,
                Color::class.java,
                Color::class.java,
            )
            .addStatement(
                """
                |final var qrCodeConfig = new $T.Builder("${qrCodeConfigViewModel.qrCodeContent.value}}")
                |    .qrCodeSize(${qrCodeConfigViewModel.size.value})
                |    .qrCodeColorConfig(${colorInstanceStringJava(qrCodeConfigViewModel.backgroundColor.value)}, ${colorInstanceStringJava(qrCodeConfigViewModel.foregroundColor.value)})
                |    .qrLogoConfig(logo, ${qrCodeConfigViewModel.logoRelativeSize.value}, ${colorInstanceStringJava(qrCodeConfigViewModel.logoBackgroundColor.value)}, $T.${qrCodeConfigViewModel.logoShape.value})
                |    .qrBorderConfig(${colorInstanceStringJava(qrCodeConfigViewModel.borderColor.value)}, ${qrCodeConfigViewModel.relativeBorderSize.value}, ${qrCodeConfigViewModel.borderRadius.value})
                |    .qrPositionalSquaresConfig(qrPositionalSquaresConfig)
                |    .build()
                """.trimMargin(),
                JavaClassName.get("io.github.simonscholz.qrcode", "QrCodeConfig"),
                JavaClassName.get("java.awt", "Color"),
                JavaClassName.get("java.awt", "Color"),
                JavaClassName.get("java.awt", "Color"),
                JavaClassName.get("io.github.simonscholz.qrcode", "LogoShape"),
                JavaClassName.get("java.awt", "Color"),
            )
            .addStatement(
                "return $T.createQrCodeApi().createQrCodeImage(qrCodeConfig)",
                JavaClassName.get("io.github.simonscholz.qrcode", "QrCodeFactory"),
            )
            .returns(BufferedImage::class.java)
            .build()

        qrCodeGenerator.addMethod(generateQrCodeMethod)

        return StringBuilder().apply {
            JavaFile.builder("io.github.simonscholz", qrCodeGenerator.build()).build().writeTo(this)
        }.toString()
    }

    private fun colorInstanceStringKotlin(color: Color): String {
        return "%T(${color.red}, ${color.green}, ${color.blue})"
    }

    private fun colorInstanceStringJava(color: Color): String {
        return "new $T(${color.red}, ${color.green}, ${color.blue})"
    }

    companion object {
        // https://github.com/square/javapoet/issues/831#issuecomment-817238209
        private const val T = "\$T"
    }
}
