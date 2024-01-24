package io.github.simonscholz

import io.github.simonscholz.qrcode.DEFAULT_IMG_SIZE
import io.github.simonscholz.qrcode.QrCodeApi
import io.github.simonscholz.qrcode.QrCodeColorConfig
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory.createQrCodeApi
import io.github.simonscholz.qrcode.QrLogoConfig
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig
import java.awt.Color
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Objects
import javax.imageio.ImageIO

private const val RELATIVE_SQUARE_BORDER_ROUND = .5
private val VIOLET = Color(0x0063, 0x000B, 0x00A5)

fun main() {
    val qrCodeApi = createQrCodeApi()
    val path = Paths.get(System.getProperty("user.home"), "qr-code-samples")
    Files.createDirectories(path)
    val qrCodeDir = path.toAbsolutePath().toString()

    createDefaultQrCode(qrCodeApi, qrCodeDir)

    val resource = Main::class.java.getClassLoader().getResource("avatar-60x.png")
    resource?.let {
        createDefaultQrCodeWithLogo(it, qrCodeApi, qrCodeDir)
        createDefaultQrCodeWithLogoAndBorder(it, qrCodeApi, qrCodeDir)
        createDefaultQrCodeWithLogoAndBorderAndPositionalSquareBorderRadius(it, qrCodeApi, qrCodeDir)
        createDefaultQrCodeWithLogoAndBorderAndPositionalSquareCircle(it, qrCodeApi, qrCodeDir)
        decentRedColor(it, qrCodeApi, qrCodeDir)
        mineCraftCreeperColor(it, qrCodeApi, qrCodeDir)
    }

    rainbowColor(qrCodeApi, qrCodeDir)

    notEnoughContrast(qrCodeApi, qrCodeDir)
}

private fun createDefaultQrCode(
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    qrCodeApi.outputQrCode(
        QrCodeConfig("https://simonscholz.github.io/", DEFAULT_IMG_SIZE),
        File(qrCodeDir, "/qr-with-defaults-kotlin.svg").outputStream(),
        "svg",
    )
}

private fun createDefaultQrCodeWithLogo(
    resource: URL,
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig(
            "https://simonscholz.github.io/",
            DEFAULT_IMG_SIZE,
            QrLogoConfig(logo),
        )
    qrCodeApi.outputQrCode(
        qrCodeConfig,
        File(qrCodeDir, "/qr-with-logo-kotlin.svg").outputStream(),
        "svg",
    )
}

private fun createDefaultQrCodeWithLogoAndBorder(
    resource: URL,
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig.Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .build()
    qrCodeApi.outputQrCode(
        qrCodeConfig,
        File(qrCodeDir, "/qr-with-logo-and-border-kotlin.svg").outputStream(),
        "svg",
    )
}

private fun createDefaultQrCodeWithLogoAndBorderAndPositionalSquareBorderRadius(
    resource: URL,
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig.Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(
                QrPositionalSquaresConfig.Builder()
                    .relativeSquareBorderRound(RELATIVE_SQUARE_BORDER_ROUND)
                    .build(),
            )
            .build()
    qrCodeApi.outputQrCode(
        qrCodeConfig,
        File(
            qrCodeDir,
            "/qr-with-logo-and-border-and-p-border-round-kotlin.svg",
        ).outputStream(),
        "svg",
    )
}

private fun createDefaultQrCodeWithLogoAndBorderAndPositionalSquareCircle(
    resource: URL,
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig.Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(QrPositionalSquaresConfig(true))
            .build()
    qrCodeApi.outputQrCode(
        qrCodeConfig,
        File(
            qrCodeDir,
            "/qr-with-logo-and-border-and-p-border-circle-kotlin.svg",
        ).outputStream(),
        "svg",
    )
}

private fun decentRedColor(
    resource: URL,
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig.Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(
                QrPositionalSquaresConfig(
                    isCircleShaped = true,
                    relativeSquareBorderRound = RELATIVE_SQUARE_BORDER_ROUND,
                    centerColor = Color.RED,
                ),
            )
            .build()
    qrCodeApi.outputQrCode(
        qrCodeConfig,
        File(qrCodeDir, "/decent-red-color-kotlin.svg").outputStream(),
        "svg",
    )
}

private fun mineCraftCreeperColor(
    resource: URL,
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val brighterGreen = Color.GREEN.brighter()
    val darkerGreen = Color.GREEN.darker().darker().darker()
    val positionalSquaresConfig =
        QrPositionalSquaresConfig.Builder()
            .relativeSquareBorderRound(RELATIVE_SQUARE_BORDER_ROUND)
            .centerColor(brighterGreen)
            .innerSquareColor(darkerGreen)
            .outerSquareColor(brighterGreen)
            .outerBorderColor(darkerGreen)
            .build()
    val qrCodeConfig =
        QrCodeConfig.Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.WHITE)
            .qrLogoConfig(logo)
            .qrCodeColorConfig(darkerGreen, brighterGreen)
            .qrPositionalSquaresConfig(positionalSquaresConfig)
            .build()
    qrCodeApi.outputQrCode(
        qrCodeConfig,
        File(qrCodeDir, "/minecraft-creeper-color-kotlin.svg").outputStream(),
        "svg",
    )
}

private fun rainbowColor(
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val resource = Objects.requireNonNull(Main::class.java.getClassLoader().getResource("rainbow.png"))
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig.Builder("https://simonscholz.github.io/")
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
            )
            .build()
    qrCodeApi.outputQrCode(
        qrCodeConfig,
        File(qrCodeDir, "/rainbow-color-kotlin.svg").outputStream(),
        "svg",
    )
}

private fun notEnoughContrast(
    qrCodeApi: QrCodeApi,
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
            qrCodeText = "https://simonscholz.github.io/",
            qrCodeColorConfig = QrCodeColorConfig(Color.BLUE, VIOLET),
            qrPositionalSquaresConfig = positionalSquaresConfig,
        )
    qrCodeApi.outputQrCode(
        qrCodeConfig,
        File(qrCodeDir, "/not-enough-contrast-kotlin.svg").outputStream(),
        "svg",
    )
}
