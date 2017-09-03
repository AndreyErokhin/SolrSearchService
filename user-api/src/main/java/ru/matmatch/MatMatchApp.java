package ru.matmatch;

import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import ru.matmatch.dataloader.CsvDataLoader;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories
@EnableSolrRepositories
@EnableSwagger2
public class MatMatchApp {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(MatMatchApp.class);
    }

    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("users")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/.*"))
                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Full text search example app using SOLR")
                .description("The API allows you to execute CRUD operations on users and full text search on first name, last name and ip address")
                .termsOfServiceUrl("www.example.com/terms-of-service")
                .contact("Andrey Erokhin")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/LICENSE")
                .version("2.0")
                .build();
    }
}
