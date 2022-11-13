package cloud.rsqaured.controller;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import cloud.rsqaured.exception.GeneralMessageException;
import cloud.rsqaured.model.Product;
import cloud.rsqaured.service.ProductService;
import cloud.rsqaured.service.StorageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/product")
public class ProductController {
    static final String FILE = "file";

    static final String METADATA_PARAMETER_NAME = "meta-data";

    private final ObjectMapper objectMapper;
    private final ProductService productService;
    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<List<Product>> get(){
        return new ResponseEntity<>(productService.get(), HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> delete(
            @PathVariable Integer id
    ){
        return new ResponseEntity<>(productService.delete(id), HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createOrUpdateDSPRProduct(MultipartHttpServletRequest request){
    	
    	
        String jsonMetadata = request.getParameter(METADATA_PARAMETER_NAME);
        MultipartFile multipartFile = request.getFile(FILE);

        System.out.println(jsonMetadata);
        
        if(nonNull(jsonMetadata) && Objects.isNull(multipartFile)) {
            try {
                return new ResponseEntity<>(
                        productService.createOrUpdateProduct(
                                objectMapper.readValue(jsonMetadata, Product.class),
                                null,
                                null
                        ),
                        HttpStatus.CREATED
                );
            } catch (IOException e) {
                throw new GeneralMessageException(e.getMessage());
            }
        }else if (nonNull(jsonMetadata) && nonNull(multipartFile)) {
            try {
            	System.out.println(multipartFile.getOriginalFilename());
                return new ResponseEntity<>(
                        productService.createOrUpdateProduct(
                                objectMapper.readValue(jsonMetadata, Product.class),
                                multipartFile.getBytes(),
                                multipartFile.getOriginalFilename()
                        ),
                        HttpStatus.CREATED
                );
            } catch (IOException e) {
                throw new GeneralMessageException(e.getMessage());
            }
        } else {
            throw new GeneralMessageException("Something went wrong with product or image upload");
        }
    }
    
    @GetMapping(value = "/{filename}")
    public String getPresignedUrl(@PathVariable String filename){
        return storageService.generatePresignedUrl(filename);
    }
    
    @GetMapping(value = "/getbyte/{filename}")
    public byte[] getS3ByteArray(@PathVariable String filename) throws IOException{
        return storageService.readS3ObjectAsBytes(filename);
    }

}
