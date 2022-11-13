package cloud.rsqaured.service;


import cloud.rsqaured.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> get();
    Product delete(Integer integer);
    Product createOrUpdateProduct(Product readValue, byte[] bytes, String fileName);
}
