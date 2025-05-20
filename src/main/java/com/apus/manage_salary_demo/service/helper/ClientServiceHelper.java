package com.apus.manage_salary_demo.service.helper;

import com.apus.manage_salary_demo.client.dto.BaseResponse;
import com.apus.manage_salary_demo.client.product.UomClient;
import com.apus.manage_salary_demo.client.product.dto.UomDto;
import com.apus.manage_salary_demo.client.resources.CurrencyClient;
import com.apus.manage_salary_demo.client.resources.DepartmentClient;
import com.apus.manage_salary_demo.client.resources.EmployeeClient;
import com.apus.manage_salary_demo.client.resources.PositionClient;
import com.apus.manage_salary_demo.client.resources.dto.CurrencyDto;
import com.apus.manage_salary_demo.client.resources.dto.EmployeeDto;
import com.apus.manage_salary_demo.common.utils.ConvertUtils;
import com.apus.manage_salary_demo.dto.ApplicableTargetDto;
import com.apus.manage_salary_demo.dto.SimpleDto;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "uomDtoCache", key = "#id")
    public UomDto getUomById(Long id) {
        return uomClient.getUomById(id).getData();
    }

    @Cacheable(value = "currencyDtoCache", key = "#id")
    public CurrencyDto getCurrencyById(Long id) {
        return currencyClient.getCurrencyById(id).getData();
    }

    @Cacheable(value = "currencyMapCache", key = "T(com.apus.manage_salary_demo.common.utils.ConvertUtils).joinLongSet(#currencyIds)")
    public Map<Long, CurrencyDto> buildCurrencyMap(Set<Long> currencyIds) {
        if (currencyIds == null || currencyIds.isEmpty()) return Collections.emptyMap();

        List<CurrencyDto> content = currencyClient
                .getListCurrency(ConvertUtils.joinLongSet(currencyIds))
                .getData().getContent();

        return content.stream()
                .collect(Collectors.toMap(CurrencyDto::getId, Function.identity()));
    }

    @Cacheable(value = "uomMapCache", key = "T(com.apus.manage_salary_demo.common.utils.ConvertUtils).joinLongSet(#uomIds)")
    public Map<Long, UomDto> buildUomMap(Set<Long> uomIds) {
        if (uomIds == null || uomIds.isEmpty()) return Collections.emptyMap();

        List<UomDto> content = uomClient
                .getListUom(ConvertUtils.joinLongSet(uomIds))
                .getData().getContent();

        return content.stream()
                .collect(Collectors.toMap(UomDto::getId, Function.identity()));
    }


    @Cacheable(value = "departmentDtoListCache", key = "T(com.apus.manage_salary_demo.common.utils.ConvertUtils).joinLongSet(#ids)")
    public List<ApplicableTargetDto> getAllDepartmentByIds(String ids) {
        return getTargetDto(departmentClient.getAllDepartmentByIds(ids));
    }

    @Cacheable(value = "positionDtoListCache", key = "T(com.apus.manage_salary_demo.common.utils.ConvertUtils).joinLongSet(#ids)")
    public List<ApplicableTargetDto> getAllPositionByIds(String ids) {
        return getTargetDto(positionClient.getAllPositionByIds(ids));
    }

    @Cacheable(value = "employeeDtoListCache", key = "T(com.apus.manage_salary_demo.common.utils.ConvertUtils).joinLongSet(#ids)")
    public List<ApplicableTargetDto> getAllEmployeeByIds(String ids) {
        return getEmployeeDto(employeeClient.getAllEmployeeByIds(ids));
    }

    public List<EmployeeDto> getAllDetailEmployeeByIds(String ids) {
        return employeeClient.getAllEmployeeByIds(ids).getData().getContent();
    }

    public EmployeeDto getEmployeeById(Long id) {
        return employeeClient.getEmployeeById(id).getData();
    }

    private List<ApplicableTargetDto> getTargetDto(BaseResponse<PagingResponse<SimpleDto>> allTargetByIds) {
        List<SimpleDto> content = allTargetByIds.getData().getContent();
        List<ApplicableTargetDto> targetDtoList = new ArrayList<>();
        for (var dto : content) {
            targetDtoList.add(ApplicableTargetDto.builder()
                    .target(dto)
                    .build());
        }
        return targetDtoList;
    }

    private List<ApplicableTargetDto> getEmployeeDto(BaseResponse<PagingResponse<EmployeeDto>> allTargetByIds) {
        List<EmployeeDto> content = allTargetByIds.getData().getContent();
        List<ApplicableTargetDto> targetDtoList = new ArrayList<>();
        for (var dto : content) {
            targetDtoList.add(ApplicableTargetDto.builder()
                    .target(SimpleDto.builder()
                            .id(dto.getId())
                            .code(dto.getCode())
                            .name(dto.getName())
                            .build())
                    .build());
        }
        return targetDtoList;
    }
}
