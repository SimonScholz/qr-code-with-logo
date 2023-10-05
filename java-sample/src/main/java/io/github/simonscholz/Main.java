package io.github.simonscholz;

import io.github.simonscholz.qrcode.QrCodeApi;
import io.github.simonscholz.qrcode.QrCodeConfig;
import io.github.simonscholz.qrcode.QrCodeConfig.Builder;
import io.github.simonscholz.qrcode.QrCodeFactory;
import io.github.simonscholz.qrcode.QrLogoConfig;
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException {
        QrCodeApi qrCodeApi = QrCodeFactory.createQrCodeApi();
        var userHomeDir = System.getProperty("user.home");
        createDefaultQrCode(qrCodeApi, userHomeDir);

        URL resource = Main.class.getClassLoader().getResource("avatar-60x.png");
        if(resource != null) {
            createDefaultQrCodeWithLogo(resource, qrCodeApi, userHomeDir);

            createDefaultQrCodeWithLogoAndCustomColors(resource, qrCodeApi, userHomeDir);

            createQrCodeWithRoundedPositionalSquares(resource, qrCodeApi, userHomeDir);

            reallyColorfulQrCode(resource, qrCodeApi, userHomeDir);
        }
    }

    private static void reallyColorfulQrCode(final URL resource, final QrCodeApi qrCodeApi, final String userHomeDir) throws IOException {
        BufferedImage logo = ImageIO.read(resource);

        final Color transparent = new Color(0, 0, 0, 0);

        QrPositionalSquaresConfig qrPositionalSquaresConfig =
            new QrPositionalSquaresConfig(Color.CYAN, new Color(0x0063,0x000B,0x00A5), Color.PINK, true);
        QrCodeConfig qrCodeConfig =new Builder("https://simonscholz.github.io/")
            .qrCodeSize(500)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(qrPositionalSquaresConfig)
            //.qrBorderConfig(Color.ORANGE.darker(), .05)
            .qrBorderConfig(transparent, .05)
            //.qrCodeColorConfig(Color.RED, Color.BLUE)
            .qrCodeColorConfig(transparent, Color.BLUE)
            .build();

        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(userHomeDir, "/qr-colorful-java.png"));
    }

    private static void createQrCodeWithRoundedPositionalSquares(URL resource, QrCodeApi qrCodeApi, String userHomeDir) throws IOException {
        BufferedImage logo = ImageIO.read(resource);

        QrPositionalSquaresConfig qrPositionalSquaresConfig =
            new QrPositionalSquaresConfig(Color.BLACK, Color.RED, Color.BLUE, true);
        QrCodeConfig qrCodeConfig =new Builder("https://simonscholz.github.io/")
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(qrPositionalSquaresConfig)
            .qrBorderConfig(Color.WHITE, .0)
            .build();

        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(userHomeDir, "/qr-rounded-positionals-java.png"));
    }

    private static void createDefaultQrCodeWithLogoAndCustomColors(URL resource, QrCodeApi qrCodeApi, String userHomeDir) throws IOException {
        BufferedImage logo = ImageIO.read(resource);

        Color darkGreen = Color.GREEN.darker().darker().darker();
        Color brightGreen = Color.GREEN.brighter();

        QrCodeConfig qrCodeConfig =new Builder("https://simonscholz.github.io/")
            .qrLogoConfig(logo)
            .qrCodeColorConfig(darkGreen, brightGreen)
            .qrPositionalSquaresConfig(new QrPositionalSquaresConfig(darkGreen, brightGreen))
            .qrBorderConfig(Color.WHITE)
            .build();

        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(userHomeDir, "/qr-with-logo-green-color-java.png"));
    }

    private static void createDefaultQrCodeWithLogo(URL resource, QrCodeApi qrCodeApi, String userHomeDir) throws IOException {
        BufferedImage logo = ImageIO.read(resource);

        QrCodeConfig qrCodeConfig = new QrCodeConfig("https://simonscholz.github.io/",
                                                     300,
                                                     new QrLogoConfig(logo));
        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(userHomeDir, "/qr-with-logo-java.png"));
    }

    private static void createDefaultQrCode(QrCodeApi qrCodeApi, String userHomeDir) throws IOException {
        var qrCode = qrCodeApi.createQrImage(new QrCodeConfig("https://simonscholz.github.io/", 200));
        ImageIO.write(qrCode, "png", new File(userHomeDir, "/qr-with-defaults-java.png"));
    }
}
