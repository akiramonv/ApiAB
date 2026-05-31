package com.example.providerapiemulator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class AccountEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "ownername", nullable = false)
    private String ownerName;

    @Column(name = "provid", nullable = false)
    private UUID provId;
}
