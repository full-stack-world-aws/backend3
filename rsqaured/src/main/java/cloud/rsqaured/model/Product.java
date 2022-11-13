package cloud.rsqaured.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import cloud.rsqaured.enums.CountryEnum;
import cloud.rsqaured.persistence.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    private Integer id;
    private String name;
    private String info;
    private Timestamp date;
    private String imageLocation;
    private String fileLocation;
    private Integer referenceNumber;
    private CountryEnum country;

    public static List<Product> productsFrom(List<ProductEntity> productEntityList){
        return productEntityList.parallelStream().map(Product::productFrom).collect(Collectors.toList());
    }
    public static Product productFrom(ProductEntity productEntity){
        return Product.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .country(productEntity.getCountry())
                .info(productEntity.getInfo())
                .referenceNumber(productEntity.getReferenceNumber())
                .imageLocation(productEntity.getImageLocation())
                .fileLocation(productEntity.getFileLocation())
                .date(productEntity.getUpdatedTimestamp())
                .build();
    }
}
