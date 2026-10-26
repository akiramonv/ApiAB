package com.example.providerapiemulator.repository;

import com.example.providerapiemulator.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    Optional<RoleEntity> findByNameIgnoreCase(String name);
}
