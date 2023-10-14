package io.github.simonscholz.qrcode.types

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * This class is a utility to create VEvents with proper syntax
 * in order to pass the outcome of the VEvent.toVEventQrCodeText() function as qrCodeText to the QrCodeApi.
 *
 * Also see https://android.googlesource.com/platform/frameworks/opt/vcard/+/ics-mr1/java/com/android/vcard/VCardConstants.java
 */
class VEvent private constructor(
    private val summary: String,
    private val description: String,
    private val location: String,
    private val start: String,
    private val end: String,
    private val other: Map<String, String>,
) {
    class Builder(private val summary: String) {
        private var description = ""
        private var location = ""
        private var start = ""
        private var end = ""
        private var other = emptyMap<String, String>()

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
         * New properties, which might be missing here or also custom properties can be defined using the X- prefix, followed by a unique name
         */
        fun other(other: Map<String, String>) = apply { this.other = other }

        fun build(): VEvent {
            return VEvent(
                summary = summary,
                description = description,
                location = location,
                start = start,
                end = end,
                other = other,
            )
        }
    }

    fun toVEventQrCodeText(): String {
        val properties = mutableListOf<String>()
        if (summary.isNotEmpty()) {
            properties.add("SUMMARY:$summary")
        }
        if (start.isNotEmpty()) {
            properties.add("DTSTART:$start")
        }
        if (end.isNotEmpty()) {
            properties.add("DTEND:$end")
        }
        if (location.isNotEmpty()) {
            properties.add("LOCATION:$location")
        }
        if (description.isNotEmpty()) {
            properties.add("DESCRIPTION:$description")
        }
        if (other.isNotEmpty()) {
            other.entries.forEach {
                properties.add("${it.key}:${it.value}")
            }
        }

        val vEventData = properties.joinToString("\n")
        return "BEGIN:VEVENT\n$vEventData\nEND:VEVENT"
    }

    override fun toString(): String = toVEventQrCodeText()

    companion object {
        private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
    }
}
