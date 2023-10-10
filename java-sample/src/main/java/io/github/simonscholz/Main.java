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

import static io.github.simonscholz.qrcode.QrCodeConfigKt.DEFAULT_IMG_SIZE;

public class Main {
    private static final double RELATIVE_SQUARE_BORDER_ROUND = .5;
    private static final Color VIOLETT = new Color(0x0063, 0x000B, 0x00A5);

    public static void main(final String[] args) throws IOException {
        final QrCodeApi qrCodeApi = QrCodeFactory.createQrCodeApi();
        final Path path = Paths.get(System.getProperty("user.home"), "qr-code-samples");
        Files.createDirectories(path);
        final var qrCodeDir = path.toAbsolutePath()
                                  .toString();

        createDefaultQrCode(qrCodeApi, qrCodeDir);

        final URL resource = Main.class.getClassLoader()
                                       .getResource("avatar-60x.png");
        if (resource != null) {
            createDefaultQrCodeWithLogo(resource, qrCodeApi, qrCodeDir);

            createDefaultQrCodeWithLogoAndBorder(resource, qrCodeApi, qrCodeDir);

            createDefaultQrCodeWithLogoAndBorderAndPositionalSquareBorderRadius(resource, qrCodeApi, qrCodeDir);

            createDefaultQrCodeWithLogoAndBorderAndPositionalSquareCircle(resource, qrCodeApi, qrCodeDir);

            decentRedColor(resource, qrCodeApi, qrCodeDir);

            mineCraftCreeperColor(resource, qrCodeApi, qrCodeDir);

            createTransparentQrCode(resource, qrCodeApi, qrCodeDir);
        }

        rainbowColor(qrCodeApi, qrCodeDir);

        notEnoughContrast(qrCodeApi, qrCodeDir);
    }

    private static void createDefaultQrCode(final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final var qrCode = qrCodeApi.createQrImage(new QrCodeConfig("https://simonscholz.github.io/", DEFAULT_IMG_SIZE));
        ImageIO.write(qrCode, "png", new File(qrCodeDir, "/qr-with-defaults-java.png"));
    }

    private static void createDefaultQrCodeWithLogo(final URL resource, final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final BufferedImage logo = ImageIO.read(resource);

        final QrCodeConfig qrCodeConfig = new QrCodeConfig("https://simonscholz.github.io/", DEFAULT_IMG_SIZE, new QrLogoConfig(logo));
        final BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/qr-with-logo-java.png"));
    }

    private static void createDefaultQrCodeWithLogoAndBorder(final URL resource, final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final BufferedImage logo = ImageIO.read(resource);

        final QrCodeConfig qrCodeConfig = new QrCodeConfig.Builder("https://simonscholz.github.io/").qrBorderConfig(Color.BLACK)
                                                                                                    .qrLogoConfig(logo)
                                                                                                    .build();
        final BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/qr-with-logo-and-border-java.png"));
    }

    private static void createDefaultQrCodeWithLogoAndBorderAndPositionalSquareBorderRadius(final URL resource, final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final BufferedImage logo = ImageIO.read(resource);

        final QrCodeConfig qrCodeConfig = new QrCodeConfig.Builder("https://simonscholz.github.io/").qrBorderConfig(Color.BLACK)
                                                                                                    .qrLogoConfig(logo)
                                                                                                    .qrPositionalSquaresConfig(new QrPositionalSquaresConfig.Builder().relativeSquareBorderRound(RELATIVE_SQUARE_BORDER_ROUND)
                                                                                                                                                                .build())
                                                                                                    .build();
        final BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/qr-with-logo-and-border-and-p-border-round-java.png"));
    }

    private static void createDefaultQrCodeWithLogoAndBorderAndPositionalSquareCircle(final URL resource, final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final BufferedImage logo = ImageIO.read(resource);

        final QrCodeConfig qrCodeConfig = new QrCodeConfig.Builder("https://simonscholz.github.io/").qrBorderConfig(Color.BLACK)
                                                                                                    .qrLogoConfig(logo)
                                                                                                    .qrPositionalSquaresConfig(new QrPositionalSquaresConfig(true))
                                                                                                    .build();
        final BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/qr-with-logo-and-border-and-p-border-circle-java.png"));
    }

    private static void decentRedColor(final URL resource, final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final BufferedImage logo = ImageIO.read(resource);

        final QrCodeConfig qrCodeConfig = new QrCodeConfig.Builder("https://simonscholz.github.io/").qrBorderConfig(Color.BLACK)
                                                                                                    .qrLogoConfig(logo)
                                                                                                    .qrPositionalSquaresConfig(new QrPositionalSquaresConfig(true, 0.2, Color.RED))
                                                                                                    .build();
        final BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/decent-red-color-java.png"));
    }

    private static void mineCraftCreeperColor(final URL resource, final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final BufferedImage logo = ImageIO.read(resource);

        final var brighterGreen = Color.GREEN.brighter();
        final var darkerGreen = Color.GREEN.darker()
                                           .darker()
                                           .darker();

        final QrPositionalSquaresConfig positionalSquaresConfig = new QrPositionalSquaresConfig.Builder().relativeSquareBorderRound(RELATIVE_SQUARE_BORDER_ROUND)
                                                                                                         .centerColor(brighterGreen)
                                                                                                         .innerSquareColor(darkerGreen)
                                                                                                         .outerSquareColor(brighterGreen)
                                                                                                         .outerBorderColor(darkerGreen)
                                                                                                         .build();

        final QrCodeConfig qrCodeConfig = new QrCodeConfig.Builder("https://simonscholz.github.io/").qrBorderConfig(Color.WHITE)
                                                                                                    .qrLogoConfig(logo)
                                                                                                    .qrCodeColorConfig(darkerGreen, brighterGreen)
                                                                                                    .qrPositionalSquaresConfig(positionalSquaresConfig)
                                                                                                    .build();
        final BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/minecraft-creeper-color-java.png"));
    }

    private static void notEnoughContrast(final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final QrPositionalSquaresConfig positionalSquaresConfig = new QrPositionalSquaresConfig.Builder().centerColor(Color.BLUE)
                                                                               .innerSquareColor(VIOLETT)
                                                                               .outerSquareColor(Color.BLUE)
                                                                               .outerBorderColor(VIOLETT)
                                                                               .build();
        final QrCodeConfig qrCodeConfig = new QrCodeConfig.Builder("https://simonscholz.github.io/").qrCodeColorConfig(Color.BLUE, VIOLETT)
                                                                                                    .qrPositionalSquaresConfig(positionalSquaresConfig)
                                                                                                    .build();
        final BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/not-enough-contrast-java.png"));
    }

    private static void rainbowColor(final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final URL resource = Objects.requireNonNull(Main.class.getClassLoader()
                                                              .getResource("rainbow.png"));
        final BufferedImage logo = ImageIO.read(resource);

        final QrCodeConfig qrCodeConfig = new QrCodeConfig.Builder("https://simonscholz.github.io/").qrBorderConfig(Color.YELLOW)
                                                                                                    .qrLogoConfig(logo)
                                                                                                    .qrCodeColorConfig(Color.BLUE, VIOLETT)
                                                                                                    .qrPositionalSquaresConfig(new QrPositionalSquaresConfig(true, RELATIVE_SQUARE_BORDER_ROUND, Color.PINK, Color.GREEN, Color.RED, Color.CYAN))
                                                                                                    .build();
        final BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        ImageIO.write(qrWithImage, "png", new File(qrCodeDir, "/rainbow-color-java.png"));
    }

    private static void createTransparentQrCode(final URL resource, final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final BufferedImage logo = ImageIO.read(resource);
        final Color transparent = new Color(0, 0, 0, 0);

        final QrPositionalSquaresConfig positionalSquaresConfig =
            new QrPositionalSquaresConfig.Builder()
                 .circleShaped(true)
                 .centerColor(Color.BLUE)
                 .innerSquareColor(Color.WHITE)
                 .outerSquareColor(Color.BLUE)
                 .outerBorderColor(transparent)
                 .build();

        final QrCodeConfig qrCodeConfig = new QrCodeConfig.Builder("https://simonscholz.github.io/")
            .qrCodeSize(150)
            .qrPositionalSquaresConfig(positionalSquaresConfig)
            .qrLogoConfig(logo)
            .qrCodeColorConfig(transparent, Color.BLUE)
            .build();
        final BufferedImage qrWithImage = qrCodeApi.createQrImage(qrCodeConfig);
        drawQrCodeOnImage(qrWithImage, qrCodeDir);
    }

    private static void drawQrCodeOnImage(final BufferedImage qrCode, final String qrCodeDir) throws IOException {
        final URL url = Objects.requireNonNull(Main.class.getClassLoader()
                                                         .getResource("cup.jpg"));
        final BufferedImage mainImg = ImageIO.read(url);
        final var graphics = mainImg.getGraphics();
        graphics.drawImage(qrCode, 330, 600, null);
        graphics.dispose();
        ImageIO.write(mainImg, "png", new File(qrCodeDir, "/transparent-color-java.png"));
    }
}
