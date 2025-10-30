package io.github.simonscholz

import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.observables.SwingRealm
import io.github.simonscholz.service.CodeGeneratorService
import io.github.simonscholz.service.ConfigService
import io.github.simonscholz.service.ImageService
import io.github.simonscholz.ui.FileUI
import io.github.simonscholz.ui.ImageUI
import io.github.simonscholz.ui.MainMenu
import io.github.simonscholz.ui.MainUI
import io.github.simonscholz.ui.PropertiesUI
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.core.databinding.observable.sideeffect.ISideEffect
import org.eclipse.core.databinding.observable.value.IObservableValue
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

fun main() {
    // GraalVM Fix
//    if (System.getProperty("java.home") == null) {
//        println("No Java Home set, assuming that we are running from GraalVM. Fixing...")
//        System.setProperty("java.home", File(".").absolutePath)
//    }

    SwingUtilities.invokeLater {
        val frame = JFrame("QR code with logo by Simon Scholz")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val dataBindingContext = DataBindingContext(SwingRealm())
        val qrCodeConfigViewModel = QrCodeConfigViewModel()
        val configService = ConfigService(qrCodeConfigViewModel)

        val imageService = ImageService(qrCodeConfigViewModel)
        val fileUi = FileUI(CodeGeneratorService(qrCodeConfigViewModel), configService, imageService)
        MainMenu.createFrameMenu(frame, qrCodeConfigViewModel.qrCodeContent, fileUi, configService)

        val (imagePanel, setImage) = ImageUI.createImagePanel(imageService, fileUi)
        val (propertiesPanel, applyOnChange) =
            PropertiesUI.createPropertiesUI(qrCodeConfigViewModel, dataBindingContext) {
                onPropertyApply(qrCodeConfigViewModel.qrCodeContent, imageService, setImage, imagePanel)
            }

        val mainPanel = MainUI.createMainPanel(imagePanel, propertiesPanel)
        frame.add(mainPanel)

        val sideEffect: ISideEffect = trackChanges(qrCodeConfigViewModel, applyOnChange, imageService, setImage, imagePanel)
        frame.addWindowListener(
            object : WindowAdapter() {
                override fun windowClosing(windowEvent: WindowEvent) {
                    configService.saveConfig()
                    dataBindingContext.dispose()
                    sideEffect.dispose()
                }
            },
        )

        configService.loadConfig()

        frame.pack()
        frame.isVisible = true
    }
}

private fun trackChanges(
    qrCodeConfigViewModel: QrCodeConfigViewModel,
    applyOnChange: () -> Boolean,
    imageService: ImageService,
    setImage: (BufferedImage) -> Unit,
    imagePanel: JPanel,
): ISideEffect =
    ISideEffect.create(
        {
            qrCodeConfigViewModel.qrCodeContent.value
            qrCodeConfigViewModel.size.value
            qrCodeConfigViewModel.backgroundColor.value
            qrCodeConfigViewModel.foregroundColor.value
            qrCodeConfigViewModel.dotShape.value

            qrCodeConfigViewModel.logoBase64.value
            qrCodeConfigViewModel.logoRelativeSize.value
            qrCodeConfigViewModel.logoBackgroundColor.value
            qrCodeConfigViewModel.logoShape.value

            qrCodeConfigViewModel.borderColor.value
            qrCodeConfigViewModel.relativeBorderSize.value
            qrCodeConfigViewModel.borderRadius.value

            qrCodeConfigViewModel.positionalSquareIsCircleShaped.value
            qrCodeConfigViewModel.positionalSquareRelativeBorderRound.value
            qrCodeConfigViewModel.positionalSquareCenterColor.value
            qrCodeConfigViewModel.positionalSquareInnerSquareColor.value
            qrCodeConfigViewModel.positionalSquareOuterSquareColor.value
            qrCodeConfigViewModel.positionalSquareOuterBorderColor.value
            qrCodeConfigViewModel.positionalSquareColorAdjustmentPatterns.value
        },
    ) {
        if (applyOnChange()) {
            onPropertyApply(qrCodeConfigViewModel.qrCodeContent, imageService, setImage, imagePanel)
        }
    }

private fun onPropertyApply(
    qrCodeContentObservable: IObservableValue<String>,
    imageService: ImageService,
    setImage: (BufferedImage) -> Unit,
    imagePanel: JPanel,
) {
    if (qrCodeContentObservable.value.isNotBlank()) {
        val qrCodeImage = imageService.renderImage()
        setImage(qrCodeImage)
        imagePanel.revalidate()
    }
}
