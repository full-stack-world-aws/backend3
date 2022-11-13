package cloud.rsqaured.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface StorageService {

    void store(byte[] data, String fileName) throws IOException;

    void delete(String bucketName, String key) throws IOException;

    byte[] readBytes(String from) throws IOException;

    InputStream getInputStream(String from) throws IOException;

    File getFile(String from) throws IOException;
    
    String generatePresignedUrl(String key);
    
    byte[] readS3ObjectAsBytes(String key) throws IOException;

}
