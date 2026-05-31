package com.example.providerapiemulator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "servicespecializations")
@Getter
@Setter
public class ServiceSpecializationEntity extends BaseEntity {
    @Column(name = "serviceid", nullable = false)
    private UUID serviceId;

    @Column(name = "specialid", nullable = false)
    private UUID specialId;
}
