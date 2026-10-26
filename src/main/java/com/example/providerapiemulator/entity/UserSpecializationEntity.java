package com.example.providerapiemulator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** Связка users <-> specializations. */
@Entity
@Table(name = "userspecializations")
@Getter
@Setter
public class UserSpecializationEntity extends BaseEntity {

    @Column(name = "specialization")
    private String specializationId;

    @Column(name = "`user`")
    private String userId;
}
