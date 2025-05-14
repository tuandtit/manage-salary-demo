package com.apus.manage_salary_demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_reward")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupReward extends AbstractAuditingEntity<Long> {

    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(length = 255, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private GroupReward parent;

    @Column(columnDefinition = "text")
    private String description;
}
