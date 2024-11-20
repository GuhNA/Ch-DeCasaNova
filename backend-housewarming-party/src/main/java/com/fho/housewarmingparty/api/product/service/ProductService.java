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

        if (dto.getUser() == null) {
            User user = userService.findById(LoggedUser.getLoggedInUserId());
            product.setUser(user);
        }

        if (dto.getBase64Image() != null && dto.getImage() == null) {
            Image image = imageService.create(dto.getBase64Image(), ImageType.PRODUCT);
            product.setImage(image);
        } else if (dto.getImage() != null) {
            product.setImage(imageService.findById(dto.getImage().getId()));
        }

        Long userId = dto.getUser() != null ? dto.getUser().getId() : LoggedUser.getLoggedInUserId();
        Configuration userConfig = configurationService.findByUserId(userId);
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

    public List<Product> createBatch(List<ProductDTO> dtos) {
        return dtos.stream().map(this::create).toList();
    }

    public Product update(Long id, ProductDTO dto) {
        findById(id);

        dto.setId(id);
        return create(dto);
    }

    public Product findById(Long id) {
        log.info("Finding product with ID: {}.", id);
        return repository.findById(id)
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

    public void createDefaultProducts(User user) {
        List<ProductDTO> products = List.of(
                createProduct("Fogão Elétrico", BigDecimal.valueOf(480.40), user,
                        "O fogão elétrico é uma alternativa moderna aos fogões a gás, oferecendo aquecimento uniforme e controle preciso de temperatura. Ideal para cozinhas contemporâneas, ele elimina a necessidade de botijões de gás e reduz o risco de vazamentos.",
                        1L),
                createProduct("Máquina de Café", BigDecimal.valueOf(195.44), user,
                        "A máquina de café permite preparar diversas bebidas à base de café de forma rápida e prática. Com diferentes configurações, é possível ajustar a intensidade e o volume, atendendo aos mais variados paladares.",
                        2L),
                createProduct("Ventilador", BigDecimal.valueOf(150.00), user,
                        "O ventilador é um aparelho essencial para manter o ambiente fresco e arejado. Disponível em modelos de mesa, coluna ou teto, ele proporciona alívio imediato em dias quentes, circulando o ar de maneira eficiente.",
                        3L),
                createProduct("Liquidificador", BigDecimal.valueOf(100.00), user,
                        "O liquidificador é um utensílio indispensável na cozinha, utilizado para preparar sucos, vitaminas, sopas e diversas receitas. Com diferentes velocidades e lâminas afiadas, ele tritura e mistura ingredientes com facilidade.",
                        4L),
                createProduct("Batedeira", BigDecimal.valueOf(120.00), user,
                        "A batedeira facilita o preparo de massas, bolos e outras receitas que exigem mistura homogênea. Com opções de velocidade e diferentes batedores, ela garante consistência e leveza nas preparações culinárias.",
                        5L),
                createProduct("Máquina de Lavar Roupas", BigDecimal.valueOf(1500.00), user,
                        "A máquina de lavar roupas automatiza o processo de lavagem, economizando tempo e esforço. Com programas específicos para diferentes tipos de tecidos e níveis de sujeira, ela assegura roupas limpas e bem cuidadas.",
                        6L),
                createProduct("Micro-ondas", BigDecimal.valueOf(400.00), user,
                        "O micro-ondas é um eletrodoméstico versátil que aquece, descongela e até cozinha alimentos em questão de minutos. Essencial para quem busca praticidade no dia a dia, ele oferece diversas funções e ajustes de potência.",
                        7L),
                createProduct("Panela Elétrica de Arroz", BigDecimal.valueOf(150.00), user,
                        "A panela elétrica de arroz cozinha o arroz de forma uniforme e no ponto certo, sem a necessidade de supervisão constante. Alguns modelos também permitem preparar legumes no vapor, ampliando sua utilidade na cozinha.",
                        8L),
                createProduct("Ferro de Passar", BigDecimal.valueOf(80.00), user,
                        "O ferro de passar é fundamental para manter as roupas sem amassados e com boa aparência. Com opções a vapor ou seco, ele facilita o processo de passar roupas, garantindo resultados profissionais em casa.",
                        9L),
                createProduct("Aspirador de Pó", BigDecimal.valueOf(250.00), user,
                        "O aspirador de pó é um aliado na limpeza doméstica, removendo poeira e detritos de pisos, carpetes e estofados. Com diferentes acessórios e potências, ele torna a higienização mais eficiente e menos trabalhosa.",
                        10L)
        );
        createBatch(products);
    }

    private ProductDTO createProduct(String name, BigDecimal price, User user, String description, Long imageId) {
        return ProductDTO.builder()
                .name(name)
                .price(price)
                .user(user)
                .description(description)
                .image(new Image(imageId))
                .build();
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
