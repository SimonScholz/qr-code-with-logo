package io.github.simonscholz.qrcode.types

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class SimpleTypesTest {
    @Test
    fun `email subject and body are url-encoded with percent-encoded spaces`() {
        val mailto = SimpleTypes.email("a@b.com", subject = "Hello World", body = "A & B")

        assertThat(mailto).isEqualTo("mailto:a@b.com?subject=Hello%20World&body=A%20%26%20B")
    }

    @Test
    fun `wifi escapes special characters in ssid and password`() {
        val wifi = SimpleTypes.wifi(ssid = "Cafe;Guest", password = "pass:word")

        assertThat(wifi).isEqualTo("WIFI:T:WPA;S:Cafe\\;Guest;P:pass\\:word;;")
    }
}
