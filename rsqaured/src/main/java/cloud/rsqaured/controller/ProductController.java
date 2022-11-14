package cloud.rsqaured.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cloud.rsqaured.model.Product;
import cloud.rsqaured.service.ProductService;
import cloud.rsqaured.service.AmazonS3StorageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/product")
public class ProductController {
    private final ProductService productService;
    private final AmazonS3StorageService amazonS3StorageService;

    @GetMapping
    public ResponseEntity<List<Product>> get() {
        return new ResponseEntity<>(productService.get(), HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> delete(
            @PathVariable Integer id
    ) {
        return new ResponseEntity<>(productService.delete(id), HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createOrUpdateProduct(MultipartHttpServletRequest request) {
        return new ResponseEntity<>(productService.createOrUpdateProduct(request), HttpStatus.CREATED);
    }
    @GetMapping(value = "/{filename}")
    public String getPresignedUrl(@PathVariable String filename) {
        return amazonS3StorageService.generatePresignedUrl(filename);
    }

}
