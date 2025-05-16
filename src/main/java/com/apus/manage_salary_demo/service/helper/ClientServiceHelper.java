package com.apus.manage_salary_demo.service.helper;

import com.apus.manage_salary_demo.client.dto.BaseResponse;
import com.apus.manage_salary_demo.client.product.UomClient;
import com.apus.manage_salary_demo.client.product.dto.UomDto;
import com.apus.manage_salary_demo.client.resources.CurrencyClient;
import com.apus.manage_salary_demo.client.resources.DepartmentClient;
import com.apus.manage_salary_demo.client.resources.EmployeeClient;
import com.apus.manage_salary_demo.client.resources.PositionClient;
import com.apus.manage_salary_demo.client.resources.dto.CurrencyDto;
import com.apus.manage_salary_demo.client.resources.dto.TargetDto;
import com.apus.manage_salary_demo.common.utils.ConvertUtils;
import com.apus.manage_salary_demo.dto.ApplicableTargetDto;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientServiceHelper {

    UomClient uomClient;
    CurrencyClient currencyClient;
    DepartmentClient departmentClient;
    PositionClient positionClient;
    EmployeeClient employeeClient;

    public UomDto getUomById(Long id) {
        return uomClient.getUomById(id).getData();
    }

    public CurrencyDto getCurrencyById(Long id) {
        return currencyClient.getCurrencyById(id).getData();
    }

    public Map<Long, CurrencyDto> buildCurrencyMap(Set<Long> currencyIds) {
        if (currencyIds == null || currencyIds.isEmpty()) return Collections.emptyMap();

        List<CurrencyDto> content = currencyClient
                .getListCurrency(ConvertUtils.joinLongSet(currencyIds))
                .getData().getContent();

        return content.stream()
                .collect(Collectors.toMap(CurrencyDto::getId, Function.identity()));
    }

    public Map<Long, UomDto> buildUomMap(Set<Long> uomIds) {
        if (uomIds == null || uomIds.isEmpty()) return Collections.emptyMap();

        List<UomDto> content = uomClient
                .getListUom(ConvertUtils.joinLongSet(uomIds))
                .getData().getContent();

        return content.stream()
                .collect(Collectors.toMap(UomDto::getId, Function.identity()));
    }

    public List<ApplicableTargetDto> getAllDepartmentByIds(String ids) {
        return getTargetDto(departmentClient.getAllDepartmentByIds(ids));
    }

    public List<ApplicableTargetDto> getAllPositionByIds(String ids) {
        return getTargetDto(positionClient.getAllPositionByIds(ids));
    }

    public List<ApplicableTargetDto> getAllEmployeeByIds(String ids) {
        return getTargetDto(employeeClient.getAllEmployeeByIds(ids));
    }

    private List<ApplicableTargetDto> getTargetDto(BaseResponse<PagingResponse<TargetDto>> allTargetByIds) {
        List<TargetDto> content = allTargetByIds.getData().getContent();
        List<ApplicableTargetDto> targetDtoList = new ArrayList<>();
        for (var dto : content) {
            targetDtoList.add(ApplicableTargetDto.builder()
                    .target(dto)
                    .build());
        }
        return targetDtoList;
    }
}
