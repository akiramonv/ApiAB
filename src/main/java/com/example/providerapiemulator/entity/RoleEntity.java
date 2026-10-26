package com.example.providerapiemulator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** Роли: admin, provider_user, superadmin, cashier, bank_office. Список открытый, поэтому String. */
@Entity
@Table(name = "roles")
@Getter
@Setter
public class RoleEntity extends BaseEntity {

    @Column(name = "name")
    private String name;
}
