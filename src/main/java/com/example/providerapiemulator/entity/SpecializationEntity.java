package com.example.providerapiemulator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "specializations")
@Getter
@Setter
public class SpecializationEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "provid", nullable = false)
    private UUID provId;
}
