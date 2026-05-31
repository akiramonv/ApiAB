package com.example.providerapiemulator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "providerservice")
@Getter
@Setter
public class ProviderServiceEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "categoryid", nullable = false)
    private UUID categoryId;

    @Column(name = "provid", nullable = false)
    private UUID provId;

    @Column(name = "accountid")
    private UUID accountId;

    @Column(name = "commid")
    private UUID commId;
}
