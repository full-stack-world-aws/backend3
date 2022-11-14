package cloud.rsqaured.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

    @Value("${ranpak.fileUploads.fileSystemRoot}")
    private String fileSystemRoot;

    @Value("${ranpak.fileUploads.productImagesLocation}")
    private String productImagesLocation;

    @Value("${ranpak.fileUploads.productFilesLocation}")
    private String productFilesLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

//        fileSystemRoot = OperatingSystemPathway.macOsFilePathSinceRootUnwritable(fileSystemRoot);

        registry.addResourceHandler("/public/products/images/**")
                .addResourceLocations("file:" + fileSystemRoot + "/" + productImagesLocation);
        registry.addResourceHandler("/public/products/files/**")
                .addResourceLocations("file:" + fileSystemRoot + "/" + productFilesLocation);
    }


}
