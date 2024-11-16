package com.fho.housewarmingparty.api.product.entity;

import java.math.BigDecimal;

import com.fho.housewarmingparty.api.image.entity.Image;
import com.fho.housewarmingparty.api.user.entity.User;
import com.fho.housewarmingparty.utils.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "product", schema = "housewarming_party")
public class Product extends AuditableEntity<Long> {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String qrCodeImage;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
