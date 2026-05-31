package com.example.providerapiemulator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "categoryservice")
@Getter
@Setter
public class CategoryServiceEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "prntcategory")
    private UUID prntCategory;

    @Column(name = "provid")
    private UUID provId;
}
