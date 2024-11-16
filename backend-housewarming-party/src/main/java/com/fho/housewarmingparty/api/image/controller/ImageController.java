package com.fho.housewarmingparty.api.image.controller;


import java.util.List;

import com.fho.housewarmingparty.api.image.dto.ImageCriteria;
import com.fho.housewarmingparty.api.image.dto.ImageDTO;
import com.fho.housewarmingparty.api.image.mapper.ImageMapper;
import com.fho.housewarmingparty.api.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Image API", description = "API for managing images")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("image")
public class ImageController {

    private final ImageService service;
    private final ImageMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload a new image", description = "Uploads a new image and returns its details")
    public ImageDTO create(@Valid @RequestBody ImageDTO dto) {
        log.info("Uploading new image.");
        return mapper.toDto(service.create(mapper.toEntity(dto)));
    }

    @GetMapping("{id}")
    @Operation(summary = "Get image by ID", description = "Returns the details of an image by its ID")
    public ImageDTO findById(@PathVariable Long id) {
        log.info("Fetching image with ID: {}.", id);
        return mapper.toDto(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "List all images", description = "Returns a list of all images")
    public List<ImageDTO> findAll(ImageCriteria criteria) {
        log.info("Fetching all images. Type: {}.", criteria.getType());
        return mapper.toDto(service.findAll(criteria));
    }
}
