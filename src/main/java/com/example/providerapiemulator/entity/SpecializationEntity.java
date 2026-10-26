package com.example.providerapiemulator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "specializations")
@Getter
@Setter
public class SpecializationEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "`Deleted`")
    private Boolean deleted;

    @Column(name = "`RelatedProvider`")
    private String providerId;
}
