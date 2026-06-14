package io.github.simonscholz.qrcode

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.IOException
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class QrCodeExtensionsTest {
    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `imageFromBase64 throws IOException when the decoded bytes are not a readable image`() {
        val notAnImage = Base64.Default.encode("this is not an image".toByteArray())

        assertThrows<IOException> {
            notAnImage.imageFromBase64()
        }
    }

    @Test
    fun `toBase64 and imageFromBase64 round-trip a valid image`() {
        val image = BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB)
        image.setRGB(0, 0, Color.RED.rgb)

        val restored = image.toBase64().imageFromBase64()

        assertThat(restored.getRGB(0, 0)).isEqualTo(Color.RED.rgb)
    }
}
