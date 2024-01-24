package io.github.simonscholz.spi

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isTrue
import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrCodeFactory
import io.github.simonscholz.qrcode.spi.Graphics2DDelegate
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.awt.Graphics2D
import java.io.ByteArrayOutputStream

class Graphics2DSvgSpiTest {
    private val testSubject = Graphics2DSvgSpi()

    @Test
    fun `supportsFormat should ignore format case`() {
        assertThat(testSubject.supportsFormat("svg")).isTrue()
        assertThat(testSubject.supportsFormat("SVG")).isTrue()
        assertThat(testSubject.supportsFormat("Svg")).isTrue()
        assertThat(testSubject.supportsFormat("SvG")).isTrue()
    }

    @Test
    fun `createQrCode should create a proper svg`() {
        val output =
            assertDoesNotThrow {
                val output = ByteArrayOutputStream()
                testSubject.createQrCode(
                    object : Graphics2DDelegate {
                        override fun drawQrCode(graphics: Graphics2D) {
                            graphics.drawRect(0, 0, 100, 100)
                        }
                    },
                    output,
                )
                output.toString("ISO-8859-1")
            }

        assertThat(output).contains("<svg", ignoreCase = true)
    }

    @Test
    fun `QrCodeApi should pick up Graphics2DSvgSpi properly and creates svg`() {
        val qrCodeApi = QrCodeFactory.createQrCodeApi()
        val output =
            assertDoesNotThrow {
                val qrCodeConfig = QrCodeConfig.Builder("Testing").build()
                val output = ByteArrayOutputStream()
                qrCodeApi.outputQrCode(qrCodeConfig, output, "svg")
                output.toString("ISO-8859-1")
            }

        assertThat(output).contains("<svg", ignoreCase = true)
    }
}
