package com.fho.housewarmingparty.api.product.dto;

import java.math.BigDecimal;

import com.fho.housewarmingparty.api.product.entity.ProductStatus;

import lombok.Data;

@Data
public class ProductCriteria {

    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private ProductStatus status;
    private String filter;
    private Long userId;
}
