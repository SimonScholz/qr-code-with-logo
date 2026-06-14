package io.github.simonscholz.qrcode.types

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import assertk.assertions.startsWith
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

    @Test
    fun `toCalendarQrCodeText wraps the event in a VCALENDAR with mandatory UID and DTSTAMP`() {
        val calendar =
            VEvent()
                .summary("Meeting")
                .uid("abc-123")
                .dtStamp("20260614T120000")
                .startDate("20260614T130000")
                .toCalendarQrCodeText()

        assertThat(calendar).isEqualTo(
            "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "PRODID:-//SimonScholz//qr-code-with-logo//EN\n" +
                "BEGIN:VEVENT\n" +
                "UID:abc-123\n" +
                "DTSTAMP:20260614T120000\n" +
                "SUMMARY:Meeting\n" +
                "DTSTART:20260614T130000\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR",
        )
    }

    @Test
    fun `toCalendarQrCodeText generates UID and DTSTAMP when none are provided`() {
        val calendar = VEvent().summary("Meeting").toCalendarQrCodeText()

        assertThat(calendar).startsWith("BEGIN:VCALENDAR")
        assertThat(calendar).contains("UID:")
        assertThat(calendar).contains("DTSTAMP:")
    }

    @Test
    fun `toVEventQrCodeText stays a bare VEVENT without a VCALENDAR wrapper or generated fields`() {
        val vEvent = VEvent().summary("Meeting").toVEventQrCodeText()

        assertThat(vEvent).isEqualTo(
            "BEGIN:VEVENT\n" +
                "SUMMARY:Meeting\n" +
                "END:VEVENT",
        )
    }
}
