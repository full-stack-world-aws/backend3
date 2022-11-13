package cloud.rsqaured.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface StorageService {

    void store(byte[] data, String fileName) throws IOException;

    void delete(String key) throws IOException;
    
    String generatePresignedUrl(String key);
    

}
