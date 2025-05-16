package com.apus.manage_salary_demo.service.helper;

import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.dto.RewardPolicyLineDto;
import com.apus.manage_salary_demo.entity.RewardPolicyLineEntity;
import com.apus.manage_salary_demo.mapper.RewardPolicyLineMapper;
import com.apus.manage_salary_demo.repository.RewardPolicyLineRepository;
import com.apus.manage_salary_demo.repository.RewardRepository;
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
public class RewardPolicyLineService {

    private final RewardPolicyLineRepository lineRepository;
    private final RewardRepository rewardRepository; // để validate allowance tồn tại
    private final RewardPolicyLineMapper lineMapper;


    @Transactional
    public void saveLines(Long policyId, List<RewardPolicyLineDto> lines) {
        if (policyId == null || lines == null || lines.isEmpty()) return;

        List<RewardPolicyLineEntity> entities = buildLines(policyId, lines);

        lineRepository.saveAll(entities);
    }

    private List<RewardPolicyLineEntity> buildLines(Long policyId, List<RewardPolicyLineDto> lines) {
        return lines.stream()
                .map(dto -> {
                    RewardPolicyLineEntity entity = lineMapper.toEntity(dto);
                    entity.setRewardId(existsReward(dto.getReward().getId()));
                    entity.setRewardPolicyId(policyId);
                    return entity;
                }).toList();
    }

    @Transactional
    public void updateLines(Long policyId, List<RewardPolicyLineDto> incomingDtos) {
        List<RewardPolicyLineEntity> currentEntities = getLinesByPolicyId(policyId);
        Map<Long, RewardPolicyLineEntity> currentMap = mapEntitiesById(currentEntities);
        Set<Long> incomingIds = extractIdsFromDtos(incomingDtos);

        deleteRemovedEntities(currentEntities, incomingIds);
        upsertEntities(policyId, incomingDtos, currentMap);
    }

    public List<RewardPolicyLineEntity> getLinesByPolicyId(Long policyId) {
        return lineRepository.findByRewardPolicyId(policyId);
    }

    private Map<Long, RewardPolicyLineEntity> mapEntitiesById(List<RewardPolicyLineEntity> entities) {
        return entities.stream()
                .collect(Collectors.toMap(RewardPolicyLineEntity::getId, Function.identity()));
    }

    private Set<Long> extractIdsFromDtos(List<RewardPolicyLineDto> dtos) {
        return dtos.stream()
                .map(RewardPolicyLineDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void deleteRemovedEntities(List<RewardPolicyLineEntity> currentEntities, Set<Long> incomingIds) {
        for (var entity : currentEntities) {
            if (!incomingIds.contains(entity.getId())) {
                lineRepository.delete(entity);
            }
        }
    }

    private void upsertEntities(Long policyId, List<RewardPolicyLineDto> dtos, Map<Long, RewardPolicyLineEntity> currentMap) {
        for (var dto : dtos) {
            if (dto.getId() != null && currentMap.containsKey(dto.getId())) {
                updateExistingEntity(dto, currentMap.get(dto.getId()));
            } else {
                createNewEntity(policyId, dto);
            }
        }
    }

    private void updateExistingEntity(RewardPolicyLineDto dto, RewardPolicyLineEntity entity) {
        lineMapper.update(dto, entity);
        if (dto.getReward().getId() != null) {
            entity.setRewardId(existsReward(dto.getReward().getId()));
        }
        lineRepository.save(entity);
    }

    private void createNewEntity(Long policyId, RewardPolicyLineDto dto) {
        RewardPolicyLineEntity newEntity = lineMapper.toEntity(dto);
        newEntity.setRewardPolicyId(policyId);
        newEntity.setRewardId(existsReward(dto.getReward().getId()));
        lineRepository.save(newEntity);
    }

    private Long existsReward(Long id) {
        return rewardRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "Reward not found with id: " + id)).getId();
    }
}
