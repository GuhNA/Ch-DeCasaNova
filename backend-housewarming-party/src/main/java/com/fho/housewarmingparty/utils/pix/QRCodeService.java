package com.fho.housewarmingparty.utils.pix;

import java.math.BigDecimal;
import java.util.Base64;

import lombok.RequiredArgsConstructor;

import com.fho.housewarmingparty.configuration.QRCodeApiClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final QRCodeApiClient qrCodeApiClient;

    public String generateQRCode(String name, String city, BigDecimal amount, String pixKey, String transactionId) {
        try {
            byte[] qrCodeBytes = qrCodeApiClient.generateQRCode(
                    name,
                    city,
                    amount.setScale(2).toString(),
                    "qr",
                    pixKey,
                    transactionId
            );

            return Base64.getEncoder().encodeToString(qrCodeBytes);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR Code from external API", e);
        }
    }
}
