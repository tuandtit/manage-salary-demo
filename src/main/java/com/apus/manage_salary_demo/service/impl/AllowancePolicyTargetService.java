package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.dto.AllowanceTargetDto;
import com.apus.manage_salary_demo.entity.AllowancePolicyApplicableTargetEntity;
import com.apus.manage_salary_demo.repository.AllowancePolicyTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AllowancePolicyTargetService {

    private final AllowancePolicyTargetRepository targetRepository;

    @Transactional
    public void saveTargets(Long policyId, List<AllowanceTargetDto> targets) {
        if (policyId == null || targets == null || targets.isEmpty()) return;

        List<AllowancePolicyApplicableTargetEntity> entities = buildTargets(policyId, targets);

        targetRepository.saveAll(entities);
    }

    private List<AllowancePolicyApplicableTargetEntity> buildTargets(Long policyId, List<AllowanceTargetDto> targets) {
        List<AllowancePolicyApplicableTargetEntity> entities = new ArrayList<>();
        for (var target : targets) {
            AllowancePolicyApplicableTargetEntity entity = AllowancePolicyApplicableTargetEntity.builder()
                    .allowancePolicyId(policyId)
                    .targetId(target.getTarget().getId())
                    .build();
            entities.add(entity);
        }
        return entities;
    }

    @Transactional
    public void updateTargets(Long policyId, List<AllowanceTargetDto> incomingDtos) {
        List<AllowancePolicyApplicableTargetEntity> currentEntities = targetRepository.findByAllowancePolicyId(policyId);
        Map<Long, AllowancePolicyApplicableTargetEntity> currentMap = mapTargetsById(currentEntities);
        Set<Long> incomingIds = extractTargetIdsFromDtos(incomingDtos);

        deleteRemovedTargets(currentEntities, incomingIds);
        upsertTargets(policyId, incomingDtos, currentMap);
    }

    public Set<Long> getTargetIdsByPolicyId(Long policyId) {
        return targetRepository.getTargetIdsByPolicyId(policyId);
    }

    private Map<Long, AllowancePolicyApplicableTargetEntity> mapTargetsById(List<AllowancePolicyApplicableTargetEntity> entities) {
        return entities.stream()
                .collect(Collectors.toMap(AllowancePolicyApplicableTargetEntity::getId, Function.identity()));
    }

    private Set<Long> extractTargetIdsFromDtos(List<AllowanceTargetDto> dtos) {
        return dtos.stream()
                .map(AllowanceTargetDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void deleteRemovedTargets(List<AllowancePolicyApplicableTargetEntity> currentEntities, Set<Long> incomingIds) {
        for (AllowancePolicyApplicableTargetEntity entity : currentEntities) {
            if (!incomingIds.contains(entity.getId())) {
                targetRepository.delete(entity);
            }
        }
    }

    private void upsertTargets(Long policyId, List<AllowanceTargetDto> dtos, Map<Long, AllowancePolicyApplicableTargetEntity> currentMap) {
        for (AllowanceTargetDto dto : dtos) {
            if (dto.getId() != null && currentMap.containsKey(dto.getId())) {
                updateExistingTarget(dto, currentMap.get(dto.getId()));
            } else {
                createNewTarget(policyId, dto);
            }
        }
    }

    private void updateExistingTarget(AllowanceTargetDto dto, AllowancePolicyApplicableTargetEntity entity) {
        entity.setTargetId(dto.getTarget().getId());
        targetRepository.save(entity);
    }

    private void createNewTarget(Long policyId, AllowanceTargetDto dto) {
        AllowancePolicyApplicableTargetEntity newEntity = AllowancePolicyApplicableTargetEntity.builder()
                .allowancePolicyId(policyId)
                .targetId(dto.getTarget().getId())
                .build();
        newEntity.setAllowancePolicyId(policyId);
        targetRepository.save(newEntity);
    }
}
