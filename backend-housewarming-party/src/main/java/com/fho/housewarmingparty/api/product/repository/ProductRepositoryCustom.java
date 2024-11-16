package com.fho.housewarmingparty.api.product.repository;

import java.math.BigDecimal;

import com.fho.housewarmingparty.api.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public interface ProductRepositoryCustom {
    BigDecimal countTotalAmount(Specification<Product> specification);
}
