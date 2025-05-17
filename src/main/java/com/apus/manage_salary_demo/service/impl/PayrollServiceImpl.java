package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.config.Translator;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.PayrollDto;
import com.apus.manage_salary_demo.dto.request.PayrollRequest;
import com.apus.manage_salary_demo.dto.request.search.PayrollSearchRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //Translator
    Translator translator;

    @Override
    @Transactional
    public BaseDto create(PayrollRequest dto) {
        PayrollEntity entity = payrollMapper.toEntity(dto);
        entity.setTotalAllowanceAmount(allowanceLineService.getTotalAllowanceAmount(dto.getAllowances()));
        PayrollEntity saved = payrollRepository.save(entity);

        allowanceLineService.saveLines(saved.getId(), dto.getAllowances());
        rewardLineService.saveLines(saved.getId(), dto.getRewards());

        return BaseDto.builder()
                .id(saved.getId())
                .build();
    }

    @Override
    @Transactional
    public BaseDto update(PayrollDto dto) {
        return null;
    }

    @Override
    public void delete(Long id) {
        payrollRepository.deleteById(id);
    }

    @Override
    public PayrollDto getById(Long id) {
        return null;
    }

    @Override
    public Page<PayrollDto> getAll(PayrollSearchRequest request) {
        return null;
    }

}
