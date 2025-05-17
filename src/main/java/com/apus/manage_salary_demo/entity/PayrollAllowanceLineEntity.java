package com.apus.manage_salary_demo.entity;

import com.apus.manage_salary_demo.common.enums.AmountItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payroll_allowance_line")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollAllowanceLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long payrollId;

    private Long groupAllowanceId;

    private Long allowanceId;

    @Enumerated(EnumType.STRING)
    private AmountItem amountItem;

    private BigDecimal amount = BigDecimal.ZERO;

    private BigDecimal taxableAmount = BigDecimal.ZERO;

    private BigDecimal insuranceAmount = BigDecimal.ZERO;
}
