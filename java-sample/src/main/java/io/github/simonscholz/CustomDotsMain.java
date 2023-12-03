package io.github.simonscholz;

import io.github.simonscholz.qrcode.QrCodeApi;
import io.github.simonscholz.qrcode.QrCodeConfig;
import io.github.simonscholz.qrcode.QrCodeConfig.Builder;
import io.github.simonscholz.qrcode.QrCodeDotShape;
import io.github.simonscholz.qrcode.QrCodeDotStyler;
import io.github.simonscholz.qrcode.QrCodeFactory;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomDotsMain {

    public static void main(final String[] args) throws IOException {
        final Path path = Paths.get(System.getProperty("user.home"), "qr-code-samples");
        Files.createDirectories(path);
        final String qrCodeDir = path.toAbsolutePath().toString();
        final QrCodeApi qrCodeApi = QrCodeFactory.createQrCodeApi();

        createQrCodeWithDotStyle(QrCodeDotShape.CIRCLE, qrCodeDir, qrCodeApi, "qr-with-CIRCLE-dots-java.png");
        createQrCodeWithDotStyle(QrCodeDotShape.ROUNDED_SQUARE, qrCodeDir, qrCodeApi, "qr-with-ROUND-SQUARE-dots-java.png");
        createQrCodeWithDotStyle(QrCodeDotShape.HEXAGON, qrCodeDir, qrCodeApi, "qr-with-HEXAGON-dots-java.png");
        createQrCodeWithDotStyle(QrCodeDotShape.TRIANGLE, qrCodeDir, qrCodeApi, "qr-with-TRIANGLE-dots-java.png");
        createQrCodeWithDotStyle(QrCodeDotShape.HEART, qrCodeDir, qrCodeApi, "qr-with-HEART-dots-java.png");
        createQrCodeWithDotStyle(QrCodeDotShape.HOUSE, qrCodeDir, qrCodeApi, "qr-with-HOUSE-dots-java.png");

        createQrCodeWithDotStyler(CustomDotsMain::drawColorfulHouseWithDoorAndWindow, qrCodeDir, qrCodeApi, "qr-with-COLORFUL-HOUSE-dots-java.png");
        createQrCodeWithDotStyler(CustomDotsMain::drawSmiley, qrCodeDir, qrCodeApi, "qr-with-SMILEY-dots-java.png");
        createQrCodeWithDotStyler(CustomDotsMain::drawSkull, qrCodeDir, qrCodeApi, "qr-with-SKULL-dots-java.png");
        createQrCodeWithDotStyler(CustomDotsMain::drawPumpkin, qrCodeDir, qrCodeApi, "qr-with-pumpkin-dots-java.png");
        createQrCodeWithDotStyler(CustomDotsMain::drawEvilPumpkin, qrCodeDir, qrCodeApi, "qr-with-pumpkin-evil-dots-java.png");
        createQrCodeWithDotStyler(CustomDotsMain::drawChristmasTree, qrCodeDir, qrCodeApi, "qr-with-christmas-tree-dots-java.png");
    }

    private static void createQrCodeWithDotStyle(final QrCodeDotShape dotStyle, final String qrCodeDir, final QrCodeApi qrCodeApi, final String filename) throws IOException {
        final QrCodeConfig qrCodeConfig = new Builder("https://simonscholz.github.io/").qrCodeDotStyler(dotStyle)
                                                                                       .qrCodeSize(800)
                                                                                       .build();
        final BufferedImage qrCodeImage = qrCodeApi.createQrCodeImage(qrCodeConfig);
        ImageIO.write(qrCodeImage, "png", new File(qrCodeDir, '/' + filename));
    }

    private static void createQrCodeWithDotStyler(final QrCodeDotStyler dotStyler, final String qrCodeDir, final QrCodeApi qrCodeApi, final String filename) throws IOException {
        final QrCodeConfig qrCodeConfig = new Builder("https://simonscholz.github.io/").qrCodeDotStyler(dotStyler)
                                                                                       .qrCodeSize(800)
                                                                                       .build();
        final BufferedImage qrCodeImage = qrCodeApi.createQrCodeImage(qrCodeConfig);
        ImageIO.write(qrCodeImage, "png", new File(qrCodeDir, '/' + filename));
    }

    private static void drawChristmasTree(final int x, final int y, final int dotSize, final Graphics2D graphics) {
        drawDotImage(x, y, dotSize, graphics, "christmas_tree.png");
    }

    private static void drawColorfulHouseWithDoorAndWindow(final int x, final int y, final int size, final Graphics2D graphic) {
        final int roofHeight = size / 2;
        final int houseWidth = size;
        final int houseHeight = size - roofHeight;

        // Draw the base of the house
        graphic.setColor(Color.RED);
        graphic.fillRect(x, y + roofHeight, houseWidth, houseHeight);

        // Draw the roof
        graphic.setColor(Color.BLUE);
        final int[] roofXPoints = {
            x,
            x + houseWidth / 2,
            x + houseWidth
        };
        final int[] roofYPoints = {
            y + roofHeight,
            y,
            y + roofHeight
        };
        graphic.fillPolygon(roofXPoints, roofYPoints, 3);

        // Draw the door
        final int doorWidth = size / 5;
        final int doorHeight = size / 2 - 1;
        final int doorX = x + (houseWidth - size / 5) / 2 + size / 10;
        final int doorY = y + roofHeight + houseHeight - doorHeight + 1;
        graphic.setColor(Color.GREEN);
        graphic.fillRect(doorX, doorY, doorWidth, doorHeight);

        // Draw the window
        final int windowSize = size / 5;
        final int windowX = x + size / 5;
        final int windowY = y + roofHeight + size / 5;
        graphic.setColor(Color.YELLOW);
        graphic.fillRect(windowX, windowY, windowSize, windowSize);
    }

    private static void drawDotImage(final int x, final int y, final int dotSize, final Graphics2D graphics, final String image) {
        final URL resource = CustomDotsMain.class.getClassLoader()
                                                 .getResource(image);
        if (resource != null) {
            try {
                final BufferedImage imageDot = ImageIO.read(resource);
                graphics.drawImage(imageDot, x, y, dotSize, dotSize, null);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void drawEvilPumpkin(final int x, final int y, final int dotSize, final Graphics2D graphics) {
        drawDotImage(x, y, dotSize, graphics, "halloween_pumpkin_evil.png");
    }

    private static void drawPumpkin(final int x, final int y, final int dotSize, final Graphics2D graphics) {
        drawDotImage(x, y, dotSize, graphics, "halloween_pumpkin.png");
    }

    private static void drawSkull(final int x, final int y, final int dotSize, final Graphics2D graphics) {
        drawDotImage(x, y, dotSize, graphics, "skull_fill.png");
    }

    private static void drawSmiley(final int x, final int y, final int dotSize, final Graphics2D graphics) {
        drawDotImage(x, y, dotSize, graphics, "smiley_fill.png");
    }
}
