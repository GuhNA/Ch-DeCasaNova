package com.fho.housewarmingparty.api.configuration.repository;

import java.util.Optional;

import com.fho.housewarmingparty.api.configuration.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    Optional<Configuration> findByUserId(Long userId);
}
