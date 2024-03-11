package io.github.simonscholz.service

import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.service.codegen.RasterGraphicsCodeGenerator
import io.github.simonscholz.service.codegen.SvgGraphicsCodeGenerator

class CodeGeneratorService(
    qrCodeConfigViewModel: QrCodeConfigViewModel,
) {
    private val rasterGraphicsCodeGenerator = RasterGraphicsCodeGenerator(qrCodeConfigViewModel)
    private val svgGraphicsCodeGenerator = SvgGraphicsCodeGenerator(qrCodeConfigViewModel)

    fun generateRasterImageKotlinCode(): String = rasterGraphicsCodeGenerator.generateKotlinCode()

    fun generateSvgImageKotlinCode(): String = svgGraphicsCodeGenerator.generateKotlinCode()

    fun generateRasterImageJavaCode(): String = rasterGraphicsCodeGenerator.generateJavaCode()

    fun generateSvgImageJavaCode(): String = svgGraphicsCodeGenerator.generateJavaCode()
}
