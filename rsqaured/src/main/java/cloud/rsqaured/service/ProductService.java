package cloud.rsqaured.service;


import cloud.rsqaured.model.Product;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface ProductService {
    List<Product> get();
    Product delete(Integer integer);
    Product createOrUpdateProduct(MultipartHttpServletRequest request);

}
