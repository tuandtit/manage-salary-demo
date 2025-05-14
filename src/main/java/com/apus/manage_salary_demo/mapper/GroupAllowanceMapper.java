package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.GroupAllowanceDto;
import com.apus.manage_salary_demo.entity.GroupAllowanceEntity;
import org.mapstruct.*;

@Mapper(
        config = DefaultConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GroupAllowanceMapper extends EntityMapper<GroupAllowanceDto, GroupAllowanceEntity> {

    @Mapping(source = "parent", target = "parent")
    @Override
    GroupAllowanceDto toDto(GroupAllowanceEntity entity);

    @Override
    @Mapping(target = "parent", ignore = true)
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void update(GroupAllowanceDto dto, @MappingTarget GroupAllowanceEntity entity);

    @Mapping(target = "parent", ignore = true)
    @Override
    GroupAllowanceEntity toEntity(GroupAllowanceDto dto);

    // ánh xạ thủ công cho ParentDto từ GroupAllowance
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    GroupAllowanceDto.ParentDto toParentDto(GroupAllowanceEntity parent);
}
