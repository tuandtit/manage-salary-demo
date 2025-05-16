package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.GroupRewardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRewardRepository extends JpaRepository<GroupRewardEntity, Long>, JpaSpecificationExecutor<GroupRewardEntity> {
    boolean existsByCode(String code);
}
