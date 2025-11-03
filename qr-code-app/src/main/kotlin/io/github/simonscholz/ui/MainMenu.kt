package io.github.simonscholz.ui

import io.github.simonscholz.qrcode.types.SimpleTypes
import io.github.simonscholz.qrcode.types.VCard
import io.github.simonscholz.qrcode.types.VEvent
import io.github.simonscholz.service.ConfigService
import io.github.simonscholz.service.SimpleUrlBrowser
import io.github.simonscholz.ui.dialogs.InputDialogs
import org.eclipse.core.databinding.observable.value.IObservableValue
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.AbstractAction
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.JSeparator
import javax.swing.KeyStroke
import kotlin.system.exitProcess

object MainMenu {
    fun createFrameMenu(
        frame: JFrame,
        qrCodeContentObservable: IObservableValue<String>,
        fileUI: FileUI,
        configService: ConfigService,
    ) {
        val menuBar = JMenuBar()
        frame.jMenuBar = menuBar

        createFileMenu(menuBar, frame, fileUI, configService)

        createSpecialContentMenu(menuBar, frame, qrCodeContentObservable)

        createGenerateCodeMenu(menuBar, frame, fileUI)

        createHelpMenu(menuBar, frame, configService)
    }

    private fun createGenerateCodeMenu(
        menuBar: JMenuBar,
        frame: JFrame,
        fileUI: FileUI,
    ) {
        val generateCodeMenu = JMenu("Generate Code")
        menuBar.add(generateCodeMenu)

        val copyJavaCodeMenuItem = JMenuItem("Copy Java Code")
        copyJavaCodeMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_DOWN_MASK)
        frame.rootPane.actionMap.put(
            "CopyJavaCodeAction",
            object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    fileUI.copyJavaCodeToClipboard()
                }
            },
        )
        copyJavaCodeMenuItem.addActionListener { fileUI.copyJavaCodeToClipboard() }
        generateCodeMenu.add(copyJavaCodeMenuItem)

        val copySvgJavaCodeMenuItem = JMenuItem("Copy SVG Java Code")
        copySvgJavaCodeMenuItem.addActionListener { fileUI.copySvgJavaCodeToClipboard() }
        generateCodeMenu.add(copySvgJavaCodeMenuItem)

        val copyKotlinCodeMenuItem = JMenuItem("Copy Kotlin Code")
        copyKotlinCodeMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK)
        frame.rootPane.actionMap.put(
            "CopyKotlinCodeAction",
            object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    fileUI.copyKotlinCodeToClipboard()
                }
            },
        )
        copyKotlinCodeMenuItem.addActionListener { fileUI.copyKotlinCodeToClipboard() }
        generateCodeMenu.add(copyKotlinCodeMenuItem)

        val copySvgKotlinCodeMenuItem = JMenuItem("Copy SVG Kotlin Code")
        copySvgKotlinCodeMenuItem.addActionListener { fileUI.copySvgKotlinCodeToClipboard() }
        generateCodeMenu.add(copySvgKotlinCodeMenuItem)
    }

    private fun createFileMenu(
        menuBar: JMenuBar,
        frame: JFrame,
        fileUI: FileUI,
        configService: ConfigService,
    ) {
        val fileMenu = JMenu("File")
        menuBar.add(fileMenu)

        val saveMenuItem = JMenuItem("Save Qr Code Image")
        // Add the keybinding for Save (Ctrl + S)
        saveMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)
        frame.rootPane.actionMap.put(
            "SaveAction",
            object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    fileUI.saveQrCodeImageFile()
                }
            },
        )

        val saveSvgMenuItem = JMenuItem("Save Qr Code as SVG")

        val copyMenuItem = JMenuItem("Copy Qr Code Image")
        // Add the keybinding for Save (Ctrl + C)
        copyMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK)
        frame.rootPane.actionMap.put(
            "CopyAction",
            object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    fileUI.copyImageToClipboard()
                }
            },
        )

        val copyBase64MenuItem = JMenuItem("Copy Qr Code Image in Base64")
        // Add the keybinding for Save (Ctrl + B)
        copyBase64MenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK)
        frame.rootPane.actionMap.put(
            "CopyBase64Action",
            object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    fileUI.copyBase64ImageToClipboard()
                }
            },
        )

        val copySvgMenuItem = JMenuItem("Copy Svg Qr Code Image")

        val importConfigMenuItem = JMenuItem("Import Config")
        // Add the keybinding for Save (Ctrl + I)
        importConfigMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK)
        frame.rootPane.actionMap.put(
            "ImportAction",
            object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    fileUI.loadConfig()
                }
            },
        )

        val exportConfigMenuItem = JMenuItem("Export Config")
        // Add the keybinding for Save (Ctrl + E)
        exportConfigMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK)
        frame.rootPane.actionMap.put(
            "ExportAction",
            object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    fileUI.saveConfig()
                }
            },
        )

        val exitMenuItem = JMenuItem("Exit")
        exitMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK)
        frame.rootPane.actionMap.put(
            "ExitAction",
            object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    exitApplication(configService)
                }
            },
        )

        saveMenuItem.addActionListener { fileUI.saveQrCodeImageFile() }
        saveSvgMenuItem.addActionListener { fileUI.saveQrCodeSvgFile() }
        copyMenuItem.addActionListener { fileUI.copyImageToClipboard() }
        copyBase64MenuItem.addActionListener { fileUI.copyBase64ImageToClipboard() }
        copySvgMenuItem.addActionListener { fileUI.copySvgImageToClipboard() }
        importConfigMenuItem.addActionListener { fileUI.loadConfig() }
        exportConfigMenuItem.addActionListener { fileUI.saveConfig() }
        exitMenuItem.addActionListener { exitApplication(configService) }

        fileMenu.add(saveMenuItem)
        fileMenu.add(saveSvgMenuItem)
        fileMenu.add(JSeparator())
        fileMenu.add(copyMenuItem)
        fileMenu.add(copyBase64MenuItem)
        fileMenu.add(copySvgMenuItem)
        fileMenu.add(JSeparator())
        fileMenu.add(importConfigMenuItem)
        fileMenu.add(exportConfigMenuItem)
        fileMenu.add(JSeparator())
        fileMenu.add(exitMenuItem)
    }

    private fun createSpecialContentMenu(
        menuBar: JMenuBar,
        frame: JFrame,
        qrCodeContentObservable: IObservableValue<String>,
    ) {
        val specialContentMenu = JMenu("Special Content")
        menuBar.add(specialContentMenu)

        val vCardMenuItem = JMenuItem("VCard")
        vCardMenuItem.addActionListener {
            InputDialogs.showVCardInputDialog()?.let {
                val qrCodeText =
                    VCard()
                        .apply {
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
                val qrCodeText =
                    VEvent()
                        .apply {
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
            InputDialogs
                .showTwoValueInputDialog(
                    title = "Geolocation",
                    firstLabel = "Latitude:",
                    secondLabel = "Longitude:",
                )?.let { (latitude, longitude) ->
                    val qrCodeText =
                        SimpleTypes.geolocation(
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
                val qrCodeText =
                    SimpleTypes.email(
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
            InputDialogs
                .showTwoValueInputDialog(
                    title = "SMS",
                    firstLabel = "Phone Number:",
                    secondLabel = "Message:",
                )?.let { (phoneNumber, message) ->
                    val qrCodeText =
                        SimpleTypes.sms(
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

        val wifiMenuItem = JMenuItem("WIFI")
        wifiMenuItem.addActionListener {
            InputDialogs
                .showThreeValueInputDialog(
                    title = "WIFI",
                    firstLabel = "SSID",
                    secondLabel = "Password",
                    thirdLabel = "Encryption type (e.g., WPA)",
                )?.let { (ssid, password, encryptionType) ->
                    val qrCodeText = SimpleTypes.wifi(ssid, password, encryptionType)
                    qrCodeContentObservable.value = qrCodeText
                }
        }
        specialContentMenu.add(wifiMenuItem)
    }

    private fun createHelpMenu(
        menuBar: JMenuBar,
        frame: JFrame,
        configService: ConfigService,
    ) {
        // Create the Help menu
        val helpMenu = JMenu("Help")
        menuBar.add(helpMenu)

        val gitHubRepoMenuItem = JMenuItem("GitHub Repository")
        val readmeMenuItem = JMenuItem("README")
        val issueMenuItem = JMenuItem("Open Issue")
        val resetPreferences = JMenuItem("Reset Preferences")

        gitHubRepoMenuItem.addActionListener { openURL(frame, "https://github.com/SimonScholz/qr-code-with-logo") }
        readmeMenuItem.addActionListener { openURL(frame, "https://github.com/SimonScholz/qr-code-with-logo/blob/main/README.adoc") }
        issueMenuItem.addActionListener { openURL(frame, "https://github.com/SimonScholz/qr-code-with-logo/issues") }
        resetPreferences.addActionListener {
            val result =
                JOptionPane.showConfirmDialog(
                    frame,
                    "Do you really want to reset all preferences?",
                    "Reset Preferences",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                )
            if (result == JOptionPane.YES_OPTION) {
                configService.resetConfig()
            }
        }

        helpMenu.add(gitHubRepoMenuItem)
        helpMenu.add(readmeMenuItem)
        helpMenu.add(issueMenuItem)
        helpMenu.add(resetPreferences)
    }

    private fun openURL(
        frame: JFrame,
        url: String,
    ) {
        try {
            SimpleUrlBrowser.browse(url)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle any exceptions that may occur when opening the URL
            JOptionPane.showMessageDialog(frame, "Error opening URL: ${e.message}", "Error", JOptionPane.ERROR_MESSAGE)
        }
    }

    private fun exitApplication(configService: ConfigService) {
        configService.saveConfig()
        exitProcess(0)
    }
}
