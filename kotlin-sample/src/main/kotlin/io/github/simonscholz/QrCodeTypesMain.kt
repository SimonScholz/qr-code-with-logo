package io.github.simonscholz

import io.github.simonscholz.qrcode.DEFAULT_IMG_SIZE
import io.github.simonscholz.qrcode.QrCodeApi
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory.createQrCodeApi
import io.github.simonscholz.qrcode.types.SimpleTypes.email
import io.github.simonscholz.qrcode.types.SimpleTypes.geolocation
import io.github.simonscholz.qrcode.types.SimpleTypes.phoneNumber
import io.github.simonscholz.qrcode.types.SimpleTypes.sms
import io.github.simonscholz.qrcode.types.SimpleTypes.url
import io.github.simonscholz.qrcode.types.SimpleTypes.wifi
import io.github.simonscholz.qrcode.types.VCard
import io.github.simonscholz.qrcode.types.VEvent
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import javax.imageio.ImageIO

/**
 * This class shows how to create QR Codes with different types.
 * For the design of a QR Code, please have a look at [Main].
 */
fun main() {
    val qrCodeApi = createQrCodeApi()
    val path = Paths.get(System.getProperty("user.home"), "qr-code-samples")
    Files.createDirectories(path)
    val qrCodeDir = path.toAbsolutePath().toString()
    createWithUrl(qrCodeApi, qrCodeDir)
    createWithGeolocation(qrCodeApi, qrCodeDir)
    createWithEmail(qrCodeApi, qrCodeDir)
    createWithPhoneNumber(qrCodeApi, qrCodeDir)
    createWithSms(qrCodeApi, qrCodeDir)
    createWithWifi(qrCodeApi, qrCodeDir)
    createWithVEvent(qrCodeApi, qrCodeDir)
    createWithVCard(qrCodeApi, qrCodeDir)
}

private fun createWithUrl(
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val url = url("https://simonscholz.dev/")
    createDefaultQrCode(qrCodeApi, url, File(qrCodeDir, "simple-url.png"))
}

private fun createWithGeolocation(
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val geolocation = geolocation(53.59659752940634, 10.006589989354053)
    createDefaultQrCode(qrCodeApi, geolocation, File(qrCodeDir, "simple-geolocation.png"))
}

private fun createWithEmail(
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val email = email("simon@example.com", "Hello World", "This is a test email")
    createDefaultQrCode(qrCodeApi, email, File(qrCodeDir, "simple-email.png"))
}

private fun createWithPhoneNumber(
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val phoneNumber = phoneNumber("+49 176 12345678")
    createDefaultQrCode(qrCodeApi, phoneNumber, File(qrCodeDir, "simple-phoneNumber.png"))
}

private fun createWithSms(
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val sms = sms("+49 176 12345678", "Hello, this is a test SMS")
    createDefaultQrCode(qrCodeApi, sms, File(qrCodeDir, "simple-sms.png"))
}

private fun createWithWifi(
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val wifi = wifi("My-Wifi", "MyWifiPassword")
    createDefaultQrCode(qrCodeApi, wifi, File(qrCodeDir, "simple-wifi.png"))
}

private fun createWithVEvent(
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val startDateTime =
        LocalDateTime
            .now()
            .plusWeeks(2)
    val vEventQrCodeText =
        VEvent()
            .summary("QR Codes with Kotlin & Java")
            .location("Java User Group Hamburg")
            .startDate(startDateTime)
            .endDate(startDateTime.plusHours(2))
            .description("Let's create QR Codes with Kotlin & Java")
            .toVEventQrCodeText()
    createDefaultQrCode(qrCodeApi, vEventQrCodeText, File(qrCodeDir, "vevent.png"))
}

private fun createWithVCard(
    qrCodeApi: QrCodeApi,
    qrCodeDir: String,
) {
    val vCardQrCodeText =
        VCard()
            .formattedName("Simon Scholz")
            .email("simon@example.com")
            .address("Main Street 1", "Hamburg", "22855")
            .organization("Self Employed")
            .phoneNumber("+49 176 12345678")
            .website("https://simonscholz.dev/")
            .toVCardQrCodeText()
    createDefaultQrCode(qrCodeApi, vCardQrCodeText, File(qrCodeDir, "vCard.png"))
}

private fun createDefaultQrCode(
    qrCodeApi: QrCodeApi,
    qrCodeText: String,
    qrCodeFile: File,
) {
    val qrCode = qrCodeApi.createQrCodeImage(QrCodeConfig(qrCodeText, DEFAULT_IMG_SIZE))
    ImageIO.write(qrCode, "png", qrCodeFile)
}
