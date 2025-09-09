package io.github.simonscholz.service

import java.io.IOException

object SimpleUrlBrowser {
    fun browse(url: String?) {
        println("Please open the following address in your browser: ")
        println(url)
        try {
            if (isMacOperatingSystem) {
                openUrlInDefaultMacOsBrowser(url)
            } else if (isWindowsOperatingSystem) {
                openUrlInDefaultWindowsBrowser(url)
            } else {
                openUrlInDefaultUnixBrowser(url)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val isMacOperatingSystem: Boolean
        get() = operatingSystemName.startsWith("Mac OS")

    private val isWindowsOperatingSystem: Boolean
        get() = operatingSystemName.startsWith("Windows")

    private val operatingSystemName: String
        get() = System.getProperty("os.name")

    @Throws(IOException::class)
    private fun openUrlInDefaultMacOsBrowser(url: String?) {
        println("Attempting to open that address in the default browser now...")
        Runtime.getRuntime().exec(arrayOf("open", url))
    }

    @Throws(IOException::class)
    private fun openUrlInDefaultWindowsBrowser(url: String?) {
        println("Attempting to open that address in the default browser now...")
        Runtime.getRuntime().exec(arrayOf("rundll32", String.format("url.dll,FileProtocolHandler %s", url)))
    }

    @Throws(Exception::class)
    private fun openUrlInDefaultUnixBrowser(url: String?) {
        var browser: String? = null
        for (b in browsers) {
            if (browser == null &&
                Runtime
                    .getRuntime()
                    .exec(arrayOf("which", b))
                    .inputStream
                    .read() != -1
            ) {
                println("Attempting to open that address in the default browser now...")
                Runtime.getRuntime().exec(arrayOf(b.also { browser = it }, url))
            }
        }
        if (browser == null) {
            throw Exception("No web browser found")
        }
    }

    private val browsers =
        arrayOf(
            "google-chrome",
            "firefox",
            "mozilla",
            "epiphany",
            "konqueror",
            "netscape",
            "opera",
            "links",
            "lynx",
            "chromium",
            "brave-browser",
        )
}
