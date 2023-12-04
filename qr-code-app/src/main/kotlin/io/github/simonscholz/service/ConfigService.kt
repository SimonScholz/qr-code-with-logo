package io.github.simonscholz.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.simonscholz.model.Mapper
import io.github.simonscholz.model.QrCodeConfig
import io.github.simonscholz.model.QrCodeConfigViewModel
import java.io.File
import java.nio.file.Paths
import java.util.prefs.Preferences
import kotlin.io.path.createDirectories

class ConfigService(
    private val qrCodeConfigViewModel: QrCodeConfigViewModel,
) {
    private val objectMapper = ObjectMapper().registerKotlinModule()

    fun saveConfig() {
        runCatching {
            val config = Mapper.fromViewModel(qrCodeConfigViewModel)
            objectMapper.writeValue(getConfigFile(), config)
        }.onFailure {
            println("Failed to save config to preferences. ${it.message}")
            it.printStackTrace()
        }
    }

    fun loadConfig() {
        runCatching {
            val config = objectMapper.readValue(getConfigFile(), QrCodeConfig::class.java)
            Mapper.applyViewModel(config, qrCodeConfigViewModel)
        }.onFailure {
            println("Failed to load config from preferences. ${it.message}")
            resetConfig()
        }
    }

    fun resetConfig() {
        runCatching {
            File(getQrCodeAppDataFolder(), QR_CODE_CONFIG_FILE).delete()
        }.onFailure {
            println("Failed to delete config file. ${it.message}")
            it.printStackTrace()
        }
    }

    fun saveConfigFile(filePath: String) {
        val config = Mapper.fromViewModel(qrCodeConfigViewModel)
        val configJson = objectMapper.writeValueAsString(config)
        val finalFilePath = if (filePath.endsWith(".json")) filePath else "$filePath.json"
        val configJsonFile = File(finalFilePath)
        configJsonFile.writeText(configJson)
    }

    fun loadConfigFile(filePath: String) {
        val configJsonFile = File(filePath)
        val configJson = configJsonFile.readText()
        val config = objectMapper.readValue(configJson, QrCodeConfig::class.java)
        Mapper.applyViewModel(config, qrCodeConfigViewModel)
    }

    private fun getConfigFile(): File {
        val configDirectory = Paths.get(getQrCodeAppDataFolder()).createDirectories()
        return File(configDirectory.toFile(), QR_CODE_CONFIG_FILE)
    }

    private fun getQrCodeAppDataFolder(): String {
        val os = System.getProperty("os.name").lowercase()

        return when {
            os.contains("win") -> {
                // Windows
                System.getenv("APPDATA")?.let { "$it/qr-code-app" } ?: throw IllegalStateException("APPDATA environment variable not found.")
            }
            os.contains("nix") || os.contains("nux") || os.contains("mac") -> {
                // Linux or macOS
                val homeDir = System.getProperty("user.home")
                "$homeDir/.config/qr-code-app"
            }
            else -> throw UnsupportedOperationException("Unsupported operating system: $os")
        }
    }

    fun saveLastUsedDirectory(id: String, directory: File) {
        preferences.put(id, directory.absolutePath)
    }

    fun getLastUsedDirectory(id: String): File? {
        val lastUsedDirectory = preferences.get(id, null)
        return if (lastUsedDirectory != null) {
            File(lastUsedDirectory)
        } else {
            null
        }
    }

    companion object {
        private const val QR_CODE_CONFIG_FILE = "config.json"
        private val preferences: Preferences = Preferences.userRoot().node("QrCodeAppPreferences")
    }
}
