package io.github.simonscholz.qrcode.types

/**
 * This class is a utility to create simple Qr Code types with proper syntax,
 * such as geolocation, email, phone number, sms, url.
 */
object SimpleTypes {
    /**
     * Contains a geographic location.
     * When scanned as QrCode it usually opens the Maps app with the geographic location as the argument.
     *
     * @param latitude  Latitude in degrees. Must be in the range [-90.0, 90.0].
     * @param longitude Longitude in degrees. Must be in the range [-180.0, 180.0].
     */
    @JvmStatic
    fun geolocation(
        latitude: Double,
        longitude: Double,
    ) = "geo:$latitude,$longitude"

    /**
     * mailto: link.
     * When scanned as QrCode it usually opens the Mail app with the params as the arguments.
     *
     * @param emailAddress Email address to send the email to.
     * @param subject      Subject of the email.
     * @param body         Body of the email.
     */
    @JvmStatic
    @JvmOverloads
    fun email(
        emailAddress: String,
        subject: String = "",
        body: String = "",
    ) = "mailto:$emailAddress?subject=$subject&body=$body"

    /**
     * tel: number.
     * When scanned as QrCode it usually opens the Phone app with the phone number as the argument.
     */
    @JvmStatic
    fun phoneNumber(phoneNumber: String) = "tel:$phoneNumber"

    /**
     * sms: number.
     * When scanned as QrCode it usually opens the Messages app with the phone number as the argument.
     *
     * @param phoneNumber Phone number to send the message to.
     * @param message     Message to send.
     */
    @JvmStatic
    @JvmOverloads
    fun sms(
        phoneNumber: String,
        message: String = "",
    ) = "sms:$phoneNumber?body=$message"

    /**
     * Simply returns the given string.
     * It is still useful to use this method to make sure it still works in the future, in case the syntax for urls changes.
     */
    @JvmStatic
    fun url(url: String) = url
}
