package io.github.simonscholz.model

import io.github.simonscholz.qrcode.LogoShape

data class QrCodeConfig(
    val qrCodeContent: String,
    val size: Int,
    val backgroundColor: ColorInfo,
    val foregroundColor: ColorInfo,
    val dotShape: String,
    val logoBase64: String,
    val logoRelativeSize: Double,
    val logoBackgroundColor: ColorInfo,
    val logoShape: LogoShape,
    val borderColor: ColorInfo,
    val relativeBorderSize: Double,
    val borderRadius: Double,
    val positionalSquareIsCircleShaped: Boolean,
    val positionalSquareRelativeBorderRound: Double,
    val positionalSquareCenterColor: ColorInfo,
    val positionalSquareInnerSquareColor: ColorInfo,
    val positionalSquareOuterSquareColor: ColorInfo,
    val positionalSquareOuterBorderColor: ColorInfo,
)

data class ColorInfo(
    val red: Int,
    val green: Int,
    val blue: Int,
    val alpha: Int,
)
