package com.example.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SearchApplication {
    private static final Logger logger = LoggerFactory.getLogger(SearchApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
        logger.info("Search Server has being boot up.");
    }
}
