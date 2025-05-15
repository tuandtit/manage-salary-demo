package com.apus.manage_salary_demo.client.resources;

import com.apus.manage_salary_demo.client.dto.BaseResponse;
import com.apus.manage_salary_demo.client.resources.dto.TargetDto;
import com.apus.manage_salary_demo.config.FeignClientConfig;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "employeeClient",
        url = "https://resources-service.dev.apusplatform.com",
        configuration = FeignClientConfig.class
)
public interface EmployeeClient {
    @GetMapping("/api/v1/employee/list")
    BaseResponse<PagingResponse<TargetDto>> getAllEmployeeByIds(@RequestParam String ids);
}
