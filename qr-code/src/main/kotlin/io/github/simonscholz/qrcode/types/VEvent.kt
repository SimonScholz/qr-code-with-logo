package io.github.simonscholz.qrcode.types

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * This class is a utility to create VEvents with proper syntax
 * in order to pass the outcome of the VEvent.toVEventQrCodeText() function as qrCodeText to the QrCodeApi.
 *
 * Also see https://android.googlesource.com/platform/frameworks/opt/vcard/+/ics-mr1/java/com/android/vcard/VCardConstants.java
 */
class VEvent {
    private var summary = ""
    private var description = ""
    private var location = ""
    private var start = ""
    private var end = ""
    private var uid = ""
    private var dtStamp = ""
    private var other = emptyMap<String, String>()

    /**
     * The summary
     */
    fun summary(summary: String) = apply { this.summary = summary }

    /**
     * The description
     */
    fun description(description: String) = apply { this.description = description }

    /**
     * The location, e.g. a street address
     */
    fun location(location: String) = apply { this.location = location }

    /**
     * The start date and time
     */
    fun startDate(start: String) = apply { this.start = start }

    /**
     * The start date and time
     */
    fun startDate(start: LocalDateTime) = apply { this.start = start.format(dateFormatter) }

    /**
     * The end date and time
     */
    fun endDate(end: String) = apply { this.end = end }

    /**
     * The end date and time
     */
    fun endDate(end: LocalDateTime) = apply { this.end = end.format(dateFormatter) }

    /**
     * The globally unique identifier of the event (iCalendar `UID`).
     * Only used by [toCalendarQrCodeText]; a random one is generated when left unset.
     */
    fun uid(uid: String) = apply { this.uid = uid }

    /**
     * The timestamp when the event was created (iCalendar `DTSTAMP`).
     * Only used by [toCalendarQrCodeText]; the current time is used when left unset.
     */
    fun dtStamp(dtStamp: String) = apply { this.dtStamp = dtStamp }

    /**
     * The timestamp when the event was created (iCalendar `DTSTAMP`).
     * Only used by [toCalendarQrCodeText]; the current time is used when left unset.
     */
    fun dtStamp(dtStamp: LocalDateTime) = apply { this.dtStamp = dtStamp.format(dateFormatter) }

    /**
     * New properties, which might be missing here or also custom properties can be defined using the X- prefix, followed by a unique name
     */
    fun other(other: Map<String, String>) = apply { this.other = other }

    private fun eventProperties(includeMandatory: Boolean): List<String> {
        val properties = mutableListOf<String>()
        if (includeMandatory) {
            properties.add("UID:${uid.ifEmpty { UUID.randomUUID().toString() }}")
            properties.add("DTSTAMP:${dtStamp.ifEmpty { LocalDateTime.now().format(dateFormatter) }}")
        }
        if (summary.isNotEmpty()) {
            properties.add("SUMMARY:${escape(summary)}")
        }
        if (start.isNotEmpty()) {
            properties.add("DTSTART:$start")
        }
        if (end.isNotEmpty()) {
            properties.add("DTEND:$end")
        }
        if (location.isNotEmpty()) {
            properties.add("LOCATION:${escape(location)}")
        }
        if (description.isNotEmpty()) {
            properties.add("DESCRIPTION:${escape(description)}")
        }
        if (other.isNotEmpty()) {
            other.entries.forEach {
                properties.add("${it.key}:${it.value}")
            }
        }
        return properties
    }

    /**
     * Builds a bare `VEVENT` block. This is what most QR-code scanners (e.g. zxing based) expect.
     * Use [toCalendarQrCodeText] when a full, RFC 5545 compliant iCalendar object is required.
     */
    fun toVEventQrCodeText(): String = "BEGIN:VEVENT\n${eventProperties(false).joinToString("\n")}\nEND:VEVENT"

    /**
     * Builds a full iCalendar (`VCALENDAR`) object wrapping the `VEVENT`, including the mandatory
     * `UID` and `DTSTAMP` properties (generated when not set). Prefer this for strict calendar
     * applications; prefer [toVEventQrCodeText] for broad QR-code scanner compatibility.
     */
    fun toCalendarQrCodeText(): String {
        val vEvent = "BEGIN:VEVENT\n${eventProperties(true).joinToString("\n")}\nEND:VEVENT"
        return "BEGIN:VCALENDAR\nVERSION:2.0\nPRODID:-//SimonScholz//qr-code-with-logo//EN\n$vEvent\nEND:VCALENDAR"
    }

    override fun toString(): String = toVEventQrCodeText()

    companion object {
        private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")

        /**
         * Escapes a free-text value according to RFC 5545 (iCalendar):
         * a backslash, comma, semicolon and newline must be escaped with a backslash.
         * Escaping the backslash first avoids escaping the backslashes we add afterwards.
         */
        private fun escape(value: String): String =
            value
                .replace("\\", "\\\\")
                .replace("\r\n", "\\n")
                .replace("\n", "\\n")
                .replace("\r", "\\n")
                .replace(";", "\\;")
                .replace(",", "\\,")
    }
}
