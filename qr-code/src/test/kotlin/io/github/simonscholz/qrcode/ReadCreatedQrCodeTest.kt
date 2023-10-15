package io.github.simonscholz.qrcode

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import io.github.simonscholz.qrcode.types.SimpleTypes
import io.github.simonscholz.qrcode.types.VCard
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Color
import java.awt.image.BufferedImage
import java.net.URL
import java.util.Objects
import javax.imageio.ImageIO

class ReadCreatedQrCodeTest {

    private val qrCodeApi = QrCodeFactory.createQrCodeApi()

    @ParameterizedTest
    @MethodSource("provideDifferentQrCodeTypeTexts")
    fun `Writing different QrCode type texts to BufferedImage and reading it should produce the same outcome`(
        qrCodeInputText: String,
    ) {
        val qrCodeImage = qrCodeApi.createQrCodeImage(QrCodeConfig(qrCodeInputText, DEFAULT_IMG_SIZE))

        val readQRCode = readQRCode(qrCodeImage)
        assertThat(readQRCode).isEqualTo(qrCodeInputText)
    }

    @Test
    fun `Creating qr code with logo, circle positional squares and border should still be readable`() {
        val qrCodeText = SimpleTypes.url("https://simonscholz.github.io/")

        val resource = logoUrl()
        val qrCodeImage = logoAndCirclePositionalSquares(resource, qrCodeApi)

        val readQRCode = readQRCode(qrCodeImage)
        assertThat(readQRCode).isEqualTo(qrCodeText)
    }

    @Test
    @Disabled("It seems that the zxing reader cannot deal with colors :-(")
    fun `Creating colorful qr code with logo and different shapes should still be readable`() {
        val qrCodeText = SimpleTypes.url("https://simonscholz.github.io/")

        val resource = logoUrl()

        val qrCodeImage = colorful(resource, qrCodeApi)

        val readQRCode = readQRCode(qrCodeImage)
        assertThat(readQRCode).isEqualTo(qrCodeText)
    }

    private fun logoUrl(): URL =
        Objects.requireNonNull(
            CreateQrCodeTest::class.java.getClassLoader()
                .getResource("rainbow.png"),
        )

    private fun logoAndCirclePositionalSquares(resource: URL, qrCodeApi: QrCodeApi): BufferedImage {
        val logo = ImageIO.read(resource)
        val positionalSquaresConfig = QrPositionalSquaresConfig.Builder()
            .circleShaped(true)
            .build()
        val qrCodeConfig = QrCodeConfig.Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.RED)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(positionalSquaresConfig)
            .build()
        return qrCodeApi.createQrCodeImage(qrCodeConfig)
    }

    private fun colorful(resource: URL, qrCodeApi: QrCodeApi): BufferedImage {
        val logo = ImageIO.read(resource)
        val bgColor = Color.RED
        val fillColor = Color.BLUE
        val positionalSquaresConfig = QrPositionalSquaresConfig.Builder()
            .circleShaped(true)
            .centerColor(fillColor)
            .innerSquareColor(bgColor)
            .outerSquareColor(fillColor)
            .outerBorderColor(bgColor)
            .build()
        val qrCodeConfig = QrCodeConfig.Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.WHITE)
            .qrLogoConfig(logo)
            .qrCodeColorConfig(bgColor, fillColor)
            .qrPositionalSquaresConfig(positionalSquaresConfig)
            .build()
        return qrCodeApi.createQrCodeImage(qrCodeConfig)
    }

    private fun readQRCode(bufferedImage: BufferedImage, hintMap: Map<DecodeHintType, *>? = null): String {
        val binaryBitmap = BinaryBitmap(
            HybridBinarizer(
                BufferedImageLuminanceSource(
                    bufferedImage,
                ),
            ),
        )
        val qrCodeResult = QRCodeMultiReader().decode(binaryBitmap, hintMap)
        return qrCodeResult.text
    }

    companion object {
        @JvmStatic
        fun provideDifferentQrCodeTypeTexts(): List<Arguments> {
            val vCard = VCard.Builder("Simon Scholz")
                .email("simon@example.com")
                .organization("Self Employed")
                .phoneNumber("+49 176 12345678")
                .website("https://simonscholz.github.io/")
                .build()
            return listOf(Arguments.of(vCard.toVCardQrCodeText()))
        }
    }
}
