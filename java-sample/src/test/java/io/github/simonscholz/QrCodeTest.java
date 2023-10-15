package io.github.simonscholz;

import io.github.simonscholz.qrcode.QrCodeApi;
import io.github.simonscholz.qrcode.QrCodeFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class QrCodeTest {

    @Test
    public void testMain(@TempDir Path tempDir) {
        assertDoesNotThrow(() -> {
            QrCodeApi qrCodeApi = QrCodeFactory.createQrCodeApi();
            Main.generateSamples(qrCodeApi, tempDir.toAbsolutePath().toString());
        });
    }

    @Test
    public void testQrCodeTypesMain(@TempDir Path tempDir) throws IOException {
        assertDoesNotThrow(() -> {
            QrCodeApi qrCodeApi = QrCodeFactory.createQrCodeApi();
            QrCodeTypesMain.generateSamples(qrCodeApi, tempDir.toAbsolutePath().toString());
        });
    }
}
