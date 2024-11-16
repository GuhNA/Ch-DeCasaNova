package com.fho.housewarmingparty.api.product.repository;

import java.util.List;
import java.util.Optional;

import com.fho.housewarmingparty.api.product.entity.Product;
import com.fho.housewarmingparty.api.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>, ProductRepositoryCustom {

    List<Product> findByStatusAndUserId(ProductStatus status, Long loggedInUserId);

    List<Product> findByUserId(Long loggedInUserId);

    Optional<Product> findByIdAndUserId(Long id, Long loggedInUserId);
}
