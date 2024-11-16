package com.fho.housewarmingparty.api.image.service;

import java.util.List;

import com.fho.housewarmingparty.api.image.dto.ImageCriteria;
import com.fho.housewarmingparty.api.image.entity.Image;
import com.fho.housewarmingparty.api.image.entity.ImageType;
import com.fho.housewarmingparty.api.image.repository.ImageRepository;
import com.fho.housewarmingparty.api.image.repository.ImageSpecification;
import com.fho.housewarmingparty.exception.ResourceNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final MessageSource messageSource;
    private final ImageRepository repository;

    public Image create(String base64, ImageType type) {
        Image image = new Image();
        image.setBase64(base64);
        image.setType(type);
        return repository.save(image);
    }

    public Image create(Image image) {
        log.info("Creating image.");
        return repository.save(image);
    }

    public Image findById(Long id) {
        log.info("Finding image with ID: {}.", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource, Image.class, id));
    }

    public List<Image> findAll(ImageCriteria criteria) {
        ImageSpecification specification = new ImageSpecification(criteria);
        List<Image> images = repository.findAll(specification);

        log.info("Fetched {} Images.", images.size());
        return images;
    }
}
