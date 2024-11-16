package com.fho.housewarmingparty.api.user.entity;

import com.fho.housewarmingparty.utils.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "user", schema = "housewarming_party")
public class User extends AuditableEntity<Long> {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "email", nullable = false, length = 320, unique = true)
    private String email;

    @Column(name = "password")
    private String password;
}
