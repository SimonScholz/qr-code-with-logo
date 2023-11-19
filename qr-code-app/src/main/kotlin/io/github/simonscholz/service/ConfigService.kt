package io.github.simonscholz.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.simonscholz.model.Mapper
import io.github.simonscholz.model.QrCodeConfig
import io.github.simonscholz.model.QrCodeConfigViewModel
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class ConfigService(
    private val qrCodeConfigViewModel: QrCodeConfigViewModel,
) {

    private val objectMapper = ObjectMapper().registerKotlinModule()

    fun saveConfig(fileName: String = "config.json") {
        val path = Paths.get(System.getProperty("user.home"), ".qr-code-app")
        Files.createDirectories(path)
        val qrCodeDir = path.toAbsolutePath().toString()

        val config = Mapper.fromViewModel(qrCodeConfigViewModel)
        val configJson = objectMapper.writeValueAsString(config)
        val configJsonFile = File(qrCodeDir, fileName)
        configJsonFile.writeText(configJson)
    }

    fun loadConfig() {
        val path = Paths.get(System.getProperty("user.home"), ".qr-code-app")
        val configJsonFile = File(path.toAbsolutePath().toString(), "config.json")
        if (configJsonFile.exists()) {
            val configJson = configJsonFile.readText()
            val config = objectMapper.readValue(configJson, QrCodeConfig::class.java)
            Mapper.applyViewModel(config, qrCodeConfigViewModel)
        }
    }
}
