package com.example.providerapiemulator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    /** Если не null - сотрудник провайдера. */
    @Column(name = "provider")
    private String providerId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "`FailedLoginAttempts`", nullable = false)
    private Integer failedLoginAttempts = 0;

    @Column(name = "`LockoutEnd`")
    private OffsetDateTime lockoutEnd;

    @Column(name = "`MustChangePassword`", nullable = false)
    private Boolean mustChangePassword = false;
}
