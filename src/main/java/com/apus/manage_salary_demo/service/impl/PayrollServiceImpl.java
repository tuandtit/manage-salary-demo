package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.config.Translator;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.PayrollDto;
import com.apus.manage_salary_demo.dto.request.search.PayrollSearchRequest;
import com.apus.manage_salary_demo.mapper.PayrollMapper;
import com.apus.manage_salary_demo.repository.PayrollRepository;
import com.apus.manage_salary_demo.service.PayrollService;
import com.apus.manage_salary_demo.service.helper.ClientServiceHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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

    //Translator
    Translator translator;

    @Override
    public BaseDto create(PayrollDto dto) {
        return null;
    }

    @Override
    public BaseDto update(PayrollDto dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

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
