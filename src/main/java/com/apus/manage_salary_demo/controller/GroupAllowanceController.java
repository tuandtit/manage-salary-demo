package com.apus.manage_salary_demo.controller;

import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.GroupAllowanceDto;
import com.apus.manage_salary_demo.dto.request.search.GroupAllowanceSearchRequest;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import com.apus.manage_salary_demo.dto.response.Response;
import com.apus.manage_salary_demo.service.GroupAllowanceService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group-allowance")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupAllowanceController {
    GroupAllowanceService groupAllowanceService;

    @PostMapping
    public Response<BaseDto> create(@Valid @RequestBody GroupAllowanceDto dto) {
        return Response.created(groupAllowanceService.create(dto));
    }

    @PutMapping
    public Response<BaseDto> update(@Valid @RequestBody GroupAllowanceDto dto) {
        return Response.ok(groupAllowanceService.update(dto));
    }

    @DeleteMapping
    public Response<Void> delete(Long id) {
        groupAllowanceService.delete(id);
        return Response.noContent();
    }

    @GetMapping
    public Response<GroupAllowanceDto> getById(@RequestParam("id") Long id) {
        return Response.ok(groupAllowanceService.getById(id));
    }

    @GetMapping("/list")
    public Response<PagingResponse<GroupAllowanceDto>> getAll(
            @ParameterObject GroupAllowanceSearchRequest request
    ) {
        return Response.ok(PagingResponse.from(groupAllowanceService.getAll(request)));
    }
}
