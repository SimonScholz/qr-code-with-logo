package io.github.simonscholz.qrcode.types

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import org.junit.jupiter.api.Test

class VEventTest {
    @Test
    fun `description containing a comma is escaped`() {
        val vEvent = VEvent().summary("Meeting").description("Bring coffee, tea").toVEventQrCodeText()

        assertThat(vEvent).contains("DESCRIPTION:Bring coffee\\, tea")
    }

    @Test
    fun `summary containing a newline is escaped so the record structure stays intact`() {
        val vEvent = VEvent().summary("Line1\nLine2").toVEventQrCodeText()

        assertThat(vEvent).contains("SUMMARY:Line1\\nLine2")
        assertThat(vEvent).doesNotContain("Line1\nLine2")
    }
}
