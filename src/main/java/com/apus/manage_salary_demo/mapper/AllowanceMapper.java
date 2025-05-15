package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.AllowanceDto;
import com.apus.manage_salary_demo.entity.AllowanceEntity;
import com.apus.manage_salary_demo.mapper.util.IncludeTypeMapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = DefaultConfigMapper.class)
public interface AllowanceMapper extends EntityMapper<AllowanceDto, AllowanceEntity>, IncludeTypeMapperUtil {

    @Override
    @Mapping(target = "groupAllowanceId", ignore = true)
    @Mapping(target = "includeType", expression = "java(mapIncludeTypesToString(dto.getIncludeTypes()))")
    @Mapping(source = "uom.id", target = "uomId")
    @Mapping(source = "currency.id", target = "currencyId")
    AllowanceEntity toEntity(AllowanceDto dto);

    @Override
    @Mapping(target = "includeTypes", expression = "java(mapIncludeTypeToList(entity.getIncludeType()))")
    @Mapping(target = "uom.id", source = "uomId")
    @Mapping(target = "currency.id", source = "currencyId")
    AllowanceDto toDto(AllowanceEntity entity);

    @Override
    @Mapping(target = "groupAllowanceId", ignore = true)
    @Mapping(target = "includeType", expression = "java(mapIncludeTypesToString(dto.getIncludeTypes()))")
    @Mapping(source = "uom.id", target = "uomId")
    @Mapping(source = "currency.id", target = "currencyId")
    void update(AllowanceDto dto, @MappingTarget AllowanceEntity entity);

    @Override
    @Mapping(target = "groupAllowance", ignore = true)
    @Mapping(target = "includeTypes", expression = "java(mapIncludeTypeToList(entity.getIncludeType()))")
    void updateDto(@MappingTarget AllowanceDto dto, AllowanceEntity entity);
}
