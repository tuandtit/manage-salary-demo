package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.GroupAllowanceDto;
import com.apus.manage_salary_demo.entity.GroupAllowance;
import org.mapstruct.*;

@Mapper(
        config = DefaultConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GroupAllowanceMapper extends EntityMapper<GroupAllowanceDto, GroupAllowance> {

    @Mapping(source = "parent", target = "parent")
    @Override
    GroupAllowanceDto toDto(GroupAllowance entity);

    @Override
    @Mapping(target = "parent", ignore = true)
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void update(GroupAllowanceDto dto, @MappingTarget GroupAllowance entity);

    @Mapping(target = "parent", ignore = true)
    @Override
    GroupAllowance toEntity(GroupAllowanceDto dto);

    // ánh xạ thủ công cho ParentDto từ GroupAllowance
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    GroupAllowanceDto.ParentDto toParentDto(GroupAllowance parent);
}
