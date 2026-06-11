package io.github.simonscholz.qrcode.types

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Utility to create EPC GiroCode / SEPA Credit Transfer payloads for QR codes.
 */
class EpcGiroCode {
    private var bic = ""
    private var recipient = ""
    private var iban = ""
    private var amount = ""
    private var purposeCode = ""
    private var reference = ""
    private var remittanceInformation = ""

    /** Optional beneficiary BIC. */
    fun bic(bic: String) = apply { this.bic = bic }

    /** Beneficiary name. */
    fun recipient(recipient: String) = apply { this.recipient = recipient }

    /** Beneficiary IBAN. */
    fun iban(iban: String) = apply { this.iban = iban }

    /** Amount in EPC format, e.g. EUR123.45. */
    fun amount(amount: String) = apply { this.amount = amount }

    /** Amount based on a decimal value formatted as EUR with two decimals. */
    fun amount(amount: BigDecimal) = apply { this.amount = "$CURRENCY${amount.setScale(2, RoundingMode.HALF_UP)}" }

    /** Optional purpose code. */
    fun purposeCode(purposeCode: String) = apply { this.purposeCode = purposeCode }

    /** Optional creditor reference. */
    fun reference(reference: String) = apply { this.reference = reference }

    /** Optional unstructured remittance information. */
    fun remittanceInformation(remittanceInformation: String) = apply { this.remittanceInformation = remittanceInformation }

    fun toEpcGiroCodeQrCodeText(): String {
        val formattedAmount = if (amount.isNotEmpty() && !amount.startsWith(CURRENCY)) "$CURRENCY$amount" else amount
        return listOf(
            SERVICE_TAG,
            VERSION,
            CHARACTER_SET,
            IDENTIFICATION,
            bic,
            recipient,
            iban,
            formattedAmount,
            purposeCode,
            reference,
            remittanceInformation,
        ).joinToString("\n")
    }

    override fun toString(): String = toEpcGiroCodeQrCodeText()

    private companion object {
        const val SERVICE_TAG = "BCD"
        const val VERSION = "002"
        const val CHARACTER_SET = "1"
        const val IDENTIFICATION = "SCT"
        const val CURRENCY = "EUR"
    }
}
