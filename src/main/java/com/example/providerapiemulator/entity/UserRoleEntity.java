package com.example.providerapiemulator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "userrole")
@Getter
@Setter
public class UserRoleEntity extends BaseEntity {
    @Column(name = "userid", nullable = false)
    private UUID userId;

    @Column(name = "roleid", nullable = false)
    private UUID roleId;
}
