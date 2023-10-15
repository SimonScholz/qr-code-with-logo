package io.github.simonscholz.qrcode

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.io.TempDir
import java.awt.Color
import java.nio.file.Path
import java.util.Objects
import javax.imageio.ImageIO

class CreateQrCodeTest {

    private val qrCodeApi = QrCodeFactory.createQrCodeApi()

    @Test
    fun `qr code can be written to file system without errors`(@TempDir tempDir: Path) {
        assertDoesNotThrow {
            val qrCodeConfig = QrCodeConfig.Builder("Testing").build()
            val qrCodeImage = qrCodeApi.createQrCodeImage(qrCodeConfig)
            val qrCodeFile = tempDir.resolve("qr-code.png")
            ImageIO.write(qrCodeImage, "png", qrCodeFile.toFile())
        }
    }

    @Test
    fun `complex qr code can be written to file system without errors`(@TempDir tempDir: Path) {
        assertDoesNotThrow {
            val resource = Objects.requireNonNull(
                CreateQrCodeTest::class.java.getClassLoader()
                    .getResource("rainbow.png"),
            )
            val logo = ImageIO.read(resource)

            val positionalSquaresConfig = QrPositionalSquaresConfig.Builder()
                .relativeSquareBorderRound(0.2)
                .innerSquareColor(Color.RED)
                .centerColor(Color.GREEN)
                .outerSquareColor(Color.BLUE)
                .outerBorderColor(Color.YELLOW)
                .build()
            val qrCodeConfig = QrCodeConfig.Builder("Testing")
                .qrCodeSize(1000)
                .qrCodeColorConfig(Color.RED, Color.BLUE)
                .qrPositionalSquaresConfig(positionalSquaresConfig)
                .qrBorderConfig(Color.BLACK, 0.05, 0.2)
                .qrLogoConfig(logo, 0.2, Color.WHITE)
                .build()
            val qrCodeImage = qrCodeApi.createQrCodeImage(qrCodeConfig)
            val qrCodeFile = tempDir.resolve("qr-code.png")
            ImageIO.write(qrCodeImage, "png", qrCodeFile.toFile())
        }
    }
}
