package cloud.rsqaured.service;

import cloud.rsqaured.component.AuthenticatedUserResolver;
import cloud.rsqaured.exception.GeneralMessageException;
import cloud.rsqaured.exception.GeneralMessageWithIdException;
import cloud.rsqaured.model.Product;
import cloud.rsqaured.persistence.entity.ProductEntity;
import cloud.rsqaured.persistence.entity.UserEntity;
import cloud.rsqaured.persistence.repository.ProductRepository;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final AuthenticatedUserResolver authenticatedUserResolver;
    private final StorageService storageService;
    private final String productImageLocation;
    private final String productFileLocation;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              AuthenticatedUserResolver authenticatedUserResolver,
                              StorageService storageService,
                              @Value("${ranpak.fileUploads.productImagesLocation}")String productImageLocation,
                              @Value("${ranpak.fileUploads.productFilesLocation}")String productFileLocation
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
    public Product delete(Integer integer){
        UserEntity userEntity = authenticatedUserResolver.user(); // runs through security context
        ProductEntity productEntity = productRepository.findById(integer).orElse(null);
        if(Objects.isNull(productEntity)) throw new GeneralMessageWithIdException("product does not exist with the following id", integer);
        if(productEntity.getUserEntity().getId() != userEntity.getId()) throw new GeneralMessageException("You are not the user that created this product");
        if(Objects.nonNull(productEntity.getImageLocation())) storageService.delete(productEntity.getImageLocation());
        productRepository.delete(productEntity);
        return Product.builder().id(0).build();
    }

    @Override
    public Product createOrUpdateProduct(Product product, byte[] imageData, String fileName) {
        UserEntity userEntity = authenticatedUserResolver.user(); // runs through security context
        ProductEntity productEntity = null;

        if(Objects.nonNull(product.getId()))
            productEntity = getProductEntityBy(product);
        else
            productEntity = new ProductEntity();

        if(Objects.nonNull(product.getCountry())) productEntity.setCountry(product.getCountry());
        if(Objects.nonNull(product.getInfo())) productEntity.setInfo(product.getInfo());
        if(Objects.nonNull(product.getName())) productEntity.setName(product.getName());
        if(Objects.nonNull(product.getReferenceNumber())) productEntity.setReferenceNumber(product.getReferenceNumber());

        productEntity.setUserEntity(userEntity);

        if (nonNull(imageData) && imageData.length > 0) {
            try {
                String finalFileName = RandomStringUtils.random(8,true,false) + fileName;
                storageService.store(imageData, finalFileName);
                productEntity.setImageLocation(finalFileName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ProductEntity productEntity1 = productRepository.save(productEntity);
        return Product.productFrom(productEntity1);
    }

    private ProductEntity getProductEntityBy(Product product){
        UserEntity userEntity = authenticatedUserResolver.user(); // runs through security context
        ProductEntity productEntity = productRepository.findById(product.getId()).orElse(null);
        if(Objects.isNull(productEntity)) throw new GeneralMessageWithIdException("product does not exist with the following id", product.getId());
        if(!productEntity.getUserEntity().equals(userEntity)) throw new GeneralMessageException("You are not the user that created this product");
        return productEntity;
    }
}
