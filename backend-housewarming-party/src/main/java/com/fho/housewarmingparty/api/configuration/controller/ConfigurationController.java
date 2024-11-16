package com.fho.housewarmingparty.api.configuration.controller;


import com.fho.housewarmingparty.api.configuration.dto.ConfigurationDTO;
import com.fho.housewarmingparty.api.configuration.mapper.ConfigurationMapper;
import com.fho.housewarmingparty.api.configuration.service.ConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Tag(name = "Configuration API", description = "API for managing configurations")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("configuration")
public class ConfigurationController {

    private final ConfigurationService service;
    private final ConfigurationMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new configuration", description = "Creates a new configuration with the provided data")
    public ResponseEntity<?> create(@Valid @RequestBody ConfigurationDTO dto) {
        log.info("Creating configuration. ID: {}.", dto.getId());
        service.create(dto);
        return ResponseEntity.ok("Configuration created successfully.");
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update an existing configuration", description = "Updates the details of an existing configuration by its ID")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ConfigurationDTO dto) {
        log.info("Updating configuration {}.", id);
        service.update(id, dto);
        return ResponseEntity.ok("Configuration updated successfully.");
    }

    @GetMapping("{id}")
    @Operation(summary = "Get configuration by ID", description = "Returns the details of a configuration by its ID")
    public ConfigurationDTO findById(@PathVariable Long id) {
        log.info("Getting configuration with id: {}.", id);
        return mapper.toDto(service.findById(id));
    }

    @GetMapping("user/{userId}")
    @Operation(summary = "Get configuration by user ID", description = "Returns the details of a configuration by its ID")
    public ConfigurationDTO findByUserId(@PathVariable Long userId) {
        log.info("Getting configuration with user id: {}.", userId);
        return mapper.toDto(service.findByUserId(userId));
    }
}
