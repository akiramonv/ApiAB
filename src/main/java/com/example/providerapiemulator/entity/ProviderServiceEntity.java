package com.example.providerapiemulator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "providerservice")
@Getter
@Setter
public class ProviderServiceEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "categoryid", nullable = false)
    private UUID categoryId;

    @Column(name = "provid", nullable = false)
    private UUID provId;

    @Column(name = "accountid", nullable = false)
    private UUID accountId;

    @Column(name = "commid")
    private UUID commId;
}
