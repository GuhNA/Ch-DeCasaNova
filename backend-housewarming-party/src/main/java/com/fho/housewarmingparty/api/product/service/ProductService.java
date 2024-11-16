package com.fho.housewarmingparty.api.product.service;

import java.math.BigDecimal;
import java.util.List;

import com.fho.housewarmingparty.api.configuration.entity.Configuration;
import com.fho.housewarmingparty.api.configuration.service.ConfigurationService;
import com.fho.housewarmingparty.api.image.entity.Image;
import com.fho.housewarmingparty.api.image.entity.ImageType;
import com.fho.housewarmingparty.api.image.service.ImageService;
import com.fho.housewarmingparty.api.product.dto.ProductCriteria;
import com.fho.housewarmingparty.api.product.dto.ProductDTO;
import com.fho.housewarmingparty.api.product.entity.Product;
import com.fho.housewarmingparty.api.product.entity.ProductStatus;
import com.fho.housewarmingparty.api.product.mapper.ProductMapper;
import com.fho.housewarmingparty.api.product.repository.ProductRepository;
import com.fho.housewarmingparty.api.product.repository.ProductSpecification;
import com.fho.housewarmingparty.api.user.entity.User;
import com.fho.housewarmingparty.api.user.service.UserService;
import com.fho.housewarmingparty.exception.ResourceNotFoundException;
import com.fho.housewarmingparty.security.authentication.LoggedUser;
import com.fho.housewarmingparty.utils.pix.QRCodeService;
import jakarta.transaction.Transactional;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final MessageSource messageSource;
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ImageService imageService;
    private final UserService userService;
    private final ConfigurationService configurationService;
    private final QRCodeService qrCodeService;

    @Transactional
    public Product create(ProductDTO dto) {
        Product product = mapper.toEntity(dto);
        product.setStatus(ProductStatus.AVAILABLE);

        User user = userService.findById(LoggedUser.getLoggedInUserId());
        product.setUser(user);

        if (dto.getBase64Image() != null && dto.getImage() == null) {
            Image image = imageService.create(dto.getBase64Image(), ImageType.PRODUCT);
            product.setImage(image);
        } else if (dto.getImage() != null) {
            product.setImage(imageService.findById(dto.getImage().getId()));
        }

        Configuration userConfig = configurationService.findByUserId(LoggedUser.getLoggedInUserId());
        if (userConfig == null || userConfig.getPixKey() == null) {
            throw new IllegalArgumentException("User Pix configuration is missing");
        }

        String qrCodeBase64 = qrCodeService.generateQRCode(
                truncate(product.getName()),
                "Araras",
                dto.getPrice(),
                userConfig.getPixKey(),
                truncate(product.getName()).replaceAll("\\s+", "")
        );

        product.setQrCodeImage(qrCodeBase64);

        return repository.save(product);
    }

    public Product update(Long id, ProductDTO dto) {
        findById(id);

        dto.setId(id);
        return create(dto);
    }

    public Product findById(Long id) {
        log.info("Finding product with ID: {}.", id);
        return repository.findByIdAndUserId(id, LoggedUser.getLoggedInUserId())
                .orElseThrow(() -> new ResourceNotFoundException(messageSource, Product.class, id));
    }

    public List<Product> findAll(ProductCriteria criteria) {
        ProductSpecification specification = new ProductSpecification(criteria);
        List<Product> products = repository.findAll(specification);

        log.info("Fetched {} Products.", products.size());
        return products;
    }

    public List<Product> findAvailable(ProductStatus status) {
        log.info("Fetching products with status: {}.", status);
        return repository.findByStatusAndUserId(status, LoggedUser.getLoggedInUserId());
    }

    public void donate(Long id) {
        Product product = findById(id);
        product.setStatus(ProductStatus.UNAVAILABLE);
        repository.save(product);
    }

    public BigDecimal countTotalAmount(ProductCriteria criteria) {
        ProductSpecification specification = new ProductSpecification(criteria);
        return repository.countTotalAmount(specification);
    }

    private String truncate(String str) {
        return str.length() > 20
                ? str.substring(0, 20).trim()
                : str;
    }
}
