package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.AllowanceDto;
import com.apus.manage_salary_demo.entity.Allowance;
import com.apus.manage_salary_demo.mapper.util.IncludeTypeMapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AllowanceMapper extends EntityMapper<AllowanceDto, Allowance>, IncludeTypeMapperUtil {

    @Override
    @Mapping(target = "groupAllowance", ignore = true)
    @Mapping(target = "includeType", expression = "java(mapIncludeTypesToString(dto.getIncludeTypes()))")
    @Mapping(source = "uom.id", target = "uomId")
    @Mapping(source = "currency.id", target = "currencyId")
    Allowance toEntity(AllowanceDto dto);

    @Override
    @Mapping(target = "includeTypes", expression = "java(mapIncludeTypeToList(entity.getIncludeType()))")
    @Mapping(target = "uom.id", source = "uomId")
    @Mapping(target = "currency.id", source = "currencyId")
    AllowanceDto toDto(Allowance entity);

    @Override
    @Mapping(target = "groupAllowance", ignore = true)
    @Mapping(target = "includeType", expression = "java(mapIncludeTypesToString(dto.getIncludeTypes()))")
    @Mapping(source = "uom.id", target = "uomId")
    @Mapping(source = "currency.id", target = "currencyId")
    void update(AllowanceDto dto, @MappingTarget Allowance entity);

    @Override
    @Mapping(target = "groupAllowance", ignore = true)
    @Mapping(target = "includeTypes", expression = "java(mapIncludeTypeToList(entity.getIncludeType()))")
    void updateDto(@MappingTarget AllowanceDto dto, Allowance entity);
}
