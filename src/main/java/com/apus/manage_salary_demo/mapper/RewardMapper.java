package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.RewardDto;
import com.apus.manage_salary_demo.entity.RewardEntity;
import com.apus.manage_salary_demo.mapper.util.IncludeTypeMapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = DefaultConfigMapper.class)
public interface RewardMapper extends EntityMapper<RewardDto, RewardEntity>, IncludeTypeMapperUtil {

    @Override
    @Mapping(target = "includeType", expression = "java(mapIncludeTypesToString(dto.getIncludeTypes()))")
    @Mapping(source = "uom.id", target = "uomId")
    @Mapping(source = "currency.id", target = "currencyId")
    RewardEntity toEntity(RewardDto dto);

    @Override
    @Mapping(target = "includeTypes", expression = "java(mapIncludeTypeToList(entity.getIncludeType()))")
    @Mapping(target = "uom.id", source = "uomId")
    @Mapping(target = "currency.id", source = "currencyId")
    RewardDto toDto(RewardEntity entity);

    @Override
    @Mapping(target = "includeType", expression = "java(mapIncludeTypesToString(dto.getIncludeTypes()))")
    @Mapping(source = "uom.id", target = "uomId")
    @Mapping(source = "currency.id", target = "currencyId")
    void update(RewardDto dto, @MappingTarget RewardEntity entity);

    @Override
    @Mapping(target = "includeTypes", expression = "java(mapIncludeTypeToList(entity.getIncludeType()))")
    void updateDto(@MappingTarget RewardDto dto, RewardEntity entity);
}
