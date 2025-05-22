package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.client.resources.dto.EmployeeDto;
import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.config.Translator;
import com.apus.manage_salary_demo.dto.*;
import com.apus.manage_salary_demo.dto.request.search.PayrollSearchRequest;
import com.apus.manage_salary_demo.entity.PayrollAllowanceLineEntity;
import com.apus.manage_salary_demo.entity.PayrollEntity;
import com.apus.manage_salary_demo.mapper.PayrollMapper;
import com.apus.manage_salary_demo.repository.PayrollRepository;
import com.apus.manage_salary_demo.service.PayrollService;
import com.apus.manage_salary_demo.service.helper.ClientServiceHelper;
import com.apus.manage_salary_demo.service.helper.PayrollAllowanceLineService;
import com.apus.manage_salary_demo.service.helper.PayrollRewardLineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PayrollServiceImpl implements PayrollService {
    //Repository
    PayrollRepository payrollRepository;

    //Mapper
    PayrollMapper payrollMapper;

    //Helper
    ClientServiceHelper clientHelper;
    PayrollAllowanceLineService allowanceLineService;
    PayrollRewardLineService rewardLineService;

    Translator translator;

    @Override
    @Transactional
    public BaseDto create(PayrollDto dto) {
        PayrollEntity entity = payrollMapper.toEntity(dto);
        entity.setTotalAllowanceAmount(allowanceLineService.getTotalAllowanceAmount(dto.getAllowances()));
        PayrollEntity saved = payrollRepository.save(entity);

        for (var line : dto.getAllowances()) {
            Long groupAllowanceId = line.getGroupAllowance().getId();
            if (!Objects.isNull(saved.getId()) && !Objects.isNull(groupAllowanceId))
                allowanceLineService.createOrUpdateLines(saved.getId(), groupAllowanceId, line.getAllowanceLines());

        }

        for (var line : dto.getRewards()) {
            Long groupRewardId = line.getGroupReward().getId();
            if (!Objects.isNull(saved.getId()) && !Objects.isNull(groupRewardId))
                rewardLineService.createOrUpdateLines(saved.getId(), groupRewardId, line.getRewardLines());

        }

        return BaseDto.builder()
                .id(saved.getId())
                .build();
    }

    @Override
    @Transactional
    public BaseDto update(PayrollDto dto) {
        var entity = existsPayrollEntity(dto.getId());
        payrollMapper.update(dto, entity);
        entity.setTotalAllowanceAmount(allowanceLineService.getTotalAllowanceAmount(dto.getAllowances()));

        var saved = payrollRepository.save(entity);

        for (var line : dto.getAllowances()) {
            Long groupAllowanceId = line.getGroupAllowance().getId();
            if (!Objects.isNull(saved.getId()) && !Objects.isNull(groupAllowanceId))
                allowanceLineService.createOrUpdateLines(saved.getId(), groupAllowanceId, line.getAllowanceLines());

        }

        for (var line : dto.getRewards()) {
            Long groupRewardId = line.getGroupReward().getId();
            if (!Objects.isNull(saved.getId()) && !Objects.isNull(groupRewardId))
                rewardLineService.createOrUpdateLines(saved.getId(), groupRewardId, line.getRewardLines());

        }

        return BaseDto.builder()
                .id(saved.getId())
                .build();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        allowanceLineService.deleteByPayrollId(id);
        rewardLineService.deleteByPayrollId(id);
        payrollRepository.deleteById(id);
    }

    @Override
    public PayrollDto getById(Long id) {
        PayrollEntity entity = existsPayrollEntity(id);
        PayrollDto dto = payrollMapper.toDto(entity);
        EmployeeDto employeeDto = clientHelper.getEmployeeById(entity.getEmployeeId());
        dto.setEmployee(SimpleDto.builder()
                .id(employeeDto.getId())
                .code(employeeDto.getCode())
                .name(employeeDto.getName())
                .build());
        dto.setDepartment(employeeDto.getDepartment());
        dto.setPosition(employeeDto.getPosition());

        List<PayrollAllowanceLineDto> allowanceLineDtos = allowanceLineService.getPayrollAllowanceLineDtosByPayrollId(id);
        List<PayrollRewardLineDto> rewardLineDtos = rewardLineService.getPayrollRewardLineDtosByPayrollId(id);

        dto.setAllowances(allowanceLineDtos);
        dto.setRewards(rewardLineDtos);
        return dto;
    }

    @Override
    public Page<PayrollDto> getAll(PayrollSearchRequest request) {
        Page<PayrollEntity> page = payrollRepository.findAll(request.specification(), request.pageable());

        if (page.isEmpty())
            return new PageImpl<>(List.of(), page.getPageable(), page.getTotalElements());

        Set<Long> employeeIds = new HashSet<>();
        for (var entity : page) {
            if (entity.getEmployeeId() != null) {
                employeeIds.add(entity.getEmployeeId());
            }
        }
        Map<Long, EmployeeDto> employeeMap = buildEmployeeMap(clientHelper.getAllDetailEmployeeByIds(employeeIds));

        List<PayrollDto> dtoList = new ArrayList<>();
        for (var entity : page) {
            PayrollDto dto = payrollMapper.toDto(entity);
            EmployeeDto employeeDto = employeeMap.get(entity.getEmployeeId());
            dto.setEmployee(SimpleDto.builder()
                    .id(employeeDto.getId())
                    .code(employeeDto.getCode())
                    .name(employeeDto.getName())
                    .build());
            dto.setDepartment(employeeDto.getDepartment());
            dto.setPosition(employeeDto.getPosition());
            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
    }

    private Map<Long, EmployeeDto> buildEmployeeMap(List<EmployeeDto> employeeByIds) {
        return employeeByIds.stream().collect(Collectors.toMap(EmployeeDto::getId, Function.identity()));
    }

    private PayrollEntity existsPayrollEntity(Long id) {
        if (id == null)
            throw new BusinessException("404", translator.toLocale("error.id.not.null"));
        return payrollRepository.findById(id).orElseThrow(
                () -> new BusinessException("404", "Payroll not found with id: " + id));
    }


}
