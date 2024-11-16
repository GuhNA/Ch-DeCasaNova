package com.fho.housewarmingparty.api.image.dto;

import com.fho.housewarmingparty.api.image.entity.ImageType;

import lombok.Data;

@Data
public class ImageCriteria {

    private Long id;
    private String base64;
    private ImageType type;
}
