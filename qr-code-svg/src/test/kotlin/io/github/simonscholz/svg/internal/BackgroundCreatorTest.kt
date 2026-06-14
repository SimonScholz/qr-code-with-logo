package io.github.simonscholz.svg.internal

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.simonscholz.qrcode.LogoShape
import org.apache.batik.dom.GenericDOMImplementation
import org.junit.jupiter.api.Test
import java.awt.Color

class BackgroundCreatorTest {
    private fun newDocument() =
        GenericDOMImplementation
            .getDOMImplementation()
            .createDocument("http://www.w3.org/2000/svg", "svg", null)

    @Test
    fun `a translucent background color is rendered with a fill-opacity attribute`() {
        val translucentRed = Color(255, 0, 0, 128)

        val element =
            BackgroundCreator.createBackground(
                document = newDocument(),
                bgColor = translucentRed,
                logoShape = LogoShape.SQUARE,
                x = 0.0,
                y = 0.0,
                logoMaxSize = 10.0,
            )

        assertThat(element.getAttribute("fill")).isEqualTo("rgb(255, 0, 0)")
        assertThat(element.getAttribute("fill-opacity")).isEqualTo("0.502")
    }

    @Test
    fun `an opaque background color does not emit a fill-opacity attribute`() {
        val element =
            BackgroundCreator.createBackground(
                document = newDocument(),
                bgColor = Color.RED,
                logoShape = LogoShape.SQUARE,
                x = 0.0,
                y = 0.0,
                logoMaxSize = 10.0,
            )

        assertThat(element.getAttribute("fill")).isEqualTo("rgb(255, 0, 0)")
        // an unset attribute is returned as an empty string
        assertThat(element.getAttribute("fill-opacity")).isEqualTo("")
    }
}
