package com.apus.manage_salary_demo.controller;

import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.RewardDto;
import com.apus.manage_salary_demo.dto.request.search.RewardSearchRequest;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import com.apus.manage_salary_demo.dto.response.Response;
import com.apus.manage_salary_demo.service.RewardService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reward")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RewardController {
    RewardService rewardService;

    @PostMapping
    public Response<BaseDto> create(@Valid @RequestBody RewardDto dto) {
        return Response.created(rewardService.create(dto));
    }

    @PutMapping
    public Response<BaseDto> update(@Valid @RequestBody RewardDto dto) {
        return Response.ok(rewardService.update(dto));
    }

    @DeleteMapping
    public Response<Void> delete(Long id) {
        rewardService.delete(id);
        return Response.noContent();
    }

    @GetMapping
    public Response<RewardDto> getById(@RequestParam("id") Long id) {
        return Response.ok(rewardService.getById(id));
    }

    @GetMapping("/list")
    public Response<PagingResponse<RewardDto>> getAll(
            @ParameterObject RewardSearchRequest request
    ) {
        return Response.ok(PagingResponse.from(rewardService.getAll(request)));
    }
}
