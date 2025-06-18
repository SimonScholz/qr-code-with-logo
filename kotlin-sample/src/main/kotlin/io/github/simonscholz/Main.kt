package io.github.simonscholz

import io.github.simonscholz.qrcode.DEFAULT_IMG_SIZE
import io.github.simonscholz.qrcode.QrCodeApi
import io.github.simonscholz.qrcode.QrCodeColorConfig
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory.createQrCodeApi
import io.github.simonscholz.qrcode.QrLogoConfig
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Objects
import javax.imageio.ImageIO

fun BufferedImage.toFile(file: File) {
    ImageIO.write(this, "png", file)
}

private const val MY_DOMAIN = "https://simonscholz.dev/"
private const val RELATIVE_SQUARE_BORDER_ROUND = .5
private val VIOLET = Color(0x0063, 0x000B, 0x00A5)

class Main

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
        createTransparentQrCode(it, qrCodeApi, qrCodeDir)
    }

    rainbowColor(qrCodeApi, qrCodeDir)

    notEnoughContrast(qrCodeApi, qrCodeDir)
}

private fun createDefaultQrCode(
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    qrCodeApi
        .createQrCodeImage(QrCodeConfig(MY_DOMAIN, DEFAULT_IMG_SIZE))
        .toFile(File(qrCodeDir, "/qr-with-defaults-kotlin.png"))
}

private fun createDefaultQrCodeWithLogo(
    resource: URL,
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig(
            MY_DOMAIN,
            DEFAULT_IMG_SIZE,
            QrLogoConfig.Bitmap(logo),
        )
    qrCodeApi.createQrCodeImage(qrCodeConfig).toFile(File(qrCodeDir, "/qr-with-logo-kotlin.png"))
}

private fun createDefaultQrCodeWithLogoAndBorder(
    resource: URL,
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig
            .Builder(MY_DOMAIN)
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .build()
    qrCodeApi.createQrCodeImage(qrCodeConfig).toFile(File(qrCodeDir, "/qr-with-logo-and-border-kotlin.png"))
}

private fun createDefaultQrCodeWithLogoAndBorderAndPositionalSquareBorderRadius(
    resource: URL,
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig
            .Builder(MY_DOMAIN)
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(
                QrPositionalSquaresConfig
                    .Builder()
                    .relativeSquareBorderRound(RELATIVE_SQUARE_BORDER_ROUND)
                    .build(),
            ).build()
    qrCodeApi.createQrCodeImage(qrCodeConfig).toFile(
        File(
            qrCodeDir,
            "/qr-with-logo-and-border-and-p-border-round-kotlin.png",
        ),
    )
}

private fun createDefaultQrCodeWithLogoAndBorderAndPositionalSquareCircle(
    resource: URL,
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig
            .Builder(MY_DOMAIN)
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(QrPositionalSquaresConfig(true))
            .build()
    qrCodeApi.createQrCodeImage(qrCodeConfig).toFile(
        File(
            qrCodeDir,
            "/qr-with-logo-and-border-and-p-border-circle-kotlin.png",
        ),
    )
}

private fun decentRedColor(
    resource: URL,
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig
            .Builder(MY_DOMAIN)
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(
                QrPositionalSquaresConfig(
                    isCircleShaped = true,
                    relativeSquareBorderRound = RELATIVE_SQUARE_BORDER_ROUND,
                    centerColor = Color.RED,
                ),
            ).build()
    qrCodeApi.createQrCodeImage(qrCodeConfig).toFile(File(qrCodeDir, "/decent-red-color-kotlin.png"))
}

private fun mineCraftCreeperColor(
    resource: URL,
    qrCodeApi: QrCodeApi,
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
            .Builder(MY_DOMAIN)
            .qrBorderConfig(Color.WHITE)
            .qrLogoConfig(logo)
            .qrCodeColorConfig(darkerGreen, brighterGreen)
            .qrPositionalSquaresConfig(positionalSquaresConfig)
            .build()
    qrCodeApi.createQrCodeImage(qrCodeConfig).toFile(File(qrCodeDir, "/minecraft-creeper-color-kotlin.png"))
}

private fun createTransparentQrCode(
    resource: URL,
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val logo = ImageIO.read(resource)
    val transparent = Color(0, 0, 0, 0)
    val positionalSquaresConfig =
        QrPositionalSquaresConfig(
            isCircleShaped = true,
            centerColor = Color.BLUE,
            innerSquareColor = Color.WHITE,
            outerSquareColor = Color.BLUE,
            outerBorderColor = transparent,
        )
    val qrCodeConfig =
        QrCodeConfig(
            qrCodeText = MY_DOMAIN,
            qrCodeSize = 150,
            qrPositionalSquaresConfig = positionalSquaresConfig,
            qrLogoConfig = QrLogoConfig.Bitmap(logo),
            qrCodeColorConfig = QrCodeColorConfig(transparent, Color.BLUE),
        )
    val qrWithImage = qrCodeApi.createQrCodeImage(qrCodeConfig)
    drawQrCodeOnImage(qrWithImage, qrCodeDir)
}

private fun drawQrCodeOnImage(
    qrCode: BufferedImage,
    qrCodeDir: String,
) {
    val url =
        Objects.requireNonNull(
            Main::class.java
                .getClassLoader()
                .getResource("cup.jpg"),
        )
    val mainImg = ImageIO.read(url)
    val graphics = mainImg.graphics
    graphics.drawImage(qrCode, 330, 600, null)
    graphics.dispose()
    ImageIO.write(mainImg, "png", File(qrCodeDir, "/transparent-color-kotlin.png"))
}

private fun rainbowColor(
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val resource = Objects.requireNonNull(Main::class.java.getClassLoader().getResource("rainbow.png"))
    val logo = ImageIO.read(resource)
    val qrCodeConfig =
        QrCodeConfig
            .Builder(MY_DOMAIN)
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
    qrCodeApi.createQrCodeImage(qrCodeConfig).toFile(File(qrCodeDir, "/rainbow-color-kotlin.png"))
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
            qrCodeText = MY_DOMAIN,
            qrCodeColorConfig = QrCodeColorConfig(Color.BLUE, VIOLET),
            qrPositionalSquaresConfig = positionalSquaresConfig,
        )
    qrCodeApi.createQrCodeImage(qrCodeConfig).toFile(File(qrCodeDir, "/not-enough-contrast-kotlin.png"))
}
