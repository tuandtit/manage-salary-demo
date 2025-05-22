package com.apus.manage_salary_demo.client.resources;

import com.apus.manage_salary_demo.client.dto.BaseResponse;
import com.apus.manage_salary_demo.client.resources.dto.CurrencyDto;
import com.apus.manage_salary_demo.config.FeignClientConfig;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(
        name = "currencyClient",
        url = "https://resources-service.dev.apusplatform.com",
        configuration = FeignClientConfig.class
)
public interface CurrencyClient {
    @GetMapping("/api/v1/currency")
    BaseResponse<CurrencyDto> getCurrencyById(@RequestParam Long currencyId);

//    @GetMapping("/api/v1/currency/list")
//    BaseResponse<PagingResponse<CurrencyDto>> getListCurrency(@RequestParam String currencyIds);
@GetMapping("/api/v1/currency/list")
BaseResponse<PagingResponse<CurrencyDto>> getListCurrency(@RequestParam Set<Long> currencyIds);
}
