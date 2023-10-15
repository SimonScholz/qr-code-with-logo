package io.github.simonscholz;

import io.github.simonscholz.qrcode.QrCodeApi;
import io.github.simonscholz.qrcode.QrCodeConfig;
import io.github.simonscholz.qrcode.QrCodeFactory;
import io.github.simonscholz.qrcode.types.SimpleTypes;
import io.github.simonscholz.qrcode.types.VCard;
import io.github.simonscholz.qrcode.types.VEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static io.github.simonscholz.qrcode.QrCodeConfigKt.DEFAULT_IMG_SIZE;

/**
 * This class shows how to create QR Codes with different types.
 * For the design of a QR Code, please have a look at {@link Main}.
 */
public class QrCodeTypesMain {
    public static void main(final String[] args) throws IOException {
        final QrCodeApi qrCodeApi = QrCodeFactory.createQrCodeApi();
        final Path path = Paths.get(System.getProperty("user.home"), "qr-code-samples");
        Files.createDirectories(path);
        final String qrCodeDir = path.toAbsolutePath().toString();

        generateSamples(qrCodeApi, qrCodeDir);
    }

    static void generateSamples(QrCodeApi qrCodeApi, String qrCodeDir) throws IOException {
        createWithUrl(qrCodeApi, qrCodeDir);
        createWithGeolocation(qrCodeApi, qrCodeDir);
        createWithEmail(qrCodeApi, qrCodeDir);
        createWithPhoneNumber(qrCodeApi, qrCodeDir);
        createWithSms(qrCodeApi, qrCodeDir);
        createWithVEvent(qrCodeApi, qrCodeDir);
        createWithVCard(qrCodeApi, qrCodeDir);
    }

    private static void createWithUrl(final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final String url = SimpleTypes.url("https://simonscholz.github.io/");
        createDefaultQrCode(qrCodeApi, url, new File(qrCodeDir, "simple-url.png"));
    }

    private static void createWithGeolocation(final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final String geolocation = SimpleTypes.geolocation(53.59659752940634, 10.006589989354053);
        createDefaultQrCode(qrCodeApi, geolocation, new File(qrCodeDir, "simple-geolocation.png"));
    }

    private static void createWithEmail(final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final String email = SimpleTypes.email("simon@example.com", "Hello World", "This is a test email");
        createDefaultQrCode(qrCodeApi, email, new File(qrCodeDir, "simple-email.png"));
    }

    private static void createWithPhoneNumber(final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final String phoneNumber = SimpleTypes.phoneNumber("+49 176 12345678");
        createDefaultQrCode(qrCodeApi, phoneNumber, new File(qrCodeDir, "simple-phoneNumber.png"));
    }

    private static void createWithSms(final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final String sms = SimpleTypes.sms("+49 176 12345678", "Hello, this is a test SMS");
        createDefaultQrCode(qrCodeApi, sms, new File(qrCodeDir, "simple-sms.png"));
    }

    private static void createWithVEvent(final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final LocalDateTime startDateTime = LocalDateTime.now()
                                                         .plusWeeks(2);
        final VEvent vevent = new VEvent.Builder("QR Codes with Kotlin & Java")
                .location("Java User Group Hamburg")
                .startDate(startDateTime)
                .endDate(startDateTime.plusHours(2))
                .description("Let's create QR Codes with Kotlin & Java")
                .build();
        final String vEventQrCodeText = vevent.toVEventQrCodeText();
        createDefaultQrCode(qrCodeApi, vEventQrCodeText, new File(qrCodeDir, "vevent.png"));
    }

    private static void createWithVCard(final QrCodeApi qrCodeApi, final String qrCodeDir) throws IOException {
        final VCard vCard = new VCard.Builder("Simon Scholz")
                .email("simon@example.com")
                .address("Main Street 1", "Hamburg", "22855")
                .organization("Self Employed")
                .phoneNumber("+49 176 12345678")
                .website("https://simonscholz.github.io/")
                .build();
        final String vCardQrCodeText = vCard.toVCardQrCodeText();
        createDefaultQrCode(qrCodeApi, vCardQrCodeText, new File(qrCodeDir, "vCard.png"));
    }

    private static void createDefaultQrCode(final QrCodeApi qrCodeApi, final String qrCodeText, final File qrCodeFile) throws IOException {
        final BufferedImage qrCode = qrCodeApi.createQrCodeImage(new QrCodeConfig(qrCodeText, DEFAULT_IMG_SIZE));
        ImageIO.write(qrCode, "png", qrCodeFile);
    }
}
