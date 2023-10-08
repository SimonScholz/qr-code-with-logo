package io.github.simonscholz;

import io.github.simonscholz.qrcode.QrCodeApi;
import io.github.simonscholz.qrcode.QrCodeConfig;
import io.github.simonscholz.qrcode.QrCodeFactory;
import io.github.simonscholz.qrcode.QrLogoConfig;
import io.github.simonscholz.qrcode.QrPositionalSquaresConfig;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        QrCodeApi qrCodeApi = QrCodeFactory.createQrCodeApi();
        final Path path = Paths.get(System.getProperty("user.home"), "qr-code-samples");
        Files.createDirectories(path);
        var qrCodeDir = path.toAbsolutePath().toString();

        createDefaultQrCode(qrCodeApi, qrCodeDir);

        URL resource = Main.class.getClassLoader().getResource("avatar-60x.png");
        if(resource != null) {
            createDefaultQrCodeWithLogo(resource, qrCodeApi, qrCodeDir);

            createDefaultQrCodeWithLogoAndBorder(resource, qrCodeApi, qrCodeDir);

            createDefaultQrCodeWithLogoAndBorderAndPositionalSquareBorderRadius(resource, qrCodeApi, qrCodeDir);

            createDefaultQrCodeWithLogoAndBorderAndPositionalSquareCircle(resource, qrCodeApi, qrCodeDir);

            decentRedColor(resource, qrCodeApi, qrCodeDir);

            mineCraftCreeperColor(resource, qrCodeApi, qrCodeDir);
        }

        rainbowColor(qrCodeApi, qrCodeDir);
    }

    private static void createDefaultQrCode(QrCodeApi qrCodeApi, String qrCodeDir) throws IOException {
        var qrCode = qrCodeApi.createQrImage(new QrCodeConfig("https://simonscholz.github.io/", 200));
        ImageIO.write(qrCode, "png", new File(qrCodeDir, "/qr-with-defaults-java.png"));
    }

    private static void createDefaultQrCodeWithLogo(URL resource, QrCodeApi qrCodeApi, String qrCodeDir) throws IOException {
        BufferedImage logo = ImageIO.read(resource);

        QrCodeConfig qrCodeConfig = new QrCodeConfig("https://simonscholz.github.io/",
            300,
            new QrLogoConfig(logo));
        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/qr-with-logo-java.png"));
    }

    private static void createDefaultQrCodeWithLogoAndBorder(URL resource, QrCodeApi qrCodeApi, String qrCodeDir) throws IOException {
        BufferedImage logo = ImageIO.read(resource);

        QrCodeConfig qrCodeConfig = new QrCodeConfig
            .Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .build();
        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/qr-with-logo-and-border-java.png"));
    }

    private static void createDefaultQrCodeWithLogoAndBorderAndPositionalSquareBorderRadius(URL resource, QrCodeApi qrCodeApi, String qrCodeDir) throws IOException {
        BufferedImage logo = ImageIO.read(resource);

        QrCodeConfig qrCodeConfig = new QrCodeConfig
            .Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(new QrPositionalSquaresConfig(false, 0.5))
            .build();
        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/qr-with-logo-and-border-and-p-border-round-java.png"));
    }

    private static void createDefaultQrCodeWithLogoAndBorderAndPositionalSquareCircle(URL resource, QrCodeApi qrCodeApi, String qrCodeDir) throws IOException {
        BufferedImage logo = ImageIO.read(resource);

        QrCodeConfig qrCodeConfig = new QrCodeConfig
            .Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(new QrPositionalSquaresConfig(true))
            .build();
        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/qr-with-logo-and-border-and-p-border-circle-java.png"));
    }

    private static void decentRedColor(URL resource, QrCodeApi qrCodeApi, String qrCodeDir) throws IOException {
        BufferedImage logo = ImageIO.read(resource);

        QrCodeConfig qrCodeConfig = new QrCodeConfig
            .Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.BLACK)
            .qrLogoConfig(logo)
            .qrPositionalSquaresConfig(new QrPositionalSquaresConfig(true, 0.2, Color.RED))
            .build();
        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/decent-red-color-java.png"));
    }

    private static void mineCraftCreeperColor(URL resource, QrCodeApi qrCodeApi, String qrCodeDir) throws IOException {
        BufferedImage logo = ImageIO.read(resource);

        var brighterGreen = Color.GREEN.brighter();
        var darkerGreen = Color.GREEN.darker().darker().darker();

        QrCodeConfig qrCodeConfig = new QrCodeConfig
            .Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.WHITE)
            .qrLogoConfig(logo)
            .qrCodeColorConfig(darkerGreen, brighterGreen)
            .qrPositionalSquaresConfig(new QrPositionalSquaresConfig(false, .5, brighterGreen, darkerGreen, brighterGreen, darkerGreen))
            .build();
        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/minecraft-creeper-color-java.png"));
    }

    private static void rainbowColor(QrCodeApi qrCodeApi, String qrCodeDir) throws IOException {
        URL resource = Objects.requireNonNull(Main.class.getClassLoader().getResource("rainbow.png"));
        BufferedImage logo = ImageIO.read(resource);

        QrCodeConfig qrCodeConfig = new QrCodeConfig
            .Builder("https://simonscholz.github.io/")
            .qrBorderConfig(Color.YELLOW)
            .qrLogoConfig(logo)
            .qrCodeColorConfig(Color.BLUE, new Color(0x0063,0x000B,0x00A5))
            .qrPositionalSquaresConfig(new QrPositionalSquaresConfig(true, .5, Color.PINK, Color.GREEN, Color.RED, Color.CYAN))
            .build();
        BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/rainbow-color-java.png"));
    }
}
