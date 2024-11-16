package com.fho.housewarmingparty.api.configuration.entity;

import com.fho.housewarmingparty.api.image.entity.Image;
import com.fho.housewarmingparty.api.user.entity.User;
import com.fho.housewarmingparty.utils.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "configuration", schema = "housewarming_party")
public class Configuration extends AuditableEntity<Long> {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String primaryColor;

    @Column
    private String pixKey;

    @ManyToOne
    @JoinColumn(name = "background_image_id", referencedColumnName = "id")
    private Image backgroundImage;

    @Column
    private String title;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;
}
