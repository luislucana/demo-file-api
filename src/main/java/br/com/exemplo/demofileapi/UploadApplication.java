package br.com.exemplo.demofileapi;

import br.com.exemplo.demofileapi.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * https://www.serasaexperian.com.br/landingpage/validador-pefin
 * https://tdn.totvs.com/pages/releaseview.action?pageId=268593094
 *
 * UTF-8 é um esquema largamente utilizado ao passo que ANSI é praticamente obsoleto.
 * ANSI usa um único byte ao passo que UTF-8 usa uma codificação multibyte.
 * UTF-8 pode representar uma gama de caracteres muito maior que o bastante limitado ANSI.
 * Pontos de código UTF-8 são uniformemente padronizados enquanto que o ANSI possui muitas diferentes versões.
 */
//@EnableAutoConfiguration
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@ComponentScan("br.com.exemplo.demofileapi.*")
@EntityScan("br.com.exemplo.demofileapi.persistence.model")
@SpringBootApplication
public class UploadApplication extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(UploadApplication.class, args);
    }
}