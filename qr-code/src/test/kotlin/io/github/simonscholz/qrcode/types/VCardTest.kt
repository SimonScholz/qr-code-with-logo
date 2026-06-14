package io.github.simonscholz.qrcode.types

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import org.junit.jupiter.api.Test

class VCardTest {
    @Test
    fun `note containing a comma is escaped`() {
        val vCard = VCard().formattedName("Max").note("Hello, world").toVCardQrCodeText()

        assertThat(vCard).contains("NOTE:Hello\\, world")
    }

    @Test
    fun `note containing a newline is escaped so the record structure stays intact`() {
        val vCard = VCard().formattedName("Max").note("Line1\nLine2").toVCardQrCodeText()

        assertThat(vCard).contains("NOTE:Line1\\nLine2")
        // the raw newline must not appear, otherwise it would break the vCard structure
        assertThat(vCard).doesNotContain("Line1\nLine2")
    }

    @Test
    fun `formatted name containing a semicolon is escaped`() {
        val vCard = VCard().formattedName("Doe; John").toVCardQrCodeText()

        assertThat(vCard).contains("FN:Doe\\; John")
    }

    @Test
    fun `name components are escaped without escaping the component separators`() {
        val vCard =
            VCard()
                .formattedName("Max")
                .name(familyName = "Mu,ller", givenName = "Max")
                .toVCardQrCodeText()

        assertThat(vCard).contains("N:Mu\\,ller;Max;;;")
    }

    @Test
    fun `photo uses a colon separator and is not text-escaped so data URIs are preserved`() {
        val dataUri = "data:image/png;base64,iVBORw0KGgo="
        val vCard = VCard().formattedName("Max").photo(dataUri).toVCardQrCodeText()

        assertThat(vCard).contains("PHOTO:$dataUri")
    }
}
