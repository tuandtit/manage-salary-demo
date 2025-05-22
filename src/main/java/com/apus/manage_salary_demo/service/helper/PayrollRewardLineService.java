package com.apus.manage_salary_demo.service.helper;

import com.apus.manage_salary_demo.client.resources.dto.CurrencyDto;
import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.common.utils.ConvertUtils;
import com.apus.manage_salary_demo.config.Translator;
import com.apus.manage_salary_demo.dto.PayrollRewardLineDto;
import com.apus.manage_salary_demo.dto.RewardLineDto;
import com.apus.manage_salary_demo.dto.SimpleDto;
import com.apus.manage_salary_demo.dto.SimpleRewardDto;
import com.apus.manage_salary_demo.entity.GroupRewardEntity;
import com.apus.manage_salary_demo.entity.PayrollRewardLineEntity;
import com.apus.manage_salary_demo.entity.RewardEntity;
import com.apus.manage_salary_demo.mapper.PayrollRewardLineMapper;
import com.apus.manage_salary_demo.repository.GroupRewardRepository;
import com.apus.manage_salary_demo.repository.PayrollRewardLineRepository;
import com.apus.manage_salary_demo.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollRewardLineService {

    private final PayrollRewardLineRepository lineRepository;
    private final RewardRepository rewardRepository;
    private final GroupRewardRepository groupRewardRepository;
    private final PayrollRewardLineMapper lineMapper;
    private final ClientServiceHelper clientHelper;
    private final ConvertUtils convertUtils;

    private final Translator translator;


    @Transactional
    public void createOrUpdateLines(Long payrollId, Long groupRewardId, List<RewardLineDto> rewardLineDtos) {
        if (ObjectUtils.isEmpty(rewardLineDtos))
            deleteByPayrollId(payrollId);

        List<PayrollRewardLineEntity> currentEntities = lineRepository.findByPayrollIdAndGroupRewardId(payrollId, groupRewardId);
        Map<Long, PayrollRewardLineEntity> currentMap = mapEntitiesById(currentEntities);

        Set<Long> incomingIds = extractIdsFromDtos(rewardLineDtos);
        if (!incomingIds.isEmpty())
            deleteRemovedEntities(currentEntities, incomingIds);

        List<PayrollRewardLineEntity> entities = new ArrayList<>();
        for (var rewardLineDto : rewardLineDtos) {
            if (Objects.isNull(rewardLineDto.getId())) {
                entities.add(buildLine(payrollId, groupRewardId, rewardLineDto));
            } else {
                entities.add(updateLine(groupRewardId, rewardLineDto, currentMap));
            }
        }
        lineRepository.saveAll(entities);
    }

    private Map<Long, PayrollRewardLineEntity> mapEntitiesById(List<PayrollRewardLineEntity> entities) {
        return entities.stream()
                .collect(Collectors.toMap(PayrollRewardLineEntity::getId, Function.identity()));
    }

    private Set<Long> extractIdsFromDtos(List<RewardLineDto> dtos) {
        return dtos.stream()
                .map(RewardLineDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }


    private PayrollRewardLineEntity buildLine(Long payrollId, Long groupRewardId, RewardLineDto rewardLine) {
        PayrollRewardLineEntity entity = lineMapper.toEntity(rewardLine);
        var rewardEntity = checkValidGroupAndReward(groupRewardId, rewardLine.getReward().getId());
        entity.setPayrollId(payrollId);
        entity.setGroupRewardId(rewardEntity.getGroupRewardId());
        entity.setRewardId(rewardEntity.getId());
        return entity;
    }

    private PayrollRewardLineEntity updateLine(Long groupRewardId, RewardLineDto rewardLineDto, Map<Long, PayrollRewardLineEntity> currentMap) {
        PayrollRewardLineEntity entity = currentMap.get(rewardLineDto.getId());
        if (Objects.isNull(entity))
            throw new BusinessException("404", "PayrollRewardLineEntity" + translator.toLocale("error.resource.not.found") + rewardLineDto.getId());

        checkValidGroupAndReward(groupRewardId, rewardLineDto.getReward().getId());
        lineMapper.update(rewardLineDto, entity);
        return entity;
    }

    private void deleteRemovedEntities(List<PayrollRewardLineEntity> currentEntities, Set<Long> incomingIds) {
        for (var entity : currentEntities) {
            if (!incomingIds.contains(entity.getId())) {
                lineRepository.delete(entity);
            }
        }
    }

    private RewardEntity checkValidGroupAndReward(Long groupRewardId, Long rewardId) {
        RewardEntity rewardEntity = existsReward(rewardId);
        if (!Objects.equals(rewardEntity.getGroupRewardId(), groupRewardId))
            throw new BusinessException("400", "The reward with ID: " + rewardEntity.getId() + " does not belong to the allowance group with ID: " + groupRewardId);
        return rewardEntity;
    }

    public BigDecimal getTotalRewardAmount(List<PayrollRewardLineDto> lines) {
        BigDecimal total = BigDecimal.ZERO;
        if (lines.isEmpty()) return total;

        for (var line : lines) {
            for (var allowance : line.getRewardLines()) {
                if (allowance != null)
                    total = total.add(allowance.getAmount());
            }
        }

        return total;
    }

    public void deleteByPayrollId(Long payrollId) {
        lineRepository.deleteByPayrollId(payrollId);
    }

    public List<PayrollRewardLineDto> getPayrollRewardLineDtosByPayrollId(Long payrollId) {
        List<PayrollRewardLineEntity> lines = lineRepository.findByPayrollId(payrollId);
        Set<Long> rewardIds = new HashSet<>();
        Set<Long> groupRewardIds = new HashSet<>();
        for (var line : lines) {
            if (line.getRewardId() != null) {
                rewardIds.add(line.getRewardId());
            }
            if (line.getGroupRewardId() != null) {
                groupRewardIds.add(line.getGroupRewardId());
            }
        }
        Map<Long, SimpleRewardDto> rewardMap = buildRewardMapFromLines(rewardIds);
        Map<Long, SimpleDto> groupRewardMap = buildGroupRewardMapFromLines(groupRewardIds);

        List<RewardLineDto> responseList = new ArrayList<>();
        for (PayrollRewardLineEntity line : lines) {
            RewardLineDto response = lineMapper.toDto(line);
            response.setGroupReward(groupRewardMap.get(line.getGroupRewardId()));
            response.setReward(rewardMap.get(line.getRewardId()));
            responseList.add(response);
        }

        // Nhóm theo groupReward và trả về kết quả
        return mapToGroupedDtos(responseList);
    }

    private Map<Long, SimpleDto> buildGroupRewardMapFromLines(Set<Long> groupRewardIds) {
        List<GroupRewardEntity> groupRewardEntities = groupRewardRepository.findAllById(groupRewardIds);
        List<SimpleDto> content = new ArrayList<>();
        for (var groupReward : groupRewardEntities) {
            content.add(SimpleDto.builder()
                    .id(groupReward.getId())
                    .code(groupReward.getCode())
                    .name(groupReward.getName())
                    .build());
        }
        return content.stream()
                .collect(Collectors.toMap(SimpleDto::getId, Function.identity()));
    }

    private Map<Long, SimpleRewardDto> buildRewardMapFromLines(Set<Long> rewardIds) {

        List<RewardEntity> rewardEntities = rewardRepository.findAllById(rewardIds);

        Set<Long> currencyIds = new HashSet<>();
        for (var reward : rewardEntities) {
            if (reward.getCurrencyId() != null) {
                currencyIds.add(reward.getCurrencyId());
            }
        }
        Map<Long, CurrencyDto> currencyMap = clientHelper.buildCurrencyMap(currencyIds);
        List<SimpleRewardDto> content = new ArrayList<>();

        for (var reward : rewardEntities) {
            List<String> includeTypes = reward.getIncludeType() == null ? List.of() : convertUtils.convert(reward.getIncludeType());
            content.add(SimpleRewardDto.builder()
                    .id(reward.getId())
                    .code(reward.getCode())
                    .includeTypes(includeTypes)
                    .currency(currencyMap.get(reward.getCurrencyId()))
                    .build());
        }

        return content.stream()
                .collect(Collectors.toMap(SimpleRewardDto::getId, Function.identity()));
    }

    private List<PayrollRewardLineDto> mapToGroupedDtos(List<RewardLineDto> responses) {
        if (responses == null || responses.isEmpty()) return Collections.emptyList();

        Map<SimpleDto, List<RewardLineDto>> groupedMap = responses.stream()
                .collect(Collectors.groupingBy(RewardLineDto::getGroupReward));

        return groupedMap.entrySet().stream()
                .map(entry -> {
                    SimpleDto group = entry.getKey();
                    List<RewardLineDto> allowanceLines = entry.getValue();

                    return PayrollRewardLineDto.builder()
                            .groupReward(group)
                            .rewardLines(allowanceLines)
                            .build();
                })
                .toList();
    }

    private RewardEntity existsReward(Long id) {
        if (Objects.isNull(id))
            throw new BusinessException("400", translator.toLocale("error.id.not.null"));
        return rewardRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "Reward " + translator.toLocale("error.resource.not.found") + id));
    }
}
