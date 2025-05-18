package com.apus.manage_salary_demo.service.helper;

import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.dto.*;
import com.apus.manage_salary_demo.dto.request.PayrollRewardLineRequest;
import com.apus.manage_salary_demo.dto.response.PayrollRewardLineResponse;
import com.apus.manage_salary_demo.entity.PayrollRewardLineEntity;
import com.apus.manage_salary_demo.entity.RewardEntity;
import com.apus.manage_salary_demo.mapper.PayrollRewardLineMapper;
import com.apus.manage_salary_demo.repository.PayrollRewardLineRepository;
import com.apus.manage_salary_demo.repository.RewardRepository;
import com.apus.manage_salary_demo.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollRewardLineService {

    private final PayrollRewardLineRepository lineRepository;
    private final RewardRepository rewardRepository;
    private final PayrollRewardLineMapper lineMapper;
    private final RewardService rewardService;


    @Transactional
    public void saveLines(Long payrollId, List<PayrollRewardLineRequest> lines) {
        if (payrollId == null || lines == null || lines.isEmpty()) return;

        List<PayrollRewardLineEntity> entities = buildLines(payrollId, lines);

        lineRepository.saveAll(entities);
    }

    private List<PayrollRewardLineEntity> buildLines(Long payrollId, List<PayrollRewardLineRequest> lines) {
        return lines.stream()
                .map(dto -> {
                    PayrollRewardLineEntity entity = lineMapper.toEntity(dto);
                    RewardEntity rewardEntity = checkValidGroupAndReward(dto);
                    entity.setPayrollId(payrollId);
                    entity.setGroupRewardId(rewardEntity.getGroupRewardId());
                    entity.setRewardId(rewardEntity.getId());
                    return entity;
                }).toList();
    }

    private RewardEntity checkValidGroupAndReward(PayrollRewardLineRequest dto) {
        RewardEntity rewardEntity = existsReward(dto.getReward().getId());
        if (!Objects.equals(rewardEntity.getGroupRewardId(), dto.getGroupReward().getId()))
            throw new BusinessException("400", "The reward with ID: " + rewardEntity.getId() + " does not belong to the reward group with ID: " + dto.getGroupReward().getId());
        return rewardEntity;
    }

    public BigDecimal getTotalRewardAmount(List<PayrollRewardLineRequest> lines) {
        BigDecimal total = BigDecimal.ZERO;
        if (lines.isEmpty()) return total;

        for (var line : lines) {
            if (line != null)
                total = total.add(line.getAmount());

        }

        return total;
    }

    @Transactional
    public void updateLines(Long payrollId, List<PayrollRewardLineRequest> incomingDtos) {
        List<PayrollRewardLineEntity> currentEntities = lineRepository.findByPayrollId(payrollId);
        Map<Long, PayrollRewardLineEntity> currentMap = mapEntitiesById(currentEntities);
        Set<Long> incomingIds = extractIdsFromDtos(incomingDtos);

        deleteRemovedEntities(currentEntities, incomingIds);
        upsertEntities(payrollId, incomingDtos, currentMap);
    }

    private Map<Long, PayrollRewardLineEntity> mapEntitiesById(List<PayrollRewardLineEntity> entities) {
        return entities.stream()
                .collect(Collectors.toMap(PayrollRewardLineEntity::getId, Function.identity()));
    }

    private Set<Long> extractIdsFromDtos(List<PayrollRewardLineRequest> dtos) {
        return dtos.stream()
                .map(PayrollRewardLineRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void deleteRemovedEntities(List<PayrollRewardLineEntity> currentEntities, Set<Long> incomingIds) {
        for (var entity : currentEntities) {
            if (!incomingIds.contains(entity.getId())) {
                lineRepository.delete(entity);
            }
        }
    }

    private void upsertEntities(Long payrollId, List<PayrollRewardLineRequest> dtos, Map<Long, PayrollRewardLineEntity> currentMap) {
        for (var dto : dtos) {
            if (dto.getId() != null && currentMap.containsKey(dto.getId())) {
                updateExistingEntity(dto, currentMap.get(dto.getId()));
            } else {
                createNewEntity(payrollId, dto);
            }
        }
    }

    private void updateExistingEntity(PayrollRewardLineRequest dto, PayrollRewardLineEntity entity) {
        lineMapper.update(dto, entity);
        if (dto.getReward().getId() != null) {
            RewardEntity rewardEntity = checkValidGroupAndReward(dto);
            entity.setRewardId(rewardEntity.getId());
            entity.setGroupRewardId(rewardEntity.getGroupRewardId());
        }
        lineRepository.save(entity);
    }

    private void createNewEntity(Long payrollId, PayrollRewardLineRequest dto) {
        PayrollRewardLineEntity newEntity = lineMapper.toEntity(dto);
        var rewardEntity = checkValidGroupAndReward(dto);
        newEntity.setPayrollId(payrollId);
        newEntity.setRewardId(rewardEntity.getGroupRewardId());
        newEntity.setGroupRewardId(rewardEntity.getId());
        lineRepository.save(newEntity);
    }

    public List<PayrollRewardLineDto> getPayrollRewardLineDtosByPayrollId(Long payrollId) {
        // Lấy danh sách entity từ service
        List<PayrollRewardLineEntity> lines = lineRepository.findByPayrollId(payrollId);

        // Xây rewardMap (mỗi rewardId -> RewardDto)
        Map<Long, RewardDto> rewardMap = buildRewardMapFromLines(lines);

        // Chuyển thành List<PayrollRewardLineResponse>
        List<PayrollRewardLineResponse> responseList = new ArrayList<>();
        for (PayrollRewardLineEntity line : lines) {
            PayrollRewardLineResponse response = lineMapper.toDto(line);
            RewardDto rewardDto = rewardMap.get(line.getRewardId());
            response.setReward(rewardDto);
            response.setGroupReward(rewardDto.getGroupReward());
            responseList.add(response);
        }

        // Nhóm theo groupAllowance và trả về kết quả
        return mapToGroupedDtos(responseList);
    }

    private List<PayrollRewardLineDto> mapToGroupedDtos(List<PayrollRewardLineResponse> responses) {
        if (responses == null || responses.isEmpty()) return Collections.emptyList();

        Map<SimpleDto, List<PayrollRewardLineResponse>> groupedMap = responses.stream()
                .collect(Collectors.groupingBy(PayrollRewardLineResponse::getGroupReward));

        return groupedMap.entrySet().stream()
                .map(entry -> {
                    SimpleDto group = entry.getKey();
                    List<RewardLineDto> rewardLines = entry.getValue().stream()
                            .map(response -> RewardLineDto.builder()
                                    .id(response.getId())
                                    .reward(response.getReward())
                                    .amountItem(response.getAmountItem())
                                    .amount(response.getAmount())
                                    .taxableAmount(response.getTaxableAmount())
                                    .insuranceAmount(response.getInsuranceAmount())
                                    .build())
                            .toList();

                    return PayrollRewardLineDto.builder()
                            .groupAllowance(group)
                            .rewardLines(rewardLines)
                            .build();
                })
                .toList();
    }

    private Map<Long, RewardDto> buildRewardMapFromLines(List<PayrollRewardLineEntity> lines) {
        Set<Long> rewardIds = new HashSet<>();
        for (var line : lines) {
            if (line.getRewardId() != null) {
                rewardIds.add(line.getRewardId());
            }
        }
        if (rewardIds.isEmpty()) return Collections.emptyMap();

        List<RewardDto> content = rewardService.getAllDetailByIds(rewardIds);

        return content.stream()
                .collect(Collectors.toMap(RewardDto::getId, Function.identity()));
    }

    private RewardEntity existsReward(Long id) {
        return rewardRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "Reward not found with id: " + id));
    }
}
