package io.github.simonscholz;

import io.github.simonscholz.qrcode.QrCodeApi;
import io.github.simonscholz.qrcode.QrCodeConfig;
import io.github.simonscholz.qrcode.QrCodeConfig.Builder;
import io.github.simonscholz.qrcode.QrCodeExtensionsKt;
import io.github.simonscholz.qrcode.QrCodeFactory;

import java.awt.image.BufferedImage;

public class Base64Main {

    public static void main(final String[] args) {
        final QrCodeApi qrCodeApi = QrCodeFactory.createQrCodeApi();
        final QrCodeConfig qrCodeConfig = new Builder("https://simonscholz.github.io/").build();
        // using createBase64QrCodeImage
        final String base64Image = qrCodeApi.createBase64QrCodeImage(qrCodeConfig);
        System.out.println(base64Image);

        // using createBase64QrCodeImageWithSize
        final BufferedImage qrCodeImage = qrCodeApi.createQrCodeImage(qrCodeConfig);
        final String base64Image2 = QrCodeExtensionsKt.toBase64(qrCodeImage);
        System.out.println(base64Image2);
    }
}
