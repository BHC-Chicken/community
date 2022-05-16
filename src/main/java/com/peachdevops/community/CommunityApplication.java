package com.peachdevops.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAsync
public class CommunityApplication extends SpringBootServletInitializer {

    @Override protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CommunityApplication.class); }

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}
