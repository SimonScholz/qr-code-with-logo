package io.github.simonscholz.svg

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.simonscholz.qrcode.QrCodeConfig
import org.apache.batik.dom.GenericDOMImplementation
import org.junit.jupiter.api.Test
import org.w3c.dom.Element

class QrCodeSvgLogoCenteringTest {
    private fun landscapeLogo(): org.w3c.dom.Document {
        val document =
            GenericDOMImplementation
                .getDOMImplementation()
                .createDocument("http://www.w3.org/2000/svg", "svg", null)
        // 2:1 landscape logo
        document.documentElement.setAttribute("viewBox", "0 0 200 100")
        return document
    }

    @Test
    fun `a non-square svg logo is centered using its cropped dimensions`() {
        val config =
            QrCodeConfig(
                qrCodeText = "centered logo",
                qrCodeSize = 300,
                qrLogoConfig = QrSvgLogoConfig(svgLogo = landscapeLogo(), relativeSize = 0.2),
            )

        val svg = QrCodeSvgFactory.createQrCodeApi().createQrCodeSvg(config)

        // logoMaxSize = 300 * 0.2 = 60; landscape 2:1 => width 60, height 30
        val logo = svg.getElementsByTagName("svg").nestedLogoElement()
        assertThat(logo.getAttribute("width")).isEqualTo("60")
        assertThat(logo.getAttribute("height")).isEqualTo("30")
        // centered: x = (300 - 60) / 2 = 120, y = (300 - 30) / 2 = 135
        assertThat(logo.getAttribute("x")).isEqualTo("120.0")
        assertThat(logo.getAttribute("y")).isEqualTo("135.0")
    }

    private fun org.w3c.dom.NodeList.nestedLogoElement(): Element {
        for (i in 0 until length) {
            val element = item(i) as Element
            // the nested logo <svg> carries the x attribute we set; the root <svg> does not
            if (element.getAttribute("x").isNotEmpty()) {
                return element
            }
        }
        throw AssertionError("No nested logo <svg> element found")
    }
}
