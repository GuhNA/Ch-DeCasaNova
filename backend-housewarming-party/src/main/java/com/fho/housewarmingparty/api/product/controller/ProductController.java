package com.fho.housewarmingparty.api.product.controller;


import java.math.BigDecimal;
import java.util.List;

import com.fho.housewarmingparty.api.product.dto.ProductCriteria;
import com.fho.housewarmingparty.api.product.dto.ProductDTO;
import com.fho.housewarmingparty.api.product.entity.Product;
import com.fho.housewarmingparty.api.product.entity.ProductStatus;
import com.fho.housewarmingparty.api.product.mapper.ProductMapper;
import com.fho.housewarmingparty.api.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Product API", description = "API for managing products")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("product")
public class ProductController {

    private final ProductService service;
    private final ProductMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new product", description = "Adds a new product and returns its details")
    public ProductDTO create(@Valid @RequestBody ProductDTO dto) {
        log.info("Adding new product.");
        return mapper.toWithoutImageDto(service.create(dto));
    }

    @PostMapping("batch")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new products", description = "Adds new products and returns its details")
    public List<ProductDTO> createBatch(@Valid @RequestBody List<ProductDTO> dtos) {
        log.info("Adding new products.");
        List<Product> products = service.createBatch(dtos);
        return products.stream().map(mapper::toWithoutImageDto)
                .toList();
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a product", description = "Updates a product's details by its ID")
    public void update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        log.info("Updating product with ID: {}.", id);
        service.update(id, dto);
    }

    @PutMapping("{id}/donate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Donate a product", description = "Updates a product's to unavailable by its ID")
    public void update(@PathVariable Long id) {
        log.info("Donate product with ID: {}.", id);
        service.donate(id);
    }

    @GetMapping("total-amount")
    @Operation(summary = "Get total amount", description = "Returns the total amount of products")
    public BigDecimal countTotalAmount(ProductCriteria criteria) {
        log.info("Fetching product with criteria: {}.", criteria);
        return service.countTotalAmount(criteria);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get product by ID", description = "Returns the details of a product by its ID")
    public ProductDTO findById(@PathVariable Long id) {
        log.info("Fetching product with ID: {}.", id);
        return mapper.toDto(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "List all products", description = "Returns a list of all products")
    public List<ProductDTO> findAll(ProductCriteria criteria) {
        log.info("Fetching all products.");
        return mapper.toDto(service.findAll(criteria));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "List products by status", description = "Returns a list of products filtered by status")
    public List<ProductDTO> findByStatus(@PathVariable ProductStatus status) {
        log.info("Fetching products with status: {}.", status);
        return mapper.toDto(service.findAvailable(status));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Deleting product with ID: {}.", id);
        service.deleteById(id);
    }
}
