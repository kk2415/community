package com.levelup.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.levelup.core")
@EnableJpaRepositories("com.levelup.core")
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Configuration
public class JpaConfig {
}
