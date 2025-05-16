package com.apus.manage_salary_demo.controller;

import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.GroupRewardDto;
import com.apus.manage_salary_demo.dto.request.search.GroupRewardSearchRequest;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import com.apus.manage_salary_demo.dto.response.Response;
import com.apus.manage_salary_demo.service.GroupRewardService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group-reward")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupRewardController {
    GroupRewardService rewardService;

    @PostMapping
    public Response<BaseDto> create(@Valid @RequestBody GroupRewardDto dto) {
        return Response.created(rewardService.create(dto));
    }

    @PutMapping
    public Response<BaseDto> update(@Valid @RequestBody GroupRewardDto dto) {
        return Response.ok(rewardService.update(dto));
    }

    @DeleteMapping
    public Response<Void> delete(Long id) {
        rewardService.delete(id);
        return Response.noContent();
    }

    @GetMapping
    public Response<GroupRewardDto> getById(@RequestParam("id") Long id) {
        return Response.ok(rewardService.getById(id));
    }

    @GetMapping("/list")
    public Response<PagingResponse<GroupRewardDto>> getAll(
            @ParameterObject GroupRewardSearchRequest request
    ) {
        return Response.ok(PagingResponse.from(rewardService.getAll(request)));
    }
}
