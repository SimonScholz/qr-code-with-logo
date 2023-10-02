package io.github.simonscholz;

import io.github.simonscholz.qrcode.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException {
        QrCodeApi qrCodeApi = QrCodeFactory.createQrCodeApi();
        var userHomeDir = System.getProperty("user.home");
        var qrCode = qrCodeApi.createQrImage(new QrCodeConfig("https://simonscholz.github.io/", 200));
        ImageIO.write(qrCode, "png", new File(userHomeDir, "/qr-with-defaults.png"));

        URL resource = Main.class.getClassLoader().getResource("avatar.png");
        if(resource != null) {
            BufferedImage logo = ImageIO.read(resource);

            Logo logoConfig = new Logo(logo, .2);
            QrCodeConfig qrCodeConfig = new QrCodeConfig("https://simonscholz.github.io/",
                300,
                new QrCode(),
                new PositionalSquares(),
                new Border(),
                logoConfig);
            BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
            ImageIO.write(qrWithImage, "png", new File(userHomeDir, "/qr-with-logo.png"));
        }

    }
}
