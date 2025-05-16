package com.apus.manage_salary_demo.controller;

import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.RewardPolicyDto;
import com.apus.manage_salary_demo.dto.request.search.RewardPolicySearchRequest;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import com.apus.manage_salary_demo.dto.response.Response;
import com.apus.manage_salary_demo.service.RewardPolicyService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reward-policy")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RewardPolicyController {
    RewardPolicyService policyService;

    @PostMapping
    public Response<BaseDto> create(@Valid @RequestBody RewardPolicyDto dto) {
        return Response.created(policyService.create(dto));
    }

    @PutMapping
    public Response<BaseDto> update(@Valid @RequestBody RewardPolicyDto dto) {
        return Response.ok(policyService.update(dto));
    }

    @DeleteMapping
    public Response<Void> delete(Long id) {
        policyService.delete(id);
        return Response.noContent();
    }

    @GetMapping
    public Response<RewardPolicyDto> getById(@RequestParam("id") Long id) {
        return Response.ok(policyService.getById(id));
    }

    @GetMapping("/list")
    public Response<PagingResponse<RewardPolicyDto>> getAll(
            @Valid @ParameterObject RewardPolicySearchRequest request
    ) {
        return Response.ok(PagingResponse.from(policyService.getAll(request)));
    }
}
