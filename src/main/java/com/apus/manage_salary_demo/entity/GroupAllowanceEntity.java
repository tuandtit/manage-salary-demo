package com.apus.manage_salary_demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "group_allowance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupAllowanceEntity extends AbstractAuditingEntity<Long> {

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private GroupAllowanceEntity parent;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "groupAllowanceEntity")
    private List<AllowanceEntity> allowanceEntities;
}
