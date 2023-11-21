package io.github.simonscholz.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.simonscholz.model.Mapper
import io.github.simonscholz.model.QrCodeConfig
import io.github.simonscholz.model.QrCodeConfigViewModel
import java.io.File
import java.util.prefs.Preferences

class ConfigService(
    private val qrCodeConfigViewModel: QrCodeConfigViewModel,
) {
    private val objectMapper = ObjectMapper().registerKotlinModule()
    private val preferences = Preferences.userRoot().node("qr-code-app")

    fun saveConfig() {
        val config = Mapper.fromViewModel(qrCodeConfigViewModel)
        val configJson = objectMapper.writeValueAsString(config)
        preferences.put("config", configJson)
    }

    fun saveConfigFile(filePath: String) {
        val config = Mapper.fromViewModel(qrCodeConfigViewModel)
        val configJson = objectMapper.writeValueAsString(config)
        val finalFilePath = if (filePath.endsWith(".json")) filePath else "$filePath.json"
        val configJsonFile = File(finalFilePath)
        configJsonFile.writeText(configJson)
    }

    fun loadConfig() {
        preferences.get("config", null)?.let {
            val config = objectMapper.readValue(it, QrCodeConfig::class.java)
            Mapper.applyViewModel(config, qrCodeConfigViewModel)
        }
    }

    fun loadConfigFile(filePath: String) {
        val configJsonFile = File(filePath)
        val configJson = configJsonFile.readText()
        val config = objectMapper.readValue(configJson, QrCodeConfig::class.java)
        Mapper.applyViewModel(config, qrCodeConfigViewModel)
    }
}
