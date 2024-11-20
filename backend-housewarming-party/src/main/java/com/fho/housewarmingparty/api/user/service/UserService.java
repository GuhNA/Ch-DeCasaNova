package com.fho.housewarmingparty.api.user.service;

import static java.lang.String.format;

import com.fho.housewarmingparty.api.configuration.service.ConfigurationService;
import com.fho.housewarmingparty.api.image.service.ImageService;
import com.fho.housewarmingparty.api.product.service.ProductService;
import com.fho.housewarmingparty.api.user.dto.UserDTO;
import com.fho.housewarmingparty.api.user.entity.User;
import com.fho.housewarmingparty.api.user.mapper.UserMapper;
import com.fho.housewarmingparty.api.user.repository.UserRepository;
import com.fho.housewarmingparty.exception.ConflictException;
import com.fho.housewarmingparty.exception.ErrorCode;
import com.fho.housewarmingparty.exception.ResourceNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    private final MessageSource messageSource;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ConfigurationService configurationService;
    private final ProductService productService;
    private final ImageService imageService;
    private final UserMapper mapper;

    public UserService(MessageSource messageSource, UserRepository repository, PasswordEncoder passwordEncoder, @Lazy ConfigurationService configurationService, @Lazy ProductService productService, ImageService imageService, UserMapper mapper) {
        this.messageSource = messageSource;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.configurationService = configurationService;
        this.productService = productService;
        this.imageService = imageService;
        this.mapper = mapper;
    }

    @Transactional
    public void create(UserDTO dto) {
        User entity = mapper.toEntity(dto);
        validateUniqueness(entity, null);

        String hashedPassword = passwordEncoder.encode(entity.getPassword());
        entity.setPassword(hashedPassword);

        User user = repository.save(entity);

        imageService.createDefaultImages();
        configurationService.createDefaultConfiguration(user, dto.getPixKey());
        productService.createDefaultProducts(user);

        log.info("Successfully created user '{}'.", user.getId());
    }

    public void update(Long id, User entity) {
        findById(id);

        validateUniqueness(entity, id);

        entity.setId(id);
        repository.save(entity);
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource, User.class, id));
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
        log.info("User '{}' was successfully deleted.", id);
    }

    private void validateUniqueness(User entity, Long id) {
        repository.findByEmail(entity.getEmail())
                .ifPresent(animal -> {
                    if (!animal.getId().equals(id)) {
                        throw new ConflictException(ErrorCode.DUPLICATED_ANIMAL_ID,
                                format("An user with the same e-mail already exists: '%s'.", entity.getEmail()));
                    }
                });
    }
}
