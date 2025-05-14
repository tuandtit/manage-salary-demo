package com.apus.manage_salary_demo.client.product;

import com.apus.manage_salary_demo.client.dto.BaseResponse;
import com.apus.manage_salary_demo.client.product.dto.UomDto;
import com.apus.manage_salary_demo.config.FeignClientConfig;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "unitClient",
        url = "https://product-manufactor-service.dev.apusplatform.com",
        configuration = FeignClientConfig.class
)
public interface UomClient {
    @GetMapping("/api/v1/unit")
    BaseResponse<UomDto> getUomById(@RequestParam Long unitId);

    @GetMapping("/api/v1/unit/list")
    BaseResponse<PagingResponse<UomDto>> getListUom(@RequestParam String ids);
}
