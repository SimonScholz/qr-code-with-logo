package io.github.simonscholz.qrcode

import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Create a base64 encoded String from a BufferedImage.
 *
 * @return base64 encoded String
 */
@OptIn(ExperimentalEncodingApi::class)
fun RenderedImage.toBase64(imageFormatName: String = "png"): String =
    ByteArrayOutputStream().use {
        ImageIO.write(this, imageFormatName, it)
        Base64.Default.encode(it.toByteArray())
    }

/**
 * Create a BufferedImage from a base64 encoded String.
 */
@OptIn(ExperimentalEncodingApi::class)
@Throws(IOException::class)
fun String.imageFromBase64(): BufferedImage =
    Base64.Default.decode(this.toByteArray()).run {
        ByteArrayInputStream(this).use {
            ImageIO.read(it)
        }
    }
