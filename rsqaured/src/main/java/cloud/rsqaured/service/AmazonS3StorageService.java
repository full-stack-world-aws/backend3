package cloud.rsqaured.service;

import java.io.IOException;

public interface AmazonS3StorageService {

    void store(byte[] data, String fileName) throws IOException;

    void delete(String key) throws IOException;
    
    String generatePresignedUrl(String key);
    

}
