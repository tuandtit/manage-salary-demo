package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.dto.AllowancePolicyLineDto;
import com.apus.manage_salary_demo.entity.AllowancePolicyLineEntity;
import com.apus.manage_salary_demo.mapper.AllowancePolicyLineMapper;
import com.apus.manage_salary_demo.repository.AllowancePolicyLineRepository;
import com.apus.manage_salary_demo.repository.AllowanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AllowancePolicyLineService {

    private final AllowancePolicyLineRepository lineRepository;
    private final AllowanceRepository allowanceRepository; // để validate allowance tồn tại
    private final AllowancePolicyLineMapper lineMapper;


    @Transactional
    public void saveLines(Long policyId, List<AllowancePolicyLineDto> lines) {
        if (policyId == null || lines == null || lines.isEmpty()) return;

        List<AllowancePolicyLineEntity> entities = buildLines(policyId, lines);

        lineRepository.saveAll(entities);
    }

    private List<AllowancePolicyLineEntity> buildLines(Long policyId, List<AllowancePolicyLineDto> lines) {
        return lines.stream()
                .map(dto -> {
                    AllowancePolicyLineEntity entity = lineMapper.toEntity(dto);
                    entity.setAllowanceId(existsAllowance(dto.getAllowance().getId()));
                    entity.setAllowancePolicyId(policyId);
                    return entity;
                }).toList();
    }

    @Transactional
    public void updateLines(Long policyId, List<AllowancePolicyLineDto> incomingDtos) {
        List<AllowancePolicyLineEntity> currentEntities = getLinesByPolicyId(policyId);
        Map<Long, AllowancePolicyLineEntity> currentMap = mapEntitiesById(currentEntities);
        Set<Long> incomingIds = extractIdsFromDtos(incomingDtos);

        deleteRemovedEntities(currentEntities, incomingIds);
        upsertEntities(policyId, incomingDtos, currentMap);
    }

    public List<AllowancePolicyLineEntity> getLinesByPolicyId(Long policyId) {
        return lineRepository.findByAllowancePolicyId(policyId);
    }

    private Map<Long, AllowancePolicyLineEntity> mapEntitiesById(List<AllowancePolicyLineEntity> entities) {
        return entities.stream()
                .collect(Collectors.toMap(AllowancePolicyLineEntity::getId, Function.identity()));
    }

    private Set<Long> extractIdsFromDtos(List<AllowancePolicyLineDto> dtos) {
        return dtos.stream()
                .map(AllowancePolicyLineDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void deleteRemovedEntities(List<AllowancePolicyLineEntity> currentEntities, Set<Long> incomingIds) {
        for (AllowancePolicyLineEntity entity : currentEntities) {
            if (!incomingIds.contains(entity.getId())) {
                lineRepository.delete(entity);
            }
        }
    }

    private void upsertEntities(Long policyId, List<AllowancePolicyLineDto> dtos, Map<Long, AllowancePolicyLineEntity> currentMap) {
        for (AllowancePolicyLineDto dto : dtos) {
            if (dto.getId() != null && currentMap.containsKey(dto.getId())) {
                updateExistingEntity(dto, currentMap.get(dto.getId()));
            } else {
                createNewEntity(policyId, dto);
            }
        }
    }

    private void updateExistingEntity(AllowancePolicyLineDto dto, AllowancePolicyLineEntity entity) {
        lineMapper.update(dto, entity);
        if (dto.getAllowance().getId() != null) {
            entity.setAllowanceId(existsAllowance(dto.getAllowance().getId()));
        }
        lineRepository.save(entity);
    }

    private void createNewEntity(Long policyId, AllowancePolicyLineDto dto) {
        AllowancePolicyLineEntity newEntity = lineMapper.toEntity(dto);
        newEntity.setAllowancePolicyId(policyId);
        newEntity.setAllowanceId(dto.getAllowance().getId());
        lineRepository.save(newEntity);
    }

    private Long existsAllowance(Long id) {
        return allowanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "Allowance not found with id: " + id)).getId();
    }
}
