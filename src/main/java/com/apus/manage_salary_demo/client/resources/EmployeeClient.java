package com.apus.manage_salary_demo.client.resources;

import com.apus.manage_salary_demo.client.dto.BaseResponse;
import com.apus.manage_salary_demo.client.resources.dto.EmployeeDto;
import com.apus.manage_salary_demo.dto.SimpleDto;
import com.apus.manage_salary_demo.config.FeignClientConfig;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(
        name = "employeeClient",
        url = "https://resources-service.dev.apusplatform.com",
        configuration = FeignClientConfig.class
)
public interface EmployeeClient {
    @GetMapping("/api/v1/employee/list")
    BaseResponse<PagingResponse<EmployeeDto>> getAllEmployeeByIds(@RequestParam Set<Long> ids);

    @GetMapping("/api/v1/employee")
    BaseResponse<EmployeeDto> getEmployeeById(@RequestParam Long employeeId);
}
