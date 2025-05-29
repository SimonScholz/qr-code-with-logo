package io.github.simonscholz

import io.github.simonscholz.qrcode.DEFAULT_IMG_SIZE
import io.github.simonscholz.qrcode.LogoShape
import io.github.simonscholz.qrcode.QrCodeColorConfig
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrLogoConfig
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig
import io.github.simonscholz.svg.QrCodeSvgApi
import io.github.simonscholz.svg.QrCodeSvgFactory.createQrCodeApi
import io.github.simonscholz.svg.QrSvgLogoConfig
import java.awt.Color
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Objects
import javax.imageio.ImageIO
import javax.xml.parsers.DocumentBuilderFactory

private const val RELATIVE_SQUARE_BORDER_ROUND = .5
private val VIOLET = Color(0x0063, 0x000B, 0x00A5)

fun main() {
    val qrCodeSvgApi = createQrCodeApi()
    val path = Paths.get(System.getProperty("user.home"), "qr-code-samples")
    Files.createDirectories(path)
    val qrCodeDir = path.toAbsolutePath().toString()

    createDefaultQrCode(qrCodeSvgApi, qrCodeDir)

    val resource = Main::class.java.getClassLoader().getResource("avatar-60x.png")
    resource?.let {
        createDefaultQrCodeWithLogo(it, qrCodeSvgApi, qrCodeDir)
        createDefaultQrCodeWithLogoAndBorder(it, qrCodeSvgApi, qrCodeDir)
        createDefaultQrCodeWithLogoAndBorderAndPositionalSquareBorderRadius(it, qrCodeSvgApi, qrCodeDir)
        createDefaultQrCodeWithLogoAndBorderAndPositionalSquareCircle(it, qrCodeSvgApi, qrCodeDir)
        decentRedColor(it, qrCodeSvgApi, qrCodeDir)
        mineCraftCreeperColor(it, qrCodeSvgApi, qrCodeDir)
    }

    svgLogo(qrCodeSvgApi, qrCodeDir)

    rainbowColor(qrCodeSvgApi, qrCodeDir)

    notEnoughContrast(qrCodeSvgApi, qrCodeDir)
}

fun svgLogo(
    qrCodeSvgApi: QrCodeSvgApi,
    qrCodeDir: String,
) {
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val logoDocument = builder.parse(Main::class.java.getClassLoader().getResourceAsStream("avatar.svg"))
    val qrCodeConfig =
        QrCodeConfig(
            "https://simonscholz.dev/",
            DEFAULT_IMG_SIZE,
            QrSvgLogoConfig(svgLogo = logoDocument, bgColor = Color.YELLOW, shape = LogoShape.CIRCLE),
        )
    qrCodeSvgApi
        .createQrCodeSvg(
            qrCodeConfig,
        ).toFile(File(qrCodeDir, "/qr-with-svg-logo-kotlin.svg"))
}

private fun createDefaultQrCode(
    qrCodeSvgApi: QrCodeSvgApi,
    qrCodeDir: String,
) {
    qrCodeSvgApi
        .createQrCodeSvg(
            QrCodeConfig("https://simonscholz.dev/", DEFAULT_IMG_SIZE),
        ).toFile(File(qrCodeDir, "/qr-with-defaults-kotlin.svg"))
}

private fun createDefaultQrCodeWithLogo(
    resource: URL,
    qrCodeSvgApi: QrCodeSvgApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig(
            "https://simonscholz.dev/",
            DEFAULT_IMG_SIZE,
            QrLogoConfig.Bitmap(logo),
        )
    qrCodeSvgApi
        .createQrCodeSvg(
            qrCodeConfig,
        ).toFile(File(qrCodeDir, "/qr-with-logo-kotlin.svg"))
}

private fun createDefaultQrCodeWithLogoAndBorder(
    resource: URL,
    qrCodeSvgApi: QrCodeSvgApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig
            .Builder("https://simonscholz.dev/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .build()
    qrCodeSvgApi
        .createQrCodeSvg(
            qrCodeConfig,
        ).toFile(File(qrCodeDir, "/qr-with-logo-and-border-kotlin.svg"))
}

private fun createDefaultQrCodeWithLogoAndBorderAndPositionalSquareBorderRadius(
    resource: URL,
    qrCodeSvgApi: QrCodeSvgApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig
            .Builder("https://simonscholz.dev/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(
                QrPositionalSquaresConfig
                    .Builder()
                    .relativeSquareBorderRound(RELATIVE_SQUARE_BORDER_ROUND)
                    .build(),
            ).build()
    qrCodeSvgApi
        .createQrCodeSvg(
            qrCodeConfig,
        ).toFile(File(qrCodeDir, "/qr-with-logo-and-border-and-positional-square-border-radius-kotlin.svg"))
}

private fun createDefaultQrCodeWithLogoAndBorderAndPositionalSquareCircle(
    resource: URL,
    qrCodeSvgApi: QrCodeSvgApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig
            .Builder("https://simonscholz.dev/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(QrPositionalSquaresConfig(true))
            .build()
    qrCodeSvgApi
        .createQrCodeSvg(
            qrCodeConfig,
        ).toFile(File(qrCodeDir, "/qr-with-logo-and-border-and-positional-square-circle-kotlin.svg"))
}

private fun decentRedColor(
    resource: URL,
    qrCodeSvgApi: QrCodeSvgApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig
            .Builder("https://simonscholz.dev/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(
                QrPositionalSquaresConfig(
                    isCircleShaped = true,
                    relativeSquareBorderRound = RELATIVE_SQUARE_BORDER_ROUND,
                    centerColor = Color.RED,
                ),
            ).build()
    qrCodeSvgApi
        .createQrCodeSvg(
            qrCodeConfig,
        ).toFile(File(qrCodeDir, "/qr-with-logo-and-border-and-positional-square-circle-decent-red-kotlin.svg"))
}

private fun mineCraftCreeperColor(
    resource: URL,
    qrCodeSvgApi: QrCodeSvgApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val brighterGreen = Color.GREEN.brighter()
    val darkerGreen =
        Color.GREEN
            .darker()
            .darker()
            .darker()
    val positionalSquaresConfig =
        QrPositionalSquaresConfig
            .Builder()
            .relativeSquareBorderRound(RELATIVE_SQUARE_BORDER_ROUND)
            .centerColor(brighterGreen)
            .innerSquareColor(darkerGreen)
            .outerSquareColor(brighterGreen)
            .outerBorderColor(darkerGreen)
            .build()
    val qrCodeConfig =
        QrCodeConfig
            .Builder("https://simonscholz.dev/")
            .qrBorderConfig(Color.WHITE)
            .qrLogoConfig(logo)
            .qrCodeColorConfig(darkerGreen, brighterGreen)
            .qrPositionalSquaresConfig(positionalSquaresConfig)
            .build()
    qrCodeSvgApi
        .createQrCodeSvg(
            qrCodeConfig,
        ).toFile(File(qrCodeDir, "/qr-with-logo-and-border-and-positional-square-circle-minecraft-creeper-kotlin.svg"))
}

private fun rainbowColor(
    qrCodeSvgApi: QrCodeSvgApi,
    qrCodeDir: String,
) {
    val resource = Objects.requireNonNull(Main::class.java.getClassLoader().getResource("rainbow.png"))
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig
            .Builder("https://simonscholz.dev/")
            .qrBorderConfig(Color.YELLOW)
            .qrLogoConfig(logo)
            .qrCodeColorConfig(Color.BLUE, VIOLET)
            .qrPositionalSquaresConfig(
                QrPositionalSquaresConfig(
                    isCircleShaped = true,
                    relativeSquareBorderRound = RELATIVE_SQUARE_BORDER_ROUND,
                    centerColor = Color.PINK,
                    innerSquareColor = Color.GREEN,
                    outerSquareColor = Color.RED,
                    outerBorderColor = Color.CYAN,
                ),
            ).build()
    qrCodeSvgApi
        .createQrCodeSvg(
            qrCodeConfig,
        ).toFile(File(qrCodeDir, "/qr-with-logo-and-border-and-positional-square-circle-rainbow-kotlin.svg"))
}

private fun notEnoughContrast(
    qrCodeSvgApi: QrCodeSvgApi,
    qrCodeDir: String,
) {
    val positionalSquaresConfig =
        QrPositionalSquaresConfig(
            centerColor = Color.BLUE,
            innerSquareColor = VIOLET,
            outerSquareColor = Color.BLUE,
            outerBorderColor = VIOLET,
        )
    val qrCodeConfig =
        QrCodeConfig(
            qrCodeText = "https://simonscholz.dev/",
            qrCodeColorConfig = QrCodeColorConfig(Color.BLUE, VIOLET),
            qrPositionalSquaresConfig = positionalSquaresConfig,
        )
    qrCodeSvgApi
        .createQrCodeSvg(
            qrCodeConfig,
        ).toFile(File(qrCodeDir, "/qr-with-logo-and-border-and-positional-square-circle-not-enough-contrast-kotlin.svg"))
}
