package cloud.rsqaured.service;

import cloud.rsqaured.component.AuthenticatedUserResolver;
import cloud.rsqaured.exception.GeneralMessageException;
import cloud.rsqaured.exception.GeneralMessageWithIdException;
import cloud.rsqaured.model.Product;
import cloud.rsqaured.persistence.entity.ProductEntity;
import cloud.rsqaured.persistence.entity.UserEntity;
import cloud.rsqaured.persistence.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final AuthenticatedUserResolver authenticatedUserResolver;
    private final AmazonS3StorageService amazonS3StorageService;
    static final String FILE = "file";
    static final String ANY_FILE = "anyFile";
    static final String METADATA_PARAMETER_NAME = "meta-data";

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
        if (Objects.nonNull(productEntity.getImageLocation())) amazonS3StorageService.delete(productEntity.getImageLocation());
        if (Objects.nonNull(productEntity.getFileLocation())) amazonS3StorageService.delete(productEntity.getFileLocation());
        productRepository.delete(productEntity);
        return Product.builder().id(0).build();
    }

//    @Transactional
    @SneakyThrows
    @Override
    public Product createOrUpdateProduct(MultipartHttpServletRequest request) {

        String jsonMetadata = request.getParameter(METADATA_PARAMETER_NAME);
        final ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(jsonMetadata, Product.class);

        ProductEntity productEntity = createOrUpdateProductEntityFrom(product);

        MultipartFile imageMultipartFile = request.getFile(FILE); // image
        MultipartFile anyMultipartFile = request.getFile(ANY_FILE); // any file

        if (Objects.isNull(productEntity.getId())) {

            if (Objects.nonNull(imageMultipartFile)) productEntity.setImageLocation(createFileName(imageMultipartFile));
            if (Objects.nonNull(anyMultipartFile)) productEntity.setFileLocation(createFileName(anyMultipartFile));

            ProductEntity productEntityAfterSave = productRepository.save(productEntity);

            if (Objects.nonNull(imageMultipartFile)) storeInS3Bucket(imageMultipartFile, productEntityAfterSave.getImageLocation());
            if (Objects.nonNull(anyMultipartFile)) storeInS3Bucket(anyMultipartFile, productEntityAfterSave.getFileLocation());

            return Product.productFrom(productEntityAfterSave);
        }else{
            if (Objects.nonNull(imageMultipartFile) && Objects.nonNull(imageMultipartFile)) productEntity.setImageLocation(createFileName(imageMultipartFile));
            if (Objects.nonNull(anyMultipartFile) && Objects.nonNull(anyMultipartFile)) productEntity.setFileLocation(createFileName(anyMultipartFile));

            ProductEntity productEntityAfterSave = productRepository.save(productEntity);

            if (Objects.nonNull(imageMultipartFile) && Objects.nonNull(productEntityAfterSave.getImageLocation())) storeInS3Bucket(imageMultipartFile, productEntityAfterSave.getImageLocation());
            if (Objects.nonNull(anyMultipartFile) && Objects.nonNull(productEntityAfterSave.getFileLocation())) storeInS3Bucket(anyMultipartFile, productEntityAfterSave.getFileLocation());

            return Product.productFrom(productEntityAfterSave);
        }
    }

    private ProductEntity createOrUpdateProductEntityFrom(Product product) {
        UserEntity userEntity = authenticatedUserResolver.user(); // runs through security context
        ProductEntity productEntity = null;

        if (Objects.nonNull(product.getId())) productEntity = getProductEntityBy(product);
        else productEntity = new ProductEntity();

        if (Objects.nonNull(product.getCountry())) productEntity.setCountry(product.getCountry());
        if (Objects.nonNull(product.getInfo())) productEntity.setInfo(product.getInfo());
        if (Objects.nonNull(product.getName())) productEntity.setName(product.getName());
        if (Objects.nonNull(product.getReferenceNumber()))
            productEntity.setReferenceNumber(product.getReferenceNumber());

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
    private void storeInS3Bucket(MultipartFile multipartFile, String finalFileName) {
        if (multipartFile.getBytes().length < 0) throw new GeneralMessageException("Something is wrong with a file you uploaded");
        amazonS3StorageService.store(multipartFile.getBytes(), finalFileName);
    }

    private String createFileName(MultipartFile multipartFile) {
        return RandomStringUtils.random(8, true, false) + multipartFile.getOriginalFilename();
    }
}
