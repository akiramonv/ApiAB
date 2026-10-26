package com.example.providerapiemulator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** Связка provider_services <-> specializations. */
@Entity
@Table(name = "provider_services_specializations")
@Getter
@Setter
public class ServiceSpecializationEntity extends BaseEntity {

    @Column(name = "service")
    private String serviceId;

    @Column(name = "specialization")
    private String specializationId;
}
