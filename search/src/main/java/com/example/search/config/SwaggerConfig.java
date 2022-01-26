package com.example.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docker(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("Huajian")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.search.controller"))
                .build();
    }

    private ApiInfo apiInfo(){
        Contact contact = new Contact("Huajian Huang", "https://github.com/KennyPacky", "huajian.kenny@gmail.com");
        return new ApiInfo(
                "Search Server Api Documentation",
                "Return the details of cities weather.",
                "1.0",
                "urn:tos",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
