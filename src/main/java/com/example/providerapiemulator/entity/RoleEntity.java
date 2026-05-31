package com.example.providerapiemulator.entity;

import com.example.providerapiemulator.model.RoleName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class RoleEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private RoleName name;
}
