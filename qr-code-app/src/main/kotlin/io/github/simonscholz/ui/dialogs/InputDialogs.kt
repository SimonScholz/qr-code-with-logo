package io.github.simonscholz.ui.dialogs

import com.github.lgooddatepicker.components.DatePicker
import com.github.lgooddatepicker.components.DateTimePicker
import net.miginfocom.swing.MigLayout
import java.awt.GridLayout
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.SpinnerNumberModel
import javax.swing.border.TitledBorder

object InputDialogs {
    fun showTwoValueInputDialog(
        title: String,
        firstLabel: String,
        secondLabel: String,
    ): Pair<String, String>? {
        val panel = JPanel()
        panel.layout = GridLayout(2, 2)

        val label1 = JLabel(firstLabel)
        val textField1 = JTextField(20)

        val label2 = JLabel(secondLabel)
        val textField2 = JTextField(20)

        panel.add(label1)
        panel.add(textField1)
        panel.add(label2)
        panel.add(textField2)

        val result = JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION)

        if (result == JOptionPane.OK_OPTION) {
            val value1 = textField1.text
            val value2 = textField2.text

            return Pair(value1, value2)
        }
        return null
    }

    fun showThreeValueInputDialog(
        title: String,
        firstLabel: String,
        secondLabel: String,
        thirdLabel: String,
    ): Triple<String, String, String>? {
        val panel = JPanel()
        panel.layout = GridLayout(3, 2)

        val label1 = JLabel(firstLabel)
        val textField1 = JTextField(20)

        val label2 = JLabel(secondLabel)
        val textField2 = JTextField(20)

        val label3 = JLabel(thirdLabel)
        val textField3 = JTextField(20)

        panel.add(label1)
        panel.add(textField1)
        panel.add(label2)
        panel.add(textField2)
        panel.add(label3)
        panel.add(textField3)

        val result = JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION)

        if (result == JOptionPane.OK_OPTION) {
            val value1 = textField1.text
            val value2 = textField2.text
            val value3 = textField3.text

            return Triple(value1, value2, value3)
        }
        return null
    }

    fun showEmailInputDialog(): Triple<String, String, String>? {
        val panel = JPanel(MigLayout())

        val emailAddressLabel = JLabel("Email address")
        val emailAddressTextField = JTextField()

        val subjectLabel = JLabel("Subject")
        val subjectTextField = JTextField()

        val bodyLabel = JLabel("Body")
        val bodyTextArea = JTextArea()

        panel.add(emailAddressLabel)
        panel.add(emailAddressTextField, "wrap, grow, width 300:300:300")
        panel.add(subjectLabel)
        panel.add(subjectTextField, "wrap, grow, width 300:300:300")
        panel.add(bodyLabel)
        panel.add(bodyTextArea, "wrap, grow, width 300:300:300, height 200:200:300")

        val result = JOptionPane.showConfirmDialog(null, panel, "Email", JOptionPane.OK_CANCEL_OPTION)

        if (result == JOptionPane.OK_OPTION) {
            val emailAddress = emailAddressTextField.text
            val subject = subjectTextField.text
            val body = bodyTextArea.text

            return Triple(emailAddress, subject, body)
        }
        return null
    }

    fun showVCardInputDialog(): VCardInput? {
        val panel = JPanel(MigLayout())

        val formattedNameLabel = JLabel("Formatted Name")
        val formattedNameTextField = JTextField()

        val borderPropertiesPanel = JPanel(MigLayout())

        borderPropertiesPanel.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Name Details (optional)",
                TitledBorder.LEFT,
                TitledBorder.TOP,
            ),
        )

        val familyNameLabel = JLabel("Family Name")
        val familyNameTextField = JTextField()

        val givenNameLabel = JLabel("Given Name")
        val givenNameTextField = JTextField()

        val additionalNameLabel = JLabel("Additional Name")
        val additionalNameTextField = JTextField()

        val namePrefixLabel = JLabel("Name Prefix")
        val namePrefixTextField = JTextField()

        val nameSuffixLabel = JLabel("Name Suffix")
        val nameSuffixTextField = JTextField()

        val organizationLabel = JLabel("Organization")
        val organizationTextField = JTextField()

        val phoneNumberLabel = JLabel("Phone Number")
        val phoneNumberTextField = JTextField()

        val emailLabel = JLabel("Email")
        val emailTextField = JTextField()

        val addressLabel = JLabel("Address")
        val addressTextField = JTextField()

        val websiteLabel = JLabel("Website")
        val websiteTextField = JTextField()

        val birthdayLabel = JLabel("Birthday")
        val birthdayDatePicker = DatePicker()

        val photoLabel = JLabel("Photo")
        val photoTextField = JTextField()

        val noteLabel = JLabel("Note")
        val noteTextField = JTextField()

        val titleLabel = JLabel("Title")
        val titleTextField = JTextField()

        val revisionDateLabel = JLabel("Revision Date")
        val revisionDateTextField = JTextField()

        val latitudeLabel = JLabel("Latitude")
        val spinnerLatitudeModel = SpinnerNumberModel(0.0, -90.0, 90.0, 0.0000001)
        val latitudeSpinner = JSpinner(spinnerLatitudeModel)

        val longitudeLabel = JLabel("Longitude")
        val spinnerLongitudeModel = SpinnerNumberModel(0.0, -180.0, 180.0, 0.0000001)
        val longitudeSpinner = JSpinner(spinnerLongitudeModel)

        val instantMessagingLabel = JLabel("Instant Messaging")
        val instantMessagingTextField = JTextField()

        val categoriesLabel = JLabel("Categories")
        val categoriesTextField = JTextField()

        panel.add(formattedNameLabel)
        panel.add(formattedNameTextField, "wrap, grow, width 300:300:300")
        borderPropertiesPanel.add(familyNameLabel)
        borderPropertiesPanel.add(familyNameTextField, "wrap, grow, width 300:300:300")
        borderPropertiesPanel.add(givenNameLabel)
        borderPropertiesPanel.add(givenNameTextField, "wrap, grow, width 300:300:300")
        borderPropertiesPanel.add(additionalNameLabel)
        borderPropertiesPanel.add(additionalNameTextField, "wrap, grow, width 300:300:300")
        borderPropertiesPanel.add(namePrefixLabel)
        borderPropertiesPanel.add(namePrefixTextField, "wrap, grow, width 300:300:300")
        borderPropertiesPanel.add(nameSuffixLabel)
        borderPropertiesPanel.add(nameSuffixTextField, "wrap, grow, width 300:300:300")
        panel.add(borderPropertiesPanel, "wrap, grow, span 2")
        panel.add(organizationLabel)
        panel.add(organizationTextField, "wrap, grow, width 300:300:300")
        panel.add(phoneNumberLabel)
        panel.add(phoneNumberTextField, "wrap, grow, width 300:300:300")
        panel.add(emailLabel)
        panel.add(emailTextField, "wrap, grow, width 300:300:300")
        panel.add(addressLabel)
        panel.add(addressTextField, "wrap, grow, width 300:300:300")
        panel.add(websiteLabel)
        panel.add(websiteTextField, "wrap, grow, width 300:300:300")
        panel.add(birthdayLabel)
        panel.add(birthdayDatePicker, "wrap, grow, width 300:300:300")
        panel.add(photoLabel)
        panel.add(photoTextField, "wrap, grow, width 300:300:300")
        panel.add(noteLabel)
        panel.add(noteTextField, "wrap, grow, width 300:300:300")
        panel.add(titleLabel)
        panel.add(titleTextField, "wrap, grow, width 300:300:300")
        panel.add(revisionDateLabel)
        panel.add(revisionDateTextField, "wrap, grow, width 300:300:300")
        panel.add(latitudeLabel)
        panel.add(latitudeSpinner, "wrap, grow, width 300:300:300")
        panel.add(longitudeLabel)
        panel.add(longitudeSpinner, "wrap, grow, width 300:300:300")
        panel.add(instantMessagingLabel)
        panel.add(instantMessagingTextField, "wrap, grow, width 300:300:300")
        panel.add(categoriesLabel)
        panel.add(categoriesTextField, "wrap, grow, width 300:300:300")

        val result = JOptionPane.showConfirmDialog(null, panel, "VCard", JOptionPane.OK_CANCEL_OPTION)

        if (result == JOptionPane.OK_OPTION) {
            val formattedName = formattedNameTextField.text
            val familyName = familyNameTextField.text
            val givenName = givenNameTextField.text
            val additionalName = additionalNameTextField.text
            val namePrefix = namePrefixTextField.text
            val nameSuffix = nameSuffixTextField.text
            val organization = organizationTextField.text
            val phoneNumber = phoneNumberTextField.text
            val email = emailTextField.text
            val address = addressTextField.text
            val website = websiteTextField.text
            val birthday = birthdayDatePicker.date
            val photo = photoTextField.text
            val note = noteTextField.text
            val title = titleTextField.text
            val revisionDate = revisionDateTextField.text
            val latitude = latitudeSpinner.value as Double
            val longitude = longitudeSpinner.value as Double
            val instantMessaging = instantMessagingTextField.text
            val categories = categoriesTextField.text

            return VCardInput(
                formattedName = formattedName,
                familyName = familyName,
                givenName = givenName,
                additionalName = additionalName,
                namePrefix = namePrefix,
                nameSuffix = nameSuffix,
                organization = organization,
                phoneNumber = phoneNumber,
                email = email,
                address = address,
                website = website,
                birthday = birthday,
                photo = photo,
                note = note,
                title = title,
                revisionDate = revisionDate,
                latitude = latitude,
                longitude = longitude,
                instantMessaging = instantMessaging,
                categories = categories,
                other = emptyMap(),
            )
        }

        return null
    }

    fun showVEventInputDialog(): VEentInput? {
        val panel = JPanel(MigLayout())

        val summaryLabel = JLabel("Summary")
        val summaryTextField = JTextField()

        val descriptionLabel = JLabel("Description")
        val descriptionTextField = JTextField()

        val locationLabel = JLabel("Location")
        val locationTextField = JTextField()

        val startDateLabel = JLabel("Start Date")
        val startDateTimePicker = DateTimePicker()

        val endDateLabel = JLabel("End Date")
        val endDateTimePicker = DateTimePicker()

        panel.add(summaryLabel)
        panel.add(summaryTextField, "wrap, grow, width 300:300:300")
        panel.add(descriptionLabel)
        panel.add(descriptionTextField, "wrap, grow, width 300:300:300")
        panel.add(locationLabel)
        panel.add(locationTextField, "wrap, grow, width 300:300:300")
        panel.add(startDateLabel)
        panel.add(startDateTimePicker, "wrap, grow, width 300:300:300")
        panel.add(endDateLabel)
        panel.add(endDateTimePicker, "wrap, grow, width 300:300:300")

        val result = JOptionPane.showConfirmDialog(null, panel, "VEvent", JOptionPane.OK_CANCEL_OPTION)

        if (result == JOptionPane.OK_OPTION) {
            val summary = summaryTextField.text
            val description = descriptionTextField.text
            val location = locationTextField.text
            val startDate = startDateTimePicker.datePicker.date
            val startTime = startDateTimePicker.timePicker.time

            val endDate = endDateTimePicker.datePicker.date
            val endTime = endDateTimePicker.timePicker.time

            val startDateTime = getLocalDateTime(startDate, startTime)
            val endDateTime = getLocalDateTime(endDate, endTime)

            return VEentInput(
                summary = summary,
                description = description,
                location = location,
                startDate = startDateTime,
                endDate = endDateTime,
            )
        }

        return null
    }

    private fun getLocalDateTime(
        startDate: LocalDate?,
        startTime: LocalTime?,
    ): LocalDateTime? =
        when {
            startDate != null && startTime != null -> LocalDateTime.of(startDate, startTime)
            startDate != null -> LocalDateTime.of(startDate, LocalTime.now())
            startTime != null -> LocalDateTime.of(LocalDate.now(), startTime)
            else -> null
        }

    data class VCardInput(
        val formattedName: String,
        val familyName: String,
        val givenName: String,
        val additionalName: String,
        val namePrefix: String,
        val nameSuffix: String,
        val organization: String,
        val phoneNumber: String,
        val email: String,
        val address: String,
        val website: String,
        val birthday: LocalDate?,
        val photo: String,
        val note: String,
        val title: String,
        val revisionDate: String,
        val latitude: Double,
        val longitude: Double,
        val instantMessaging: String,
        val categories: String,
        val other: Map<String, String>,
    )

    data class VEentInput(
        val summary: String?,
        val description: String?,
        val location: String?,
        val startDate: LocalDateTime?,
        val endDate: LocalDateTime?,
    )
}
