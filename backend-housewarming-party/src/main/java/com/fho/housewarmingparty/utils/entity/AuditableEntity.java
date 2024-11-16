package com.fho.housewarmingparty.utils.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity<I extends Serializable> implements BaseEntity<I> {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
}