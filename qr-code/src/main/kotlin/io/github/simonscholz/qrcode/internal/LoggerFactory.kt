package io.github.simonscholz.qrcode.internal

import java.util.logging.Logger

internal object LoggerFactory {
    /**
     * Creates a JUL [Logger] named after the provided class.
     */
    fun forClass(clazz: Class<*>): Logger = Logger.getLogger(clazz.name)

    /**
     * Inline variant for idiomatic usage in Kotlin files.
     */
    inline fun <reified T> forClass(): Logger = Logger.getLogger(T::class.java.name)
}
