package com.github.simonscholz;

import io.github.simonscholz.qrcode.QrCodeApi;
import io.github.simonscholz.qrcode.QrCodeConfig;
import io.github.simonscholz.qrcode.QrCodeFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        QrCodeApi qrCodeApi = QrCodeFactory.createQrCodeApi();
        var userHomeDir = System.getProperty("user.home");
        var qrCode = qrCodeApi.createQrImage(new QrCodeConfig("https://simonscholz.github.io/", 200));
        ImageIO.write(qrCode, "png", new File(userHomeDir, "/qr-with-defaults.png"));

        var logo = ImageIO.read(new File("/home/simon/Pictures/qr-codes/doorbit.png"));
    }
}
