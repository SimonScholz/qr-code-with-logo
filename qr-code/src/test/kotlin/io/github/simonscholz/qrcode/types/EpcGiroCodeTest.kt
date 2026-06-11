package io.github.simonscholz.qrcode.types

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class EpcGiroCodeTest {
    @Test
    fun `toEpcGiroCodeQrCodeText should follow expected EPC line format`() {
        val epcGiroCodeText =
            EpcGiroCode()
                .bic("DEUTDEFFXXX")
                .recipient("Max Mustermann")
                .iban("DE89370400440532013000")
                .amount("EUR123.45")
                .reference("Rechnung 2026-001")
                .remittanceInformation("Vielen Dank fuer Ihren Einkauf")
                .toEpcGiroCodeQrCodeText()

        assertThat(epcGiroCodeText).isEqualTo(
            "BCD\n" +
                "002\n" +
                "1\n" +
                "SCT\n" +
                "DEUTDEFFXXX\n" +
                "Max Mustermann\n" +
                "DE89370400440532013000\n" +
                "EUR123.45\n" +
                "\n" +
                "Rechnung 2026-001\n" +
                "Vielen Dank fuer Ihren Einkauf",
        )
    }
}

