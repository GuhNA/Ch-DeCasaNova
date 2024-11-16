package com.fho.housewarmingparty.api.configuration.service;

import com.fho.housewarmingparty.api.configuration.dto.ConfigurationDTO;
import com.fho.housewarmingparty.api.configuration.entity.Configuration;
import com.fho.housewarmingparty.api.configuration.mapper.ConfigurationMapper;
import com.fho.housewarmingparty.api.configuration.repository.ConfigurationRepository;
import com.fho.housewarmingparty.api.image.entity.Image;
import com.fho.housewarmingparty.api.image.entity.ImageType;
import com.fho.housewarmingparty.api.image.service.ImageService;
import com.fho.housewarmingparty.api.user.entity.User;
import com.fho.housewarmingparty.api.user.service.UserService;
import com.fho.housewarmingparty.exception.ResourceNotFoundException;
import com.fho.housewarmingparty.security.authentication.LoggedUser;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final MessageSource messageSource;
    private final ConfigurationRepository repository;
    private final ConfigurationMapper mapper;
    private final ImageService imageService;
    private final UserService userService;

    public void create(ConfigurationDTO dto) {
        Configuration configuration = mapper.toEntity(dto);
        configuration.setUser(userService.findById(LoggedUser.getLoggedInUserId()));

        if (dto.getBackgroundImage() != null) {
            Image image = imageService.create(dto.getBackgroundImage().getBase64(), ImageType.BACKGROUND);
            configuration.setBackgroundImage(image);
        }

        repository.save(configuration);
    }

    public void update(Long id, ConfigurationDTO dto) {
        findById(id);

        dto.setId(id);
        create(dto);
    }

    public Configuration findByUserId(Long userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource, Configuration.class, userId));
    }

    public Configuration findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource, Configuration.class, id));
    }

    public void createDefaultConfiguration(User user, String pixKey) {
        Configuration configuration = new Configuration();
        configuration.setUser(user);
        configuration.setPixKey(pixKey);

        repository.save(configuration);
    }
}
