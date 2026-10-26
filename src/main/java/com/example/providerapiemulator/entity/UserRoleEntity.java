package com.example.providerapiemulator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** Связка users <-> roles. Колонка "user" - зарезервированное слово, поэтому в кавычках. */
@Entity
@Table(name = "userroles")
@Getter
@Setter
public class UserRoleEntity extends BaseEntity {

    @Column(name = "`user`")
    private String userId;

    @Column(name = "role")
    private String roleId;
}
