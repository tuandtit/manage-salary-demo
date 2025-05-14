package com.apus.manage_salary_demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({
        "com.apus.manage_salary_demo.repository"
})
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {
}
