package com.example.providerapiemulator.entity;

import com.example.providerapiemulator.model.CommissionLevelName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "commissionlevel")
@Getter
@Setter
public class CommissionLevel extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private CommissionLevelName name;
}
