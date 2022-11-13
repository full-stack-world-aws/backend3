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

import javax.annotation.PostConstruct;

@Service
public class FileSystemStorageService implements StorageService {

	@Value("${aws.s3bucketName}")
	private String s3bucketName;
	@Value("${aws.accessKeyId}")
	private String accessKeyId;
	@Value("${aws.secretAccessKey}")
	private String secretAccessKey;
	private AmazonS3 s3;
	@Override
	public void store(byte[] data, String fileName) throws IOException {

		AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId,
				secretAccessKey);
		Region region = Region.US_EAST_1;
		S3Client s3 = S3Client.builder().region(region).credentialsProvider(StaticCredentialsProvider.create(awsCreds))
				.build();
		PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(s3bucketName).key(fileName).build();
		s3.putObject(objectRequest, RequestBody.fromBytes(data));
	}

	@Override
	public void delete(String key) throws IOException {
		s3.deleteObject(s3bucketName, key);
	}
	@Override
	public String generatePresignedUrl(String key) {
		// Set the expiry time
		java.util.Date expiration = new java.util.Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 60;
		expiration.setTime(expTimeMillis);

		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(s3bucketName, key)
				.withMethod(HttpMethod.GET).withExpiration(expiration);
		URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
		System.out.println("Pre-Signed URL: " + url.toString());
		return url.toString();
	}

	@Override
	public byte[] readS3ObjectAsBytes(String key) throws IOException {
		S3Object object = s3.getObject(s3bucketName, key); 
		byte[] byteArray = IOUtils.toByteArray(object.getObjectContent());
		return byteArray;
	}


	@PostConstruct
	private void initAPI() {
		s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials(accessKeyId,secretAccessKey)))
				.build();
	}
}
