package com.fho.housewarmingparty.api.image.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "image", schema = "housewarming_party")
public class Image {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String base64;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageType type;
}
