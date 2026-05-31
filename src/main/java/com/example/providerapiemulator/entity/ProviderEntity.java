package com.example.providerapiemulator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "providers")
@Getter
@Setter
public class ProviderEntity extends BaseEntity {
    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "shortname")
    private String shortName;

    @Column(name = "commlvl", nullable = false)
    private UUID commLvl;

    @Column(name = "commid")
    private UUID commId;
}
