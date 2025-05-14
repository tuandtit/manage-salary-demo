package com.apus.manage_salary_demo.controller;

import com.apus.manage_salary_demo.dto.AllowanceDto;
import com.apus.manage_salary_demo.dto.request.search.AllowanceSearchRequest;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import com.apus.manage_salary_demo.dto.response.Response;
import com.apus.manage_salary_demo.service.AllowanceService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/allowance")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AllowanceController {
    AllowanceService allowanceService;

    @PostMapping
    public Response<AllowanceDto> create(@Valid @RequestBody AllowanceDto dto) {
        return Response.created(allowanceService.create(dto));
    }

    @PutMapping
    public Response<AllowanceDto> update(@Valid @RequestBody AllowanceDto dto) {
        return Response.ok(allowanceService.update(dto));
    }

    @DeleteMapping
    public Response<Void> delete(Long id) {
        allowanceService.delete(id);
        return Response.noContent();
    }

    @GetMapping
    public Response<AllowanceDto> getById(@RequestParam("id") Long id) {
        return Response.ok(allowanceService.getById(id));
    }

    @GetMapping("/list")
    public Response<PagingResponse<AllowanceDto>> getAll(
            @ParameterObject AllowanceSearchRequest request
    ) {
        return Response.ok(PagingResponse.from(allowanceService.getAll(request)));
    }
}
