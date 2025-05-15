package com.apus.manage_salary_demo.controller;

import com.apus.manage_salary_demo.dto.AllowancePolicyDto;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.request.search.AllowancePolicySearchRequest;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import com.apus.manage_salary_demo.dto.response.Response;
import com.apus.manage_salary_demo.service.AllowancePolicyService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/allowance-policy")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AllowancePolicyController {
    AllowancePolicyService policyService;

    @PostMapping
    public Response<BaseDto> create(@Valid @RequestBody AllowancePolicyDto dto) {
        return Response.created(policyService.create(dto));
    }

    @PutMapping
    public Response<BaseDto> update(@Valid @RequestBody AllowancePolicyDto dto) {
        return Response.ok(policyService.update(dto));
    }

    @DeleteMapping
    public Response<Void> delete(Long id) {
        policyService.delete(id);
        return Response.noContent();
    }

    @GetMapping
    public Response<AllowancePolicyDto> getById(@RequestParam("id") Long id) {
        return Response.ok(policyService.getById(id));
    }

    @GetMapping("/list")
    public Response<PagingResponse<AllowancePolicyDto>> getAll(
            @Valid @ParameterObject AllowancePolicySearchRequest request
    ) {
        return Response.ok(PagingResponse.from(policyService.getAll(request)));
    }
}
