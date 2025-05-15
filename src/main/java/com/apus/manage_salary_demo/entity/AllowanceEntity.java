package com.apus.manage_salary_demo.entity;

import com.apus.manage_salary_demo.common.enums.AllowanceRewardType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "allowance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllowanceEntity extends AbstractAuditingEntity<Long> {

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "include_type", nullable = false, length = 50)
    private String includeType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AllowanceRewardType type = AllowanceRewardType.CASH;

    @Column(name = "uom_id")
    private Long uomId;

    @Column(name = "currency_id")
    private Long currencyId;

    @Column(name = "group_allowance_id")
    private Long groupAllowanceId;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
