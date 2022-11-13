package cloud.rsqaured.service;

import static java.nio.file.Paths.get;
import static java.nio.file.StandardOpenOption.READ;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import utilities.os.OperatingSystemPathway;

@Service
public class FileSystemStorageService implements StorageService {

	private final String fileSystemRoot;

	@Value("${aws.s3bucketName}")
	private String s3bucketName;

	public FileSystemStorageService(@Value("${ranpak.fileUploads.fileSystemRoot}") String fileSystemRoot) {
		fileSystemRoot = OperatingSystemPathway.macOsFilePathSinceRootUnwritable(fileSystemRoot);
		this.fileSystemRoot = fileSystemRoot;
	}

	@Override
	public void store(byte[] data, String fileName) throws IOException {

		AwsBasicCredentials awsCreds = AwsBasicCredentials.create("AKIAW4TLEAE3BUYNZKN3",
				"YyIgVphgaiazSQRgDYDr3DwtuvhSi8hR64autxuX");
		Region region = Region.US_EAST_1;
		S3Client s3 = S3Client.builder().region(region).credentialsProvider(StaticCredentialsProvider.create(awsCreds))
				.build();
		PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(s3bucketName).key(fileName).build();
		s3.putObject(objectRequest, RequestBody.fromBytes(data));
	}

	@Override
	public void delete(String bucketName, String key) throws IOException {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials("AKIAW4TLEAE3BUYNZKN3", "YyIgVphgaiazSQRgDYDr3DwtuvhSi8hR64autxuX")))
				.build();
		s3.deleteObject(bucketName, key);

	}

	@Override
	public byte[] readBytes(String from) throws IOException {
		return Files.readAllBytes(get(fileSystemRoot, from));
	}

	@Override
	public InputStream getInputStream(String from) throws IOException {
		return Files.newInputStream(get(fileSystemRoot, from), READ);
	}

	@Override
	public File getFile(String from) throws IOException {
		return new File(get(fileSystemRoot, from).toString());
	}

	@Override
	public String generatePresignedUrl(String key) {
		// Set the expiry time
		java.util.Date expiration = new java.util.Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 60;
		expiration.setTime(expTimeMillis);
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials("AKIAW4TLEAE3BUYNZKN3", "YyIgVphgaiazSQRgDYDr3DwtuvhSi8hR64autxuX")))
				.build();
		
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(s3bucketName, key)
				.withMethod(HttpMethod.GET).withExpiration(expiration);
		URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
		System.out.println("Pre-Signed URL: " + url.toString());
		return url.toString();
	}

	@Override
	public byte[] readS3ObjectAsBytes(String key) throws IOException {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials("AKIAW4TLEAE3BUYNZKN3", "YyIgVphgaiazSQRgDYDr3DwtuvhSi8hR64autxuX")))
				.build();
		
		S3Object object = s3.getObject(s3bucketName, key); 
		byte[] byteArray = IOUtils.toByteArray(object.getObjectContent());
		// TODO Auto-generated method stub
		return byteArray;
	}
}
