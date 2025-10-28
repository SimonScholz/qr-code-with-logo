package io.github.simonscholz.model

import java.awt.Color

object Mapper {
    fun fromViewModel(viewModel: QrCodeConfigViewModel): QrCodeConfig =
        QrCodeConfig(
            qrCodeContent = viewModel.qrCodeContent.value,
            size = viewModel.size.value,
            backgroundColor = viewModel.backgroundColor.value.toColorInfo(),
            foregroundColor = viewModel.foregroundColor.value.toColorInfo(),
            dotShape = viewModel.dotShape.value.name,
            logoBase64 = viewModel.logoBase64.value,
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
            positionalSquareColorAdjustmentPatterns = viewModel.positionalSquareColorAdjustmentPatterns.value,
        )

    fun applyViewModel(
        qrCodeConfig: QrCodeConfig,
        qrCodeConfigViewModel: QrCodeConfigViewModel,
    ) {
        qrCodeConfigViewModel.apply {
            qrCodeContent.value = qrCodeConfig.qrCodeContent
            size.value = qrCodeConfig.size
            backgroundColor.value = qrCodeConfig.backgroundColor.toColor()
            foregroundColor.value = qrCodeConfig.foregroundColor.toColor()
            dotShape.value = DotShapes.valueOf(qrCodeConfig.dotShape)
            logoBase64.value = qrCodeConfig.logoBase64
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
            positionalSquareColorAdjustmentPatterns.value = qrCodeConfig.positionalSquareColorAdjustmentPatterns
        }
    }
}

fun Color.toColorInfo(): ColorInfo = ColorInfo(red, green, blue, alpha)

fun ColorInfo.toColor(): Color = Color(red, green, blue, alpha)
