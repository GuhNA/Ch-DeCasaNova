package com.fho.housewarmingparty.api.configuration.dto;

import com.fho.housewarmingparty.api.image.entity.Image;
import com.fho.housewarmingparty.api.user.entity.User;

import lombok.Data;

@Data
public class ConfigurationDTO {

    private Long id;
    private String primaryColor;
    private String title;
    private String pixKey;
    private Image backgroundImage;
    private String qrCodeBase64;
    private User user;
}
