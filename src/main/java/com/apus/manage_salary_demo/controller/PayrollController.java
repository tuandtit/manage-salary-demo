package com.apus.manage_salary_demo.controller;

import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.PayrollDto;
import com.apus.manage_salary_demo.dto.request.search.PayrollSearchRequest;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import com.apus.manage_salary_demo.dto.response.Response;
import com.apus.manage_salary_demo.service.PayrollService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payroll")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PayrollController {
    PayrollService payrollService;

    @PostMapping
    public Response<BaseDto> create(@Valid @RequestBody PayrollDto dto) {
        return Response.created(payrollService.create(dto));
    }

    @PutMapping
    public Response<BaseDto> update(@Valid @RequestBody PayrollDto dto) {
        return Response.ok(payrollService.update(dto));
    }

    @DeleteMapping
    public Response<Void> delete(Long id) {
        payrollService.delete(id);
        return Response.noContent();
    }

    @GetMapping
    public Response<PayrollDto> getById(@RequestParam("id") Long id) {
        return Response.ok(payrollService.getById(id));
    }

    @GetMapping("/list")
    public Response<PagingResponse<PayrollDto>> getAll(
            @Valid @ParameterObject PayrollSearchRequest request
    ) {
        return Response.ok(PagingResponse.from(payrollService.getAll(request)));
    }
}
