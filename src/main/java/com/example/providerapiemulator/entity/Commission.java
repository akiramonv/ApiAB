package com.example.providerapiemulator.entity;

import com.example.providerapiemulator.model.CommissionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "commission")
@Getter
@Setter
public class Commission extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "commissiontype", nullable = false)
    private CommissionType commissionType;

    @Column(name = "commissionvalue", nullable = false, precision = 12, scale = 2)
    private BigDecimal commissionValue;
}
