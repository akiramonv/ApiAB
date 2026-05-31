package com.example.providerapiemulator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "userspecialization")
@Getter
@Setter
public class UserSpecializationEntity extends BaseEntity {
    @Column(name = "userid", nullable = false)
    private UUID userId;

    @Column(name = "specialid", nullable = false)
    private UUID specialId;
}
