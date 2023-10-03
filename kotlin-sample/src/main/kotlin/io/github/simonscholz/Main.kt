package io.github.simonscholz

import io.github.simonscholz.qrcode.QrCodeApi
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory.createQrCodeApi
import io.github.simonscholz.qrcode.QrLogoConfig
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

class Main

fun main() {
    val qrCodeApi = createQrCodeApi()
    val userHomeDir = System.getProperty("user.home")
    val qrCode = qrCodeApi.createQrImage(QrCodeConfig("https://simonscholz.github.io/", 200))
    ImageIO.write(qrCode, "png", File(userHomeDir, "/qr-with-defaults-kotlin.png"))

    val resource: URL? =  Main::class.java.getClassLoader().getResource("avatar.png")
    resource?.let {
        createDefaultQrCodeWithLogo(it, qrCodeApi, userHomeDir)
    }
}

private fun createDefaultQrCodeWithLogo(
    it: URL,
    qrCodeApi: QrCodeApi,
    userHomeDir: String?,
): Boolean {
    val logo = ImageIO.read(it)
    val qrLogoConfigConfig = QrLogoConfig(logo, .2)
    val qrCodeConfig = QrCodeConfig(
        "https://simonscholz.github.io/",
        300,
        qrLogoConfigConfig,
    )
    val qrWithImage = qrCodeApi.createQrImage(qrCodeConfig)
    return ImageIO.write(qrWithImage, "png", File(userHomeDir, "/qr-with-logo-kotlin.png"))
}
