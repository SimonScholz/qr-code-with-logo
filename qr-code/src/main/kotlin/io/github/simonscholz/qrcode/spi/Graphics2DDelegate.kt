package io.github.simonscholz.qrcode.spi

import java.awt.Graphics2D

interface Graphics2DDelegate {
    fun drawQrCode(graphics: Graphics2D)
}
