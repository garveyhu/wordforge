package com.sunyard.wordforge.complex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * swagger2配置类
 *
 * @author Archer
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(
                new ApiInfoBuilder()
                    .title("wordforge")
                    .description("word熔炉")
                    .termsOfServiceUrl("https://www.sunyard.com")
                    .contact(new Contact("Archer", "https://ip:port/doc.html", "h96599021@gmail.com"))
                    .version("1.0")
                    .build()
            )
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.sunyard.wordforge"))
            .paths(PathSelectors.any())
            .build();
    }
}
