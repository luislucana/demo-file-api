package br.com.exemplo.demofileapi;

import br.com.exemplo.demofileapi.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

//@EnableAutoConfiguration
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@ComponentScan("br.com.exemplo.demofileapi.*")
@SpringBootApplication
public class UploadApplication extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(UploadApplication.class, args);
    }
}