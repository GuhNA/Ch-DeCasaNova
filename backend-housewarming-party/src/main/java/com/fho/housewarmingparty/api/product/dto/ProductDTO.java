package com.fho.housewarmingparty.api.product.dto;

import java.math.BigDecimal;

import com.fho.housewarmingparty.api.image.entity.Image;
import com.fho.housewarmingparty.api.product.entity.ProductStatus;
import com.fho.housewarmingparty.api.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {

    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private Image image;
    private String base64Image;
    private ProductStatus status;
    private User user;
}
