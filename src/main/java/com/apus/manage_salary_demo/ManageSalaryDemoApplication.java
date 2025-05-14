package com.apus.manage_salary_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ManageSalaryDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManageSalaryDemoApplication.class, args);
	}

}
