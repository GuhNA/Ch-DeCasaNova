package com.fho.housewarmingparty.api.product.mapper;

import com.fho.housewarmingparty.api.product.dto.ProductDTO;
import com.fho.housewarmingparty.api.product.entity.Product;
import com.fho.housewarmingparty.utils.mapper.DataMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper extends DataMapper<Product, ProductDTO> {

    @Mapping(target = "image", ignore = true)
    @Mapping(target = "user", ignore = true)
    ProductDTO toWithoutImageDto(Product entity);

    @Override
    @Mapping(target = "user", ignore = true)
    ProductDTO toDto(Product entity);
}
