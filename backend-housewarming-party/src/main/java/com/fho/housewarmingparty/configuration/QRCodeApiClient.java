package com.fho.housewarmingparty.configuration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "qrCodeApi", url = "https://gerarqrcodepix.com.br/api/v1")
public interface QRCodeApiClient {

    @GetMapping
    byte[] generateQRCode(
        @RequestParam("nome") String name,
        @RequestParam("cidade") String city,
        @RequestParam("valor") String value,
        @RequestParam("saida") String outputType,
        @RequestParam("chave") String pixKey,
        @RequestParam("txid") String transactionId
    );
}
