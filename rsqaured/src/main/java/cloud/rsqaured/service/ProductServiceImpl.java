package cloud.rsqaured.service;

import cloud.rsqaured.component.AuthenticatedUserResolver;
import cloud.rsqaured.exception.GeneralMessageException;
import cloud.rsqaured.exception.GeneralMessageWithIdException;
import cloud.rsqaured.model.Product;
import cloud.rsqaured.persistence.entity.ProductEntity;
import cloud.rsqaured.persistence.entity.UserEntity;
import cloud.rsqaured.persistence.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final AuthenticatedUserResolver authenticatedUserResolver;
    private final StorageService storageService;
    private final String productImageLocation;
    private final String productFileLocation;
    static final String FILE = "file";
    static final String ANY_FILE = "anyFile";

    static final String METADATA_PARAMETER_NAME = "meta-data";

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              AuthenticatedUserResolver authenticatedUserResolver,
                              StorageService storageService,
                              @Value("${ranpak.fileUploads.productImagesLocation}") String productImageLocation,
                              @Value("${ranpak.fileUploads.productFilesLocation}") String productFileLocation
    ) {
        this.productRepository = productRepository;
        this.authenticatedUserResolver = authenticatedUserResolver;
        this.storageService = storageService;
        this.productImageLocation = productImageLocation;
        this.productFileLocation = productFileLocation;
    }

    @Override
    public List<Product> get() {
        UserEntity userEntity = authenticatedUserResolver.user(); // runs through security context
        List<ProductEntity> productEntityList = productRepository.findByUserEntity(userEntity);
        return Product.productsFrom(productEntityList);
    }

    @SneakyThrows
    @Override
    public Product delete(Integer integer) {
        UserEntity userEntity = authenticatedUserResolver.user(); // runs through security context
        ProductEntity productEntity = productRepository.findById(integer).orElse(null);
        if (Objects.isNull(productEntity))
            throw new GeneralMessageWithIdException("product does not exist with the following id", integer);
        if (productEntity.getUserEntity().getId() != userEntity.getId())
            throw new GeneralMessageException("You are not the user that created this product");
        if (Objects.nonNull(productEntity.getImageLocation())) storageService.delete(productEntity.getImageLocation());
        if (Objects.nonNull(productEntity.getFileLocation())) storageService.delete(productEntity.getFileLocation());
        productRepository.delete(productEntity);
        return Product.builder().id(0).build();
    }

    @SneakyThrows
    @Transactional
    @Override
    public Product createOrUpdateProduct(MultipartHttpServletRequest request) {

        String jsonMetadata = request.getParameter(METADATA_PARAMETER_NAME);
        final ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(jsonMetadata, Product.class);

        ProductEntity productEntity = createOrUpdateProductEntityFrom(product);

        MultipartFile imageMultipartFile = request.getFile(FILE); // image
        if (Objects.nonNull(imageMultipartFile)) {
            String finalFileName = storeInS3BucketAndReturnFileName(imageMultipartFile);
            productEntity.setImageLocation(finalFileName);
        }

        MultipartFile anyMultipartFile = request.getFile(ANY_FILE); // any file
        if (Objects.nonNull(anyMultipartFile)) {
            String finalFileName = storeInS3BucketAndReturnFileName(anyMultipartFile);
            productEntity.setFileLocation(finalFileName);
        }

        ProductEntity productEntityAfterSave = productRepository.save(productEntity);
        return Product.productFrom(productEntityAfterSave);
    }

    private ProductEntity createOrUpdateProductEntityFrom(Product product) {
        UserEntity userEntity = authenticatedUserResolver.user(); // runs through security context
        ProductEntity productEntity = null;

        if (Objects.nonNull(product.getId())) productEntity = getProductEntityBy(product);
        else productEntity = new ProductEntity();

        if (Objects.nonNull(product.getCountry())) productEntity.setCountry(product.getCountry());
        if (Objects.nonNull(product.getInfo())) productEntity.setInfo(product.getInfo());
        if (Objects.nonNull(product.getName())) productEntity.setName(product.getName());
        if (Objects.nonNull(product.getReferenceNumber())) productEntity.setReferenceNumber(product.getReferenceNumber());

        productEntity.setUserEntity(userEntity);
        return productEntity;
    }

    private ProductEntity getProductEntityBy(Product product) {
        UserEntity userEntity = authenticatedUserResolver.user(); // runs through security context
        ProductEntity productEntity = productRepository.findById(product.getId()).orElse(null);
        if (Objects.isNull(productEntity))
            throw new GeneralMessageWithIdException("product does not exist with the following id", product.getId());
        if (!productEntity.getUserEntity().equals(userEntity))
            throw new GeneralMessageException("You are not the user that created this product");
        return productEntity;
    }

    @SneakyThrows
    private String storeInS3BucketAndReturnFileName(MultipartFile multipartFile) {
        if (multipartFile.getBytes().length < 0) throw new GeneralMessageException("Something is wrong with a file you uploaded");
        String finalFileName = RandomStringUtils.random(8, true, false) + multipartFile.getOriginalFilename();
        storageService.store(multipartFile.getBytes(), finalFileName);
        return finalFileName;
    }
}
