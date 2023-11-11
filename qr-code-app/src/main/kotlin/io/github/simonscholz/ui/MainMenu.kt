package io.github.simonscholz.ui

import io.github.simonscholz.qrcode.types.SimpleTypes
import io.github.simonscholz.qrcode.types.VCard
import io.github.simonscholz.qrcode.types.VEvent
import io.github.simonscholz.service.ImageService
import io.github.simonscholz.ui.dialogs.InputDialogs
import org.eclipse.core.databinding.observable.value.IObservableValue
import java.awt.Desktop
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.net.URI
import javax.swing.AbstractAction
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.KeyStroke
import kotlin.system.exitProcess

object MainMenu {
    fun createFrameMenu(frame: JFrame, qrCodeContentObservable: IObservableValue<String>, imageService: ImageService) {
        // Create and set the menu bar
        val menuBar = JMenuBar()
        frame.jMenuBar = menuBar

        createFileMenu(menuBar, frame, imageService)

        createSpecialContentMenu(menuBar, frame, qrCodeContentObservable)

        createHelpMenu(menuBar, frame)
    }

    private fun createFileMenu(menuBar: JMenuBar, frame: JFrame, imageService: ImageService) {
        val fileMenu = JMenu("File")
        menuBar.add(fileMenu)

        // Create menu items for File menu
        val saveMenuItem = JMenuItem("Save Qr Code Image")
        // Add the keybinding for Save (Ctrl + S)
        saveMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)
        frame.rootPane.actionMap.put(
            "SaveAction",
            object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    imageService.saveFile()
                }
            },
        )
        val exitMenuItem = JMenuItem("Exit")
        exitMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK)
        frame.rootPane.actionMap.put(
            "ExitAction",
            object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    exitApplication()
                }
            },
        )

        // Add ActionListeners to the menu items
        saveMenuItem.addActionListener { imageService.saveFile() }
        exitMenuItem.addActionListener { exitApplication() }

        // Add menu items to the File menu
        fileMenu.add(saveMenuItem)
        fileMenu.add(exitMenuItem)
    }

    private fun createSpecialContentMenu(menuBar: JMenuBar, frame: JFrame, qrCodeContentObservable: IObservableValue<String>) {
        val specialContentMenu = JMenu("Special Content")
        menuBar.add(specialContentMenu)

        val vCardMenuItem = JMenuItem("VCard")
        vCardMenuItem.addActionListener {
            InputDialogs.showVCardInputDialog()?.let {
                val qrCodeText = VCard().apply {
                    it.formattedName.takeIf { it.isNotEmpty() }?.let { formattedName(it) }
                    it.familyName.takeIf { it.isNotEmpty() }?.let { name(familyName = it) }
                    it.givenName.takeIf { it.isNotEmpty() }?.let { name(givenName = it) }
                    it.additionalName.takeIf { it.isNotEmpty() }?.let { name(additionalName = it) }
                    it.namePrefix.takeIf { it.isNotEmpty() }?.let { name(namePrefix = it) }
                    it.nameSuffix.takeIf { it.isNotEmpty() }?.let { name(nameSuffix = it) }
                    it.organization.takeIf { it.isNotEmpty() }?.let { organization(it) }
                    it.phoneNumber.takeIf { it.isNotEmpty() }?.let { phoneNumber(it) }
                    it.email.takeIf { it.isNotEmpty() }?.let { email(it) }
                    it.address.takeIf { it.isNotEmpty() }?.let { address(it) }
                    it.website.takeIf { it.isNotEmpty() }?.let { website(it) }
                    it.birthday?.let { birthday(it) }
                    it.photo.takeIf { it.isNotEmpty() }?.let { photo(it) }
                    it.note.takeIf { it.isNotEmpty() }?.let { note(it) }
                    it.title.takeIf { it.isNotEmpty() }?.let { title(it) }
                    it.revisionDate.takeIf { it.isNotEmpty() }?.let { revisionDate(it) }
                    geographicPosition(it.latitude, it.longitude)
                    it.instantMessaging.takeIf { it.isNotEmpty() }?.let { impp(it) }
                    it.categories.takeIf { it.isNotEmpty() }?.let { categories(it) }
                    it.other.takeIf { it.isNotEmpty() }?.let { other(it) }
                }.toVCardQrCodeText()

                qrCodeContentObservable.value = qrCodeText
            }
        }
        specialContentMenu.add(vCardMenuItem)

        val vEventMenuItem = JMenuItem("VEvent")
        vEventMenuItem.addActionListener {
            InputDialogs.showVEventInputDialog()?.let { (summary, description, location, startDate, endDate) ->
                val qrCodeText = VEvent().apply {
                    summary?.let { summary(summary) }
                    description?.let { description(description) }
                    location?.let { location(location) }
                    startDate?.let { startDate(startDate) }
                    endDate?.let { endDate(endDate) }
                }.toVEventQrCodeText()

                qrCodeContentObservable.value = qrCodeText
            }
        }
        specialContentMenu.add(vEventMenuItem)

        val geolocationMenuItem = JMenuItem("Geolocation")
        geolocationMenuItem.addActionListener {
            InputDialogs.showTwoValueInputDialog(
                title = "Geolocation",
                firstLabel = "Latitude:",
                secondLabel = "Longitude:",
            )?.let { (latitude, longitude) ->
                val qrCodeText = SimpleTypes.geolocation(
                    latitude = latitude.toDouble(),
                    longitude = longitude.toDouble(),
                )

                qrCodeContentObservable.value = qrCodeText
            }
        }
        specialContentMenu.add(geolocationMenuItem)

        val emailMenuItem = JMenuItem("Email")
        emailMenuItem.addActionListener {
            InputDialogs.showEmailInputDialog()?.let { (emailAddress, subject, body) ->
                val qrCodeText = SimpleTypes.email(
                    emailAddress = emailAddress,
                    subject = subject,
                    body = body,
                )

                qrCodeContentObservable.value = qrCodeText
            }
        }
        specialContentMenu.add(emailMenuItem)

        val phoneNumberMenuItem = JMenuItem("Phone Number")
        phoneNumberMenuItem.addActionListener {
            val inputValue = JOptionPane.showInputDialog(frame, "Enter phone number:")
            inputValue?.let {
                val qrCodeText = SimpleTypes.phoneNumber(inputValue)
                qrCodeContentObservable.value = qrCodeText
            }
        }
        specialContentMenu.add(phoneNumberMenuItem)

        val smsMenuItem = JMenuItem("SMS")
        smsMenuItem.addActionListener {
            InputDialogs.showTwoValueInputDialog(
                title = "SMS",
                firstLabel = "Phone Number:",
                secondLabel = "Message:",
            )?.let { (phoneNumber, message) ->
                val qrCodeText = SimpleTypes.sms(
                    phoneNumber = phoneNumber,
                    message = message,
                )

                qrCodeContentObservable.value = qrCodeText
            }
        }
        specialContentMenu.add(smsMenuItem)

        val urlMenuItem = JMenuItem("URL")
        urlMenuItem.addActionListener {
            val inputValue = JOptionPane.showInputDialog(frame, "Enter url:")
            inputValue?.let {
                val qrCodeText = SimpleTypes.url(inputValue)
                qrCodeContentObservable.value = qrCodeText
            }
        }
        specialContentMenu.add(urlMenuItem)
    }

    private fun createHelpMenu(menuBar: JMenuBar, frame: JFrame) {
        // Create the Help menu
        val helpMenu = JMenu("Help")
        menuBar.add(helpMenu)

        val gitHubRepoMenuItem = JMenuItem("GitHub Repository")
        val readmeMenuItem = JMenuItem("README")
        val issueMenuItem = JMenuItem("Open Issue")

        gitHubRepoMenuItem.addActionListener { openURL(frame, "https://github.com/SimonScholz/qr-code-with-logo") }
        readmeMenuItem.addActionListener { openURL(frame, "https://github.com/SimonScholz/qr-code-with-logo/blob/main/README.adoc") }
        issueMenuItem.addActionListener { openURL(frame, "https://github.com/SimonScholz/qr-code-with-logo/issues") }

        helpMenu.add(gitHubRepoMenuItem)
        helpMenu.add(readmeMenuItem)
        helpMenu.add(issueMenuItem)
    }

    private fun openURL(frame: JFrame, url: String) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI(url))
            } else {
                // Handle the case where the Desktop class is not supported
                JOptionPane.showMessageDialog(frame, "Desktop is not supported on this platform.", "Error", JOptionPane.ERROR_MESSAGE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle any exceptions that may occur when opening the URL
            JOptionPane.showMessageDialog(frame, "Error opening URL: ${e.message}", "Error", JOptionPane.ERROR_MESSAGE)
        }
    }

    private fun exitApplication() {
        exitProcess(0)
    }
}
