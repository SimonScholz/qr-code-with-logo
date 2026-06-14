package io.github.simonscholz.qrcode.types

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

/**
 * This class is a utility to create VCards with proper syntax
 * in order to pass the outcome of the VCard.toVCardQrCodeText() function as qrCodeText to the QrCodeApi.
 *
 * Also see https://android.googlesource.com/platform/frameworks/opt/vcard/+/ics-mr1/java/com/android/vcard/VCardConstants.java
 */
class VCard {
    private var formattedName = ""
    private var name = ""
    private var organization = ""
    private var phoneNumber = ""
    private var email = ""
    private var address = ""
    private var website = ""
    private var birthday = ""
    private var photo = ""
    private var note = ""
    private var title = ""
    private var revisionDate = ""
    private var latitude = ""
    private var longitude = ""
    private var instantMessaging = ""
    private var categories = ""
    private var other = emptyMap<String, String>()

    /**
     * The formatted name
     */
    fun formattedName(formattedName: String) = apply { this.formattedName = formattedName }

    /**
     * Build the name component
     *
     * @param familyName - last name, surname
     * @param givenName - first name
     * @param additionalName - middle name
     * @param namePrefix - prefix, e.g. Mr., Mrs., Dr.
     * @param nameSuffix - suffix, e.g. Jr., Sr.
     */
    @JvmOverloads
    fun name(
        familyName: String = "",
        givenName: String = "",
        additionalName: String = "",
        namePrefix: String = "",
        nameSuffix: String = "",
    ) = apply {
        // escape each component individually so the structural `;` separators are preserved
        this.name =
            listOf(familyName, givenName, additionalName, namePrefix, nameSuffix)
                .joinToString(";") { escape(it) }
    }

    /**
     * The organization
     */
    fun organization(org: String) = apply { this.organization = org }

    /**
     * The phone number
     */
    fun phoneNumber(number: String) = apply { this.phoneNumber = number }

    /**
     * The email address
     */
    fun email(email: String) = apply { this.email = email }

    /**
     * Build the address component
     *
     * @param street - street name and number
     * @param city - city
     * @param postalCode - postal code
     * @param country - country
     * @param state - state or region
     * @param postOfficeBox - post office box
     * @param extendedAddress - extended address, e.g. apartment or suite number
     */
    @JvmOverloads
    fun address(
        street: String = "",
        city: String = "",
        postalCode: String = "",
        country: String = "",
        state: String = "",
        postOfficeBox: String = "",
        extendedAddress: String = "",
    ) = apply {
        // escape each component individually so the structural `;` separators are preserved
        this.address =
            listOf(postOfficeBox, extendedAddress, street, city, state, postalCode, country)
                .joinToString(";") { escape(it) }
    }

    /**
     * Url of a website
     */
    fun website(url: String) = apply { this.website = url }

    /**
     * The birthday
     */
    fun birthday(date: String) = apply { this.birthday = date }

    fun birthday(birthday: LocalDate) = apply { this.birthday = birthday.format(DateTimeFormatter.ISO_LOCAL_DATE) }

    /**
     * Can be an url to an image or base64 encoded image
     */
    fun photo(photo: String) = apply { this.photo = photo }

    /**
     * Allows for additional notes or comments about the contact
     */
    fun note(note: String) = apply { this.note = note }

    /**
     * Specifies the job title or position of the contact within the organization
     */
    fun title(title: String) = apply { this.title = title }

    /**
     * Indicates the last time the VCard was updated.
     * Should be in ISO 8601 format, e.g., 2023-10-13T15:30:00Z
     *
     * @param revisionDate string representation
     */
    fun revisionDate(revisionDate: String) = apply { this.revisionDate = revisionDate }

    /**
     * Indicates the last time the VCard was updated.
     *
     * Will be translated to ISO 8601 format, e.g., 2023-10-13T15:30:00Z
     */
    fun revisionDate(revisionDate: LocalDateTime) = apply { this.revisionDate = revisionDate.format(DateTimeFormatter.ISO_INSTANT) }

    /**
     * Indicates the last time the VCard was updated.
     *
     * Will be translated to ISO 8601 format, e.g., 2023-10-13T15:30:00Z
     */
    fun revisionDate(revisionDate: TemporalAccessor) = apply { this.revisionDate = DateTimeFormatter.ISO_INSTANT.format(revisionDate) }

    /**
     * Specifies the latitude and longitude of a location
     */
    fun geographicPosition(
        latitude: Double,
        longitude: Double,
    ) = apply {
        this.latitude = latitude.toString()
        this.longitude = longitude.toString()
    }

    /**
     * Instant Messaging and Presence Protocol: Specifies instant messaging handles, including the IM protocol used (e.g., Skype, AIM, XMPP)
     */
    fun impp(instantMessaging: String) = apply { this.instantMessaging = instantMessaging }

    /**
     * Specifies categories or groups that the contact belongs to
     */
    fun categories(categories: String) = apply { this.categories = categories }

    /**
     * New properties, which might be missing here or also custom properties can be defined using the X- prefix, followed by a unique name
     */
    fun other(other: Map<String, String>) = apply { this.other = other }

    fun toVCardQrCodeText(): String {
        val properties = mutableListOf<String>()
        properties.add("FN:${escape(formattedName)}")
        // name is already escaped per component in name()
        if (name.isNotEmpty()) {
            properties.add("N:$name")
        }
        if (organization.isNotEmpty()) {
            properties.add("ORG:${escape(organization)}")
        }
        if (phoneNumber.isNotEmpty()) {
            properties.add("TEL:$phoneNumber")
        }
        if (email.isNotEmpty()) {
            properties.add("EMAIL:$email")
        }
        // address is already escaped per component in address()
        if (address.isNotEmpty()) {
            properties.add("ADR:$address")
        }
        if (website.isNotEmpty()) {
            properties.add("URL:$website")
        }
        if (birthday.isNotEmpty()) {
            properties.add("BDAY:$birthday")
        }
        if (photo.isNotEmpty()) {
            // PHOTO is a URI value (e.g. a data: URI). It must use a `:` separator and must not be
            // text-escaped, otherwise the `;` and `,` inside a data URI would be corrupted.
            properties.add("PHOTO:$photo")
        }
        if (note.isNotEmpty()) {
            properties.add("NOTE:${escape(note)}")
        }
        if (title.isNotEmpty()) {
            properties.add("TITLE:${escape(title)}")
        }
        if (revisionDate.isNotEmpty()) {
            properties.add("REV:$revisionDate")
        }
        if (latitude.isNotEmpty() && longitude.isNotEmpty()) {
            properties.add("GEO:$latitude;$longitude")
        }
        if (instantMessaging.isNotEmpty()) {
            properties.add("IMPP:$instantMessaging")
        }
        if (categories.isNotEmpty()) {
            properties.add("CATEGORIES:$categories")
        }
        if (other.isNotEmpty()) {
            other.entries.forEach {
                properties.add("${it.key}:${it.value}")
            }
        }

        val vCardData = properties.joinToString("\n")
        return "BEGIN:VCARD\nVERSION:4.0\n$vCardData\nEND:VCARD"
    }

    override fun toString(): String = toVCardQrCodeText()

    private companion object {
        /**
         * Escapes a free-text value according to RFC 6350 (vCard 4.0):
         * a backslash, comma, semicolon and newline must be escaped with a backslash.
         * Escaping the backslash first avoids escaping the backslashes we add afterwards.
         */
        fun escape(value: String): String =
            value
                .replace("\\", "\\\\")
                .replace("\r\n", "\\n")
                .replace("\n", "\\n")
                .replace("\r", "\\n")
                .replace(";", "\\;")
                .replace(",", "\\,")
    }
}
