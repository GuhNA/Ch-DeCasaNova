package com.fho.housewarmingparty.api.image.repository;

import com.fho.housewarmingparty.api.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long>, JpaSpecificationExecutor<Image> {
}
