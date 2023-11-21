package io.github.simonscholz.model

import java.awt.Color

object Mapper {

    fun fromViewModel(viewModel: QrCodeConfigViewModel): QrCodeConfig {
        return QrCodeConfig(
            qrCodeContent = viewModel.qrCodeContent.value,
            size = viewModel.size.value,
            backgroundColor = viewModel.backgroundColor.value.toColorInfo(),
            foregroundColor = viewModel.foregroundColor.value.toColorInfo(),
            logo = viewModel.logo.value,
            logoRelativeSize = viewModel.logoRelativeSize.value,
            logoBackgroundColor = viewModel.logoBackgroundColor.value.toColorInfo(),
            logoShape = viewModel.logoShape.value,
            borderColor = viewModel.borderColor.value.toColorInfo(),
            relativeBorderSize = viewModel.relativeBorderSize.value,
            borderRadius = viewModel.borderRadius.value,
            positionalSquareIsCircleShaped = viewModel.positionalSquareIsCircleShaped.value,
            positionalSquareRelativeBorderRound = viewModel.positionalSquareRelativeBorderRound.value,
            positionalSquareCenterColor = viewModel.positionalSquareCenterColor.value.toColorInfo(),
            positionalSquareInnerSquareColor = viewModel.positionalSquareInnerSquareColor.value.toColorInfo(),
            positionalSquareOuterSquareColor = viewModel.positionalSquareOuterSquareColor.value.toColorInfo(),
            positionalSquareOuterBorderColor = viewModel.positionalSquareOuterBorderColor.value.toColorInfo(),
        )
    }

    fun applyViewModel(qrCodeConfig: QrCodeConfig, qrCodeConfigViewModel: QrCodeConfigViewModel) {
        qrCodeConfigViewModel.apply {
            qrCodeContent.value = qrCodeConfig.qrCodeContent
            size.value = qrCodeConfig.size
            backgroundColor.value = qrCodeConfig.backgroundColor.toColor()
            foregroundColor.value = qrCodeConfig.foregroundColor.toColor()
            logo.value = qrCodeConfig.logo
            logoRelativeSize.value = qrCodeConfig.logoRelativeSize
            logoBackgroundColor.value = qrCodeConfig.logoBackgroundColor.toColor()
            logoShape.value = qrCodeConfig.logoShape
            borderColor.value = qrCodeConfig.borderColor.toColor()
            relativeBorderSize.value = qrCodeConfig.relativeBorderSize
            borderRadius.value = qrCodeConfig.borderRadius
            positionalSquareIsCircleShaped.value = qrCodeConfig.positionalSquareIsCircleShaped
            positionalSquareRelativeBorderRound.value = qrCodeConfig.positionalSquareRelativeBorderRound
            positionalSquareCenterColor.value = qrCodeConfig.positionalSquareCenterColor.toColor()
            positionalSquareInnerSquareColor.value = qrCodeConfig.positionalSquareInnerSquareColor.toColor()
            positionalSquareOuterSquareColor.value = qrCodeConfig.positionalSquareOuterSquareColor.toColor()
            positionalSquareOuterBorderColor.value = qrCodeConfig.positionalSquareOuterBorderColor.toColor()
        }
    }
}

fun Color.toColorInfo(): ColorInfo = ColorInfo(red, green, blue, alpha)

fun ColorInfo.toColor(): Color = Color(red, green, blue, alpha)