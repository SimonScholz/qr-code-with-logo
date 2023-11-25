package io.github.simonscholz.qrcode

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Create a base64 encoded String from a BufferedImage.
 *
 * @return base64 encoded String
 */
@OptIn(ExperimentalEncodingApi::class)
fun BufferedImage.toBase64(): String =
    ByteArrayOutputStream().use {
        ImageIO.write(this, "png", it)
        Base64.Default.encode(it.toByteArray())
    }
