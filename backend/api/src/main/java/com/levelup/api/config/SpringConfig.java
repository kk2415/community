package com.levelup.api.config;

import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.levelup.api.util.file.FileStore;
import com.levelup.api.util.file.S3FileStore;
import com.levelup.api.util.jwt.TokenProvider;
import com.levelup.core.domain.base.Auditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringConfig implements WebMvcConfigurer {

    @Bean
    public FileStore fileStore() {
        return new S3FileStore(new AmazonS3Client());
    }

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider();
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new Auditor();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/Task/study/levelup/images/");
    }
}
