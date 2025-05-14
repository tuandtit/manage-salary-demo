package com.apus.manage_salary_demo.config;

import com.apus.manage_salary_demo.common.constant.AppConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AuditorAwareConfig {

    @Bean(name = "auditorAware")
    public AuditorAware<String> auditorAware() {
        return () -> {
            // TODO: Bạn có thể lấy từ SecurityContext nếu dùng Spring Security
            // return Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());

            return Optional.of(AppConstant.SYSTEM); // fallback mặc định nếu chưa dùng security
        };
    }
}
