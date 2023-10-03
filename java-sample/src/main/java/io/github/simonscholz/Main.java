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
        createDefaultQrCode(qrCodeApi, userHomeDir);

        URL resource = Main.class.getClassLoader().getResource("avatar.png");
        if(resource != null) {
            createDefaultQrCodeWithLogo(resource, qrCodeApi, userHomeDir);

            createDefaultQrCodeWithLogoAndCustomColors(resource, qrCodeApi, userHomeDir);
        }
    }

    private static void createDefaultQrCodeWithLogoAndCustomColors(URL resource, QrCodeApi qrCodeApi, String userHomeDir) throws IOException {
        BufferedImage logo = ImageIO.read(resource);

        Color darkGreen = Color.GREEN.darker().darker().darker();
        Color brightGreen = Color.GREEN.brighter();

        QrLogoConfig qrLogoConfigConfig = new QrLogoConfig(logo, .2);
        QrCodeColorConfig qrCodeColorConfig = new QrCodeColorConfig(darkGreen, brightGreen);
        QrPositionalSquaresConfig qrPositionalSquaresConfig = new QrPositionalSquaresConfig(darkGreen, brightGreen);
        QrBorderConfig qrBorderConfig = new QrBorderConfig(Color.WHITE);
        QrCodeConfig qrCodeConfig = new QrCodeConfig("https://simonscholz.github.io/",
            300,
            qrLogoConfigConfig,
            qrCodeColorConfig,
            qrPositionalSquaresConfig,
            qrBorderConfig
        );
        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(userHomeDir, "/qr-with-logo.colorful-java.png"));
    }

    private static void createDefaultQrCodeWithLogo(URL resource, QrCodeApi qrCodeApi, String userHomeDir) throws IOException {
        BufferedImage logo = ImageIO.read(resource);

        QrLogoConfig qrLogoConfigConfig = new QrLogoConfig(logo, .2);
        QrCodeConfig qrCodeConfig = new QrCodeConfig("https://simonscholz.github.io/",
            300,
            qrLogoConfigConfig);
        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(userHomeDir, "/qr-with-logo-java.png"));
    }

    private static void createDefaultQrCode(QrCodeApi qrCodeApi, String userHomeDir) throws IOException {
        var qrCode = qrCodeApi.createQrImage(new QrCodeConfig("https://simonscholz.github.io/", 200));
        ImageIO.write(qrCode, "png", new File(userHomeDir, "/qr-with-defaults-java.png"));
    }
}
