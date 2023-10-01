package com.github.simonscholz;

import io.github.simonscholz.qrcode.QrCodeApi;
import io.github.simonscholz.qrcode.QrCodeFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        QrCodeApi qrCodeApi = QrCodeFactory.createQrCodeApi();
        var qrCode = qrCodeApi.createQrImage("https://simonscholz.github.io/",
            200,
            true,
            0.2,
            Color.WHITE,
            Color.GRAY,
            Color.RED,
            0,
            null);
        var userHomeDir = System.getProperty("user.home");
        ImageIO.write(qrCode, "png", new File(userHomeDir, "/qr.png"));

        var qrCode2 = QrCodeFactory.createQrImage("https://simonscholz.github.io/");
        ImageIO.write(qrCode2, "png", new File(userHomeDir, "/qr-with-defaults.png"));
    }
}
