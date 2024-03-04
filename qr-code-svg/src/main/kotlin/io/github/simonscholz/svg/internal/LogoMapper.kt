package io.github.simonscholz.svg.internal

import io.github.simonscholz.qrcode.QrCodeConfig
import io.github.simonscholz.qrcode.QrLogoConfig
import io.github.simonscholz.svg.QrCodeSvgConfig
import io.github.simonscholz.svg.QrSvgLogoConfig

internal object LogoMapper {
    fun mapQrCodeSvgConfigToQrCodeConfig(qrCodeSvgConfig: QrCodeSvgConfig): QrCodeConfig =
        QrCodeConfig(
            qrCodeText = qrCodeSvgConfig.qrCodeText,
            qrCodeSize = qrCodeSvgConfig.qrCodeSize,
            qrLogoConfig = qrCodeSvgConfig.qrLogoConfig?.let(::mapQrSvgLogoConfigToQrLogoConfig),
            qrCodeColorConfig = qrCodeSvgConfig.qrCodeColorConfig,
            qrPositionalSquaresConfig = qrCodeSvgConfig.qrPositionalSquaresConfig,
            qrCodeDotStyler = qrCodeSvgConfig.qrCodeDotStyler,
            qrBorderConfig = qrCodeSvgConfig.qrBorderConfig,
        )

    private fun mapQrSvgLogoConfigToQrLogoConfig(qrSvgLogoConfig: QrSvgLogoConfig): QrLogoConfig? {
        if (qrSvgLogoConfig.logo == null && qrSvgLogoConfig.base64Logo == null) {
            return null
        } else if (qrSvgLogoConfig.svgLogoDocument != null) {
            // prefer svg logo over base64 and logo
            return null
        }

        return QrLogoConfig(
            logo = qrSvgLogoConfig.logo,
            base64Logo = qrSvgLogoConfig.base64Logo,
            relativeSize = qrSvgLogoConfig.relativeSize,
            bgColor = qrSvgLogoConfig.bgColor,
            shape = qrSvgLogoConfig.shape,
        )
    }
}
